package com.example.teambeans;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Provides websocket communication functionality.
 * @author Kaitlyn Hoopingarner
 */
public class WebSocket {
    private WebSocketClient mWebSocketClient;
    private EditText mInput;
    private TextView mOutput;
    private String username;

    /**
     * Constructs a new WebSocket instance.
     * @param username Username of the user currently logged in.
     * @param v View of the layout of the screen using Websocket.
     * @param inputId Id of the EditText Android Studio UI element that sends messages.
     * @param outputId Id of the TextView Android Studio UI element that displays messages.
     */
    public WebSocket(String username, View v, int inputId, int outputId) {
        this.username = username;
        mOutput = v.findViewById(outputId);
        connectWebSocket();
    }

    /**
     * Establishes a connection to the websocket and handlers for message send, recieve, and error events.
     */
    private void connectWebSocket() {
        URI uri;
        try {
            /*
             * To test the clientside without the backend, simply connect to an echo server such as:
             *  "ws://echo.websocket.org"
             */
            uri = new URI("ws://10.24.226.25:8080/chatroom/"+username); // 10.0.2.2 = localhost
            // uri = new URI("ws://echo.websocket.org");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
            }

            @Override
            public void onMessage(String msg) {
                Log.i("Websocket", "Message Received");
                // Appends the message received to the previous messages
                mOutput.append("\n" + msg);
            }

            @Override
            public void onClose(int errorCode, String reason, boolean remote) {
                Log.i("Websocket", "Closed " + reason);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };

        mWebSocketClient.connect();
    }

    /**
     * Closes the websocket connection.
     */
    public void closeWebSocket() {
        mWebSocketClient.close();
    }

    /**
     * Sends a message to the websocket.
     * @param message Message to be sent.
     */
    public void sendMessage(String message) {
        mWebSocketClient.send(message);
    }
}
