package coms309.teambeans.ModelResponses;

import java.util.Date;

public class ConversationResponse {

    private String username;
    private String contactedUsername;
    private Date date_created;
    private Long conversationId;

    public ConversationResponse() {
    }

    public ConversationResponse(String username, String contactedUsername, Date date_created, Long conversationId) {
        this.username = username;
        this.contactedUsername = contactedUsername;
        this.date_created = date_created;
        this.conversationId = conversationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactedUsername() {
        return contactedUsername;
    }

    public void setContactedUsername(String contactedUsername) {
        this.contactedUsername = contactedUsername;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
}
