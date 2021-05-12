package it.polimi.ingsw.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.model.card.Color;

import java.util.ArrayList;

public class CardDevData {
    private int level;
    private int victoryPoints;
    private ColorData color;
    private ArrayList<ResourceData> resourceReq;

    private ArrayList<EffectData> effects;

    private ArrayList<ResourceData> productionCost;
    private ArrayList<ResourceData> productionEarn;


    public CardDevData(@JsonProperty("level")int level,
                       @JsonProperty("victoryPoints")int victoryPoints,
                       @JsonProperty("color")ColorData color,
                       @JsonProperty("resourceReq")ArrayList<ResourceData> resourceReq,
                       @JsonProperty("productionCost")ArrayList<ResourceData> productionCost,
                       @JsonProperty("productionEarn")ArrayList<ResourceData> productionEarn) {
        this.level = level;
        this.victoryPoints = victoryPoints;
        this.color = color;
        this.resourceReq = resourceReq;
        this.productionCost = productionCost;
        this.productionEarn = productionEarn;
    }

    @JsonCreator(mode= JsonCreator.Mode.PROPERTIES)
    public CardDevData(@JsonProperty("level")int level,
                       @JsonProperty("victoryPoints")int victoryPoints,
                       @JsonProperty("color")ColorData color,
                       @JsonProperty("resourceReq")ArrayList<ResourceData> resourceReq,
                       @JsonProperty("effects")ArrayList<EffectData> effects,
                       @JsonProperty("productionCost")ArrayList<ResourceData> productionCost,
                       @JsonProperty("productionEarn")ArrayList<ResourceData> productionEarn){
        this.level = level;
        this.victoryPoints = victoryPoints;
        this.color = color;
        this.resourceReq = resourceReq;
        this.effects = effects;
        this.productionCost=null;
        this.productionEarn=null;
    }

    public CardDevData(int level, ColorData color){
        this.level=level;
        this.color=color;
    }

    public int getLevel() {
        return level;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public ColorData getColor() {
        return color;
    }

    public String colorToColor(){
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
            case WHITE:
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

    public ArrayList<EffectData> getEffects() {
        return effects;
    }

    public String toCLIForLeader(){
        String s;
        s=colorToColor()+PrintAssistant.ANSI_BLACK+" DEV LV"+getLevel()+" "+PrintAssistant.ANSI_RESET+" ";
        return s;
    }
}
