package it.polimi.ingsw.model.card.creationEffect;

import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NavigableMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseEffectTest {

    Warehouse warehouse;
    OnCreationEffect onCreationEffect;
    @BeforeEach
    void init(){
        warehouse = new Warehouse();
    }
    @Test
    void nullDepots() {
        NavigableMap<String, Integer> players = new TreeMap<>();
        players.put("ciao1", 1);
        players.put("ciao2", 2);
        players.put("cioa3", 3);
        System.out.println(players.higherKey("ciao1"));
        System.out.println(players.higherKey("ciao3"));
    }


}