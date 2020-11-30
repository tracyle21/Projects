package coms309.teambeans.ModelResponses;

import java.util.List;

public class UserResponse {
    List<String> users;

    public UserResponse() {
    }

    public UserResponse(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
