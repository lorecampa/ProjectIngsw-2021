package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ClientGUI extends Application {

    private Stage stage;

    private Scene scene;
    private final FXMLLoader fxmlLoader = new FXMLLoader();

    private final ControllerHandler controllerHandler = ControllerHandler.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        setUpController();

        controllerHandler.getPrimaryController().activate();

        //TODO JUST FOR DEBUG
        //controllerHandler.getMarketController().setUpMarket();
        //controllerHandler.getMarketController().activate();
        stage.show();
    }

    private void setUpController() throws IOException {
        primarySetUp();
        secondarySetUp();
        marketSetUp();
    }

    private URL getResources(String fxml){
        // problemi con thread???
        return getClass().getResource(fxml + ".fxml");
    }

    private void primarySetUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResources("primary"));
        Parent root = loader.load(); //Get graphics root
        PrimaryController controller = loader.getController(); //Get graphics controller
        controller.setUp(root, stage); //Add root to controller
        controllerHandler.setPrimaryController(controller);
    }

    private void secondarySetUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResources("secondary"));
        Parent root = loader.load(); //Get graphics root
        SecondaryController controller = loader.getController(); //Get graphics controller
        controller.setUp(root, stage); //Add root to controller
        controllerHandler.setSecondaryController(controller);
    }

    private void marketSetUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResources("market"));
        Parent root = loader.load(); //Get graphics root
        MarketController controller = loader.getController(); //Get graphics controller
        controller.setUp(root, stage); //Add root to controller
        controllerHandler.setMarketController(controller);
    }
    /*
    private Parent loadFXML(String fxml) throws IOException {
        fxmlLoader.setLocation(ClientGUI.class.getResource(fxml+".fxml"));
        return fxmlLoader.load();
    }

     */

    public static void main(String[] args) {
        launch();
    }
}
