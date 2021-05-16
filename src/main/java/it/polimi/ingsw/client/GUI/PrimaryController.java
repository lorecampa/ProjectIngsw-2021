package it.polimi.ingsw.client.GUI;

import javafx.fxml.FXML;

import java.io.IOException;

public class PrimaryController {
    @FXML
    private void switchToSecondary() throws IOException {
        System.out.println("pressed");
        ClientGUI.setRoot("secondary");
    }
}
