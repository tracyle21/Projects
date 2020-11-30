package coms309.teambeans.DTOs;

public class UserDescriptionDTO {

    private String username;
    private String description;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserDescriptionDTO() {
    }

    public UserDescriptionDTO(String username, String description) {
        this.username = username;
        this.description = description;
    }
}
