package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.serverMessage.LeaderManage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;


public class PreGameSelectionController extends Controller {
    private final Client client = Client.getInstance();
    private int leaderDiscarded = 0;
    private int howManyRes = 0;
    private ArrayList<ResourceData> currResSel = new ArrayList<>();

    @FXML AnchorPane customMessageBox;
    @FXML Label customMessageLabel;


    @FXML AnchorPane chooseResourcesBox;
    @FXML
    Label howManyResLabel;

    @FXML ImageView leader1;
    @FXML ImageView leader2;
    @FXML ImageView leader3;
    @FXML ImageView leader4;

    @FXML Button btn1;
    @FXML Button btn2;
    @FXML Button btn3;
    @FXML Button btn4;


    @FXML AnchorPane box1;
    @FXML AnchorPane box2;
    @FXML AnchorPane box3;
    @FXML AnchorPane box4;

    @Override
    public void showErrorMessage(String msg) {
        howManyResLabel.setText(msg);
        customMessageBox.setVisible(true);
    }

    @FXML
    public void sendResources(ActionEvent event){
        System.out.println(event.getSource());

    }

    @FXML
    public void discardLeader(ActionEvent event){
        if (event.getSource().equals(btn1)){
            client.writeToStream(new LeaderManage(1,true));
            box1.setVisible(false);
        }else if (event.getSource().equals(btn2)){
            client.writeToStream(new LeaderManage(2,true));
            box2.setVisible(false);
        }else if (event.getSource().equals(btn3)){
            client.writeToStream(new LeaderManage(3,true));
            box3.setVisible(false);
        }else if (event.getSource().equals(btn4)){
            client.writeToStream(new LeaderManage(4,true));
            box4.setVisible(false);
        }else{
            showErrorMessage("Invalid selection");
        }

    }

    //---------------------
    //EXTERNAL METHODS
    //--------------------
    public void setLeaderImages(ArrayList<String> paths){

        leader1.setImage(new Image(paths.get(0)));
        leader2.setImage(new Image(paths.get(1)));
        leader3.setImage(new Image(paths.get(2)));
        leader4.setImage(new Image(paths.get(3)));



    }

    @Override
    public void setUpAll() {

    }

    public void showChooseResourcesBox(){
        box1.setVisible(false);
        box2.setVisible(false);
        box3.setVisible(false);
        box4.setVisible(false);

        chooseResourcesBox.setVisible(true);
    }

    public void setHowManyRes(int howManyRes) {
        this.howManyRes = howManyRes;
    }
}
