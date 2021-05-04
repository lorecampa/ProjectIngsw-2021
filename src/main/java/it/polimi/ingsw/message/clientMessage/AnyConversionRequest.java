package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;

import java.util.ArrayList;

public class AnyConversionRequest {
    private final ArrayList<ResourceData> optionConversion;
    private final int numOfAnyRequirement;
    private final int numOfAnyProduction;

    @JsonCreator
    public AnyConversionRequest(@JsonProperty("optionConversion") ArrayList<ResourceData> optionConversion,
                                @JsonProperty("numOfAnyRequirement")int numOfAnyRequirement,
                                @JsonProperty("numOfAnyProduction")int numOfAnyProduction) {
        this.optionConversion = optionConversion;
        this.numOfAnyRequirement = numOfAnyRequirement;
        this.numOfAnyProduction = numOfAnyProduction;
    }
    @JsonCreator
    public AnyConversionRequest(@JsonProperty("optionConversion")ArrayList<ResourceData> optionConversion,
                                @JsonProperty("numOfAnyRequirement")int numOfAnyRequirement) {
        this.optionConversion = optionConversion;
        this.numOfAnyRequirement = numOfAnyRequirement;
        this.numOfAnyProduction = 0;
    }
    @JsonCreator
    public AnyConversionRequest(@JsonProperty("numOfAnyProduction")int numOfAnyProduction) {
        this.optionConversion = null;
        this.numOfAnyRequirement = 0;
        this.numOfAnyProduction = numOfAnyProduction;
    }

    public ArrayList<ResourceData> getOptionConversion() {
        return optionConversion;
    }

    public int getNumOfAnyRequirement() {
        return numOfAnyRequirement;
    }

    public int getNumOfAnyProduction() {
        return numOfAnyProduction;
    }
}
