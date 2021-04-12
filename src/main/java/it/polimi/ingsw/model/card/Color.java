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
        if (this.equals(Color.GREEN)) return 0;
        else if(this.equals(Color.PURPLE)) return 1;
        else if(this.equals(Color.BLUE)) return 2;
        else if(this.equals(Color.YELLOW)) return 3;
        else return -1;

    }
}
