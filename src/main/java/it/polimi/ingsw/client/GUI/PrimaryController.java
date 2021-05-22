package it.polimi.ingsw.client.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Objects;

public class PrimaryController extends Controller{
    @FXML
    private Button primaryButton;
    @FXML
    private Button secButton;

    @FXML
    private void switchToSecondary() throws IOException {
        System.out.println("pressed");
        primaryButton.setText("CAMBIATOOO");

        ControllerHandler.getInstance().getSecondaryController().activate();
        //primaryButton.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("secondary.fxml"))));
    }

    @FXML
    public void prova(){
        System.out.println("1");
        primaryButton.setText("prova");
    }

    @FXML
    public void prova2(){
        System.out.println("2");
        secButton.setText("prova2");
    }

}
