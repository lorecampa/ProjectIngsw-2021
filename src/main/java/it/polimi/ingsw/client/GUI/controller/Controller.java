package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.Views;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        stage.setScene(scene);
        setUpAll();
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




    public void showErrorMessage(String msg){
        System.out.println(msg);
    }

}
