package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.server.ServerMessageHandler;
import it.polimi.ingsw.message.bothMessage.ConnectionMessage;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConnectionMessage.class, name = "ConnectionMessage"),
        @JsonSubTypes.Type(value = ReconnectionMessage.class, name = "ReconnectionMessage")
})
public interface ServerMessage extends Message {
    void process (ServerMessageHandler handler);
}
