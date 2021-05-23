package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.CLIMessageHandler;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.DeckDevData;
import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.client.data.FaithTrackData;
import it.polimi.ingsw.client.data.MarketData;

import java.util.ArrayList;


public class GameSetup implements ClientMessage{
    private final ArrayList<String> usernames;
    private final MarketData market;
    private final DeckDevData deckDev;
    private final ArrayList<ArrayList<FaithTrackData>> faithTracks;
    private final ArrayList<EffectData> baseProd;

    @JsonCreator
    public GameSetup(@JsonProperty("usernames")ArrayList<String> usernames,
                     @JsonProperty("market")MarketData market,
                     @JsonProperty("deckDev")DeckDevData deckDev,
                     @JsonProperty("faithTracks")ArrayList<ArrayList<FaithTrackData>> faithTracks,
                     @JsonProperty("baseProduction")ArrayList<EffectData> baseProd) {
        this.usernames = usernames;
        this.market = market;
        this.deckDev = deckDev;
        this.faithTracks = faithTracks;
        this.baseProd=baseProd;
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

    public ArrayList<ArrayList<FaithTrackData>> getFaithTracks() { return faithTracks; }

    public ArrayList<EffectData> getBaseProd() {
        return baseProd;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.gameSetUp(this);
    }
}
