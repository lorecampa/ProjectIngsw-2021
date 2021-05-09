package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.Optional;

public class DevelopmentAction implements ServerMessage {
    private final int row;
    private final int column;
    private final int locateSlot;

    @JsonCreator
    public DevelopmentAction(@JsonProperty("row")int row,
                             @JsonProperty("column")int column,
                             @JsonProperty("locateSlot")int locateSlot) {
        this.row = row;
        this.column = column;
        this.locateSlot = locateSlot;
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


    @Override
    public void process(ServerMessageHandler handler) {
        System.out.println("DevelopmentAction Handler");
    }
}
