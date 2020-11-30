package coms309.teambeans.Models;


import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "Conversation this message belongs to", name = "conversation", required = true)
    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ApiModelProperty(notes = "User who sent message", name = "user", required = true)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ApiModelProperty(notes = "Date and time of when a message was created", name = "timeCreated", required = true)
    @DateTimeFormat
    private Date timeCreated; //sql or util version?

    @ApiModelProperty(notes = "Body of the message", name = "text", required = true)
    private String text;

    public Message() {
    }

    public Message(Conversation conversation, User user, Date timeCreated, String text) {
        this.conversation = conversation;
        this.user = user;
        this.timeCreated = timeCreated;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
