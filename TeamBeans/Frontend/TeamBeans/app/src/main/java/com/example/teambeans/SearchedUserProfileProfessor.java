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
 * SearchedUserProfileProfessor process the data and display the data for searched_user_profile_professor.xml. It is for user with the role of PROFESSOR.
 *
 * @author Renee
 */
public class SearchedUserProfileProfessor extends Fragment {
    private Button addFeedbackBtn;
    private Button backBtn;
    private Button viewFeedbackBtn;
    private TextView takeDescriptionProf;
    private TextView takeClassTeaching;
    ImageView profilePicture;
    public static String descriptionProfOld = "";
    public static String ClassTeachingOld = "";
    private ArrayList<Integer> position = new ArrayList<Integer>();
    private String user;
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
        final View view = inflater.inflate(R.layout.searched_user_profile_professor, container, false);
        this.getActivity().setTitle("My Profile");
        final Context context = getContext();

//        editProfileButtonProf = view.findViewById(R.id.editProfileBtnProf);

        Login login = new Login();
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
        //set the value to text
        TextView usernameText = (TextView) view.findViewById(R.id.usernameProf);
        addFeedbackBtn = view.findViewById(R.id.addFeedbackBtn);
        backBtn = view.findViewById(R.id.toSearchBtn);
        viewFeedbackBtn = view.findViewById(R.id.viewFeedbackBtn);

        usernameText.setText(user);

        takeDescriptionProf = view.findViewById(R.id.descriptionProf);
        takeClassTeaching = view.findViewById(R.id.takeClassTeaching);

        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + user + "/description";
        connection(context, url, "description");
        url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + user + "/coursesTeaching";
        connection(context, url, "coursesTeaching");
        addFeedbackBtnOnClickListener(context);
        addBackBtnOnClickListener(context, searchResults);
        viewFeedbackBtnfOnClickListener(context);

        return view;
    }

    private void addFeedbackBtnOnClickListener(final Context context) {
        addFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Login login = new Login();
                if (!login.user.equals(user)) {
                    final Bundle bundle = new Bundle();
                    bundle.putString("searchedRole", "PROFESSOR");
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

    private void viewFeedbackBtnfOnClickListener(final Context context) {
        viewFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                bundle.putString("searchedRole", "PROFESSOR");
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
     * This method handles JSONRequest for GET request (language, description, class teaching). Then, it sets the string response to the textview respectively.
     *
     * @param context The context of the view
     * @param url     The url for the JSON request
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
                            String responseC = "";
                            if (connect.equals("coursesTeaching")) {
                                responseC = response.substring(12, response.length() - 2);
                            }
                            if (connect.equals("description")) {
                                descriptionProfOld = "";
                                descriptionProfOld = descriptionProfOld.concat(response);
                                takeDescriptionProf.setText(descriptionProfOld);
                            } else if (connect.equals("coursesTeaching")) {
                                ClassTeachingOld = "";
                                ClassTeachingOld = ClassTeachingOld.concat(responseC).replaceAll("\"", "").replaceAll(",", ", ");
                                editUserProfile.findPosition(ClassTeachingOld, position);
                                ClassTeachingOld = editUserProfile.addNewLine(ClassTeachingOld, position);
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
}
