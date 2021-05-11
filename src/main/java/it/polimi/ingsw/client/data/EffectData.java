package it.polimi.ingsw.client.data;

import java.util.ArrayList;

public class EffectData {
    String descriptions;
    ArrayList<ResourceData> resourcesBefore;
    ArrayList<ResourceData> resourcesAfter;

    public EffectData(String descriptions, ArrayList<ResourceData> resourcesBefore, ArrayList<ResourceData> resourcesAfter) {
        this.descriptions = descriptions;
        this.resourcesBefore = resourcesBefore;
        this.resourcesAfter = resourcesAfter;
    }
}
