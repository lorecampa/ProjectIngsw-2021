package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.FaithTrackData;

public class NormalCell implements Cell{

    private final int idVaticanReport;

    @JsonCreator
    public NormalCell(@JsonProperty("idVaticanReport") int idVaticanReport) {
        this.idVaticanReport = idVaticanReport;
    }

    /**
     * Method that does nothing since a normal cell hasn't any effect
     */
    @Override
    public void doAction(FaithTrack faithTrack) {}

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
        boolean vaticanRep = idVaticanReport != -1;
        return new FaithTrackData(0,-1,vaticanRep,false,0,false);
    }
}
