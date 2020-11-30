package coms309.teambeans.Controllers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import ch.qos.logback.core.encoder.EchoEncoder;
import coms309.teambeans.DTOs.*;
import coms309.teambeans.ModelResponses.*;
import coms309.teambeans.Models.*;
import coms309.teambeans.Repositories.*;
import coms309.teambeans.Services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@Api(value = "UserController", tags = {"User API"})
@SwaggerDefinition(tags = {
        @Tag(name = "User API", description = "Admin interface to manage changes/updates for a user")
})
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @ApiOperation(value = "Get overview of a specific user")
    @RequestMapping("/users/{username}")
    public GetUserResponse getUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        Collection<Role> roles = user.getRoles();
        String userRole = "";
        for(Role role : roles) {
            userRole = role.getName();
        }
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername(), user.getPassword(), userRole);
        GetUserResponse getUserResponse = new GetUserResponse(userRegistrationDTO);
        return getUserResponse;
    }

    @ApiOperation(value = "Register a new user")
    @RequestMapping(method = RequestMethod.POST, value = "/users")
    public UserRegistrationDTO registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        try {
            userService.addUser(userRegistrationDTO);
            return userRegistrationDTO;
        } catch (Exception e) {
            return null;
        }

    }

    @ApiOperation(value = "Login a User")
    @PostMapping("/login")
    public UserLoginDTO login(@RequestBody UserLoginDTO userLoginDTO) {
        return userService.login(userLoginDTO) ? userLoginDTO : null;
    }

    @ApiOperation(value = "Add a course as previously taken for a user")
    @PutMapping("/addOldCourse")
    public CourseDTO addOldCourse(@RequestBody CourseDTO courseDTO) {
        Course c = courseRepository.findByMajorNameAndNumber(courseDTO.getMajorName(), courseDTO.getNumber());
        User userToUpdate = userRepository.findByUsername(courseDTO.getUsername());

        Collection<Course> updatedList = userToUpdate.getCourseOldList();
        updatedList.add(c);
        userToUpdate.setCourseOldList(updatedList);
        userRepository.save(userToUpdate);
        return courseDTO;
    }

    @ApiOperation(value = "Delete a course from previously taken for a user")
    @PostMapping("/deleteOldCourse")
    public String deleteOldCourse(@RequestParam String username, @RequestParam String majorName, @RequestParam Integer number) {
        try {
            Course course = courseRepository.findByMajorNameAndNumber(majorName, number);
            User userToUpdate = userRepository.findByUsername(username);
            Collection<Course> listToDelete = userToUpdate.getCourseOldList();
            listToDelete.remove(course);
            userToUpdate.setCourseOldList(listToDelete);
            userRepository.save(userToUpdate);
            return "Success";
        } catch (Exception e) {
            return "Failure";
        }
    }

    @ApiOperation(value = "Add a course as currently taken for a user")
    @PutMapping("/addCurrentCourse")
    public CourseDTO addCurrentCourse(@RequestBody CourseDTO courseDTO) {
        Course c = courseRepository.findByMajorNameAndNumber(courseDTO.getMajorName(), courseDTO.getNumber());
        User userToUpdate = userRepository.findByUsername(courseDTO.getUsername());

        Collection<Course> updatedList = userToUpdate.getCourseCurrentList();
        updatedList.add(c);
        userToUpdate.setCourseCurrentList(updatedList);
        userRepository.save(userToUpdate);
        return courseDTO;
    }

    @ApiOperation(value = "Delete a course from currently taken for a user")
    @PostMapping("/deleteCurrentCourse")
    public String deleteCurrentCourse(@RequestParam String username, @RequestParam String majorName, @RequestParam Integer number) {
        try {
            Course course = courseRepository.findByMajorNameAndNumber(majorName, number);
            User userToUpdate = userRepository.findByUsername(username);

            Collection<Course> listToDelete = userToUpdate.getCourseCurrentList();
            listToDelete.remove(course);
            userToUpdate.setCourseCurrentList(listToDelete);
            userRepository.save(userToUpdate);
            return "Success";
        } catch (Exception e) {
            return "Failure";
        }
    }

    @ApiOperation(value = "Add a language as known for a user")
    @PutMapping("/addLanguage")
    public LanguageDTO addLanguage(@RequestBody LanguageDTO languageDTO) {
        Language language = languageRepository.findByName(languageDTO.getName());
        User userToUpdate = userRepository.findByUsername(languageDTO.getUsername());

        Collection<Language> updatedList = userToUpdate.getLanguages();
        updatedList.add(language);
        userToUpdate.setLanguages(updatedList);
        userRepository.save(userToUpdate);
        return languageDTO;
    }

    @ApiOperation(value = "Delete a language as a known language for a user")
    @PostMapping("/deleteLanguage")
    public String deleteLanguage(@RequestParam String username, @RequestParam String name) {
        try {
            Language language = languageRepository.findByName(name);
            User userToUpdate = userRepository.findByUsername(username);

            Collection<Language> listToDelete = userToUpdate.getLanguages();
            listToDelete.remove(language);
            userToUpdate.setLanguages(listToDelete);
            userRepository.save(userToUpdate);
            return "Success";
        } catch (Exception e) {
            return "Failure";
        }
    }

    @ApiOperation(value = "Add a user description for a user")
    @PutMapping("/addUserDescription")
    public UserDescriptionDTO addUserDescription(@RequestBody UserDescriptionDTO userDescriptionDTO) {
        User userToUpdate = userRepository.findByUsername(userDescriptionDTO.getUsername());
        userToUpdate.setDescription(userDescriptionDTO.getDescription());
        userRepository.save(userToUpdate);
        return userDescriptionDTO;
    }

    @ApiOperation(value = "Update to a new password for a user")
    @PutMapping("/updatePassword")
    public UpdatePasswordDTO updatePasswordString(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        User userToUpdate = userRepository.findByUsername(updatePasswordDTO.getUsername());
        if(userToUpdate.getPassword().equals(updatePasswordDTO.getOldPassword())) {
            userToUpdate.setPassword(updatePasswordDTO.getNewPassword());
            userRepository.save(userToUpdate);
            return updatePasswordDTO;
        }
        return null;
    }

    @ApiOperation(value = "Add a course as currently teaching for a user")
    @PutMapping("/addTeachingCourse")
    public CourseDTO addTeachingCourse(@RequestBody CourseDTO courseDTO) {
        Course c = courseRepository.findByMajorNameAndNumber(courseDTO.getMajorName(), courseDTO.getNumber());
        User userToUpdate = userRepository.findByUsername(courseDTO.getUsername());

        Collection<Course> updatedList = userToUpdate.getCoursesTeaching();
        updatedList.add(c);
        userToUpdate.setCoursesTeaching(updatedList);
        userRepository.save(userToUpdate);
        return courseDTO;
    }

    @ApiOperation(value = "Delete a course from currently teaching for a user")
    @PostMapping("/deleteTeachingCourse")
    public String deleteTeachingCourse(@RequestParam String username, @RequestParam String majorName, @RequestParam Integer number) {
        try {
            Course course = courseRepository.findByMajorNameAndNumber(majorName, number);
            User userToUpdate = userRepository.findByUsername(username);

            Collection<Course> listToDelete = userToUpdate.getCoursesTeaching();
            listToDelete.remove(course);
            userToUpdate.setCoursesTeaching(listToDelete);
            userRepository.save(userToUpdate);
            return "Success";
        } catch (Exception e) {
            return "Failure";
        }
    }

    @ApiOperation(value = "Add a course as currently assisting for a user")
    @PutMapping("/addAssistingCourse")
    public CourseDTO addAssistingCourse(@RequestBody CourseDTO courseDTO) {
        Course c = courseRepository.findByMajorNameAndNumber(courseDTO.getMajorName(), courseDTO.getNumber());
        User userToUpdate = userRepository.findByUsername(courseDTO.getUsername());

        Collection<Course> updatedList = userToUpdate.getCoursesAssisting();
        updatedList.add(c);
        userToUpdate.setCoursesAssisting(updatedList);
        userRepository.save(userToUpdate);
        return courseDTO;
    }

    @ApiOperation(value = "Delete a course from currently assisting for a user")
    @PostMapping("/deleteAssistingCourse")
    public String deleteAssistingCourse(@RequestParam String username, @RequestParam String majorName, @RequestParam Integer number) {
        try {
            Course course = courseRepository.findByMajorNameAndNumber(majorName, number);
            User userToUpdate = userRepository.findByUsername(username);

            Collection<Course> listToDelete = userToUpdate.getCoursesAssisting();
            listToDelete.remove(course);
            userToUpdate.setCoursesAssisting(listToDelete);
            userRepository.save(userToUpdate);
            return "Success";
        } catch (Exception e) {
            return "Failure";
        }
    }

    @ApiOperation(value = "Add a profile picture for a user")
    @PutMapping("/addProfilePicture")
    public UserProfilePictureDTO addProfilePicture(@RequestBody UserProfilePictureDTO userProfilePictureDTO) {
        User userToUpdate = userRepository.findByUsername(userProfilePictureDTO.getUsername());
        userToUpdate.setProfilePicture(userProfilePictureDTO.getProfilePicture());
        userRepository.save(userToUpdate);
        return userProfilePictureDTO;
    }

    @ApiOperation(value = "Add a contact for a user")
    @PutMapping("/addContactForUser")
    public ContactDTO addContactForUser(@RequestBody ContactDTO contactDTO) {
        User userToUpdate = userRepository.findByUsername(contactDTO.getUsername());
        User userToBeAdded = userRepository.findByUsername(contactDTO.getContactUsername());

        Collection<User> contactList = userToUpdate.getContacts();
        contactList.add(userToBeAdded);
        userToUpdate.setContacts(contactList);
        userRepository.save(userToUpdate);
        return contactDTO;
    }

    @ApiOperation(value = "Delete a contact for a user")
    @PostMapping("/deleteContactFromUser")
    public String deleteContactFromUser(@RequestParam String username, @RequestParam String contactUsername) {
        try {
            User userToUpdate = userRepository.findByUsername(username);
            User userToBeRemoved = userRepository.findByUsername(contactUsername);

            Collection<User> contactList = userToUpdate.getContacts();
            contactList.remove(userToBeRemoved);
            userToUpdate.setContacts(contactList);
            userRepository.save(userToUpdate);
            return "Success";
        } catch (Exception e) {
            return "Failure";
        }
    }

    @ApiOperation(value = "Add/Update a user's rating")
    @PutMapping("/addUserRating")
    public RatingProfile addUserRating(@RequestParam String username, @RequestBody RatingProfile ratingProfile)
    {
        User userToUpdate = userRepository.findByUsername(username);
        RatingProfile profileToUpdate = userToUpdate.getRatingProfile();
        profileToUpdate.setAttitude(ratingProfile.getAttitude());
        profileToUpdate.setCommunication(ratingProfile.getCommunication());
        profileToUpdate.setTeamwork(ratingProfile.getTeamwork());
        profileToUpdate.setWorkQuality(ratingProfile.getWorkQuality());
        profileToUpdate.setOverall(ratingProfile.getOverall());
        profileToUpdate.setTotalReviewed(ratingProfile.getTotalReviewed());
        userToUpdate.setRatingProfile(profileToUpdate);
        userRepository.save(userToUpdate);
        return ratingProfile;
    }

    @ApiOperation(value = "Add a conversation between two users")
    @PutMapping("/addConversation")
    public ConversationResponse addConversation(@RequestBody ConversationDTO conversationDTO) { //conversation between two users, with request body call
        User initialUser = userRepository.findByUsername(conversationDTO.getUsername()); //grab both users
        User contactedUser = userRepository.findByUsername(conversationDTO.getContactedUsername()); //both users are in this conversation

        Collection<Conversation> initialConversationList = initialUser.getConversations();
        Collection<Conversation> contactedConversationList = contactedUser.getConversations();
        conversationRepository.save(new Conversation(conversationDTO.getDate_created(), null));

        Conversation c = conversationRepository.findFirstByOrderByIdDesc();
        initialConversationList.add(c);
        contactedConversationList.add(c);
        initialUser.setConversations(initialConversationList);
        contactedUser.setConversations(contactedConversationList);
        userRepository.save(initialUser);
        userRepository.save(contactedUser);
        return new ConversationResponse(conversationDTO.getUsername(), conversationDTO.getContactedUsername(), conversationDTO.getDate_created(), c.getId());
    }

    @ApiOperation(value = "Add a conversation between three users")
    @PutMapping("/addConversationThree")
    public ConversationThreeResponse addConversationThree(@RequestParam String username1, @RequestParam String username2, @RequestParam String username3, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        try {
            User user = userRepository.findByUsername(username1);
            User user1 = userRepository.findByUsername(username2);
            User user2 = userRepository.findByUsername(username3);

            Collection<Conversation> conversations = user.getConversations();
            Collection<Conversation> conversations1 = user1.getConversations();
            Collection<Conversation> conversations2 = user2.getConversations();
            conversationRepository.save(new Conversation(date, null));

            Conversation c = conversationRepository.findFirstByOrderByIdDesc();
            conversations.add(c);
            conversations1.add(c);
            conversations2.add(c);
            user.setConversations(conversations);
            user1.setConversations(conversations1);
            user2.setConversations(conversations2);
            userRepository.save(user);
            userRepository.save(user1);
            userRepository.save(user2);
            return new ConversationThreeResponse(username1, username2, username3, date, c.getId());
        } catch (Exception e) {
            return null;
        }

    }

    @ApiOperation(value = "Add a conversation between four users")
    @PutMapping("/addConversationFour")
    public ConversationFourResponse addConversationFour(@RequestParam String username1, @RequestParam String username2, @RequestParam String username3, @RequestParam String username4, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        try {
            User user = userRepository.findByUsername(username1);
            User user1 = userRepository.findByUsername(username2);
            User user2 = userRepository.findByUsername(username3);
            User user3 = userRepository.findByUsername(username4);

            Collection<Conversation> conversations = user.getConversations();
            Collection<Conversation> conversations1 = user1.getConversations();
            Collection<Conversation> conversations2 = user2.getConversations();
            Collection<Conversation> conversations3 = user3.getConversations();
            conversationRepository.save(new Conversation(date, null));

            Conversation c = conversationRepository.findFirstByOrderByIdDesc();
            conversations.add(c);
            conversations1.add(c);
            conversations2.add(c);
            conversations3.add(c);
            user.setConversations(conversations);
            user1.setConversations(conversations1);
            user2.setConversations(conversations2);
            user3.setConversations(conversations3);
            userRepository.save(user);
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            return new ConversationFourResponse(username1, username2, username3, username4, date, c.getId());
        } catch (Exception e) {
            return null;
        }
    }

    @ApiOperation(value = "Add a message to a conversation")
    @PutMapping("/addMessageToConversation")
    public String addMessageToConversation(@RequestBody MessageDTO messageDTO) {
        try {
            User user = userRepository.findByUsername(messageDTO.getUsername());
            Conversation conversation = conversationRepository.findById(messageDTO.getConversationId()).orElse(null);

            Message message = new Message(conversation, user, messageDTO.getDateSent(), messageDTO.getMessageText());
            messageRepository.save(message);
            return "Success";
        } catch (Exception e) {
            return "Failure";
        }

    }

    @ApiOperation(value = "Remove a user from a conversation")
    @PostMapping("/removeUserFromConversation")
    public String removeUserFromConversation(@RequestParam String userToRemove, @RequestParam Long conversationId) {
        User user = userRepository.findByUsername(userToRemove);
        Collection<Conversation> conversations = user.getConversations();
        Collection<Message> messages = user.getMessages();

        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        conversations.remove(conversation);
        user.setConversations(conversations);

        for(Message message : messages) {
            if(message.getConversation().getId() == conversationId) {
                messageRepository.delete(message);
            }
        }
        messages.removeIf(m -> (m.getConversation().getId() == conversationId));


        user.setMessages(messages);
        userRepository.save(user);
        return "Success";
    }


    @ApiOperation(value = "Permanently delete a conversation between two users")
    @PostMapping("/deleteConversationForTwo")
    public String deleteConversation(@RequestParam String username1, @RequestParam String username2, @RequestParam Long conversationId) {
        try {
            Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
            User user1 = userRepository.findByUsername(username1);
            Collection<Conversation> conversations1 = user1.getConversations();
            Collection<Message> messages1 = user1.getMessages();
            User user2 = userRepository.findByUsername(username2);
            Collection<Conversation> conversations2 = user2.getConversations();
            Collection<Message> messages2 = user2.getMessages();

            for(Message message : messages1) {
                if(message.getConversation().getId() == conversationId) {
                    messageRepository.delete(message);
                }
            }
            messages1.removeIf(m -> (m.getConversation().getId() == conversationId));

            for(Message message : messages2) {
                if(message.getConversation().getId() == conversationId) {
                    messageRepository.delete(message);
                }
            }
            messages2.removeIf(m -> (m.getConversation().getId() == conversationId));

            conversations1.remove(conversation);
            user1.setConversations(conversations1);
            userRepository.save(user1);
            conversations2.remove(conversation);
            user2.setConversations(conversations2);
            userRepository.save(user2);

            conversationRepository.delete(conversation);
            return "Success";
        } catch (Exception e) {
            return "Failure";
        }
    }

    @ApiOperation(value = "average out the values of a rating profile once a single user reviews")
    @PutMapping("/averageOutRatingProfile")
    public RatingResponse averageRatingProfile(@RequestParam String username, @RequestParam Double newTeamwork, @RequestParam Double newCommunication,
                                              @RequestParam Double newWorkQuality, @RequestParam Double newAttitude, @RequestParam Double newOverall) {
        User userToUpdate = userRepository.findByUsername(username);
        RatingProfile userRP = userToUpdate.getRatingProfile();
        Integer newTotalReviewed = userRP.getTotalReviewed() + 1;

        Double teamworkUpdate = userToUpdate.getTeamworkRatings() + newTeamwork;
        userToUpdate.setTeamworkRatings(Math.round(teamworkUpdate * 10) / 10.0);
        Double communicationUpdate = userToUpdate.getCommunicationRatings() + newCommunication;
        userToUpdate.setCommunicationRatings(Math.round(communicationUpdate * 10) / 10.0);
        Double workQualityUpdate = userToUpdate.getWorkQualityRatings() + newWorkQuality;
        userToUpdate.setWorkQualityRatings(Math.round(workQualityUpdate * 10) / 10.0);
        Double attitudeUpdate = userToUpdate.getAttitudeRatings() + newAttitude;
        userToUpdate.setAttitudeRatings(Math.round(attitudeUpdate * 10) / 10.0);
        Double overallUpdate = userToUpdate.getOverallRatings() + newOverall;
        userToUpdate.setOverallRatings(Math.round(overallUpdate * 10) / 10.0);


        userRP.setTeamwork(Math.round(userToUpdate.getTeamworkRatings() / newTotalReviewed * 10) / 10.0);
        userRP.setCommunication(Math.round(userToUpdate.getCommunicationRatings() / newTotalReviewed * 10) / 10.0);
        userRP.setWorkQuality(Math.round(userToUpdate.getWorkQualityRatings() / newTotalReviewed * 10) / 10.0);
        userRP.setAttitude(Math.round(userToUpdate.getAttitudeRatings() / newTotalReviewed * 10) / 10.0);
        userRP.setOverall(Math.round(userToUpdate.getOverallRatings() / newTotalReviewed * 10) / 10.0);
        userRP.setTotalReviewed(newTotalReviewed);
        userToUpdate.setRatingProfile(userRP);
        userRepository.save(userToUpdate);
        RatingResponse retVal = new RatingResponse(userRP);
        return retVal;
    }
}
