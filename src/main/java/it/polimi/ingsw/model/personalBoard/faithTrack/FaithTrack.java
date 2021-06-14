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
 * FaithTrack of a player
 */
public class FaithTrack extends GameMasterObservable  implements Observable<FaithTrackObserver>{
    @JsonIgnore
    List<FaithTrackObserver> faithTrackObserverList = new ArrayList<>();
    @JsonIgnore
    private final ArrayList<Boolean> popeFavorAcquired = new ArrayList<>();

    private int victoryPoints;
    private int popeFavorVP;
    private int currentPositionOnTrack;
    private final ArrayList<Integer> popeFavor;
    private final ArrayList<Cell> track;



    /**
     * Construct a faith track in a state based on parameters.
     * @param victoryPoints victory points acquired by the player due to victory cells.
     * @param popeFavorVP victory points acquired by the player due to pope favors-
     * @param currentPositionOnTrack current position of the player on the track
     * @param popeFavor victory points of each pope favor.
     * @param track cells of the track.
     */
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

    /**
     * Set all the pope favor acquired to false.
     */
    private void initializePopeFavorAcquired(){
        for (int i = 0; i < popeFavor.size(); i++) {
            popeFavorAcquired.add(false);
        }
    }

    /**
     * Return the number of victory points acquired from victory cells.
     * @return the number of victory points.
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Return the number of victory points acquired from pope favors.
     * @return the number of victory points.
     */
    public int getPopeFavorVP(){return popeFavorVP;}

    /**
     * Return the sum of all victory points acquired from victory cells and pope favor.
     * @return the sum of all victory points.
     */
    public int allVP(){
        return  popeFavorVP+victoryPoints;
    }

    /**
     * Set the victory points acquired from victory cells.
     * @param victoryPoints the number of victory points to set.
     */
    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    /**
     * Return the current position of the player on the track.
     * @return the current position of the player.
     */
    public int getCurrentPositionOnTrack() { return currentPositionOnTrack; }

    /**
     * Move the player on the track and activate the effect of every cell.
     * @param positions the number of moves on track of the player.
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
     * Increase the player's position on track.
     */
    public void increasePlayerPosition(){
        if (!hasReachedEnd()){
            currentPositionOnTrack++;
            notifyAllObservers(FaithTrackObserver::positionIncrease);
            if (hasReachedEnd()){
                notifyGameMaster(GameMasterObserver::winningCondition);
            }
        }
    }

    /**
     * Return true if the player has reached the end of the track.
     * @return true if the player has reached the end of the track.
     */
    private boolean hasReachedEnd(){
        return currentPositionOnTrack == track.size() - 1;
    }

    /**
     * Activate the effect of cell in the current position of the player.
     */
    public void doCurrentCellAction(){
        track.get(currentPositionOnTrack).doAction(this);
    }

    /**
     * Increase the victory points of the player if it's in a Vatican Report.
     * @param idVaticanReport the id of the Vatican Report activated.
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

    /**
     * Return a ArrayList of FaithTrackData based on the current state of the faith track.
     * @return a ArrayList of FaithTrackData based on the current state of the market.
     */
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
