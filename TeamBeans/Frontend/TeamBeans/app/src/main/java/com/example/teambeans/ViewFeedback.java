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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewFeedback extends Fragment {
    private Button backBtn;
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
    private int totalReviewed;
    RequestQueue queue;
    private Context context;
    private TextView reviewed;
    private TextView numOverall;
    private TextView numCommunication;
    private TextView numTeamwork;
    private TextView numWorkquality;
    private TextView numAttitude;
    private TextView showTeamwork;
    ForMockitoTest ForMockitoTest;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.view_feedback, container, false);
        context = getContext();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            searchedRole = bundle.getString("searchedRole", "");
            searchedUsername = bundle.getString("searchedUsername", "");
        }
        this.getActivity().setTitle(searchedUsername + "'s Average Feedback");

        queue = Volley.newRequestQueue(context);

        backBtn = view.findViewById(R.id.backBtn);
        overallRatingBar = view.findViewById(R.id.overall_ratingBar);
        communicationRatingBar = view.findViewById(R.id.communication_ratingBar);
        teamworkRatingBar = view.findViewById(R.id.teamwork_ratingBar);
        workqualityRatingBar = view.findViewById(R.id.workquality_ratingBar);
        attitudeRatingBar = view.findViewById(R.id.attitude_ratingBar);
        reviewed = view.findViewById(R.id.rating);
        numOverall = view.findViewById(R.id.numOverall);
        numCommunication = view.findViewById(R.id.numCommunication);
        numTeamwork = view.findViewById(R.id.numTeamwork);
        numWorkquality = view.findViewById(R.id.numWorkQuality);
        numAttitude = view.findViewById(R.id.numAttitude);
        showTeamwork = view.findViewById(R.id.teamwork_rating);
        getRating();
        if(!searchedRole.equals("STUDENT")){
            System.out.println("NOT STUDENT" + searchedRole);
            showTeamwork.setVisibility(view.GONE);
            numTeamwork.setVisibility(view.GONE);
            teamworkRatingBar.setVisibility(view.GONE);
           // teamworkRatingBarOnClickListener();
        }
        backBtnOnClickListener();
        return view;
    }
//
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

    private void backBtnOnClickListener() {
        backBtn.setOnClickListener(new View.OnClickListener() {
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

    private void setRatingBar(){
        overallRatingBar.setIsIndicator(true);
        communicationRatingBar.setIsIndicator(true);
        teamworkRatingBar.setIsIndicator(true);
        workqualityRatingBar.setIsIndicator(true);
        attitudeRatingBar.setIsIndicator(true);

        overallRatingBar.setRating(overallRating);
        communicationRatingBar.setRating(communicationRating);
        teamworkRatingBar.setRating(teamworkRating);
        workqualityRatingBar.setRating(workqualityRating);
        attitudeRatingBar.setRating(attitudeRating);
        reviewed.setText("Total number of reviews: " + totalReviewed);

        numOverall.setText(String.valueOf(overallRating));
        numCommunication.setText(String.valueOf(communicationRating));
        numTeamwork.setText(String.valueOf(teamworkRating));
        numWorkquality.setText(String.valueOf(workqualityRating));
        numAttitude.setText(String.valueOf(attitudeRating));
    }

    private void getRating() {
        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + searchedUsername + "/ratingProfile";
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

    public float connectionForGETRatingTesting(String url){
        return ForMockitoTest.connectionForGETRatingTesting(url);
    }
}
