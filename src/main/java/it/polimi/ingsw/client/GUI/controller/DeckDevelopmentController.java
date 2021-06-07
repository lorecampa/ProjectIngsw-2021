package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.DeckDevData;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;


public class DeckDevelopmentController extends Controller{
    private Node currNodeSelected = null;

    @FXML AnchorPane background;
    @FXML AnchorPane currCardSelBox;
    @FXML ImageView currCard;
    @FXML Button buyCardBtn;
    @FXML GridPane gridPaneDeck;

    @FXML
    public void initialize(){
        gridPaneDeck.setVisible(true);
        currCardSelBox.setVisible(false);
    }

    @Override
    public void setUpAll() {
        setUpDeckImages(Client.getInstance().getDeckDevData());
        showDeckDev();

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - background.getPrefWidth()) * 0.5;
        double y = bounds.getMinY() + (bounds.getHeight() - background.getPrefHeight()) * 0.5;
        super.stage.setX(x);
        super.stage.setY(y);
    }

    @FXML
    public void goBackToPersonalBoard(){
        ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
    }

    @FXML
    public void selectCard(MouseEvent event){
        Node source = event.getPickResult().getIntersectedNode();
        currNodeSelected = source;
        ImageView imageSelected;
        try {
            imageSelected = (ImageView) source;
        }catch (Exception e){
            System.out.println("lol ez");
            return;
        }
        currCard.setImage(imageSelected.getImage());
        gridPaneDeck.setDisable(true);
        currCardSelBox.setVisible(true);
    }

    @FXML
    public void buyCard(){
        int row = GridPane.getRowIndex(currNodeSelected);
        int col = GridPane.getColumnIndex(currNodeSelected);
        gridPaneDeck.setDisable(false);

        PersonalBoardController controller = (PersonalBoardController) ControllerHandler
                .getInstance().getController(Views.PERSONAL_BOARD);


        ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
        controller.askCardSlotSelection(row, col);
    }

    @FXML
    public void showDeckDev(){
        currCardSelBox.setVisible(false);
        gridPaneDeck.setDisable(false);
        gridPaneDeck.setVisible(true);
    }

    //---------------------
    //EXTERNAL METHODS
    //--------------------

    public void setUpDeckImages(DeckDevData deckDev){
        for (int i = 0; i < deckDev.getDeck().size(); i++){
            for (int j = 0; j < deckDev.getDeck().get(0).size(); j++){
                ImageView imageView = (ImageView) getNodeByRowColumnIndex(i, j);

                if (!deckDev.getDeck().get(i).get(j).isEmpty()){
                    imageView.setImage(new Image(deckDev.getDeck().get(i).get(j).get(0).toResourcePath()));
                }else{
                    int backId = 16*i + j + 1;
                    URL url = this.getClass().getResource("/GUI/back/"+backId+".png");
                    assert url != null;
                    imageView.setImage(new Image(url.toString()));
                    imageView.setDisable(true);
                }
            }
        }

    }

    private Node getNodeByRowColumnIndex (final int row, final int column) {
        Node result = null;
        ObservableList<Node> children = gridPaneDeck.getChildren();

        for (Node node : children) {
            ImageView image = (ImageView) node;
            if(GridPane.getRowIndex(image) == row && GridPane.getColumnIndex(image) == column) {
                result = node;
                break;
            }
        }

        return result;
    }
}
