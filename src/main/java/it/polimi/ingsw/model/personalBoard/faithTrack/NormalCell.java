package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.FaithTrackData;

/**
 * Implementation of cell interface that represent the normal cell.
 */
public class NormalCell implements Cell{

    private final int idVaticanReport;

    /**
     * Construct a normal cell with a specific vatican report id.
     * @param idVaticanReport the vatican report id.
     */
    @JsonCreator
    public NormalCell(@JsonProperty("idVaticanReport") int idVaticanReport) {
        this.idVaticanReport = idVaticanReport;
    }

    /**
     * Does nothing since a normal cell hasn't any effect
     */
    @Override
    public void doAction(FaithTrack faithTrack) {}

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
        return new FaithTrackData(0,-1,vaticanRep,false,0,false);
    }
}
