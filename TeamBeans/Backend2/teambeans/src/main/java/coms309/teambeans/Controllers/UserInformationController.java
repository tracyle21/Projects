package coms309.teambeans.Controllers;


import coms309.teambeans.DTOs.MessageDTO;
import coms309.teambeans.ModelResponses.*;
import coms309.teambeans.Models.*;
import coms309.teambeans.Repositories.ConversationRepository;
import coms309.teambeans.Repositories.CourseRepository;
import coms309.teambeans.Repositories.LanguageRepository;
import coms309.teambeans.Repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.GeneratedValue;
import java.util.*;

@Api(value = "UserInformationController", tags = {"User Information API"})
@SwaggerDefinition(tags = {
        @Tag(name = "User Information API", description = "Admin interface to manage information about a user")
})
@RestController
public class UserInformationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @ApiOperation(value = "Get list of courses taken for a user")
    @GetMapping("/users/{name}/oldCourses")
    public CourseResponse getOldCourses(@PathVariable String name) {
        User user = userRepository.findByUsername(name);
        Collection<Course> oldCourseList = user.getCourseOldList();

        List<String> courseNames = new ArrayList<>();
        for (Course c : oldCourseList) {
            courseNames.add(c.getMajorName() + " " + c.getNumber());
        }

        CourseResponse courseResponse = new CourseResponse(courseNames);
        return courseResponse;
    }

    @ApiOperation(value = "Get list of currently taking courses for a user")
    @GetMapping("/users/{name}/currentCourses")
    public CourseResponse getCurrentCourses(@PathVariable String name) {
        User user = userRepository.findByUsername(name);
        Collection<Course> currentCourseList = user.getCourseCurrentList();

        List<String> courseNames = new ArrayList<>();
        for (Course c : currentCourseList) {
            courseNames.add(c.getMajorName() + " " + c.getNumber());
        }

        CourseResponse courseResponse = new CourseResponse(courseNames);
        return courseResponse;
    }

    @ApiOperation(value = "Get list of courses teaching for a user")
    @GetMapping("/users/{name}/coursesTeaching")
    public CourseResponse getCoursesTeaching(@PathVariable String name) {
        User user = userRepository.findByUsername(name);
        Collection<Course> coursesTeaching = user.getCoursesTeaching();

        List<String> courseNames = new ArrayList<>();
        for (Course c : coursesTeaching) {
            courseNames.add(c.getMajorName() + " " + c.getNumber());
        }

        CourseResponse courseResponse = new CourseResponse(courseNames);
        return courseResponse;
    }

    @ApiOperation(value = "Get list of courses assisting with for a user")
    @GetMapping("/users/{name}/coursesAssisting")
    public CourseResponse getCoursesAssisting(@PathVariable String name) {
        User user = userRepository.findByUsername(name);
        Collection<Course> coursesAssisting = user.getCoursesAssisting();

        List<String> courseNames = new ArrayList<>();
        for (Course c : coursesAssisting) {
            courseNames.add(c.getMajorName() + " " + c.getNumber());
        }

        CourseResponse courseResponse = new CourseResponse(courseNames);
        return courseResponse;
    }

    @ApiOperation(value = "Get list of languages known by a user")
    @GetMapping("/users/{name}/languages")
    public LanguageResponse getLanguages(@PathVariable String name) {
        User user = userRepository.findByUsername(name);
        Collection<Language> languages = user.getLanguages();

        List<String> languageNames = new ArrayList<>();
        for (Language l : languages) {
            languageNames.add(l.getName());
        }

        LanguageResponse languageResponse = new LanguageResponse(languageNames);
        return languageResponse;
    }

    @ApiOperation(value = "Get description statement for a user")
    @GetMapping("/users/{name}/description")
    public String getDescription(@PathVariable String name) {
        User user = userRepository.findByUsername(name);
        return user.getDescription();
    }

    @ApiOperation(value = "Get string url for a profile picture for a user")
    @GetMapping("/users/{name}/profilePicture")
    public String getProfilePicture(@PathVariable String name) {
        User user = userRepository.findByUsername(name);
        return user.getProfilePicture();
    }

    @ApiOperation(value = "Get email registered for a user")
    @GetMapping("/users/{name}/email")
    public String getEmail(@PathVariable String name) {
        User user = userRepository.findByUsername(name);
        return user.getEmail();
    }

    @ApiOperation(value = "Get role for a user")
    @GetMapping("/users/{name}/role")
    public String getRole(@PathVariable String name) { //TODO
        User user = userRepository.findByUsername(name);
        Collection<Role> roles = user.getRoles();
        String roleName = "";
        for(Role role : roles) {
            roleName = role.getName();
        }
        return roleName;
    }

    @ApiOperation(value = "Get list of contacts for a user")
    @GetMapping("/users/{name}/contacts")
    public UserResponse getContacts(@PathVariable String name) {
        User user = userRepository.findByUsername(name);
        Collection<User> contacts = user.getContacts();

        List<String> contactNames = new ArrayList<>();
        for (User u : contacts) {
            contactNames.add(u.getUsername());
        }

        UserResponse userResponse = new UserResponse(contactNames);
        return userResponse;
    }

    @ApiOperation(value = "Get the overall rating/feedback profile for a user")
    @GetMapping("/users/{name}/ratingProfile")
    public RatingResponse getRatingProfile(@PathVariable String name) {
        User user = userRepository.findByUsername(name);
        RatingProfile ratingProfile = user.getRatingProfile();
        RatingResponse ratingResponse = new RatingResponse(ratingProfile);
        return ratingResponse;
    }

    @ApiOperation(value = "Get list of ALL conversation IDs a user is in")
    @GetMapping("/getConversationIdForUser")
    public ConversationIDResponse getConversationIdForUser(@RequestParam String username) { //gets all the conversation ids a user is a part of
        User user = userRepository.findByUsername(username);
        List<Long> idList = new ArrayList<>();
        Collection<Conversation> conversations = user.getConversations();

        for(Conversation conversation : conversations) {
            idList.add(conversation.getId());
        }

        ConversationIDResponse conversationIDResponse = new ConversationIDResponse(idList);
        return conversationIDResponse;
    }

    @ApiOperation(value = "Get list of users given a conversation id")
    @GetMapping("/getUsersInConversationsFromId")
    public UserResponse getUsersInConversationsFromId(@RequestParam Long id) {
        Optional<Conversation> conversation = conversationRepository.findById(id);
        List<String> usernames = new ArrayList<>();

        Collection<User> list = conversation.get().getUsers();

        for(User u : list) {
            usernames.add(u.getUsername());
        }

        UserResponse userResponse = new UserResponse(usernames);
        return userResponse;
    }

    @ApiOperation(value = "Get list of users taking a particular course")
    @GetMapping("/getNamesTakingACourse")
    public UserResponse getUsersInCourse(@RequestParam String name, @RequestParam Integer number) {
        Course course = courseRepository.findByMajorNameAndNumber(name, number);
        List<String> nameList = new ArrayList<>();

        Collection<User> userList = course.getUsers();

        for(User u : userList) {
            nameList.add(u.getFirstName() + " " + u.getLastName());
        }

        UserResponse userResponse = new UserResponse(nameList);

        return userResponse;
    }

    @ApiOperation(value = "Get list of professors teaching a particular course")
    @GetMapping("/getProfessorsTeachingACourse")
    public UserResponse getProfessorsTeachingACourse(@RequestParam String courseName, @RequestParam Integer number) {
        Course course = courseRepository.findByMajorNameAndNumber(courseName, number);
        List<String> profList = new ArrayList<>();
        Collection<User> list = course.getProfessors();

        for(User u : list) {
            profList.add(u.getFirstName() + " " + u.getLastName());
        }

        UserResponse userResponse = new UserResponse(profList);
        return userResponse;
    }

    @ApiOperation(value = "Get list of TAs for a particular course")
    @GetMapping("/getAssistantsForACourse")
    public UserResponse getAssistantsForACourse(@RequestParam String courseName, @RequestParam Integer number) {
        Course course = courseRepository.findByMajorNameAndNumber(courseName, number);
        List<String> assistantList = new ArrayList<>();
        Collection<User> list = course.getTeachingAssistants();

        for(User u : list) {
            assistantList.add(u.getFirstName() + " " + u.getLastName());
        }

        UserResponse userResponse = new UserResponse(assistantList);
        return userResponse;
    }

    @ApiOperation(value = "Get list of all users registered")
    @GetMapping("/getAllUsers")
    public UserResponse getAllUsers() {
        List<User> listOfUsers = userRepository.findAll();
        List<String> usernames = new ArrayList<>();

        for(User u : listOfUsers) {
            usernames.add(u.getUsername());
        }

        UserResponse userResponse = new UserResponse(usernames);
        return userResponse;
    }

    @ApiOperation(value = "Get list of users that know a certain language")
    @GetMapping("/getUsersForLanguage")
    public UserResponse getUsersForLanguage(@RequestParam String language) {
        Language l = languageRepository.findByName(language);
        List<String> usernames = new ArrayList<>();

        for(User user : l.getUsers()) {
            usernames.add(user.getUsername());
        }

        UserResponse userResponse = new UserResponse(usernames);
        return userResponse;
    }

    @ApiOperation(value = "Get list of messages for a particular conversation")
    @GetMapping("/getMessagesForConversation")
    public MessageResponse getMessagesForConversation(@RequestParam Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        Collection<Message> messages = conversation.getMessages();
        List<MessageDTO> conversationMessages = new ArrayList<>();

        for(Message message : messages) {
            MessageDTO messageDTO = new MessageDTO(message.getUser().getUsername(), conversationId, message.getText(), message.getTimeCreated());
            conversationMessages.add(messageDTO);
        }

        MessageResponse messageResponse = new MessageResponse(conversationMessages);
        return messageResponse;
    }

    @ApiOperation(value = "Get list of messages sent by a user")
    @GetMapping("/getAllMessagesForUser")
    public MessageResponse getAllMessagesForUser(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        Collection<Message> messages = user.getMessages();
        List<MessageDTO> messageDTOList = new ArrayList<>();

        for(Message message : messages) {
            MessageDTO messageDTO = new MessageDTO(username, message.getConversation().getId(), message.getText(), message.getTimeCreated());
            messageDTOList.add(messageDTO);
        }

        MessageResponse messageResponse = new MessageResponse(messageDTOList);
        return messageResponse;
    }

    @ApiOperation(value = "Returns the most recently created conversation ID")
    @GetMapping("/returnMostRecentConversationID")
    public String returnMostRecentConversationID() {
        return conversationRepository.findFirstByOrderByIdDesc().getId().toString();
    }

    @ApiOperation(value = "Returns the subcategory Teamwork for a specific user")
    @GetMapping("/getTeamworkRating")
    public String getTeamworkRating(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        return user.getRatingProfile().getTeamwork().toString();
    }

    @ApiOperation(value = "Returns the subcategory Communication for a specific user")
    @GetMapping("/getCommunicationRating")
    public String getCommunicationRating(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        return user.getRatingProfile().getCommunication().toString();
    }

    @ApiOperation(value = "Returns the subcategory work quality for a specific user")
    @GetMapping("/getWorkQualityRating")
    public String getWorkQualityRating(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        return user.getRatingProfile().getWorkQuality().toString();
    }

    @ApiOperation(value = "Returns the subcategory attitude for a specific user")
    @GetMapping("/getAttitudeRating")
    public String getAttitudeRating(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        return user.getRatingProfile().getAttitude().toString();
    }

    @ApiOperation(value = "Returns the subcategory overall total for a specific user")
    @GetMapping("/getOverallRating")
    public String getOverallRating(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        return user.getRatingProfile().getOverall().toString();
    }

    @ApiOperation(value = "Returns the total number that reviewed a user for a specific user")
    @GetMapping("/getTotalReviewed")
    public String getTotalReviewed(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        return user.getRatingProfile().getTotalReviewed().toString();
    }
}
