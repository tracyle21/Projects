package coms309.teambeans.DTOs;

import java.util.Date;

public class ConversationDTO { //for one on one conversation

    private String username;
    private String contactedUsername;
    private Date date_created;

    public ConversationDTO() {
    }

    public ConversationDTO(String username, String contactedUsername, Date date_created) {
        this.username = username;
        this.contactedUsername = contactedUsername;
        this.date_created = date_created;
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
}
