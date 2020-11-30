package coms309.teambeans.Models;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name =  "user", uniqueConstraints = @UniqueConstraint(columnNames = { "username", "email" }))
public class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "first name for a user", name = "firstName", required = true)
    private String firstName;

    @ApiModelProperty(notes = "last name for a user", name = "lastName", required = true)
    private String lastName;

    @ApiModelProperty(notes = "Email for a user", name = "email", required = true)
    @Column(unique = true)
    private String email;

    @ApiModelProperty(notes = "username for a user", name = "username", required = true)
    @Column(unique = true)
    private String username;

    @ApiModelProperty(notes = "password for a user", name = "password", required = true)
    private String password;

    @ApiModelProperty(notes = "Role for a user", name = "roles", required = true)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @ApiModelProperty(notes = "Courses previously taken for a user", name = "courseOldList")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(
            name = "users_oldCourses",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "course_id", referencedColumnName = "id"))
    private Collection<Course> courseOldList;

    @ApiModelProperty(notes = "Courses currently taking for a user", name = "courseCurrentList")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(
            name = "users_CurrentCourses",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "course_id", referencedColumnName = "id"))
    private Collection<Course> courseCurrentList;

    @ApiModelProperty(notes = "Languages known by a user", name = "languages")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(
            name = "users_languages",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "language_id", referencedColumnName = "id"))
    private Collection<Language> languages;

    @ApiModelProperty(notes = "description for a user", name = "description", required = true)
    private String description;

    @ApiModelProperty(notes = "Courses teaching for a user", name = "coursesTeaching")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(
            name = "users_coursesTeaching",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "course_id", referencedColumnName = "id"))
    private Collection<Course> coursesTeaching;

    @ApiModelProperty(notes = "Courses assisting with for a user", name = "coursesAssisting")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(
            name = "users_coursesAssisting",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "course_id", referencedColumnName = "id"))
    private Collection<Course> coursesAssisting;

    @ApiModelProperty(notes = "profile picture string for a user", name = "profilePicture", required = true)
    private String profilePicture;

    @ApiModelProperty(notes = "contacts for a user", name = "contacts")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(
            name = "users_contacts",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "contact_id", referencedColumnName = "id"))
    private Collection<User> contacts;

    @ApiModelProperty(notes = "rating profile for a user", name = "ratingProfile", required = true)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_rating", referencedColumnName = "id")
    private RatingProfile ratingProfile;

    @ApiModelProperty(notes = "conversations a user is in", name = "conversations")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(
            name = "users_conversations",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "conversation_id", referencedColumnName = "id"))
    private Collection<Conversation> conversations;

    @ApiModelProperty(notes = "messages a user has sent", name = "messages")
    @OneToMany(mappedBy = "user")
    private Collection<Message> messages;

    @ApiModelProperty(notes = "Chat room messages a user has sent", name = "chatRoomMessages")
    @OneToMany(mappedBy = "userRoom")
    private Collection<ChatRoomMessage> chatRoomMessages;

    private Double teamworkRatings;
    private Double communicationRatings;
    private Double workQualityRatings;
    private Double attitudeRatings;
    private Double overallRatings;

    public User() {

    }

    public User(String firstName, String lastName, String email, String username, String password, Collection<Role> roles, Collection<Course> courseOldList, Collection<Course> courseCurrentList, Collection<Language> languages, String description, Collection<Course> coursesTeaching, Collection<Course> coursesAssisting, String profilePicture, Collection<User> contacts,
                RatingProfile ratingProfile, Collection<Conversation> conversations, Double teamworkRatings, Double communicationRatings, Double workQualityRatings
    , Double attitudeRatings, Double overallRatings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.courseOldList = courseOldList;
        this.courseCurrentList = courseCurrentList;
        this.languages = languages;
        this.description = description;
        this.coursesTeaching = coursesTeaching;
        this.coursesAssisting = coursesAssisting;
        this.profilePicture = profilePicture;
        this.contacts = contacts;
        this.ratingProfile = ratingProfile;
        this.conversations = conversations;
        this.teamworkRatings = teamworkRatings;
        this.communicationRatings = communicationRatings;
        this.workQualityRatings = workQualityRatings;
        this.attitudeRatings = attitudeRatings;
        this.overallRatings = overallRatings;
    }

    public Double getCommunicationRatings() {
        return communicationRatings;
    }

    public void setCommunicationRatings(Double communicationRatings) {
        this.communicationRatings = communicationRatings;
    }

    public Double getWorkQualityRatings() {
        return workQualityRatings;
    }

    public void setWorkQualityRatings(Double workQualityRatings) {
        this.workQualityRatings = workQualityRatings;
    }

    public Double getAttitudeRatings() {
        return attitudeRatings;
    }

    public void setAttitudeRatings(Double attitudeRatings) {
        this.attitudeRatings = attitudeRatings;
    }

    public Double getOverallRatings() {
        return overallRatings;
    }

    public void setOverallRatings(Double overallRatings) {
        this.overallRatings = overallRatings;
    }

    public Double getTeamworkRatings() {
        return teamworkRatings;
    }

    public void setTeamworkRatings(Double teamworkRatings) {
        this.teamworkRatings = teamworkRatings;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }

    public Collection<ChatRoomMessage> getChatRoomMessages() {
        return chatRoomMessages;
    }

    public void setChatRoomMessages(Collection<ChatRoomMessage> chatRoomMessages) {
        this.chatRoomMessages = chatRoomMessages;
    }

    public Collection<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Collection<Conversation> conversations) {
        this.conversations = conversations;
    }

    public RatingProfile getRatingProfile() {
        return ratingProfile;
    }

    public void setRatingProfile(RatingProfile ratingProfile) {
        this.ratingProfile = ratingProfile;
    }

    public Collection<User> getContacts() {
        return contacts;
    }

    public void setContacts(Collection<User> contacts) {
        this.contacts = contacts;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Collection<Course> getCoursesAssisting() {
        return coursesAssisting;
    }

    public void setCoursesAssisting(Collection<Course> coursesAssisting) {
        this.coursesAssisting = coursesAssisting;
    }

    public Collection<Course> getCoursesTeaching() {
        return coursesTeaching;
    }

    public void setCoursesTeaching(Collection<Course> coursesTeaching) {
        this.coursesTeaching = coursesTeaching;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Collection<Course> getCourseOldList() {
        return courseOldList;
    }

    public void setCourseOldList(Collection<Course> courseOldList) {
        this.courseOldList = courseOldList;
    }

    public Collection<Course> getCourseCurrentList() {
        return courseCurrentList;
    }

    public void setCourseCurrentList(Collection<Course> courseCurrentList) {
        this.courseCurrentList = courseCurrentList;
    }

    public Collection<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(Collection<Language> languages) {
        this.languages = languages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
