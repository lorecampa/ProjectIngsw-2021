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
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
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

class MakeProductionControllerTest {

    GameSetting gameSetting;
    GameMaster gameMaster;
    PersonalBoard personalBoard;
    ResourceManager resourceManager;
    CardManager cardManager;
    Controller controller;
    ObjectMapper mapper;
    ArrayList<Leader> leaders;
    String devCardString1, devCardString2;
    Development devCard1, devCard2;
    ArrayList<Resource> anyConv;

    @BeforeEach
    @Test
    void init() throws IOException, JsonFileModificationError, DeckDevelopmentCardException {
        mapper= new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        leaders =
                mapper.readValue(new File("src/main/resources/json/leader.json"), new TypeReference<>() {});
        devCardString1 = "{\n" +
                "    \"@class\": \"Development\",\n" +
                "    \"victoryPoints\": 1,\n" +
                "    \"requirements\": [\n" +
                "      {\n" +
                "        \"@class\": \"ResourceReq\",\n" +
                "        \"resourceReq\": [\n" +
                "          {\n" +
                "            \"type\" : \"SHIELD\",\n" +
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
                "          },\n" +
                "          {\n" +
                "            \"type\" : \"STONE\",\n" +
                "            \"value\": 1\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ],\n" +
                "    \"onCreationEffects\": [],\n" +
                "    \"level\" : 1,\n" +
                "    \"color\": \"GREEN\"\n" +
                "  }";

        devCardString2 = "{\n" +
                "    \"@class\": \"Development\",\n" +
                "    \"victoryPoints\": 3,\n" +
                "    \"requirements\": [\n" +
                "      {\n" +
                "        \"@class\": \"ResourceReq\",\n" +
                "        \"resourceReq\": [\n" +
                "          {\n" +
                "            \"type\" : \"SERVANT\",\n" +
                "            \"value\": 3\n" +
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
                "            \"value\": 2\n" +
                "          }\n" +
                "        ],\n" +
                "        \"resourceAcquired\" : [\n" +
                "          {\n" +
                "            \"type\": \"SERVANT\",\n" +
                "            \"value\": 1\n" +
                "          },\n" +
                "          {\n" +
                "            \"type\": \"SHIELD\",\n" +
                "            \"value\": 1\n" +
                "          },\n" +
                "          {\n" +
                "            \"type\": \"STONE\",\n" +
                "            \"value\": 1\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ],\n" +
                "    \"onCreationEffects\": [],\n" +
                "    \"level\" : 1,\n" +
                "    \"color\": \"PURPLE\"\n" +
                "  }";

        devCard1 = mapper.readValue(devCardString1, Development.class);
        devCard2 = mapper.readValue(devCardString2, Development.class);

        gameSetting = new GameSetting(3);
        ArrayList<String> players = new ArrayList<>();
        players.add("Lorenzo");
        players.add("Matteo");
        players.add("Davide");

        gameMaster = new GameMaster(gameSetting, players);
        gameMaster.nextPlayer();


        personalBoard = gameMaster.getPlayerPersonalBoard(gameMaster.getCurrentPlayer());
        resourceManager = personalBoard.getResourceManager();
        cardManager = personalBoard.getCardManager();

        //3 COIN
        //2 STONE
        //2 SHIELD
        resourceManager.addToStrongbox(ResourceFactory.createResource(ResourceType.COIN, 3));
        resourceManager.addToStrongbox(ResourceFactory.createResource(ResourceType.STONE, 2));
        resourceManager.addToStrongbox(ResourceFactory.createResource(ResourceType.SERVANT, 2));
        assertDoesNotThrow(()->resourceManager.addToWarehouse(true, 1,
                ResourceFactory.createResource(ResourceType.SHIELD, 2)));

        devCard1.attachCardToUser(personalBoard, gameMaster.getMarket());
        devCard2.attachCardToUser(personalBoard, gameMaster.getMarket());



        //production cost:   coin 1, any 2
        //production profit: faith 1, any 2, stone 1
        assertDoesNotThrow(()->cardManager.addDevCardTo(devCard1, 0));
        cardManager.emptyCardSlotBuffer();


        //ReqProduction    =  2 coin
        //ProfitProduction =  1 servant, 1 shield, 1 stone
        assertDoesNotThrow(()->cardManager.addDevCardTo(devCard2, 1));
        cardManager.emptyCardSlotBuffer();

        controller = new Controller(gameMaster, new Match(3, new Server(), 1));
        resourceManager.restoreRM();
        controller.changeTurnState(TurnState.LEADER_MANAGE_BEFORE);
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
    void normalProduction(){
        assertEquals(controller.getTurnState(), TurnState.LEADER_MANAGE_BEFORE);

        controller.normalProductionAction(1);
        assertEquals(controller.getTurnState(), TurnState.PRODUCTION_ACTION);

        assertThrows(Exception.class, ()->cardManager.developmentProduce(1));

        controller.stopProductionCardSelection();
        assertEquals(controller.getTurnState(), TurnState.PRODUCTION_RESOURCE_REMOVING);

    }

    @Test
    public void anyConversionProduction(){
        assertEquals(controller.getTurnState(), TurnState.LEADER_MANAGE_BEFORE);

        controller.baseProduction();
        assertEquals(controller.getTurnState(), TurnState.ANY_PRODUCE_COST_CONVERSION);

        assertThrows(Exception.class, ()->resourceManager.convertAnyRequirement(
                resourceArray(0, 0, 0, 2, 0, 1), false));

        assertThrows(Exception.class, ()->resourceManager.convertAnyRequirement(
                resourceArray(1, 1, 1, 0, 0, 0), false));

        assertThrows(Exception.class, ()->resourceManager.convertAnyRequirement(
                resourceArray(0, 3, 0, 0, 0, 0), false));


        assertDoesNotThrow(()->resourceManager.convertAnyRequirement(
                resourceArray(1, 0, 0, 1, 0, 0), false));
        assertEquals(controller.getTurnState(), TurnState.ANY_PRODUCE_PROFIT_CONVERSION);

        assertThrows(Exception.class, ()->resourceManager.convertAnyProductionProfit(
                resourceArray(0, 2, 0, 0, 0, 0)));

        assertDoesNotThrow(()->resourceManager.convertAnyProductionProfit(
                resourceArray(0, 1, 0, 0, 0, 0)));
        assertEquals(controller.getTurnState(), TurnState.PRODUCTION_ACTION);

        controller.stopProductionCardSelection();
        assertEquals(controller.getTurnState(), TurnState.PRODUCTION_RESOURCE_REMOVING);

        controller.clearBufferFromMarket();
        assertEquals(controller.getTurnState(), TurnState.LEADER_MANAGE_AFTER);

    }

    @Test
    void productionWhiteLeader(){
        assertEquals(controller.getTurnState(), TurnState.LEADER_MANAGE_BEFORE);
        attachLeader(13);
        attachLeader(14);

        controller.leaderProductionAction(0);
        assertEquals(controller.getTurnState(), TurnState.ANY_PRODUCE_PROFIT_CONVERSION);
        assertDoesNotThrow(()->resourceManager.convertAnyProductionProfit(
                resourceArray(1, 0, 0, 0, 0, 0)));

        assertEquals(controller.getTurnState(), TurnState.PRODUCTION_ACTION);
        assertThrows(Exception.class, ()->cardManager.activateLeader(0));
        assertEquals(controller.getTurnState(), TurnState.PRODUCTION_ACTION);


        controller.leaderProductionAction(1);
        assertEquals(controller.getTurnState(), TurnState.ANY_PRODUCE_PROFIT_CONVERSION);
        assertDoesNotThrow(()->resourceManager.convertAnyProductionProfit(
                resourceArray(0, 1, 0, 0, 0, 0)));

        assertEquals(controller.getTurnState(), TurnState.PRODUCTION_ACTION);
        assertThrows(Exception.class, ()->cardManager.activateLeader(1));
        assertEquals(controller.getTurnState(), TurnState.PRODUCTION_ACTION);

        controller.normalProductionAction(1);
        assertEquals(controller.getTurnState(), TurnState.PRODUCTION_ACTION);
        assertThrows(Exception.class, ()->cardManager.developmentProduce(1));

        controller.stopProductionCardSelection();
        assertEquals(controller.getTurnState(), TurnState.PRODUCTION_RESOURCE_REMOVING);

    }


}

