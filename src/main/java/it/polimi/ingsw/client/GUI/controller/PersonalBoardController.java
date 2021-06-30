package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.GUI.ControllerHandler;
import it.polimi.ingsw.client.GUI.Views;
import it.polimi.ingsw.client.ModelClient;
import it.polimi.ingsw.client.data.*;
import it.polimi.ingsw.message.serverMessage.*;
import it.polimi.ingsw.model.resource.ResourceType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import java.util.*;
import java.util.stream.Collectors;

public class PersonalBoardController extends Controller{
    @FXML private Pane background;
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
    @FXML private ImageView effectLeader1;
    @FXML private ImageView effectLeader2;
    @FXML private ImageView leader1;
    @FXML private ImageView leader2;
    @FXML private ImageView le_depot_11;
    @FXML private ImageView le_depot_12;
    @FXML private ImageView le_depot_21;
    @FXML private ImageView le_depot_22;
    @FXML private ChoiceBox<String> choice_username;


    @FXML private AnchorPane customMessageBox;

    @FXML private AnchorPane bufferBox;
    @FXML private Button discardMarketResBtn;
    @FXML private Label bufferCustomLabel;
    @FXML private GridPane bufferGrid;

    @FXML private Button selectSlot1Btn;
    @FXML private Button selectSlot2Btn;
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

    @FXML private ImageView logError;
    @FXML private ImageView imageBuffer4;
    @FXML private Label labelBuffer4;
    @FXML private Label bufferTop4;
    @FXML private Button decreaseResBuffer4;
    @FXML private Button btn_endTurn;
    @FXML private ImageView inkwell_image;
    @FXML private Label playerName_label;

    private ImageView startImage;
    private ImageView destImage;
    private enum ProdState {NOT_IN_PROD,INITIAL,ALREADY_PROD}
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
    private final ArrayList<Button> selectCardSlotButtons = new ArrayList<>();
    private final ArrayList<ImageView> leadersEffect = new ArrayList<>();
    private final ArrayList<Button> discardButton = new ArrayList<>();
    private final HashMap<ResourceType, ImageView> resourceBufferImages = new HashMap<>();
    private final HashMap<ResourceType, Label> resourceBufferLabelsMap = new HashMap<>();
    private final HashMap<ResourceType, Label> resourceBufferTopLabelsMap = new HashMap<>();
    private final HashMap<ResourceType, Button> resourceBufferDecreaseBtnMap = new HashMap<>();
    private int numOfAnyToConvert;
    private final HashMap<ResourceType, Integer> anyConverted = new HashMap<>() {{
        put(ResourceType.COIN,0);
        put(ResourceType.SERVANT, 0);
        put(ResourceType.SHIELD,0);
        put(ResourceType.STONE, 0);
    }};



    /**
     * Method that prepare all the marbles and buttons in the scene
     */
    @FXML
    public void initialize(){
        prodState = ProdState.NOT_IN_PROD;
        setUpCustomMessageBox(customMessageBox);
        setUpTrack();
        setUpPopeFavor();
        setUpDepots();
        setUpStrongbox();
        setUpCardSlots();
        setUpLeaders();
        setUpLeadersProd();
        setUpBuffer();
        setUpDiscard();
        setUpSelectCardSlotButton();
    }

    /**
     * See {@link Controller#setUpAll()}
     */
    @Override
    public void setUpAll(){
        resetBoard();
        ModelData model = Client.getInstance().getMyModel().toModelData();
        loadBoard(model);
        btn_back.setVisible(false);
        setStandardBoard();
        currentShowed = Client.getInstance().getMyName();
        playerName_label.setText(currentShowed);
        setUpBackground(background);
    }

