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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;

/**
 * Provides functionality for the Inbox screen view and logic.
 * Uses the InboxRequest class to send requests to the server and the
 * Inbox Adapter class to populate the list of conversations on the screen.
 * @author Kaitlyn Hoopingarner (kjh04)
 * @see InboxRequest
 * @see InboxAdapter
 */
public class Inbox extends Fragment {
    InboxElement tempElement;
    RequestQueue queue;
    FloatingActionButton addConvoButton;
    FloatingActionButton refreshButton;
    public ArrayList<InboxElement> inbox = new ArrayList<InboxElement>();
    ArrayList<JSONObject> recipientsUserInfo = new ArrayList<JSONObject>();
    ArrayList<Integer> usersConversationIds = new ArrayList<Integer>();
    private String newConversationId;

    InboxAdapter inboxAdapter;
    private View inboxLayout;
    private View newConversationLayout;
    private int mScreen;
    private static final int INBOXSCREEN = 0;
    private static final int NEWCONVERSATIONSCREEN = 1;

    private Button sendButton;
    private Button cancelButton;
    private Button addRecipientButton;
    private EditText recipientsInput;
    private EditText newMessageInput;
    private ChipGroup recipientsList;
    private Chip chip;
    private String currentUser;
    private String currentUsersConversationIDs;

    private InboxRequest model;

