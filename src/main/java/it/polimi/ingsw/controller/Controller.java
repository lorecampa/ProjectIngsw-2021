package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.InvalidOrganizationWarehouseException;
import it.polimi.ingsw.exception.TooMuchResourceDepotException;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;

public class Controller {
    private GameMaster gameMaster;

    public Controller(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
    }


    public void nextTurn(String username) {
        if (!isYourTurn(username)) return;
        gameMaster.nextPlayer();
    }

    public void addResourceToStrongbox(Resource res, String username){
        gameMaster.getPlayerPersonalBoard(username)
                .getResourceManager()
                .addToStrongbox(res);
    }

    public void addResourceToWarehouse(Resource res, int index, String username) throws InvalidOrganizationWarehouseException, TooMuchResourceDepotException {
        gameMaster.getPlayerPersonalBoard(username)
                .getResourceManager().addToWarehouse(true, index, res);
    }


    public void marketAction(int row, int col, String  username){
        ResourceManager rm = gameMaster.getPlayerPersonalBoard(username)
                .getResourceManager();
    }


    private boolean isYourTurn(String username){
        return gameMaster.getCurrentPlayer().equals(username);
    }



}
