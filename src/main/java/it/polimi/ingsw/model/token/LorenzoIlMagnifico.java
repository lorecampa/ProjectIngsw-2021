package it.polimi.ingsw.model.token;

import it.polimi.ingsw.model.card.Color;

public interface LorenzoIlMagnifico {
    void discardDevelopmentSinglePlayer(Color color, int num);
    void shuffleToken();
    void increaseLorenzoFaithPosition(int pos);
}
