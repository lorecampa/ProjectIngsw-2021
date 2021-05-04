package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;

import java.util.ArrayList;

public class AnyResponse {
    private final ArrayList<ResourceData> resources;
    private final Boolean isProductionAny;

    @JsonCreator
    public AnyResponse(@JsonProperty("resources") ArrayList<ResourceData> resources,
                       @JsonProperty("isProductionAny")Boolean isProductionAny) {
        this.resources = resources;
        this.isProductionAny = isProductionAny;
    }

    public ArrayList<ResourceData> getResources() {
        return resources;
    }

    public Boolean getProductionAny() {
        return isProductionAny;
    }
}
