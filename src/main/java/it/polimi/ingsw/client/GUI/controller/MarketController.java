package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.client.data.MarketData;
import it.polimi.ingsw.message.serverMessage.MarketAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

public class MarketController extends Controller{

    @FXML private VBox background;
    @FXML private Button btn_row0;
    @FXML private Button btn_row1;
    @FXML private Button btn_row2;

    @FXML private Button btn_col0;
    @FXML private Button btn_col1;
    @FXML private Button btn_col2;
    @FXML private Button btn_col3;

    @FXML private ImageView marble_ins;
    @FXML private ImageView marble_00;
    @FXML private ImageView marble_01;
    @FXML private ImageView marble_02;
    @FXML private ImageView marble_03;
    @FXML private ImageView marble_10;
    @FXML private ImageView marble_11;
    @FXML private ImageView marble_12;
    @FXML private ImageView marble_13;
    @FXML private ImageView marble_20;
    @FXML private ImageView marble_21;
    @FXML private ImageView marble_22;
    @FXML private ImageView marble_23;

    private ArrayList<ArrayList<ImageView>> marbles;

    /**
     * Method that prepare all the marbles and buttons in the scene
     */
    @FXML
    public void initialize(){
        setUpMarbles();
    }

    /**
     * See {@link Controller#setUpAll()}
     */
    @Override
    public void setUpAll(){
        MarketData marketData = Client.getInstance().getMarketData();
        marble_ins.setImage(new Image(marketData.getExtraMarble().toMarbleResource()));
        ArrayList<ArrayList<ColorData>> marketTray = marketData.getMarketTray();
        for (int i = 0; i < marketTray.size(); i++) {
            for (int j = 0; j < marketTray.get(i).size(); j++) {
                marbles.get(i).get(j).setImage(new Image(marketTray.get(i).get(j).toMarbleResource()));
            }
        }

        setUpBackground(background);

    }

    /**
     * Method that creates a matrix of marbles
     */
    private void setUpMarbles(){
        marbles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ArrayList<ImageView> row = new ArrayList<>();
            switch (i){
                case 0:
                    row.add(marble_00);
                    row.add(marble_01);
                    row.add(marble_02);
                    row.add(marble_03);
                    break;
                case 1:
                    row.add(marble_10);
                    row.add(marble_11);
                    row.add(marble_12);
                    row.add(marble_13);
                    break;
                case 2:
                    row.add(marble_20);
                    row.add(marble_21);
                    row.add(marble_22);
                    row.add(marble_23);
                    break;
            }
            marbles.add(row);
        }
    }

    /**
     * Method that handle a insert marble in row event
     * @param event the event to be handled
     */
    @FXML
    public void insertInRow(ActionEvent event){
        if (event.getSource().equals(btn_row0))
            Client.getInstance().writeToStream(new MarketAction(0,true));
        if (event.getSource().equals(btn_row1))
            Client.getInstance().writeToStream(new MarketAction(1,true));
        if (event.getSource().equals(btn_row2))
            Client.getInstance().writeToStream(new MarketAction(2,true));
    }

    /**
     * Method that handle a insert marble in column event
     * @param event the event to be handled
     */
    @FXML
    public void insertInCol(ActionEvent event){
        if (event.getSource().equals(btn_col0))
            Client.getInstance().writeToStream(new MarketAction(0,false));
        if (event.getSource().equals(btn_col1))
            Client.getInstance().writeToStream(new MarketAction(1,false));
        if (event.getSource().equals(btn_col2))
            Client.getInstance().writeToStream(new MarketAction(2,false));
        if (event.getSource().equals(btn_col3))
            Client.getInstance().writeToStream(new MarketAction(3,false));
    }

    /**
     * Method attached to the "go back" button
     */
    @FXML
    public void back(){
        ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
    }


    /**
     * Method that handle insertion buttons hover effects
     * @param event the event to be handled
     */
    public void hoverButton(MouseEvent event){
        if (event.getSource().equals(btn_row0)){
            setCSS(true, true, 0);

        }else if (event.getSource().equals(btn_row1)){
            setCSS(true, true, 1);

        }else if (event.getSource().equals(btn_row2)){
            setCSS(true, true, 2);

        }else if (event.getSource().equals(btn_col0)){
            setCSS(false, true, 0);

        }else if (event.getSource().equals(btn_col1)) {
            setCSS(false, true, 1);

        }else if (event.getSource().equals(btn_col2)){
            setCSS(false, true, 2);

        }else if (event.getSource().equals(btn_col3)){
            setCSS(false, true, 3);

        }

    }

    /**
     * Method that handle insertion buttons hover exit
     * @param event the event to be handled
     */
    public void hoverButtonExit(MouseEvent event){
        if (event.getSource().equals(btn_row0)){
            setCSS(true, false, 0);

        }else if (event.getSource().equals(btn_row1)){
            setCSS(true, false, 1);

        }else if (event.getSource().equals(btn_row2)){
            setCSS(true, false, 2);

        }else if (event.getSource().equals(btn_col0)){
            setCSS(false, false, 0);

        }else if (event.getSource().equals(btn_col1)) {
            setCSS(false, false, 1);

        }else if (event.getSource().equals(btn_col2)){
            setCSS(false, false, 2);

        }else if (event.getSource().equals(btn_col3)){
            setCSS(false, false, 3);

        }
    }

    /**
     * Method used to change the  css style of a specific button
     * @param isRow true if it is a button in the row side, false otherwise
     * @param isSet already set
     * @param num to set
     */
    private void setCSS(boolean isRow, boolean isSet, int num){
        if(isRow) {
            ArrayList<ImageView> myRow=marbles.get(num);
            myRow.forEach(x->{
                if (isSet) {
                    x.getStyleClass().add("marble");
                } else {
                    x.getStyleClass().remove("marble");
                }
            });
        }
        else{
            for(int i=0; i<3; i++){
                if(isSet)
                    marbles.get(i).get(num).getStyleClass().add("marble");
                else
                    marbles.get(i).get(num).getStyleClass().remove("marble");
            }
        }
    }
}
