package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.ControllerHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController extends Controller{
    public final ControllerHandler handler = ControllerHandler.getInstance();

    @FXML
    Button multiBtn;

    @FXML
    Button singleBtn;

    @FXML
    Button quitBtn;

    @FXML
    public void initialize(){
        System.out.println(("ciao"));
    }

    //---------------------
    //INTERNAL METHODS
    //--------------------

    public void playMultiplayer(){
        //handler.changeStage(Views.MAIN_MENU);
    }

    public void playSinglePlayer(){

    }

    public void quitGame(){

    }


    //---------------------
    //EXTERNAL METHODS
    //--------------------



}
