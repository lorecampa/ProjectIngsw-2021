package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.bothArchitectureMessage.*;
import it.polimi.ingsw.server.ServerMessageHandler;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConnectionMessage.class, name = "ConnectionMessage"),
        @JsonSubTypes.Type(value = DepotModify.class, name = "DepotModify"),
        @JsonSubTypes.Type(value = DepotSwitch.class, name = "DepotSwitch"),
        @JsonSubTypes.Type(value = PingPongMessage.class, name = "PingPongMessage"),
        @JsonSubTypes.Type(value = StrongboxModify.class, name = "StrongboxModify"),
        @JsonSubTypes.Type(value = AnyProductionResponse.class, name = "AnyProductionResponse"),
        @JsonSubTypes.Type(value = AnyRequirementResponse.class, name = "AnyRequirementResponse"),
        @JsonSubTypes.Type(value = DevelopmentAction.class, name = "DevelopmentAction"),
        @JsonSubTypes.Type(value = DrawToken.class, name = "DrawToken"),
        @JsonSubTypes.Type(value = LeaderManage.class, name = "LeaderManage"),
        @JsonSubTypes.Type(value = MarketAction.class, name = "MarketAction"),
        @JsonSubTypes.Type(value = ProductionAction.class, name = "ProductionAction"),
        @JsonSubTypes.Type(value = ReconnectionMessage.class, name = "ReconnectionMessage"),
        @JsonSubTypes.Type(value = WhiteMarbleForLeader.class, name = "WhiteMarbleForLeader")
})

public interface ServerMessage extends Message {
    void process (ServerMessageHandler handler);
}
