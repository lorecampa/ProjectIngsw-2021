package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;

public class StarTurn implements ClientMessage{
    private final String username;

    @JsonCreator
    public StarTurn(@JsonProperty("username") String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.newTurn(this);
    }
}
