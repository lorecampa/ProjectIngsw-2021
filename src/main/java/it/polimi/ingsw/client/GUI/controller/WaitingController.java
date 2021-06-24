package it.polimi.ingsw.client.GUI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;



public class WaitingController extends Controller {

    @FXML private AnchorPane background;
    @FXML private AnchorPane customMessageBox;
    @FXML private Label message;

    /**
     * Method that prepare the scene
     */
    @FXML
    public void initialize() {
        setUpCustomMessageBox(customMessageBox);
    }

    /**
     * See {@link Controller#setUpAll()}
     */
    @Override
    public void setUpAll() {
        setUpBackground(background);
    }

    /**
     * Method that display a specific message
     */
    public void setLobbyMessage() {
        showCustomMessage("Waiting for other players to join");
    }
    /**
     * Method that display a specific message
     */
    public void setPreMatchMessage() {
        showCustomMessage("Waiting for other players to finish pre-match setup");
    }
    /**
     * Method that display a specific message
     */
    public void setReconnectMessage(String username) {
        showCustomMessage("Reconnected as " + username + " Waiting for your turn to load the game");

    }

    /**
     * See {@link Controller#showCustomMessage(String)}
     * @param msg the message to be displayed
     */
    @Override
    public void showCustomMessage(String msg) {
        TextField tf = new TextField(msg);
        customMessageBox.setVisible(true);
        message.textProperty().bind(tf.textProperty());
    }
}
