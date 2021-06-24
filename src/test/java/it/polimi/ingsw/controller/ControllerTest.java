package it.polimi.ingsw.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class ControllerTest {

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
    void init() throws  Exception {
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


        controller = new Controller(gameMaster, new Match(3, new Server(new String[0]), 1));


    }

    @Test
    void turnTest(){
        if (controller.isYourTurn("Lorenzo")){
            assertFalse(controller.isYourTurn("Davide"));
            assertFalse(controller.isYourTurn("Matteo"));
        }else if (controller.isYourTurn("Davide")){
            assertFalse(controller.isYourTurn("Lorenzo"));
            assertFalse(controller.isYourTurn("Matteo"));
        }else if (controller.isYourTurn("Matteo")){
            assertFalse(controller.isYourTurn("Davide"));
            assertFalse(controller.isYourTurn("Lorenzo"));}
    }

    @Test
    void endGame(){
        assertDoesNotThrow(()-> controller.endGame());
    }

    @Test
    void reconnectGame(){
        assertDoesNotThrow(()->controller.reconnectGameMessage("Lorenzo"));
    }

    @Test
    void leaderManage(){
        assertDoesNotThrow(()->controller.leaderManage(0,true));
        assertDoesNotThrow(()->controller.leaderManage(0,true));
        assertDoesNotThrow(()->controller.leaderManage(0,true));
        assertDoesNotThrow(()->controller.leaderManage(0,false));
    }

    @Test
    void cheatTest(){
        assertDoesNotThrow(()->controller.cheat());
    }

    @Test
    void productionTest(){
        assertDoesNotThrow(()-> controller.baseProduction());
        assertDoesNotThrow(()-> controller.leaderProductionAction(0));
    }

    @Test
    void anyTest(){
        assertDoesNotThrow(()->controller.anyConversion(new ArrayList<>(){{add(ResourceFactory.createResource(ResourceType.COIN,1));}}));
    }

    @Test
    void strongbox(){
        assertDoesNotThrow(()->controller.subToStrongbox(ResourceFactory.createResource(ResourceType.COIN,1)));
    }

    @Test
    void depotModify() {
        assertDoesNotThrow(()->controller.depotModify(ResourceFactory.createResource(ResourceType.COIN,1),1,true));
    }

    @Test
    void switchDepots() {
        assertDoesNotThrow(()->controller.switchDepots(1,false,1,false));
    }


    @Test
    void discardLeaderSetUp() {
        assertDoesNotThrow(()->controller.discardLeaderSetUp(1,"Lorenzo"));
    }

    @Test
    void autoInsertSetUpResources() {
        assertDoesNotThrow(()->controller.autoInsertSetUpResources("Lorenzo"));
    }

    @Test
    void insertSetUpResources() {
        assertDoesNotThrow(()->controller.insertSetUpResources(new ArrayList<>(){{add(ResourceFactory.createResource(ResourceType.COIN,1));}},"Lorenzo"));
    }
}
