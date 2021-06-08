package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.server.ServerMessageHandler;

public class WhiteMarbleConversionResponse implements ServerMessage{
    private final int leaderIndex;
    private final int numOfWhiteMarble;

    @JsonCreator
    public WhiteMarbleConversionResponse(@JsonProperty("leaderIndex")int leaderIndex,
                                         @JsonProperty("numOfWhiteMarble")int numOfWhiteMarble) {
        this.leaderIndex = leaderIndex;
        this.numOfWhiteMarble = numOfWhiteMarble;
    }

    public int getLeaderIndex() {
        return leaderIndex;
    }

    public int getNumOfWhiteMarble() {
        return numOfWhiteMarble;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleWhiteMarbleConversion(this);

        handler.getVirtualClient().ifPresent(x->x.addToLog(this));
    }

    @Override
    public String toString() {
        return " - White Marble Conversion Response";
    }
}
