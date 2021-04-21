package it.polimi.ingsw.model.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.model.card.Color;

public class CardToken implements Token{
    private final Color cardColor;
    private final int numDiscard;

    @JsonCreator
    public CardToken(@JsonProperty("cardColor") Color cardColor,
                     @JsonProperty("numDiscard") int numDiscard) {
        this.cardColor = cardColor;
        this.numDiscard = numDiscard;
    }

    @Override
    public void doActionToken(LorenzoIlMagnifico lorenzoIlMagnifico) throws DeckDevelopmentCardException {
        lorenzoIlMagnifico.discardDevelopment(cardColor, numDiscard);
    }



    @Override
    public String toString() {
        return "\nCardToken" +
                "\ncardColor= " + cardColor +
                "\nnumDiscard=" + numDiscard;
    }
}
