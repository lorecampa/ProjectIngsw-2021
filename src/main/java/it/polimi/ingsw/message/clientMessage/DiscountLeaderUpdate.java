package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;

import java.util.ArrayList;

public class DiscountLeaderUpdate implements ClientMessage{
    private final ArrayList<ResourceData> discounts;
    private final boolean discard;
    private final String username;

    @JsonCreator
    public DiscountLeaderUpdate(@JsonProperty("discount") ArrayList<ResourceData> discounts,
                                @JsonProperty("discard") boolean discard,
                                @JsonProperty("username") String username) {
        this.discounts = discounts;
        this.discard = discard;
        this.username = username;
    }

    public ArrayList<ResourceData> getDiscounts() {
        return discounts;
    }

    public boolean isDiscard() {
        return discard;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.discountLeaderUpdate(this);
    }
}
