package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.command.NumOfPlayerCMD;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;


public class SecondaryController extends Controller{
    @FXML
    Button sendButton;
    @FXML
    TextField textField;
    @FXML
    javafx.scene.control.Label label;

    @FXML
    private void send(){
        new NumOfPlayerCMD(textField.getText(), Client.getInstance(new String[0]));
    }

    @FXML
    public void numOfPlayerRequest(){
        label.setText("Insert the number of players");
    }

}
