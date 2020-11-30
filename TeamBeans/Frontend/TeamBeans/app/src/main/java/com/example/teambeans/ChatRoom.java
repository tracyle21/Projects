package com.example.teambeans;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Provides functionality logic and view of the chat room screen.
 * @author Kaitlyn Hoopingarner
 */
public class ChatRoom extends Fragment {
    private WebSocket socket;
    private TextView messageOutput;
    private EditText messageInput;
    private Button sendBtn;

    /**
     * Lifetime catch of screen, sets up the view of the screen and initializes data.
     * Opens connection to the websocket and uses the WebSocket class to send and receive messages.
     * @param inflater Layout inflater of the screen.
     * @param container App view frame.
     * @param savedInstanceState Preliminary data for the screen.
     * @return Created view of the screen.
     * @see WebSocket
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.chat_room, container, false);
        Login login = new Login();
        String username = login.user;

        socket = new WebSocket(username, view, R.id.editChatMessage, R.id.m_output);

        messageInput = view.findViewById(R.id.editChatMessage);
        messageOutput = view.findViewById(R.id.m_output);
        sendBtn = view.findViewById(R.id.sendChatMessageButton);

        messageOutput.setMovementMethod(new ScrollingMovementMethod());

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the message from the input
                String message = messageInput.getText().toString();

                // If the message is not empty, send the message
                if(message != null && message.length() > 0){
                    socket.sendMessage(message);
                }
            }
        });


        return view;
    }

    /**
     * Lifetime catch for when the screen is left.
     * Closes connection to the websocket.
     */
    @Override
    public void onStop() {
        super.onStop();
        socket.closeWebSocket();
    }

    /**
     * Lifetime catch for when the screen is destroyed.
     * Closes connection to the websocket.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.closeWebSocket();
    }

}
