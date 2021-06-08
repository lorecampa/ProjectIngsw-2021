package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.client.command.MainMenuCMD;
import it.polimi.ingsw.client.command.QuitCMD;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.serverMessage.QuitGame;
import it.polimi.ingsw.message.serverMessage.SinglePlayerMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class MainMenuController extends Controller{
    public final ControllerHandler handler = ControllerHandler.getInstance();
    public final Client client = Client.getInstance();
    public final String mainMenuMusic = "startGameSong.mp3";

    @FXML
    AnchorPane background;

    @FXML
    ImageView musicImage;

    @FXML
    Button multiBtn;

    @FXML
    Button singleBtn;

    @FXML
    Button reconnectBtn;

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

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - background.getPrefWidth()) * 0.5;
        double y = bounds.getMinY() + (bounds.getHeight() - background.getPrefHeight()) * 0.5;
        super.stage.setX(x);
        super.stage.setY(y);
    }

    @FXML
    public void changeMusic(){
        ControllerHandler.getInstance().changeMusic();
        ControllerHandler.getInstance().setMusicImage(musicImage);

    }

    @FXML
    public void reconnect() throws FileNotFoundException {
        int matchID;
        int clientID;

        File file = new File(client.getNameFile());
        Scanner scanner= new Scanner(file);
        matchID=Integer.parseInt(scanner.nextLine());
        clientID=Integer.parseInt(scanner.nextLine());

        client.writeToStream( new ReconnectionMessage(matchID,clientID));
        client.setState(ClientState.ENTERING_LOBBY);
    }


    //---------------------
    //EXTERNAL METHODS
    //--------------------



}
