package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WhiteMarbleForLeader {
    private final int leaderIndex;
    private final int numOfWhiteMarble;

    @JsonCreator
    public WhiteMarbleForLeader(@JsonProperty("leaderIndex")int leaderIndex,
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
}
