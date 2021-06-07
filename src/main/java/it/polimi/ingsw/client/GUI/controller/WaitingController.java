package it.polimi.ingsw.client.GUI.controller;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class WaitingController extends Controller {

    @FXML
    AnchorPane background;
    @FXML Label message;

    @Override
    public void setUpAll() {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - background.getPrefWidth()) * 0.5;
        double y = bounds.getMinY() + (bounds.getHeight() - background.getPrefHeight()) * 0.5;
        super.stage.setX(x);
        super.stage.setY(y);
    }

    public void setLobbyMessage(){
        message.setText("Waiting for other players to join");
    }

    public void setPreMatchMessage(){
        message.setText("Waiting for other players to finish pre-match setup");
    }
}
