package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.data.CardLeaderData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.serverMessage.AnyResponse;
import it.polimi.ingsw.message.serverMessage.LeaderManage;
import it.polimi.ingsw.model.resource.ResourceType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;


public class PreGameSelectionController extends Controller {
    private final Client client = Client.getInstance();

    private int howManyRes = 0;
    private boolean isSelectionOver = false;
    private HashMap<ResourceType, Integer> resNumMap = new HashMap<>();

    @FXML AnchorPane customMessageBox;
    @FXML Label customMessageLabel;

    //resource box
    @FXML AnchorPane chooseResourcesBox;
    @FXML Label howManyResLabel;
    @FXML ImageView coin;
    @FXML ImageView coin1;
    @FXML Button decreaseCoin;
    @FXML Label coinNum;

    @FXML ImageView shield;
    @FXML ImageView shield1;
    @FXML Button decreaseShield;
    @FXML Label shieldNum;

    @FXML ImageView servant;
    @FXML ImageView servant1;
    @FXML Button decreaseServant;
    @FXML Label servantNum;

    @FXML ImageView stone;
    @FXML ImageView stone1;
    @FXML Button decreaseStone;
    @FXML Label stoneNum;

    @FXML Button sendResBtn;

    //leader box
    @FXML AnchorPane leaderBox;
    @FXML ImageView leader1;
    @FXML ImageView leader2;
    @FXML ImageView leader3;
    @FXML ImageView leader4;

    @FXML AnchorPane box1;
    @FXML AnchorPane box2;
    @FXML AnchorPane box3;
    @FXML AnchorPane box4;

    @FXML Button btn1;
    @FXML Button btn2;
    @FXML Button btn3;
    @FXML Button btn4;


    @Override
    public void showCustomMessage(String msg) {
        Label label = (Label) customMessageBox.getChildren().get(0);
        label.setText(msg);
        customMessageBox.setVisible(true);
        showFadedErrorMessage(customMessageBox);
    }



    @FXML
    public void sendResources(){
        ArrayList<ResourceData> resources = resNumMap.keySet()
                .stream().filter(x -> resNumMap.get(x) != 0)
                .map(x -> new ResourceData(x, resNumMap.get(x)))
                .collect(Collectors.toCollection(ArrayList::new));
        client.writeToStream(new AnyResponse(resources));
    }
    @FXML
    public void decreaseRes(ActionEvent event){
        ResourceType res = null;
        int oldValue;
        if (event.getSource().equals(decreaseCoin)){
            res = ResourceType.COIN;
            oldValue = resNumMap.get(res);
            resNumMap.put(res, oldValue - 1);
            coinNum.setText(Integer.toString(oldValue -1));
            if (oldValue == 1){
                decreaseCoin.setVisible(false);
            }
        }else if(event.getSource().equals(decreaseShield)){
            res = ResourceType.SHIELD;
            oldValue = resNumMap.get(res);
            resNumMap.put(res, oldValue - 1);
            shieldNum.setText(Integer.toString(oldValue -1));
            if (oldValue == 1){
                decreaseShield.setVisible(false);
            }
        }else if(event.getSource().equals(decreaseServant)){
            res = ResourceType.SERVANT;
            oldValue = resNumMap.get(res);
            resNumMap.put(res, oldValue - 1);
            servantNum.setText(Integer.toString(oldValue -1));
            if (oldValue == 1){
                decreaseServant.setVisible(false);
            }
        }else if(event.getSource().equals(decreaseStone)){
            res = ResourceType.STONE;
            oldValue = resNumMap.get(res);
            resNumMap.put(res, oldValue - 1);
            stoneNum.setText(Integer.toString(oldValue -1));
            if (oldValue == 1){
                decreaseStone.setVisible(false);
            }
        }
        if (isSelectionOver) {
            isSelectionOver = false;
            sendResBtn.setVisible(false);
        }
    }

    @FXML
    public void increaseRes(MouseEvent event){
        if (isSelectionOver) return;
        ResourceType res = null;
        int oldValue;
        if (event.getSource().equals(coin)){
            res = ResourceType.COIN;
            oldValue = resNumMap.get(res);
            resNumMap.put(res, oldValue + 1);
            coinNum.setText(Integer.toString(oldValue + 1));
            if (oldValue == 0){
                decreaseCoin.setVisible(true);
            }
        }else if(event.getSource().equals(shield)){
            res = ResourceType.SHIELD;
            oldValue = resNumMap.get(res);
            resNumMap.put(res, oldValue + 1);
            shieldNum.setText(Integer.toString(oldValue + 1));
            if (oldValue == 0){
                decreaseShield.setVisible(true);
            }
        }else if(event.getSource().equals(servant)){
            res = ResourceType.SERVANT;
            oldValue = resNumMap.get(res);
            resNumMap.put(res, oldValue + 1);
            servantNum.setText(Integer.toString(oldValue + 1));
            if (oldValue == 0){
                decreaseServant.setVisible(true);
            }
        }else if(event.getSource().equals(stone)){
            res = ResourceType.STONE;
            oldValue = resNumMap.get(res);
            resNumMap.put(res, oldValue + 1);
            stoneNum.setText(Integer.toString(oldValue + 1));
            if (oldValue == 0){
                decreaseStone.setVisible(true);
            }
        }else{
            return;
        }

        if (resNumMap.values().stream().mapToInt(x -> x).sum() == howManyRes){
            isSelectionOver = true;
            sendResBtn.setVisible(true);
        }

    }

    @FXML
    public void discardLeader(ActionEvent event){
        if (event.getSource().equals(btn1)){
            client.writeToStream(new LeaderManage(0,true));
            box1.setVisible(false);
        }else if (event.getSource().equals(btn2)){
            client.writeToStream(new LeaderManage(1,true));
            box2.setVisible(false);
        }else if (event.getSource().equals(btn3)){
            client.writeToStream(new LeaderManage(2,true));
            box3.setVisible(false);
        }else if (event.getSource().equals(btn4)){
            client.writeToStream(new LeaderManage(3,true));
            box4.setVisible(false);
        }else{
            showCustomMessage("Invalid selection");
            return;
        }
    }

    //---------------------
    //EXTERNAL METHODS
    //--------------------
    public void setUpLeader(ArrayList<CardLeaderData> cards){
        leader1.setImage(new Image(cards.get(0).toResourcePath()));

        leader2.setImage(new Image(cards.get(1).toResourcePath()));

        leader3.setImage(new Image(cards.get(2).toResourcePath()));

        leader4.setImage(new Image(cards.get(3).toResourcePath()));



    }

    @Override
    public void setUpAll() {
        Arrays.stream(ResourceType.values())
                .forEach(x -> {
                    if (x != ResourceType.FAITH && x != ResourceType.ANY){
                        resNumMap.put(x, 0);
                    }
                });

        showLeaderBox();
        sendResBtn.setVisible(false);

    }

    public void showChooseResourcesBox(){
        howManyResLabel.setText("You have "+ howManyRes + " to choose");
        leaderBox.setVisible(false);
        chooseResourcesBox.setVisible(true);
        customMessageBox.setVisible(false);
    }
    public void showLeaderBox(){
        leaderBox.setVisible(true);
        chooseResourcesBox.setVisible(false);
    }

    public void setHowManyRes(int howManyRes) {
        this.howManyRes = howManyRes;
    }
}