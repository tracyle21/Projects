package com.example.teambeans;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Registration extends Fragment {
    // Declaration of layout elements
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private Button cancelButton;
    private Button registerButton;
    private RadioGroup roleRadioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration, container, false);
        final Context context = getContext();
        final Context context1 = getActivity().getApplicationContext();

        // Initialize layout elements
        firstNameInput = view.findViewById(R.id.firstNameInput);
        lastNameInput = view.findViewById(R.id.lastNameInput);
        usernameInput = view.findViewById(R.id.usernameInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        emailInput = view.findViewById(R.id.emailInput);
        cancelButton = view.findViewById(R.id.cancelButton);
        registerButton = view.findViewById(R.id.registerButton);
        roleRadioGroup = view.findViewById(R.id.groupradio);

        passwordInput.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        // On cancel button click, display the login screen
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Display login screen
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Login login = new Login();

                fragmentTransaction.replace(R.id.flContent, login);
                fragmentTransaction.commit();
            }
        });

        // On register button click, create a new user and inflate the login screen
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Get registration inputs
                String firstName = firstNameInput.getText().toString();
                String lastName = lastNameInput.getText().toString();
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String email = emailInput.getText().toString();

                int radioButtonID = roleRadioGroup.getCheckedRadioButtonId();
                View radioButton = roleRadioGroup.findViewById(radioButtonID);
                int idx = roleRadioGroup.indexOfChild(radioButton);
                RadioButton r = (RadioButton) roleRadioGroup.getChildAt(idx);
                String role = r.getText().toString().toUpperCase();

                // Send registration inputs to back
                JSONObject obj = new JSONObject();

                try {
                    obj.put("firstName", firstName);
                    obj.put("lastName", lastName);
                    obj.put("email", email);
                    obj.put("username", username);
                    obj.put("password", password);
                    obj.put("role", role);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(context);

               // String url ="http://10.0.2.2:8080/users";
                //String url ="http://10.0.2.2:8080/users";
                String url ="http://10.24.226.25:8080/users";

                // Request a string response from the provided URL.
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response != null) {
                                    System.out.println("RESPONSE " + response.toString());
                                    Toast toast = Toast.makeText(context1, "Registration Success!!!", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                else {
                                    Toast toast = Toast.makeText(context1, "Registration Failed", Toast.LENGTH_LONG);
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
                            Toast toast = Toast.makeText(context1, "Registration Failed", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                // Add the request to the RequestQueue
                queue.add(request);
                // Inflate login screen
                //can't do this since response is always null

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Login login = new Login();

                fragmentTransaction.replace(R.id.flContent, login);
                fragmentTransaction.commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
