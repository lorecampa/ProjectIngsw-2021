package it.polimi.ingsw.model;

import it.polimi.ingsw.commonInterfaces.Observer;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameMaster implements Observer {
    private String currentPlayer = "";
    private final Map<String, PersonalBoard> playersPersonalBoard = new HashMap<>();
    private int vaticanReportReached = 0;

    public void addPlayer(String nickname) throws IOException {
        PersonalBoard playerPersonalBoard = new PersonalBoard();
        playersPersonalBoard.put(nickname,playerPersonalBoard);
    }

    public void setCurrentPlayer(String player){
        currentPlayer = player;
    }

    public void setVaticanReportReached(int idVaticanReport){
        vaticanReportReached = idVaticanReport;
    }

    @Override
    public void updateFromFaithTrack() {

    }
}
