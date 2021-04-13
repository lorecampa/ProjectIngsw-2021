package it.polimi.ingsw.model.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @Type(value = NormalCell.class, name = "normal"),
        @Type(value = VictoryCell.class, name = "victory"),
        @Type(value = PopeSpaceCell.class, name = "pope") })
public interface Cell {

    /**
     * Method implemented by all cells that will perform a different effect for each type
     * @param faithTrack is the player faith track
     */
    void doAction(FaithTrack faithTrack);

    /**
     * Method to get if the cell is in a particular Vatican Report space
     * @param idVR is the id of the Vatican Report that I want to check if the cell is in
     * @return is true if the cell is in that specific Vatican Report otherwise it return false
     */
    boolean isInVaticanReport(int idVR);

    /**
     * Method to get the vatican report id
     * @return is the vatican report id
     */
    int getIdVaticanReport();
}
