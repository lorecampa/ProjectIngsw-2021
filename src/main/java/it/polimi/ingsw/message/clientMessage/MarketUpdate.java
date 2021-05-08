package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.model.card.Color;

import java.util.ArrayList;

public class MarketUpdate implements ClientMessage{
    private final ArrayList<ColorData> sequence;
    private final ColorData lastMarble;
    private final int selection;
    private final boolean isRow;

    @JsonCreator
    public MarketUpdate(@JsonProperty("sequence") ArrayList<ColorData> sequence,
                        @JsonProperty("lastMarble")ColorData lastMarble,
                        @JsonProperty("selection")int selection,
                        @JsonProperty("isRow")boolean isRow) {
        this.sequence = sequence;
        this.lastMarble = lastMarble;
        this.selection = selection;
        this.isRow = isRow;
    }

    public ArrayList<ColorData> getSequence() {
        return sequence;
    }

    public ColorData getLastMarble() {
        return lastMarble;
    }

    public int getSelection() {
        return selection;
    }

    public boolean isRow() {
        return isRow;
    }


    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("MarketUpdateClientHandler");
    }
}
