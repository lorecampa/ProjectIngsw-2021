package it.polimi.ingsw.message.bothArchitectureMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.Optional;

public class StrongboxModify implements ClientMessage, ServerMessage {
    private final ResourceData resource;
    private final boolean isAdd;
    private final String username;

    @JsonCreator
    public StrongboxModify(@JsonProperty("resource") ResourceData resource,
                           @JsonProperty("isAdd") boolean isAdd,
                           @JsonProperty("username")String username) {
        this.resource = resource;
        this.isAdd = isAdd;
        this.username = username;
    }

    @JsonCreator
    public StrongboxModify(@JsonProperty("resource") ResourceData resource,
                           @JsonProperty("isAdd") boolean isAdd) {
        this.resource = resource;
        this.isAdd = isAdd;
        this.username = null;
    }


    public ResourceData getResource() {
        return resource;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public boolean isAdd() {
        return isAdd;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("StrongboxModifyHandler");
    }

    @Override
    public void process(ServerMessageHandler handler) {
        System.out.println("StrongboxModifyHandler");
    }
}
