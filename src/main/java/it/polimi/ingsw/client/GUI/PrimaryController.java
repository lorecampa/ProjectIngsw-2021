package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.command.MainMenuCMD;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class PrimaryController extends Controller{
    @FXML
    private Button primaryButton;
    @FXML
    private Button secButton;

    private final Client client = Client.getInstance(new String[0]);
    @FXML
    private void switchToSecondary() throws IOException {
        new MainMenuCMD("1", client).doCommand();
        ControllerHandler.getInstance().getSecondaryController().activate();
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
