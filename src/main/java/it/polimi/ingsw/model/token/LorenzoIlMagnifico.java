package it.polimi.ingsw.model.token;

import it.polimi.ingsw.model.card.Color;

/**
 * A class implement LorenzoIlMagnifico to indicate that can perform LorenzoIlMagnifico's action.
 */
public interface LorenzoIlMagnifico {
    /**
     * Discard a number of card of a specific color in the deck development.
     * @param color the color to discard.
     * @param num the number of card to discard.
     */
    void discardDevelopmentSinglePlayer(Color color, int num);

    /**
     * Shuffle all token in the token's deck.
     */
    void shuffleToken();

    /**
     * Increase the position of LorenzoIlMagnifico on the faith track.
     * @param pos the number of positions.
     */
    void increaseLorenzoFaithPosition(int pos);
}
