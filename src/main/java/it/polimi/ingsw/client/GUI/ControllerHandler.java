package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;

public class ControllerHandler {
    private PrimaryController primaryController;
    private SecondaryController secondaryController;


    private static ControllerHandler instance = null;
    private ControllerHandler() { }

    public static ControllerHandler getInstance(){
        if (instance == null) instance = new ControllerHandler();
        return instance;
    }

    public PrimaryController getPrimaryController() {
        return primaryController;
    }

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    public SecondaryController getSecondaryController() {
        return secondaryController;
    }

    public void setSecondaryController(SecondaryController secondaryController) {
        this.secondaryController = secondaryController;
    }
}
