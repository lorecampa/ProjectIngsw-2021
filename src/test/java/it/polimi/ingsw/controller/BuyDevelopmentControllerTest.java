package it.polimi.ingsw.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exception.DeckDevelopmentCardException;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.TurnState;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.VirtualClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BuyDevelopmentControllerTest {

    private final static String[] players = new String[]{"Lorenzo", "Matteo", "Davide"};
    PersonalBoard pb;
    ResourceManager rm;
    Controller controller;
    ObjectMapper mapper = new ObjectMapper();
    ArrayList<Leader> leaders;



    public static class GameMasterStub extends GameMaster{
        public GameMasterStub() throws IOException, JsonFileModificationError {
            super(new GameSetting(players.length), Arrays.stream(players)
                    .collect(Collectors.toCollection(ArrayList::new)));
        }

    }

    public static class MatchStub extends Match{
        private String messageClass;
        public MatchStub() {
            super(players.length, new Server(), 1);
            registerCustomVC();

        }
        private void registerCustomVC(){
            for (String player: players){
                super.getActivePlayers().add(new VirtualClient(player, this));
            }
        }

        @Override
        public void sendAllPlayers(ClientMessage message) {
            this.messageClass = message.getClass().getSimpleName();
            System.out.println(messageClass);
        }

        @Override
        public void sendSinglePlayer(String username, ClientMessage message) {
            this.messageClass = message.getClass().getSimpleName();
            System.out.println(messageClass);
        }
    }

    public static class ControllerStub extends Controller{
        public ControllerStub(GameMasterStub gm) throws IOException, JsonFileModificationError {
            super(gm, new MatchStub());
            nextTurn();
        }
    }

    public static class ResourceManagerStub extends ResourceManager{
        public ResourceManagerStub() {
            super();
        }
    }

    @BeforeEach
    @Test
    void init() throws IOException, JsonFileModificationError, DeckDevelopmentCardException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        leaders =
                mapper.readValue(new File("src/main/resources/json/leader.json"), new TypeReference<>() {});
        GameMasterStub gm = new GameMasterStub();
        controller = new ControllerStub(gm);

        pb = gm.getPlayerPersonalBoard(gm.getCurrentPlayer());
        rm = pb.getResourceManager();
        //3 COIN
        //2 STONE
        //2 SHIELD
        rm.addToStrongbox(ResourceFactory.createResource(ResourceType.COIN, 3));
        rm.addToStrongbox(ResourceFactory.createResource(ResourceType.STONE, 3));
        rm.addToStrongbox(ResourceFactory.createResource(ResourceType.SHIELD, 3));
        rm.addToStrongbox(ResourceFactory.createResource(ResourceType.SERVANT, 3));

        assertDoesNotThrow(()-> rm.addToWarehouse(true, 1,
                ResourceFactory.createResource(ResourceType.SHIELD, 2)));




    }


    @Test
    void normalBuyDev(){
        assertEquals(controller.getTurnState(), TurnState.LEADER_MANAGE_BEFORE);
        controller.developmentAction(0, 0, 0);
        assertEquals(controller.getTurnState(), TurnState.BUY_DEV_RESOURCE_REMOVING);


    }


}