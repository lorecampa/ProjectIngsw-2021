package it.polimi.ingsw.client.data;

import it.polimi.ingsw.client.PrintAssistant;

import java.net.URL;

public enum ColorData {
    GRAY("GRAY"),
    PURPLE("PURPLE"),
    RED("RED"),
    BLUE("BLUE"),
    YELLOW("YELLOW"),
    GREEN("GREEN"),
    WHITE("WHITE");

    private final String displayName;
    private final String greyMarble = "/it/polimi/ingsw/client/GUI/punchboard/grey_marble.png";
    private final String yellowMarble = "/it/polimi/ingsw/client/GUI/punchboard/yellow_marble.png";
    private final String blueMarble = "/it/polimi/ingsw/client/GUI/punchboard/blue_marble.png";
    private final String redMarble = "/it/polimi/ingsw/client/GUI/punchboard/red_marble.png";
    private final String purpleMarble = "/it/polimi/ingsw/client/GUI/punchboard/purple_marble.png";
    private final String whiteMarble = "/it/polimi/ingsw/client/GUI/punchboard/white_marble.png";

    ColorData(String displayName) {
        this.displayName = displayName;
    }

    public String toColor(){
        String s="";
        switch(this){
            case GRAY:
                s= PrintAssistant.ANSI_CYAN_BACKGROUND;
                break;
            case YELLOW:
                s=PrintAssistant.ANSI_YELLOW_BACKGROUND;
                break;
            case BLUE:
                s=PrintAssistant.ANSI_BLUE_BACKGROUND;
                break;
            case RED:
                s=PrintAssistant.ANSI_RED_BACKGROUND;
                break;
            case PURPLE:
                s=PrintAssistant.ANSI_PURPLE_BACKGROUND;
                break;
            case WHITE:
                s=PrintAssistant.ANSI_WHITE_BACKGROUND;
                break;
        }
        return s;
    }

    public String toMarbleResource(){
        URL s = null;
        switch(this){
            case GRAY:
                s = this.getClass().getResource(greyMarble);
                break;
            case YELLOW:
                s = this.getClass().getResource(yellowMarble);
                break;
            case BLUE:
                s=this.getClass().getResource(blueMarble);
                break;
            case RED:
                s=this.getClass().getResource(redMarble);
                break;
            case PURPLE:
                s=this.getClass().getResource(purpleMarble);
                break;
            case WHITE:
                s=this.getClass().getResource(whiteMarble);
                break;
        }
        assert s != null;
        return s.toString();
    }
}
