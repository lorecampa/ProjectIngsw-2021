package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import java.util.*;

/**
 * GameEndedController class is the class that manage the GUI view of the game ending
 */
public class GameEndedController extends Controller{

    @FXML private AnchorPane background;
    @FXML private GridPane matchRankingGrid;
    @FXML private Button goToMainMenuBtn;


    /**
     * See {@link Controller#setUpAll()} ()}
     */
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

    /**
     * Method attached to the "go to main menu" button, clear client models and
     * switch the view to the main menu
     */
    @FXML
    public void goToMainMenu(){
        Client.getInstance().setState(ClientState.MAIN_MENU);
        Client.getInstance().clearModels();
        ControllerHandler.getInstance().changeView(Views.MAIN_MENU);
    }

    /**
     * Method attached to the "go back" button, permit personal board observing after game ending
     */
    @FXML
    public void back(){
        PersonalBoardController controller = (PersonalBoardController) ControllerHandler.getInstance().getController(Views.PERSONAL_BOARD);
        ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
        controller.setUpForEnd();
    }

    /**
     * Method that display the final result of the game
     * @param players the map with key the number of point reached and value the player username
     */
    public void setUpRanking(Map<Float, String> players){
        ObservableList<Node> children = matchRankingGrid.getChildren();
        children.forEach(x -> {
            Label label = (Label) x;
            label.setVisible(false);
        });
        TreeMap<Float, String> matchRanking = new TreeMap<>(Collections.reverseOrder());
        matchRanking.putAll(players);
        Set<Map.Entry<Float, String>> entries = matchRanking.entrySet();
        int i = 0;
        for(Map.Entry<Float, String> entry : entries){
            Label label = (Label) children.get(i);
            label.setText(Math.round(entry.getKey())+": "+entry.getValue());
            label.setVisible(true);
            i++;
        }
    }
}
