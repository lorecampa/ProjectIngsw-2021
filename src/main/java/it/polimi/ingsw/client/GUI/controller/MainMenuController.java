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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MainMenuController extends Controller{
    public final ControllerHandler handler = ControllerHandler.getInstance();
    public final Client client = Client.getInstance();

    @FXML
    Button multiBtn;

    @FXML
    Button singleBtn;

    @FXML
    Button quitBtn;

    @FXML
    Text mainTitle;

    //---------------------
    //INTERNAL METHODS
    //--------------------

    public void playMultiplayer(){
        client.setState(ClientState.ENTERING_LOBBY);
        client.writeToStream(new ConnectionMessage(ConnectionType.CONNECT, ""));
    }

    public void playSinglePlayer(){
        //TODO
    }

    public void quitGame(){
        client.writeToStream(new QuitGame());
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void setUpAll() {
        //mainTitle.setFont(Font.loadFont("file:resources/GUI/fonts/Ruritania.ttf",120));
    }

    //---------------------
    //EXTERNAL METHODS
    //--------------------



}
