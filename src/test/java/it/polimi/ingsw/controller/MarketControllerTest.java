package it.polimi.ingsw.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MarketControllerTest {

    GameSetting gameSetting;
    GameMaster gameMaster;
    CardManager cardManager;
    PersonalBoard personalBoard;
    Market market;
    ResourceManager resourceManager;
    Controller controller;
    ObjectMapper mapper = new ObjectMapper();
    ArrayList<Leader> leaders;

    @BeforeEach
    @Test
    void init() throws IOException, JsonFileModificationError {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        leaders =
                mapper.readValue(new File("src/main/resources/json/leader.json"), new TypeReference<>() {});

        gameSetting = new GameSetting(3);
        ArrayList<String> players = new ArrayList<>();
        players.add("Lorenzo");
        players.add("Matteo");
        players.add("Davide");

        gameMaster = new GameMaster(gameSetting, players);
        gameMaster.nextPlayer();
        personalBoard = gameMaster.getPlayerPersonalBoard(gameMaster.getCurrentPlayer());
        cardManager = personalBoard.getCardManager();
        resourceManager = personalBoard.getResourceManager();
        market = gameMaster.getMarket();


        controller = new Controller(gameMaster);

    }

    private void printState(){
        System.out.println("State: " + controller.getTurnState());
    }

    private void attachLeader(int index){
        Leader leader = leaders.get(index);
        leader.attachCardToUser(personalBoard, gameMaster.getMarket());
        personalBoard.getCardManager().addLeader(leader);
    }

    @Test
    void marketActionWithNoLeaders() {
        printState();
        controller.marketAction(2, true);
        System.out.println(gameMaster.getMarket().getWhiteMarbleDrew());
        printState();
    }

    private String arrayToString(ArrayList<ResourceData> array){
        String x = "[";
        for (ResourceData res: array){
            x+= res.getType() + ": " + res.getValue();
        }
        x += "]";
        return x;

    }

    @Test
    void marketActionWithTwoLeaders() {
        assertEquals(0, cardManager.howManyMarbleEffects());
        //1 stone
        attachLeader(10);
        assertEquals(1, cardManager.howManyMarbleEffects());
        //1 coin
        attachLeader(11);
        assertEquals(2, cardManager.howManyMarbleEffects());

        Map<Integer, ArrayList<ResourceData>> listOfMarbleEffect = cardManager.listOfMarbleEffect();
        for (int i: listOfMarbleEffect.keySet()){
            System.out.println(i + ": " + arrayToString(listOfMarbleEffect.get(i)));
        }


        printState();
        controller.marketAction(2, true);

        System.out.println(market.getResourceToSend());
        int whiteMarble = market.getWhiteMarbleDrew();
        System.out.println("WhiteMarble: " + whiteMarble);
        printState();

        assertDoesNotThrow(()->
                controller.leaderWhiteMarbleConversion(0, 1));
        printState();

        assertDoesNotThrow(()->
                controller.leaderWhiteMarbleConversion(1, 1));
        printState();



    }


    @Test
    void marketActionOneTwoLeaders() {
        assertEquals(0, cardManager.howManyMarbleEffects());
        //1 stone
        attachLeader(10);
        assertEquals(1, cardManager.howManyMarbleEffects());

        printState();
        controller.marketAction(2, false);
        printState();


    }


}