package com.example.teambeans;

import android.content.Context;
import android.widget.Toast;

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

/**
 * This class is soley for mockito testing
 * @author Renee
 */
public class ForMockitoTest {
    Context context;
    RequestQueue queue;
    float overallRating;
    float communicationRating;
    float teamworkRating;
    float workqualityRating;
    float attitudeRating;
    int totalReviewed;

    /**
     * Constructor ForMockitoTest
     * @param c The context of the view
     */
    public ForMockitoTest(Context c) {
        context = c;
        queue = Volley.newRequestQueue(context);
    }

    /**
     * This method is written soley for mockito test (GET).
     * @param url the url for JSON request (GET)
     */
    public void connectionfForGETTesting(String url) {
        final StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("Mockito response for GET request: " + response);
                           // String responseC = response;

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
        queue.add(jsonObjectRequest);

    }
    /**
     * This method is written soley for mockito test (POST).
     * @param username username of the login user
     * @param url the url for JSON request
     * @param currentText the text that wanted to be deleted/post
     */
    public void connectionForPOSTTesting(final String username, String url, final String currentText) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Response delete" + response);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("name", currentText);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    /**
     * This method is written soley for mockito test (PUT).
     * @param username username of the login user
     * @param url the url for JSON request
     * @param currentText the text that wanted to be deleted/post
     * @param status The type of request
     * @param connect Contains the name for the request(language, description, class taken, current class)
     */
    public void connectionForPUTTesting(final String username, String url, final String currentText, int status, String connect) {
        //System.out.println("TESTING HERE:" + getBeforeSpace("COMS 309")+"|sec|"+ getAfterSpace("COMS 309"));
        EditUserProfile editUserProfile = new EditUserProfile();
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", username);
            if (connect.equals("language")) {
                obj.put("name", currentText);
            } else if (connect.equals("description")) {
                obj.put("description", currentText);
            } else if (connect.equals("classTaken") || connect.equals("currentClass")||connect.equals("courseAssist")||connect.equals("courseTeaching")) {
                obj.put("majorName", editUserProfile.getBeforeSpace(currentText));
                obj.put("number", editUserProfile.getAfterSpace(currentText));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Request a string response from the provided URL.
        final JsonObjectRequest request = new JsonObjectRequest(status, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("Response put " + response.toString());
                        } catch (Error e) {
                            System.out.println("ERROR IN RESPONSE: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR " + error.getMessage());
                    }
                });
        // Add the request to the RequestQueue
        queue.add(request);
    }

    public float connectionForGETRatingTesting(String url) {
       // String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + searchedUsername + "/ratingProfile";
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
        return teamworkRating;
    }

    public void connectionForPUTRatingTesting(final String username, final String role, String url) {
       // String url = "http://coms-309-rp-09.cs.iastate.edu:8080/averageOutRatingProfile";

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
                params.put("username", username);
                if (role.equals("STUDENT")) {
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
}
