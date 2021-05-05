package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;

public class FaithTrackIncrement implements ClientMessage{
    private final int position;
    private final String username;

    @JsonCreator
    public FaithTrackIncrement(@JsonProperty("position")int position,
                               @JsonProperty("username")String username) {
        this.position = position;
        this.username = username;
    }

    public int getPosition() {
        return position;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("FaithTrackPositionHandler");
    }
}
