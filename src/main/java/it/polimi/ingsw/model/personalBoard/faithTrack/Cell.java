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
    void doAction(FaithTrack faithTrack);
    boolean isInVaticanReport(int idVR);
}
