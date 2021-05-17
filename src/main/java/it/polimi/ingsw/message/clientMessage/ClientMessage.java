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
        @JsonSubTypes.Type(value = DepotLeaderUpdate.class, name = "DepotLeaderUpdate"),
        @JsonSubTypes.Type(value = DepotUpdate.class, name = "DepotUpdate"),
        @JsonSubTypes.Type(value = DiscountLeaderUpdate.class, name = "DiscountLeaderUpdate"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "ErrorMessage"),
        @JsonSubTypes.Type(value = GameOver.class, name = "GameOver"),
        @JsonSubTypes.Type(value = GameSetup.class, name = "GameSetup"),
        @JsonSubTypes.Type(value = LeaderActivate.class, name = "LeaderActivate"),
        @JsonSubTypes.Type(value = LeaderDiscard.class, name = "LeaderDiscard"),
        @JsonSubTypes.Type(value = LeaderSetUpMessage.class, name ="LeaderSetUpMessage"),
        @JsonSubTypes.Type(value = MainMenuMessage.class, name = "MainMenuMessage"),
        @JsonSubTypes.Type(value = ManageResourcesRequest.class, name = "ManageResourcesRequest"),
        @JsonSubTypes.Type(value = MarketUpdate.class, name = "MarketUpdate"),
        @JsonSubTypes.Type(value = MatchStart.class, name = "MatchStart"),
        @JsonSubTypes.Type(value = PopeFavorActivated.class, name = "PopeFavorActivated"),
        @JsonSubTypes.Type(value = RemoveDeckDevelopmentCard.class, name = "RemoveDeckDevelopmentCard"),
        @JsonSubTypes.Type(value = StarTurn.class, name = "StarTurn"),
        @JsonSubTypes.Type(value = StrongboxUpdate.class, name = "StrongboxUpdate"),
        @JsonSubTypes.Type(value = WhiteMarbleConversionRequest.class, name = "WhiteMarbleConverted"),
        @JsonSubTypes.Type(value = WinningCondition.class, name = "WinningCondition"),
})

public interface ClientMessage extends Message{
    void process (ClientMessageHandler handler);
}
