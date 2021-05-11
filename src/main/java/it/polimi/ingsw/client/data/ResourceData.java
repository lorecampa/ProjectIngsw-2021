package it.polimi.ingsw.client.data;

import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.model.resource.ResourceType;

public class ResourceData {
    private ResourceType type;
    private int value;

    public ResourceData(ResourceType type, int value) {
        this.type = type;
        this.value = value;
    }
    public ResourceData(ResourceType type) {
        this.type = type;
        this.value = 0;
    }
    public ResourceType getType() {
        return type;
    }
    public int getValue() {
        return value;
    }

    public String toCli(){
        String s="";
        switch(type){
            case STONE:
                s= PrintAssistant.ANSI_BLUE_BACKGROUND+PrintAssistant.ANSI_BLACK+" ST";
                break;
            case COIN:
                s=PrintAssistant.ANSI_YELLOW_BACKGROUND+PrintAssistant.ANSI_BLACK+" CO";
                break;
            case SHIELD:
                s=PrintAssistant.ANSI_CYAN_BACKGROUND+PrintAssistant.ANSI_BLACK+" SH";
                break;
            case FAITH:
                s=PrintAssistant.ANSI_RED_BACKGROUND+PrintAssistant.ANSI_BLACK+" FA";
                break;
            case SERVANT:
                s=PrintAssistant.ANSI_PURPLE_BACKGROUND+PrintAssistant.ANSI_BLACK+" SE";
                break;
            case ANY:
                s=PrintAssistant.ANSI_WHITE_BACKGROUND +PrintAssistant.ANSI_BLACK+" AN";
                break;
        }
        s+=" "+value+" "+PrintAssistant.ANSI_RESET+" ";
        return s;
    }
    /*
    public String toColor(){
        String s="";
        switch(type){
            case STONE:
                s=PrintAssistant.ANSI_CYAN_BACKGROUND;
                break;
            case COIN:
                s=PrintAssistant.ANSI_YELLOW_BACKGROUND;
                break;
            case SHIELD:
                s= PrintAssistant.ANSI_BLUE_BACKGROUND;
                break;
            case FAITH:
                s=PrintAssistant.ANSI_RED_BACKGROUND;
                break;
            case SERVANT:
                s=PrintAssistant.ANSI_PURPLE_BACKGROUND;
                break;
            case ANY:
                s=PrintAssistant.ANSI_WHITE_BACKGROUND;
                break;
        }
        return s;
    }
    */
}
