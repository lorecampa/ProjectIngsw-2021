package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.bothArchitectureMessage.*;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConnectionMessage.class, name = "ConnectionMessage"),
        @JsonSubTypes.Type(value = PingPongMessage.class, name = "PingPongMessage"),
        @JsonSubTypes.Type(value = ReconnectionMessage.class, name = "ReconnectionMessage"),

        @JsonSubTypes.Type(value = AnyConversionRequest.class, name = "AnyConversionRequest"),
        @JsonSubTypes.Type(value = BufferUpdate.class, name = "BufferUpdate"),
        @JsonSubTypes.Type(value = CardSlotUpdate.class, name = "CardSlotUpdate"),
        @JsonSubTypes.Type(value = DepotUpdate.class, name = "DepotUpdate"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "ErrorMessage"),
        @JsonSubTypes.Type(value = FaithTrackIncrement.class, name = "FaithTrackIncrement"),
        @JsonSubTypes.Type(value = GameSetup.class, name = "GameSetup"),
        @JsonSubTypes.Type(value = LeaderStatusUpdate.class, name = "LeaderStatusUpdate"),
        @JsonSubTypes.Type(value = LeaderSetUpMessage.class, name ="LeaderSetUpMessage"),
        @JsonSubTypes.Type(value = MainMenuMessage.class, name = "MainMenuMessage"),
        @JsonSubTypes.Type(value = MarketUpdate.class, name = "MarketUpdate"),
        @JsonSubTypes.Type(value = PopeFavorActivated.class, name = "PopeFavorActivated"),
        @JsonSubTypes.Type(value = RemoveDeckDevelopmentCard.class, name = "RemoveDeckDevelopmentCard"),
        @JsonSubTypes.Type(value = StarTurn.class, name = "StarTurn"),
        @JsonSubTypes.Type(value = StrongboxUpdate.class, name = "StrongboxUpdate"),
        @JsonSubTypes.Type(value = WhiteMarbleConversionRequest.class, name = "WhiteMarbleConverted"),
})
public interface ClientMessage extends Message{
    void process (ClientMessageHandler handler);
}
