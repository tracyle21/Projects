package com.example.teambeans;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.MockView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teambeans.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

public class Login extends Fragment {
    EditText username, password;
    Button submitBtn;
    Button registerBtn;
    ProgressBar loading;
    CircleImageView beans;
    public static String user;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.login, container, false);
        final Context context = getContext();
        final Context context1 = getActivity().getApplicationContext();


        username = (EditText) rootView.findViewById(R.id.username);
        password = (EditText) rootView.findViewById(R.id.password);
        submitBtn = (Button) rootView.findViewById(R.id.submitBtn);
        registerBtn = (Button) rootView.findViewById(R.id.registerBtn);
        loading = rootView.findViewById(R.id.progress);
        beans = rootView.findViewById(R.id.beans);

        loading.setVisibility(View.GONE);


        //hide password
        password.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                String pass = password.getText().toString();


                final Bundle bundle = new Bundle();
                bundle.putString("Username", user);
                bundle.putString("Password", pass);
                bundle.putStringArrayList("names", new ArrayList<String>());

                // Send login inputs to back end
                JSONObject obj = new JSONObject();

                try {
                    obj.put("username", user);
                    obj.put("password", pass);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loading.setVisibility(View.VISIBLE);
                beans.setVisibility(View.GONE);
                RequestQueue queue = Volley.newRequestQueue(context);

                //String url ="http://10.0.2.2:8080/login";
                //String url ="http://coms-309-rp-09.cs.iastate.edu:8080/login";
                String url ="http://10.24.226.25:8080/login";
                // Request a string response from the provided URL.
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response){
                            try {
                                VolleyLog.v("Response:%n %s", response);
                                System.out.println("RESPONSE" + response);
                                if(response != null)
                                {
                                    loginUser(bundle);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.setVisibility(View.GONE);
                            Toast toast = Toast.makeText(context1, "Login Failed", Toast.LENGTH_SHORT);
                            toast.show();
                            VolleyLog.e("Error: " + error.getMessage());
                            System.out.println("ERROR " + error.getMessage());
                        }
                });


                // Add the request to the RequestQueue
                queue.add(request);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Registration registration = new Registration();

                fragmentTransaction.replace(R.id.flContent, registration);
                fragmentTransaction.commit();
            }
        });

        return rootView;
    }

    private void loginUser(Bundle bundle) {
        MainActivity act = (MainActivity) getActivity();
        act.ChangeLock(); //unlocks nav bar after passing login
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Dashboard dash = new Dashboard();
        //passing the text
        dash.setArguments(bundle);

        fragmentTransaction.replace(R.id.flContent, dash);

        fragmentTransaction.commit();
    }

    private void failLogin() {

    }
}
