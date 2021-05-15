package it.polimi.ingsw.client.data;

import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeckDevDataTest {
    @Test
    public void testDeck(){
        ArrayList<ResourceData> resourceReq = new ArrayList<>();
        ArrayList<ResourceData> cost = new ArrayList<>();
        ArrayList<ResourceData> earn = new ArrayList<>();
        resourceReq.add(new ResourceData(ResourceType.COIN, 2));
        resourceReq.add(new ResourceData(ResourceType.STONE, 2));
        cost.add(new ResourceData(ResourceType.SERVANT, 1));
        cost.add(new ResourceData(ResourceType.ANY, 2));
        earn.add(new ResourceData(ResourceType.FAITH, 4));
        earn.add(new ResourceData(ResourceType.SHIELD, 2));
        //CardDevData cdd=new CardDevData(1, 2, ColorData.BLUE, resourceReq, cost, earn );
        //CardDevData cdd1=new CardDevData(2, 5, Color.PURPLE, resourceReq, cost, earn );

        EffectData effData = new EffectData("xxxx", cost, earn);
        ArrayList<EffectData> effectsD= new ArrayList<>();
        effectsD.add(effData);

        ArrayList<ArrayList<ArrayList<CardDevData>>> deckDevelopment= new ArrayList<>();

        final int INITIAL_ROW_DECK = 3;
        final int INITIAL_COLUMN_DECK = 4;
        final int INITIAL_DEPTH_DECK = 4;

        for (int i = 0; i < INITIAL_ROW_DECK; i++){                     //lv
            //row
            ArrayList<ArrayList<CardDevData>> row = new ArrayList<>();
            for (int j = 0; j < INITIAL_COLUMN_DECK; j++){              //colore
                //depth
                ArrayList<CardDevData> pippo = new ArrayList<>();
                ArrayList<DeckDevData> cardBlock = new ArrayList<>();
                for (int k = 0; k < INITIAL_DEPTH_DECK; k++) {
                    ColorData color=ColorData.WHITE;
                    switch(j){
                        case  0:
                            color=ColorData.BLUE;
                            break;
                        case 1:
                            color=ColorData.PURPLE;
                            break;
                        case 2:
                            color=ColorData.YELLOW;
                            break;
                        case 3:
                            color=ColorData.GREEN;
                            break;
                    }
                    pippo.add(new CardDevData(i+1, 2+k, color, resourceReq, effectsD ));

                }
                row.add(pippo);
            }
            deckDevelopment.add(row);
        }
        DeckDevData mioDeck= new DeckDevData(deckDevelopment);
        System.out.println(mioDeck.toString());

        mioDeck.getCard(0,2);
        mioDeck.getCard(0,2);
        mioDeck.getCard(0,2);
        mioDeck.getCard(0,2);
        mioDeck.getCard(2,1);
        mioDeck.getCard(2,1);
        mioDeck.getCard(2,1);
        CardDevData c1= mioDeck.getCard(2,1);
        if(mioDeck.rowColValid(-1, 2)){
            System.out.println("ciao1");
        }
        if(mioDeck.rowColValid(1, 4)){
            System.out.println("ciao2");
        }
        if(mioDeck.rowColValid(2, 1)){
            System.out.println("ciao3");
        }
        if(mioDeck.rowColValid(20, 10)){
            System.out.println("ciao4");
        }
        System.out.println(mioDeck.toString());

        mioDeck.reInsertCard(c1, 2, 1);
        System.out.println(mioDeck.toString());
    }

}