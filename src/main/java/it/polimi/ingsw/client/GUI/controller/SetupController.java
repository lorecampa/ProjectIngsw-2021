package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;


public class SetupController extends Controller{
    @FXML private AnchorPane background;
    @FXML private AnchorPane numOfPlayerBox;
    @FXML private AnchorPane usernameBox;
    @FXML private TextField numOfPlayer;
    @FXML private TextField username;
    @FXML private ImageView musicImage;
    @FXML private Slider musicVolume;
    @FXML private AnchorPane customMessageBox;

    private boolean isNumOfPlayer = true;

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
        ControllerHandler.getInstance().setMusicImage(musicImage);
        musicVolume.setValue(ControllerHandler.getInstance().getVolume()*100);
        reset();
        setUpBackground(background);
    }

    /**
     * Method that sends user num of player or username response
     */
    @FXML
    public void sendData(){
        if (isNumOfPlayer){
            int num;
            try{
                num = Integer.parseInt(numOfPlayer.getText());
                Client.getInstance().writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER, num));
            }catch (Exception e){
                numOfPlayer.clear();
                showCustomMessage("Please insert a number between 1 and 4");
            }
        }else{
            String name = username.getText();
            if (name.length() == 0 || name.length() > 20)
                showCustomMessage("Insert a valid username, length between 1 and 20");
            else {
                Client.getInstance().setMyName(username.getText());
                Client.getInstance().writeToStream(new ConnectionMessage(ConnectionType.USERNAME, username.getText()));
            }
        }
    }

    /**
     * See {@link Controller#showCustomMessage(String)}
     * @param msg the message to be displayed
     */
    @Override
    public void showCustomMessage(String msg) {
        Label label = (Label) customMessageBox.getChildren().get(0);
        TextField  tf = new TextField(msg);
        label.textProperty().bind(tf.textProperty());
        label.setTextFill(Paint.valueOf("Red"));
        customMessageBox.setVisible(true);
        showFadedErrorMessage(customMessageBox);
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
     * Method that resets the visibility of all elements
     */
    public void reset(){
        usernameBox.setVisible(false);
        numOfPlayerBox.setVisible(false);
    }

    /**
     * Method that makes visible the box containing the number of player form
     */
    public void showNumOfPLayer(){
        isNumOfPlayer = true;
        usernameBox.setVisible(false);
        numOfPlayerBox.setVisible(true);
    }

    /**
     * Method that makes visible the box containing the username form
     */
    public void showInsertUsername(){
        isNumOfPlayer = false;
        usernameBox.setVisible(true);
        numOfPlayerBox.setVisible(false);
    }
}
