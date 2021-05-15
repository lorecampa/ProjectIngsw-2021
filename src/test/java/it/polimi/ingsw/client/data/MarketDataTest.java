package it.polimi.ingsw.client.data;

import it.polimi.ingsw.client.PrintAssistant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MarketDataTest {
    @Test
    public void testMarket(){
        MarketData market;
        int col=4;
        int rig=3;
        ArrayList<ArrayList<ColorData>> theTray = new ArrayList<>();
        for(int i=0;i<rig;i++){
            ArrayList<ColorData> rowOfRes= new ArrayList<>();
            for(int j=0; j<col; j++){
                if(i==1)
                    rowOfRes.add( ColorData.YELLOW);
                if(i==0)
                    rowOfRes.add( ColorData.BLUE);
                if(i==2)
                    rowOfRes.add( ColorData.PURPLE);
                if(i==3)
                    rowOfRes.add( ColorData.BLUE);
            }
            theTray.add(rowOfRes);
        }
        market = new MarketData(theTray, ColorData.PURPLE, rig, col);
        PrintAssistant.instance.printf(market.toString());
        //market.insertMarbleInCol(0);
        market.insertMarbleInCol(2);
        PrintAssistant.instance.printf(market.toString());
    }

}