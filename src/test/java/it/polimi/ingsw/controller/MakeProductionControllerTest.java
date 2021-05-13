package it.polimi.ingsw.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    String devCardString;
    Development devCard;
    ArrayList<Resource> anyConv;

    @BeforeEach
    @Test
    void init() throws IOException, JsonFileModificationError, DeckDevelopmentCardException {
        mapper= new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        //ReqProduction    =    1 coin, 2 any
        //ProfitProduction =    1 fatih, 2 any, 1 stone
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

        devCard = mapper.readValue(devCardString, Development.class);

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

        assertDoesNotThrow(()->resourceManager.addToWarehouse(true, 1,
                ResourceFactory.createResource(ResourceType.SHIELD, 2)));
        devCard.attachCardToUser(personalBoard, gameMaster.getMarket());


        //production cost:   coin 1, any 2
        //production profit: faith 1, any 2, stone 1
        assertDoesNotThrow(()->cardManager.addDevCardTo(devCard, 0));
        cardManager.emptyCardSlotBuffer();
        controller = new Controller(gameMaster);

    }

    private void printState(){
        System.out.println("State: "+ controller.getTurnState() + "\n");

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
    void test(){
        resourceManager.newTurn();
        printState();

        controller.normalProductionAction(0, false);
        printState();

        controller.anyRequirementResponse(
                resourceArray(0, 0, 0, 2, 0, 1), false);
        printState();

        controller.anyRequirementResponse(
                resourceArray(0, 0, 0, 2, 0, 0), false);
        printState();


        controller.anyProductionProfitResponse(resourceArray(0, 0, 0, 2, 1, 2));
        printState();

        controller.anyProductionProfitResponse(resourceArray(0, 0, 1, 0, 0, 0));
        printState();
        controller.anyProductionProfitResponse(resourceArray(0, 1, 0, 0, 0, 0));
        printState();

        controller.stopProductionCardSelection();
        printState();
        //resourceRemoving


        controller.subToStrongbox(ResourceFactory.createResource(ResourceType.COIN, 2));
        printState();

        controller.subToStrongbox(ResourceFactory.createResource(ResourceType.COIN, 1));
        printState();

        controller.subToWarehouse(ResourceFactory.createResource(ResourceType.SHIELD, 1), 1, true);
        printState();

        controller.subToStrongbox(ResourceFactory.createResource(ResourceType.STONE, 2));
        printState();

    }


    void test2(){
        resourceManager.newTurn();

    }

}

