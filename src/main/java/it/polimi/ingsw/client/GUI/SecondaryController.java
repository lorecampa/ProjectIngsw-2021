package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ModelClient;
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
    Label label;


    @FXML
    private void send(){
        new NumOfPlayerCMD(textField.getText(), Client.getInstance());
    }

    @FXML
    public void numOfPlayerRequest(){
        ModelClient model = Client.getInstance().getMyModel();
        label.setText(model.getUsername());
    }

}
