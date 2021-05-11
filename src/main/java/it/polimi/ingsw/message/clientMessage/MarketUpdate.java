package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.model.card.Color;

import java.util.ArrayList;

public class MarketUpdate implements ClientMessage{
    private final ArrayList<ArrayList<ColorData>> marketTray;
    private final ColorData lastMarble;

    @JsonCreator
    public MarketUpdate(@JsonProperty("marketTray") ArrayList<ArrayList<ColorData>> marketTray,
                        @JsonProperty("lastMarble")ColorData lastMarble) {
        this.marketTray = marketTray;
        this.lastMarble = lastMarble;
    }

    public ArrayList<ArrayList<ColorData>> getMarketTray() {
        return marketTray;
    }

    public ColorData getLastMarble() {
        return lastMarble;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("MarketUpdateClientHandler");
    }
}
