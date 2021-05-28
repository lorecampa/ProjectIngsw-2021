package it.polimi.ingsw.client.GUI.controller;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.data.CardLeaderData;
import it.polimi.ingsw.message.serverMessage.LeaderManage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LeaderController extends Controller{

    @FXML private ImageView leader;
    @FXML private Button btn_activate;
    @FXML private Button btn_discard;
    @FXML private Button btn_back;

    private int leaderIndex;

    @FXML
    public void leaderActivate(){
        Client.getInstance().writeToStream(new LeaderManage(leaderIndex, false));
    }

    @FXML
    public void leaderDiscard(){
        Client.getInstance().writeToStream(new LeaderManage(leaderIndex, true));
    }

    @FXML
    public void back(){
        ControllerHandler.getInstance().changeView(Views.PERSONAL_BOARD);
    }

    public void setLeaderIndex(int leaderIndex) {
        this.leaderIndex = leaderIndex;
    }

    @Override
    public void setUpAll() {
        CardLeaderData leaderData = Client.getInstance().getMyModel().toModelData().getLeaders().get(leaderIndex);
        leader.setImage(new Image(leaderData.toResourcePath()));
        if (leaderData.isActive())
            btn_activate.setDisable(true);
    }

    @Override
    public void showErrorMessage(String msg) {

    }
}
