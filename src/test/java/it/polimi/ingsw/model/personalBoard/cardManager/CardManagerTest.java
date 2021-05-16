package it.polimi.ingsw.model.personalBoard.cardManager;

import it.polimi.ingsw.exception.CardAlreadyUsed;
import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.exception.NotEnoughRequirementException;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.card.requirement.Requirement;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardManagerTest {

    PersonalBoard personalBoard;
    GameSetting gs;
    CardManager cardManager;
    Development devLv1_1, devLv1_2;
    Development devLv2_1, devLv2_2;
    Development devLv3_1, devLv3_2;
    Leader leader1;
    Leader leader2;
    ArrayList<Effect> effects = new ArrayList<>();
    ArrayList<Requirement> requirements = new ArrayList<>();

    private void clearBuffer(){
        cardManager.emptyCardSlotBuffer();
    }

    @BeforeEach
    void setUp(){
        assertDoesNotThrow(()-> gs = new GameSetting(4));
        Development baseProduction = gs.getBaseProduction();
        ResourceManager rm = new ResourceManager();
        baseProduction.setResourceManager(rm);
        assertDoesNotThrow(()-> personalBoard = new PersonalBoard("player1",
                gs.getFaithTrack(),
                rm,
                new CardManager(baseProduction)));

        cardManager = personalBoard.getCardManager();
        personalBoard.getResourceManager().addToStrongbox(ResourceFactory.createResource(ResourceType.STONE,5));
        devLv1_1 = new Development(1,requirements,effects,effects,1,Color.BLUE); devLv1_2 = new Development(1,requirements,effects,effects,1,Color.BLUE);
        devLv2_1 = new Development(1, requirements ,effects,effects, 2, Color.YELLOW); devLv2_2 = new Development(1, requirements ,effects,effects, 2, Color.BLUE);
        devLv3_1 = new Development(1, requirements ,effects,effects, 3, Color.GREEN); devLv3_2 = new Development(1, requirements ,effects,effects, 3, Color.GREEN);
        leader1 = new Leader(1,requirements,effects,effects);
        leader2 = new Leader(1,requirements,effects,effects);
    }

    @Test
    void doIHaveDevWithLv() {
        assertThrows(NotEnoughRequirementException.class, ()->cardManager.doIHaveDev(2,Color.ANY, 1));
        assertDoesNotThrow(() -> cardManager.addDevCardTo(devLv1_1,1));
        clearBuffer();

        assertDoesNotThrow(() -> cardManager.addDevCardTo(devLv2_1,1));
        clearBuffer();

        assertDoesNotThrow(() -> cardManager.addDevCardTo(devLv3_1,1));
        clearBuffer();

        assertDoesNotThrow(() -> cardManager.addDevCardTo(devLv1_2,2));
        clearBuffer();

        assertDoesNotThrow(()-> cardManager.doIHaveDev(2, Color.ANY, 1));
        assertDoesNotThrow(()-> cardManager.doIHaveDev(1, Color.ANY, 2));
        assertDoesNotThrow(()-> cardManager.doIHaveDev(1, Color.ANY, 3));
    }

    @Test
    void doIhaveDevWithColor() {
        assertThrows(NotEnoughRequirementException.class, ()->cardManager.doIHaveDev(2, Color.BLUE, 0));

        assertDoesNotThrow(() -> cardManager.addDevCardTo(devLv1_1,1));
        clearBuffer();

        assertDoesNotThrow(() -> cardManager.addDevCardTo(devLv2_1,1));
        clearBuffer();

        assertDoesNotThrow(() -> cardManager.addDevCardTo(devLv3_1,1));
        clearBuffer();

        assertDoesNotThrow(() -> cardManager.addDevCardTo(devLv1_2,2));
        clearBuffer();

        assertDoesNotThrow(()->cardManager.doIHaveDev(2,Color.BLUE, 0));
        assertDoesNotThrow(()->cardManager.doIHaveDev(1,Color.YELLOW, 0));
        assertDoesNotThrow(()->cardManager.doIHaveDev(1,Color.GREEN, 0));
    }

    @Test
    void discardLeader(){
        assertThrows(IndexOutOfBoundsException.class, () -> cardManager.discardLeader(0));
        cardManager.addLeader(leader1);
        assertDoesNotThrow(() -> cardManager.discardLeader(0));
        cardManager.addLeader(leader1);
        cardManager.addLeader(leader2);
        assertDoesNotThrow(() -> cardManager.discardLeader(1));
        assertDoesNotThrow(() -> cardManager.discardLeader(0));
        cardManager.addLeader(leader1);
        cardManager.addLeader(leader2);
        assertDoesNotThrow(() -> cardManager.discardLeader(0));
        assertDoesNotThrow(() -> cardManager.discardLeader(0));
    }

    @Test
    void activateLeader() {
        assertThrows(IndexOutOfBoundsException.class, () -> cardManager.activateLeader(0));
        cardManager.addLeader(leader1);
        assertDoesNotThrow(() -> cardManager.activateLeader(0));
    }

    @Test
    void addDevelopmentCardTo() {
        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.addDevCardTo(devLv1_1,-1));
        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.addDevCardTo(devLv1_1,3));

        assertDoesNotThrow(()-> cardManager.addDevCardTo(devLv1_1,0));
        clearBuffer();

        assertDoesNotThrow(()-> cardManager.addDevCardTo(devLv1_1,1));
        clearBuffer();

        assertDoesNotThrow(()-> cardManager.addDevCardTo(devLv1_1,2));
        clearBuffer();

        assertThrows(CardWithHigherOrSameLevelAlreadyIn.class,()->cardManager.addDevCardTo(devLv3_2,0));
        assertThrows(CardWithHigherOrSameLevelAlreadyIn.class,()->cardManager.addDevCardTo(devLv1_2,0));
    }

    @Test
    void activateLeaderEffect() {
        cardManager.addLeader(leader1);
        cardManager.addLeader(leader2);

        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.activateLeaderEffect(-1, TurnState.WHITE_MARBLE_CONVERSION));
        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.activateLeaderEffect(2, TurnState.PRODUCTION_ACTION));

        assertDoesNotThrow(()-> cardManager.activateLeaderEffect(0, TurnState.WHITE_MARBLE_CONVERSION));
        assertThrows(CardAlreadyUsed.class, ()-> cardManager.activateLeaderEffect(0, TurnState.PRODUCTION_ACTION));
        assertDoesNotThrow(()-> cardManager.activateLeaderEffect(1, TurnState.WHITE_MARBLE_CONVERSION));


        cardManager.restoreCM();

        assertDoesNotThrow(()-> cardManager.activateLeaderEffect(0, TurnState.WHITE_MARBLE_CONVERSION));
        assertDoesNotThrow(()-> cardManager.activateLeaderEffect(1, TurnState.WHITE_MARBLE_CONVERSION));
    }

    @Test
    void developmentProduce() {
        assertDoesNotThrow(()-> cardManager.addDevCardTo(devLv1_1,0));
        clearBuffer();

        assertDoesNotThrow(()-> cardManager.addDevCardTo(devLv1_2,1));
        clearBuffer();

        assertDoesNotThrow(()-> cardManager.addDevCardTo(devLv2_1,0));
        clearBuffer();

        assertDoesNotThrow(()-> cardManager.addDevCardTo(devLv3_1,0));
        clearBuffer();


        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.developmentProduce(-1));
        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.developmentProduce(2));
        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.developmentProduce(3));


        assertDoesNotThrow(()-> cardManager.developmentProduce(0));
        assertDoesNotThrow(()-> cardManager.developmentProduce(1));

        assertThrows(CardAlreadyUsed.class, ()->cardManager.developmentProduce(0));
        assertThrows(CardAlreadyUsed.class, ()-> cardManager.developmentProduce(1));


        cardManager.restoreCM();

        assertDoesNotThrow(()-> cardManager.developmentProduce(0));
        assertDoesNotThrow(()-> cardManager.developmentProduce(1));

    }

    @Test
    void baseProductionProduce() {
        assertDoesNotThrow(()->cardManager.baseProductionProduce());
        assertThrows(CardAlreadyUsed.class, ()-> cardManager.baseProductionProduce());
        cardManager.restoreCM();
        personalBoard.getResourceManager().restoreRM();
        assertDoesNotThrow(()-> cardManager.baseProductionProduce());
    }
}