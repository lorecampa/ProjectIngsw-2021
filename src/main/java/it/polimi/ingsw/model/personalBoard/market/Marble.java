package it.polimi.ingsw.model.personalBoard.market;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.client.data.ColorData;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RedMarble.class, name = "red"),
        @JsonSubTypes.Type(value = WhiteMarble.class, name = "white"),
        @JsonSubTypes.Type(value = BlueMarble.class, name = "blue"),
        @JsonSubTypes.Type(value = GreyMarble.class, name = "grey"),
        @JsonSubTypes.Type(value = YellowMarble.class, name = "yellow"),
        @JsonSubTypes.Type(value = PurpleMarble.class, name = "purple") })
public interface Marble {
    /**
     * Method implemented by all marbles that will perform a different effect for each type
     * @param market is the reference to the market of the game
     */
    void doMarbleAction(Market market);

    ColorData getColorData();
}
