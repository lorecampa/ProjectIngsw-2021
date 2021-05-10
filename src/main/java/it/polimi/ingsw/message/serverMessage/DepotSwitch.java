package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import java.util.Optional;

public class DepotSwitch implements ClientMessage {
    private final int from;
    private final int isFromLeader;
    private final int to;
    private final boolean isToLeader;



    @JsonCreator
    public DepotSwitch(@JsonProperty("from")int from,
                       @JsonProperty("isFromLeader")int isFromLeader,
                       @JsonProperty("to")int to,
                       @JsonProperty("isToLeader")boolean isToLeader) {
        this.from = from;
        this.isFromLeader = isFromLeader;
        this.to = to;
        this.isToLeader = isToLeader;
    }

    public int getFrom() {
        return from;
    }

    public int getIsFromLeader() {
        return isFromLeader;
    }

    public int getTo() {
        return to;
    }

    public boolean isToLeader() {
        return isToLeader;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("DepotSwitchHandler");
    }
}
