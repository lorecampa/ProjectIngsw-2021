package it.polimi.ingsw.client.data;

import it.polimi.ingsw.client.PrintAssistant;

import java.util.ArrayList;

public class DeckDevData {
    private ArrayList<ArrayList<ArrayList<CardDevData>>> deck;

    public DeckDevData(ArrayList<ArrayList<ArrayList<CardDevData>>> deck) {
        this.deck = deck;
    }

    public CardDevData getCard(int row, int col){
        return deck.get(row).get(col).get(0);
    }

    /*
    public static void main(String[] args) {
        ArrayList<ResourceData> resourceReq = new ArrayList<>();
        ArrayList<ResourceData> cost = new ArrayList<>();
        ArrayList<ResourceData> earn = new ArrayList<>();
        resourceReq.add(new ResourceData(ResourceType.COIN, 2));
        resourceReq.add(new ResourceData(ResourceType.STONE, 2));
        cost.add(new ResourceData(ResourceType.SERVANT, 1));
        cost.add(new ResourceData(ResourceType.ANY, 2));
        earn.add(new ResourceData(ResourceType.FAITH, 4));
        earn.add(new ResourceData(ResourceType.SHIELD, 2));
        CardDevData cdd=new CardDevData(1, 2, Color.BLUE, resourceReq, cost, earn );
        //CardDevData cdd1=new CardDevData(2, 5, Color.PURPLE, resourceReq, cost, earn );


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
                    Color color=Color.ANY;
                    switch(j){
                        case  0:
                            color=Color.BLUE;
                            break;
                        case 1:
                            color=Color.PURPLE;
                            break;
                        case 2:
                            color=Color.YELLOW;
                            break;
                        case 3:
                            color=Color.GREEN;
                            break;
                    }
                    pippo.add(new CardDevData(i+1, 2, color, resourceReq, cost, earn ));

                }
                row.add(pippo);
            }
            deckDevelopment.add(row);
        }
        DeckDevData mioDeck= new DeckDevData(deckDevelopment);
        System.out.println(mioDeck.toString());


    }
    */
    @Override
    public String toString() {
        //SE UN MAZZETTO E' VUOTO ESPLODE TUTTO
        int width=174;
        int widthCard = 180/4;
        int OFFSET_AllIGN=3;
        String titleDeckDev= PrintAssistant.instance.stringBetweenChar("Deck Developer", ' ',width , ' ', ' ');
        PrintAssistant.instance.printf(titleDeckDev, PrintAssistant.ANSI_BLACK, PrintAssistant.ANSI_YELLOW_BACKGROUND);
        ArrayList<String> rowsDeck= new ArrayList<>();
        StringBuilder row= new StringBuilder();
        int writtenRow=0;
        String firstLine="";
        for(int i=0;i<deck.size(); i++){
            for(int j=0; j<9; j++) {
                rowsDeck.add("");
            }
        }

        for(int i=0;i<deck.size(); i++){

            for(int j=0; j<deck.get(i).size(); j++){
                if(i==0){
                    firstLine += PrintAssistant.instance.fitToWidth("", widthCard, '_', ' ', ' ', OFFSET_AllIGN);
                    if(j==deck.get(i).size()-1)
                        PrintAssistant.instance.printf(firstLine);
                }

                writtenRow=i*9;
                row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("CARD LV" + deck.get(i).get(j).get(0).getLevel() + " +" + deck.get(i).get(j).get(0).getVictoryPoints() + "VP ", ' ', widthCard - 2, ' ', ' '));
                row = new StringBuilder("|" + deck.get(i).get(j).get(0).getColorToColor() + row + PrintAssistant.ANSI_RESET + "|");
                rowsDeck.set(indexCardSlot(i, writtenRow), rowsDeck.get(indexCardSlot(i, writtenRow))+row);
                writtenRow++;
                if(!deck.get(i).get(j).get(0).getResourceReq().isEmpty()){
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth("To buy you had to pay:", widthCard, ' ', '|', '|', OFFSET_AllIGN));
                    rowsDeck.set(indexCardSlot(i, writtenRow), rowsDeck.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                    row = new StringBuilder();
                    for(ResourceData s : deck.get(i).get(j).get(0).getResourceReq()){

                        row.append(s.toCli());
                    }
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthCard, ' ', '|', '|', OFFSET_AllIGN));
                    rowsDeck.set(indexCardSlot(i, writtenRow), rowsDeck.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                }
                if(!deck.get(i).get(j).get(0).getProductionCost().isEmpty()){
                    row = new StringBuilder("To make production you have to pay:");
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthCard, ' ', '|', '|', OFFSET_AllIGN));
                    rowsDeck.set(indexCardSlot(i, writtenRow), rowsDeck.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                    row = new StringBuilder();
                    for(ResourceData s : deck.get(i).get(j).get(0).getProductionCost()){
                        row.append(s.toCli());
                    }
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthCard, ' ', '|', '|', OFFSET_AllIGN));
                    rowsDeck.set(indexCardSlot(i, writtenRow), rowsDeck.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                }
                if(!deck.get(i).get(j).get(0).getProductionEarn().isEmpty()){
                    row = new StringBuilder("You will earn from production:");
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthCard, ' ', '|', '|', OFFSET_AllIGN));
                    rowsDeck.set(indexCardSlot(i, writtenRow), rowsDeck.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                    row = new StringBuilder();
                    for(ResourceData s : deck.get(i).get(j).get(0).getProductionEarn()){
                        row.append(s.toCli());
                    }
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthCard, ' ', '|', '|', OFFSET_AllIGN));
                    rowsDeck.set(indexCardSlot(i, writtenRow), rowsDeck.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                }
                row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("END CARD", ' ', widthCard - 2, ' ', ' '));
                row = new StringBuilder("|" + deck.get(i).get(j).get(0).getColorToColor() + row + PrintAssistant.ANSI_RESET + "|");
                rowsDeck.set(indexCardSlot(i, writtenRow), rowsDeck.get(indexCardSlot(i, writtenRow))+row);
                writtenRow++;
                if(i==deck.size()-1)
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth("", widthCard, '_', '|', '|', OFFSET_AllIGN));
                else
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth("", widthCard, ' ', '|', '|', OFFSET_AllIGN));

                rowsDeck.set(indexCardSlot(i, writtenRow), rowsDeck.get(indexCardSlot(i, writtenRow))+row);
                //writtenRow++;
            }
        }
        PrintAssistant.instance.printfMultipleString(rowsDeck);
        return "";
    }

    private int indexCardSlot(int i, int writtenRow){
        //return (i*9)+writtenRow;
        return writtenRow;
    }
}
