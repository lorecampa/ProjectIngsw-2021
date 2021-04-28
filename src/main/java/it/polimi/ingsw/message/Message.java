package it.polimi.ingsw.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")

@JsonSubTypes({
        @JsonSubTypes.Type(value = ServerMessage.class, name = "ServerMessage"),
        @JsonSubTypes.Type(value = ClientMessage.class, name = "ClientMessage")
})
public interface Message{

}
