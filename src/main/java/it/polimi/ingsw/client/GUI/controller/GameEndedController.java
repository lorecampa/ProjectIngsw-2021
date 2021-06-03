package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.PrintAssistant;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.*;

public class GameEndedController extends Controller{

    @FXML private GridPane matchRankingGrid;
    @FXML private Button goToMainMenuBtn;

    @Override
    public void setUpAll() {
        matchRankingGrid.setVisible(true);
        goToMainMenuBtn.setVisible(true);
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
