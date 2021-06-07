package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.command.NumOfPlayerCMD;
import it.polimi.ingsw.client.command.UsernameCMD;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetupController extends Controller{
    private final Client client = Client.getInstance();
    private boolean isNumOfPlayer = true;


    @FXML AnchorPane background;
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

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - background.getPrefWidth()) * 0.5;
        double y = bounds.getMinY() + (bounds.getHeight() - background.getPrefHeight()) * 0.5;
        super.stage.setX(x);
        super.stage.setY(y);
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
