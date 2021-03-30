package it.polimi.ingsw.model.personalBoard.faithTrack;

public class VictoryCell implements Cell{

    private final FaithTrack faithTrack;
    private final int victoryPoints;
    private final int idVaticanReport;

    public VictoryCell(FaithTrack faithTrack, int victoryPoints, int idVaticanReport) {
        this.faithTrack = faithTrack;
        this.victoryPoints = victoryPoints;
        this.idVaticanReport = idVaticanReport;
    }

    /**
     * Method that set the victory points of player's faith track to the cell's victory points
     */
    @Override
    public void doAction() {

    }

    /**
     * Method to get if the cell is in a particular Vatican Report space
     * @param idVR is the id of the Vatican Report that I want to check if the cell is in
     * @return is true if the cell is in that specific Vatican Report otherwise it return false
     */
    @Override
    public boolean isInVaticanReport(int idVR) {
        return idVaticanReport == idVR;
    }
}
