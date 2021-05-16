package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;

import java.util.ArrayList;

public class DiscountLeaderUpdate implements ClientMessage{
    private final ArrayList<ResourceData> discounts;
    private final boolean discard;

    @JsonCreator
    public DiscountLeaderUpdate(@JsonProperty("discount") ArrayList<ResourceData> discounts,
                                @JsonProperty("discard") boolean discard) {
        this.discounts = discounts;
        this.discard = discard;
    }

    public ArrayList<ResourceData> getDiscounts() {
        return discounts;
    }

    public boolean isDiscard() {
        return discard;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.discountLeaderUpdate(this);
    }
}
