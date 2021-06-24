package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.data.CardLeaderData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.serverMessage.AnyResponse;
import it.polimi.ingsw.message.serverMessage.LeaderManage;
import it.polimi.ingsw.model.resource.ResourceType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;


public class PreGameSelectionController extends Controller {
    @FXML private AnchorPane background;
    @FXML private ImageView musicImage;
    @FXML private AnchorPane customMessageBox;
    @FXML private AnchorPane chooseResourcesBox;
    @FXML private Label howManyResLabel;
    @FXML private ImageView coin;
    @FXML private Button decreaseCoin;
    @FXML private Label coinNum;
    @FXML private ImageView shield;
    @FXML private Button decreaseShield;
    @FXML private Label shieldNum;
    @FXML private ImageView servant;
    @FXML private Button decreaseServant;
    @FXML private Label servantNum;
    @FXML private ImageView stone;
    @FXML private Button decreaseStone;
    @FXML private Label stoneNum;
    @FXML private Button sendResBtn;
    @FXML private AnchorPane leaderBox;
    @FXML private ImageView leader1;
    @FXML private ImageView leader2;
    @FXML private ImageView leader3;
    @FXML private ImageView leader4;
    @FXML private Button btn1;
    @FXML private Button btn2;
    @FXML private Button btn3;
    @FXML private Button btn4;
    @FXML private Slider musicVolume;

    private final Client client = Client.getInstance();
    private final ArrayList<ImageView> leaders = new ArrayList<>();
    private final HashMap<Button, ImageView> leaderButtonMap = new HashMap<>();
    private final ArrayList<Button> decreaseResourcesBtn = new ArrayList<>();
    private final ArrayList<Label> numResourcesLabel = new ArrayList<>();
    private int howManyRes = 0;
    private boolean isSelectionOver = false;
    private final HashMap<ResourceType, Integer> resNumMap = new HashMap<>() {{
        put(ResourceType.COIN,0);
        put(ResourceType.SERVANT, 0);
        put(ResourceType.SHIELD,0);
        put(ResourceType.STONE, 0);
    }};

    /**
     * Method that prepare the scene
     */
    @FXML
    public void initialize(){
        leaderButtonMap.put(btn1, leader1);
        leaders.add(leader1);

        leaderButtonMap.put(btn2, leader2);
        leaders.add(leader2);

        leaderButtonMap.put(btn3, leader3);
        leaders.add(leader3);

        leaderButtonMap.put(btn4, leader4);
        leaders.add(leader4);

        decreaseResourcesBtn.add(decreaseStone);
        decreaseResourcesBtn.add(decreaseCoin);
        decreaseResourcesBtn.add(decreaseServant);
        decreaseResourcesBtn.add(decreaseShield);

        numResourcesLabel.add(stoneNum);
        numResourcesLabel.add(coinNum);
        numResourcesLabel.add(servantNum);
        numResourcesLabel.add(shieldNum);

        showLeaderBox();

        musicVolume.valueProperty().addListener((ov, old_val, new_val) -> {
            double volume = new_val.doubleValue()/100;
            ControllerHandler.getInstance().setVolume(volume);});

        setUpCustomMessageBox(customMessageBox);
    }

    /**
     * See {@link Controller#setUpAll()} ()}
     */
    @Override
    public void setUpAll() {
        ControllerHandler.getInstance().setMusicImage(musicImage);
        showLeaderBox();
        musicVolume.setValue(ControllerHandler.getInstance().getVolume()*100);
        setUpBackground(background);
    }

    /**
     * See {@link Controller#showCustomMessage(String)}
     * @param msg the message to be displayed
     */
    @Override
    public void showCustomMessage(String msg) {
        Label label = (Label) customMessageBox.getChildren().get(0);
        TextField tf = new TextField(msg);

        label.textProperty().bind(tf.textProperty());

        customMessageBox.setVisible(true);
        showFadedErrorMessage(customMessageBox);
    }


    /**
     * Method attache to the "play" button, it sends the resources and wait the match
     * to start
     */
    @FXML
    public void sendResources(){
        ArrayList<ResourceData> resources = resNumMap.keySet()
                .stream().filter(x -> resNumMap.get(x) != 0)
                .map(x -> new ResourceData(x, resNumMap.get(x)))
                .collect(Collectors.toCollection(ArrayList::new));

        WaitingController controller = (WaitingController) ControllerHandler.getInstance().getController(Views.WAITING);
        controller.setPreMatchMessage();
        ControllerHandler.getInstance().changeView(Views.WAITING);

        client.writeToStream(new AnyResponse(resources));

    }

    /**
     * Method that handle the deselection of a resource
     * @param event the event to be handled
     */
    @FXML
    public void decreaseRes(ActionEvent event){
        ResourceType res;
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

    /**
     * Method that handle the resource selection
     * @param event the event to be handled
     */
    @FXML
    public void increaseRes(MouseEvent event){
        if (isSelectionOver) return;
        ResourceType res;
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

    /**
     * Method that handle the leader discard
     * @param event the event to be handled
     */
    @FXML
    public void discardLeader(ActionEvent event){
        Button btn = (Button) event.getSource();
        ImageView leaderSelected = leaderButtonMap.get(btn);
        leaderSelected.getParent().setVisible(false);
        int index = leaders.indexOf(leaderSelected);

        for (int i = 0; i < index; i++) {
            if (!leaders.get(i).getParent().isVisible())
                index--;
        }

        Client.getInstance().writeToStream(new LeaderManage(index, true));
    }


    /**
     * Method that setup the leaders images
     * @param cards the leaders to be displayed
     */
    public void setUpLeaderImages(ArrayList<CardLeaderData> cards){
        for (int i = 0; i < leaders.size(); i++){
            leaders.get(i).setImage(new Image(cards.get(i).toResourcePath(true)));
        }
    }


    /**
     * Method attached to the sound image, it stops the music if it is playing, otherwise it will start it
     */
    @FXML
    public void changeMusic(){
        ControllerHandler.getInstance().changeMusic();
        ControllerHandler.getInstance().setMusicImage(musicImage);
    }


    /**
     * Method that makes visible the box that permit the resource choosing
     */
    public void showChooseResourcesBox(){
        reset();
        howManyResLabel.setText("You have "+ howManyRes + " to choose");
        numResourcesLabel.forEach(x -> x.setText(Integer.toString(0)));
        decreaseResourcesBtn.forEach(x -> x.setVisible(false));
        chooseResourcesBox.setVisible(true);
    }

    /**
     * Method that makes visible the box that permit the leader choosing
     */
    public void showLeaderBox(){
        reset();
        leaders.forEach(x -> x.getParent().setVisible(true));
        leaderBox.setVisible(true);
    }

    /**
     * Setters for the amount of resources to choose
     * @param howManyRes the number of resources to choose
     */
    public void setHowManyRes(int howManyRes) {
        this.howManyRes = howManyRes;
    }

    /**
     * Method that resets the panes visibility
     */
    private void reset(){
        customMessageBox.setVisible(false);
        leaderBox.setVisible(false);
        chooseResourcesBox.setVisible(false);
        sendResBtn.setVisible(false);
    }
}