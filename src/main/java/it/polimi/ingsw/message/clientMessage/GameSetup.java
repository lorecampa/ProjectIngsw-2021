package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.DeckDevData;
import it.polimi.ingsw.client.data.MarketData;

import java.util.ArrayList;


public class GameSetup implements ClientMessage{
    private final ArrayList<String> usernames;
    private final MarketData market;
    private final DeckDevData deckDev;

    public GameSetup(@JsonProperty("usernames")ArrayList<String> usernames,
                     @JsonProperty("market")MarketData market,
                     @JsonProperty("deckDev")DeckDevData deckDev) {
        this.usernames = usernames;
        this.market = market;
        this.deckDev = deckDev;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }

    public MarketData getMarket() {
        return market;
    }

    public DeckDevData getDeckDev() {
        return deckDev;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.gameSetUp(this);
    }
}
