package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.FaithTrackData;

/**
 * Implementation of cell interface that represent the pope space cell.
 */
public class PopeSpaceCell implements Cell{

    private final int victoryPoints;
    private final int idVaticanReport;

    /**
     * Construct a pope space cell with a specific vatican report id and victory points.
     * @param victoryPoints the number of victory points.
     * @param idVaticanReport the vatican report id.
     */
    @JsonCreator
    public PopeSpaceCell(@JsonProperty("victoryPoints") int victoryPoints,
                         @JsonProperty("idVaticanReport") int idVaticanReport) {
        this.victoryPoints = victoryPoints;
        this.idVaticanReport = idVaticanReport;
    }

    /**
     * Set the victory points of player's faith track to the cell's victory points and
     * call the method that manage the activation of a Vatican Report
     */
    @Override
    public void doAction(FaithTrack faithTrack) {
        if (victoryPoints != -1)
            faithTrack.setVictoryPoints(victoryPoints);
        faithTrack.notifyGameMaster(x -> x.vaticanReportReached(idVaticanReport));
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
        return new FaithTrackData(0,victoryPoints,true,true,0,false);
    }
}