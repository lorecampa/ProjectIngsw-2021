package it.polimi.ingsw.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class EffectData {
    String descriptions;
    ArrayList<ResourceData> resourcesBefore;
    ArrayList<ResourceData> resourcesAfter;

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
        StringBuilder row= new StringBuilder();
        row = new StringBuilder(descriptions + ": ");
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
        String row= "";
        for(ResourceData r : resourcesBefore){
            row+=r.toCli();
        }
        return row;
    }

    public String resourceAfterToCli(){
        String row= "";
        for(ResourceData r : resourcesAfter){
            row+=r.toCli();
        }
        return row;
    }

}
