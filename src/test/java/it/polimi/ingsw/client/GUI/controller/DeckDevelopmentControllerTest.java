package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.data.DeckDevData;
import it.polimi.ingsw.client.data.MarketData;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.card.Development;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;



class DeckDevelopmentControllerTest{

    GameSetting gameSetting;
    @Test
    void setUpDeckImages()  {
        assertDoesNotThrow(() -> gameSetting = new GameSetting(1));


        DeckDevData deckDevData = new DeckDevData(gameSetting.getDeckDevelopment().stream()
                .map(row -> row.stream()
                        .map(singleDeck -> singleDeck.stream()
                                .map(Development::toCardDevData)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new)));

        /*
        Platform.runLater(()->{
            DeckDevelopmentController deckController = (DeckDevelopmentController) ControllerHandler.getInstance().getController(Views.DECK_DEV);
            deckController.setUpDeckImages(deckDevData);
            ControllerHandler.getInstance().changeView(Views.DECK_DEV);
        });

        ClientGUI.main(null);
        */

    }

}