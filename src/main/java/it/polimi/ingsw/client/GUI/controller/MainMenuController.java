package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.serverMessage.QuitGame;
import it.polimi.ingsw.message.serverMessage.SinglePlayerMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainMenuController extends Controller{
    public final ControllerHandler handler = ControllerHandler.getInstance();
    public final Client client = Client.getInstance();
    public final String mainMenuMusic = "startGameSong.mp3";
    @FXML private AnchorPane background;
    @FXML private ImageView musicImage;
    @FXML private Slider musicVolume;
    @FXML private AnchorPane customMessageBox;
    @FXML private Label customMessageLabel;


    /**
     * Method that prepare the scene
     */
    @FXML
    public void initialize(){
        musicVolume.valueProperty().addListener((ov, old_val, new_val) -> {
            double volume = new_val.doubleValue()/100;
            ControllerHandler.getInstance().setVolume(volume);});

        setUpCustomMessageBox(customMessageBox);
    }

    /**
     * See {@link Controller#setUpAll()}
     */
    @Override
    public void setUpAll() {
        ControllerHandler.getInstance().startSong(mainMenuMusic);
        ControllerHandler.getInstance().setMusicImage(musicImage);
        setUpBackground(background);
    }

    /**
     * Method attached to the sound image, it stops the music if it is playing, otherwise it will start it
     */
    @FXML
    public void changeMusic(){
        ControllerHandler.getInstance().changeMusic();
        ControllerHandler.getInstance().setMusicImage(musicImage);

    }

    /**
     * Method attached to the button "reconnect"
     * @throws FileNotFoundException if the file with the last match information are not present
     */
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

    /**
     * See {@link Controller#showCustomMessage(String)}
     * @param msg the message to be displayed
     */
    @Override
    public void showCustomMessage(String msg) {
        TextField tf = new TextField(msg);
        customMessageLabel.textProperty().bind(tf.textProperty());
        customMessageBox.setVisible(true);
        showFadedErrorMessage(customMessageBox);
    }



    /**
     * Method attached to the "play multiplayer" button
     */
    public void playMultiplayer(){
        client.setState(ClientState.ENTERING_LOBBY);
        client.writeToStream(new ConnectionMessage(ConnectionType.CONNECT, ""));
    }

    /**
     * Method attached to the "play single player" button
     */
    public void playSinglePlayer(){
        client.setState(ClientState.ENTERING_LOBBY);
        client.writeToStream(new SinglePlayerMessage());
    }

    /**
     * Method attached to the "quit game" button
     */
    public void quitGame(){
        client.writeToStream(new QuitGame());
        Platform.exit();
        System.exit(0);
    }


}
