package it.polimi.ingsw.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;


public class MarketData {
    private final ArrayList<ArrayList<ColorData>> marketTray;
    private ColorData extraMarble;
    private final int numRow;
    private final int numCol;

    @JsonCreator
    public MarketData(@JsonProperty("marketTray") ArrayList<ArrayList<ColorData>> marketTray,
                      @JsonProperty("extraMarble") ColorData extraMarble,
                      @JsonProperty("numRow") int numRow,
                      @JsonProperty("numCol")int numCol) {
        this.marketTray = marketTray;
        this.extraMarble = extraMarble;
        this.numRow = numRow;
        this.numCol = numCol;
    }

    public void insertMarbleInRow(int row) throws IndexOutOfBoundsException{
        ColorData tempMarble = marketTray.get(row).get(0);
        marketTray.get(row).remove(0);
        marketTray.get(row).add(numCol - 1 , extraMarble);
        extraMarble = tempMarble;
    }

    public void insertMarbleInCol(int col){

        ColorData tempMarble = marketTray.get(0).get(col);
        for (int i = 0; i < numRow - 1; i++) {
            marketTray.get(i).remove(col);
            marketTray.get(i).add(col,marketTray.get(i+1).get(col));
        }
        marketTray.get(numRow - 1).remove(col);
        marketTray.get(numRow - 1).add( col, extraMarble); //ho aggiunto col per index
        extraMarble = tempMarble;
    }

    public boolean validRow(int row){
        return row >= 0 && row < numRow;
    }
    public boolean validCol(int col){
        return col >= 0 && col < numCol;
    }
/*
    public static void main(String[] args) {
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
*/
    @Override
    public String toString() {
        int WIDTH_MARBLE = 8;
        int width=(WIDTH_MARBLE +1)*numCol+ WIDTH_MARBLE *2;
        StringBuilder row;
        row = new StringBuilder("Market");
        ArrayList<String> rowsMarket=new ArrayList<>();
        String titleMarket=PrintAssistant.instance.stringBetweenChar(row.toString(), ' ', width, ' ', ' ');
        PrintAssistant.instance.printf(titleMarket, PrintAssistant.ANSI_BLACK, PrintAssistant.ANSI_YELLOW_BACKGROUND);
        rowsMarket.add("");
        row = new StringBuilder();
        int HEIGHT_MARBLE = 3;
        Integer middleHeightMarble=(Integer)(HEIGHT_MARBLE /2);
        Integer middleHeightRow=(Integer)(numRow/2);
        for(int i=0; i<numRow; i++){
            for(int k = 0; k< HEIGHT_MARBLE; k++){
                row = new StringBuilder();
                for(int j=0; j<numCol; j++){
                    row.append(marketTray.get(i).get(j).toColor());
                    row.append(PrintAssistant.instance.generateAStringOf(' ', WIDTH_MARBLE));
                    row.append(PrintAssistant.ANSI_RESET + " ");
                }
                if(k==middleHeightMarble)
                    row.append("ROW N").append(i).append(" ");
                else
                    row.append("       ");
                if(i==middleHeightRow){
                    row.append(extraMarble.toColor());
                    row.append(PrintAssistant.instance.generateAStringOf(' ', WIDTH_MARBLE));
                    row.append(PrintAssistant.ANSI_RESET + " ");
                }
                rowsMarket.add(row.toString());
            }
            rowsMarket.add("");
            row = new StringBuilder();
        }
        for(int j=0; j<numCol; j++){
            row.append(PrintAssistant.instance.stringBetweenChar("COL N" + (j), ' ', WIDTH_MARBLE + 2, ' ', ' '));
        }
        rowsMarket.add(row.toString());
        PrintAssistant.instance.printfMultipleString(rowsMarket);
        return "";
    }
}
