package it.polimi.ingsw.message.bothArchitectureMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.Optional;

public class DevelopmentAction implements ClientMessage, ServerMessage {
    private final int row;
    private final int column;
    private final int locateSlot;
    private final String username;

    @JsonCreator
    public DevelopmentAction(@JsonProperty("row")int row,
                             @JsonProperty("column")int column,
                             @JsonProperty("locateSlot")int locateSlot) {
        this.row = row;
        this.column = column;
        this.locateSlot = locateSlot;
        this.username = null;
    }

    @JsonCreator
    public DevelopmentAction(@JsonProperty("row")int row,
                             @JsonProperty("column")int column,
                             @JsonProperty("locateSlot")int locateSlot,
                             @JsonProperty("username")String username) {
        this.row = row;
        this.column = column;
        this.locateSlot = locateSlot;
        this.username = username;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getLocateSlot() {
        return locateSlot;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("DevelopmentAction Handler");
    }

    @Override
    public void process(ServerMessageHandler handler) {
        System.out.println("DevelopmentAction Handler");
    }
}
