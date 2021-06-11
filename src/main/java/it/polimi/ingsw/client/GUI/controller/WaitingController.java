package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.ControllerHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class WaitingController extends Controller {

    @FXML
    AnchorPane background;
    @FXML
    Label message;

    private final double MAX_TEXT_WIDTH = 400;
    private final double defaultFontSize = 46;
    private final Font defaultFont = Font.font(defaultFontSize);

    @FXML
    public void initialize() {
        message.setFont(defaultFont);
        message.textProperty().addListener((observable, oldValue, newValue) -> {
            Text tmpText = new Text(newValue);
            tmpText.setFont(defaultFont);

            double textWidth = tmpText.getLayoutBounds().getWidth();

            if (textWidth <= MAX_TEXT_WIDTH) {
                message.setFont(defaultFont);
            } else {
                double newFontSize = defaultFontSize * MAX_TEXT_WIDTH / textWidth;
                message.setFont(Font.font(defaultFont.getFamily(), newFontSize));
            }
        });
    }

    @Override
    public void setUpAll() {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - background.getPrefWidth()) * 0.5;
        double y = bounds.getMinY() + (bounds.getHeight() - background.getPrefHeight()) * 0.5;
        super.stage.setX(x);
        super.stage.setY(y);
    }

    public void setLobbyMessage() {
        TextField tf = new TextField("Waiting for other players to join");

        message.textProperty().bind(tf.textProperty());
    }

    public void setPreMatchMessage() {
        TextField tf = new TextField("Waiting for other players to finish pre-match setup");

        message.textProperty().bind(tf.textProperty());
    }

    public void setReconnectMessage(String username) {
        TextField tf = new TextField("Reconnected as " + username + " Waiting for your turn to load the game");
        message.textProperty().bind(tf.textProperty());
    }
}