    /**
     * Lifetime catch of screen, sets up the view of the screen and initializes data.
     * @param inflater Layout inflater of the screen.
     * @param container App view frame.
     * @param savedInstanceState Preliminary data for the screen.
     * @return Created view of the screen.
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.inbox, container, false);
        final Context activityContext = getActivity().getApplicationContext();
        final Context context = getContext();
        Context c = getContext();

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Inbox");

        Login login = new Login();
        model = new InboxRequest(context);

        currentUser = login.user;

        queue = Volley.newRequestQueue(context);

        inboxLayout = view.findViewById(R.id.conversationList);
        newConversationLayout = view.findViewById(R.id.newConvoLayout);

        addConvoButton = view.findViewById(R.id.addConversationButton);
        refreshButton = view.findViewById(R.id.refreshConvoButton);

        // New Conversation screen layout ui components
        sendButton = view.findViewById(R.id.sendButton);
        cancelButton = view.findViewById(R.id.cancelConvoButton);
        addRecipientButton = view.findViewById(R.id.addRecipientButton);
        recipientsInput = view.findViewById(R.id.to);
        newMessageInput = view.findViewById(R.id.message);
        recipientsList = view.findViewById(R.id.recipientsList);

        final Toast invalidRecipientToast = Toast.makeText(activityContext, "Invalid Recipient", Toast.LENGTH_SHORT);
        final Toast tooManyRecipientsToast = Toast.makeText(activityContext, "Only 3 Recipients Allowed", Toast.LENGTH_SHORT);
        final Toast userExistsToast = Toast.makeText(activityContext, "User is already a part of the conversation", Toast.LENGTH_SHORT);
        final Toast userDoesNotExistToast = Toast.makeText(activityContext, "User not found", Toast.LENGTH_SHORT);
        final Toast cannotAddSelfToast = Toast.makeText(activityContext, "Cannot add yourself to the conversation", Toast.LENGTH_SHORT);

        addRecipientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If no text is entered, display a toast message
                // If the recipient is the current user, display a toast message
                String input = recipientsInput.getText().toString();

                if (input.length() == 0) {
                    invalidRecipientToast.show();
                } else if (input.equals(currentUser)) {
                    cannotAddSelfToast.show();
                } else {
                    // Check that the text entered is the username of a valid user
                    String url = "http://10.24.226.25:8080/users/" + input;

                    // Request a string response from the provided URL
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    boolean valid = false;
                                    JSONObject userInfo = null;
                                    String recipientName = null;
                                    String recipientUsername = null;

                                    System.out.println("response " + response);

                                    try {
                                        userInfo = response.getJSONObject("userInformation");
                                        recipientName = userInfo.getString("firstName");
                                        recipientUsername = userInfo.getString("username");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    // Check if there are 3 users added to the conversation
                                    int numRecipients = recipientsList.getChildCount();

                                    if (numRecipients < 3) {
                                        // Check if that recipient is already in the conversation
                                        boolean exists = false;

                                        for (JSONObject i : recipientsUserInfo) {
                                            String curUsername = null;

                                            try {
                                                curUsername = i.getString("username");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            if (curUsername.equals(recipientUsername)) {
                                                exists = true;
                                                userExistsToast.show();
                                            }
                                        }

                                        if (!exists) {
                                            // Add the user to the chip group displaying the recipients' of the message names
                                            Chip chip = (Chip) inflater.inflate(R.layout.recipient_chip, null, false);
                                            chip.setText(recipientUsername);

                                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Chip c = view.findViewById(R.id.chip);
                                                    String name = c.getText().toString();
                                                    int indexToRemove = -1;
                                                    int index = 0;
                                                    for (JSONObject i : recipientsUserInfo) {
                                                        String cur = null;

                                                        try {
                                                            cur = i.getString("username");
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                        if (cur.equals(name)) {
                                                            indexToRemove = index;
                                                        }

                                                        index += 1;
                                                    }

                                                    recipientsUserInfo.remove(indexToRemove);
                                                    recipientsList.removeView(c);
                                                }
                                            });

                                            if (chip.getParent() != null) {
                                                ((ChipGroup) chip.getParent()).removeView(chip);
                                            }

                                            recipientsList.addView(chip);
                                            recipientsUserInfo.add(userInfo);
                                            valid = true;
                                        }
                                    } else {
                                        // Display a message to user
                                        tooManyRecipientsToast.show();
                                    }

                                    // Clear the recipientsInput, if recipient is valid
                                    if (valid) {
                                        recipientsInput.getText().clear();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error.getMessage() == null) {
                                        userDoesNotExistToast.show();
                                    }

                                    System.out.println("ERROR " + error.getMessage());
                                }
                            });


                    // Add the request to the RequestQueue
                    queue.add(request);
                }
            }
        });


        addConvoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renderNewConversationScreen();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current time
                String time = getCurrentDisplayTime();

                // Get data from screen
                String namesInput = recipientsInput.getText().toString();
                String messageInput = newMessageInput.getText().toString();

                // Create new InboxElement
                ArrayList<String> convoNames = new ArrayList<String>();

                for (JSONObject i : recipientsUserInfo) {
                    String curName = null;
                    try {
                        curName = i.getString("username");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    convoNames.add(curName);
                }

                String message = messageInput;


                InboxElement newElement = new InboxElement(convoNames, message, time);

                // Add InboxElement to inbox
                addToInbox(newElement);

                // Render inbox screen
                renderInboxScreen();

                // Clear new conversation inputs
                recipientsInput.getText().clear();
                newMessageInput.getText().clear();
                recipientsUserInfo.clear();
                recipientsList.removeAllViews();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Render inbox screen
                renderInboxScreen();

                // Clear new conversation inputs
                recipientsInput.getText().clear();
                newMessageInput.getText().clear();
                recipientsUserInfo.clear();
                recipientsList.removeAllViews();
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.conversationList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        inboxAdapter = new InboxAdapter(getActivity(), inbox);
        recyclerView.setAdapter(inboxAdapter);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Empty Inbox
                inbox.clear();
                inboxAdapter.notifyDataSetChanged();
                // Refresh list of conversations
                getUsersConversations();
                inboxAdapter.notifyDataSetChanged();
            }
        });

        // Get existing conversations and add them to the inbox
        getUsersConversations();
        inboxAdapter.notifyDataSetChanged();

        renderInboxScreen();
        return view;
    }

    /**
     * Adds a new conversation to the inbox.
     * Adds the new conversation to the list on the screen and the database.
     *
     * @param newElement InboxElement that contains data about the conversation that will be added to the inbox.
     */
    public void addToInbox(final InboxElement newElement) {
        // Add conversation to database
        final int numRecipients = newElement.getNumRecipients();
        JSONObject body = new JSONObject();
        final String[] recipientsList = newElement.getRecipientsList();
        final String messageDate = displayTimeToDatabaseTime(newElement.getTimeDisplay());

        // Add conversation to inbox screen
        final Thread t1 = new Thread(new Runnable() {
            @Override
            synchronized public void run() {
                if (numRecipients == 1) {
                    addTwoUserConversation(currentUser, recipientsList[0], messageDate, newElement.getMessage());
                }
                else if (numRecipients == 2) {
                    addThreeUserConversation(currentUser, recipientsList[0], recipientsList[1], messageDate, newElement.getMessage());
                }
                else if (numRecipients == 3) {
                    addFourUserConversation(currentUser, recipientsList[0], recipientsList[1], recipientsList[2], messageDate, newElement.getMessage());
                }
            }
        });
        final Thread t2 = new Thread(new Runnable() {
            @Override
            synchronized public void run() {
                RequestFuture<String> idFuture = RequestFuture.newFuture();
                String rurl = "http://10.24.226.25:8080/returnMostRecentConversationID";
                StringRequest r = new StringRequest(Request.Method.GET, rurl, idFuture, idFuture);
                queue.add(r);

                try {
                    String res = null;
                    while (res == null) {
                        try {
                            res = idFuture.get(5, TimeUnit.SECONDS);
                            newElement.setId(res);
//                            inbox.add(newElement);
                            new Handler(Looper.getMainLooper()).post(new Runnable(){
                                @Override
                                public void run() {
                                    inboxAdapter.notifyDataSetChanged();
                                }
                            });

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } catch (ExecutionException e) {
                    System.out.println("EXECUTION ERR");
                } catch (TimeoutException e) {
                    System.out.println("TIMEOUT");
                }
            }
        });

        final Thread t3 = new Thread(new Runnable() {
            @Override
            synchronized public void run() {
                // Empty Inbox
                inbox.clear();
                new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run() {
                        inboxAdapter.notifyDataSetChanged();
                    }
                });
                // Refresh list of conversations
                getUsersConversations();
                inboxAdapter.notifyDataSetChanged();
            }
        });

        Thread t = new Thread(new Runnable() {
            @Override
            synchronized public void run() {
                t1.start();
                t2.start();
                t3.start();
                try {
                    t1.join();
                    t2.join();
                    t3.join();
                } catch ( Exception e) {
                    System.out.println("Interrupted");
                }
            }
        });

        t.start();
    }

