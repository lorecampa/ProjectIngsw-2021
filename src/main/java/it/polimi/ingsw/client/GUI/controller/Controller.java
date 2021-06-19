package it.polimi.ingsw.client.GUI.controller;


import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

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
     * abstract method called before scene change
     */
    public abstract void setUpAll();

    /**
     * set up the scene and change it
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

}