    /**
     * See {@link Controller#showCustomMessage(String)}
     * @param msg the message to be displayed
     */
    @Override
    public void showCustomMessage(String msg) {
        Label label = (Label) customMessageBox.getChildren().get(0);
        TextField  tf = new TextField(msg);
        label.textProperty().bind(tf.textProperty());
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

    /**
     * Method that setup the GUI elements of the faith track
     */
    private void setUpTrack(){
        track.addAll(Arrays.asList(pos0,pos1,pos2,pos3,pos4,pos5,pos6,pos7,pos8,pos9,pos10,pos11,pos12,pos13,pos14,
                pos15,pos16,pos17,pos18,pos19,pos20,pos21,pos22,pos23,pos24));
        track.forEach(image -> image.setVisible(false));
    }

    /**
     * Method that setup the GUI elements of the card slot buttons
     */
    private void setUpSelectCardSlotButton(){
        selectCardSlotButtons.add(selectSlot1Btn);
        selectCardSlotButtons.add(selectSlot2Btn);
        selectCardSlotButtons.add(selectSlot3Btn);
        selectCardSlotButtons.forEach(x -> x.setVisible(false));
    }

    /**
     * Method that setup the GUI elements of the discard button
     */
    private void setUpDiscard(){
        discardButton.add(btn_discard1);
        discardButton.add(btn_discard2);
    }

    /**
     * Method that setup the GUI elements of the leader cards
     */
    private void setUpLeaders(){
        leaders.add(leader1);
        leaders.add(leader2);

        ArrayList<ImageView> leaderDepot1 = new ArrayList<>();
        leaderDepot1.add(le_depot_11);
        leaderDepot1.add(le_depot_12);
        leadersDepots.add(leaderDepot1);
        ArrayList<ImageView> leaderDepot2 = new ArrayList<>();
        leaderDepot2.add(le_depot_21);
        leaderDepot2.add(le_depot_22);
        leadersDepots.add(leaderDepot2);

        leadersDepots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setVisible(false)));
    }

    /**
     * Method that setup the GUI elements of the leaders production
     */
    private void setUpLeadersProd(){
        leadersEffect.add(effectLeader1);
        leadersEffect.add(effectLeader2);

        leadersEffect.forEach(imageView -> imageView.setVisible(false));
    }

    /**
     * Method that setup the GUI elements of the the depots
     */
    private void setUpDepots(){
        ArrayList<ImageView> row0 = new ArrayList<>();
        row0.add(dep11);

        ArrayList<ImageView> row1 = new ArrayList<>();
        row1.add(dep21);
        row1.add(dep22);

        ArrayList<ImageView> row2 = new ArrayList<>();
        row2.add(dep31);
        row2.add(dep32);
        row2.add(dep33);

        depots.add(row0);
        depots.add(row1);
        depots.add(row2);

    }

    /**
     * Method that setup the GUI elements of the strongbox
     */
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

    /**
     * Method that setup the GUI elements of the card slot
     */
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

    /**
     * Method that setup the GUI elements of the pope favor
     */
    public void setUpPopeFavor(){
        popeFavorsDiscard.add(popeFavor1_Discard);
        popeFavorsDiscard.add(popeFavor2_Discard);
        popeFavorsDiscard.add(popeFavor3_Discard);

        popeFavorsAcquired.add(popeFavor1_Acquired);
        popeFavorsAcquired.add(popeFavor2_Acquired);
        popeFavorsAcquired.add(popeFavor3_Acquired);

        popeFavorsAcquired.forEach(image -> image.setVisible(false));
    }

    /**
     * Method that setup the GUI elements of the all buffer
     */
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

    //-------------------------------
    // LOADING FROM MODEL PART TO GUI
    //-------------------------------

    /**
     * Methods that load and display the faith track from the model
     * @param model the model of the player
     */
    public void loadFaithTrack(ModelData model){
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

    /**
     * Methods that load and display the standard depots from the model
     * @param model the model of the player
     */
    public void loadStandardDepots(ModelData model){
        ArrayList<ResourceData> standardDepots = model.getStandardDepot();
        for (int i = 0; i < standardDepots.size(); i++) {
            updateDepot(i, standardDepots.get(i), true);
        }
    }
    /**
     * Methods that load and display the leader depots from the model
     * @param model the model of the player
     */
    public void loadLeaderDepots(ModelData model){
        ArrayList<ResourceData> le_depots = model.getLeaderDepot();
        ArrayList<CardLeaderData> leadersData = model.getLeaders();

        boolean isWarehouse;
        for (int i = 0; i < le_depots.size(); i++) {
            for (int j = i; j < leadersData.size(); j++) {
                if (leadersData.get(j).isActive()) {
                    isWarehouse = leadersData.get(j).getEffects().stream().anyMatch(effectData -> effectData.getType().equals(EffectType.WAREHOUSE));
                    if (isWarehouse){
                        leadersDepots.get(j).forEach(imageView -> imageView.setVisible(true));
                        for (int h = 0; h < le_depots.get(i).getValue(); h++) {
                            leadersDepots.get(j).get(h).setImage(new Image(le_depots.get(i).toResourceImage()));
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Methods that load and display the leaders from the model
     * @param model the model of the player
     */
    public void loadLeader(ModelData model){
        ArrayList<CardLeaderData> leadersData = model.getLeaders();
        boolean owned = Client.getInstance().getMyName().equals(model.getUsername());

        for (int i = 0; i < leadersData.size() && i<2; i++) {
            CardLeaderData leaderData = leadersData.get(i);
            leaders.get(i).setImage(new Image(leaderData.toResourcePath()));
            boolean activated = leaderData.isActive();

            if (activated) {
                leaders.get(i).setDisable(true);
                discardButton.get(i).setVisible(false);
            }
            else if (owned) {
                leaders.get(i).setDisable(false);
                discardButton.get(i).setVisible(true);
            }
        }
    }

    /**
     * Methods that load and display the strongbox from the model
     * @param model the model of the player
     */
    public void loadStrongBox(ModelData model){
        ArrayList<ResourceData> strongBoxData = model.getStrongbox();
        for (ResourceData resourceData : strongBoxData){
            strongboxLabelMap.get(resourceData.getType()).setText(String.valueOf(resourceData.getValue()));
        }
    }

    /**
     * Methods that load and display the card slot from the model
     * @param model the model of the player
     */
    public void loadCardSlots(ModelData model){
        ArrayList<ArrayList<CardDevData>>cardSlotsData = model.getCardSlots();
        for (int i = 0; i < cardSlotsData.size(); i++) {
            for (int j = 0; j < cardSlotsData.get(i).size(); j++) {
                cardSlots.get(i).get(j).setImage(new Image(cardSlotsData.get(i).get(j).toResourcePath()));
                cardSlots.get(i).get(j).setVisible(true);
            }
        }
    }

    /**
     * Method that load all names in the selection box "other players"
     */
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

    /**
     * Method that makes visible the personal board in standard mode
     */
    public void setStandardBoard(){
        // LEADER & LEADER DISCARD
        ArrayList<CardLeaderData> leadersData = Client.getInstance().getMyModel().toModelData().getLeaders();
        for (int i = 0; i < leadersData.size() && i<2; i++) {
            CardLeaderData leaderData = leadersData.get(i);
            boolean activated = leaderData.isActive();
            if (activated) {
                leaders.get(i).setDisable(true);
                discardButton.get(i).setVisible(false);
            }
            else {
                leaders.get(i).setDisable(false);
                discardButton.get(i).setVisible(true);
                discardButton.get(i).setDisable(false);
            }
        }

        //LEADER EFFECTS
        leadersDepots.forEach(imageViews -> imageViews.forEach(imageView -> {
            imageView.setDisable(true);
            imageView.setOnMouseClicked(null);
            imageView.setOnDragDetected(null);
            imageView.setOnDragOver(null);
            imageView.setOnDragDropped(null);
        }));

        leadersEffect.forEach(imageView -> {
            imageView.setVisible(false);
            imageView.setOnMouseClicked(null);
        });

        //DEPOTS
        depots.forEach(imageViews -> imageViews.forEach(imageView -> {
            imageView.setDisable(true);
            imageView.setOnMouseClicked(null);
            imageView.setOnDragDetected(null);
            imageView.setOnDragOver(null);
            imageView.setOnDragDropped(null);
        }));

        //STRONGBOX
        strongboxImageMap.forEach((type, imageView) -> imageView.setDisable(true));

        //CARD SLOT BTN
        selectCardSlotButtons.forEach(x -> x.setVisible(false));

        //CARDS
        cardSlots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setDisable(true)));

        //BASE PROD
        baseProd.setDisable(true);

        //BUTTONS
        btn_prod.setVisible(true);

        btn_market.setVisible(true);
        choice_username.setVisible(true);
        btn_players.setVisible(true);
        btn_endTurn.setVisible(true);
        btn_deck.setVisible(true);

        btn_back.setVisible(false);

        bufferBox.setVisible(false);

        customMessageBox.setVisible(false);

        logError.setVisible(true);

        btn_deck.setText("DECK DEV");
        btn_deck.setOnAction(event -> showDeckDev());
    }

    /**
     * Method that makes visible the personal board for buying card
     */
    private void setBoardForBuy(){
        //disable leader
        disableLeader();

        //disable buttons
        btn_prod.setVisible(false);
        btn_market.setVisible(false);
        choice_username.setVisible(false);
        btn_players.setVisible(false);
        btn_endTurn.setVisible(false);

        logError.setVisible(false);

        //enable card slot selection
        selectCardSlotButtons.forEach(x -> x.setVisible(true));

        //change deck dev button
        btn_deck.setText("CANCEL BUYING");
        btn_deck.setOnAction(event -> setStandardBoard());
    }

    /**
     * Method that makes visible the personal board for paying
     */
    private void setBoardForPay(){
        //DEPOTS
        depots.forEach(depot -> depot.forEach(x -> {
            x.setDisable(false);
            x.setOnMouseClicked(this::removeDepotRes);
        }));

        //STRONGBOX
        strongboxImageMap.values().forEach(imageView -> imageView.setDisable(false));

        //LEADER DEPOT
        leadersDepots.forEach(depot -> depot.forEach(x -> {
            x.setDisable(false);
            x.setOnMouseClicked(this::removeDepotRes);
        }));

        logError.setVisible(false);

        disableLeaderAndButtons();
    }

    /**
     * Method that makes visible the personal board for resource positioning and sets all the listener
     */
    private void setBoardForPos(){
        //DEPOTS
        depots.forEach(imageViews -> imageViews.forEach(
                imageView -> {
                    imageView.setDisable(false);
                    imageView.setOnDragDetected(this::dragDetected);
                    imageView.setOnDragOver(this::dragOver);
                    imageView.setOnDragDropped(this::dragDropped);
                }));

        //LEADERS DEPOTS
        leadersDepots.forEach(imageViews -> imageViews
                .forEach(imageView -> {
                    imageView.setDisable(false);
                    imageView.setOnDragDetected(this::dragDetected);
                    imageView.setOnDragOver(this::dragOver);
                    imageView.setOnDragDropped(this::dragDropped);
                }));

        logError.setVisible(false);

        disableLeaderAndButtons();

    }

    /**
     * Method that disable all the leaders
     */
    private void disableLeader(){
        leaders.forEach(imageView -> imageView.setDisable(true));
        discardButton.forEach(button -> button.setDisable(true));
    }

    /**
     * Method that disable all the leaders and the button
     */
    private void disableLeaderAndButtons(){
        //disable leader
        disableLeader();
        //disable buttons
        btn_prod.setVisible(false);
        btn_market.setVisible(false);
        choice_username.setVisible(false);
        btn_deck.setVisible(false);
        btn_players.setVisible(false);
        btn_endTurn.setVisible(false);
    }

    /**
     * Method that makes visible the personal board for production
     */
    private void setBoardForProd(){
        //disable leader
        disableLeader();

        logError.setVisible(false);

        //disable buttons
        btn_market.setVisible(false);
        choice_username.setVisible(false);
        btn_deck.setVisible(false);
        btn_players.setVisible(false);
        btn_endTurn.setVisible(false);

        btn_prod.setText("END PRODUCTION");
        btn_prod.setVisible(true);

        //LEADER PROD
        ArrayList<CardLeaderData> leadersData = Client.getInstance().getMyModel().toModelData().getLeaders();
        for (int i = 0; i < leadersData.size(); i++) {
            if (leadersData.get(i).getEffects().stream().anyMatch(effectData -> effectData.getType().equals(EffectType.PRODUCTION))
                    && leadersData.get(i).isActive()) {
                leadersEffect.get(i).setVisible(true);
                leadersEffect.get(i).setOnMouseClicked(this::leaderProdClicked);
            }
        }

        //BASE PROD
        baseProd.setDisable(false);

        //CARD
        ArrayList<ArrayList<CardDevData>> cardSlotsData = Client.getInstance().getMyModel().toModelData().getCardSlots();
        for (int i = 0; i < cardSlotsData.size(); i++) {
            if (cardSlotsData.get(i).size() > 0)
                cardSlots.get(i).get(cardSlotsData.get(i).size()-1).setDisable(false);
        }
    }

    /**
     * Method that makes visible the personal board for any conversion
     */
    private void setBoardForAnyConv(){
        logError.setVisible(false);
        disableLeaderAndButtons();
    }

    /**
     * Method that makes visible the personal board for marble conversion
     */
    private void setBoardForMarbleConv(){
        disableLeaderAndButtons();
        logError.setVisible(false);
        ArrayList<CardLeaderData> leadersData = Client.getInstance().getMyModel().toModelData().getLeaders();
        for (int i = 0; i < leadersData.size(); i++) {
            if (leadersData.get(i).getEffects().stream().anyMatch(effectData -> effectData.getType().equals(EffectType.MARBLE))
                    && leadersData.get(i).isActive()) {
                leadersEffect.get(i).setVisible(true);
                leadersEffect.get(i).setOnMouseClicked(this::leaderMarbleClicked);
            }
        }
    }

    /**
     * Method that makes visible the personal board for game ending
     */
    private void setBoardForEndGame(){
        logError.setVisible(false);
        disableLeaderAndButtons();
    }

    //-------------------------
    // RESET
    //-------------------------
    /**
     * Method that set the reset the values and the visibility of all the GUI elements
     */
    private void resetBoard(){
        btn_back.setVisible(false);
        bufferBox.setVisible(false);
        resetFaithTrack();
        resetStandardDepots();
        resetLeaderDepots();
        resetLeader();
        resetCardSlots();
    }

    /**
     * Method that reset the card slots
     */
    public void resetCardSlots() {
        cardSlots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setVisible(false)));
        cardSlots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setDisable(true)));
        baseProd.setDisable(true);
    }

    /**
     * Method that reset the leader card
     */
    public void resetLeader() {
        leaders.forEach(imageView -> imageView.setDisable(true));
        discardButton.forEach(button -> button.setVisible(false));
        leaders.forEach(imageView -> imageView.setImage(new Image("/GUI/back/leader_back.png")));
    }

    /**
     * Method that reset the standard depots
     */
    public void resetStandardDepots() {
        depots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setImage(null)));
    }

    /**
     *Method that reset the leader depots
     */
    public void resetLeaderDepots(){
        leadersDepots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setVisible(false)));
        leadersDepots.forEach(imageViews -> imageViews.forEach(imageView -> imageView.setImage(null)));
    }

    /**
     * Method that reset the faith track
     */
    public void resetFaithTrack() {
        track.forEach(imageView -> imageView.setVisible(false));
        popeFavorsAcquired.forEach(imageView -> imageView.setVisible(false));
        popeFavorsDiscard.forEach(imageView -> imageView.setVisible(true));
    }


    //-------------------------
    // LOAD WHOLE MODEL TO GUI
    //-------------------------

    /**
     * Method that set up the personal board for view the state of another player
     * @param username the username of the player to be displayed
     */
    public void setUpOtherPlayer(String username){
        resetBoard();

        btn_prod.setVisible(false);
        btn_market.setVisible(false);
        btn_deck.setVisible(false);

        ModelData model = Client.getInstance().getModelOf(username).toModelData();
        loadBoard(model);

        logError.setVisible(false);

        btn_back.setText("BACK");
        btn_back.setOnAction(t -> back());
        btn_back.setVisible(true);

        currentShowed = username;
        playerName_label.setText(currentShowed);
    }

    /**
     * Method that loads a specific model board
     * @param model the model with the board to be displayed
     */
    private void loadBoard(ModelData model){
        loadFaithTrack(model);
        loadStandardDepots(model);
        loadLeaderDepots(model);
        loadLeader(model);
        loadStrongBox(model);
        loadCardSlots(model);
        loadChoiceBox();
        inkwell_image.setVisible(Client.getInstance().getModelOf(model.getUsername()).isInkwell());
    }

    //-----------------
    // GUI ACTIONS
    //-----------------

    /**
     * Method attached to the "market" button
     */
    @FXML
    public void marketButton(){
        ControllerHandler.getInstance().changeView(Views.MARKET);
    }

    /**
     * Method that handle the click on a leader image
     */
    @FXML
    public void leaderClicked(MouseEvent event){
        if (event.getSource().equals(leader1))
            Client.getInstance().writeToStream(new LeaderManage(0, false));
        else
            Client.getInstance().writeToStream(new LeaderManage(1, false));
    }

    /**
     * Method that handle the marble leader click
     */
    @FXML
    public void leaderMarbleClicked(MouseEvent actionEvent){
        if (actionEvent.getSource().equals(effectLeader1))
            Client.getInstance().writeToStream(new WhiteMarbleConversionResponse(0, 1));
        else
            Client.getInstance().writeToStream(new WhiteMarbleConversionResponse(1, 1));
    }

    /**
     * Method that handle the production leader click
     */
    @FXML
    public void leaderProdClicked(MouseEvent actionEvent){
        prodState = ProdState.ALREADY_PROD;
        if (actionEvent.getSource().equals(effectLeader1))
           Client.getInstance().writeToStream(new ProductionAction(0, true));
        else
           Client.getInstance().writeToStream(new ProductionAction(1, true));
    }


    /**
     * Method that handle the discard leader button
     * @param actionEvent the event to be handled
     */
    @FXML
    public void leaderDiscard(ActionEvent actionEvent){
        if (actionEvent.getSource().equals(btn_discard1))
            Client.getInstance().writeToStream(new LeaderManage(0, true));
        else
            Client.getInstance().writeToStream(new LeaderManage(1, true));
    }

    /**
     * Method attached to the "other player" button
     */
    @FXML
    public void otherPlayerClicker(){
        if (Client.getInstance().existAModelOf(choice_username.getValue()))
            setUpOtherPlayer(choice_username.getValue());
    }

    /**
     * Method that handle the click on development card during production
     */
    @FXML
    public void cardProdClicked(MouseEvent event){
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

    /**
     * Method attached to the "production" button
     */
    @FXML
    public void productionClicked(){
        switch (prodState) {
            case NOT_IN_PROD:
                setBoardForProd();
                prodState = ProdState.INITIAL;
                break;
            case INITIAL:
                setStandardBoard();
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

    /**
     * Method attached to the "exitProd" button
     */
    private void exitProd(){
        setStandardBoard();
        Client.getInstance().writeToStream(new EndProductionSelection());
    }

    /**
     * Method attached to the "back" button
     */
    @FXML
    public void back(){
        setUpAll();
    }

    /**
     * Method attached to the "deck development" button
     */
    @FXML
    public void showDeckDev(){
        ControllerHandler.getInstance().changeView(Views.DECK_DEV);
    }

    /**
     * Method that handle the card slot choice
     * @param rowDevCard the row of the card in the deck development
     * @param colDevCard the column of the card in the deck development
     */
    public void askCardSlotSelection(int rowDevCard, int colDevCard){
        this.rowDevCard = rowDevCard;
        this.colDevCard = colDevCard;
        setBoardForBuy();
    }

    /**
     * Method attached  to the click of one of the cards slot during selection
     * @param event the event to be handled
     */
    @FXML
    public void selectCardSlot(ActionEvent event){
        Button btn = (Button) event.getSource();
        int slot = selectCardSlotButtons.indexOf(btn);
        Client.getInstance().writeToStream(new DevelopmentAction(rowDevCard, colDevCard, slot));
    }

    /**
     * Method that handle the depot modify
     * @param index the depot index
     * @param depot the depot updated
     * @param isNormalDepot true if it is normal depot, false otherwise
     */
    public void updateDepot(int index, ResourceData depot, boolean isNormalDepot){
        if (isNormalDepot){
            for (int i = 0; i < depot.getValue(); i++){
                depots.get(index).get(i).setImage(new Image(depot.toResourceImage()));
            }
        }
    }


    /**
     * Method that handle end of production
     */
    public void endLocalProduction(){
        if (prodState != ProdState.NOT_IN_PROD)
            setBoardForProd();
        bufferBox.setVisible(false);
    }

    /**
     * Method that set up the marble conversion
     */
    public void setUpMarbleConv(){
        setBoardForMarbleConv();
        bufferGrid.setVisible(false);
        bufferBox.setVisible(true);
    }

    /**
     * Method that setup the any conversion phase
     * @param conversion the resource that are possible to be converted
     * @param num the num of resources to convert
     */
    public void setUpAnyConversion(ArrayList<ResourceData> conversion, int num){
        resetBuffer();
        setBoardForAnyConv();
        bufferGrid.setVisible(true);
        resourceBufferImages.values().forEach(x -> x.setDisable(false));
        resourceBufferDecreaseBtnMap.values().forEach(x -> x.setOnAction(this::decreaseAnySelected));
        anyConverted.keySet().forEach(x -> anyConverted.put(x, 0));

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
        bufferBox.setVisible(true);
    }

    /**
     * Method that handle resource click in conversion phase
     * @param event the event to be handled
     */
    private void selectAnyToConvert(MouseEvent event){
        ImageView source = (ImageView) event.getSource();
        ResourceType type = getResTypeFromImage(resourceBufferImages, source);
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
            ArrayList<ResourceData> response = anyConverted.keySet()
                    .stream().map(x -> new ResourceData(x, anyConverted.get(x)))
                    .collect(Collectors.toCollection(ArrayList::new));

            Client.getInstance().writeToStream(new AnyResponse(response));
        }
    }

    /**
     * Method that handle the resource decrease in any conversion
     * @param event the event to be handled
     */
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

    //END GAME

    /**
     * Method that handle the game ending
     */
    public void setUpForEnd(){
        setBoardForEndGame();
        btn_back.setText("MAIN MENU");
        btn_back.setVisible(true);
        btn_back.setOnAction(event -> {
            Client.getInstance().setState(ClientState.MAIN_MENU);
            Client.getInstance().clearModels();
            ControllerHandler.getInstance().changeView(Views.MAIN_MENU);
        });
    }

    //RESOURCE FORM MARKET METHODS

    /**
     * Method that reset the buffer of resources
     */
    private void resetBuffer(){
        resourceBufferLabelsMap.values().forEach(x -> {
            x.setVisible(false);
            x.setText(Integer.toString(0));
        });

        resourceBufferTopLabelsMap.values().forEach(x ->{
            x.setVisible(false);
            x.setText(Integer.toString(0));
        });

        resourceBufferDecreaseBtnMap.values().forEach(x -> x.setVisible(false));

        discardMarketResBtn.setVisible(false);

        resourceBufferImages.values().forEach(x ->{
            x.setVisible(true);
            x.setDisable(true);
        });
    }

    /**
     * Method that setup the warehouse for resource removing
     * @param resources the resources to remove
     */
    public void setUpWarehouseResourceRemoving(ArrayList<ResourceData> resources){
        resetBuffer();
        setBoardForPay();

        resourceBufferLabelsMap.values().forEach(x -> x.setVisible(true));
        for (ResourceData res : resources) {
            resourceBufferLabelsMap.get(res.getType()).setText(Integer.toString(res.getValue()));
        }

        bufferBox.setVisible(true);

    }

    /**
     * Method handle the click on a strongbox resource during removing phase
     * @param event the event to be handled
     */
    public void removeStrongboxRes(MouseEvent event){
        ImageView source = (ImageView) event.getSource();
        ResourceType type = getResTypeFromImage(strongboxImageMap, source);
        Label label = strongboxLabelMap.get(type);
        int resNum = Integer.parseInt(label.getText());
        if (resNum > 0){
            Client.getInstance().writeToStream(new StrongboxModify(new ResourceData(type, 1)));
        }

    }

    /**
     * Method handle the click on a depot resource during removing phase
     * @param event the event to be handled
     */
    public void removeDepotRes(MouseEvent event) {
        ImageView source = (ImageView) event.getSource();
        ResourceType type;
        boolean isNormalDepot;
        int index;
        if (source.getImage() != null) {
            if (depots.stream().anyMatch(x -> x.contains(source))) {
                isNormalDepot = true;
                index = getImageDepotIndex(depots, source);
                type = Client.getInstance().getMyModel().toModelData().getStandardDepot().get(index).getType();
            } else if (leadersDepots.stream().anyMatch(x -> x.contains(source))) {
                isNormalDepot = false;
                index = getImageDepotIndex(leadersDepots, source);
                type = Client.getInstance().getMyModel().toModelData().getLeaderDepot().get(index).getType();

            } else {
                return;
            }
            Client.getInstance().writeToStream(new DepotModify(index, new ResourceData(type, 1),
                    isNormalDepot));
        }
    }

    /**
     * Utility method that returns the type of a specific image
     * @param map the map with the association image - type
     * @param image the target
     * @return the image type
     */
    private ResourceType getResTypeFromImage(Map<ResourceType, ImageView> map, ImageView image){
        ResourceType typeSource = null;
        for (ResourceType type: map.keySet()){
            if (map.get(type).equals(image)){
                typeSource = type;
                break;
            }
        }
        return typeSource;
    }

    /**
     * Method that prepare the board for resources allocation
     * @param resources the resources that need to be allocated
     */
    public void setUpResourceFromMarket(ArrayList<ResourceData> resources) {
        resetBuffer();
        bufferGrid.setVisible(true);
        resourceBufferLabelsMap.values().forEach(x -> x.setVisible(true));
        discardMarketResBtn.setVisible(true);
        for (ResourceData res : resources) {
            resourceBufferLabelsMap.get(res.getType()).setText(Integer.toString(res.getValue()));

            resourceBufferImages.get(res.getType()).setDisable(false);
            resourceBufferImages.get(res.getType()).setOnDragDetected(this::dragDetected);

        }

        setBoardForPos();
        bufferBox.setVisible(true);
    }

    /**
     * Method that handle the resources from market discard
     */
    public void discardMarketResources(){
        bufferBox.setVisible(false);
        resetBuffer();
        Client.getInstance().writeToStream(new DiscardResourcesFromMarket());
    }

    /**
     * Method that handle drag detection
     * @param event the event to be handled
     */
    public void dragDetected(MouseEvent event){
        Node source = event.getPickResult().getIntersectedNode();
        ImageView imageView;
        try{ imageView = (ImageView) source;}
        catch (Exception e){ return;}
        if (imageView.getImage() != null) {
            Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(imageView.getImage());
            startImage = (ImageView) event.getSource();
            db.setContent(content);
            event.consume();
        }
    }

    /**
     * Method that handle drag over
     * @param event the event to be handled
     */
    public void dragOver(DragEvent event){
        if (event.getDragboard().hasImage()) {
            event.acceptTransferModes(TransferMode.ANY);
            destImage = (ImageView) event.getTarget();
        }
        event.consume();
    }

    /**
     * Utility method that gets the depot index of a specific image
     * @param depots the depots
     * @param imageView the target
     * @return the index
     */
    private int getImageDepotIndex(ArrayList<ArrayList<ImageView>> depots, ImageView imageView){
        for (int i = 0; i < depots.size(); i++) {
            if (depots.get(i).stream().anyMatch(x -> x.equals(imageView))){
                return i;
            }
        }
        return -1;
    }

    /**
     * Method return true if the image is in the normal depots
     * @param imageView the target
     * @return return true if the image is in the normal depots, false otherwise
     */
    private boolean isNormalDepot(ImageView imageView){
        return depots.stream().anyMatch(x -> x.contains(imageView));
    }

    /**
     * Method finds the index normal or leader depot index
     * @param imageView the target
     * @return the index
     */
    private int computeIndex(ImageView imageView){
        int index;
        if (depots.stream().anyMatch(x -> x.contains(imageView))){
            index = getImageDepotIndex(depots, imageView);
        }else if (leadersDepots.stream().anyMatch(x->x.contains(imageView))){
            index = getImageDepotIndex(leadersDepots, imageView);
            int numOfLeaderBefore = 0;
            ArrayList<CardLeaderData> leaderData = Client.getInstance().getMyModel().toModelData().getLeaders();
            for (int i = 0; i < index; i++) {
                if (!leaderData.get(i).isActive() || leaderData.get(i).isActive() &&
                        leaderData.get(i).getEffects().stream()
                                .noneMatch(effectData -> effectData.getType().equals(EffectType.WAREHOUSE)))
                    numOfLeaderBefore++;
            }
            index -= numOfLeaderBefore;
        }else{
            throw new UnsupportedOperationException();
        }
        return index;
    }

    /**
     * Method that insert image dragged in depot
     */
    private void insertInDepot(){
        ResourceType type = null;
        for(ResourceType resType: resourceBufferImages.keySet()){
            if (resourceBufferImages.get(resType).equals(startImage)){
                type = resType;
                break;
            }
        }
        boolean isNormalDepot = isNormalDepot(destImage);
        int indexDest = computeIndex(destImage);

        Client.getInstance().writeToStream(new DepotModify(indexDest,
                new ResourceData(type, 1), isNormalDepot));
    }

    /**
     * Method that handle image dragged switch
     */
    private void switchDepots(){
        boolean isFromNormalDepot = isNormalDepot(startImage);
        int startIndex = computeIndex(startImage);

        boolean isToNormalDepot = isNormalDepot(destImage);
        int destIndex = computeIndex(destImage);

        Client.getInstance().writeToStream(new DepotSwitch(startIndex, isFromNormalDepot, destIndex, isToNormalDepot));

    }

    /**
     * Method that handle drag drop
     * @param event
     */
    public void dragDropped(DragEvent event){
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasImage()) {
            if (resourceBufferImages.containsValue(startImage)){
                insertInDepot();
            }else {
                switchDepots();
            }
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }


    /**
     * Method that handle buffer update
     * @param bufferUpdated the new buffer updated
     */
    public void bufferUpdate(ArrayList<ResourceData> bufferUpdated){
        bufferBox.setVisible(true);
        selectCardSlotButtons.forEach(x -> x.setVisible(false));
        if (bufferUpdated.stream().mapToInt(ResourceData::getValue).sum() == 0){
            bufferBox.setVisible(false);
            setStandardBoard();
        }else{
            resourceBufferLabelsMap.values().forEach(x -> x.setText(Integer.toString(0)));
            for (ResourceType type: resourceBufferImages.keySet()){
                if (bufferUpdated.stream().filter(x -> x.getValue() > 0)
                        .map(ResourceData::getType).noneMatch(x -> x == type)){
                    resourceBufferImages.get(type).setDisable(true);
                }
            }
            for (ResourceData res : bufferUpdated) {
                resourceBufferLabelsMap.get(res.getType()).setText(Integer.toString(res.getValue()));
            }
        }
    }


    public void setBufferLabel(String msg){
        bufferCustomLabel.setText(msg);
    }

    /**
     * Method attached to "end turn" button
     */
    @FXML
    public void endTurn(){
        Client.getInstance().writeToStream(new EndTurn());
    }


    /**
     * Method attached to the "show log error" button
     */
    @FXML
    public void showLogError(){
        ControllerHandler.getInstance().changeView(Views.LOG_ERROR);
    }
}