    /**
     * Displays the inbox screen which shows the list of the user's conversation.
     */
    private void renderInboxScreen() {
        mScreen = INBOXSCREEN;

        addConvoButton.show();
        refreshButton.show();
        inboxLayout.setVisibility(mScreen == INBOXSCREEN ? View.VISIBLE : View.GONE);
        newConversationLayout.setVisibility(mScreen == NEWCONVERSATIONSCREEN ? View.VISIBLE : View.GONE);
    }

    public void sortByTime() {
        Collections.sort(inbox);
    }

    /**
     * Displays the new conversation screen where the user can start a new conversation.
     */
    private void renderNewConversationScreen() {
        mScreen = NEWCONVERSATIONSCREEN;

        addConvoButton.hide();
        refreshButton.hide();
        inboxLayout.setVisibility(mScreen == INBOXSCREEN ? View.VISIBLE : View.GONE);
        newConversationLayout.setVisibility(mScreen == NEWCONVERSATIONSCREEN ? View.VISIBLE : View.GONE);
    }

    /**
     * Adds a conversation with two users to the database.
     * @param user1 The username of the user currently logged in.
     * @param user2 The username of the recipient of the messages in the conversation.
     * @param messageDate The date of the first message in database format.
     * @param firstMessage The first message of the conversation.
     */
    public void addTwoUserConversation(String user1, String user2, String messageDate, String firstMessage) {
        // Check if date is in right format
        if (messageDate.indexOf("T") != -1) {
            // Call server to add a two user conversation
            model.addTwoUserConversation(user1, user2, messageDate, firstMessage);
        }
    }

