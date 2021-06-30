package it.polimi.ingsw.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.PrintAssistant;

import java.util.ArrayList;

public class DeckDevData {
    private final ArrayList<ArrayList<ArrayList<CardDevData>>> deck;

    @JsonIgnore
    private final int WIDTH = 180;
    @JsonIgnore
    private final int WIDTH_CARD = WIDTH / 4;

    @JsonCreator
    public DeckDevData(@JsonProperty("deck") ArrayList<ArrayList<ArrayList<CardDevData>>> deck) {
        this.deck = deck;
    }

    public CardDevData getCard(int row, int col) {
        CardDevData cardToSend = deck.get(row).get(col).get(0);
        deck.get(row).get(col).remove(0);
        return cardToSend;

    }

    public ArrayList<ArrayList<ArrayList<CardDevData>>> getDeck() {
        return deck;
    }

    public void removeCardDevData(int row, int col){
        deck.get(row).get(col).remove(0);
    }

    /**
     * @deprecated */
    public void reInsertCard(CardDevData card, int row, int col) {
        deck.get(row).get(col).add(0, card);
    }

    public boolean rowColValid(int row, int col) {
        if (row >= deck.size() || col >= deck.get(0).size() || row < 0 || col < 0)
            return false;
        return !deck.get(row).get(col).isEmpty();
    }

    private void printTitle() {
        String titleDeckDev = PrintAssistant.instance.stringBetweenChar("Deck Developer", ' ', WIDTH, ' ', ' ');
        PrintAssistant.instance.printf(titleDeckDev, PrintAssistant.ANSI_BLACK, PrintAssistant.ANSI_YELLOW_BACKGROUND);

    }

    private void printFirstLine() {
        StringBuilder firstLine = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            firstLine.append(PrintAssistant.instance.fitToWidth("", WIDTH_CARD, '_', ' ', ' '));
        }
        PrintAssistant.instance.printf(firstLine.toString());
    }

    private int emptyCard(ArrayList<String> rowsDeck, int writtenRow) {
        for (int k = 0; k < 8; k++) {
            rowsDeck.set(writtenRow, rowsDeck.get(writtenRow) + emptyLine());
            writtenRow++;
        }
        return writtenRow;
    }

    private String emptyLine(){
        return PrintAssistant.instance.fitToWidth("", WIDTH_CARD, ' ', '|', '|');
    }

    private String lastLine(){return PrintAssistant.instance.fitToWidth("", WIDTH_CARD, '_', '|', '|');}

    private String cardCostHeader() {
        return PrintAssistant.instance.fitToWidth("To buy you had to pay:", WIDTH_CARD, ' ', '|', '|');
    }

    private String cardProductionCostHeader() {
        return PrintAssistant.instance.fitToWidth("To make production you have to pay:", WIDTH_CARD, ' ', '|', '|');
    }

    private String cardProductionEarnHeader() {
        return PrintAssistant.instance.fitToWidth("You will earn from production:", WIDTH_CARD, ' ', '|', '|');
    }

    @Override
    public String toString() {

        printTitle();

        ArrayList<String> rowsDeck = new ArrayList<>();

        int writtenRow;

        int HEIGHT_CARD = 9;
        for (int i = 0; i < deck.size(); i++) {
            for (int j = 0; j < HEIGHT_CARD; j++) {
                rowsDeck.add("");
            }
        }

        for (int i = 0; i < deck.size(); i++) {
            if (i == 0) printFirstLine();

            for (int j = 0; j < deck.get(i).size(); j++) {
                writtenRow = i * HEIGHT_CARD;
                if (deck.get(i).get(j).isEmpty()) {
                    writtenRow = emptyCard(rowsDeck, writtenRow);
                } else {
                    CardDevData card = deck.get(i).get(j).get(0);
                    rowsDeck.set(writtenRow, rowsDeck.get(writtenRow) + card.cardHeader(WIDTH_CARD));
                    writtenRow++;

                    if (!deck.get(i).get(j).get(0).getResourceReq().isEmpty()) {
                        rowsDeck.set(writtenRow, rowsDeck.get(writtenRow) + cardCostHeader());
                        writtenRow++;

                        rowsDeck.set(writtenRow, rowsDeck.get(writtenRow) + card.cardCost(WIDTH_CARD));
                        writtenRow++;
                    }
                    if (!deck.get(i).get(j).get(0).getEffects().get(0).getResourcesBefore().isEmpty()) {

                        rowsDeck.set(writtenRow, rowsDeck.get(writtenRow) + cardProductionCostHeader());
                        writtenRow++;

                        rowsDeck.set(writtenRow, rowsDeck.get(writtenRow) + card.cardProductionCost(WIDTH_CARD));
                        writtenRow++;
                    }
                    if (!deck.get(i).get(j).get(0).getEffects().get(0).getResourcesAfter().isEmpty()) {
                        rowsDeck.set(writtenRow, rowsDeck.get(writtenRow) + cardProductionEarnHeader());
                        writtenRow++;

                        rowsDeck.set(writtenRow, rowsDeck.get(writtenRow) + card.cardProductionEarn(WIDTH_CARD));
                        writtenRow++;
                    }
                    rowsDeck.set(writtenRow, rowsDeck.get(writtenRow) + card.cardEnd(WIDTH_CARD));
                    writtenRow++;
                }

                if (i == deck.size() - 1)
                    rowsDeck.set(writtenRow, rowsDeck.get(writtenRow) + lastLine());
                else
                    rowsDeck.set(writtenRow, rowsDeck.get(writtenRow) + emptyLine());
            }
        }
        PrintAssistant.instance.printfMultipleString(rowsDeck);
        return "";
    }
}
