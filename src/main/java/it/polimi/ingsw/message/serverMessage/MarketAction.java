package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.server.ServerMessageHandler;

public class MarketAction implements ServerMessage{
    private final int selection;
    private final boolean row;
    private final String username;

    @JsonCreator
    public MarketAction(@JsonProperty("selection") int selection,
                        @JsonProperty("row") boolean row,
                        @JsonProperty("username") String username) {
        this.selection = selection;
        this.row = row;
        this.username = username;
    }

    public int getSelection() {
        return selection;
    }

    public boolean isRow() {
        return row;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        System.out.println("ServerMessageHandler");
    }
}
