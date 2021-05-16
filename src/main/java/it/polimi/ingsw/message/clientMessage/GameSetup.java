package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.DeckDevData;
import it.polimi.ingsw.client.data.FaithTrackData;
import it.polimi.ingsw.client.data.MarketData;

import java.util.ArrayList;


public class GameSetup implements ClientMessage{
    private final ArrayList<String> usernames;
    private final MarketData market;
    private final DeckDevData deckDev;
    private final ArrayList<FaithTrackData> faithTrack;

    @JsonCreator
    public GameSetup(@JsonProperty("usernames")ArrayList<String> usernames,
                     @JsonProperty("market")MarketData market,
                     @JsonProperty("deckDev")DeckDevData deckDev,
                     @JsonProperty("faithTrack")ArrayList<FaithTrackData> faithTrack) {
        this.usernames = usernames;
        this.market = market;
        this.deckDev = deckDev;
        this.faithTrack = faithTrack;
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

    public ArrayList<FaithTrackData> getFaithTrack() { return faithTrack; }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.gameSetUp(this);
    }
}
