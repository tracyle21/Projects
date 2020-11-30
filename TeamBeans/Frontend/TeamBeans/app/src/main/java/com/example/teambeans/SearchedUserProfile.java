package com.example.teambeans;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
 * SearchedUserProfile process the data and display the data for searched_user_profile.xml. It is for user with the role of STUDENT.
 *
 * @author Renee
 */
public class SearchedUserProfile extends Fragment {
    private String user;
    private Button backBtn;
    private Button addFeedbackBtn;
    private Button viewFeedbackBtn;
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
    private ArrayList<String> searchResults = new ArrayList<String>();

    /**
     * Lifetime catch of screen, sets up the view of the screen and initializes data.
     *
     * @param inflater           Layout inflater of the screen.
     * @param container          App view frame.
     * @param savedInstanceState Preliminary data for the screen.
     * @return Created view of the screen.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.search_user_profile, container, false);

        final Context context = getContext();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = bundle.getString("Username", "");
            searchResults = bundle.getStringArrayList("names");
        }
        if (user == null) {
            AddFeedback AddFeedback = new AddFeedback();
            user = AddFeedback.searchedUsername;
            ViewFeedback ViewFeedback = new ViewFeedback();
            if (ViewFeedback.searchedUsername != null) {
                user = ViewFeedback.searchedUsername;
            }
        }
        this.getActivity().setTitle(user + "'s Profile");
        addFeedbackBtn = view.findViewById(R.id.addFeedbackBtn);
        backBtn = view.findViewById(R.id.toSearchBtn);
        profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        viewFeedbackBtn = view.findViewById(R.id.viewFeedbackBtn);
        // TODO Add a default profile picture
        profilePicture.setImageResource(R.drawable.default_user_avatar);

        //set the value to text
        TextView usernameText = (TextView) view.findViewById(R.id.username);

        usernameText.setText(user);

        takeDescription = view.findViewById(R.id.description);
        takeClassTaken = view.findViewById(R.id.takeClassTaken);
        takeCurrentClass = view.findViewById(R.id.takeCurrentClass);
        takeLanguage = view.findViewById(R.id.takeProgrammingLanguage);

        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + user + "/description";
        connection(context, url, "description");
        url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + user + "/currentCourses";
        connection(context, url, "currentClass");
        url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + user + "/oldCourses";
        connection(context, url, "classTaken");
        url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + user + "/languages";
        connection(context, url, "language");
        addFeedbackBtnOnClickListener(context);
        addBackBtnOnClickListener(context, searchResults);
        viewFeedbackBtnfOnClickListener(context);
        return view;
    }

    /**
     * This method changes the fragment by using Fragment Transaction.(From SearchedUserProfile -> AddFeedback)
     *
     * @param context The context of the view
     */
    private void addFeedbackBtnOnClickListener(final Context context) {
        addFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Login login = new Login();
                if (!login.user.equals(user)) {
                    final Bundle bundle = new Bundle();
                    bundle.putString("searchedRole", "STUDENT");
                    bundle.putString("searchedUsername", user);
                    AddFeedback AddFeedback = new AddFeedback();
                    //passing the text
                    AddFeedback.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContent, AddFeedback);
                    fragmentTransaction.commit();
                } else {
                    Toast toast = Toast.makeText(context, "You cannot give yourself a rating!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private void viewFeedbackBtnfOnClickListener(final Context context) {
        viewFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                bundle.putString("searchedRole", "STUDENT");
                bundle.putString("searchedUsername", user);
                ViewFeedback ViewFeedback = new ViewFeedback();
                //passing the text
                ViewFeedback.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent, ViewFeedback);
                fragmentTransaction.commit();
            }
        });
    }

    /**
     * This method changes the fragment by using Fragment Transaction.(From SearchedUserProfile -> Dashboard)
     *
     * @param context The context of the view
     */
    private void addBackBtnOnClickListener(final Context context, final ArrayList<String> results) {
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                bundle.putStringArrayList("names", results);
                Dashboard dashboard = new Dashboard();
                //passing the text
                dashboard.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent, dashboard);
                fragmentTransaction.commit();
            }
        });
    }

    /**
     * This method handles JSONRequest for GET request (language, description, class taken, current class). Then, it sets the string response to the textview respectively.
     *
     * @param context The context of the view
     * @param url     The url for the JSON request
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
                            String responseC = "";
                            if (connect.equals("currentClass") || connect.equals("classTaken")) {
                                responseC = response.substring(12, response.length() - 2);
                            }
                            if (connect.equals("description")) {
                                descriptionOld = "";
                                descriptionOld = descriptionOld.concat(response);
                                takeDescription.setText(descriptionOld);
                            } else if (connect.equals("language")) {
                                programmingLanguageOld = "";
                                programmingLanguageOld = programmingLanguageOld.concat(response.substring(14, response.length() - 2)).replaceAll("\"", "").replaceAll(",", ", ");
                                editUserProfile.findPosition(programmingLanguageOld, position);
                                programmingLanguageOld = editUserProfile.addNewLine(programmingLanguageOld, position);
                                takeLanguage.setText(programmingLanguageOld);
                            } else if (connect.equals("currentClass")) {
                                currentClassOld = "";
                                currentClassOld = currentClassOld.concat(responseC).replaceAll("\"", "").replaceAll(",", ", ");
                                editUserProfile.findPosition(currentClassOld, position);
                                currentClassOld = editUserProfile.addNewLine(currentClassOld, position);
                                takeCurrentClass.setText(currentClassOld);
                            } else if (connect.equals("classTaken")) {
                                classTakenOld = "";
                                classTakenOld = classTakenOld.concat(responseC).replaceAll("\"", "").replaceAll(",", ", ");
                                editUserProfile.findPosition(classTakenOld, position);
                                classTakenOld = editUserProfile.addNewLine(classTakenOld, position);
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
}