    /**
     * Adds a conversation with three users to the database.
     * @param user1 The username of the user currently logged in.
     * @param user2 A username of a recipient of the messages in the conversation.
     * @param user3 A username of a recipient of the messages in the conversation.
     * @param dateSent The date of the first message in database format.
     * @param firstMessage The first message of the conversation.
     */
    public void addThreeUserConversation(String user1, String user2, String user3, String dateSent, String firstMessage) {
        // Check if date is in right format
        if (dateSent.indexOf("T") != -1) {
            // Call server to add a three user conversation
            model.addThreeUserConversation(user1, user2, user3, dateSent, firstMessage);
        }
    }

    /**
     * Adds a conversation with three users to the database.
     * @param user1 The username of the user currently logged in.
     * @param user2 A username of a recipient of the messages in the conversation.
     * @param user3 A username of a recipient of the messages in the conversation.
     * @param user4 A username of a recipient of the messages in the conversation.
     * @param dateSent The date of the first message in database format.
     * @param firstMessage The first message of the conversation.
     */
    public void addFourUserConversation(String user1, String user2, String user3, String user4, String dateSent, String firstMessage) {
        // Check if date is in right format
        if (dateSent.indexOf("T") != -1) {
            // Call server to add a four user conversation
            model.addFourUserConversation(user1, user2, user3, user4, dateSent, firstMessage);
        }
    }

