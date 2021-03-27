package it.polimi.ingsw.model.personalBoard.market;

import it.polimi.ingsw.exception.NegativeResourceException;
import it.polimi.ingsw.exception.WrongMarketDimensionException;
import it.polimi.ingsw.exception.WrongMarblesNumberException;
import it.polimi.ingsw.model.resource.Resource;


import java.util.ArrayList;
import java.util.Collections;

/**
 * Class that will manage the game Market in which a player can insert a marble in the marketTray to acquire resources.
 */
public class Market {
    private final int numCol;
    private final int numRow;
    private final int numBlueMarble;
    private final int numGreyMarble;
    private final int numPurpleMarble;
    private final int numRedMarble;
    private final int numWhiteMarble;
    private final int numYellowMarble;



    private ArrayList<ArrayList<Marble>> marketTray;
    private Marble marbleToInsert;
    private int numOfWhiteMarbleDrew = 0;
    private final ArrayList<Resource> resourcesToSend = new ArrayList<>();

    /**
     * Constructor of the class market, that will setup the market tray in a random state with the chosen number of each
     * type of marble.
     * @param numCol is the number of columns
     * @param numRow is the number of rows
     * @param numBlueMarble is the number of blue marbles
     * @param numGreyMarble is the number of grey marbles
     * @param numPurpleMarble is the number of purple marbles
     * @param numRedMarble is the number of red marbles
     * @param numWhiteMarble is the number of white marbles
     * @param numYellowMarble is the number of yellow marbles
     */
    public Market(int numCol, int numRow, int numBlueMarble, int numGreyMarble, int numPurpleMarble,
                  int numRedMarble, int numWhiteMarble, int numYellowMarble)
            throws WrongMarketDimensionException, WrongMarblesNumberException {

        this.numCol = numCol;
        this.numRow = numRow;
        this.numBlueMarble = numBlueMarble;
        this.numGreyMarble = numGreyMarble;
        this.numPurpleMarble = numPurpleMarble;
        this.numRedMarble = numRedMarble;
        this.numWhiteMarble = numWhiteMarble;
        this.numYellowMarble = numYellowMarble;

        // check if it's possible to create the market
        if (numCol <= 0 || numRow <= 0)
            throw new WrongMarketDimensionException("Number of rows/columns negative or zero");

        int numOfMarbles = numBlueMarble + numPurpleMarble + numGreyMarble +
                           numRedMarble + numWhiteMarble + numYellowMarble;

        if (numOfMarbles != (numCol * numRow) + 1)
            throw new WrongMarblesNumberException("Number of marbles does not match the dimension of the market");

        // array in which will be stored all marbles to insert in the market tray
        ArrayList<Marble> allMarbles = new ArrayList<>();

        // creation of all marbles
        for (int i = 0; i < numBlueMarble; i++) {
            allMarbles.add(new BlueMarble(this));
        }
        for (int i = 0; i < numYellowMarble; i++) {
            allMarbles.add(new YellowMarble(this));
        }
        for (int i = 0; i < numGreyMarble; i++) {
            allMarbles.add(new GreyMarble(this));
        }
        for (int i = 0; i < numPurpleMarble; i++) {
            allMarbles.add(new PurpleMarble(this));
        }
        for (int i = 0; i < numRedMarble; i++) {
            allMarbles.add(new RedMarble(this));
        }
        for (int i = 0; i < numWhiteMarble; i++) {
            allMarbles.add(new WhiteMarble(this));
        }

        // shuffle the marbles
        Collections.shuffle(allMarbles);

        // setup of the market tray using the shuffled array of marble
        this.marketTray = new ArrayList<>();

        for (int i = 0; i < numCol; i++) {
            ArrayList<Marble> marketCol = new ArrayList<>();
            for (int j = 0; j < numRow; j++) {
                marketCol.add(allMarbles.get(i*numRow + j));
            }
            marketTray.add(marketCol);
        }

        // set the marble to insert with the last marble
        this.marbleToInsert = allMarbles.get(numCol*numRow);
    }

    /**
     * Method to add a resource in the array that will be send to the resource manager.
     * @param resource is the resource that need to be added.
     * @throws NegativeResourceException if the resource saved in the array will have a negative value
     */
    public void addInResourcesToSend(Resource resource) throws NegativeResourceException {
        if(resourcesToSend.contains(resource)){
            resourcesToSend.get(resourcesToSend.indexOf(resource)).addValue(resource.getValue());
        }else {
            resourcesToSend.add(resource);
        }
    }

    /**
     * Method to increase the value that track the number of white marbles drew.
     */
    public void increaseWhiteMarbleDrew(){
        numOfWhiteMarbleDrew++;
    }

    /**
     * Method to get the number of white marble drew.
     * @return is the number of white marble drew
     */
    public int getWhiteMarbleDrew(){
        return numOfWhiteMarbleDrew;
    }

}
