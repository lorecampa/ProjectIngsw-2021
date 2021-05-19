package it.polimi.ingsw.model.token;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PositionToken.class, name = "PositionToken"),
        @JsonSubTypes.Type(value = CardToken.class, name = "CardToken")
})
public interface Token{
    void doActionToken(LorenzoIlMagnifico lorenzoIlMagnifico);
}

