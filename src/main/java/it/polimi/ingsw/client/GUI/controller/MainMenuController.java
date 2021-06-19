package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.ControllerHandler;

import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.message.serverMessage.QuitGame;
import it.polimi.ingsw.message.serverMessage.SinglePlayerMessage;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    Slider musicVolume;

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

    @FXML AnchorPane customMessageBox;
    @FXML Label customMessageLabel;

    private final double MAX_TEXT_WIDTH = 400;
    private final double defaultFontSize = 46;
    private final Font defaultFont = Font.font(defaultFontSize);


    @FXML
    public void initialize(){
        musicVolume.valueProperty().addListener((ov, old_val, new_val) -> {
            double volume = new_val.doubleValue()/100;
            ControllerHandler.getInstance().setVolume(volume);});

        customMessageBox.setVisible(false);

        customMessageLabel.setFont(defaultFont);
        customMessageLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            Text tmpText = new Text(newValue);
            tmpText.setFont(defaultFont);

            double textWidth = tmpText.getLayoutBounds().getWidth();

            if (textWidth <= MAX_TEXT_WIDTH) {
                customMessageLabel.setFont(defaultFont);
            } else {
                double newFontSize = defaultFontSize * MAX_TEXT_WIDTH / textWidth;
                customMessageLabel.setFont(Font.font(defaultFont.getFamily(), newFontSize));
            }
        });
    }

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


        File file = new File(Client.getInstance().DATA_LAST_GAME);
        if(!file.exists()){
            showCustomMessage("There is no game to reconnect yet!");
            return;
        }
        Scanner scanner= new Scanner(file);
        matchID=Integer.parseInt(scanner.nextLine());
        clientID=Integer.parseInt(scanner.nextLine());

        client.writeToStream( new ReconnectionMessage(matchID,clientID));
        client.setState(ClientState.ENTERING_LOBBY);
    }

    @Override
    public void showCustomMessage(String msg) {
        TextField tf = new TextField(msg);

        customMessageLabel.textProperty().bind(tf.textProperty());


        customMessageBox.setVisible(true);
        showFadedErrorMessage(customMessageBox);
    }

    //---------------------
    //EXTERNAL METHODS
    //--------------------



}
