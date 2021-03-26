package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.model.resource.Resource;


import java.util.ArrayList;
import java.util.Collections;

/**
 * Class that will manage the game Market in which a player can insert a marble in the marketTray to acquire resources.
 */

public class Market {
    private ArrayList<ArrayList<Marble>> marketTray;
    private Marble marbleToInsert;
    private int numOfWhiteMarbleDrew = 0;
    private final ArrayList<Resource> resourcesToSend = new ArrayList<>();

    /**
     * Constructor of the class market, that will setup the market tray in a random state with a set number of each
     * type of marble.
     */

    public Market() {
        // array in which will be stored all marbles to insert in the market tray
        ArrayList<Marble> allMarbles = new ArrayList<>();

        // creation of all the marbles
        for (int numNormalMarble = 0; numNormalMarble < 2; numNormalMarble++) {
            allMarbles.add(new BlueMarble(this));
            allMarbles.add(new YellowMarble(this));
            allMarbles.add(new GreyMarble(this));
            allMarbles.add(new PurpleMarble(this));
        }
        for (int numWhiteMarble = 0; numWhiteMarble < 4; numWhiteMarble++) {
            allMarbles.add(new WhiteMarble(this));
        }
        allMarbles.add(new RedMarble(this));

        // shuffle the marbles
        Collections.shuffle(allMarbles);

        // setup of the market tray using the shuffled array of marble
        this.marketTray = new ArrayList<>();
        for (int numOfCol = 0; numOfCol < 4; numOfCol++) {
            ArrayList<Marble> marketCol = new ArrayList<>();
            marketCol.add(allMarbles.get(numOfCol * 3));
            marketCol.add(allMarbles.get(numOfCol * 3 + 1));
            marketCol.add(allMarbles.get(numOfCol * 3 + 2));

            marketTray.add(marketCol);
        }

        // set the marble to insert with the last marble
        this.marbleToInsert = allMarbles.get(12);
    }


    public void addInResourcesToSend(Resource resource) throws NegativeResourceException {
        if(resourcesToSend.contains(resource)){
            resourcesToSend.get(resourcesToSend.indexOf(resource)).addValue(resource.getValue());
        }else {
            resourcesToSend.add(resource);
        }
    }
}
