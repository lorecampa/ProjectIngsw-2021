package it.polimi.ingsw.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BuyDevelopmentControllerTest {

    GameSetting gameSetting;
    GameMaster gameMaster;
    PersonalBoard personalBoard;
    ResourceManager resourceManager;
    Controller controller;
    ObjectMapper mapper = new ObjectMapper();
    ArrayList<Leader> leaders;
    String devCardString;
    Development devCard;
    ArrayList<Resource> anyConv;

    @BeforeEach
    @Test
    void init() throws IOException, JsonFileModificationError, DeckDevelopmentCardException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        leaders =
                mapper.readValue(new File("src/main/resources/json/leader.json"), new TypeReference<>() {});
        devCardString = "{\n" +
                "    \"@class\": \"Development\",\n" +
                "    \"victoryPoints\": 1,\n" +
                "    \"requirements\": [\n" +
                "      {\n" +
                "        \"@class\": \"ResourceReq\",\n" +
                "        \"resourceReq\": [\n" +
                "          {\n" +
                "            \"type\" : \"SHIELD\",\n" +
                "            \"value\": 2\n" +
                "          },\n" +
                "          {\n" +
                "            \"type\" : \"ANY\",\n" +
                "            \"value\": 2\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ],\n" +
                "    \"onActivationEffects\": [\n" +
                "      {\n" +
                "        \"@class\" : \"ProductionEffect\",\n" +
                "        \"resourceCost\": [\n" +
                "          {\n" +
                "            \"type\" : \"COIN\",\n" +
                "            \"value\": 1\n" +
                "          },\n" +
                "          {\n" +
                "            \"type\" : \"ANY\",\n" +
                "            \"value\": 2\n" +
                "          }\n" +
                "        ],\n" +
                "        \"resourceAcquired\" : [\n" +
                "          {\n" +
                "            \"type\": \"FAITH\",\n" +
                "            \"value\": 1\n" +
                "          },\n" +
                "          {\n" +
                "            \"type\" : \"ANY\",\n" +
                "            \"value\": 2\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ],\n" +
                "    \"onCreationEffects\": [],\n" +
                "    \"level\" : 1,\n" +
                "    \"color\": \"GREEN\"\n" +
                "  }";


        //cost: 2 shield, 2 any
        devCard = mapper.readValue(devCardString, Development.class);

        gameSetting = new GameSetting(3);
        ArrayList<String> players = new ArrayList<>();
        players.add("Lorenzo");
        players.add("Matteo");
        players.add("Davide");

        gameMaster = new GameMaster(gameSetting, players);
        gameMaster.nextPlayer();
        gameMaster.onDeckDevelopmentCardRemove(0, 0);
        gameMaster.pushDeckDevelopmentCard(0, 0, devCard);

        personalBoard = gameMaster.getPlayerPersonalBoard(gameMaster.getCurrentPlayer());
        resourceManager = personalBoard.getResourceManager();
        //3 COIN
        //2 STONE
        //2 SHIELD
        resourceManager.addToStrongbox(ResourceFactory.createResource(ResourceType.COIN, 3));
        resourceManager.addToStrongbox(ResourceFactory.createResource(ResourceType.STONE, 2));
        assertDoesNotThrow(()->resourceManager.addToWarehouse(true, 1,
                ResourceFactory.createResource(ResourceType.SHIELD, 2)));


        controller = new Controller(gameMaster, new Match(3, new Server(), 1));
        resourceManager.restoreRM();

    }

    private void attachLeader(int index){
        Leader leader = leaders.get(index);
        leader.attachCardToUser(personalBoard, gameMaster.getMarket());
        leader.setActive();
        personalBoard.getCardManager().addLeader(leader);
    }

    private ArrayList<Resource> resourceArray(int coin, int shield, int servant, int stone, int faith, int any){
        ArrayList<Resource> production = new ArrayList<>();
        if (coin > 0){
            production.add(ResourceFactory.createResource(ResourceType.COIN, coin));
        }
        if (shield > 0){
            production.add(ResourceFactory.createResource(ResourceType.SHIELD, shield));
        }
        if (servant > 0){
            production.add(ResourceFactory.createResource(ResourceType.SERVANT, servant));
        }
        if (stone > 0){
            production.add(ResourceFactory.createResource(ResourceType.STONE, stone));
        }
        if (faith > 0){
            production.add(ResourceFactory.createResource(ResourceType.FAITH, faith));
        }
        if (any > 0){
            production.add(ResourceFactory.createResource(ResourceType.ANY, any));
        }
        return production;
    }



    @Test
    void normalBuyDev(){
        assertEquals(controller.getTurnState(), TurnState.LEADER_MANAGE_BEFORE);
        controller.developmentAction(0, 0, 0);
        assertEquals(controller.getTurnState(), TurnState.ANY_BUY_DEV_CONVERSION);

        assertDoesNotThrow(()->resourceManager.convertAnyRequirement(
                resourceArray(1, 0, 0, 1, 0, 0), true));
        assertEquals(controller.getTurnState(), TurnState.BUY_DEV_RESOURCE_REMOVING);

        controller.clearBufferFromMarket();
        assertEquals(controller.getTurnState(), TurnState.LEADER_MANAGE_AFTER);


    }
}