package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.server.ServerMessageHandler;

public class QuitGame implements ServerMessage{
    private final String username;

    @JsonCreator
    public QuitGame(@JsonProperty("username") String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ServerMessageHandler handler) {

    }
}
