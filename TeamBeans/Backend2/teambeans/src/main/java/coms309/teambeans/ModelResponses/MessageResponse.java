package coms309.teambeans.ModelResponses;

import coms309.teambeans.DTOs.MessageDTO;
import coms309.teambeans.Models.Message;

import java.util.Collection;
import java.util.List;

public class MessageResponse {
    List<MessageDTO> messageList;

    public MessageResponse() {
    }

    public MessageResponse(List<MessageDTO> messageList) {
        this.messageList = messageList;
    }

    public List<MessageDTO> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageDTO> messageList) {
        this.messageList = messageList;
    }
}
