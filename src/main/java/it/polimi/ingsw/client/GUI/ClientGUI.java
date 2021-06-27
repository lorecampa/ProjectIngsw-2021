package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.controller.Controller;
import it.polimi.ingsw.message.serverMessage.QuitGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ClientGUI extends Application {
    private final List<Views> views = Arrays.asList(Views.values());
    private Stage stage;
    private final ControllerHandler controllerHandler = ControllerHandler.getInstance();

    @Override
    public void start(Stage stage) throws IOException {

        this.stage = stage;
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/GUI/icons/icon.png"))));
        setUpControllers();
        stage.setResizable(false);
        controllerHandler.changeView(Views.MAIN_MENU);
        stage.show();
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

    }

    private void closeWindowEvent(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle("Quit application");
        alert.setContentText("Are you sure you want to exit?");
        alert.initOwner(stage.getOwner());
        Optional<ButtonType> res = alert.showAndWait();

        if(res.isPresent()) {
            if(res.get().equals(ButtonType.CANCEL)){
                event.consume();
            }else if(res.get().equals(ButtonType.YES)){
                Client.getInstance().writeToStream(new QuitGame());
                Platform.exit();
                System.exit(0);
            }
        }
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
