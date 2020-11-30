package com.example.teambeans;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Adapter for displaying search results in Dashboard screen
 * @author Kathryn Rohlfing (kathrynr) with role updates by Renee Teoh
 * @see Dashboard
 */
public class SearchAdapter extends ArrayAdapter<String> {
    Button profile;
    String name;
    Context context;

    ArrayList<String> names;

    /**
     * Creates a SearchAdapter given a context and the list of results
     * @param context Context in which to add SearchAdapter
     * @param names List of usernames to display initially
     */
    public SearchAdapter(Context context, ArrayList<String> names) {
        super(context, 0, names);
        this.names = names;
        this.context = context;
    }

    /**
     * Sets up view and initializes variables for one line of search results
     * @param position Position of the current item in the list of results
     * @param convertView View inflated from search xml layout
     * @param parent Parent ViewGroup used to inflate convertView
     * @return View of current line
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        name = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search, parent, false);
        }
        // Lookup view for data population
        final TextView searchName = (TextView) convertView.findViewById(R.id.searchUserName);
        profile = (Button) convertView.findViewById(R.id.searchProfileBtn);
        // Populate the data into the template view using the data object
        searchName.setText(name);
        // Return the completed view to render on screen
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bundle bundle = new Bundle();
                name = (String) searchName.getText();
                bundle.putString("Username", name);
                bundle.putStringArrayList("names", names);

                // Send login inputs to back end
                JSONObject obj = new JSONObject();

                try {
                    obj.put("username", name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getRoles(bundle, name); // copied from inside request
            }
        });

        return convertView;
    }

    /**
     * Loads a user's profile on click of profile button next to their name in the search results
     * Allows a user to view a classmate's, TA's, or professor's profile
     * @param bundle Bundle used for passing user information to searched profile screen
     * @param role Role of the user being searched, determines type of profile to load
     */
    private void loadProfile(Bundle bundle, String role) {
        MainActivity act = (MainActivity) context;
        FragmentManager fragmentManager = act.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (role.equals("STUDENT")) {
            SearchedUserProfile searched = new SearchedUserProfile();
            //passing the text
            searched.setArguments(bundle);
            fragmentTransaction.replace(R.id.flContent, searched);
            fragmentTransaction.commit();
            searched.getView();
        }else if(role.equals("TA")) {
            SearchedUserProfileTA searched = new SearchedUserProfileTA();
            //passing the text
            searched.setArguments(bundle);
            fragmentTransaction.replace(R.id.flContent, searched);
            fragmentTransaction.commit();
            searched.getView();
        }else if(role.equals("PROFESSOR")) {
            SearchedUserProfileProfessor searched = new SearchedUserProfileProfessor();
            //passing the text
            searched.setArguments(bundle);
            fragmentTransaction.replace(R.id.flContent, searched);
            fragmentTransaction.commit();
            searched.getView();
        }
    }

    /**
     * Gets the role of a specific user so that the proper profile can be loaded
     * @param bundle Bundle used to pass information to profile screen
     * @param name Name of the user whose role is needed
     */
    private void getRoles(final Bundle bundle, String name) {
        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + name + "/role";
        final String[] searchedRole = {""};
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // System.out.println("RoleResponse" + response);
                            searchedRole[0] = response;
                            // System.out.println("ARRAY{0]" + role[0]);
                            loadProfile(bundle, searchedRole[0]);
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