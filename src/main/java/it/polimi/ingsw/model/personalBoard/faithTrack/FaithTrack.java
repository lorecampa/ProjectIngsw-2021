package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.FaithTrackData;
import it.polimi.ingsw.observer.FaithTrackObserver;
import it.polimi.ingsw.observer.GameMasterObservable;
import it.polimi.ingsw.observer.GameMasterObserver;
import it.polimi.ingsw.observer.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class that manage the Faith Track of a player
 */
public class FaithTrack extends GameMasterObservable  implements Observable<FaithTrackObserver>{
    List<FaithTrackObserver> faithTrackObserverList = new ArrayList<>();

    private int victoryPoints;
    private int popeFavorVP;
    private int currentPositionOnTrack;
    private final ArrayList<Integer> popeFavor;
    private final ArrayList<Cell> track;

    @JsonIgnore
    private final ArrayList<Boolean> popeFavorAcquired = new ArrayList<>();

    @JsonCreator
    public FaithTrack(@JsonProperty("victoryPoints") int victoryPoints,
                      @JsonProperty("popeFavorVP") int popeFavorVP,
                      @JsonProperty("currentPositionOnTrack") int currentPositionOnTrack,
                      @JsonProperty("popeFavor") ArrayList<Integer> popeFavor,
                      @JsonProperty("track") ArrayList<Cell> track) {
        this.victoryPoints = victoryPoints;
        this.popeFavorVP = popeFavorVP;
        this.currentPositionOnTrack = currentPositionOnTrack;
        this.popeFavor = popeFavor;
        this.track = track;

        initializePopeFavorAcquired();
    }

    private void initializePopeFavorAcquired(){
        for (int i = 0; i < popeFavor.size(); i++) {
            popeFavorAcquired.add(false);
        }
    }

    /**
     * Method to get the number of victory points acquired from the track
     * @return is the number of victory points
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Method to get the number of victory points acquired from pope favors
     * @return is the number of victory points
     */
    public int getPopeFavorVP(){return popeFavorVP;}

    /**
     * Method to set the victory points acquired from the track
     * @param victoryPoints is the number of victory points to set
     */
    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    /**
     * Method to get the current position of the player
     * @return is the current position of the player
     */
    public int getCurrentPositionOnTrack() { return currentPositionOnTrack; }

    /**
     * Method to move the player on the track and activate the effect of every cell
     * @param positions is the number of moves on track of the player
     */
    public void movePlayer(int positions){
        for (int i = 0; i < positions; i++) {
            if(!hasReachedEnd()){
                increasePlayerPosition();
                track.get(currentPositionOnTrack).doAction(this);
            }
        }
    }

    /**
     * Method to increase the player's position on track
     */
    public void increasePlayerPosition(){
        if (!hasReachedEnd()){
            currentPositionOnTrack++;
            notifyAllObservers(FaithTrackObserver::positionIncrease);
            if (hasReachedEnd()){
                notifyGameMasterObserver(GameMasterObserver::winningCondition);
            }
        }
    }

    private boolean hasReachedEnd(){
        return currentPositionOnTrack == track.size() - 1;
    }

    /**
     * Method to activate the effect of the current cell
     */
    public void doCurrentCellAction(){
        track.get(currentPositionOnTrack).doAction(this);
    }

    /**
     * Method to increase the victory points of the player if it's in a Vatican Report
     * @param idVaticanReport is the id of the Vatican Report activated
     */
    public void popeFavorActivated(int idVaticanReport){

        if(track.get(currentPositionOnTrack).isInVaticanReport(idVaticanReport)){
            popeFavorVP += popeFavor.get(idVaticanReport);
            popeFavorAcquired.set(idVaticanReport, true);
            notifyAllObservers(x -> x.popeFavorReached(idVaticanReport, false));
        }else{
            notifyAllObservers(x -> x.popeFavorReached(idVaticanReport, true));
        }

    }


    public ArrayList<FaithTrackData> toFaithTrackData(){
        ArrayList<FaithTrackData> faithTrackData = new ArrayList<>();
        int index = 0;

        for (Cell cell : track){
            FaithTrackData cellDataRaw = cell.toData();

            boolean isAcquired = false;
            int popeSpaceVP = 0;
            if (cellDataRaw.isPopeFavor()) {
                isAcquired = popeFavorAcquired.get(cell.getIdVaticanReport());
                popeSpaceVP = popeFavor.get(cell.getIdVaticanReport());
            }

            FaithTrackData cellData = new FaithTrackData(index,cellDataRaw.getVictoryPoints(),
                    cellDataRaw.isVaticanReport(),cellDataRaw.isPopeFavor(),popeSpaceVP, isAcquired);
            faithTrackData.add(cellData);
            index++;
        }

        return faithTrackData;
    }


    @Override
    public void attachObserver(FaithTrackObserver observer) {
        faithTrackObserverList.add(observer);
    }

    @Override
    public void notifyAllObservers(Consumer<FaithTrackObserver> consumer) {
        faithTrackObserverList.forEach(consumer);
    }
}