    /**
     * Gets the conversation data stored in the database for the currently logged in user.
     */
    private void getUsersConversations() {
        final ArrayList<String> conversationIDs = new ArrayList<String>();

        String url = "http://10.24.226.25:8080/getConversationIdForUser?username=" + currentUser;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        System.out.println("Response: " + response);
                        JSONArray convoIds = null;

                        try {
                            convoIds = response.getJSONArray("conversationIDs");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for (int i=0; i < convoIds.length(); i++) {
                            String curId = null;

                            try {
                                curId = Integer.toString((Integer) convoIds.get(i));
                                System.out.println("curId: " + curId);
                                // Get the users in a conversation
                                final ArrayList<String> u = new ArrayList<>();
                                final String[] m = new String[1];
                                final String[] ds = new String[1];
                                final String url1 = "http://10.24.226.25:8080/getUsersInConversationsFromId?id=" + curId;
                                final String url2 = "http://10.24.226.25:8080/getMessagesForConversation?conversationId=" + curId;
                                final String finalCurId = curId;
                                Thread t = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Get the users in a conversation
                                        RequestFuture<JSONObject> future = RequestFuture.newFuture();
                                        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url1, null, future, future);
                                        queue.add(request1);

                                        try {
                                            JSONObject response = null;
                                            while (response == null) {
                                                try {
                                                    response = future.get(5, TimeUnit.SECONDS);
                                                } catch (InterruptedException e) {
                                                    Thread.currentThread().interrupt();
                                                }
                                            }

                                            System.out.println("R " + response);
                                            JSONArray x = null;
                                            try {
                                                x = response.getJSONArray("users");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            for (int i=0; i < x.length(); i++) {
                                                try {
                                                    u.add((String) x.get(i));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } catch (ExecutionException e) {
                                            System.out.println("EXECUTION ERR");
                                        } catch (TimeoutException e) {
                                            System.out.println("TIMEOUT");
                                        }

                                        System.out.println("u" + u);

                                        // Get the latest message in a conversation
                                        future = RequestFuture.newFuture();
                                        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url2, null, future, future);
                                        queue.add(request2);

                                        try {
                                            JSONObject response2 = null;
                                            while (response2 == null) {
                                                try {
                                                    response2 = future.get(5, TimeUnit.SECONDS);
                                                } catch (InterruptedException e) {
                                                    Thread.currentThread().interrupt();
                                                }
                                            }

                                            System.out.println("R2 " + response2);
                                            JSONArray list = response2.getJSONArray("messageList");
                                            JSONObject item = (JSONObject) list.get(0);
                                            m[0] = item.getString("messageText");
                                            ds[0] = item.getString("dateSent");

                                            // Add element to inbox
                                            // Create new InboxElement
                                            InboxElement newElement = new InboxElement(u, m[0], databaseToDisplayTime(ds[0]));
                                            newElement.setId(finalCurId);

                                            // Add InboxElement to inbox
                                            inbox.add(newElement);
                                            sortByTime();

                                            new Handler(Looper.getMainLooper()).post(new Runnable(){
                                                @Override
                                                public void run() {
                                                    inboxAdapter.notifyDataSetChanged();
                                                }
                                            });
                                        } catch (ExecutionException e) {
                                            System.out.println("EXECUTION ERR");
                                        } catch (TimeoutException e) {
                                            System.out.println("TIMEOUT");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                t.start();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

    /**
     * Converts the display time format to the database time format.
     * @param time The time in display format.
     * @return The time in database format.
     */
    public String displayTimeToDatabaseTime(String time) {
        String databaseTime = "";
        String t = time.substring(0,8);
        String m = time.substring(9, 11);
        String d = time.substring(12, 14);
        String y = time.substring(15);

        databaseTime = y + "-" + m + "-" + d + "T" + t + ".000+00:00";

        return databaseTime;
    }

    /**
     * Converts the database time format to the display time format.
     * @param time The time in database format.
     * @return The time in display format.
     */
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

    /**
     * Gets the current time.
     * @return The current time in display format.
     */
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

    // Testing helper methods

    /**
     * Sets the class instance that contains the server requests.
     * @param m Instance of the InboxRequest class.
     */
    public void setModel(InboxRequest m) { this.model = m;}

    /**
     * Sets the username of the current logged in user.
     * @param username Username of the current logged in user.
     */
    public void setCurrentUser(String username) {this.currentUser = username;}

    /**
     * Gets the username of the current logged in user.
     * @return Username of the current logged in user.
     */
    public String getCurrentUser() {return this.currentUser;}

    /**
     * Gets the list of data for the inbox.
     * @return ArrayList of data elements of the inbox.
     */
    public ArrayList<InboxElement> getInbox() {return this.inbox;}

    /**
     * Stores data for an item (conversation) in the inbox.
     */
    static class InboxElement implements Comparable<Inbox.InboxElement> {
        private ArrayList<String> names;
        private String message;
        private String time;
        private String id;

        /**
         * Constructs a new InboxElement.
         * @param names Recipients of the messages in the conversation.
         * @param message The most recent message of the conversation.
         * @param time The date of the most recent message in display format.
         */
        public InboxElement(ArrayList<String> names, String message, String time) {
            this.names = names;
            this.message = message;
            this.time = time;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String[] getRecipientsList() {
            String recipients[] = new String[names.size()];

            for (int i=0; i < names.size(); i++) {
                recipients[i] = names.get(i);
            }

            return recipients;
        }

        /**
         * Gets the number of recipients in a conversation.
         * @return Number of recipients in a conversation.
         */
        public int getNumRecipients() {
            return names.size();
        }

        /**
         * Gets the display format (String) of the names of the recipients of the messages in the conversation.
         * @return Names of recipients of the conversation messages in display format.
         */
        public String getNamesDisplay() {
            String displayValue = "";

            for (int i=0; i < names.size(); i++) {
                if (i == names.size() - 1) {
                    displayValue += names.get(i);
                } else {
                    displayValue += names.get(i) + ", ";
                }
            }

            return displayValue;
        }

        /**
         * Gets the most recent message of the conversation.
         * @return The most recent message of the conversation.
         */
        public String getMessage() {
            return message;
        }

        /**
         * Sets the message for the conversation.
         * Should store the most recent message in the conversation.
         * @param newMessage Message to be stored in the data and appear as the message preview for the conversation.
         */
        public void setMessage(String newMessage) { message = newMessage; }

        /**
         * Gets the preview of the message.
         * @return The first 30 characters of the messages as a preview for display.
         */
        public String getMessagePreviewDisplay() {
            if (message.length() > 29) {
                return message.substring(0, 29) + "...";
            }
            else {
                return message;
            }
        }

        /**
         * Gets the time the most recent message was sent in display format.
         * @return
         */
        public String getTimeDisplay() {
            return time;
        }

        /**
         * Compares the time sent of a conversation.
         * @param m Message for another message to be compared with.
         * @return 1 if caller is greater than m, -1 if caller is less than m, 0 if they are equal.
         */
        @Override
        public int compareTo(Inbox.InboxElement m) {
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

            String mTime = m.getTimeDisplay();
            String curTime = this.getTimeDisplay();

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


