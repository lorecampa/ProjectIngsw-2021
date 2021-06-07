package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.PrintAssistant;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameEndedController extends Controller{

    @FXML
    AnchorPane background;
    @FXML private GridPane matchRankingGrid;
    @FXML private Button goToMainMenuBtn;

    @Override
    public void setUpAll() {
        matchRankingGrid.setVisible(true);
        goToMainMenuBtn.setVisible(true);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - background.getPrefWidth()) * 0.5;
        double y = bounds.getMinY() + (bounds.getHeight() - background.getPrefHeight()) * 0.5;
        super.stage.setX(x);
        super.stage.setY(y);
    }

    public void setUpRanking(Map<Integer, String> players){
        ObservableList<Node> children = matchRankingGrid.getChildren();

        TreeMap<Integer, String> matchRanking = new TreeMap<>(Collections.reverseOrder());
        matchRanking.putAll(players);
        Set<Map.Entry<Integer, String>> entries = matchRanking.entrySet();
        int i = 0;
        for(Map.Entry<Integer, String> entry : entries){
            Label label = (Label) children.get(i);
            label.setText(entry.getKey()+": "+entry.getValue());
            i++;
        }
    }


    @FXML
    public void goToMainMenu(){
        Client.getInstance().setState(ClientState.MAIN_MENU);
        Client.getInstance().clearModels();
        ControllerHandler.getInstance().changeView(Views.MAIN_MENU);
    }





}
