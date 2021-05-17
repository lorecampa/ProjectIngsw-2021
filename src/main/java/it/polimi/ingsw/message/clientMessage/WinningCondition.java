package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;

public class WinningCondition implements ClientMessage{
    private final String username;

    @JsonCreator
    public WinningCondition(@JsonProperty("username")String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.winningCondition(this);
    }
}
