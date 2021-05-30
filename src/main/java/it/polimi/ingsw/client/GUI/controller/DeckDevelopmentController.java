package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.data.DeckDevData;
import it.polimi.ingsw.message.serverMessage.DevelopmentAction;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;


public class DeckDevelopmentController extends Controller{
    private Node currNodeSelected = null;
    private ArrayList<ImageView> deck = new ArrayList<>();

    @FXML Label customMessage;
    @FXML AnchorPane currCardSelBox;
    @FXML ImageView currCard;
    @FXML Button buyCardBtn;
    @FXML GridPane gridPaneDeck;



    @Override
    public void setUpAll() {
        gridPaneDeck.setVisible(true);
        currCardSelBox.setVisible(false);
        customMessage.setVisible(false);

    }

    public void selectCard(MouseEvent event){
        Node source = event.getPickResult().getIntersectedNode();
        System.out.println("Row: "+GridPane.getColumnIndex(source));
        System.out.println("Col: "+GridPane.getRowIndex(source));
        currNodeSelected = source;
        ImageView imageSelected = (ImageView) source;
        currCard.setImage(imageSelected.getImage());

        gridPaneDeck.setDisable(true);
        gridPaneDeck.setVisible(false);
        currCardSelBox.setVisible(true);
    }

    public void buyCard(ActionEvent event){
        int row = GridPane.getRowIndex(currNodeSelected);
        int col = GridPane.getColumnIndex(currNodeSelected);
        //TODO change locate slot position
        Client.getInstance().writeToStream(new DevelopmentAction(row, col,0));
    }

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
                imageView.setImage(new Image(deckDev.getDeck().get(i).get(j).get(0).toResourcePath()));
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
