package coms309.teambeans.DTOs;

import java.sql.Blob;

public class UserProfilePictureDTO {

    private String username;
    private String profilePicture;

    public UserProfilePictureDTO() {
    }

    public UserProfilePictureDTO(String username, String profilePicture) {
        this.username = username;
        this.profilePicture = profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
