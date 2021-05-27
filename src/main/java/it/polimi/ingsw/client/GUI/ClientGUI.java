package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.controller.Controller;
import it.polimi.ingsw.client.GUI.controller.MarketController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.text.View;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ClientGUI extends Application {
    private List<Views> views = Arrays.asList(Views.values());
    private Stage stage;
    private Scene scene;
    private ControllerHandler controllerHandler = ControllerHandler.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        setUpControllers();
        //scene = new Scene(loadFXML("primary"), 640, 480);
        //stage.setScene(scene);
        controllerHandler.getController(Views.MAIN_MENU).activate();
        stage.show();
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

    private void setUp1() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResources("market"));
        Parent root = loader.load(); //Get graphics root
        Controller controller =  loader.getController(); //Get graphics controller
        controller.setUp(new Scene(root), stage); //Add root to controller
        controllerHandler.addController(Views.MARKET,controller);

    }


    private URL getResources(String fxml){
        return getClass().getResource(fxml + ".fxml");
    }


    public static void main(String[] args){
        launch(args);
    }
}
