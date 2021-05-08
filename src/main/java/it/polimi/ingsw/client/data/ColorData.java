package it.polimi.ingsw.client.data;

import it.polimi.ingsw.client.PrintAssistant;

public enum ColorData {
    GRAY,
    PURPLE,
    RED,
    BLUE,
    YELLOW,
    WHITE;



    public String toCli(){
        String s="";
        switch(this){
            case GRAY:
                s= PrintAssistant.ANSI_BLUE_BACKGROUND+PrintAssistant.ANSI_BLACK;
                break;
            case YELLOW:
                s=PrintAssistant.ANSI_YELLOW_BACKGROUND+PrintAssistant.ANSI_BLACK;
                break;
            case BLUE:
                s=PrintAssistant.ANSI_CYAN_BACKGROUND+PrintAssistant.ANSI_BLACK;
                break;
            case RED:
                s=PrintAssistant.ANSI_RED_BACKGROUND+PrintAssistant.ANSI_BLACK;
                break;
            case PURPLE:
                s=PrintAssistant.ANSI_PURPLE_BACKGROUND+PrintAssistant.ANSI_BLACK;
                break;
            case WHITE:
                s=PrintAssistant.ANSI_WHITE_BACKGROUND +PrintAssistant.ANSI_BLACK;
                break;
        }
        s+=" "+PrintAssistant.ANSI_RESET;
        return s;
    }


}
