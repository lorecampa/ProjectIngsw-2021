package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.message.ClientMessageHandler;
import it.polimi.ingsw.message.Message;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")

@JsonSubTypes({
        @JsonSubTypes.Type(value = BuyDevelopmentCardMessage.class, name = "BuyDevelopmentCardMessage"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "ErrorMessage"),
        @JsonSubTypes.Type(value = ActivateProductionMessage.class, name = "ActivateProductionMessage")
})
public interface ClientMessage extends Message<ClientMessageHandler>{

}
