package it.polimi.ingsw.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.PrintAssistant;

public class FaithTrackData {
    private final int numberOfCell;
    private final int victoryPoints;
    private final boolean vaticanReport;
    private final boolean popeFavor;
    private final int victoryPopeFavor;
    private boolean acquired;

    @JsonCreator
    public FaithTrackData(@JsonProperty("numberOfCell") int numberOfCell,
                          @JsonProperty("victoryPoints") int victoryPoints,
                          @JsonProperty("vaticanReport") boolean vaticanReport,
                          @JsonProperty("popeFavor") boolean popeFavor,
                          @JsonProperty("victoryPopeFavor") int victoryPopeFavor,
                          @JsonProperty("acquired") boolean acquired) {
        this.numberOfCell = numberOfCell;
        this.victoryPoints = victoryPoints;
        this.vaticanReport = vaticanReport;
        this.popeFavor = popeFavor;
        this.victoryPopeFavor=victoryPopeFavor;
        this.acquired = acquired;
    }

    public int getNumberOfCell() {
        return numberOfCell;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean isVaticanReport() {
        return vaticanReport;
    }

    public boolean isPopeFavor() {
        return popeFavor;
    }

    public int getVictoryPopeFavor() {
        return victoryPopeFavor;
    }

    public String cardVictoryPoint(){
        String victoryPointsString;
        if(victoryPoints==-1){
            victoryPointsString="      ";
        }
        else{
            victoryPointsString="VP: "+ PrintAssistant.instance.padRight(victoryPoints +"", 2);
        }
        return victoryPointsString;
    }

    public void setAcquired(boolean acquired) { this.acquired = acquired; }

    public String cardPopeFavor(){
        String popeFavorString;
        if(popeFavor){
            if (acquired)
                popeFavorString=PrintAssistant.ANSI_GREEN_BACKGROUND+ PrintAssistant.ANSI_BLACK + "PoFa("+PrintAssistant.instance.padRight(victoryPopeFavor+"", 2)+")" + PrintAssistant.ANSI_RESET;
            else
                popeFavorString="PoFa("+PrintAssistant.instance.padRight(victoryPopeFavor+"", 2)+")";
        }
        else{
            popeFavorString="        ";
        }
        return popeFavorString;
    }

    public String cardPopeFavorColor(){
        String colorBorderIn;
        if(popeFavor){
            colorBorderIn=PrintAssistant.ANSI_RED;
        }
        else{
            colorBorderIn=PrintAssistant.ANSI_RESET;
        }
        return colorBorderIn;
    }

    public String cardVaticanReportColor(){
        String colorBorderOut;
        if(vaticanReport){
            colorBorderOut=PrintAssistant.ANSI_YELLOW;
        }
        else{
            colorBorderOut=PrintAssistant.ANSI_RESET;
        }
        return colorBorderOut;
    }

}
