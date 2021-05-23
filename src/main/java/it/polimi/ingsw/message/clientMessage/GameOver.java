package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.CLIMessageHandler;
import it.polimi.ingsw.client.ClientMessageHandler;

import java.util.Map;

public class GameOver implements ClientMessage{
    private final Map<Integer, String> players;

    @JsonCreator
    public GameOver(@JsonProperty("players") Map<Integer, String> players) {
        this.players = players;
    }

    public Map<Integer, String> getPlayers() {
        return players;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.gameOver(this);
    }
}
