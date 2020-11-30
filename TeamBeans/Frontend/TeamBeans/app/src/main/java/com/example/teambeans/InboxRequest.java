package com.example.teambeans;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Handles server requests for the Inbox class.
 * @author Kaitlyn Hoopingarner (kjh04)
 * @see Inbox
 */
public class InboxRequest {
    Context context;
    RequestQueue queue;

    /**
     * Constructs a new InboxRequest instance.
     * @param c Context of the Inbox fragment.
     */
    public InboxRequest(Context c) {
        context = c;
        queue = Volley.newRequestQueue(context);
    }

    /**
     * Creates and sends a request to the server to add a two user conversation to the database.
     * @param currentUser Username of the user currently logged in.
     * @param contactedUser Username of the recipient of the messages of the conversation.
     * @param messageDate Date that the first message was sent in database format.
     * @param message The first message of the conversation to be added.
     */
    public void addTwoUserConversation(final String currentUser, final String contactedUser, final String messageDate, final String message) {
        final String addConvoUrl = "http://10.24.226.25:8080/addConversation";

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestFuture<JSONObject> addConvoFuture = RequestFuture.newFuture();
                RequestFuture<JSONObject> addMessageFuture = RequestFuture.newFuture();
                JSONObject addConvoBody = new JSONObject();
                String convoId = null;

                try {
                    addConvoBody.put("username", currentUser);
                    addConvoBody.put("contactedUsername", contactedUser);
                    addConvoBody.put("date_created", messageDate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest addConvoRequest = new JsonObjectRequest(Request.Method.PUT, addConvoUrl, addConvoBody, addConvoFuture, addConvoFuture);
                queue.add(addConvoRequest);

                try {
                    JSONObject addConvoResponse = null;
                    while (addConvoResponse == null) {
                        try {
                            addConvoResponse = addConvoFuture.get(5, TimeUnit.SECONDS);
                            convoId = addConvoResponse.getString("conversationId");
//                            e.setId(convoId);

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ExecutionException e) {
                    System.out.println("EXECUTION ERR");
                } catch (TimeoutException e) {
                    System.out.println("TIMEOUT");
                }

//                RequestFuture<String> idFuture = RequestFuture.newFuture();
//                String rurl = "http://10.24.226.25:8080/returnMostRecentConversationID";
//                StringRequest r = new StringRequest(Request.Method.GET, rurl, idFuture, idFuture);
//                queue.add(r);

                addMessageToConversation(convoId, currentUser, message, messageDate);
            }
        });
        t.start();
    }

    /**
     * Creates and sends a request to the server to add a three user conversation to the database.
     * @param currentUser Username of the user currently logged in.
     * @param user2 Username of one of the recipients of the messages of the conversation.
     * @param user3 Username of one of the recipients of the messages of the conversation.
     * @param messageDate Date that the first message was sent in database format.
     * @param message The first message of the conversation to be added.
     */
    public void addThreeUserConversation(final String currentUser, String user2, String user3, final String messageDate, final String message) {
        String url = "http://10.24.226.25:8080/addConversationThree?username1=" + currentUser + "&username2=" + user2 + "&username3=" + user3 + "&date=" + messageDate;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response){
                    String id = null;
                    try {
                        id = response.getString("conversationId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    addMessageToConversation(id, currentUser, message, messageDate);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("ERROR " + error.getMessage());
                }
            });

        queue.add(request);
    }

    /**
     * Creates and sends a request to the server to add a three user conversation to the database.
     * @param currentUser Username of the user currently logged in.
     * @param user2 Username of one of the recipients of the messages of the conversation.
     * @param user3 Username of one of the recipients of the messages of the conversation.
     * @param user4 Username of one of the recipients of the messages of the conversation.
     * @param messageDate Date that the first message was sent in database format.
     * @param message The first message of the conversation to be added.
     */
    public void addFourUserConversation(final String currentUser, String user2, String user3, String user4, final String messageDate, final String message) {
        String url = "http://10.24.226.25:8080/addConversationFour?username1=" + currentUser + "&username2=" + user2 + "&username3=" + user3 + "&username4=" + user4 + "&date=" + messageDate;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        String id = null;
                        try {
                            id = response.getString("conversationId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        addMessageToConversation(id, currentUser, message, messageDate);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR " + error.getMessage());
                    }
                });

        queue.add(request);
    }

    /**
     * Creates and sends a request to the server to add a message to a conversation.
     * @param id Id of the conversation.
     * @param currentUser Username of the user currently logged in.
     * @param message Message to be added to the conversation.
     * @param messageDate Date the message to be added to the conversation was sent in database format.
     */
    public void addMessageToConversation(String id, String currentUser, String message, String messageDate) {
        final JSONObject body = new JSONObject();

        try {
            body.put("username", currentUser);
            body.put("conversationId", id);
            body.put("messageText", message);
            body.put("dateSent", messageDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://10.24.226.25:8080/addMessageToConversation";

        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR " + error.getMessage());
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return body.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        queue.add(request);
    }
}
