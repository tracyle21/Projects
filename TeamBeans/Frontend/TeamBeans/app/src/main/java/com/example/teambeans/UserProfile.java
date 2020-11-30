package com.example.teambeans;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * UserProfile process the data and display the data for user_profile.xml. It is for user with the role of STUDENT.
 *
 * @author Renee
 */
public class UserProfile extends Fragment {
    private Button editProfileButton;
    private TextView takeDescription;
    private TextView takeClassTaken;
    private TextView takeCurrentClass;
    private TextView takeLanguage;
    ImageView profilePicture;
    public static String descriptionOld = "";
    public static String currentClassOld = "";
    public static String classTakenOld = "";
    public static String programmingLanguageOld = "";
    private ArrayList<Integer> position = new ArrayList<Integer>();
    ForMockitoTest ForMockitoTest;
    private ConstraintLayout layout;
    private Button showRatingBtn;
    private Button hideRatingBtn;
    private float overallRating;
    private float communicationRating;
    private float teamworkRating;
    private float workqualityRating;
    private float attitudeRating;
    private int totalReviewed;
    private RatingBar overallRatingBar;
    private RatingBar communicationRatingBar;
    private RatingBar teamworkRatingBar;
    private RatingBar workqualityRatingBar;
    private RatingBar attitudeRatingBar;
    private TextView reviewed;
    private TextView numOverall;
    private TextView numCommunication;
    private TextView numTeamwork;
    private TextView numWorkquality;
    private TextView numAttitude;
    /**
     * Lifetime catch of screen, sets up the view of the screen and initializes data.
     * @param inflater Layout inflater of the screen.
     * @param container App view frame.
     * @param savedInstanceState Preliminary data for the screen.
     * @return Created view of the screen.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_profile, container, false);
        this.getActivity().setTitle("My Profile");
        final Context context = getContext();
        ForMockitoTest = new ForMockitoTest(context);
        Login login = new Login();

        editProfileButton = view.findViewById(R.id.editProfileBtn);
        profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        showRatingBtn = view.findViewById(R.id.showRatingBtn);
        hideRatingBtn = view.findViewById(R.id.hideRatingBtn);

        // TODO Add a default profile picture
        profilePicture.setImageResource(R.drawable.default_user_avatar);

        //set the value to text
        TextView usernameText = (TextView) view.findViewById(R.id.username);

        usernameText.setText(login.user);

        takeDescription = view.findViewById(R.id.description);
        takeClassTaken = view.findViewById(R.id.takeClassTaken);
        takeCurrentClass = view.findViewById(R.id.takeCurrentClass);
        takeLanguage = view.findViewById(R.id.takeProgrammingLanguage);

        overallRatingBar = view.findViewById(R.id.overall_ratingBar);
        communicationRatingBar = view.findViewById(R.id.communication_ratingBar);
        teamworkRatingBar = view.findViewById(R.id.teamwork_ratingBar);
        workqualityRatingBar = view.findViewById(R.id.workquality_ratingBar);
        attitudeRatingBar = view.findViewById(R.id.attitude_ratingBar);
        reviewed = view.findViewById(R.id.rating);
        numOverall = view.findViewById(R.id.numOverall);
        numCommunication = view.findViewById(R.id.numCommunication);
        numTeamwork = view.findViewById(R.id.numTeamwork);
        numWorkquality = view.findViewById(R.id.numWorkQuality);
        numAttitude = view.findViewById(R.id.numAttitude);

        getRating(context);
        layout = view.findViewById(R.id.rating_layout);
        hideRatingBtn.setVisibility(view.INVISIBLE);
        showRatingBtn.setVisibility(view.VISIBLE);

        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/description";
        connection(context, url, "description");
        url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/currentCourses";
        connection(context, url, "currentClass");
        url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/oldCourses";
        connection(context, url, "classTaken");
        url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/languages";
        connection(context, url, "language");

        showRatingBtnOnClickListener(view);
        hideRatingBtnOnClickListener(view);
        editProfileButtonOnClickListener(context);

        return view;
    }
    private void showRatingBtnOnClickListener(final View view) {
        showRatingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layout.setVisibility(view.VISIBLE);
                hideRatingBtn.setVisibility(view.VISIBLE);
                showRatingBtn.setVisibility(view.INVISIBLE);
            }
        });
    }

    private void hideRatingBtnOnClickListener(final View view) {
        hideRatingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layout.setVisibility(view.GONE);
                hideRatingBtn.setVisibility(view.INVISIBLE);
                showRatingBtn.setVisibility(view.VISIBLE);
            }
        });
    }
    /**
     * This method changes the fragment by using Fragment Transaction.(From UserProfile -> EditUserProfile)
     * @param context The context of the view
     */
    private void editProfileButtonOnClickListener(final Context context) {
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Display login screen
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.flContent, new EditUserProfile());
                fragmentTransaction.commit();
            }
        });
    }

    /**
     * This method handles JSONRequest for GET request (language, description, class taken, current class). Then, it sets the string response to the textview respectively.
     * @param context The context of the view
     * @param url The url for the JSON request
     * @param connect Contains the name for the request(language, description, class taken, current class)
     */
    private void connection(Context context, String url, final String connect) {
        final EditUserProfile editUserProfile = new EditUserProfile();
        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        final StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //System.out.println("HereResponse" + response);
                            String responseC ="";
                            if(connect.equals("currentClass") || connect.equals("classTaken")){
                               responseC = response.substring(12, response.length() - 2);
                            }
                            if(connect.equals("description")) {
                                descriptionOld = "";
                                descriptionOld = descriptionOld.concat(response);
                                takeDescription.setText(descriptionOld);
                            }else if(connect.equals("language")) {
                                programmingLanguageOld = "";
                                programmingLanguageOld = programmingLanguageOld.concat(response.substring(14,response.length()-2)).replaceAll("\"", "").replaceAll(",", ", ");
                                editUserProfile.findPosition(programmingLanguageOld, position);
                                programmingLanguageOld=editUserProfile.addNewLine(programmingLanguageOld,position);
                                takeLanguage.setText(programmingLanguageOld);
                            }else if(connect.equals("currentClass")) {
                                currentClassOld = "";
                                currentClassOld = currentClassOld.concat(responseC).replaceAll("\"", "").replaceAll(",", ", ");
                                editUserProfile.findPosition(currentClassOld, position);
                                currentClassOld = editUserProfile.addNewLine(currentClassOld, position);
                                takeCurrentClass.setText(currentClassOld);
                            }else if(connect.equals("classTaken")) {
                                classTakenOld = "";
                                classTakenOld = classTakenOld.concat(responseC).replaceAll("\"", "").replaceAll(",", ", ");
                                editUserProfile.findPosition(classTakenOld,position);
                                classTakenOld=editUserProfile.addNewLine(classTakenOld,position);
                                takeClassTaken.setText(classTakenOld);
                            }
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR " + error.getMessage());
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }

    private void setRatingBar(){
        overallRatingBar.setIsIndicator(true);
        communicationRatingBar.setIsIndicator(true);
        teamworkRatingBar.setIsIndicator(true);
        workqualityRatingBar.setIsIndicator(true);
        attitudeRatingBar.setIsIndicator(true);

        overallRatingBar.setRating(overallRating);
        communicationRatingBar.setRating(communicationRating);
        teamworkRatingBar.setRating(teamworkRating);
        workqualityRatingBar.setRating(workqualityRating);
        attitudeRatingBar.setRating(attitudeRating);
        reviewed.setText("Total number of reviews: " + totalReviewed);

        numOverall.setText(String.valueOf(overallRating));
        numCommunication.setText(String.valueOf(communicationRating));
        numTeamwork.setText(String.valueOf(teamworkRating));
        numWorkquality.setText(String.valueOf(workqualityRating));
        numAttitude.setText(String.valueOf(attitudeRating));
    }

    private void getRating(Context context) {
        Login login = new Login();
        final RequestQueue queue = Volley.newRequestQueue(context);

        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/ratingProfile";
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("ratingProfile");
                            overallRating = Float.parseFloat(jsonObject.getString("overall"));
                            communicationRating = Float.parseFloat(jsonObject.getString("communication"));
                            teamworkRating = Float.parseFloat(jsonObject.getString("teamwork"));
                            workqualityRating = Float.parseFloat(jsonObject.getString("workQuality"));
                            attitudeRating = Float.parseFloat(jsonObject.getString("attitude"));
                            totalReviewed = Integer.parseInt(jsonObject.getString("totalReviewed"));
                            System.out.println("Response get rating " + overallRating + " " + communicationRating + " "  + teamworkRating + " "  + workqualityRating + " "  + attitudeRating + " "  + totalReviewed );
                            // String responseC = response;
                            setRatingBar();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR " + error.getMessage());
                    }
                });
        queue.add(jsonObjectRequest);

    }

    /**
     * This method is written soley for mockito test (GET).
     * @param url the url for JSON request
     *@return the url for JSON request
     */
    public String connectionfForGETTesting(String url){
        ForMockitoTest.connectionfForGETTesting(url);
        return url;
    }


}
