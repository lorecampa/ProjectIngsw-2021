package it.polimi.ingsw.client.GUI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WaitingController extends Controller {

    @FXML Label message;

    @Override
    public void setUpAll() {
    }

    public void setLobbyMessage(){
        message.setText("Waiting for other players to join");
    }

    public void setPreMatchMessage(){
        message.setText("Waiting for other players to finish pre-match setup");
    }
}
