package com.example.teambeans;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatEnvironmentRequest {
    Context context;
    RequestQueue queue;

    public ChatEnvironmentRequest(Context c) {
        context = c;
        queue = Volley.newRequestQueue(context);
    }

    public void addMessage(String id, String from, String message, String messageDate) {
        final JSONObject body = new JSONObject();

        try {
            body.put("username", from);
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
