package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChooseAction {
    private final boolean isNormalActionPossible;
    private final boolean isLeaderManagingPossible;

    @JsonCreator
    public ChooseAction(@JsonProperty("isNormalActionPossible")boolean isNormalActionPossible,
                        @JsonProperty("isLeaderManagingPossible")boolean isLeaderManagingPossible) {
        this.isNormalActionPossible = isNormalActionPossible;
        this.isLeaderManagingPossible = isLeaderManagingPossible;
    }

    public boolean isNormalActionPossible() {
        return isNormalActionPossible;
    }

    public boolean isLeaderManagingPossible() {
        return isLeaderManagingPossible;
    }
}
