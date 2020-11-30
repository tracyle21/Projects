package com.example.teambeans;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
 * UserProfileProfessor process the data and display the data for user_profile_professor.xml. It is for user with the role of PROFESSOR.
 *
 * @author Renee
 */
public class UserProfileProfessor extends Fragment {
    private Button editProfileButtonProf;
    private TextView takeDescriptionProf;
    private TextView takeClassTeaching;
    ImageView profilePicture;
    public static String descriptionProfOld = "";
    public static String ClassTeachingOld = "";
    private ArrayList<Integer> position = new ArrayList<Integer>();
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
    private RatingBar workqualityRatingBar;
    private RatingBar attitudeRatingBar;
    private TextView reviewed;
    private TextView numOverall;
    private TextView numCommunication;
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
        final View view = inflater.inflate(R.layout.user_profile_professor, container, false);
        this.getActivity().setTitle("My Profile");
        final Context context = getContext();

        editProfileButtonProf = view.findViewById(R.id.editProfileBtnProf);
        showRatingBtn = view.findViewById(R.id.showRatingBtn);
        hideRatingBtn = view.findViewById(R.id.hideRatingBtn);

        Login login = new Login();

        //set the value to text
        TextView usernameText = (TextView) view.findViewById(R.id.usernameProf);

        usernameText.setText(login.user);

        takeDescriptionProf = view.findViewById(R.id.descriptionProf);
        takeClassTeaching = view.findViewById(R.id.takeClassTeaching);

        overallRatingBar = view.findViewById(R.id.overall_ratingBar);
        communicationRatingBar = view.findViewById(R.id.communication_ratingBar);
        workqualityRatingBar = view.findViewById(R.id.workquality_ratingBar);
        attitudeRatingBar = view.findViewById(R.id.attitude_ratingBar);
        reviewed = view.findViewById(R.id.rating);
        numOverall = view.findViewById(R.id.numOverall);
        numCommunication = view.findViewById(R.id.numCommunication);
        numWorkquality = view.findViewById(R.id.numWorkQuality);
        numAttitude = view.findViewById(R.id.numAttitude);

        getRating(context);
        layout = view.findViewById(R.id.rating_layout);
        hideRatingBtn.setVisibility(view.INVISIBLE);
        showRatingBtn.setVisibility(view.VISIBLE);

        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/description";
        connection(context, url, "description");
        url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/coursesTeaching";
        connection(context, url, "coursesTeaching");

        showRatingBtnOnClickListener(view);
        hideRatingBtnOnClickListener(view);
        editProfileButtonProfOnClickListener(context);

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
     * This method changes the fragment by using Fragment Transaction.(From UserProfileProfessor -> EditUserProfileProfessor)
     * @param context The context of the view
     */
    private void editProfileButtonProfOnClickListener(final Context context) {
        editProfileButtonProf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Display login screen
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent, new EditUserProfileProfessor());
                fragmentTransaction.commit();
            }
        });
    }

    /**
     * This method handles JSONRequest for GET request (language, description, class teaching). Then, it sets the string response to the textview respectively.
     * @param context The context of the view
     * @param url The url for the JSON request
     * @param connect Contains the name for the request(language, description, class teaching)
     */
    private void connection(Context context, String url, final String connect) {
        final EditUserProfile editUserProfile = new EditUserProfile();
        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        final StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("ProfHereResponse" + response);
                            String responseC ="";
                            if(connect.equals("coursesTeaching")){
                                responseC = response.substring(12, response.length() - 2);
                            }
                            if(connect.equals("description")) {
                                descriptionProfOld = "";
                                descriptionProfOld = descriptionProfOld.concat(response);
                                takeDescriptionProf.setText(descriptionProfOld);
                            }else if(connect.equals("coursesTeaching")) {
                                ClassTeachingOld = "";
                                ClassTeachingOld = ClassTeachingOld.concat(responseC).replaceAll("\"", "").replaceAll(",", ", ");
                                editUserProfile.findPosition(ClassTeachingOld,position);
                                ClassTeachingOld=editUserProfile.addNewLine(ClassTeachingOld,position);
                                // System.out.println("NULL?" + ClassAssistingOld);
                                takeClassTeaching.setText(ClassTeachingOld);

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
        workqualityRatingBar.setIsIndicator(true);
        attitudeRatingBar.setIsIndicator(true);

        overallRatingBar.setRating(overallRating);
        communicationRatingBar.setRating(communicationRating);
        workqualityRatingBar.setRating(workqualityRating);
        attitudeRatingBar.setRating(attitudeRating);
        reviewed.setText("Total number of reviews: " + totalReviewed);

        numOverall.setText(String.valueOf(overallRating));
        numCommunication.setText(String.valueOf(communicationRating));
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
}
