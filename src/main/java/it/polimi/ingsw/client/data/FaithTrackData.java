package it.polimi.ingsw.client.data;

import it.polimi.ingsw.client.PrintAssistant;

public class FaithTrackData {
    private final int numberofCell;
    private final int victoryPoints;
    private final boolean vaticanReport;
    private final boolean popeFavor;
    private final int victoryPopeFavor;

    public FaithTrackData(int numberofCell, int victoryPoints, boolean vaticanReport, boolean popeFavor, int victoryPopeFavor) {
        this.numberofCell = numberofCell;
        this.victoryPoints = victoryPoints;
        this.vaticanReport = vaticanReport;
        this.popeFavor = popeFavor;
        this.victoryPopeFavor=victoryPopeFavor;
    }

    public int getNumberofCell() {
        return numberofCell;
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
        if(victoryPoints==0){
            victoryPointsString="      ";
        }
        else{
            victoryPointsString="VP: "+ PrintAssistant.instance.padRight(victoryPoints +"", 2);
        }
        return victoryPointsString;
    }

    public String cardPopeFavor(){
        String popeFavorString;
        if(popeFavor){
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
