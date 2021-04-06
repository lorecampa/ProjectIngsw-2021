package it.polimi.ingsw.commonInterfaces;

public interface Observer {
    void updateFromFaithTrack(int idVR);
    void updateFromResourceManager(int positions);
    void updateFromCardManager();
}
