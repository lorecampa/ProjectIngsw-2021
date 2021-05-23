package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.CLIMessageHandler;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.MarketData;

public class MarketUpdate implements ClientMessage{
    private final MarketData market;

    @JsonCreator
    public MarketUpdate(@JsonProperty("market")MarketData market) {
        this.market = market;
    }

    public MarketData getMarket() {
        return market;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.marketUpdate(this);
    }
}
