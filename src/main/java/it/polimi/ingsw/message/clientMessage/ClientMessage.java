package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.message.ClientMessageHandler;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.bothMessage.ConnectionMessage;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConnectionMessage.class, name = "ConnectionMessage"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "ErrorMessage")
})
public interface ClientMessage extends Message{
    void process (ClientMessageHandler handler);
}
