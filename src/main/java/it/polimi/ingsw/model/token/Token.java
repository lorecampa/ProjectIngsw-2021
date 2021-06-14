package it.polimi.ingsw.model.token;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A class implements Token to indicate that's a token.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PositionToken.class, name = "PositionToken"),
        @JsonSubTypes.Type(value = CardToken.class, name = "CardToken")
})
public interface Token{
    /**
     * Perform the action defined by the type of the token.
     * @param lorenzoIlMagnifico is a class that can perform LorenzoIlMagnifico's actions.
     */
    void doActionToken(LorenzoIlMagnifico lorenzoIlMagnifico);
}

