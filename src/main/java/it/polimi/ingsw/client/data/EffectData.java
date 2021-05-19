package it.polimi.ingsw.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class EffectData {
    private final String descriptions;
    private final ArrayList<ResourceData> resourcesBefore;
    private final ArrayList<ResourceData> resourcesAfter;

    @JsonCreator
    public EffectData(@JsonProperty("descriptions")String descriptions,
                      @JsonProperty("resourcesBefore")ArrayList<ResourceData> resourcesBefore,
                      @JsonProperty("resourcesAfter")ArrayList<ResourceData> resourcesAfter) {
        this.descriptions = descriptions;
        this.resourcesBefore = resourcesBefore;
        this.resourcesAfter = resourcesAfter;
    }

    @Override
    public String toString() {

        StringBuilder row = new StringBuilder(descriptions + " ");
        for(ResourceData r : resourcesBefore){
            row.append(r.toCli());
        }
        if(resourcesAfter!=null && !resourcesAfter.isEmpty()){
            row.append(">>> ");
            for(ResourceData r : resourcesAfter){
                row.append(r.toCli());
            }
        }
        return row.toString();
    }

    public String getDescriptions() {
        return descriptions;
    }

    public ArrayList<ResourceData> getResourcesBefore() {
        return resourcesBefore;
    }

    public ArrayList<ResourceData> getResourcesAfter() {
        return resourcesAfter;
    }

    public String resourceBeforeToCli(){
        StringBuilder row= new StringBuilder();
        for(ResourceData r : resourcesBefore){
            row.append(r.toCli());
        }
        return row.toString();
    }

    public String resourceAfterToCli(){
        StringBuilder row= new StringBuilder();
        for(ResourceData r : resourcesAfter){
            row.append(r.toCli());
        }
        return row.toString();
    }

}
