package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import it.polimi.ingsw.client.data.FaithTrackData;

/**
 * A class implement the Cell interface to indicate that it's a cell of the faith track.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @Type(value = NormalCell.class, name = "normal"),
        @Type(value = VictoryCell.class, name = "victory"),
        @Type(value = PopeSpaceCell.class, name = "pope") })
public interface Cell{

    /**
     * Perform a different effect on faith track based of the cell type.
     * @param faithTrack the player faith track
     */
    void doAction(FaithTrack faithTrack);

    /**
     * Return true if the cell is in a particular Vatican Report space.
     * @param idVR the id of the Vatican Report that I want to check if the cell is in.
     * @return true if the cell is in that specific Vatican Report space.
     */
    boolean isInVaticanReport(int idVR);

    /**
     * Return the vatican report id.
     * @return the vatican report id.
     */
    int getIdVaticanReport();

    /**
     * Return a FaithTrackData based on the cell.
     * @return a FaithTrackData based on the cell.
     */
    FaithTrackData toData();
}
