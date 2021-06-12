package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.FaithTrackData;

/**
 * Implementation of cell interface that represent the victory cell.
 */
public class VictoryCell implements Cell{

    private final int victoryPoints;
    private final int idVaticanReport;

    /**
     * Construct a victory cell with a specific vatican report id and victory points.
     * @param victoryPoints the number of victory points.
     * @param idVaticanReport the vatican report id.
     */
    @JsonCreator
    public VictoryCell(@JsonProperty("victoryPoints") int victoryPoints,
                       @JsonProperty("idVaticanReport") int idVaticanReport) {
        this.victoryPoints = victoryPoints;
        this.idVaticanReport = idVaticanReport;
    }

    /**
     * Set the victory points of player's faith track to the cell's victory points
     */
    @Override
    public void doAction(FaithTrack faithTrack) {
            faithTrack.setVictoryPoints(victoryPoints);
    }

    /**
     * See {@link Cell#isInVaticanReport(int)}.
     */
    @Override
    public boolean isInVaticanReport(int idVR) {
        return idVaticanReport == idVR;
    }

    /**
     * See {@link Cell#getIdVaticanReport()}.
     */
    @Override
    public int getIdVaticanReport() {
        return idVaticanReport;
    }

    /**
     * See {@link Cell#toData()}.
     */
    @Override
    public FaithTrackData toData() {
        boolean vaticanRep = idVaticanReport != -1;
        return new FaithTrackData(0,victoryPoints,vaticanRep,false,0,false);
    }
}
