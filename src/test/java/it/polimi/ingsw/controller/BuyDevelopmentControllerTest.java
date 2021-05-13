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
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        //2 SERVANT
        //2 SHIELD
        resourceManager.addToStrongbox(ResourceFactory.createResource(ResourceType.COIN, 3));
        resourceManager.addToStrongbox(ResourceFactory.createResource(ResourceType.STONE, 2));
        assertDoesNotThrow(()->resourceManager.addToWarehouse(true, 1,
                ResourceFactory.createResource(ResourceType.SHIELD, 2)));


        controller = new Controller(gameMaster);

    }

    private void printState(){
        System.out.println("State: " + controller.getTurnState() + "\n");
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
        printState();
        resourceManager.newTurn();
        assertDoesNotThrow(()->controller.developmentAction(0, 0, 0));
        System.out.println("Any Required: " + resourceManager.getAnyRequired());
        printState();
        System.out.println("\n");

        //first any response
        System.out.println(1);
        anyConv = resourceArray(0, 0, 0, 0, 1, 0);
        controller.anyRequirementResponse(anyConv, true);
        printState();

        //second any response
        System.out.println(2);
        anyConv = resourceArray(0, 0, 0, 0, 0, 1);
        controller.anyRequirementResponse(anyConv, true);
        printState();


        //third any response
        System.out.println(3);
        anyConv = resourceArray(0, 2, 0, 0, 0, 0);
        controller.anyRequirementResponse(anyConv, true);
        printState();

        //fourth any response
        System.out.println(4);
        anyConv = resourceArray(1, 0, 1, 0, 0, 0);
        controller.anyRequirementResponse(anyConv, true);
        printState();

        //final true any response
        System.out.println(5);
        anyConv = resourceArray(2, 0, 0, 0, 0, 0);
        controller.anyRequirementResponse(anyConv, true);
        printState();

    }
}