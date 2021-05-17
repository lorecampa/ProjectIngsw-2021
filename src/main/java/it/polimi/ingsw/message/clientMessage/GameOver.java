package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;

import java.util.TreeMap;

public class GameOver implements ClientMessage{
    private final TreeMap<Integer, String> players;

    @JsonCreator
    public GameOver(@JsonProperty("players") TreeMap<Integer, String> players) {
        this.players = players;
    }

    public TreeMap<Integer, String> getPlayers() {
        return players;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.gameOver(this);
    }
}
