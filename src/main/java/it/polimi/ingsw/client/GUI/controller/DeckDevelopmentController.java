package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.data.DeckDevData;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;



public class DeckDevelopmentController extends Controller{
    private Node currNodeSelected = null;


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
    }

    public void goBackToPersonalBoard(){
        ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
    }

    public void selectCard(MouseEvent event){
        Node source = event.getPickResult().getIntersectedNode();
        currNodeSelected = source;
        ImageView imageSelected = (ImageView) source;
        currCard.setImage(imageSelected.getImage());
        gridPaneDeck.setDisable(true);
        currCardSelBox.setVisible(true);
    }

    public void buyCard(){
        int row = GridPane.getRowIndex(currNodeSelected);
        int col = GridPane.getColumnIndex(currNodeSelected);
        gridPaneDeck.setDisable(false);

        PersonalBoardController controller = (PersonalBoardController) ControllerHandler
                .getInstance().getController(Views.PERSONAL_BOARD);

        controller.askCardSlotSelection(row, col);
        ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);

    }

    public void showDeckDev(){
        currCardSelBox.setVisible(false);
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
