package it.polimi.ingsw.client.GUI.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;


public class DeckDevelopmentController extends Controller{
    private ImageView[] deck = new ImageView[12];

    @FXML Label customMessage;

    @FXML AnchorPane deckBox;
    @FXML ImageView row0col0;
    @FXML ImageView row1col0;
    @FXML ImageView row2col0;

    @FXML ImageView row0col1;
    @FXML ImageView row1col1;
    @FXML ImageView row2col1;

    @FXML ImageView row0col2;
    @FXML ImageView row1col2;
    @FXML ImageView row2col2;

    @FXML ImageView row0col3;
    @FXML ImageView row1col3;
    @FXML ImageView row2col3;


    @Override
    public void setUpAll() {
        deckBox.setVisible(true);
        customMessage.setVisible(false);
        deck[0] = row0col0;
        deck[1] = row1col0;
        deck[2] = row2col0;
        deck[3] = row0col1;
        deck[4] = row1col1;
        deck[5] = row2col1;
        deck[6] = row0col2;
        deck[7] = row1col2;
        deck[8] = row2col2;
        deck[9] = row0col3;
        deck[10] = row1col3;
        deck[11] = row2col3;
    }

    public void selectCard(MouseEvent event){

    }
}
