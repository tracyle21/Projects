package com.example.teambeans;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * EditUserProfileTA process the data and display the data for edit_user_profile_ta.xml. It is for user with the role of TA.
 *
 * @author Renee
 */
public class EditUserProfileTA extends Fragment {
    private EditText description;
    final private UserProfileTA userProfileTA = new UserProfileTA();
    final private EditUserProfile EditUserProfile = new EditUserProfile();
    private Button saveProfileBtn;
    private Button addClassAssistBtn;
    private Button deleteClassAssistBtn;
    private TextView takeClassAssist;
    private String listOfClassAssist = "";
    private ArrayList<String> classAssist = new ArrayList<String>();
    private ArrayList<Integer> position = new ArrayList<Integer>();
    private int flag1 = 0;
    private int flag2 = 0;
    final private Login login = new Login();
    ForMockitoTest ForMockitoTest;

    /**
     * Lifetime catch of screen, sets up the view of the screen and initializes data.
     *
     * @param inflater           Layout inflater of the screen.
     * @param container          App view frame.
     * @param savedInstanceState Preliminary data for the screen.
     * @return Created view of the screen.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit_user_profile_ta, container, false);
        final Context context = getContext();
        final Context context1 = getActivity().getApplicationContext();
        this.getActivity().setTitle("Edit Profile");

        ForMockitoTest = new ForMockitoTest(context);
        description = (EditText) view.findViewById(R.id.description2);

        final TextView usernameText = (TextView) view.findViewById(R.id.username);
        usernameText.setText(login.user);

        takeClassAssist = (TextView) view.findViewById(R.id.showclassAssistingInDatabase);
        takeClassAssist.setText(userProfileTA.ClassAssistingOld);

        saveProfileBtn = view.findViewById(R.id.saveProfileBtn);
        addClassAssistBtn = view.findViewById(R.id.addclassAssistingBtn);
        deleteClassAssistBtn = view.findViewById(R.id.deleteclassAssistingBtn);

        if (userProfileTA.descriptionTAOld != null) {
            System.out.println("edit user description from user profile: " + userProfileTA.descriptionTAOld);
            description.setText(userProfileTA.descriptionTAOld, TextView.BufferType.EDITABLE);
        }

        String[] classes = {"COMS 309", "COMS 331", "COMS 342", "COMS 352", "COMS 402", "ENGL 150", "ENGL 250", "ENGL 309", "ENGL 314"};
        final Spinner spinner1 = (Spinner) view.findViewById(R.id.class_assist);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, classes);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        deleteClassAssistBtnOnClickListener(context, context1, spinner1, view);
        addClassAssistBtnOnClickListener(context, context1, spinner1, view);
        saveProfileBtnOnClickListener(context, context1);
        return view;
    }

    /**
     * This method delete a class assisting by sending a post request to spring boot. Mainly, process the data obtained by the spinner.
     *
     * @param context  The context of the view
     * @param context1 The context of the view for toast
     * @param spinner1 The spinner of class assisting
     * @param view     View of the screen
     */
    private void deleteClassAssistBtnOnClickListener(final Context context, final Context context1, final Spinner spinner1, final View view) {
        deleteClassAssistBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView showClassAssist = (TextView) view.findViewById(R.id.showClassAssist);
                if (flag2 == 0) {
                    flag1 = 0;
                    classAssist.removeAll(classAssist);
                    listOfClassAssist = "";
                    showClassAssist.setText(listOfClassAssist);
                    //System.out.println("Should be empty" + listOfProgrammingLanguage);
                }
                String currentClassText = spinner1.getSelectedItem().toString();
                //System.out.println("currentClassText: "+currentClassText );
                if (userProfileTA.ClassAssistingOld.contains(currentClassText)) {
                    if (!classAssist.contains(currentClassText)) {
                        classAssist.add(currentClassText);
                        if (flag2 != 0) {
                            listOfClassAssist = listOfClassAssist.concat(", ");
                            if (flag2 % 4 == 0) {
                                listOfClassAssist = listOfClassAssist.concat("\n");
                            }
                        }
                        listOfClassAssist = listOfClassAssist.concat(currentClassText);
                        // System.out.println("LIST OF CLASS: "+listOfClass );
                        showClassAssist.setText(listOfClassAssist);
                        flag2++;
                        String url = "http://10.24.226.25:8080/deleteAssistingCourse";
                        postRequestTA(context, url, currentClassText, "classAssist");
                    } else {
                        Toast toast = Toast.makeText(context1, "Class have been selected!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(context1, "Class is not in database", Toast.LENGTH_LONG);
                    toast.show();
                }


            }
        });
    }

    /**
     * This method add a class assisting by sending a put request to spring boot.  Mainly, process the data obtained by the spinner.
     *
     * @param context  The context of the view
     * @param context1 The context of the view for toast
     * @param spinner1 The spinner of class assisting
     * @param view     View of the screen
     */
    private void addClassAssistBtnOnClickListener(final Context context, final Context context1, final Spinner spinner1, final View view) {
        addClassAssistBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String currentClassText = spinner1.getSelectedItem().toString();
                TextView showClassAssist = (TextView) view.findViewById(R.id.showClassAssist);
                //System.out.println("currentClassText: "+currentClassText );
                if (flag1 == 0) {
                    flag2 = 0;
                    classAssist.removeAll(classAssist);
                    listOfClassAssist = "";
                    showClassAssist.setText(listOfClassAssist);
                    //System.out.println("Should be empty" + listOfProgrammingLanguage);
                }
                if (!userProfileTA.ClassAssistingOld.contains(currentClassText)) {
                    if (!classAssist.contains(currentClassText)) {
                        classAssist.add(currentClassText);
                        if (flag1 != 0) {
                            listOfClassAssist = listOfClassAssist.concat(", ");
                            if (flag1 % 4 == 0) {
                                listOfClassAssist = listOfClassAssist.concat("\n");
                            }
                        }
                        listOfClassAssist = listOfClassAssist.concat(currentClassText);
                        // System.out.println("LIST OF CLASS: "+listOfClass );

                        showClassAssist.setText(listOfClassAssist);
                        flag1++;
                        String url = "http://10.24.226.25:8080/addAssistingCourse";
                        connectionTA(context, context1, url, currentClassText, Request.Method.PUT, "classAssist", false);
                    } else {
                        Toast toast = Toast.makeText(context1, "Class have been selected!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(context1, "Class have been selected!", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

    }

    /**
     * This method will be accessed when the saveProfileBtn is called. It put add user description to the database by sending a put request to Spring Boot.
     * @param context  The context of the view
     * @param context1 The context of the view for toast
     */
    private void saveProfileBtnOnClickListener(final Context context, final Context context1) {
        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descriptionText = description.getText().toString();
                if (descriptionText != null) {
                    String url = "http://10.24.226.25:8080/addUserDescription";
                    connectionTA(context, context1, url, descriptionText, Request.Method.PUT, "description", true);
                }
            }
        });
    }

    /**
     * This method changes the fragment by using Fragment Transaction. (From EditUserProfileTA -> UserProfileTA)
     * @param context1 The context of the view
     */
    private void changeTransaction(Context context1) {
        Toast toast = Toast.makeText(context1, "Edit Success!!", Toast.LENGTH_LONG);
        toast.show();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, new UserProfileTA());
        fragmentTransaction.commit();
    }

    /**
     * This method handles JSONRequest for PUT request (description, class assisting)
     * @param context The context of the view
     * @param context1 The context of the view for toast
     * @param url The url for the JSON request
     * @param currentText The text that want to be put
     * @param status The type of request
     * @param connect Contains the name for the request(description, class assisting)
     * @param savedButtonClicked true, save button is clicked, false, save button is not clicked
     */
    private void connectionTA(final Context context, final Context context1, String url, String currentText, int status, final String connect, final boolean savedButtonClicked) {
        //System.out.println("TESTING HERE:" + getBeforeSpace("COMS 309")+"|sec|"+ getAfterSpace("COMS 309"));
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", login.user);
            if (connect.equals("description")) {
                obj.put("description", currentText);
            } else if (connect.equals("classAssist")) {
                obj.put("majorName", EditUserProfile.getBeforeSpace(currentText));
                obj.put("number", EditUserProfile.getAfterSpace(currentText));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        final JsonObjectRequest request = new JsonObjectRequest(status, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null) {
                                System.out.println("RESPONSE " + response.toString());
                                if (savedButtonClicked) {
                                    changeTransaction(context1);
                                }
                                if (connect.equals("classAssist")) {
                                    String url2 = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/coursesAssisting";
                                    updateListTA(context, url2, connect);
                                }
                            } else {
                                Toast toast = Toast.makeText(context1, " Failed", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } catch (Error e) {
                            System.out.println("ERROR IN RESPONSE: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR " + error.getMessage());
                        Toast toast = Toast.makeText(context1, "Failed", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
        // Add the request to the RequestQueue
        queue.add(request);
    }

    /**
     * This method handles JSONRequest for GET request (class assisting). Then, it update
     * the content of a the specific textview.
     * @param context The context of the view
     * @param url The url for the JSON request
     * @param connect Contains the name for the request(class assisting)
     */
    private void updateListTA(Context context, String url, final String connect) {
        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        final StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //  System.out.println("HereResponse" + response);
                            String responseC = "";
                            //    System.out.println("HEREResponse: " + responseC);
                            if (connect.equals("classAssist")) {
                                responseC = response.substring(12, response.length() - 2).replaceAll("\"", "").replaceAll(",", ", ");
                                EditUserProfile.findPosition(responseC, position);
                                responseC = EditUserProfile.addNewLine(responseC, position);
                                takeClassAssist.setText(responseC);
                                userProfileTA.ClassAssistingOld = responseC;
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

    /**
     * This method handles JSONRequest for POST request (class assisting). [delete data]
     * @param context The context of the view
     * @param url The url for the JSON request
     * @param currentText The text that want to be post
     * @param connect  Contains the name for the request(class assisting)
     */
    public void postRequestTA(final Context context, String url, final String currentText, final String connect) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //let's parse json data
                try {
                    System.out.println("response delete" + response);
                    if (connect.equals("classAssist")) {
                        String url2 = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/coursesAssisting";
                        updateListTA(context, url2, connect);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Login login = new Login();
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", login.user);
                if (connect.equals("classAssist")) {
                    params.put("majorName", EditUserProfile.getBeforeSpace(currentText));
                    params.put("number", EditUserProfile.getAfterSpace(currentText));
                }
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

    /**
     * This method is written soley for mockito test (PUT).
     * @param username username of the login user
     * @param url the url for JSON request
     * @param currentText the text that wanted to be deleted/post
     * @param status The type of request
     * @param connect Contains the name for the request(language, description, class taken, current class)
     *@return the url for JSON request
     */
    public String connectionForPUTTesting(String username, String url, String currentText, int status, String connect) {
        ForMockitoTest.connectionForPUTTesting(username, url, currentText, status, connect);
        return url;
    }
}