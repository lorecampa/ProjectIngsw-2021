package it.polimi.ingsw.client.data;

import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.model.card.Color;

import java.util.ArrayList;

public class CardDevData {
    private int level;
    private int victoryPoints;
    private Color color;
    private ArrayList<ResourceData> resourceReq;
    private ArrayList<ResourceData> productionCost;
    private ArrayList<ResourceData> productionEarn;

    public CardDevData(int level, int victoryPoints, Color color, ArrayList<ResourceData> resourceReq, ArrayList<ResourceData> productionCost, ArrayList<ResourceData> productionEarn) {
        this.level = level;
        this.victoryPoints = victoryPoints;
        this.color = color;
        this.resourceReq = resourceReq;
        this.productionCost = productionCost;
        this.productionEarn = productionEarn;
    }

    public int getLevel() {
        return level;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public Color getColor() {
        return color;
    }

    public String getColorToColor(){
        String s="";
        switch (color){
            case BLUE:
                s = PrintAssistant.ANSI_BLUE_BACKGROUND;
                break;
            case GREEN:
                s = PrintAssistant.ANSI_GREEN_BACKGROUND;
                break;
            case PURPLE:
                s = PrintAssistant.ANSI_PURPLE_BACKGROUND;
                break;
            case YELLOW:
                s = PrintAssistant.ANSI_YELLOW_BACKGROUND;
                break;
            case ANY:
                s = PrintAssistant.ANSI_RESET;
                break;
        }
        return s;
    }

    public ArrayList<ResourceData> getResourceReq() {
        return resourceReq;
    }

    public ArrayList<ResourceData> getProductionCost() {
        return productionCost;
    }

    public ArrayList<ResourceData> getProductionEarn() {
        return productionEarn;
    }
}
