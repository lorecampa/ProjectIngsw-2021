package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.CLIMessageHandler;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import java.util.ArrayList;
import java.util.Map;

public class WhiteMarbleConversionRequest implements ClientMessage{
    private final int numOfWhiteMarbleDrew;
    private final Map<Integer, ArrayList<ResourceData>> listOfConversion;

    @JsonCreator
    public WhiteMarbleConversionRequest(@JsonProperty("numOfWhiteMarbleDrew") int numOfWhiteMarbleDrew,
            @JsonProperty("listOfConversion") Map<Integer, ArrayList<ResourceData>> listOfConversion) {

        this.numOfWhiteMarbleDrew = numOfWhiteMarbleDrew;
        this.listOfConversion = listOfConversion;
    }

    public int getNumOfWhiteMarbleDrew() {
        return numOfWhiteMarbleDrew;
    }

    public Map<Integer, ArrayList<ResourceData>> getListOfConversion() {
        return listOfConversion;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.whiteMarbleConversion(this);
    }
}
