package it.polimi.ingsw.model.card;

/**
 * Class Color is an enumeration containing all the card color
 */
public enum Color {
    GREEN("Green"),
    PURPLE("Purple"),
    BLUE("Blue"),
    YELLOW("Yellow"),
    ANY("Any");

    private final String displayName;

    /**
     * Constructor Color create a new Color instance
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

    /**
     * Method getColumnDeckDevelopment
     * @return int - the column of the color in the matrix of development card, if the color has not
     * a reference to the development matrix then it returns -1 (ANY)
     */
    public int getColumnDeckDevelopment(){
        switch (this){
            case GREEN:
                return 0;
            case PURPLE:
                return 1;
            case BLUE:
                return 2;
            case YELLOW:
                return 3;
            default:
                return -1;
        }

    }
}
