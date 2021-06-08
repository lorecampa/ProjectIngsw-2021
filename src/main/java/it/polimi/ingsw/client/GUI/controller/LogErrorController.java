package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.LinkedHashMap;

public class LogErrorController extends Controller{

    @FXML private ScrollPane scrollPane;
    @FXML private VBox containerVbox;
    @FXML private Button backToPBBtn;
    @FXML private AnchorPane noErrorLogBox;

    @Override
    public void setUpAll() {
        LinkedHashMap<String , String> log = Client.getInstance().getMyModel().getErrorMessagesLog();
        if (log.isEmpty()){
            scrollPane.setVisible(false);
            noErrorLogBox.setVisible(true);
        }else{
            containerVbox.getChildren().clear();
            noErrorLogBox.setVisible(false);
            scrollPane.setVisible(true);
            setUpErrorLog(log);
        }
    }


    private void setUpErrorLog(LinkedHashMap<String, String> logError){
        for (String date: logError.keySet()){
            HBox hbox = createHboxContainer();
            hbox.getChildren().add(createDateLabel(date));
            hbox.getChildren().add(createErrorLabel(logError.get(date)));

            containerVbox.getChildren().add(0, hbox);
        }

    }


    private Label createDateLabel(String date) {
        Label labelDate = new Label();
        labelDate.setWrapText(true);
        labelDate.setPrefWidth(145);
        labelDate.setPrefHeight(70);
        labelDate.setMaxWidth(Region.USE_PREF_SIZE);
        labelDate.setMinWidth(Region.USE_PREF_SIZE);
        labelDate.setFont(new Font(15));
        labelDate.setAlignment(Pos.CENTER);

        labelDate.setText(date);
        return labelDate;
    }

    private Label createErrorLabel(String error) {
        Label labelError = new Label();
        labelError.setWrapText(true);
        labelError.setPrefWidth(170);
        labelError.setPrefHeight(80);
        labelError.setMaxWidth(Region.USE_PREF_SIZE);
        labelError.setMinWidth(Region.USE_PREF_SIZE);

        labelError.setFont(new Font(15));
        labelError.setAlignment(Pos.CENTER);
        labelError.setText(error);

        return labelError;
    }

    private HBox createHboxContainer(){
        HBox container = new HBox();
        container.setPrefWidth(315);
        container.setPrefHeight(70);
        container.setMinWidth(Region.USE_PREF_SIZE);
        container.setMinHeight(Region.USE_PREF_SIZE);
        container.setStyle("-fx-background-color: rgba(190,190,190,0.3);");

        return container;
    }

    @FXML
    public void goBack(){
        ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
    }


}
