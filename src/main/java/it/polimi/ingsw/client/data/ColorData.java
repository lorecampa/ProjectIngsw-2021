package it.polimi.ingsw.client.data;

import it.polimi.ingsw.client.PrintAssistant;

import java.net.URL;
@SuppressWarnings("FieldCanBeLocal")
public enum ColorData {
    GREY("GREY"),
    PURPLE("PURPLE"),
    RED("RED"),
    BLUE("BLUE"),
    YELLOW("YELLOW"),
    GREEN("GREEN"),
    WHITE("WHITE");

    @SuppressWarnings("unused")
    private final String displayName;

    ColorData(String displayName) {
        this.displayName = displayName;
    }

    public String toColor(){
        String s="";
        switch(this){
            case GREY:
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
        String greyMarble = "/GUI/punchboard/grey_marble.png";
        String yellowMarble = "/GUI/punchboard/yellow_marble.png";
        String blueMarble = "/GUI/punchboard/blue_marble.png";
        String redMarble = "/GUI/punchboard/red_marble.png";
        String purpleMarble = "/GUI/punchboard/purple_marble.png";
        String whiteMarble = "/GUI/punchboard/white_marble.png";
        switch(this){
            case GREY:
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
