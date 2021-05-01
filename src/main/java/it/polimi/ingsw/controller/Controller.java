package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameMaster;

public class Controller {
    private GameMaster gameMaster;

    public Controller(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
    }


    public void nextTurn(String username) {
        if (!isYourTurn(username)){

        }
        gameMaster.nextPlayer();
    }


    private boolean isYourTurn(String username){
        return gameMaster.getCurrentPlayer().equals(username);
    }



}
