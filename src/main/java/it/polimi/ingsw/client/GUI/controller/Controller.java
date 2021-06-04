package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.Views;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.View;
import java.io.IOException;
import java.util.Objects;

public abstract class Controller {
    private Stage stage;
    private Scene scene;

    public void setUp(@NotNull Scene scene,@NotNull Stage stage){
        this.stage = stage;
        this.scene = scene; //Just create a new scene with predefined size
    }

    public abstract void setUpAll();

    public void activate(){
        setUpAll();
        stage.setScene(scene);
        //handle resize
    }

    public void activateOnNew(Views view){
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/client/GUI/"+view + ".fxml")));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        setUpAll();
    }





    public void showCustomMessage(String msg){
    }

    public void showFadedErrorMessage(AnchorPane pane){
        FadeTransition fader = createFader(pane);
        fader.play();
    }

    private FadeTransition createFader(Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(5), node);
        fade.setFromValue(1);
        fade.setToValue(0);
        return fade;
    }

    public String getResourcePath(String name){
        return Objects.requireNonNull(getClass().getResource("/GUI/" + name)).toString();
    }

}
