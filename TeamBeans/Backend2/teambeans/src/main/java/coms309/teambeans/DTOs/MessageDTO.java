package coms309.teambeans.DTOs;

import java.util.Date;

public class MessageDTO {
    String username;
    Long conversationId;
    String messageText;
    Date dateSent;

    public MessageDTO() {
    }

    public MessageDTO(String username, Long conversationId, String messageText, Date dateSent) {
        this.username = username;
        this.conversationId = conversationId;
        this.messageText = messageText;
        this.dateSent = dateSent;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
