package it.polimi.ingsw.model.personalBoard.market;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.exception.WrongMarketDimensionException;
import it.polimi.ingsw.exception.WrongMarblesNumberException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.observer.GameMasterObservable;
import it.polimi.ingsw.observer.GameMasterObserver;
import it.polimi.ingsw.observer.MarketObserver;
import it.polimi.ingsw.observer.Observable;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Class that manage the game Market in which a player can insert a marble in the marketTray to acquire resources.
 */
public class Market extends GameMasterObservable implements Observable<MarketObserver> {
    List<MarketObserver> marketObserverList = new ArrayList<>();

    private final int numCol;
    private final int numRow;
    private final ArrayList<ArrayList<Marble>> marketTray;
    private Marble marbleToInsert;
    private int numOfWhiteMarbleDrew = 0;
    private int whiteMarbleToTransform = 0;
    private final ArrayList<Resource> resourcesToSend = new ArrayList<>();

    /**
     * Constructor of the class market, that will setup the market tray in a random state with the chosen number of each
     * type of marble.
     * @param numCol is the number of columns
     * @param numRow is the number of rows
     * @param allMarbles is the array that contains all the marbles of the market
     * @throws WrongMarketDimensionException if it's impossible to create a market with that dimensions
     * @throws WrongMarblesNumberException  if the number of marbles does not match with the dimension of the market
     */
    @JsonCreator
    public Market(@JsonProperty("numRow") int numRow,
                  @JsonProperty("numCol") int numCol,
                  @JsonProperty("allMarbles") ArrayList<Marble> allMarbles)
            throws WrongMarketDimensionException, WrongMarblesNumberException {

        this.numCol = numCol;
        this.numRow = numRow;

        // check if it's possible to create the market
        if (numCol <= 0 || numRow <= 0)
            throw new WrongMarketDimensionException("Number of rows/columns negative or zero");

        int numOfMarbles = allMarbles.size();

        if (numOfMarbles != (numCol * numRow) + 1)
            throw new WrongMarblesNumberException("Number of marbles does not match the dimension of the market");

        // shuffle the marbles
        Collections.shuffle(allMarbles);

        // setup of the market tray using the shuffled array of marble
        this.marketTray = new ArrayList<>();

        for (int i = 0; i < numRow; i++) {
            ArrayList<Marble> marketCol = new ArrayList<>();
            for (int j = 0; j < numCol; j++) {
                marketCol.add(allMarbles.get(i*numCol + j));
            }
            marketTray.add(marketCol);
        }

        // set the marble to insert with the last marble
        this.marbleToInsert = allMarbles.get(numCol*numRow);



    }

    /**
     * Method to add a resource in the array that will be send to the resource manager.
     * @param resource is the resource that need to be added.
     */
    public void addInResourcesToSend(Resource resource) {
        // check if there's already the resource in the array
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

    /**
     * Method to insert the extra marble in a specific row of the market tray and call the action of every marble
     * in that row.
     * @param row is the row in which the marble will be insert
     * @throws IndexOutOfBoundsException if the selected row does not exist
     */
    public void insertMarbleInRow(int row) throws IndexOutOfBoundsException{
        for (Marble marble : marketTray.get(row)) {
            marble.doMarbleAction(this);
        }

        Marble tempMarble = marketTray.get(row).get(0);
        marketTray.get(row).remove(0);
        marketTray.get(row).add(numCol - 1 , marbleToInsert);
        marbleToInsert = tempMarble;


        //state change
        if (getWhiteMarbleDrew() > 0){
            notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.WHITE_MARBLE_CONVERSION));
        }

        //observer
        //TODO mandare matrice
        ArrayList<ColorData> rowUpdated = new ArrayList<>();
        for (int i = 0; i < numRow; i++){
            rowUpdated.add(marketTray.get(row).get(i).getColorData());
        }
        notifyAllObservers(x ->
                x.marketTrayChange(rowUpdated, marbleToInsert.getColorData(), row, true));
    }


    /**
     * Method to insert the extra marble in a specific column of the market tray and call the action of every marble
     * in that column.
     * @param col is the column in which the marble will be insert
     * @throws IndexOutOfBoundsException if the selected column does not exist
     */
    public void insertMarbleInCol(int col) throws IndexOutOfBoundsException{
        for (int i = 0; i < numRow; i++) {
            marketTray.get(i).get(col).doMarbleAction(this);
        }

        Marble tempMarble = marketTray.get(0).get(col);
        for (int i = 0; i < numRow - 1; i++) {
            marketTray.get(i).remove(col);
            marketTray.get(i).add(col,marketTray.get(i+1).get(col));
        }
        marketTray.get(numRow - 1).remove(col);
        marketTray.get(numRow - 1).add(col, marbleToInsert);
        marbleToInsert = tempMarble;

        //state change
        if (getWhiteMarbleDrew() > 0){
            notifyGameMasterObserver(x -> x.onTurnStateChange(TurnState.WHITE_MARBLE_CONVERSION));
        }

        //TODO mettere matrice
        ArrayList<ColorData> colUpdated = new ArrayList<>();
        for (int i = 0; i < numRow; i++){
            colUpdated.add(marketTray.get(i).get(col).getColorData());
        }
        notifyAllObservers(x ->
                x.marketTrayChange(colUpdated, marbleToInsert.getColorData(), col, false));


    }


    /**
     * Method to get a copy of the resources got from market
     * @return is the array in which the resources are stored
     */
    public ArrayList<Resource> getResourceToSend(){
        return resourcesToSend.stream()
                .map(Res -> ResourceFactory.createResource(Res.getType(), Res.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public void insertLeaderResources(ArrayList<Resource> resources){
        resources.forEach(this::addInResourcesToSend);

    }

    public void setWhiteMarbleToTransform(int whiteMarbleToTransform) {
        int delta = numOfWhiteMarbleDrew - whiteMarbleToTransform;
        //control that we don't exceed setting leader white marble assignment
        if (delta <= 0){
            this.whiteMarbleToTransform = numOfWhiteMarbleDrew;
            numOfWhiteMarbleDrew = 0;
        }else{
            numOfWhiteMarbleDrew = delta;
            this.whiteMarbleToTransform = whiteMarbleToTransform;
        }

        this.whiteMarbleToTransform = whiteMarbleToTransform;

    }

    public int getWhiteMarbleToTransform() {
        return whiteMarbleToTransform;
    }

    /**
     * Method to reset the market after it's been used
     */
    public void reset(){
        numOfWhiteMarbleDrew = 0;
        resourcesToSend.clear();
    }


    @Override
    public void attachObserver(MarketObserver observer) {
        marketObserverList.add(observer);
    }

    @Override
    public void notifyAllObservers(Consumer<MarketObserver> consumer) {
        marketObserverList.forEach(consumer);
    }
}
