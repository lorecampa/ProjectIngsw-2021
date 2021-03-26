package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.activationEffect.OnActivationEffect;
import it.polimi.ingsw.model.card.requirement.Requirement;

import java.util.ArrayList;

public class Development extends  Card{

    private int level;
    private Color color;

    public Development(int victoryPoints, int owner,
                       ArrayList<Requirement> requirements,
                       ArrayList<OnActivationEffect> onActivationEffects,
                       int level, Color color) {

        super(victoryPoints, owner, requirements, onActivationEffects);
        this.level = level;
        this.color = color;
    }

    public void buyCard(){
        //TODO
        //remove resources from strongbox and wharehouse

    }


}
