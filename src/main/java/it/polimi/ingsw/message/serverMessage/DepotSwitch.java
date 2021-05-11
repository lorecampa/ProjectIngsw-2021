package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import java.util.Optional;

public class DepotSwitch implements ClientMessage {
    private final int from;
    private final boolean fromLeader;
    private final int to;
    private final boolean toLeader;

    @JsonCreator
    public DepotSwitch(@JsonProperty("from")int from,
                       @JsonProperty("fromLeader")boolean fromLeader,
                       @JsonProperty("to")int to,
                       @JsonProperty("toLeader")boolean toLeader) {
        this.from = from;
        this.fromLeader = fromLeader;
        this.to = to;
        this.toLeader = toLeader;
    }

    public int getFrom() {
        return from;
    }



    public int getTo() {
        return to;
    }

    public boolean isFromLeader() {
        return fromLeader;
    }

    public boolean isToLeader() {
        return toLeader;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("DepotSwitchHandler");
    }
}
