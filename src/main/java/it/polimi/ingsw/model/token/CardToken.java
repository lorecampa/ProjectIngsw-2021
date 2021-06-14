package it.polimi.ingsw.model.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.card.Color;

/**
 * Card Token is token that discard cards in the deck development.
 */
public class CardToken implements Token{
    private final Color cardColor;
    private final int numDiscard;

    /**
     * Construct a Card Token with specific attributes.
     * @param cardColor the color of the cards to discard.
     * @param numDiscard the number of cards to discard.
     */
    @JsonCreator
    public CardToken(@JsonProperty("cardColor") Color cardColor,
                     @JsonProperty("numDiscard") int numDiscard) {
        this.cardColor = cardColor;
        this.numDiscard = numDiscard;
    }

    /**
     * Discard the cards in the deck development.
     * @param lorenzoIlMagnifico is a class that can perform LorenzoIlMagnifico's actions.
     */
    @Override
    public void doActionToken(LorenzoIlMagnifico lorenzoIlMagnifico){
        lorenzoIlMagnifico.discardDevelopmentSinglePlayer(cardColor, numDiscard);
    }

    /**
     * Return a string that describe the Position Token.
     * @return a string that describe the Position Token.
     */
    @Override
    public String toString() {
        return "\nCardToken" +
                "\ncardColor= " + cardColor +
                "\nnumDiscard=" + numDiscard;
    }
}
