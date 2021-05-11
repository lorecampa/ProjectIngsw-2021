package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;

public class PopeFavorActivated implements ClientMessage{

    private final int popeSpaceCell;
    private final boolean discard;
    private final String username;


    @JsonCreator
    public PopeFavorActivated(@JsonProperty("popeSpaceCell") int popeSpaceCell,
                              @JsonProperty("discard") boolean discard,
                              @JsonProperty("username") String username) {

        this.popeSpaceCell = popeSpaceCell;
        this.discard = discard;
        this.username = username;
    }


    public int getPopeSpaceCell() {
        return popeSpaceCell;
    }

    public boolean isDiscard() {
        return discard;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("PopeFavorActivated Handler");
    }
}
