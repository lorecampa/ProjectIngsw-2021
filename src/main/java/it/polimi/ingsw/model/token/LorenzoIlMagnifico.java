package it.polimi.ingsw.model.token;

import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.model.card.Color;

public interface LorenzoIlMagnifico {
    void discardDevelopmentSinglePlayer(Color color, int num) throws DeckDevelopmentCardException;
    void shuffleToken();
    void increaseLorenzoFaithPosition(int pos);
}
