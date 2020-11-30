package coms309.teambeans.Models;


import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "conversation")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "Date conversation was created", name = "dateCreated", required = true)
    @DateTimeFormat
    private Date dateCreated;

    @ApiModelProperty(notes = "Messages in a Conversation", name = "messages", required = true)
    @OneToMany(mappedBy = "conversation")
    private Collection<Message> messages;

    @ApiModelProperty(notes = "Users in the conversation", name = "users", required = true)
    @ManyToMany(mappedBy = "conversations")
    private Collection<User> users;

    public Conversation() {
    }

    public Conversation(Date dateCreated, Collection<Message> messages) {
        this.dateCreated = dateCreated;
        this.messages = messages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}
