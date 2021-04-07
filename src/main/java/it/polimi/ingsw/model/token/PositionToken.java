package it.polimi.ingsw.model.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionToken implements Token {
    private int position;
    private final boolean isShuffle;
    private LorenzoIlMagnifico lorenzoIlMagnifico = null;

    @JsonCreator
    public PositionToken(@JsonProperty("position") int position,
                         @JsonProperty("isShuffle") boolean isShuffle) {
        this.position = position;
        this.isShuffle = isShuffle;
    }

    @Override
    public void doActionToken() {
        lorenzoIlMagnifico.increaseFaithPosition(position);
        if(isShuffle) lorenzoIlMagnifico.shuffleToken();
    }

    @Override
    public void attachLorenzoIlMagnifico(LorenzoIlMagnifico lorenzoIlMagnifico) {
        this.lorenzoIlMagnifico = lorenzoIlMagnifico;
    }

    @Override
    public String toString() {
        return "\nPositionToken" +
                "\nposition= " + position +
                "\nisShuffle= " + isShuffle;
    }
}
