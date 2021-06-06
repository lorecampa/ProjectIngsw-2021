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
import it.polimi.ingsw.message.serverMessage.SinglePlayerMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Objects;

public class MainMenuController extends Controller{
    public final ControllerHandler handler = ControllerHandler.getInstance();
    public final Client client = Client.getInstance();
    public final String mainMenuMusic = "startGameSong.mp3";

    @FXML
    ImageView musicImage;

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
        client.setState(ClientState.ENTERING_LOBBY);
        client.writeToStream(new SinglePlayerMessage());
    }

    public void quitGame(){
        client.writeToStream(new QuitGame());
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void setUpAll() {
        ControllerHandler.getInstance().startSong(mainMenuMusic);
        ControllerHandler.getInstance().setMusicImage(musicImage);
    }

    @FXML
    public void changeMusic(){
        ControllerHandler.getInstance().changeMusic();
        ControllerHandler.getInstance().setMusicImage(musicImage);

    }


    //---------------------
    //EXTERNAL METHODS
    //--------------------



}
