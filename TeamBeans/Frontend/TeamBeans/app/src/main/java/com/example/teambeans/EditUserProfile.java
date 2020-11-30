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
 * EditUserProfile process the data and display the data for edit_user_profile.xml. It is for user with the role of STUDENT.
 * @author Renee
 */
public class EditUserProfile extends Fragment {

    private Button addCurrentClassBtn;
    private Button addClassTakenBtn;
    private Button addProgrammingLanguageBtn;
    private Button saveProfileBtn;
    private Button deleteProgrammingLanguageBtn;
    private Button deleteCurrentClassBtn;
    private Button deleteClassTakenBtn;
    private String listOfClassTaken = "";
    private String listOfProgrammingLanguage = "";
    private String listOfCurrentClass = "";
    private ArrayList<String> currentClass = new ArrayList<String>();
    private ArrayList<String> classTaken = new ArrayList<String>();
    private ArrayList<String> programmingLanguage = new ArrayList<String>();
    private ArrayList<Integer> position = new ArrayList<Integer>();
    private int flag1 = 0;
    private int flag2 = 0;
    private int flag3 = 0;
    private int flag4 = 0;
    private int flag5 = 0;
    private int flag6 = 0;
    private EditText description;
    private TextView takeClassTaken;
    private TextView takeCurrentClass;
    private TextView takeLanguage;
    final private UserProfile userProfile = new UserProfile();
    final private Login login = new Login();
    ForMockitoTest ForMockitoTest;

    /**
     * Lifetime catch of screen, sets up the view of the screen and initializes data.
     * @param inflater Layout inflater of the screen.
     * @param container App view frame.
     * @param savedInstanceState Preliminary data for the screen.
     * @return Created view of the screen.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit_user_profile, container, false);
        final Context context = getContext();
        final Context context1 = getActivity().getApplicationContext();
        this.getActivity().setTitle("Edit Profile");
        ForMockitoTest = new ForMockitoTest(context);

        addCurrentClassBtn = view.findViewById(R.id.addCurrentClassBtn);
        addClassTakenBtn = view.findViewById(R.id.addClassTakenBtn);
        addProgrammingLanguageBtn = view.findViewById(R.id.addProgrammingLanguageBtn);
        saveProfileBtn = view.findViewById(R.id.saveProfileBtn);
        deleteProgrammingLanguageBtn = view.findViewById(R.id.deleteProgrammingLanguageBtn);
        deleteCurrentClassBtn = view.findViewById(R.id.deleteCurrentClassBtn);
        deleteClassTakenBtn = view.findViewById(R.id.deleteClassTakenBtn);

        //set the value to text
        final TextView usernameText = (TextView) view.findViewById(R.id.username);
        takeClassTaken = (TextView) view.findViewById(R.id.showClassTakenInDatabase);
        takeCurrentClass = (TextView) view.findViewById(R.id.showCurrentClassInDatabase);
        takeLanguage = (TextView) view.findViewById(R.id.showProgrammingLanguageInDatabase);

        usernameText.setText(login.user);
        takeClassTaken.setText(userProfile.classTakenOld);
        takeCurrentClass.setText(userProfile.currentClassOld);
        takeLanguage.setText(userProfile.programmingLanguageOld);

        description = (EditText) view.findViewById(R.id.description2);

        //userProfile.connection(context,url,connect);
        if (userProfile.descriptionOld != null) {
            System.out.println("edit user description from user profile: " + userProfile.descriptionOld);
            description.setText(userProfile.descriptionOld, TextView.BufferType.EDITABLE);
        }
        String[] classes = {"COMS 309", "COMS 331", "COMS 342", "COMS 352", "COMS 402", "ENGL 150", "ENGL 250", "ENGL 309", "ENGL 314"};
        String[] languages = {"Java", "C++", "C", "Python", "Javascript", "C#", "PHP", "Dart", "Ruby", "Kotlin", "Cobol", "Rust", "VBA"};

        final Spinner spinner1 = (Spinner) view.findViewById(R.id.current_class);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, classes);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        final Spinner spinner2 = (Spinner) view.findViewById(R.id.class_taken);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, classes);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        final Spinner spinner3 = (Spinner) view.findViewById(R.id.programming_languages);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, languages);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        addCurrentClassBtnOnClickListener(context, context1, spinner1, view);
        addClassTakenBtnOnClickListener(context, context1, spinner2, view);
        addProgrammingLanguageBtnOnClickListener(context, context1, spinner3, view);
        deleteCurrentClassBtn(context, context1, spinner1, view);
        deleteClassTakenBtn(context, context1, spinner2, view);
        deleteProgrammingLanguageBtn(context, context1, spinner3, view);
        saveProfileBtnOnClickListener(context, context1);

        return view;
    }

    /**
     * Return the string that is before a space. This is to get the course name.
     * @param str A string that contains course name and course code.
     * @return A string that contains course name
     */
    public String getBeforeSpace(String str) {
        if (str.contains(" ")) {
            str = str.substring(0, str.indexOf(" "));
            System.out.println("beforeSpace" + str);
        }
        return str;
    }

