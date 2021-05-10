package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.bothArchitectureMessage.*;
import it.polimi.ingsw.message.serverMessage.DepotModify;
import it.polimi.ingsw.message.serverMessage.DepotSwitch;
import it.polimi.ingsw.message.serverMessage.StrongboxModify;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConnectionMessage.class, name = "ConnectionMessage"),
        @JsonSubTypes.Type(value = DepotModify.class, name = "DepotModify"),
        @JsonSubTypes.Type(value = DepotSwitch.class, name = "DepotSwitch"),
        @JsonSubTypes.Type(value = PingPongMessage.class, name = "PingPongMessage"),
        @JsonSubTypes.Type(value = StrongboxModify.class, name = "StrongboxModify"),

        @JsonSubTypes.Type(value = AnyConversionRequest.class, name = "AnyConversionRequest"),
        @JsonSubTypes.Type(value = BufferUpdate.class, name = "BufferUpdate"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "ErrorMessage"),
        @JsonSubTypes.Type(value = FaithTrackIncrement.class, name = "FaithTrackIncrement"),
        @JsonSubTypes.Type(value = GameSetup.class, name = "GameSetup"),
        @JsonSubTypes.Type(value = MainMenuMessage.class, name = "MainMenuMessage"),
        @JsonSubTypes.Type(value = MarketUpdate.class, name = "MarketUpdate"),
        @JsonSubTypes.Type(value = NewTurn.class, name = "NewTurn"),
        @JsonSubTypes.Type(value = PopeFavorActivated.class, name = "PopeFavorActivated"),
        @JsonSubTypes.Type(value = ReconnectionMessage.class, name = "ReconnectionMessage"),
        @JsonSubTypes.Type(value = RemoveDeckDevelopmentCard.class, name = "RemoveDeckDevelopmentCard"),
        @JsonSubTypes.Type(value = StarTurn.class, name = "StarTurn"),
        @JsonSubTypes.Type(value = WhiteMarbleConverted.class, name = "WhiteMarbleConverted"),
})
public interface ClientMessage extends Message{
    void process (ClientMessageHandler handler);
}
