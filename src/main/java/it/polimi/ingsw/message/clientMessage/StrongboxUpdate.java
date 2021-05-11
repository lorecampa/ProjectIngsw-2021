package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import java.util.ArrayList;

public class StrongboxUpdate implements ClientMessage{
    private final ArrayList<ResourceData> strongboxUpdated;
    private final String username;

    @JsonCreator
    public StrongboxUpdate(@JsonProperty("strongboxUpdated") ArrayList<ResourceData> strongboxUpdated,
                           @JsonProperty("username") String username) {
        this.strongboxUpdated = strongboxUpdated;
        this.username = username;
    }

    public ArrayList<ResourceData> getStrongboxUpdated() {
        return strongboxUpdated;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("StrongboxUpdate Handler");
    }
}
