package coms309.teambeans.ModelResponses;

import java.util.Date;

public class ConversationThreeResponse {

    private String username;
    private String username2;
    private String username3;
    private Date date_created;
    private Long conversationId;

    public ConversationThreeResponse() {
    }

    public ConversationThreeResponse(String username, String username2, String username3, Date date_created, Long conversationId) {
        this.username = username;
        this.username2 = username2;
        this.username3 = username3;
        this.date_created = date_created;
        this.conversationId = conversationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getUsername3() {
        return username3;
    }

    public void setUsername3(String username3) {
        this.username3 = username3;
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
