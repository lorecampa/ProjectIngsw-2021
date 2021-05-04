package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.ConnectionMessage;
import it.polimi.ingsw.message.PingPongMessage;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PingPongMessage.class, name = "PingPongMessage"),
        @JsonSubTypes.Type(value = ConnectionMessage.class, name = "ConnectionMessage"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "ErrorMessage"),
        @JsonSubTypes.Type(value = AnyConversionRequest.class, name = "AnyConversionRequest"),
        @JsonSubTypes.Type(value = ChooseAction.class, name = "ChooseAction"),
        @JsonSubTypes.Type(value = EndTurn.class, name = "EndTurn"),
        @JsonSubTypes.Type(value = FaithTrackIncrement.class, name = "FaithTrackIncrement"),
        @JsonSubTypes.Type(value = ResourceManagement.class, name = "ResourceManagement"),
        @JsonSubTypes.Type(value = StarTurn.class, name = "StarTurn"),
        @JsonSubTypes.Type(value = WarehousePlayerUpdate.class, name = "WarehousePlayerUpdate"),
        @JsonSubTypes.Type(value = WhiteMarbleConversionRequest.class, name = "WhiteMarbleConversionRequest")

})
public interface ClientMessage extends Message{
    void process (ClientMessageHandler handler);
}
