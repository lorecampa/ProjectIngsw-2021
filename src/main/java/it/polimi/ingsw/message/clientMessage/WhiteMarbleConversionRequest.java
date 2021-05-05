package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WhiteMarbleConversionRequest {
    private final int numOfWhiteMarbleDrew;

    @JsonCreator
    public WhiteMarbleConversionRequest(@JsonProperty("numOfWhiteMarbleDrew") int numOfWhiteMarbleDrew) {
        this.numOfWhiteMarbleDrew = numOfWhiteMarbleDrew;
    }

    public int getNumOfWhiteMarbleDrew() {
        return numOfWhiteMarbleDrew;
    }
}
