package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.observer.FaithTrackObserver;
import it.polimi.ingsw.observer.Observable;

import java.util.ArrayList;

/**
 * Class that manage the Faith Track of a player
 */
public class FaithTrack extends Observable<FaithTrackObserver> {
    private int victoryPoints;
    private int popeFavorVP;
    private int currentPositionOnTrack;
    private final ArrayList<Integer> popeFavor;
    private final ArrayList<Cell> track;


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
            currentPositionOnTrack++;
            track.get(currentPositionOnTrack).doAction(this);
        }
    }

    /**
     * Method to increase the player's position on track
     */
    public void increasePlayerPosition(){ currentPositionOnTrack++;}

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
        if(track.get(currentPositionOnTrack).isInVaticanReport(idVaticanReport))
            popeFavorVP += popeFavor.get(idVaticanReport);
    }


}
