package coms309.teambeans.ModelResponses;

import coms309.teambeans.DTOs.UserRegistrationDTO;

import java.util.List;

public class GetUserResponse {
    UserRegistrationDTO userInformation;

    public GetUserResponse() {
    }

    public GetUserResponse(UserRegistrationDTO userInformation) {
        this.userInformation = userInformation;
    }

    public UserRegistrationDTO getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserRegistrationDTO userInformation) {
        this.userInformation = userInformation;
    }
}
