package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;
import java.util.ArrayList;

public class WhiteMarbleConversionRequest implements ClientMessage{
    private final int numOfWhiteMarbleDrew;
    private final ArrayList<ResourceData> whiteMarbleConversion;

    @JsonCreator
    public WhiteMarbleConversionRequest(@JsonProperty("numOfWhiteMarbleDrew") int numOfWhiteMarbleDrew,
            @JsonProperty("whiteMarbleConversion") ArrayList<ResourceData> whiteMarbleConversion) {

        this.numOfWhiteMarbleDrew = numOfWhiteMarbleDrew;
        this.whiteMarbleConversion = whiteMarbleConversion;
    }

    public int getNumOfWhiteMarbleDrew() {
        return numOfWhiteMarbleDrew;
    }

    public ArrayList<ResourceData> getWhiteMarbleConversion() {
        return whiteMarbleConversion;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("WhiteMarbleConvertedHandler");
    }
}
