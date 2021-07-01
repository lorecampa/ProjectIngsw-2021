package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ReconnectGameMessage implements ClientMessage{

    // for all
    private final ArrayList<String> usernames;
    private final MarketData market;
    private final DeckDevData deckDev;
    private final ArrayList<EffectData> baseProd;
    private final ArrayList<ModelData> models;
    //player username
    private final String playerUsername;


    @JsonCreator
    public ReconnectGameMessage(@JsonProperty("usernames") ArrayList<String> usernames,
                                @JsonProperty("market")MarketData market,
                                @JsonProperty("deckDev")DeckDevData deckDev,
                                @JsonProperty("baseProd")ArrayList<EffectData> baseProd,
                                @JsonProperty("models")ArrayList<ModelData> models,
                                @JsonProperty("playerUsername") String playerUsername) {
        this.usernames = usernames;
        this.market = market;
        this.deckDev = deckDev;
        this.baseProd = baseProd;
        this.models = models;
        this.playerUsername = playerUsername;
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

    public String getPlayerUsername() {
        return playerUsername;
    }


    @Override
    public void process(ClientMessageHandler handler) {
        handler.reconnectGameSetUp(this);
    }
}
