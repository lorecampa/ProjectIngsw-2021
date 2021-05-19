package it.polimi.ingsw.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.PrintAssistant;

import java.util.ArrayList;


public class MarketData {
    private final ArrayList<ArrayList<ColorData>> marketTray;
    private final ColorData extraMarble;
    private final int numRow;
    private final int numCol;
    @JsonIgnore
    private final int WIDTH_MARBLE = 8;

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

    public boolean validRow(int row){
        return row >= 0 && row < numRow;
    }
    public boolean validCol(int col){
        return col >= 0 && col < numCol;
    }

    private void printTitle(int width) {
        String titleDeckDev = PrintAssistant.instance.stringBetweenChar("Market", ' ', width, ' ', ' ');
        PrintAssistant.instance.printf(titleDeckDev, PrintAssistant.ANSI_BLACK, PrintAssistant.ANSI_YELLOW_BACKGROUND);

    }

    private String marble(ColorData marble){
        return marble.toColor() +
                PrintAssistant.instance.generateAStringOf(' ', WIDTH_MARBLE) +
                PrintAssistant.ANSI_RESET + " ";
    }

    @Override
    public String toString() {
        final int width=(WIDTH_MARBLE +1)*numCol+ WIDTH_MARBLE *2;

        printTitle(width);

        StringBuilder row;
        ArrayList<String> rowsMarket=new ArrayList<>();
        rowsMarket.add("");
        row = new StringBuilder();

        int HEIGHT_MARBLE = 3;
        int middleHeightMarble= HEIGHT_MARBLE /2;
        int middleHeightRow= numRow/2;

        for(int i=0; i<numRow; i++){
            for(int k = 0; k< HEIGHT_MARBLE; k++){

                row = new StringBuilder();
                for(int j=0; j<numCol; j++){
                    row.append(marble(marketTray.get(i).get(j)));
                }

                if(k==middleHeightMarble)
                    row.append(i).append("  ");
                else {
                    row.append(PrintAssistant.ANSI_BLACK + "|");
                    row.append("  ");
                }

                if(i==middleHeightRow){
                    row.append(marble(extraMarble));
                    row.append(PrintAssistant.ANSI_BLACK + "|");
                }

                rowsMarket.add(row.toString());
            }
            if (i != numRow - 1)
                rowsMarket.add("");
            row = new StringBuilder();
        }
        for(int j=0; j<numCol; j++){
            row.append(PrintAssistant.instance.stringBetweenChar(String.valueOf(j), ' ', WIDTH_MARBLE + 1 , ' ', ' '));
        }
        rowsMarket.add(row.toString());
        PrintAssistant.instance.printfMultipleString(rowsMarket);
        return "";
    }
}
