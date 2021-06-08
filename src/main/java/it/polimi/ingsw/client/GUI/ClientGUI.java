package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.controller.Controller;

import it.polimi.ingsw.message.serverMessage.QuitGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClientGUI extends Application {
    private final List<Views> views = Arrays.asList(Views.values());
    private Stage stage;
    private Scene scene;
    private final ControllerHandler controllerHandler = ControllerHandler.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        setUpControllers();
        stage.setResizable(false);
        controllerHandler.changeView(Views.MAIN_MENU);
        stage.show();

        stage.setOnCloseRequest(t -> {
            Client.getInstance().writeToStream(new QuitGame());
            Platform.exit();
            System.exit(0);
        });

    }

    private void setUpControllers() throws IOException {
        for (Views view: views){
            FXMLLoader loader = new FXMLLoader(getResources(view.getName()));
            Parent root = loader.load();
            Controller controller = loader.getController(); //Get graphics controller
            controller.setUp(new Scene(root), stage); //Add root to controller
            controllerHandler.addController(view, controller);
        }
    }

    private URL getResources(String fxml){
        return getClass().getResource(fxml + ".fxml");
    }

    public static void main(String[] args){
        launch(args);
    }
}
