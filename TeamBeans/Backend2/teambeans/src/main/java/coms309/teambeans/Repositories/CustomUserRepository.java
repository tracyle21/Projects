package coms309.teambeans.Repositories;

import coms309.teambeans.DTOs.UserRegistrationDTO;
import coms309.teambeans.Models.Course;
import coms309.teambeans.Models.RatingProfile;
import coms309.teambeans.Models.Role;
import coms309.teambeans.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository("customUserRepository")
public class CustomUserRepository {

    @Autowired
    private UserRepository userRepository;

    public User save(UserRegistrationDTO userRegistrationDTO) {
        User u = new User(userRegistrationDTO.getFirstName(), userRegistrationDTO.getLastName(), userRegistrationDTO.getEmail(), userRegistrationDTO.getUsername(), userRegistrationDTO.getPassword(),
                Arrays.asList(new Role(userRegistrationDTO.getRole())), null, null, null, null, null, null, null, null,
                new RatingProfile(0.0, 0.0, 0.0, 0.0, 0.0, 0), null, 0.0, 0.0, 0.0, 0.0, 0.0);
        return userRepository.save(u);
    }
}
