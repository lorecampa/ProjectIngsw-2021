package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.command.NumOfPlayerCMD;
import it.polimi.ingsw.client.command.UsernameCMD;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class SetupController extends Controller{
    private final Client client = Client.getInstance();
    private boolean isNumOfPlayer = true;


    @FXML Label customMessage;
    @FXML AnchorPane numOfPlayerBox;
    @FXML AnchorPane usernameBox;
    @FXML TextField numOfPlayer;
    @FXML TextField username;
    @FXML Button playButton;
    @FXML ImageView musicImage;





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
        ControllerHandler.getInstance().setMusicImage(musicImage);
        isNumOfPlayer = true;
        numOfPlayerBox.setVisible(true);
        usernameBox.setVisible(false);
        customMessage.setVisible(false);
    }


    public void showInsertUsername(){
        isNumOfPlayer = false;
        usernameBox.setVisible(true);
        numOfPlayerBox.setVisible(false);
    }



    @Override
    public void showCustomMessage(String msg) {
        customMessage.setVisible(true);
        customMessage.setText(msg);
    }

    @FXML
    public void changeMusic(){
        ControllerHandler.getInstance().changeMusic();
        ControllerHandler.getInstance().setMusicImage(musicImage);
    }
}
