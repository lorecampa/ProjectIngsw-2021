package it.polimi.ingsw.model.card;

import it.polimi.ingsw.client.data.ColorData;

/**
 * Class Color is an enumeration containing all the card color.
 */
public enum Color {
    GREEN("Green"),
    PURPLE("Purple"),
    BLUE("Blue"),
    YELLOW("Yellow"),
    ANY("Any");

    private final String displayName;

    /**
     * Construct a Color with a specific display name.
     * @param displayName the name that will be displayed on screen.
     */
    Color(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Return the display name.
     * @return the display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Return a ColorData based on the color.
     * @return a ColorData based on the color.
     */
    public ColorData toColorData(){
        switch(this){
            case YELLOW:
                return ColorData.YELLOW;
            case PURPLE:
                return ColorData.PURPLE;
            case GREEN:
                return ColorData.GREEN;
            case BLUE:
                return ColorData.BLUE;
            default:
                return ColorData.WHITE;
        }
    }


    /**
     * Return the column index in the deck development based on color, if the color has not a reference to the
     * development matrix then it returns -1 (ANY).
     * @return the column index in the deck development based on color.
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
