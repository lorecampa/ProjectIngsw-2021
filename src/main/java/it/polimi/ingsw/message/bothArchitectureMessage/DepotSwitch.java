package it.polimi.ingsw.message.bothArchitectureMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import java.util.Optional;

public class DepotSwitch implements ClientMessage {
    private final int from;
    private final int to;
    private final boolean isToLeader;
    private final String username;

    @JsonCreator
    public DepotSwitch(@JsonProperty("from")int from,
                       @JsonProperty("to")int to,
                       @JsonProperty("isToLeader")boolean isToLeader,
                       @JsonProperty("username") String username) {
        this.from = from;
        this.to = to;
        this.isToLeader = isToLeader;
        this.username = username;
    }

    @JsonCreator
    public DepotSwitch(@JsonProperty("from")int from,
                       @JsonProperty("to")int to,
                       @JsonProperty("isToLeader")boolean isToLeader) {
        this.from = from;
        this.to = to;
        this.isToLeader = isToLeader;
        this.username = null;
    }


    public boolean isToLeader() {
        return isToLeader;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }


    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("DepotSwitchHandler");
    }
}
