package coms309.teambeans.Models;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "chatRoomMessage")
public class ChatRoomMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "User that sent the message", name = "userRoom", required = true)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userRoom;

    @ApiModelProperty(notes = "Date and time message was sent", name = "teamwork", required = true)
    //@DateTimeFormat
    private LocalDateTime timeCreated;

    @ApiModelProperty(notes = "message body", name = "text", required = true)
    private String text;

    public ChatRoomMessage() {
    }

    public ChatRoomMessage(User userRoom, LocalDateTime timeCreated, String text) {
        this.userRoom = userRoom;
        this.timeCreated = timeCreated;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserRoom() {
        return userRoom;
    }

    public void setUserRoom(User userRoom) {
        this.userRoom = userRoom;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
