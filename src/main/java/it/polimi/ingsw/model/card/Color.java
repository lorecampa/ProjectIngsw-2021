package it.polimi.ingsw.model.card;

/**
 * Class Color is an enumeration containing all the card color
 */
public enum Color {
    GREEN("Green"),
    BLUE("Blue"),
    YELLOW("Yellow"),
    PURPLE("Purple");

    private final String displayName;

    /**
     * Constructor Color create a new Color istance
     * @param displayName of type String is the name that will be displayed on screen
     */
    Color(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Method getDisplayName is a getter for the display name
     * @return String - the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
