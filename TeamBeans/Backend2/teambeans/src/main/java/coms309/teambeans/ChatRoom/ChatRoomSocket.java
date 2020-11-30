package coms309.teambeans.ChatRoom;

import coms309.teambeans.Models.ChatRoomMessage;
import coms309.teambeans.Models.User;
import coms309.teambeans.Repositories.ChatRoomMessageRepository;
import coms309.teambeans.Repositories.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller //or is this component? or both?
//@Component
@ServerEndpoint(value = "/chatroom/{username}")
public class ChatRoomSocket {

    private static ChatRoomMessageRepository chatRoomMessageRepository;
    private static UserRepository userRepository;

    @Autowired
    public void setChatRoomMessageRepository(ChatRoomMessageRepository chatRoomMessageRepo) {
        chatRoomMessageRepository = chatRoomMessageRepo;
    }


    @Autowired
    public void setUserRepository(UserRepository userRepo) {
        userRepository = userRepo;
    }

    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(ChatRoomSocket.class); //?? had to cast to resolve an error here

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {

        logger.info("Entered into Open");

        // store connecting user information
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);

        //Send chat history to the newly connected user
        sendMessageToParticularUser(username, getChatHistory());

        // broadcast that new user joined
        String message = "User:" + username + " has joined the chat";
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        // Handle new messages
        logger.info("Entered into Message: Got Message:" + message);
        String username = sessionUsernameMap.get(session);

        // Direct message to a user using the format "@username <message>"
        if (message.startsWith("@")) {
            String destUsername = message.split(" ")[0].substring(1);

            // send the message to the sender and receiver
            sendMessageToParticularUser(destUsername, "[DM] " + username + ": " + message);
            sendMessageToParticularUser(username, "[DM] " + username + ": " + message);

        }
        else {
            broadcast(username + ": " + message);
        }

        // Saving chat history to repository
        User user = userRepository.findByUsername(username);
        LocalDateTime timeStamp = LocalDateTime.now();
        chatRoomMessageRepository.save(new ChatRoomMessage(user, timeStamp, message));
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        // remove the user connection information
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // broadcast that the user disconnected
        String message = username + " has disconnected from chat";
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }

    private void sendMessageToParticularUser(String username, String message) {
        try
        {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        }
        catch (IOException e)
        {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }

    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }

        });
    }

    private String getChatHistory() {
        List<ChatRoomMessage> chatRoomMessages = chatRoomMessageRepository.findAll();

        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        if(chatRoomMessages != null && chatRoomMessages.size() != 0) {
            for (ChatRoomMessage chatRoomMessage : chatRoomMessages) {
                sb.append(chatRoomMessage.getUserRoom().getUsername() + ": " + chatRoomMessage.getText() + "\n");
            }
        }
        return sb.toString();
    }

}
