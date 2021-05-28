package it.polimi.ingsw.client.GUI.controller;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.data.ColorData;
import it.polimi.ingsw.client.data.MarketData;
import it.polimi.ingsw.message.serverMessage.MarketAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class MarketController extends Controller{
    @FXML Button btn_row0;
    @FXML Button btn_row1;
    @FXML Button btn_row2;

    @FXML Button btn_col0;
    @FXML Button btn_col1;
    @FXML Button btn_col2;
    @FXML Button btn_col3;

    @FXML ImageView marble_ins;
    @FXML ImageView marble_00;
    @FXML ImageView marble_01;
    @FXML ImageView marble_02;
    @FXML ImageView marble_03;
    @FXML ImageView marble_10;
    @FXML ImageView marble_11;
    @FXML ImageView marble_12;
    @FXML ImageView marble_13;
    @FXML ImageView marble_20;
    @FXML ImageView marble_21;
    @FXML ImageView marble_22;
    @FXML ImageView marble_23;


    private ArrayList<ArrayList<ImageView>> marbles;

    @FXML
    public void initialize(){
        setUpMarbles();
        setUpBtns();
    }

    @Override
    public void showErrorMessage(String msg) {

    }

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

    private void setUpBtns(){
        ArrayList<Button> btns = new ArrayList<>();
        btns.add(btn_col0);
        btns.add(btn_col1);
        btns.add(btn_col2);
        btns.add(btn_col3);
        btns.add(btn_row0);
        btns.add(btn_row1);
        btns.add(btn_row2);
        btns.forEach(this::setUpBtn);

    }

    private void setUpBtn(Button btn){
        btn.setStyle("-fx-background-color:transparent;");

        btn.setOnMouseEntered(t -> btn.setStyle("-fx-border-color:blue;-fx-background-color:transparent;"));

        btn.setOnMouseExited(t -> btn.setStyle("-fx-background-color:transparent;"));
    }


    @FXML
    public void insertInRow(ActionEvent event){
        if (event.getSource().equals(btn_row0))
            Client.getInstance().writeToStream(new MarketAction(0,true));
        if (event.getSource().equals(btn_row1))
            Client.getInstance().writeToStream(new MarketAction(1,true));
        if (event.getSource().equals(btn_row2))
            Client.getInstance().writeToStream(new MarketAction(2,true));
    }

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
    }


}
