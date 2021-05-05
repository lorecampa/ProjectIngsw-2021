package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;

import java.util.ArrayList;

public class AnyConversionRequest implements ClientMessage{
    private final ArrayList<ResourceData> optionConversion;
    private final ArrayList<ResourceData> optionOfDiscountNotUsed;
    private final int numOfAny;
    private final boolean isProduction;

    @JsonCreator
    public AnyConversionRequest(@JsonProperty("optionConversion") ArrayList<ResourceData> optionConversion,
                                @JsonProperty("optionOfDiscountNotUsed") ArrayList<ResourceData> optionOfDiscountNotUsed,
                                @JsonProperty("numOfAny")int numOfAny) {
        this.optionConversion = optionConversion;
        this.optionOfDiscountNotUsed = optionOfDiscountNotUsed;
        this.numOfAny = numOfAny;
        this.isProduction = false;
    }
    @JsonCreator
    public AnyConversionRequest(@JsonProperty("numOfAny")int numOfAny) {
        this.optionConversion = null;
        this.optionOfDiscountNotUsed = null;
        this.numOfAny = numOfAny;
        this.isProduction = true;
    }


    public ArrayList<ResourceData> getOptionConversion() {
        return optionConversion;
    }

    public ArrayList<ResourceData> getOptionOfDiscountNotUsed() {
        return optionOfDiscountNotUsed;
    }

    public int getNumOfAny() {
        return numOfAny;
    }

    public boolean isProduction() {
        return isProduction;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("AnyRequestHandler");
    }
}
