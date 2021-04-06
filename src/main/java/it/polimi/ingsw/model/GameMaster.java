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

    public PersonalBoard getPlayerPersonalBoard(String nickName) {
        return playersPersonalBoard.get(nickName);
    }

    /**
     * Method to manage the activation of a popeSpace from a player Faith Track
     * @param idVR is the id of the popeSpace activated
     */
    @Override
    public void updateFromFaithTrack(int idVR) {
        if(vaticanReportReached == idVR){
            for (PersonalBoard personalBoard : playersPersonalBoard.values()){
                personalBoard.getFaithTrack().popeFavorActivated(idVR);
            }
            vaticanReportReached ++;
        }
    }

    /**
     * Method to manage the advancement of player after the current player discarded resources
     * @param positions is the number of move each player must do
     */
    @Override
    public void updateFromResourceManager(int positions) {
        for (int i = 0; i < positions; i++) {
            for (PersonalBoard personalBoard : playersPersonalBoard.values()){
                if(!personalBoard.equals(playersPersonalBoard.get(currentPlayer))){
                    personalBoard.getFaithTrack().increasePlayerPosition();
                }
            }
            for (PersonalBoard personalBoard : playersPersonalBoard.values()){
                if(!personalBoard.equals(playersPersonalBoard.get(currentPlayer))){
                    personalBoard.getFaithTrack().doCurrentCellAction();
                }
            }
        }
    }

    /**
     * Method to move the current player after the discard of a card leader
     */
    @Override
    public void updateFromCardManager() {
        playersPersonalBoard.get(currentPlayer).getFaithTrack().movePlayer(1);
    }
}