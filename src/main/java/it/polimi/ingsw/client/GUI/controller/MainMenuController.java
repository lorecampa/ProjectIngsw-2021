package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.command.MainMenuCMD;
import it.polimi.ingsw.client.command.QuitCMD;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.serverMessage.QuitGame;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController extends Controller{
    public final ControllerHandler handler = ControllerHandler.getInstance();
    public final Client client = Client.getInstance();

    @FXML
    Button multiBtn;

    @FXML
    Button singleBtn;

    @FXML
    Button quitBtn;

    @Override
    public void showErrorMessage(String msg) {

    }

    //---------------------
    //INTERNAL METHODS
    //--------------------

    public void playMultiplayer(){
        client.writeToStream(new ConnectionMessage(ConnectionType.CONNECT, ""));
        client.setState(ClientState.ENTERING_LOBBY);
        //handler.changeView(Views.SETUP);
    }

    public void playSinglePlayer(){
        //TODO
    }

    public void quitGame(){
        new QuitCMD(null, client).doCommand();
    }

    @Override
    public void setUpAll() {

    }

    //---------------------
    //EXTERNAL METHODS
    //--------------------



}
