package it.polimi.ingsw.observer;

/**
 * A class can implement the FaithTrackObserver interface when it wants to be informed of changes in observable objects.
 */
public interface FaithTrackObserver {

    /**
     * Get an update from the FaithTrack when the position of the player as increased.
     */
    void positionIncrease();

    /**
     * Get an update from the FaithTrack when a pope favor is reached.
     * @param idVaticanReport the id of the vatican report.
     * @param isDiscard true if the player has obtained the pope favor.
     */
    void popeFavorReached(int idVaticanReport, boolean isDiscard);


}
