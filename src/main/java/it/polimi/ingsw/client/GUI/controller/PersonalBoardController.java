package it.polimi.ingsw.client.GUI.controller;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.ModelClient;
import it.polimi.ingsw.client.data.*;
import it.polimi.ingsw.message.serverMessage.*;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

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

    @FXML private ImageView imageCoinStrong;
    @FXML private ImageView imageServStrong;
    @FXML private ImageView imageStoneStrong;
    @FXML private ImageView imageShieldStrong;

    @FXML private Label strong_coin;
    @FXML private Label strong_serv;
    @FXML private Label strong_shield;
    @FXML private Label strong_stone;
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


    @FXML private AnchorPane customMessageBox;

    @FXML private AnchorPane bufferBox;

    @FXML private Pane cardSlot1;
    @FXML private Button selectSlot1Btn;
    @FXML private Pane cardSlot2;
    @FXML private Button selectSlot2Btn;
    @FXML private Pane cardSlot3;
    @FXML private Button selectSlot3Btn;

    @FXML private ImageView imageBuffer1;
    @FXML private Label labelBuffer1;
    @FXML private Label bufferTop1;
    @FXML private Button decreaseResBuffer1;

    @FXML private ImageView imageBuffer2;
    @FXML private Label labelBuffer2;
    @FXML private Label bufferTop2;
    @FXML private Button decreaseResBuffer2;


    @FXML private ImageView imageBuffer3;
    @FXML private Label labelBuffer3;
    @FXML private Label bufferTop3;
    @FXML private Button decreaseResBuffer3;


    @FXML private ImageView imageBuffer4;
    @FXML private Label labelBuffer4;
    @FXML private Label bufferTop4;
    @FXML private Button decreaseResBuffer4;



    //from market res
    private ImageView startImage;
    private ImageView destImage;


    private enum ProdState {NOT_IN_PROD,INITIAL,ALREADY_PROD;}
    private ProdState prodState;


    private String currentShowed;
    private final ArrayList<ImageView> track = new ArrayList<>();
    private final ArrayList<ImageView> popeFavorsDiscard = new ArrayList<>();
    private final ArrayList<ImageView> popeFavorsAcquired = new ArrayList<>();
    private final ArrayList<ArrayList<ImageView>> depots = new ArrayList<>();
    private final Map<ResourceType,Label> strongboxLabelMap = new HashMap<>();
    private final Map<ResourceType,ImageView> strongboxImageMap = new HashMap<>();
    private final ArrayList<ArrayList<ImageView>> cardSlots = new ArrayList<>();
    private final ArrayList<ImageView> leaders = new ArrayList<>();
    private final ArrayList<ArrayList<ImageView>> leadersDepots = new ArrayList<>();
    private int rowDevCard;
    private int colDevCard;
    private final ArrayList<ImageView> leadersProd = new ArrayList<>();
    private final ArrayList<Button> discardButton = new ArrayList<>();

    private final HashMap<ResourceType, ImageView> resourceBufferImages = new HashMap<>();
    private final HashMap<ResourceType, Label> resourceBufferLabelsMap = new HashMap<>();
    private final HashMap<ResourceType, Label> resourceBufferTopLabelsMap = new HashMap<>();
    private final HashMap<ResourceType, Button> resourceBufferDecreaseBtnMap = new HashMap<>();
    private int numOfAnyToConvert;
    private HashMap<ResourceType, Integer> anyConverted = new HashMap<>() {{
        put(ResourceType.COIN,0);
        put(ResourceType.SERVANT, 0);
        put(ResourceType.SHIELD,0);
        put(ResourceType.STONE, 0);
    }};


    @FXML
    public void initialize(){
        prodState = ProdState.NOT_IN_PROD;

        btn_back.setVisible(false);
        bufferBox.setVisible(false);

        cardSlot1.setDisable(true);
        cardSlot2.setDisable(true);
        cardSlot3.setDisable(true);

        setUpTrack();
        setUpPopeFavor();
        setUpDepots();
        setUpStrongbox();
        setUpCardSlots();
        setUpLeaders();
        setUpLeadersProd();
        setUpBuffer();
        setUpDiscard();
    }

    private void setUpBuffer(){
        resourceBufferImages.put(ResourceType.COIN, imageBuffer1);
        resourceBufferLabelsMap.put(ResourceType.COIN, labelBuffer1);
        resourceBufferTopLabelsMap.put(ResourceType.COIN, bufferTop1);
        resourceBufferDecreaseBtnMap.put(ResourceType.COIN, decreaseResBuffer1);

        resourceBufferImages.put(ResourceType.SHIELD, imageBuffer2);
        resourceBufferLabelsMap.put(ResourceType.SHIELD, labelBuffer2);
        resourceBufferTopLabelsMap.put(ResourceType.SHIELD, bufferTop2);
        resourceBufferDecreaseBtnMap.put(ResourceType.SHIELD, decreaseResBuffer2);


        resourceBufferImages.put(ResourceType.SERVANT, imageBuffer3);
        resourceBufferLabelsMap.put(ResourceType.SERVANT, labelBuffer3);
        resourceBufferTopLabelsMap.put(ResourceType.SERVANT, bufferTop3);
        resourceBufferDecreaseBtnMap.put(ResourceType.SERVANT, decreaseResBuffer3);


        resourceBufferImages.put(ResourceType.STONE, imageBuffer4);
        resourceBufferLabelsMap.put(ResourceType.STONE, labelBuffer4);
        resourceBufferTopLabelsMap.put(ResourceType.STONE, bufferTop4);
        resourceBufferDecreaseBtnMap.put(ResourceType.STONE, decreaseResBuffer4);

    }

    private void setUpDiscard(){
        discardButton.add(btn_discard1);
        discardButton.add(btn_discard2);
    }


    @Override
    public void showCustomMessage(String msg) {
        Label label = (Label) customMessageBox.getChildren().get(0);
        label.setText(msg);
        label.setTextFill(Paint.valueOf("Red"));
        customMessageBox.setVisible(true);
        showFadedErrorMessage(customMessageBox);
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

        ArrayList<ImageView> leaderDepot1 = new ArrayList<>();
        leaderDepot1.add(le_depot_11);
        leaderDepot1.add(le_depot_12);
        leadersDepots.add(leaderDepot1);
        ArrayList<ImageView> leaderDepot2 = new ArrayList<>();
        leaderDepot1.add(le_depot_21);
        leaderDepot1.add(le_depot_22);
        leadersDepots.add(leaderDepot2);

        leadersDepots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setVisible(false)));
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
        //row0.forEach(imageView -> imageView.setVisible(false));
        ArrayList<ImageView> row1 = new ArrayList<>();
        row1.add(dep21);
        row1.add(dep22);
        //row1.forEach(imageView -> imageView.setVisible(false));
        ArrayList<ImageView> row2 = new ArrayList<>();
        row2.add(dep31);
        row2.add(dep32);
        row2.add(dep33);
        //row2.forEach(imageView -> imageView.setVisible(false));

        depots.add(row0);
        depots.add(row1);
        depots.add(row2);

    }

    private void setUpStrongbox(){
        strongboxLabelMap.put(ResourceType.COIN,strong_coin);
        strongboxLabelMap.put(ResourceType.SERVANT,strong_serv);
        strongboxLabelMap.put(ResourceType.SHIELD,strong_shield);
        strongboxLabelMap.put(ResourceType.STONE,strong_stone);

        strongboxImageMap.put(ResourceType.COIN, imageCoinStrong);
        strongboxImageMap.put(ResourceType.SHIELD, imageShieldStrong);
        strongboxImageMap.put(ResourceType.SERVANT, imageServStrong);
        strongboxImageMap.put(ResourceType.STONE, imageStoneStrong);
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

    public void loadStandardDepots(ModelData model){
        //DEPOTS
        ArrayList<ResourceData> standardDepots = model.getStandardDepot();
        for (int i = 0; i < standardDepots.size(); i++) {
            updateDepot(i, standardDepots.get(i), true);
        }
    }

    public void loadLeaderDepots(ModelData model){
        //LEADER DEPOTS
        ArrayList<ResourceData> le_depots = model.getLeaderDepot();
        ArrayList<CardLeaderData> leadersData = model.getLeaders();

        for (int i = 0; i < le_depots.size(); i++) {
            boolean isWarehouse = leadersData.get(i).getEffects().stream().anyMatch(effectData -> effectData.getType().equals(EffectType.WAREHOUSE));
            if (isWarehouse){
                leadersDepots.get(i).forEach(imageView -> imageView.setVisible(true));
                for (int j = 0; j < le_depots.get(0).getValue(); j++) {
                    leadersDepots.get(i).get(j).setImage(new Image(le_depots.get(0).toResourceImage()));
                }
            }
        }
    }

    public void loadLeader(ModelData model){
        //LEADERS
        ArrayList<CardLeaderData> leadersData = model.getLeaders();
        boolean owned = Client.getInstance().getMyName().equals(model.getUsername());

        for (int i = 0; i < leadersData.size() && i<2; i++) {
            CardLeaderData leaderData = leadersData.get(i);
            leaders.get(i).setImage(new Image(leaderData.toResourcePath()));
            boolean activated = leaderData.isActive();

            if (activated)
                leaders.get(i).setDisable(true);
            else if (owned)
                leaders.get(i).setDisable(false);

            discardButton.get(i).setVisible(owned);
        }
    }

    public void loadStrongBox(ModelData model){
        //STRONGBOX
        ArrayList<ResourceData> strongBoxData = model.getStrongbox();
        for (ResourceData resourceData : strongBoxData){
            strongboxLabelMap.get(resourceData.getType()).setText(String.valueOf(resourceData.getValue()));
        }
    }

    public void loadCardSlots(ModelData model){
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
        btn_prod.setVisible(!disable);
        btn_market.setVisible(!disable);
        btn_deck.setVisible(!disable);
    }

    private void setDisableBoardForProd(boolean disable){
        btn_market.setVisible(!disable);
        btn_deck.setVisible(!disable);
        btn_players.setVisible(!disable);
        choice_username.setVisible(!disable);

        leaders.forEach(imageView -> imageView.setDisable(disable));
        discardButton.forEach(button -> button.setDisable(disable));
    }

    public void setBoardForBuffer(){
        depots.forEach(imageViews -> imageViews.stream()
                .filter(x -> x.getImage() != null).forEach(x -> x.setOnMouseClicked(this::depotClick)));

        leadersDepots.forEach(imageViews -> imageViews
                .forEach(imageView -> {
                    imageView.setOnMouseClicked(this::leaderDepotClick);
                }));

        strongboxImageMap.values().forEach(x -> x.setDisable(false));
    }

    public void leaderDepotClick(MouseEvent event){
        ImageView source = (ImageView) event.getSource();
        int index = getImageDepotIndex(leadersDepots, source);
        ResourceType type = Client.getInstance().getMyModel().toModelData().getLeaderDepot().get(index).getType();

        Client.getInstance().writeToStream(new DepotModify(index, new ResourceData(type, 1), false));
    }

    public void depotClick(MouseEvent event){
        ImageView source = (ImageView) event.getSource();
        int index = getImageDepotIndex(depots, source);
        ResourceType type = Client.getInstance().getMyModel().toModelData().getStandardDepot().get(index).getType();

        Client.getInstance().writeToStream(new DepotModify(index, new ResourceData(type, 1), true));
    }
    
    @FXML
    public void strongboxClick(MouseEvent event){
        ImageView source = (ImageView) event.getSource();
        ResourceType res = null;
        for (ResourceType resourceType: strongboxImageMap.keySet()){
            if (strongboxImageMap.get(resourceType).equals(source)){
                res = resourceType;
                break;
            }
        }

        Client.getInstance().writeToStream(new StrongboxModify(new ResourceData(res, 1)));
    }


    //-------------------------
    // RESET
    //---

    private void resetBoard(){
        resetFaithTrack();
        resetStandardDepots();
        resetLeaderDepots();
        resetLeader();
        resetCardSlots();
    }

    public void resetBuffer(){

    }

    public void resetCardSlots() {
        cardSlots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setVisible(false)));
        cardSlots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setDisable(true)));
        baseProd.setDisable(true);
    }

    public void resetLeader() {
        leaders.forEach(imageView -> imageView.setDisable(true));
        discardButton.forEach(button -> button.setVisible(false));
        leaders.forEach(imageView -> imageView.setImage(new Image("/GUI/back/leader_back.png")));
    }

    public void resetStandardDepots() {
        depots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setImage(null)));
    }

    public void resetLeaderDepots(){
        leadersDepots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setVisible(false)));
        leadersDepots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setImage(null)));
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

        btn_back.setVisible(false);

        currentShowed = Client.getInstance().getMyName();
    }

    public void setUpOtherPlayer(String username){
        resetBoard();
        setDisableBoardForOther(true);

        ModelData model = Client.getInstance().getModelOf(username).toModelData();
        loadBoard(model);

        btn_back.setVisible(true);

        currentShowed = username;
    }

    private void loadBoard(ModelData model){
        loadFaithTrack(model);
        loadStandardDepots(model);
        loadLeaderDepots(model);
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
        System.out.println("leader cliked");
        if (event.getSource().equals(leader1)){}
            //Client.getInstance().writeToStream(new LeaderManage(0, false));
        else{}
            //Client.getInstance().writeToStream(new LeaderManage(1, false));
    }

    @FXML
    public void leaderProdClicked(MouseEvent actionEvent){
        System.out.println("leader prod");
        prodState = ProdState.ALREADY_PROD;
        if (actionEvent.getSource().equals(prodLeader1)) {
           // Client.getInstance().writeToStream(new ProductionAction(0, true));
        }
        else{}
           // Client.getInstance().writeToStream(new ProductionAction(1, true));
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
        System.out.println("card dev clicked");
        prodState = ProdState.ALREADY_PROD;
        if (event.getSource().equals(baseProd))
            Client.getInstance().writeToStream(new BaseProduction());
        else{
            for (int i = 0; i < cardSlots.size(); i++) {
                if(cardSlots.get(i).stream().anyMatch(imageView -> imageView.equals(event.getSource())))
                    Client.getInstance().writeToStream(new ProductionAction(i,false));
            }
        }
    }

    @FXML
    public void productionClicked(){
        switch (prodState) {
            case NOT_IN_PROD:
                activateProd();
                prodState = ProdState.INITIAL;
                break;
            case INITIAL:
                softExitProd();
                prodState = ProdState.NOT_IN_PROD;
                btn_prod.setText("PRODUCTION");
                break;
            case ALREADY_PROD:
                exitProd();
                prodState = ProdState.NOT_IN_PROD;
                btn_prod.setText("PRODUCTION");
                break;
        }
    }

    private void activateProd(){
        setDisableBoardForProd(true);
        btn_prod.setText("END PRODUCTION");
        ArrayList<CardLeaderData> leadersData = Client.getInstance().getMyModel().toModelData().getLeaders();
        for (int i = 0; i < leadersData.size(); i++) {
            if (leadersData.get(i).getEffects().stream().anyMatch(effectData -> effectData.getType().equals(EffectType.PRODUCTION))
                && leadersData.get(i).isActive())
                leadersProd.get(i).setVisible(true);
        }
        baseProd.setDisable(false);
        ArrayList<ArrayList<CardDevData>> cardSlotsData = Client.getInstance().getMyModel().toModelData().getCardSlots();
        for (int i = 0; i < cardSlotsData.size(); i++) {
            if (cardSlotsData.get(i).size() > 0)
                cardSlots.get(i).get(cardSlotsData.get(i).size()-1).setDisable(false);
        }
    }

    private void softExitProd(){
        setDisableBoardForProd(false);
        //leadersProd.forEach(imageView -> imageView.setVisible(false));
        baseProd.setDisable(true);
        cardSlots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setDisable(true)));
    }

    private void exitProd(){
        softExitProd();
        Client.getInstance().writeToStream(new EndProductionSelection());
    }

    @FXML
    public void back(){
        setUpAll();
    }

    @FXML
    public void depotResourcePressed(MouseEvent mouseEvent){
        ModelData modelData = Client.getInstance().getMyModel().toModelData();
        ResourceData resourceData;
        for (int i = 0; i < depots.size(); i++) {
            if (depots.get(i).stream().anyMatch(imageView -> imageView.equals(mouseEvent.getSource()))){
                resourceData = new ResourceData(modelData.getStandardDepot().get(i).getType(), 1);
                Client.getInstance().writeToStream(new DepotModify(i, resourceData, true));
                break;
            }
        }
    }

    @FXML
    public void depotLeaderPressed(MouseEvent mouseEvent){
        ModelData modelData = Client.getInstance().getMyModel().toModelData();
        ResourceData resourceData;
        for (int i = 0; i < leadersDepots.size(); i++) {
            if (leadersDepots.get(i).equals(mouseEvent.getSource())){
                if (i<2) {
                    resourceData = new ResourceData(modelData.getLeaderDepot().get(0).getType(), 1);
                    Client.getInstance().writeToStream(new DepotModify(0, resourceData, true));
                }
                else{
                    resourceData = new ResourceData(modelData.getLeaderDepot().get(1).getType(), 1);
                    Client.getInstance().writeToStream(new DepotModify(1, resourceData, true));
                }
            }
        }
    }

    @FXML
    public void strongboxPressed(MouseEvent mouseEvent){
        if (strong_stone.equals(mouseEvent.getSource()))
            Client.getInstance().writeToStream(new StrongboxModify(new ResourceData(ResourceType.STONE,1)));
        else if(strong_shield.equals(mouseEvent.getSource()))
            Client.getInstance().writeToStream(new StrongboxModify(new ResourceData(ResourceType.SHIELD,1)));
        else if (strong_serv.equals(mouseEvent.getSource()))
            Client.getInstance().writeToStream(new StrongboxModify(new ResourceData(ResourceType.SERVANT,1)));
        else if (strong_coin.equals(mouseEvent.getSource()))
            Client.getInstance().writeToStream(new StrongboxModify(new ResourceData(ResourceType.COIN,1)));
    }

    public void showDeckDev(){
        ControllerHandler.getInstance().changeView(Views.DECK_DEV);
    }

    public void askCardSlotSelection(int rowDevCard, int colDevCard){
        this.rowDevCard = rowDevCard;
        this.colDevCard = colDevCard;
    }

    @FXML
    public void selectCardSlot(ActionEvent event){
        int slot;
        if (event.getSource().equals(selectSlot1Btn)){
            slot = 0;
        }else if(event.getSource().equals(selectSlot2Btn)){
            slot = 1;
        }else if(event.getSource().equals(selectSlot3Btn)){
            slot = 2;
        }else{
            return;
        }

        Client.getInstance().writeToStream(new DevelopmentAction(rowDevCard, colDevCard, slot));
    }

    //warehouse resource insertion
    public void updateDepot(int index, ResourceData depot, boolean isNormalDepot){
        if (isNormalDepot){
            for (int i = 0; i < depot.getValue(); i++){
                depots.get(index).get(i).setImage(new Image(depot.toResourceImage()));
            }
        }
    }

    public void updateStrongbox(ArrayList<ResourceData> strongboxUpdated){
        for (ResourceData res: strongboxUpdated){
            strongboxLabelMap.get(res.getType()).setText(Integer.toString(res.getValue()));
        }
    }


    //ANY CONVERSION REQUEST

    public void endLocalProduction(){
        bufferBox.setVisible(false);
    }

    public void setUpAnyConversion(ArrayList<ResourceData> conversion, int num){
        resourceBufferLabelsMap.values().forEach(x -> x.setText("0"));
        resourceBufferTopLabelsMap.values().forEach(x -> x.setText("0"));
        resourceBufferTopLabelsMap.values().forEach(x -> x.setVisible(false));
        resourceBufferLabelsMap.values().forEach(x -> x.setVisible(false));
        anyConverted.keySet().forEach(x -> anyConverted.put(x, 0));

        resourceBufferDecreaseBtnMap.values().forEach(x -> {
            x.setVisible(false);
            x.setOnAction(this::decreaseAnySelected);
        });

        bufferBox.setVisible(true);
        numOfAnyToConvert = num;
        if (conversion == null){
            resourceBufferImages.values().forEach(x->x.setOnMouseClicked(this::selectAnyToConvert));
        }else{
            conversion.forEach(x -> {
                resourceBufferImages.get(x.getType()).setOnMouseClicked(this::selectAnyToConvert);
                Label topLabel = resourceBufferTopLabelsMap.get(x.getType());
                topLabel.setText(Integer.toString(x.getValue()));
                topLabel.setVisible(true);
            });
        }

    }

    private void selectAnyToConvert(MouseEvent event){
        ImageView source = (ImageView) event.getSource();
        ResourceType type = null;
        for(ResourceType resType: resourceBufferImages.keySet()){
            if (resourceBufferImages.get(resType).equals(source)){
                type = resType;
                break;
            }
        }
        Label topLabel = resourceBufferTopLabelsMap.get(type);
        Label label = resourceBufferLabelsMap.get(type);
        Button btn = resourceBufferDecreaseBtnMap.get(type);

        label.setText(Integer.toString(Integer.parseInt(label.getText()) + 1));
        if (!label.isVisible()){
            label.setVisible(true);
        }

        if (!btn.isVisible()){
            btn.setVisible(true);
        }

        if(topLabel.isVisible()){
            int oldTopValue = Integer.parseInt(topLabel.getText());
            topLabel.setText(Integer.toString(oldTopValue - 1));
            if (oldTopValue == 1){
                source.setDisable(true);
            }
        }


        anyConverted.put(type, anyConverted.get(type) + 1);
        if (anyConverted.values().stream().mapToInt(x -> x).sum() == numOfAnyToConvert){
            System.out.println("Any response sent now!");
            ArrayList<ResourceData> response = anyConverted.keySet()
                    .stream().map(x -> new ResourceData(x, anyConverted.get(x)))
                    .collect(Collectors.toCollection(ArrayList::new));

            Client.getInstance().writeToStream(new AnyResponse(response));
        }
    }
    public void decreaseAnySelected(ActionEvent event){
        Button source = (Button) event.getSource();
        ResourceType typeSource = null;
        for (ResourceType type: resourceBufferDecreaseBtnMap.keySet()){
            if (resourceBufferDecreaseBtnMap.get(type).equals(source)){
                typeSource = type;
                break;
            }
        }
        int oldNum = anyConverted.get(typeSource);
        if (oldNum == 1){
            source.setVisible(false);
        }
        anyConverted.put(typeSource, oldNum - 1);
        resourceBufferLabelsMap.get(typeSource).setText(Integer.toString(oldNum - 1));


        int oldTopNum = Integer.parseInt(resourceBufferTopLabelsMap.get(typeSource).getText());
        resourceBufferTopLabelsMap.get(typeSource).setText(Integer.toString(oldTopNum + 1));

        resourceBufferImages.get(typeSource).setDisable(false);
    }




    //RESOURCE FORM MARKET METHODS

    public void setUpResourceFromMarket(ArrayList<ResourceData> resources) {
        resourceBufferLabelsMap.values().forEach(x -> x.setText("0"));
        resourceBufferTopLabelsMap.values().forEach(x -> x.setVisible(false));
        resourceBufferDecreaseBtnMap.values().forEach(x -> x.setVisible(false));

        bufferBox.setVisible(true);
        for (ResourceData res : resources) {
            resourceBufferLabelsMap.get(res.getType()).setText(Integer.toString(res.getValue()));
        }

        resourceBufferImages.values().forEach(imageView -> {
            imageView.setOnDragDetected(this::dragDetected);
        });

        depots.forEach(imageViews -> imageViews.forEach(
                imageView -> {
                    imageView.setOnDragDetected(this::dragDetected);
                    imageView.setOnDragOver(this::dragOver);
                    imageView.setOnDragDropped(this::dragDropped);
                }

        ));

        leadersDepots.forEach(imageViews -> imageViews
                .forEach(imageView -> {
                    imageView.setOnDragDetected(this::dragDetected);
                    imageView.setOnDragOver(this::dragOver);
                    imageView.setOnDragDropped(this::dragDropped);
                }));
    }

    public void dragDetected(MouseEvent event){
        Node source = event.getPickResult().getIntersectedNode();
        ImageView imageView = (ImageView) source;

        Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(imageView.getImage());
        startImage = (ImageView) event.getSource();
        db.setContent(content);
        event.consume();
    }



    public void dragOver(DragEvent event){
        System.out.println("Drag Over");
        if (event.getDragboard().hasImage()) {
            event.acceptTransferModes(TransferMode.ANY);
            destImage = (ImageView) event.getTarget();
        }
        event.consume();
    }

    private int getImageDepotIndex(ArrayList<ArrayList<ImageView>> depots, ImageView imageView){
        for (int i = 0; i < depots.size(); i++) {
            if (depots.get(i).stream().anyMatch(x -> x.equals(imageView))){
                return i;
            }
        }
        return -1;
    }


    public void dragDropped(DragEvent event){
        System.out.println("Drag Dropped");
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasImage()) {
            if (resourceBufferImages.containsValue(startImage)){
                ResourceType type = null;
                for(ResourceType resType: resourceBufferImages.keySet()){
                    if (resourceBufferImages.get(resType).equals(startImage)){
                        type = resType;
                        break;
                    }
                }
                boolean isNormalDepot;
                int indexDest;
                if (depots.stream().anyMatch(x -> x.contains(destImage))){
                    isNormalDepot = true;
                    indexDest = getImageDepotIndex(depots, destImage);
                }else if (leadersDepots.stream().anyMatch(x->x.contains(destImage))){
                    isNormalDepot = false;
                    indexDest = getImageDepotIndex(leadersDepots, destImage);
                }else{
                    return;
                }
                System.out.println("Depot insertion:\nDepotIndex: " + indexDest + " isNormal: "
                        + isNormalDepot + " ResType: " + type);

                Client.getInstance().writeToStream(new DepotModify(indexDest, new ResourceData(type, 1), isNormalDepot));
                return;
            }

            boolean isFromNormalDepot;
            int startIndex;
            if (depots.stream().anyMatch(x -> x.contains(startImage))){
                isFromNormalDepot = true;
                startIndex = getImageDepotIndex(depots, startImage);
            }else if(leadersDepots.stream().anyMatch(x->x.contains(startImage))){
                isFromNormalDepot = false;
                startIndex = getImageDepotIndex(leadersDepots, startImage);
            }else{
                return;
            }
            boolean isToNormalDepot;
            int destIndex;
            if (depots.stream().anyMatch(x -> x.contains(destImage))){
                isToNormalDepot = true;
                destIndex = getImageDepotIndex(depots, destImage);

            }else if (leadersDepots.stream().anyMatch(x->x.contains(destImage))){
                isToNormalDepot = false;
                destIndex = getImageDepotIndex(leadersDepots, destImage);
            }else{
                return;
            }
            System.out.println("Switch:\nFromDepot: " +startIndex + " isNormal: " + isFromNormalDepot);
            System.out.println("ToDepot: " +destIndex + " isNormal: " + isToNormalDepot);
            Client.getInstance().writeToStream(new DepotSwitch(startIndex, isFromNormalDepot, destIndex, isToNormalDepot));

            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }


    public void bufferUpdate(ArrayList<ResourceData> bufferUpdated){
        resourceBufferLabelsMap.values().forEach(x -> x.setText("0"));
        resourceBufferTopLabelsMap.values().forEach(x -> x.setVisible(false));
        resourceBufferDecreaseBtnMap.values().forEach(x -> x.setVisible(false));

        bufferBox.setVisible(true);
        for (ResourceData res : bufferUpdated) {
            resourceBufferLabelsMap.get(res.getType()).setText(Integer.toString(res.getValue()));
        }

    }

}