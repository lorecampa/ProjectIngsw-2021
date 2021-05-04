package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DevelopmentAction {
    private final int row;
    private final int column;
    private final int locateSlot;
    private final String username;

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

    public String getUsername() {
        return username;
    }
}
