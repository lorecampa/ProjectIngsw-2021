package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGUI extends Application {

    private Scene scene;
    private final FXMLLoader fxmlLoader = new FXMLLoader();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();

        ClientMessageHandler clientMessageHandler = new ClientMessageHandler(this);
        clientMessageHandler.provaMessaggioDaServer();
        clientMessageHandler.pr2();
    }

    private Parent loadFXML(String fxml) throws IOException {
        fxmlLoader.setLocation(ClientGUI.class.getResource(fxml+".fxml"));
        return fxmlLoader.load();
    }

    public Object getController(String fxml){
        return fxmlLoader.getController();
    }

    public static void main(String[] args) {
        launch();
    }
}
