package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class FaithTrack {
    private int victoryPoints;
    private int currentPositionOnTrack;
    private final ArrayList<Cell> track;

    public FaithTrack() {
        this.victoryPoints = 0;
        this.currentPositionOnTrack = 0;
        this.track = null;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

}
