package com.example.teambeans;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ChatEnvironment extends Fragment {
    RequestQueue queue;
    private ChatEnvironmentRequest model;
    private MessageAdapter messageAdapter;
    private FloatingActionButton toInboxButton;
    private FloatingActionButton refreshButton;
    private EditText editMessageBox;
    private Button sendMessageButton;
    public ArrayList<ChatEnvironment.Message> messages;
    private String conversationId;
    private String currentUser;

    /**
     * Lifetime catch of screen, sets up the view of the screen and initializes data.
     * @param inflater Layout inflater of the screen.
     * @param container App view frame.
     * @param savedInstanceState Preliminary data for the screen.
     * @return Created view of the screen.
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.chat_environment, container, false);
        final Context context = getContext();
        conversationId = getArguments().getString("conversationId");
        Login login = new Login();
        currentUser = login.user;
        queue = Volley.newRequestQueue(context);

        String chatTitle = getArguments().getString("Title");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(chatTitle);

        model = new ChatEnvironmentRequest(context);

        messages = new ArrayList<>();

        toInboxButton = view.findViewById(R.id.backToInboxButton);
        sendMessageButton = view.findViewById(R.id.sendMessageButton);
        refreshButton = view.findViewById(R.id.refreshMessagesButton);
        editMessageBox = view.findViewById(R.id.editTextMessage);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Send Button Clicked");
                // Get current time
                String time = getCurrentDisplayTime();

                // Get data from screen
                String text = (String) editMessageBox.getText().toString();
                String from = currentUser;

                // Create new message
                Message newMessage = new Message(text, from, time);

                addMessageToMessages(newMessage);
            }
        });

        toInboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment nextFragment = new Inbox();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, nextFragment).addToBackStack(null).commit();
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.messageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(getActivity(), messages);
        recyclerView.setAdapter(messageAdapter);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Empty messages
                messages.clear();
                messageAdapter.notifyDataSetChanged();
                // Refresh list of conversations
                getMessages();
                messageAdapter.notifyDataSetChanged();
            }
        });

        // Populate message list with messages
        getMessages();

        return view;
    }

    public void addMessageToMessages(ChatEnvironment.Message m) {
        if (m.getFrom() != null && m.getTime() != null && m.getMessage() != null) {
            // Add message to the list of messages
            messages.add(m);
            sortByTime();
            // Save message to the database
            String t = displayTimeToDatabaseTime(m.getTime());
            if (t.indexOf("T") != -1) {
                model.addMessage(conversationId, m.getFrom(), m.getMessage(), t);
            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void setModel(ChatEnvironmentRequest m) { this.model = m;}
    public void setCurrentUser(String username) {this.currentUser = username;}

    private void getMessages() {
        String url = "http://10.24.226.25:8080/getMessagesForConversation?conversationId=" + conversationId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Get All Messages Response:" + response);
                        JSONArray messageList;
                        String messageFrom;
                        String messageText;
                        String messageTime;
                        try {
                            messageList = response.getJSONArray("messageList");

                            for (int i=0; i < messageList.length(); i++) {
                                JSONObject curMessageData = (JSONObject) messageList.get(i);
                                messageFrom = curMessageData.getString("username");
                                messageText = curMessageData.getString("messageText");
                                messageTime = databaseToDisplayTime(curMessageData.getString("dateSent"));

                                Message messageToAdd = new Message(messageText, messageFrom, messageTime);

                                messages.add(messageToAdd);
                                sortByTime();

                                new Handler(Looper.getMainLooper()).post(new Runnable(){
                                    @Override
                                    public void run() {
                                        messageAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
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

        queue.add(request);
    }

    public void sortByTime() {
        Collections.sort(messages, Collections.reverseOrder());
    }

    public String displayTimeToDatabaseTime(String time) {
        String databaseTime = "";
        String t = time.substring(0,8);
        String m = time.substring(9, 11);
        String d = time.substring(12, 14);
        String y = time.substring(15);

        databaseTime = y + "-" + m + "-" + d + "T" + t + ".000+00:00";

        return databaseTime;
    }

    public String databaseToDisplayTime(String time) {
        if (time.equals(null)) {
            return null;
        }
        String display = "";
        System.out.println("time " + time);
        String year = time.substring(0,4);
        String numMonth = time.substring(5,7);
        String numDay = time.substring(8,10);
        String timeSent = time.substring(11,19);

        display = timeSent + " " + numMonth + "-" + numDay + "-" + year;

        return display;
    }

    public String getCurrentDisplayTime() {
        String display = "";
        Calendar c = Calendar.getInstance();
        String time = c.getTime().toString();
        String monthText = time.substring(4, 7).trim();
        String monthNum = null;

        if (monthText.equals("Jan")) {
            monthNum = "01";
        }
        else if (monthText.equals("Feb")) {
            monthNum = "02";
        }
        else if (monthText.equals("Mar")) {
            monthNum = "03";
        }
        else if (monthText.equals("Apr")) {
            monthNum = "04";
        }
        else if (monthText.equals("May")) {
            monthNum = "05";
        }
        else if (monthText.equals("Jun")) {
            monthNum = "06";
        }
        else if (monthText.equals("Jul")) {
            monthNum = "07";
        }
        else if (monthText.equals("Aug")) {
            monthNum = "08";
        }
        else if (monthText.equals("Sep")) {
            monthNum = "09";
        }
        else if (monthText.equals("Oct")) {
            monthNum = "10";
        }
        else if (monthText.equals("Nov")) {
            monthNum = "11";
        }
        else if (monthText.equals("Dec")) {
            monthNum = "12";
        }

        String year = time.substring(24);
        String day = time.substring(8, 10);
        String t = time.substring(11,19);

        display = t + " " + monthNum + "-" + day + "-" + year;

        return display;
    }

    static class Message  implements Comparable<Message> {
        private String message;
        private String from;
        private String time;

        public Message(String message, String from, String time) {
            this.message = message;
            this.from = from;
            this.time = time;
        }

        public String getMessage() {
            return message;
        }

        public String getFrom() {
            return from;
        }

        public String getTime() {
            return time;
        }

        /**
         * Compares the time sent of a message.
         * @param m Message for another message to be compared with.
         * @return 1 if caller is greater than m, -1 if caller is less than m, 0 if they are equal.
         */
        @Override
        public int compareTo(Message m) {
            int mYear;
            int mDay;
            int mMonth;
            int mHour;
            int mMin;
            int mSec;
            int curYear;
            int curDay;
            int curMonth;
            int curHour;
            int curMin;
            int curSec;

            String mTime = m.getTime();
            String curTime = this.getTime();

            mYear = Integer.parseInt(mTime.substring(15));
            mDay = Integer.parseInt(mTime.substring(12,14));
            mMonth = Integer.parseInt(mTime.substring(9, 11));
            mHour = Integer.parseInt(mTime.substring(0, 2));
            mMin = Integer.parseInt(mTime.substring(3, 5));
            mSec = Integer.parseInt(mTime.substring(6, 8));

            curYear = Integer.parseInt(curTime.substring(15));
            curDay = Integer.parseInt(curTime.substring(12,14));
            curMonth = Integer.parseInt(curTime.substring(9, 11));
            curHour = Integer.parseInt(curTime.substring(0, 2));
            curMin = Integer.parseInt(curTime.substring(3, 5));
            curSec = Integer.parseInt(curTime.substring(6, 8));

            // Check if times are different
            if (curYear > mYear) {
                return 1;
            }
            else if (curYear < mYear) {
                return -1;
            }
            else {
                if (curMonth > mMonth) {
                    return 1;
                }
                else if (curMonth < mMonth) {
                    return -1;
                }
                else {
                    if (curDay > mDay) {
                        return 1;
                    }
                    else if (curDay < mDay) {
                        return -1;
                    }
                    else {
                        if (curHour > mHour) {
                            return 1;
                        }
                        else if (curHour < mHour) {
                            return -1;
                        }
                        else {
                            if (curMin > mMin) {
                                return 1;
                            }
                            else if (curMin < mMin) {
                                return -1;
                            }
                            else {
                                if (curSec > mSec) {
                                    return 1;
                                }
                                else if (curSec < mSec) {
                                    return -1;
                                }
                                else {
                                    return 0;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
