package it.polimi.ingsw.client.data;

public class FaithTrackData {
    private int numberofCell;
    private int victoryPoints;
    private boolean vaticanReport;
    private boolean popeFavor;
    private int victoryPopeFavor;

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
}
