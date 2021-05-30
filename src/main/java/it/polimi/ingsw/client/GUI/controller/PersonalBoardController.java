package it.polimi.ingsw.client.GUI.controller;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.ModelClient;
import it.polimi.ingsw.client.data.*;
import it.polimi.ingsw.message.serverMessage.LeaderManage;
import it.polimi.ingsw.model.resource.ResourceType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.*;
import java.util.stream.Collectors;

public class PersonalBoardController extends Controller{
    @FXML private ImageView pos0;
    @FXML private ImageView pos5;
    @FXML private ImageView pos4;
    @FXML private ImageView pos3;
    @FXML private ImageView pos2;
    @FXML private ImageView pos1;
    @FXML private ImageView pos12;
    @FXML private ImageView pos11;
    @FXML private ImageView pos10;
    @FXML private ImageView pos9;
    @FXML private ImageView pos8;
    @FXML private ImageView pos7;
    @FXML private ImageView pos6;
    @FXML private ImageView pos19;
    @FXML private ImageView pos18;
    @FXML private ImageView pos17;
    @FXML private ImageView pos16;
    @FXML private ImageView pos15;
    @FXML private ImageView pos14;
    @FXML private ImageView pos13;
    @FXML private ImageView pos22;
    @FXML private ImageView pos21;
    @FXML private ImageView pos20;
    @FXML private ImageView pos23;
    @FXML private ImageView pos24;
    @FXML private ImageView popeFavor1_Discard;
    @FXML private ImageView popeFavor1_Acquired;
    @FXML private ImageView popeFavor2_Discard;
    @FXML private ImageView popeFavor2_Acquired;
    @FXML private ImageView popeFavor3_Discard;
    @FXML private ImageView popeFavor3_Acquired;
    @FXML private ImageView dep33;
    @FXML private ImageView dep32;
    @FXML private ImageView dep31;
    @FXML private ImageView dep22;
    @FXML private ImageView dep21;
    @FXML private ImageView dep11;
    @FXML private Text strong_coin;
    @FXML private Text strong_serv;
    @FXML private Text strong_shield;
    @FXML private Text strong_stone;
    @FXML private ImageView card11;
    @FXML private ImageView card12;
    @FXML private ImageView card13;
    @FXML private ImageView card21;
    @FXML private ImageView card22;
    @FXML private ImageView card23;
    @FXML private ImageView card31;
    @FXML private ImageView card32;
    @FXML private ImageView card33;
    @FXML private ImageView baseProd;
    @FXML private Button btn_back;
    @FXML private Button btn_market;
    @FXML private Button btn_prod;
    @FXML private Button btn_players;
    @FXML private Button btn_deck;
    @FXML private Button btn_discard1;
    @FXML private Button btn_discard2;
    @FXML private ImageView prodLeader1;
    @FXML private ImageView prodLeader2;
    @FXML private ImageView leader1;
    @FXML private ImageView leader2;
    @FXML private ImageView le_depot_11;
    @FXML private ImageView le_depot_12;
    @FXML private ImageView le_depot_21;
    @FXML private ImageView le_depot_22;
    @FXML private ChoiceBox<String> choice_username;


    private String currentShowed;
    private final ArrayList<ImageView> track = new ArrayList<>();
    private final ArrayList<ImageView> popeFavorsDiscard = new ArrayList<>();
    private final ArrayList<ImageView> popeFavorsAcquired = new ArrayList<>();
    private final ArrayList<ArrayList<ImageView>> depots = new ArrayList<>();
    private final Map<ResourceType,Text> strongBox = new HashMap<>();
    private final ArrayList<ArrayList<ImageView>> cardSlots = new ArrayList<>();
    private final ArrayList<ImageView> leaders = new ArrayList<>();
    private final ArrayList<ImageView> leadersDepots = new ArrayList<>();
    private final ArrayList<ImageView> leadersProd = new ArrayList<>();

    @FXML
    public void initialize(){
        btn_back.setVisible(false);
        btn_back.setDisable(true);
        setUpTrack();
        setUpPopeFavor();
        setUpDepots();
        setUpStrongbox();
        setUpCardSlots();
        setUpLeaders();
        setUpLeadersProd();
    }

    public String getCurrentShowed() {
        return currentShowed;
    }

