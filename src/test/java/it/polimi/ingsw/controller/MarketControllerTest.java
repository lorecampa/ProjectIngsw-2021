package it.polimi.ingsw.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.TurnState;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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


        controller = new Controller(gameMaster, new Match(3, new Server(), 1));

    }



    private void attachLeader(int index){
        Leader leader = leaders.get(index);
        leader.attachCardToUser(personalBoard, gameMaster.getMarket());
        leader.setActive();
        personalBoard.getCardManager().addLeader(leader);
    }

    @Test
    void marketActionWithNoLeaders() {
        assertEquals(controller.getTurnState(), TurnState.LEADER_MANAGE_BEFORE);

        assertEquals(controller.getTurnState(), TurnState.LEADER_MANAGE_BEFORE);
        controller.marketAction(2, true);
        assertEquals(controller.getTurnState(), TurnState.MARKET_RESOURCE_POSITIONING);

    }



    @Test
    void marketActionWithTwoLeaders() {
        assertEquals(controller.getTurnState(), TurnState.LEADER_MANAGE_BEFORE);

        assertEquals(0, cardManager.howManyMarbleEffects());
        //1 stone
        attachLeader(10);
        assertEquals(1, cardManager.howManyMarbleEffects());

        //1 coin
        attachLeader(11);
        assertEquals(2, cardManager.howManyMarbleEffects());

        controller.marketAction(2, true);
        int numOfWhiteMarble = market.getWhiteMarbleDrew();
        if (numOfWhiteMarble > 0){
            assertEquals(controller.getTurnState(), TurnState.WHITE_MARBLE_CONVERSION);
        }

        controller.leaderWhiteMarbleConversion(0, 1);
        if (numOfWhiteMarble - 1 > 0){
            assertEquals(controller.getTurnState(), TurnState.WHITE_MARBLE_CONVERSION);
            controller.leaderWhiteMarbleConversion(1, numOfWhiteMarble - 1);
        }
        assertEquals(controller.getTurnState(), TurnState.MARKET_RESOURCE_POSITIONING);


    }


    @Test
    void marketActionOneLeader() {
        assertEquals(controller.getTurnState(), TurnState.LEADER_MANAGE_BEFORE);

        assertEquals(0, cardManager.howManyMarbleEffects());
        //1 stone
        attachLeader(10);
        assertEquals(1, cardManager.howManyMarbleEffects());

        controller.marketAction(2, true);
        assertEquals(controller.getTurnState(), TurnState.MARKET_RESOURCE_POSITIONING);

    }


}