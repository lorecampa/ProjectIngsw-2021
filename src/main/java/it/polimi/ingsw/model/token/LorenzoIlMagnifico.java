package it.polimi.ingsw.model.token;

import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.model.card.Color;

public interface LorenzoIlMagnifico {
    void discardDevelopment(Color color, int num) throws DeckDevelopmentCardException;
    void shuffleToken();
    void increaseFaithPosition(int pos);
}
