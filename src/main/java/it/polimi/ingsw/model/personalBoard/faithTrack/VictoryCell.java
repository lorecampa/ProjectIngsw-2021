package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VictoryCell implements Cell{

    private final int victoryPoints;
    private final int idVaticanReport;

    @JsonCreator
    public VictoryCell(@JsonProperty("victoryPoints") int victoryPoints,
                       @JsonProperty("idVaticanReport") int idVaticanReport) {
        this.victoryPoints = victoryPoints;
        this.idVaticanReport = idVaticanReport;
    }

    /**
     * Method that set the victory points of player's faith track to the cell's victory points
     */
    @Override
    public void doAction(FaithTrack faithTrack) {
            faithTrack.setVictoryPoints(victoryPoints);
    }

    /**
     * Method to get if the cell is in a particular Vatican Report space
     * @param idVR is the id of the Vatican Report that I want to check if the cell is in
     * @return is true if the cell is in that specific Vatican Report otherwise it return false
     */
    @Override
    public boolean isInVaticanReport(int idVR) {
        return idVaticanReport == idVR;
    }

    /**
     * Method to get the vatican report id
     * @return is the vatican report id
     */
    @Override
    public int getIdVaticanReport() {
        return idVaticanReport;
    }
}