    //------------------------
    // SET UP VARIABLES
    //------------------------
    private void setUpTrack(){
        track.addAll(Arrays.asList(pos0,pos1,pos2,pos3,pos4,pos5,pos6,pos7,pos8,pos9,pos10,pos11,pos12,pos13,pos14,
                pos15,pos16,pos17,pos18,pos19,pos20,pos21,pos22,pos23,pos24));
        track.forEach(image -> image.setVisible(false));
    }

    private void setUpLeaders(){
        leaders.add(leader1);
        leaders.add(leader2);

        leadersDepots.add(le_depot_11);
        leadersDepots.add(le_depot_12);
        leadersDepots.add(le_depot_21);
        leadersDepots.add(le_depot_22);

        leadersDepots.forEach(imageView -> imageView.setVisible(false));

    }

    private void setUpLeadersProd(){
        leadersProd.add(prodLeader1);
        leadersProd.add(prodLeader2);

        prodLeader1.setVisible(false);
        prodLeader2.setVisible(false);
    }

    private void setUpDepots(){
        ArrayList<ImageView> row0 = new ArrayList<>();
        row0.add(dep11);
        row0.forEach(imageView -> imageView.setVisible(false));
        ArrayList<ImageView> row1 = new ArrayList<>();
        row1.add(dep21);
        row1.add(dep22);
        row1.forEach(imageView -> imageView.setVisible(false));
        ArrayList<ImageView> row2 = new ArrayList<>();
        row2.add(dep31);
        row2.add(dep32);
        row2.add(dep33);
        row2.forEach(imageView -> imageView.setVisible(false));

        depots.add(row0);
        depots.add(row1);
        depots.add(row2);

    }

    private void setUpStrongbox(){
        strongBox.put(ResourceType.COIN,strong_coin);
        strongBox.put(ResourceType.SERVANT,strong_serv);
        strongBox.put(ResourceType.SHIELD,strong_shield);
        strongBox.put(ResourceType.STONE,strong_stone);
    }

    private void setUpCardSlots(){
        ArrayList<ImageView> slot1 = new ArrayList<>();
        slot1.add(card11);
        slot1.add(card12);
        slot1.add(card13);
        slot1.forEach(imageView -> imageView.setVisible(false));
        ArrayList<ImageView> slot2 = new ArrayList<>();
        slot2.add(card21);
        slot2.add(card22);
        slot2.add(card23);
        slot2.forEach(imageView -> imageView.setVisible(false));
        ArrayList<ImageView> slot3 = new ArrayList<>();
        slot3.add(card31);
        slot3.add(card32);
        slot3.add(card33);
        slot3.forEach(imageView -> imageView.setVisible(false));

        cardSlots.add(slot1);
        cardSlots.add(slot2);
        cardSlots.add(slot3);
    }

    public void setUpPopeFavor(){
        popeFavorsDiscard.add(popeFavor1_Discard);
        popeFavorsDiscard.add(popeFavor2_Discard);
        popeFavorsDiscard.add(popeFavor3_Discard);

        popeFavorsAcquired.add(popeFavor1_Acquired);
        popeFavorsAcquired.add(popeFavor2_Acquired);
        popeFavorsAcquired.add(popeFavor3_Acquired);

        popeFavorsAcquired.forEach(image -> image.setVisible(false));
    }

