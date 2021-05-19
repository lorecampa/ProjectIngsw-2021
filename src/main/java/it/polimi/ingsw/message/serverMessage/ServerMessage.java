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
        @JsonSubTypes.Type(value = PingPongMessage.class, name = "PingPongMessage"),
        @JsonSubTypes.Type(value = ReconnectionMessage.class, name = "ReconnectionMessage"),

        @JsonSubTypes.Type(value = AnyResponse.class, name = "AnyResponse"),
        @JsonSubTypes.Type(value = BaseProduction.class, name = "BaseProduction"),
        @JsonSubTypes.Type(value = DepotModify.class, name = "DepotModify"),
        @JsonSubTypes.Type(value = DepotSwitch.class, name = "DepotSwitch"),
        @JsonSubTypes.Type(value = DevelopmentAction.class, name = "DevelopmentAction"),
        @JsonSubTypes.Type(value = DiscardResourcesFromMarket.class, name = "DiscardResourcesFromMarket"),
        @JsonSubTypes.Type(value = DrawToken.class, name = "DrawToken"),
        @JsonSubTypes.Type(value = EndProductionSelection.class, name = "EndProductionSelection"),
        @JsonSubTypes.Type(value = EndTurn.class, name = "EndTurn"),
        @JsonSubTypes.Type(value = LeaderManage.class, name = "LeaderManage"),
        @JsonSubTypes.Type(value = MarketAction.class, name = "MarketAction"),
        @JsonSubTypes.Type(value = ProductionAction.class, name = "ProductionAction"),
        @JsonSubTypes.Type(value = QuitGame.class, name = "QuitGame"),
        @JsonSubTypes.Type(value = StrongboxModify.class, name = "StrongboxModify"),
        @JsonSubTypes.Type(value = WhiteMarbleConversionResponse.class, name = "WhiteMarbleConversionResponse")
})

public interface ServerMessage extends Message {
    void process (ServerMessageHandler handler);
}
