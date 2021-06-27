package it.polimi.ingsw.client.GUI.controller;


import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Controller is an abstract class that defines the default behaviour of all classes
 * that manage a FXML view
 */
public abstract class Controller {
    protected Stage stage;
    protected Scene scene;

    /**
     * setter for the stage and scene of the game
     * @param scene the game scene
     * @param stage the game stage
     */
    public void setUp(@NotNull Scene scene,@NotNull Stage stage){
        this.stage = stage;
        this.scene = scene;
    }

    /**
     * Method called before view change, it reset the nodes visibility and values to their default states
     */
    public abstract void setUpAll();

    /**
     * Set up the scene and change it
     */
    public void activate(){
        stage.setIconified(false);
        setUpAll();
        stage.setScene(scene);
    }

    /**
     * Method that display a custom message in scene, re-implemented by the controllers that
     * need to display a message
     * @param msg the message to be displayed
     */
    public void showCustomMessage(String msg){ }

    /**
     * Method that creates a faded transition to a node
     * @param node the node to be processed
     */
    public void showFadedErrorMessage(Node node){
        FadeTransition fader = new FadeTransition(Duration.seconds(5), node);
        fader.setFromValue(1);
        fader.setToValue(0);
        fader.play();
    }


    /**
     * Method that prepare the background in the center position
     * @param background the pane to be set
     */
    public void setUpBackground(Pane background){
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - background.getPrefWidth()) * 0.5;
        double y = bounds.getMinY() + (bounds.getHeight() - background.getPrefHeight()) * 0.5;
        stage.setX(x);
        stage.setY(y);
    }


    /**
     * Method that prepare the resize property of the custom message box
     * @param customMessageBox the custom message box pane to be set
     */
    public void setUpCustomMessageBox(Pane customMessageBox){
        double defaultMessageFontSize = 46;
        Font font = Font.font(defaultMessageFontSize);
        double MAX_TEXT_WIDTH = 400;

        customMessageBox.setVisible(false);
        Label label = (Label) customMessageBox.getChildren().get(0);
        label.setFont(font);
        label.textProperty().addListener((observable, oldValue, newValue) -> {
            Text tmpText = new Text(newValue);
            tmpText.setFont(font);

            double textWidth = tmpText.getLayoutBounds().getWidth();

            if (textWidth <= MAX_TEXT_WIDTH) {
                label.setFont(font);
            } else {
                double newFontSize = defaultMessageFontSize * MAX_TEXT_WIDTH / textWidth;
                label.setFont(Font.font(font.getFamily(), newFontSize));
            }
        });
    }


}
