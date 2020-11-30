package com.example.teambeans;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Dashboard screen and logic for searching users with or without filters on results
 * Uses SearchAdapter to display results, and can launch SearchedUserProfile screen
 * when a user is selected
 * @author Kathryn Rohlfing (kathrynr)
 * @see SearchAdapter
 * @see SearchedUserProfile
 */
public class Dashboard extends Fragment {
    SearchView search;
    ListView filterListView1;
    ListView filterListView2;
    ListView listView;
    ArrayList<String> filter = new ArrayList<String>();
    ArrayList<String> options1 = new ArrayList<String>();
    ArrayList<String> options2 = new ArrayList<String>();
    OptionsAdapter filterAdapter1;
    OptionsAdapter filterAdapter2;
    SearchAdapter listAdapter;
    ArrayList<String> results = new ArrayList<String>();
    Context context;
    RequestQueue queue;
    ProgressBar loading;
    RadioGroup radioButtons;
    String url = "";


    /**
     * Sets up view of the screen, initializes variables, and sets listener for search
     * @param inflater Layout inflater of the screen.
     * @param container App view frame.
     * @param savedInstanceState Preliminary data for the screen.
     * @return Created view of the screen.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.dashboard, container, false);
        context = getContext();
        this.getActivity().setTitle("Dashboard");

        search = (SearchView) rootView.findViewById(R.id.searchView);
        listView = (ListView) rootView.findViewById(R.id.listView);
        filterListView1 = (ListView) rootView.findViewById(R.id.filterListView1);
        filterListView2 = (ListView) rootView.findViewById(R.id.filterListView2);
        radioButtons = (RadioGroup) rootView.findViewById(R.id.textView);


        populate();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            results = bundle.getStringArrayList("names");
        }

        listAdapter = new SearchAdapter(context, results);
        filterAdapter1 = new OptionsAdapter(context, options1, 1);
        filterAdapter2 = new OptionsAdapter(context, options2, 2);

        listView.setAdapter((listAdapter));

        filterListView1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        filterListView2.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        filterListView1.setAdapter(filterAdapter1);
        filterListView2.setAdapter(filterAdapter2);

        loading = rootView.findViewById(R.id.progress);
        loading.setVisibility(View.GONE);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String value) {
                final String contents = value;
                queue = Volley.newRequestQueue(context);
                url = "http://10.24.226.25:8080/getAllUsers";
                // Request a string response from the provided URL.
                System.out.println(value);
                loading.setVisibility(View.VISIBLE);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) { try {
                                    if (response != null) {
                                        listAdapter.clear();
                                        System.out.println(response);
                                        JSONArray usersArray = response.getJSONArray("users");
                                        for(int i = 0; i < usersArray.length(); i++)
                                        {
                                            results.add(usersArray.getString(i));
                                            listAdapter.notifyDataSetChanged();
                                        }
                                        //filter(contents, usersArray);
                                        filter2(contents, usersArray);
                                        loading.setVisibility(View.GONE);
                                        Toast toast = Toast.makeText(context, "Search Success", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                            } catch (Exception e) {
                                System.out.println("Exception caught");
                                loading.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, "Search Failed", Toast.LENGTH_LONG);
                                toast.show();
                                VolleyLog.e("Error: " + error.getMessage() + " " + error.toString());
                                System.out.println("ERROR " + error.getMessage() + " " + error.toString());
                                loading.setVisibility(View.GONE);
                            }
                        });
                // Add the request to the RequestQueue
                queue.add(request);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String value) {
                return false;
            }
        });
        return rootView;
    }

    /**
     * Filters results by making server requests with given parameters to allow users to search
     * for others with certain languages or classes and returns the updated results list
     * @param contents The string input into the search bar by a user
     * @param usersArray The array of usernames provided initially by server
     * @return New list of usernames to display in search list
     * @throws JSONException
     */
    public List<String> filter2(String contents, JSONArray usersArray) throws JSONException {
        results.clear();
        for(int i = 0; i < usersArray.length();i++)
        {
            String current = usersArray.getString(i);
            if(contents.equals("all") || current.equals(contents) || (current.length() > contents.length() && current.contains(contents)) || (current.length() < contents.length() && contents.contains(current)))
            {
                results.add(current);
            }
        }
        for(int i =0; i< filterAdapter1.getThisFilter().size(); i++)
        {
            if(filterAdapter1.getThisFilter().get(i)) {
                url = "http://10.24.226.25:8080/getUsersForLanguage?language=";
                url += filterAdapter1.getThisNames().get(i);
                //url += filterAdapter1.filter.get(i);
                makeRequest(url);
            }
        }
        for(int i =0; i< filterAdapter2.getThisFilter().size(); i++)
        {

            if(filterAdapter2.getThisFilter().get(i)) {
                int id = radioButtons.indexOfChild(getActivity().findViewById(radioButtons.getCheckedRadioButtonId()));
                url = "http://10.24.226.25:8080/getNamesTakingACourse?name=";
                String[] split = filterAdapter2.getThisNames().get(i).split(" ");
                url += split[0] + "&number=";
                url += split[1];
                if(id == 1) // TA
                {
                    url = "http://10.24.226.25:8080/getAssistantsForACourse?courseName=" + split[0] + "&number=" + split[1];
                }
                else if(id == 2) //Professor
                {
                    url = "http://10.24.226.25:8080/getProfessorsTeachingACourse?courseName=" + split[0] + "&number=" + split[1];
                }
                makeRequest(url);
            }
        }
        loading.setVisibility(View.GONE);
        return results;
    }

    /**
     * Makes requests to the server to find users meeting certain requirements, as called in
     * filter
     * @param url The server url to make request from
     */
    public void makeRequest(String url)
    {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) { try {
                            loading.setVisibility(View.VISIBLE);
                                if (response != null) {
                                    System.out.println(response);
                                    JSONArray usersArray = response.getJSONArray("users");
                                    List<String> temp = new ArrayList<String>();
                                    for(int i = 0; i < usersArray.length(); i++)
                                    {
                                        String[] res = usersArray.getString(i).split("\\s+");
                                        temp.add(res[0]);
                                    }
                                    results.retainAll(temp); // only keep the things that are in both
                                    listAdapter.notifyDataSetChanged();
                                }
                            loading.setVisibility(View.GONE);
                        } catch (Exception e) {
                            System.out.println("Exception caught");
                            loading.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.setVisibility(View.GONE);
                            Toast toast = Toast.makeText(context, "Search Failed", Toast.LENGTH_LONG);
                            toast.show();
                            VolleyLog.e("Error: " + error.getMessage() + " " + error.toString());
                            System.out.println("ERROR " + error.getMessage() + " " + error.toString());
                        }
                    });
        // Add the request to the RequestQueue
        queue.add(request);
    }
    public void populate(){

        String[] languages = {"Java", "C++", "C", "Python", "Javascript", "C#", "PHP", "Dart", "Ruby", "Kotlin", "Cobol", "Rust", "VBA", "End"};
        for(int i = 0; i < languages.length;i++)
        {
            options1.add(languages[i]);
        }
        String[] classes = {"COMS 309", "COMS 331", "COMS 342", "COMS 352", "COMS 402", "ENGL 150", "ENGL 250", "ENGL 309", "ENGL 314", "End"};
        for(int i = 0; i < classes.length;i++)
        {
            options2.add(classes[i]);
        }
    }
}