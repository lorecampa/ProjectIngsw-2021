package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class FaithTrack {
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

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public int getPopeFavorVP(){return popeFavorVP;}

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public int getCurrentPositionOnTrack() { return currentPositionOnTrack; }

    public void movePlayer(int positions){
        for (int i = 0; i < positions; i++) {
            currentPositionOnTrack++;
            track.get(currentPositionOnTrack).doAction(this);
        }
    }

    public void addVPForPopeFavor(int popeFavorNum){ popeFavorVP += popeFavor.get(popeFavorNum); }

    public void increasePlayerPosition(){ currentPositionOnTrack++;}
}
