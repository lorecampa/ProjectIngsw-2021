package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.command.NumOfPlayerCMD;
import it.polimi.ingsw.client.command.UsernameCMD;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetupController extends Controller{
    private final Client client = Client.getInstance();
    private boolean isNumOfPlayer = true;

    @FXML AnchorPane background;
    @FXML AnchorPane numOfPlayerBox;
    @FXML AnchorPane usernameBox;
    @FXML TextField numOfPlayer;
    @FXML TextField username;
    @FXML Button playButton;
    @FXML ImageView musicImage;
    @FXML Slider musicVolume;

    @FXML AnchorPane customMessageBox;

    private final double MAX_TEXT_WIDTH = 400;
    private final double defaultFontSize = 46;
    private final Font defaultFont = Font.font(defaultFontSize);

    @FXML
    public void sendData(){
        if (isNumOfPlayer){
            int num;
            try{
                num = Integer.parseInt(numOfPlayer.getText());
                client.writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER, num));
            }catch (Exception e){
                numOfPlayer.clear();
                showCustomMessage("Please insert a number between 1 and 4");
            }
        }else{
            String name = username.getText();
            if (name.length() == 0 || name.length() > 20)
                showCustomMessage("Insert a valid username, length between 1 and 20");
            else {
                client.setMyName(username.getText());
                client.writeToStream(new ConnectionMessage(ConnectionType.USERNAME, username.getText()));
            }
        }
    }

    @FXML
    public void initialize(){
        musicVolume.valueProperty().addListener((ov, old_val, new_val) -> {
            double volume = new_val.doubleValue()/100;
            ControllerHandler.getInstance().setVolume(volume);});
        customMessageBox.setVisible(false);

        Label label = (Label) customMessageBox.getChildren().get(0);
        label.setFont(defaultFont);
        label.textProperty().addListener((observable, oldValue, newValue) -> {
            Text tmpText = new Text(newValue);
            tmpText.setFont(defaultFont);

            double textWidth = tmpText.getLayoutBounds().getWidth();

            if (textWidth <= MAX_TEXT_WIDTH) {
                label.setFont(defaultFont);
            } else {
                double newFontSize = defaultFontSize * MAX_TEXT_WIDTH / textWidth;
                label.setFont(Font.font(defaultFont.getFamily(), newFontSize));
            }
        });
    }


    //---------------------
    //EXTERNAL METHODS
    //--------------------

    @Override
    public void setUpAll() {
        ControllerHandler.getInstance().setMusicImage(musicImage);
        isNumOfPlayer = true;
        numOfPlayerBox.setVisible(false);
        usernameBox.setVisible(false);

        musicVolume.setValue(ControllerHandler.getInstance().getVolume()*100);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - background.getPrefWidth()) * 0.5;
        double y = bounds.getMinY() + (bounds.getHeight() - background.getPrefHeight()) * 0.5;
        super.stage.setX(x);
        super.stage.setY(y);
    }

    public void showNumOfPLayer(){
        isNumOfPlayer = true;
        usernameBox.setVisible(false);
        numOfPlayerBox.setVisible(true);
    }


    public void showInsertUsername(){
        isNumOfPlayer = false;
        usernameBox.setVisible(true);
        numOfPlayerBox.setVisible(false);
    }



    @Override
    public void showCustomMessage(String msg) {
        Label label = (Label) customMessageBox.getChildren().get(0);

        TextField  tf = new TextField(msg);
        label.textProperty().bind(tf.textProperty());


        label.setTextFill(Paint.valueOf("Red"));
        customMessageBox.setVisible(true);
        showFadedErrorMessage(customMessageBox);
    }

    @FXML
    public void changeMusic(){
        ControllerHandler.getInstance().changeMusic();
        ControllerHandler.getInstance().setMusicImage(musicImage);
    }
}
