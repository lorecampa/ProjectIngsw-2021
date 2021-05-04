package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MarketAction {
    private final int selection;
    private final boolean isRow;
    private final String username;

    @JsonCreator
    public MarketAction(@JsonProperty("selection")int selection,
                        @JsonProperty("isRow") boolean isRow,
                        @JsonProperty("username")String username) {
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
}
