package it.polimi.ingsw.client.GUI;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public abstract class Controller {
    private Stage stage;
    private Scene scene;

    public void setUp(@NotNull Parent root,@NotNull Stage stage){
        this.stage = stage;
        this.scene = new Scene(root); //Just create a new scene with predefined size
    }

    public void activate(){
        stage.setScene(scene);
    }
}
