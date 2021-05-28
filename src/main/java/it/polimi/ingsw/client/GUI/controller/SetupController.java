package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.command.NumOfPlayerCMD;
import it.polimi.ingsw.client.command.UsernameCMD;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class SetupController extends Controller{
    private final ControllerHandler handler = ControllerHandler.getInstance();
    private final Client client = Client.getInstance();
    private boolean isNumOfPlayer = true;


    @FXML Label customMessage;
    @FXML AnchorPane numOfPlayerBox;
    @FXML AnchorPane usernameBox;
    @FXML TextField numOfPlayer;
    @FXML TextField username;
    @FXML Button playButton;




    @FXML
    public void sendData(){
        if (isNumOfPlayer){
            new NumOfPlayerCMD(numOfPlayer.getText(), client).doCommand();
        }else{
            new UsernameCMD(username.getText(), client).doCommand();
        }
    }


    //---------------------
    //EXTERNAL METHODS
    //--------------------

    @Override
    public void setUpAll() {

    }
    public void showInsertUsername(){
        isNumOfPlayer = false;
        usernameBox.setVisible(true);
        numOfPlayerBox.setVisible(false);
    }


    @Override
    public void showErrorMessage(String msg) {
        customMessage.setVisible(true);
        customMessage.setText(msg);
    }
}
