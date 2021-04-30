package it.polimi.ingsw.model.personalBoard.cardManager;

import it.polimi.ingsw.exception.CardAlreadyUsed;
import it.polimi.ingsw.exception.CardWithHigherOrSameLevelAlreadyIn;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.card.Effect.State;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.card.requirement.Requirement;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
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

    @BeforeEach
    void setUp(){
        assertDoesNotThrow(()-> gs = new GameSetting(4));
        assertDoesNotThrow(()-> personalBoard = new PersonalBoard("player1", gs.getFaithTrack(),
                gs.getBaseProduction()));

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
        assertFalse(cardManager.doIHaveDevWithLv(2,1));
        assertDoesNotThrow(() -> cardManager.addDevelopmentCardTo(devLv1_1,1));
        assertDoesNotThrow(() -> cardManager.addDevelopmentCardTo(devLv2_1,1));
        assertDoesNotThrow(() -> cardManager.addDevelopmentCardTo(devLv3_1,1));
        assertDoesNotThrow(() -> cardManager.addDevelopmentCardTo(devLv1_2,2));
        assertTrue(cardManager.doIHaveDevWithLv(2,1));
        assertTrue(cardManager.doIHaveDevWithLv(1,2));
        assertTrue(cardManager.doIHaveDevWithLv(1,3));
    }

    @Test
    void doIhaveDevWithColor() {
        assertFalse(cardManager.doIhaveDevWithColor(2, Color.BLUE));
        assertDoesNotThrow(() -> cardManager.addDevelopmentCardTo(devLv1_1,1));
        assertDoesNotThrow(() -> cardManager.addDevelopmentCardTo(devLv2_1,1));
        assertDoesNotThrow(() -> cardManager.addDevelopmentCardTo(devLv3_1,1));
        assertDoesNotThrow(() -> cardManager.addDevelopmentCardTo(devLv1_2,2));
        assertTrue(cardManager.doIhaveDevWithColor(2,Color.BLUE));
        assertTrue(cardManager.doIhaveDevWithColor(1,Color.YELLOW));
        assertTrue(cardManager.doIhaveDevWithColor(1,Color.GREEN));
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
        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.addDevelopmentCardTo(devLv1_1,-1));
        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.addDevelopmentCardTo(devLv1_1,3));

        assertDoesNotThrow(()-> cardManager.addDevelopmentCardTo(devLv1_1,0));
        assertDoesNotThrow(()-> cardManager.addDevelopmentCardTo(devLv1_1,1));
        assertDoesNotThrow(()-> cardManager.addDevelopmentCardTo(devLv1_1,2));

        assertThrows(CardWithHigherOrSameLevelAlreadyIn.class,()->cardManager.addDevelopmentCardTo(devLv3_2,0));
        assertThrows(CardWithHigherOrSameLevelAlreadyIn.class,()->cardManager.addDevelopmentCardTo(devLv1_2,0));
    }

    @Test
    void activateLeaderEffect() {
        cardManager.addLeader(leader1);
        cardManager.addLeader(leader2);

        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.activateLeaderEffect(-1, State.MARKET_STATE));
        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.activateLeaderEffect(2,State.PRODUCTION_STATE));

        assertDoesNotThrow(()-> cardManager.activateLeaderEffect(0,State.MARKET_STATE));
        assertThrows(CardAlreadyUsed.class, ()-> cardManager.activateLeaderEffect(0, State.PRODUCTION_STATE));
        assertDoesNotThrow(()-> cardManager.activateLeaderEffect(1,State.MARKET_STATE));

        cardManager.clearUsed();

        assertDoesNotThrow(()-> cardManager.activateLeaderEffect(0,State.MARKET_STATE));
        assertDoesNotThrow(()-> cardManager.activateLeaderEffect(1,State.MARKET_STATE));
    }

    @Test
    void developmentProduce() {
        assertDoesNotThrow(()-> cardManager.addDevelopmentCardTo(devLv1_1,0));
        assertDoesNotThrow(()-> cardManager.addDevelopmentCardTo(devLv1_2,1));
        assertDoesNotThrow(()-> cardManager.addDevelopmentCardTo(devLv2_1,0));
        assertDoesNotThrow(()-> cardManager.addDevelopmentCardTo(devLv3_1,0));

        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.developmentProduce(1,-1));
        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.developmentProduce(1,2));
        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.developmentProduce(0,0));
        assertThrows(IndexOutOfBoundsException.class, ()-> cardManager.developmentProduce(2,1));

        assertDoesNotThrow(()-> cardManager.developmentProduce(1,0));
        assertDoesNotThrow(()-> cardManager.developmentProduce(1,1));
        assertDoesNotThrow(()-> cardManager.developmentProduce(2,0));
        assertDoesNotThrow(()-> cardManager.developmentProduce(3,0));

        assertThrows(CardAlreadyUsed.class, ()-> cardManager.developmentProduce(1,0));
        assertThrows(CardAlreadyUsed.class, ()-> cardManager.developmentProduce(1,1));
        assertThrows(CardAlreadyUsed.class, ()-> cardManager.developmentProduce(2,0));
        assertThrows(CardAlreadyUsed.class, ()-> cardManager.developmentProduce(3,0));

        cardManager.clearUsed();

        assertDoesNotThrow(()-> cardManager.developmentProduce(1,0));
        assertDoesNotThrow(()-> cardManager.developmentProduce(1,1));
        assertDoesNotThrow(()-> cardManager.developmentProduce(2,0));
        assertDoesNotThrow(()-> cardManager.developmentProduce(3,0));
    }

    @Test
    void baseProductionProduce() {
        assertDoesNotThrow(()->cardManager.baseProductionProduce());
        assertThrows(CardAlreadyUsed.class, ()-> cardManager.baseProductionProduce());
        cardManager.clearUsed();
        personalBoard.getResourceManager().clearBuffers();
        assertDoesNotThrow(()-> cardManager.baseProductionProduce());
    }
}