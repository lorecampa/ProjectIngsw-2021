package it.polimi.ingsw.client.GUI;

import javafx.fxml.FXML;

import java.io.IOException;

public class SecondaryController extends Controller{
    @FXML
    private void switchToPrimary() throws IOException {
        ControllerHandler.getInstance().getPrimaryController().activate();
    }
}
