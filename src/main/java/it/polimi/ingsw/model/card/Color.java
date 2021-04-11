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

    /**
     * Method getColumnDeckDevelopment
     * @return int - the column of the color in the matrix of development card
     */
    public int getColumnDeckDevelopment(){
        if (this.getDisplayName().equals(Color.GREEN.getDisplayName())) return 0;
        else if(this.getDisplayName().equals(Color.PURPLE.getDisplayName())) return 1;
        else if(this.getDisplayName().equals(Color.BLUE.getDisplayName())) return 2;
        else return 3;
    }
}
