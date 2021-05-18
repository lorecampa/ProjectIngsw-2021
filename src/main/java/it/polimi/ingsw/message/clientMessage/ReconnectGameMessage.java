package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.*;

import java.util.ArrayList;

public class ReconnectGameMessage implements ClientMessage{

    // for all
    private final ArrayList<String> usernames;
    private final MarketData market;
    private final DeckDevData deckDev;
    private final ArrayList<EffectData> baseProd;

    private final ArrayList<ModelData> models;

    @JsonCreator
    public ReconnectGameMessage(@JsonProperty("usernames") ArrayList<String> usernames,
                                @JsonProperty("market")MarketData market, @JsonProperty("deckDev")DeckDevData deckDev,
                                @JsonProperty("baseProd")ArrayList<EffectData> baseProd, @JsonProperty("models")ArrayList<ModelData> models) {
        this.usernames = usernames;
        this.market = market;
        this.deckDev = deckDev;
        this.baseProd = baseProd;
        this.models = models;
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

    public ArrayList<EffectData> getBaseProd() {
        return baseProd;
    }

    public ArrayList<ModelData> getModels() {
        return models;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.reconnectGameSetUp(this);
    }
}
