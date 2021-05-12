package it.polimi.ingsw.client.data;

import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EffectDataTest {
    @Test
    public void testPrint(){
        ArrayList<ResourceData> before=new ArrayList<>();
        ArrayList<ResourceData> after=new ArrayList<>();
        ResourceData rs1= new ResourceData(ResourceType.COIN, 2);
        ResourceData rs2= new ResourceData(ResourceType.SERVANT, 1);
        before.add(rs1);
        after.add(rs2);
        EffectData ef= new EffectData("pippo", before, after);
        System.out.println(ef.toString());
    }
}