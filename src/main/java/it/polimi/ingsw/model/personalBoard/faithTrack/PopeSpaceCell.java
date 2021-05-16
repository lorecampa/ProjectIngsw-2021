package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.FaithTrackData;

public class PopeSpaceCell implements Cell{

    private final int victoryPoints;
    private final int idVaticanReport;

    @JsonCreator
    public PopeSpaceCell(@JsonProperty("victoryPoints") int victoryPoints,
                         @JsonProperty("idVaticanReport") int idVaticanReport) {
        this.victoryPoints = victoryPoints;
        this.idVaticanReport = idVaticanReport;
    }

    /**
     * Method that set the victory points of player's faith track to the cell's victory points and
     * call the method that manage the activation of a Vatican Report
     */
    @Override
    public void doAction(FaithTrack faithTrack) {
        if (victoryPoints != -1)
            faithTrack.setVictoryPoints(victoryPoints);
        faithTrack.notifyGameMasterObserver(x -> x.vaticanReportReached(idVaticanReport));
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

    @Override
    public FaithTrackData toData() {
        return new FaithTrackData(0,victoryPoints,true,true,0,false);
    }
}