    /**
     * This method delete a current class by sending a post request to spring boot. Mainly, process the data obtained by the spinner.
     *
     * @param context The context of the view
     * @param context1 The context of the view for toast
     * @param spinner1 The spinner of current class
     * @param view View of the screen
     */
    private void deleteCurrentClassBtn(final Context context, final Context context1, final Spinner spinner1, final View view) {
        deleteCurrentClassBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView showCurrentClass = (TextView) view.findViewById(R.id.showCurrentClass);
                if (flag6 == 0) {
                    flag1 = 0;
                    currentClass.removeAll(currentClass);
                    listOfCurrentClass = "";
                    showCurrentClass.setText(listOfCurrentClass);
                    // System.out.println("Should be empty" + listOfProgrammingLanguage);
                }
                String currentClassText = spinner1.getSelectedItem().toString();
                //System.out.println("HERE");
                //System.out.println("currentClassText: "+currentClassText );
                if (userProfile.currentClassOld.contains(currentClassText)) {
                    if (!currentClass.contains(currentClassText)) {
                        currentClass.add(currentClassText);
                        if (flag6 != 0) {
                            listOfCurrentClass = listOfCurrentClass.concat(", ");
                            if (flag6 % 4 == 0) {
                                listOfCurrentClass = listOfCurrentClass.concat("\n");
                            }
                        }
                        listOfCurrentClass = listOfCurrentClass.concat(currentClassText);
                        // System.out.println("LIST OF CLASS: " + listOfCurrentClass);

                        showCurrentClass.setText(listOfCurrentClass);
                        flag6++;
                        String url = "http://10.24.226.25:8080/deleteCurrentCourse";
                        postRequest(context, url, currentClassText, "currentClass");
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
     * This method delete a class taken by sending a post request to spring boot. Mainly, process the data obtained by the spinner.
     *
     * @param context The context of the view
     * @param context1 The context of the view for toast
     * @param spinner2 The spinner of class taken
     * @param view View of the screen
     */
    private void deleteClassTakenBtn(final Context context, final Context context1, final Spinner spinner2, final View view) {
        deleteClassTakenBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView showClassTaken = (TextView) view.findViewById(R.id.showClassTaken);
                if (flag5 == 0) {
                    flag2 = 0;
                    classTaken.removeAll(classTaken);
                    listOfClassTaken = "";
                    showClassTaken.setText(listOfClassTaken);
                    //System.out.println("Should be empty" + listOfProgrammingLanguage);
                }
                String currentClassText = spinner2.getSelectedItem().toString();
                //System.out.println("currentClassText: "+currentClassText );
                if (userProfile.classTakenOld.contains(currentClassText)) {
                    if (!classTaken.contains(currentClassText)) {
                        classTaken.add(currentClassText);
                        if (flag5 != 0) {
                            listOfClassTaken = listOfClassTaken.concat(", ");
                            if (flag5 % 4 == 0) {
                                listOfClassTaken = listOfClassTaken.concat("\n");
                            }
                        }
                        listOfClassTaken = listOfClassTaken.concat(currentClassText);
                        // System.out.println("LIST OF CLASS: "+listOfClass );
                        showClassTaken.setText(listOfClassTaken);
                        flag5++;
                        String url = "http://10.24.226.25:8080/deleteOldCourse";
                        postRequest(context, url, currentClassText, "classTaken");
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
     * This method delete a language by sending a post request to spring boot.  Mainly, process the data obtained by the spinner.
     *
     * @param context The context of the view
     * @param context1 The context of the view for toast
     * @param spinner3 The spinner of language
     * @param view View of the screen
     */
    private void deleteProgrammingLanguageBtn(final Context context, final Context context1, final Spinner spinner3, final View view) {
        deleteProgrammingLanguageBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView showProgrammingLanguage = (TextView) view.findViewById(R.id.showProgrammingLanguage);
                if (flag4 == 0) {
                    flag3 = 0;
                    programmingLanguage.removeAll(programmingLanguage);
                    listOfProgrammingLanguage = "";
                    showProgrammingLanguage.setText(listOfProgrammingLanguage);
                    //System.out.println("Should be empty" + listOfProgrammingLanguage);
                }
                String currentLanguageText = spinner3.getSelectedItem().toString();
                // System.out.println("HERE");
                //System.out.println("currentClassText: "+currentClassText );
                if (userProfile.programmingLanguageOld.contains(currentLanguageText)) {
                    if (!programmingLanguage.contains(currentLanguageText)) {
                        programmingLanguage.add(currentLanguageText);
                        if (flag4 != 0) {
                            listOfProgrammingLanguage = listOfProgrammingLanguage.concat(", ");
                            if (flag4 % 4 == 0) {
                                listOfProgrammingLanguage = listOfProgrammingLanguage.concat("\n");
                            }
                        }
                        listOfProgrammingLanguage = listOfProgrammingLanguage.concat(currentLanguageText);
                        // System.out.println("LIST OF CLASS: "+listOfClass );
                        showProgrammingLanguage.setText(listOfProgrammingLanguage);
                        flag4++;
                        String url = "http://10.24.226.25:8080/deleteLanguage";
                        postRequest(context, url, currentLanguageText, "language");
                    } else {
                        Toast toast = Toast.makeText(context1, "Language have been selected!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(context1, "Language is not in database", Toast.LENGTH_LONG);
                    toast.show();
                }

            }

        });
    }

    /**
     * This method add a current class by sending a put request to spring boot. Mainly, process the data obtained by the spinner.
     *
     * @param context The context of the view
     * @param context1 The context of the view for toast
     * @param spinner1 The spinner of current class
     * @param view View of the screen
     */
    private void addCurrentClassBtnOnClickListener(final Context context, final Context context1, final Spinner spinner1, final View view) {
        addCurrentClassBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView showCurrentClass = (TextView) view.findViewById(R.id.showCurrentClass);
                String currentClassText = spinner1.getSelectedItem().toString();
                if (flag1 == 0) {
                    flag6 = 0;
                    currentClass.removeAll(currentClass);
                    listOfCurrentClass = "";
                    showCurrentClass.setText(listOfCurrentClass);
                    //System.out.println("Should be empty" + listOfProgrammingLanguage);
                }
                //System.out.println("HERE");
                //System.out.println("currentClassText: "+currentClassText );
                if (!userProfile.currentClassOld.contains(currentClassText)) {
                    if (!currentClass.contains(currentClassText)) {
                        currentClass.add(currentClassText);
                        if (flag1 != 0) {
                            listOfCurrentClass = listOfCurrentClass.concat(", ");
                            if (flag1 % 4 == 0) {
                                listOfCurrentClass = listOfCurrentClass.concat("\n");
                            }
                        }
                        listOfCurrentClass = listOfCurrentClass.concat(currentClassText);
                        // System.out.println("LIST OF CLASS: " + listOfCurrentClass);

                        showCurrentClass.setText(listOfCurrentClass);
                        flag1++;
                        String url = "http://10.24.226.25:8080/addCurrentCourse";
                        connection(context, context1, url, currentClassText, Request.Method.PUT, "currentClass", false);
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
     * This method add a class taken by sending a put request to spring boot.  Mainly, process the data obtained by the spinner.
     *
     * @param context The context of the view
     * @param context1 The context of the view for toast
     * @param spinner2 The spinner of class taken
     * @param view View of the screen
     */
    private void addClassTakenBtnOnClickListener(final Context context, final Context context1, final Spinner spinner2, final View view) {
        addClassTakenBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String currentClassText = spinner2.getSelectedItem().toString();
                TextView showClassTaken = (TextView) view.findViewById(R.id.showClassTaken);
                if (flag2 == 0) {
                    flag5 = 0;
                    classTaken.removeAll(classTaken);
                    listOfClassTaken = "";
                    showClassTaken.setText(listOfClassTaken);
                    //System.out.println("Should be empty" + listOfProgrammingLanguage);
                }
                //System.out.println("currentClassText: "+currentClassText );
                if (!userProfile.classTakenOld.contains(currentClassText)) {
                    if (!classTaken.contains(currentClassText)) {
                        classTaken.add(currentClassText);
                        if (flag2 != 0) {
                            listOfClassTaken = listOfClassTaken.concat(", ");
                            if (flag2 % 4 == 0) {
                                listOfClassTaken = listOfClassTaken.concat("\n");
                            }
                        }
                        listOfClassTaken = listOfClassTaken.concat(currentClassText);
                        // System.out.println("LIST OF CLASS: "+listOfClass );

                        showClassTaken.setText(listOfClassTaken);
                        flag2++;
                        String url = "http://10.24.226.25:8080/addOldCourse";
                        connection(context, context1, url, currentClassText, Request.Method.PUT, "classTaken", false);
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
     * This method add a language by sending a put request to spring boot. Mainly, process the data obtained by the spinner.
     *
     * @param context The context of the view
     * @param context1 The context of the view for toast
     * @param spinner3 The spinner of language
     * @param view View of the screen
     */
    private void addProgrammingLanguageBtnOnClickListener(final Context context, final Context context1, final Spinner spinner3, final View view) {
        addProgrammingLanguageBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String currentLanguageText = spinner3.getSelectedItem().toString();
                TextView showProgrammingLanguage = (TextView) view.findViewById(R.id.showProgrammingLanguage);
                // System.out.println("HERE");
                if (flag3 == 0) {
                    flag4 = 0;
                    programmingLanguage.removeAll(programmingLanguage);
                    listOfProgrammingLanguage = "";
                    showProgrammingLanguage.setText(listOfProgrammingLanguage);
                    //System.out.println("Should be empty" + listOfProgrammingLanguage);
                }
                System.out.println("CHECK: " + userProfile.programmingLanguageOld);
                if (currentLanguageText.equals("C")) {
                    currentLanguageText += "*";
                }
                if (currentLanguageText.equals("Java")) {
                    currentLanguageText += "*";
                }
                if (!userProfile.programmingLanguageOld.contains(currentLanguageText)) {
                    if (currentLanguageText.equals("C*")) {
                        currentLanguageText = currentLanguageText.replace("*", "");
                    }
                    if (currentLanguageText.equals("Java*")) {
                        currentLanguageText = currentLanguageText.replace("*", "");
                    }
                    if (!programmingLanguage.contains(currentLanguageText)) {
                        programmingLanguage.add(currentLanguageText);
                        if (flag3 != 0) {
                            listOfProgrammingLanguage = listOfProgrammingLanguage.concat(", ");
                            if (flag3 % 4 == 0) {
                                listOfProgrammingLanguage = listOfProgrammingLanguage.concat("\n");
                            }
                        }
                        listOfProgrammingLanguage = listOfProgrammingLanguage.concat(currentLanguageText);
                        // System.out.println("LIST OF CLASS: "+ listOfClass );

                        showProgrammingLanguage.setText(listOfProgrammingLanguage);
                        flag3++;
                        String url = "http://10.24.226.25:8080/addLanguage";
                        connection(context, context1, url, currentLanguageText, Request.Method.PUT, "language", false);
                    } else {
                        Toast toast = Toast.makeText(context1, "Language have been selected!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(context1, "Language have been selected!2", Toast.LENGTH_LONG);
                    toast.show();
                }


            }

        });
    }

    /**
     * This method will be accessed when the saveProfileBtn is called. It put add user description to the database by sending a put request to Spring Boot.
     * @param context The context of the view
     * @param context1 The context of the view for toast
     */
    private void saveProfileBtnOnClickListener(final Context context, final Context context1) {
        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descriptionText = description.getText().toString();
                if (descriptionText != null) {
                    String url = "http://10.24.226.25:8080/addUserDescription";
                    connection(context, context1, url, descriptionText, Request.Method.PUT, "description", true);
                }
            }
        });
    }
    /**
     * Return the string that is after a space. This is to get the course code.
     * @param str A string that contains course name and course code.
     * @return A string that contains course code
     */
    public String getAfterSpace(String str) {
        if (str.contains(" ")) {
            str = str.substring(str.indexOf(" ") + 1, str.length());
            System.out.println("afterSpace" + str);
        }
        return str;
    }

    /**
     * This method changes the fragment by using Fragment Transaction. (From EditUserProfile -> UserProfile)
     * @param context1 The context of the view
     */
    private void changeTransaction(Context context1) {
        Toast toast = Toast.makeText(context1, "Edit Success!!", Toast.LENGTH_LONG);
        toast.show();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, new UserProfile());
        fragmentTransaction.commit();
    }

    /**
     * This method handles JSONRequest for PUT request (language, description, class taken, current class)
     * @param context The context of the view
     * @param context1 The context of the view for toast
     * @param url The url for the JSON request
     * @param currentText The text that want to be put
     * @param status The type of request
     * @param connect Contains the name for the request(language, description, class taken, current class)
     * @param savedButtonClicked true, save button is clicked, false, save button is not clicked
     */
    private void connection(final Context context, final Context context1, String url, String currentText, int status, final String connect, final boolean savedButtonClicked) {
        //System.out.println("TESTING HERE:" + getBeforeSpace("COMS 309")+"|sec|"+ getAfterSpace("COMS 309"));
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", login.user);
            if (connect.equals("language")) {
                obj.put("name", currentText);
            } else if (connect.equals("description")) {
                obj.put("description", currentText);
            } else if (connect.equals("classTaken") || connect.equals("currentClass")) {
                obj.put("majorName", getBeforeSpace(currentText));
                obj.put("number", getAfterSpace(currentText));
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
                                if (connect.equals("language")) {
                                    String url2 = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/languages";
                                    updateList(context, url2, connect);
                                } else if (connect.equals("classTaken")) {
                                    String url2 = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/oldCourses";
                                    updateList(context, url2, connect);
                                } else if (connect.equals("currentClass")) {
                                    String url2 = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/currentCourses";
                                    updateList(context, url2, connect);
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
     * This method handles JSONRequest for GET request (language, class taken, current class). Then, it update
     * the content of a the specific textview.
     * @param context The context of the view
     * @param url The url for the JSON request
     * @param connect Contains the name for the request(language, class taken, current class)
     */
    public void updateList(Context context, String url, final String connect) {
        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        final StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //  System.out.println("HereResponse" + response);
                            String responseC = "";
                            if (connect.equals("currentClass") || connect.equals("classTaken")) {
                                responseC = response.substring(12, response.length() - 2).replaceAll("\"", "").replaceAll(",", ", ");
                                findPosition(responseC, position);
                                responseC = addNewLine(responseC, position);
                                //    System.out.println("HEREResponse: " + responseC);
                            }
                            if (connect.equals("language")) {
                                responseC = response.substring(14, response.length() - 2).replaceAll("\"", "").replaceAll(",", ", ");
                                findPosition(responseC, position);
                                responseC = addNewLine(responseC, position);
                                takeLanguage.setText(responseC);
                                userProfile.programmingLanguageOld = responseC;
                            } else if (connect.equals("currentClass")) {
                                takeCurrentClass.setText(responseC);
                                userProfile.currentClassOld = responseC;
                            } else if (connect.equals("classTaken")) {
                                takeClassTaken.setText(responseC);
                                userProfile.classTakenOld = responseC;
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
     * This method add a new line character for every 4 courses/languages in a string.
     * @param str A string that contains courses
     * @param position An array list that contains the position of ',' in the string
     * @return A new string with the new line character
     */
    public static String addNewLine(String str, ArrayList<Integer> position) {
        System.out.println("BEFORE STRING IS\n" + str);
        String temp = "";
        int pos = 0;
        for (int i = 0; i < position.size(); i++) {
            if ((i + 1) % 4 == 0 && (i + 1) > 0) {
                System.out.println("TOEDIT:" + position.get(i));
                temp += str.substring(pos, position.get(i) + 2) + '\n';
                pos = position.get(i) + 2;
                System.out.println("POSITIONS:" + pos);
            }
        }
        temp += str.substring(pos, str.length());
        System.out.println("STRING IS\n" + temp);
        return temp;

    }

    /**
     * This method add the position of ',' into an array list
     * @param str A string that contains courses
     * @param position An array list that wanted to be stored
     */
    public static void findPosition(String str, ArrayList<Integer> position) {
        position.removeAll(position);
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ',') {
                position.add(i);
            }
        }
        System.out.println("position" + Arrays.toString(position.toArray()));
    }

    /**
     * This method handles JSONRequest for POST request (language, class taken, current class). [delete data]
     * @param context The context of the view
     * @param url The url for the JSON request
     * @param currentText The text that want to be post
     * @param connect  Contains the name for the request(language, class taken, current class)
     */
    public void postRequest(final Context context, String url, final String currentText, final String connect) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //let's parse json data
                try {
                    System.out.println("response delete" + response);
                    if (connect.equals("language")) {
                        String url2 = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/languages";
                        updateList(context, url2, "language");
                    } else if (connect.equals("classTaken")) {
                        String url2 = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/oldCourses";
                        updateList(context, url2, connect);
                    } else if (connect.equals("currentClass")) {
                        String url2 = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/currentCourses";
                        updateList(context, url2, connect);
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
                if (connect.equals("language")) {
                    params.put("name", currentText);
                } else if (connect.equals("classTaken") || connect.equals("currentClass")) {
                    params.put("majorName", getBeforeSpace(currentText));
                    params.put("number", getAfterSpace(currentText));
                }
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    /**
     * This method is written soley for mockito test (POST).
     * @param username username of the login user
     * @param url the url for JSON request
     * @param currentText the text that wanted to be deleted/post
     * @return the url for JSON request
     */
    public String connectionForPOSTTesting(String username, String url, String currentText){
        ForMockitoTest.connectionForPOSTTesting(username, url, currentText);
        return url;
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
    public String connectionForPUTTesting(String username, String url,String currentText, int status, String connect){
        ForMockitoTest.connectionForPUTTesting(username,url,currentText,status,connect);
        return url;
    }
}
