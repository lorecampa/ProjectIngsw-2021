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
import java.net.URL;

/**
 * DeckDevelopmentController class is the class that manage the GUI view of deck development cards
 */
public class DeckDevelopmentController extends Controller{
    private Node currNodeSelected = null;

    @FXML AnchorPane background;
    @FXML AnchorPane currCardSelBox;
    @FXML ImageView currCard;
    @FXML Button buyCardBtn;
    @FXML GridPane gridPaneDeck;


    /**
     * See {@link Controller#setUpAll()} ()}
     */
    @Override
    public void setUpAll() {
        setUpDeckImages();
        showDeckDev();
        setUpBackground(background);
    }

    /**
     * Method attached to the goBack button that change the current view
     */
    @FXML
    public void goBackToPersonalBoard(){
        ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
    }

    /**
     * Method that handle mouse event over a deck dev card, makes it bigger in order to
     * visualize it better
     * @param event the mouse event that will be handled
     */
    @FXML
    public void selectCard(MouseEvent event){
        Node source = event.getPickResult().getIntersectedNode();
        currNodeSelected = source;
        ImageView imageSelected;
        try {
            imageSelected = (ImageView) source;
        }catch (Exception e){
            return;
        }
        currCard.setImage(imageSelected.getImage());
        gridPaneDeck.setDisable(true);
        currCardSelBox.setVisible(true);
    }

    /**
     * Method that handle the buy card button in the current card selected
     */
    @FXML
    public void buyCard(){
        int row = GridPane.getRowIndex(currNodeSelected);
        int col = GridPane.getColumnIndex(currNodeSelected);
        gridPaneDeck.setDisable(false);

        ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);

        PersonalBoardController controller = (PersonalBoardController) ControllerHandler
                .getInstance().getController(Views.PERSONAL_BOARD);
        controller.askCardSlotSelection(row, col);
    }

    /**
     * Method that set to true the visibility of all nodes that concern the deck dev
     */
    @FXML
    public void showDeckDev(){
        currCardSelBox.setVisible(false);
        gridPaneDeck.setDisable(false);
        gridPaneDeck.setVisible(true);
    }


    /**
     * Method that set the deck development card images
     */
    public void setUpDeckImages(){
        DeckDevData deckDev = Client.getInstance().getDeckDevData();
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

    /**
     * Method that return a specific node in the deck development grid pane
     * @param row the row index
     * @param column the col index
     * @return the node in (row, col) or null if it is not present
     */
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
