package coms309.teambeans.DTOs;

public class ContactDTO {

    private String username;

    private String contactUsername;

    public ContactDTO() {
    }

    public ContactDTO(String username, String contactUsername) {
        this.username = username;
        this.contactUsername = contactUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactUsername() {
        return contactUsername;
    }

    public void setContactUsername(String contactUsername) {
        this.contactUsername = contactUsername;
    }
}
