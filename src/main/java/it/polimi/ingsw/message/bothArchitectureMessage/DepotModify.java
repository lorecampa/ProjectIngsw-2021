package it.polimi.ingsw.message.bothArchitectureMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.Optional;

public class DepotModify implements ClientMessage, ServerMessage {
    private final int depotIndex;
    private final ResourceData resource;
    private final boolean isAdd;
    private final boolean isDepotLeader;
    private final String username;

    @JsonCreator
    public DepotModify(@JsonProperty("depotIndex") int depotIndex,
                       @JsonProperty("resource")ResourceData resource,
                       @JsonProperty("idDepotLeader") boolean isDepotLeader,
                       @JsonProperty("username")String username) {
        this.depotIndex = depotIndex;
        this.resource = resource;
        this.isDepotLeader = isDepotLeader;
        this.username = username;

        this.isAdd = false;
    }

    @JsonCreator
    public DepotModify(@JsonProperty("depotIndex") int depotIndex,
                       @JsonProperty("resource")ResourceData resource,
                       @JsonProperty("idDepotLeader") boolean isDepotLeader,
                       @JsonProperty("isAdd")boolean isAdd) {
        this.depotIndex = depotIndex;
        this.resource = resource;
        this.isAdd = isAdd;
        this.isDepotLeader = isDepotLeader;
        this.username = null;
    }

    public int getDepotIndex() {
        return depotIndex;
    }

    public boolean isDepotLeader() {
        return isDepotLeader;
    }

    public ResourceData getResource() {
        return resource;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("DepotModifyHandler");
    }

    @Override
    public void process(ServerMessageHandler handler) {
        System.out.println("DepotModifyHandler");
    }
}
