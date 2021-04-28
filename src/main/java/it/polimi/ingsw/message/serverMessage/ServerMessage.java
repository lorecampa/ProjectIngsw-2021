package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.ServerMessageHandler;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")

@JsonSubTypes({
        @JsonSubTypes.Type(value = BuyDevelopmentCardMessage.class, name = "BuyDevelopmentCardMessage"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "ErrorMessage"),
        @JsonSubTypes.Type(value = ActivateProductionMessage.class, name = "ActivateProductionMessage")
})
public interface ServerMessage extends Message {
    void process (ServerMessageHandler handler);
}