    //-------------------------------
    // LOADING FROM MODEL PART TO GUI
    //-------------------------------
    public void loadFaithTrack(ModelData model){
        //FAITH TRACK
        ArrayList<FaithTrackData> faithTrackData = model.getFaithTrack();
        track.get(model.getCurrentPosOnFaithTrack()).setVisible(true);
        ArrayList<FaithTrackData> popeFavor = faithTrackData.stream().filter(FaithTrackData::isPopeFavor).collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < popeFavor.size(); i++) {
            if (popeFavor.get(i).isAcquired()){
                popeFavorsAcquired.get(i).setVisible(true);
                popeFavorsDiscard.get(i).setVisible(false);
            }
        }
    }

    private void loadDepots(ModelData model){
        //DEPOTS
        ArrayList<ResourceData> standardDepots = model.getStandardDepot();
        for (int i = 0; i < standardDepots.size(); i++) {
            for (int j = 0; j < standardDepots.get(i).getValue(); j++) {
                depots.get(i).get(j).setImage(new Image(standardDepots.get(i).toResourceImage()));
                depots.get(i).get(j).setVisible(true);
            }
        }
        //LEADER DEPOTS
        ArrayList<ResourceData> le_depots = model.getLeaderDepot();
        ArrayList<CardLeaderData> leadersData = model.getLeaders();
        switch (le_depots.size()){
            case 1:
                boolean isWarehouse = leadersData.get(0).getEffects().stream().anyMatch(effectData -> effectData.getType().equals(EffectType.WAREHOUSE));
                if (isWarehouse) {
                    for (int i = 0; i < le_depots.get(0).getValue(); i++) {
                        leadersDepots.get(i).setImage(new Image(le_depots.get(0).toResourceImage()));
                    }
                } else {
                    for (int i = 0; i < le_depots.get(0).getValue(); i++) {
                        leadersDepots.get(i+2).setImage(new Image(le_depots.get(0).toResourceImage()));
                    }
                }
                break;
            case 2:
                for (int i = 0; i < le_depots.get(0).getValue(); i++) {
                    leadersDepots.get(i).setImage(new Image(le_depots.get(0).toResourceImage()));
                }
                for (int i = 0; i < le_depots.get(1).getValue(); i++) {
                    leadersDepots.get(i+2).setImage(new Image(le_depots.get(1).toResourceImage()));
                }
                break;
        }
    }

    public void loadLeader(ModelData model){
        //LEADERS
        ArrayList<CardLeaderData> leadersData = model.getLeaders();
        boolean owned = Client.getInstance().getMyName().equals(model.getUsername());
        for (int i = 0; i < leadersData.size() && i<2; i++) {
            CardLeaderData leaderData = leadersData.get(i);
            leaders.get(i).setImage(new Image(leaderData.toResourcePath()));
            switch (i){
                case 0:
                    btn_discard1.setDisable(!owned);
                    btn_discard1.setVisible(owned);
                    leaders.get(i).setDisable(!owned);
                    break;
                case 1:
                    btn_discard2.setDisable(!owned);
                    btn_discard2.setVisible(owned);
                    leaders.get(i).setDisable(!owned);
                    break;
            }
        }
    }

    private void loadStrongBox(ModelData model){
        //STRONGBOX
        ArrayList<ResourceData> strongBoxData = model.getStrongbox();
        for (ResourceData resourceData : strongBoxData){
            strongBox.get(resourceData.getType()).setText(String.valueOf(resourceData.getValue()));
        }
    }

    private void loadCardSlots(ModelData model){
        //CARD SLOTS
        ArrayList<ArrayList<CardDevData>>cardSlotsData = model.getCardSlots();
        for (int i = 0; i < cardSlotsData.size(); i++) {
            for (int j = 0; j < cardSlotsData.get(i).size(); j++) {
                cardSlots.get(i).get(j).setImage(new Image(cardSlotsData.get(i).get(j).toResourcePath()));
                cardSlots.get(i).get(j).setVisible(true);
            }
        }
    }

    private void loadChoiceBox(){
        choice_username.getItems().clear();
        ArrayList<String> usernames = Client.getInstance().getModels()
                .stream().map(ModelClient::getUsername).filter(s -> !s.equals(Client.getInstance().getMyName()))
                .collect(Collectors.toCollection(ArrayList::new));
        choice_username.getItems().addAll(usernames);
    }

    //----------------
    // UTILITIES
    //----------------
    private void setDisableBoardForOther(boolean disable){
        btn_prod.setDisable(disable);
        btn_prod.setVisible(!disable);
        btn_market.setDisable(disable);
        btn_market.setVisible(!disable);
        btn_deck.setDisable(disable);
        btn_deck.setVisible(!disable);
    }

    private void setDisableBoardForProd(boolean disable){
        setDisableLeaderBtn(disable);
        btn_market.setDisable(disable);
        btn_market.setVisible(!disable);
        btn_deck.setDisable(disable);
        btn_deck.setVisible(!disable);
    }

    public void setDisableLeaderBtn(boolean disable){
        leader1.setDisable(disable);
        leader2.setDisable(disable);

        prodLeader1.setVisible(false);
        prodLeader2.setVisible(false);

        btn_discard1.setDisable(disable);
        btn_discard1.setVisible(!disable);

        btn_discard2.setDisable(disable);
        btn_discard2.setVisible(!disable);
    }

    private void resetBoard(){
        setDisableLeaderBtn(true);
        resetFaithTrack();
        resetDepots();
        resetLeader();
        resetCardSlots();
    }

    private void resetCardSlots() {
        cardSlots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setVisible(false)));
        cardSlots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setDisable(true)));
        baseProd.setDisable(true);
    }

    public void resetLeader() {
        leaders.forEach(imageView -> imageView.setImage(new Image("/GUI/back/leader_back.png")));
    }

    private void resetDepots() {
        depots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setVisible(false)));
        leadersDepots.forEach(imageView -> imageView.setVisible(false));
    }

    public void resetFaithTrack() {
        track.forEach(imageView -> imageView.setVisible(false));
        popeFavorsAcquired.forEach(imageView -> imageView.setVisible(false));
        popeFavorsDiscard.forEach(imageView -> imageView.setVisible(true));
    }

    //-------------------------
    // LOAD WHOLE MODEL TO GUI
    //-------------------------

    @Override
    public void setUpAll(){
        resetBoard();
        setDisableBoardForOther(false);

        ModelData model = Client.getInstance().getMyModel().toModelData();
        loadBoard(model);

        btn_back.setDisable(true);
        btn_back.setVisible(false);

        currentShowed = Client.getInstance().getMyName();
    }

    public void setUpOtherPlayer(String username){
        resetBoard();
        setDisableBoardForOther(true);

        ModelData model = Client.getInstance().getModelOf(username).toModelData();
        loadBoard(model);

        btn_back.setDisable(false);
        btn_back.setVisible(true);

        currentShowed = username;
    }

    private void loadBoard(ModelData model){
        loadFaithTrack(model);
        loadDepots(model);
        loadLeader(model);
        loadStrongBox(model);
        loadCardSlots(model);
        loadChoiceBox();
    }

    //-----------------
    // GUI ACTIONS
    //-----------------

    @FXML
    public void marketButton(){
        ControllerHandler.getInstance().changeView(Views.MARKET);
    }

    @FXML
    public void leaderClicked(MouseEvent event){
        if (event.getSource().equals(leader1))
            Client.getInstance().writeToStream(new LeaderManage(0, false));
        else
            Client.getInstance().writeToStream(new LeaderManage(1, false));
    }

    @FXML
    public void leaderProdClicked(MouseEvent actionEvent){
        if (actionEvent.getSource().equals(prodLeader1)) {
            System.out.println("leader 1 prod");//Client.getInstance().writeToStream(new ProductionAction(0, true));
        }
        else
            System.out.println("leader 2 prod");//Client.getInstance().writeToStream(new ProductionAction(1, true));
    }

    @FXML
    public void leaderDiscard(ActionEvent actionEvent){
        if (actionEvent.getSource().equals(btn_discard1))
            Client.getInstance().writeToStream(new LeaderManage(0, true));
        else
            Client.getInstance().writeToStream(new LeaderManage(1, true));
    }

    @FXML
    public void otherPlayerClicker(){
        if (Client.getInstance().existAModelOf(choice_username.getValue()))
            setUpOtherPlayer(choice_username.getValue());
    }

    @FXML
    public void cardProdClicked(MouseEvent event){
        if (event.getSource().equals(baseProd))
            System.out.println("base prod");//Client.getInstance().writeToStream(new BaseProduction());
        else{
            for (int i = 0; i < cardSlots.size(); i++) {
                if(cardSlots.get(i).stream().anyMatch(imageView -> imageView.equals(event.getSource())))
                    System.out.println("card slot " + i);//Client.getInstance().writeToStream(new ProductionAction(i,false));
            }
        }
    }

    @FXML
    public void activateProduction(){
        setDisableBoardForProd(true);
        ArrayList<CardLeaderData> leadersData = Client.getInstance().getMyModel().toModelData().getLeaders();
        for (int i = 0; i < leadersData.size(); i++) {
            if (leadersData.get(i).getEffects().stream().anyMatch(effectData -> effectData.getType().equals(EffectType.PRODUCTION)))
                leadersProd.get(i).setVisible(true);
        }
        baseProd.setDisable(false);
        ArrayList<ArrayList<CardDevData>> cardSlotsData = Client.getInstance().getMyModel().toModelData().getCardSlots();
        for (int i = 0; i < cardSlotsData.size(); i++) {
            cardSlots.get(i).get(cardSlotsData.get(i).size()-1).setDisable(false);
        }

    }

    @FXML
    public void back(){
        setUpAll();

    }

    public void showDeckDev(){
        Platform.runLater(()-> ControllerHandler.getInstance().changeView(Views.DECK_DEV));
    }
}