package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.Map;
import java.util.Optional;

public class MarketAction implements ServerMessage{
    private final int selection;
    private final boolean isRow;
    private final String username;

    @JsonCreator
    public MarketAction(@JsonProperty("selection") int selection,
                        @JsonProperty("isRow") boolean isRow,
                        @JsonProperty("username") String username) {
        this.selection = selection;
        this.isRow = isRow;
        this.username = username;
    }

    public int getSelection() {
        return selection;
    }

    public boolean isRow() {
        return isRow;
    }

    public String getUsername() {
        return username;
    }


    @Override
    public void process(ServerMessageHandler handler) {
        System.out.println("ServerMessageHandler");
    }
}
