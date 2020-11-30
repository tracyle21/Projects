package coms309.teambeans.ModelResponses;

import java.util.List;

public class ConversationIDResponse {
    List<Long> conversationIDs;

    public ConversationIDResponse() {
    }

    public ConversationIDResponse(List<Long> conversationIDs) {
        this.conversationIDs = conversationIDs;
    }

    public List<Long> getConversationIDs() {
        return conversationIDs;
    }

    public void setConversationIDs(List<Long> conversationIDs) {
        this.conversationIDs = conversationIDs;
    }
}
