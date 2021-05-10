package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.server.ServerMessageHandler;

import java.util.ArrayList;

public class AnyRequirementResponse implements ServerMessage {
    private final ArrayList<ResourceData> resources;
    private final boolean isBuyDevelopmentAny;

    @JsonCreator
    public AnyRequirementResponse(@JsonProperty("resources") ArrayList<ResourceData> resources,
                                  @JsonProperty("buydevelopmentany") boolean isProductionAny) {
        this.resources = resources;
        this.isBuyDevelopmentAny = isProductionAny;
    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }

    public boolean isBuyDevelopmentAny() {
        return isBuyDevelopmentAny;
    }


    @Override
    public void process(ServerMessageHandler handler) {
        System.out.println("AnyRequirementResponseHandler");
    }
}
