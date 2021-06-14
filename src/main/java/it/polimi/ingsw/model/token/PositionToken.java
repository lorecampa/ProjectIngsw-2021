package it.polimi.ingsw.model.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Position Token is token that perform an action on LorenzoIlMagnifico's faith track position.
 */
public class PositionToken implements Token {
    private final int position;
    private final boolean isShuffle;

    /**
     * Construct a Position Token with specific attributes.
     * @param position the number of positions to move LorenzoIlMagnifico
     * @param isShuffle true if the token will shuffle the token's deck.
     */
    @JsonCreator
    public PositionToken(@JsonProperty("position") int position,
                         @JsonProperty("isShuffle") boolean isShuffle) {
        this.position = position;
        this.isShuffle = isShuffle;
    }

    /**
     * Increase LorenzoIlMagnifico position on faith track and shuffle the token's deck is isShuffle is true.
     * @param lorenzoIlMagnifico is a class that can perform LorenzoIlMagnifico's actions.
     */
    @Override
    public void doActionToken(LorenzoIlMagnifico lorenzoIlMagnifico) {
        lorenzoIlMagnifico.increaseLorenzoFaithPosition(position);
        if(isShuffle) lorenzoIlMagnifico.shuffleToken();
    }

    /**
     * Return a string that describe the Position Token.
     * @return a string that describe the Position Token.
     */
    @Override
    public String toString() {
        return "\nPositionToken" +
                "\nposition= " + position +
                "\nisShuffle= " + isShuffle;
    }
}
