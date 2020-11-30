package coms309.teambeans.Services;

import coms309.teambeans.DTOs.UserLoginDTO;
import coms309.teambeans.DTOs.UserRegistrationDTO;
import coms309.teambeans.Models.User;
import coms309.teambeans.Repositories.CustomUserRepository;
import coms309.teambeans.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private UserRepository userRepository;

    public void addUser(UserRegistrationDTO userRegistrationDTO) {
        customUserRepository.save(userRegistrationDTO);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean login(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByUsernameAndPassword(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        if (user == null) {
            return false;
        }
        return true;
    }

}
