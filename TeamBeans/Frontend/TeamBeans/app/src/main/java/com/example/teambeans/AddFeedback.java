package com.example.teambeans;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddFeedback extends Fragment {
    private Button addFeedbackToDatabaseBtn;
    private Button cancelBtn;
    private String searchedRole;
    private RatingBar overallRatingBar;
    private RatingBar communicationRatingBar;
    private RatingBar teamworkRatingBar;
    private RatingBar workqualityRatingBar;
    private RatingBar attitudeRatingBar;
    public static String searchedUsername;
    private float overallRating;
    private float communicationRating;
    private float teamworkRating;
    private float workqualityRating;
    private float attitudeRating;
    private TextView showTeamwork;
    RequestQueue queue;
    private Context context;
    ForMockitoTest ForMockitoTest;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_feedback, container, false);
        context = getContext();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            searchedRole = bundle.getString("searchedRole", "");
            searchedUsername = bundle.getString("searchedUsername", "");
        }
        this.getActivity().setTitle(searchedUsername + "'s Feedback");

        queue = Volley.newRequestQueue(context);

        addFeedbackToDatabaseBtn = view.findViewById(R.id.addFeedbackToDatabaseBtn);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        overallRatingBar = view.findViewById(R.id.overall_ratingBar);
        communicationRatingBar = view.findViewById(R.id.communication_ratingBar);
        teamworkRatingBar = view.findViewById(R.id.teamwork_ratingBar);
        workqualityRatingBar = view.findViewById(R.id.workquality_ratingBar);
        attitudeRatingBar = view.findViewById(R.id.attitude_ratingBar);
        showTeamwork = view.findViewById(R.id.teamwork_rating);
        if(!searchedRole.equals("STUDENT")){
           teamworkRatingBar.setIsIndicator(true);
            showTeamwork.setVisibility(view.GONE);
            teamworkRatingBar.setVisibility(view.GONE);
           // teamworkRatingBarOnClickListener();
        }

        addFeedbackToDatabaseBtnOnClickListener();
        cancelBtnOnClickListener();
        return view;
    }
//    private void teamworkRatingBarOnClickListener() {
//        teamworkRatingBar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    Toast toast = Toast.makeText(context, "Teamwork not applicable in this role!", Toast.LENGTH_LONG);
//                    toast.show();
//                }
//                return true;
//            }
//        });
//    }
    private void addFeedbackToDatabaseBtnOnClickListener() {
        addFeedbackToDatabaseBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  System.out.println("RATING IS:" + overallRatingBar.getRating());
                setRating();
                putRating();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (searchedRole.equals("STUDENT")) {
                    fragmentTransaction.replace(R.id.flContent, new SearchedUserProfile());
                }else if(searchedRole.equals("TA")){
                    fragmentTransaction.replace(R.id.flContent, new SearchedUserProfileTA());
                }else if(searchedRole.equals("PROFESSOR")){
                    fragmentTransaction.replace(R.id.flContent, new SearchedUserProfileProfessor());
                }
                fragmentTransaction.commit();
            }
        });
    }

    private void cancelBtnOnClickListener() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  System.out.println("RATING IS:" + overallRatingBar.getRating());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (searchedRole.equals("STUDENT")) {
                    fragmentTransaction.replace(R.id.flContent, new SearchedUserProfile());
                }else if(searchedRole.equals("TA")){
                    fragmentTransaction.replace(R.id.flContent, new SearchedUserProfileTA());
                }else if(searchedRole.equals("PROFESSOR")){
                    fragmentTransaction.replace(R.id.flContent, new SearchedUserProfileProfessor());
                }
                fragmentTransaction.commit();
            }
        });
    }

    private void setRating() {
        this.overallRating = overallRatingBar.getRating();
        this.communicationRating = communicationRatingBar.getRating();
        if (searchedRole.equals("STUDENT")) {
            this.teamworkRating = teamworkRatingBar.getRating();
        }
        this.workqualityRating = workqualityRatingBar.getRating();
        this.attitudeRating = attitudeRatingBar.getRating();
    }

    private void putRating() {
        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/averageOutRatingProfile";

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Response rating" + response);
                    Toast toast = Toast.makeText(context, "Add rating success!", Toast.LENGTH_LONG);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(context, "Failed", Toast.LENGTH_LONG);
                toast.show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", searchedUsername);
                if (searchedRole.equals("STUDENT")) {
                    params.put("newTeamwork", String.valueOf(teamworkRating));
                }else{
                    params.put("newTeamwork", String.valueOf(0.0));
                }
                params.put("newCommunication", String.valueOf(communicationRating));
                params.put("newWorkQuality", String.valueOf(workqualityRating));
                params.put("newAttitude", String.valueOf(attitudeRating));
                params.put("newOverall", String.valueOf(overallRating));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public String connectionForPUTRatingTesting(final String username, final String role, String url){
        ForMockitoTest.connectionForPUTRatingTesting(username, role, url);
        return url;
    }
}
