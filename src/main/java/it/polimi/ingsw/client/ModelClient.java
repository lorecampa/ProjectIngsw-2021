package it.polimi.ingsw.client;

import it.polimi.ingsw.client.data.*;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;

public class ModelClient {
    private final Integer NUMBER_OF_CELL_FAITH=10;
    private final Integer DIM_CELL_CHAR = 14;

    private final String username;
    private Integer currentPosOnFaithTrack;
    private ArrayList<FaithTrackData> faithTrack = new ArrayList<>();
    private ArrayList<ResourceData> standardDepot = new ArrayList<>();
    private ArrayList<ResourceData> leaderDepot = new ArrayList<>();
    private ArrayList<ResourceData> strongbox= new ArrayList<>();
    private ArrayList<ArrayList<CardDevData>> cardSlots = new ArrayList<>();
    private ArrayList<CardLeaderData> leaders = new ArrayList<>();

    //attributes to CLI
    int lengthInChar = DIM_CELL_CHAR*NUMBER_OF_CELL_FAITH;
    int widthColumn;

    public ModelClient(String username) {
        this.username = username;
        setUpForDebug();
        //realSetUp();
    }

    private void realSetUp(){
        this.currentPosOnFaithTrack=0;
        for(int i=0;i<3;i++){
            standardDepot.add(new ResourceData(ResourceType.ANY, 0));
        }
        strongbox.add(new ResourceData(ResourceType.COIN, 0));
        strongbox.add(new ResourceData(ResourceType.SERVANT, 0));
        strongbox.add(new ResourceData(ResourceType.STONE, 0));
        strongbox.add(new ResourceData(ResourceType.SHIELD, 0));

        for (int i = 0; i < 3; i++) {
            cardSlots.add(new ArrayList<>());
        }
    }

    private void setUpForDebug(){
        this.currentPosOnFaithTrack=6;
        for(int i=0;i<3;i++){
            standardDepot.add(new ResourceData(ResourceType.COIN, i+1));
        }
/*
        for(int i=0;i<3;i++){
            leaderDepot.add(new ResourceData(ResourceType.STONE, i+1));
        }

 */
        for(int i=0; i<25; i++){
            faithTrack.add(new FaithTrackData(i+1, (i%3==0? i: 0), (i==3||i==4||i==5||i==15||i==16||i==17)?true:false, (i==5||i==17)?true:false, i));
        }
        strongbox.add(new ResourceData(ResourceType.COIN, 12));
        strongbox.add(new ResourceData(ResourceType.SERVANT, 2));
        strongbox.add(new ResourceData(ResourceType.STONE, 1));
        strongbox.add(new ResourceData(ResourceType.SHIELD, 50));
        ArrayList<ResourceData> resourceReq=new ArrayList<>();

        ArrayList<ResourceData> cost=new ArrayList<>();
        ArrayList<ResourceData> earn=new ArrayList<>();
        ArrayList<ResourceData> cost1=new ArrayList<>();
        ArrayList<ResourceData> earn1=new ArrayList<>();
        resourceReq.add(new ResourceData(ResourceType.COIN, 2));
        resourceReq.add(new ResourceData(ResourceType.STONE, 2));

        cost.add(new ResourceData(ResourceType.SERVANT, 1));
        cost.add(new ResourceData(ResourceType.ANY, 2));
        cost.add(new ResourceData(ResourceType.SHIELD, 3));

        earn.add(new ResourceData(ResourceType.FAITH, 4));
        earn.add(new ResourceData(ResourceType.SHIELD, 2));

        cost1.add(new ResourceData(ResourceType.ANY,  1));
        earn1.add(new ResourceData(ResourceType.COIN,  2));


        EffectData effData = new EffectData("Prod", cost, earn);
        EffectData effData2 = new EffectData("Discount", cost, null);
        EffectData effData3 = new EffectData("Market", cost, earn1);
        ArrayList<EffectData> effectsD= new ArrayList<>();
        effectsD.add(effData);

        /*
        CardDevData cdd=new CardDevData(1, 2, ColorData.BLUE, resourceReq, cost, earn );
        CardDevData cdd1=new CardDevData(2, 5, ColorData.PURPLE, resourceReq, cost, earn );
         */

        for (int i = 0; i < 3; i++) {
            cardSlots.add(new ArrayList<>());
        }

        CardDevData cdd=new CardDevData(1, 2, ColorData.BLUE, resourceReq, effectsD);
        CardDevData cdd1=new CardDevData(2, 5, ColorData.PURPLE, resourceReq, effectsD);
        cardSlots.get(0).add(cdd);
        //cardSlots.get(0).add(cdd1);
        cardSlots.get(1).add(cdd);
        cardSlots.get(1).add(cdd1);
        //cardSlots.get(2).add(cdd);
        //cardSlots.get(2).add(cdd1);

        ArrayList<EffectData> effectsL= new ArrayList<>();
        effectsL.add(effData2);
        effectsL.add(effData);
        effectsL.add(effData3);

        ArrayList<String> effects=new ArrayList<>();
        effects.add("Extra Depot COIN 2");
        effects.add("Discount SHIELD 1");
        CardLeaderData cl=new CardLeaderData(4, cardSlots.get(0), cost, effectsL,false);
        CardLeaderData cl2=new CardLeaderData(4, cardSlots.get(0), cost, effectsL,true);
        leaders.add(cl);
        //leaders.add(cl2);
        //leaders.add(cl);
        leaders.add(cl);
    }


    private boolean canAddToCardSlot(ArrayList<CardDevData> cardSlot, CardDevData cardToAdd){
        return cardSlot.get(cardSlot.size() - 1).getLevel() < cardToAdd.getLevel();
    }

    public void addToCardSlot(int index, CardDevData card){
        if (canAddToCardSlot(cardSlots.get(index), card))
            cardSlots.get(index).add(card);
    }

    public String getUsername() {
        return username;
    }

    public void setFaithTrack(ArrayList<FaithTrackData> faithTrack) {
        this.faithTrack = faithTrack;
    }

    public void setStandardDepotAt(int index, ResourceData newDepot) {
        standardDepot.set(index, newDepot);
    }

    public void setLeaderDepotAt(int index, ResourceData newDepot) {
        leaderDepot.set(index, newDepot);
    }

    public void addLeaderDepot(ResourceData depotToAdd){
        leaderDepot.add(depotToAdd);
    }

    public void setStrongbox(ArrayList<ResourceData> strongbox) {
        this.strongbox = strongbox;
    }

    public void setLeaders(ArrayList<CardLeaderData> leaders) {
        this.leaders = leaders;
    }

    public void removeLeaderAt(int index){
        leaders.remove(index);
    }

    public void setActiveLeaderAt(int index){
        leaders.get(index).setActive(true);
    }

    public boolean validIndexForLeader(int index){
        return (index>=0 && index< leaders.size());
    }

    public void addToCurrPositionOnFaithTrack(int value){
        currentPosOnFaithTrack+=value;
    }

    public void printAll() {
        printFaithTrack();
        printResources();
        printCardSlots(true);
        printLeaders();
    }

    private void printTitle(String title){
        String titleToPrint =PrintAssistant.instance.stringBetweenChar(title, ' ', lengthInChar, ' ', ' ');
        PrintAssistant.instance.printf(titleToPrint, PrintAssistant.ANSI_BLACK, PrintAssistant.ANSI_YELLOW_BACKGROUND);
    }

    public void printFaithTrack(){
        printTitle(username+"'s Faith Track");

        int num = faithTrack.size();
        int cellAlreadyDraw=0;
        String colorBorderOut;
        String colorBorderIn;
        String victoryPoint;
        String numCell;
        String popeFavor;
        ArrayList<String> rowOfFaith=new ArrayList<>();
        rowOfFaith.add("");
        rowOfFaith.add("");
        rowOfFaith.add("");
        rowOfFaith.add("");
        rowOfFaith.add("");
        rowOfFaith.add("");
        rowOfFaith.add("");

        while (num!=0){
            rowOfFaith.set(0, "");
            rowOfFaith.set(1, "");
            rowOfFaith.set(2, "");
            rowOfFaith.set(3, "");
            rowOfFaith.set(4, "");
            rowOfFaith.set(5, "");
            rowOfFaith.set(6, "");
            int cellToDraw;
            if(num>NUMBER_OF_CELL_FAITH){
                cellToDraw=NUMBER_OF_CELL_FAITH;
                num-=NUMBER_OF_CELL_FAITH;
            }
            else{
                cellToDraw=num;
                num-=cellToDraw;
            }
            for(int i=0; i<cellToDraw; i++){

                if(faithTrack.get(i+cellAlreadyDraw).getVictoryPoints()==0){
                    victoryPoint="      ";
                }
                else{
                    victoryPoint="VP: "+PrintAssistant.instance.padRight(faithTrack.get(i+cellAlreadyDraw).getVictoryPoints()+"", 2);
                }

                if(faithTrack.get(i+cellAlreadyDraw).isPopeFavor()){
                    colorBorderIn=PrintAssistant.ANSI_RED;
                    popeFavor="PoFa("+PrintAssistant.instance.padRight(faithTrack.get(i+cellAlreadyDraw).getVictoryPopeFavor()+"", 2)+")";
                }
                else{
                    colorBorderIn=PrintAssistant.ANSI_RESET;
                    popeFavor="        ";
                }

                if(faithTrack.get(i+cellAlreadyDraw).isVaticanReport()){
                    colorBorderOut=PrintAssistant.ANSI_YELLOW;
                }
                else{
                    colorBorderOut=PrintAssistant.ANSI_RESET;
                }

                if(faithTrack.get(i+cellAlreadyDraw).getNumberofCell()==currentPosOnFaithTrack){
                    numCell=PrintAssistant.ANSI_PURPLE_BACKGROUND +PrintAssistant.ANSI_BLACK + PrintAssistant.instance.padRight(username.substring(0,1), 2) +PrintAssistant.ANSI_RESET+ colorBorderIn;

                }
                else{
                    numCell=PrintAssistant.instance.padRight(faithTrack.get(i+cellAlreadyDraw).getNumberofCell()+"", 2);
                }

                rowOfFaith.set(0, rowOfFaith.get(0)+colorBorderOut+" ____________ ");
                rowOfFaith.set(1, rowOfFaith.get(1)+colorBorderOut+"| "+colorBorderIn+" ________ "+colorBorderOut+" |");
                rowOfFaith.set(2, rowOfFaith.get(2)+colorBorderOut+"| "+colorBorderIn+"| "+victoryPoint+" |"+colorBorderOut+" |");
                rowOfFaith.set(3, rowOfFaith.get(3)+colorBorderOut+"| "+colorBorderIn+"|   "+numCell+"   |"+colorBorderOut+" |");
                rowOfFaith.set(4, rowOfFaith.get(4)+colorBorderOut+"| "+colorBorderIn+"|"+ popeFavor+"|"+colorBorderOut+" |");
                rowOfFaith.set(5, rowOfFaith.get(5)+colorBorderOut+"| "+colorBorderIn+"|________|"+colorBorderOut+" |");
                rowOfFaith.set(6, rowOfFaith.get(6)+colorBorderOut+"|____________|");
            }
            cellAlreadyDraw+=cellToDraw;
            rowOfFaith.set(6, rowOfFaith.get(6)+"\n");
            PrintAssistant.instance.printfMultipleString(rowOfFaith);
        }
    }

    //METHODS FOR PRINT RESOURCE
    private void printResourceHeader(){
        String titleLeaderDepot="";
        if(!leaderDepot.isEmpty()){
            widthColumn  = lengthInChar/3;
            titleLeaderDepot=PrintAssistant.instance.stringBetweenChar("LE.DEPOT", '_', widthColumn, ' ', ' ');
        }
        else{
            widthColumn = (lengthInChar/2);
        }

        String title=PrintAssistant.instance.stringBetweenChar("STRONBOX", '_', widthColumn, ' ', ' ');
        title+=PrintAssistant.instance.stringBetweenChar("ST.DEPOT", '_', widthColumn, ' ', ' ');
        title+=titleLeaderDepot;
        PrintAssistant.instance.printf(title);
    }

    private String resourceBoxEnd(){
        return PrintAssistant.instance.stringBetweenChar("END_", '_',widthColumn, '|','|');
    }

    private void strongboxToCli(ArrayList<String> rowsOfResources){
        String row;
        for(int i=0;i<strongbox.size();i++){
            row=(1+i)+")"+strongbox.get(i).getType()+": "+strongbox.get(i).getValue();
            row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', '|','|');
            rowsOfResources.add(row);
        }
        rowsOfResources.add(resourceBoxEnd());
    }

    private void standardDepotToCli(ArrayList<String> rowsOfResources){
        String row;
        int i;
        for(i=0; i<standardDepot.size(); i++){
            if(standardDepot.get(i).getType()==ResourceType.ANY){
                row=(1+i)+")"+"EMPTY";
            }
            else{
                row=(1+i)+")"+standardDepot.get(i).getType()+": "+standardDepot.get(i).getValue()+"/"+(i+1);
            }
            row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', '|','|');
            rowsOfResources.set(i, rowsOfResources.get(i)+row);
        }
        rowsOfResources.set(i, rowsOfResources.get(i)+resourceBoxEnd());
    }

    private void leaderDepotToCli(ArrayList<String> rowsOfResources){
        String row;
        int i;
        if(!leaderDepot.isEmpty()){
            for(i=0; i<leaderDepot.size(); i++){
                row=(1+i)+")"+leaderDepot.get(i).getType()+": "+leaderDepot.get(i).getValue()+"/"+(2);
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', '|','|');
                rowsOfResources.set(i, rowsOfResources.get(i)+row);
            }
            rowsOfResources.set(i, rowsOfResources.get(i)+resourceBoxEnd());
        }

        PrintAssistant.instance.printfMultipleString(rowsOfResources);
    }

    public void printResources(){
        printTitle(username+"'s Resources");

        ArrayList<String> rowsOfResources=new ArrayList<>();

        printResourceHeader();

        strongboxToCli(rowsOfResources);

        standardDepotToCli(rowsOfResources);

        leaderDepotToCli(rowsOfResources);
    }

    // METHODS FOR CARD SLOT
    private void printCardSlotLegend(){
        StringBuilder row;
        row = new StringBuilder("Legend resources:");
        row = new StringBuilder(PrintAssistant.instance.stringBetweenChar(row.toString(), ' ', lengthInChar, ' ', ' '));
        PrintAssistant.instance.printf(row.toString());

        ArrayList<ResourceData> resourceToShow=new ArrayList<>();
        resourceToShow.add(new ResourceData(ResourceType.COIN, 1));
        resourceToShow.add(new ResourceData(ResourceType.SHIELD, 1));
        resourceToShow.add(new ResourceData(ResourceType.STONE, 1));
        resourceToShow.add(new ResourceData(ResourceType.SERVANT, 1));
        resourceToShow.add(new ResourceData(ResourceType.FAITH, 1));
        resourceToShow.add(new ResourceData(ResourceType.ANY, 1));

        row = new StringBuilder();
        for(ResourceData r : resourceToShow){
            row.append(r.toCli()).append("= ").append(r.getType()).append("  ");
        }
        row = new StringBuilder(PrintAssistant.instance.stringBetweenChar(row.toString(), ' ', lengthInChar, ' ', ' '));
        PrintAssistant.instance.printf(row.toString());
    }

    private void printCardSlotHeader(){
        String title=PrintAssistant.instance.stringBetweenChar("CARD SLOT1", '_', widthColumn, ' ', ' ');
        title+=PrintAssistant.instance.stringBetweenChar("CARD SLOT2", '_', widthColumn, ' ', ' ');
        title+=PrintAssistant.instance.stringBetweenChar("CARD SLOT3", '_', widthColumn, ' ', ' ');
        PrintAssistant.instance.printf(title);
    }

    private String cardCostHeader(){
        return PrintAssistant.instance.fitToWidth("To buy you had to pay:", widthColumn, ' ', '|', '|');
    }

    private String cardProductionCostHeader(){
        return PrintAssistant.instance.fitToWidth("To make production you have to pay:", widthColumn, ' ', '|', '|');
    }

    private String cardProductionEarnHeader(){
        return PrintAssistant.instance.fitToWidth("You will earn from production:", widthColumn, ' ', '|', '|');
    }

    private String cardLastRow(){return PrintAssistant.instance.fitToWidth("", widthColumn, ' ', '|', '|');}

    private String cardEndRow(){return PrintAssistant.instance.stringBetweenChar("END*", '_', widthColumn, '|', '|');}

    private void emptyCardSlot(ArrayList<String> rowOfCardSlots){
        StringBuilder row;
        int writtenRow = 0;
        while (rowOfCardSlots.size() < 2){
            rowOfCardSlots.add("");
        }

        row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("Empty*", ' ', widthColumn, '|', '|'));
        rowOfCardSlots.set(0, rowOfCardSlots.get(0)+row);
        writtenRow++;
        row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("END*", '_', widthColumn, '|', '|'));
        rowOfCardSlots.set(1, rowOfCardSlots.get(1)+row);
        writtenRow++;

        fillRestOfRows(rowOfCardSlots,writtenRow);
    }

    private void fillRestOfRows(ArrayList<String> rowOfCardSlots, int start){
        for (int i = start; i < rowOfCardSlots.size(); i++) {
            rowOfCardSlots.set(i, rowOfCardSlots.get(i) + PrintAssistant.instance.generateAStringOf(' ', widthColumn));
        }
    }

    private void setUpForCardSlot(ArrayList<String> rowOfCardSlots, int indexOfCardSlot, int indexOfCard, int size, boolean minimize){
        String row = PrintAssistant.instance.generateAStringOf(' ', widthColumn * indexOfCardSlot);

        int LENGTH_OF_DEV = 9;
        if (minimize){
            int numOfRow;
            int LENGTH_OF_MIN_DEV = 2;
            if (indexOfCard < size - 1)
                numOfRow = (indexOfCard + 1) * LENGTH_OF_MIN_DEV;
            else
                numOfRow = indexOfCard * LENGTH_OF_MIN_DEV + LENGTH_OF_DEV;
            while(rowOfCardSlots.size() <= numOfRow){
                rowOfCardSlots.add(row);
            }
        }else {
            while (rowOfCardSlots.size() <= (indexOfCard + 1) * LENGTH_OF_DEV) {
                rowOfCardSlots.add(row);
            }
        }
    }

    private void cardSlotToCli(ArrayList<String> rowOfCardSlots,int indexOfCardSlot, ArrayList<CardDevData> cardSlot, boolean minimize){
        int totalWrittenRow = 0;
        if(cardSlot.isEmpty()){
            emptyCardSlot(rowOfCardSlots);
        }
        else{
            for(int i=0; i < cardSlot.size(); i++) {
                setUpForCardSlot(rowOfCardSlots, indexOfCardSlot, i, cardSlot.size(), minimize);

                rowOfCardSlots.set(totalWrittenRow, rowOfCardSlots.get(totalWrittenRow) + cardSlot.get(i).cardHeader(widthColumn));
                totalWrittenRow++;

                if (!minimize || i == cardSlot.size() - 1) {
                    if (!cardSlot.get(i).getResourceReq().isEmpty()) {

                        rowOfCardSlots.set(totalWrittenRow, rowOfCardSlots.get(totalWrittenRow) + cardCostHeader());
                        totalWrittenRow++;

                        rowOfCardSlots.set(totalWrittenRow, rowOfCardSlots.get(totalWrittenRow) + cardSlot.get(i).cardCost(widthColumn));
                        totalWrittenRow++;
                    }
                    if (!cardSlot.get(i).getEffects().get(0).getResourcesBefore().isEmpty()) {

                        rowOfCardSlots.set(totalWrittenRow, rowOfCardSlots.get(totalWrittenRow) + cardProductionCostHeader());
                        totalWrittenRow++;

                        rowOfCardSlots.set(totalWrittenRow, rowOfCardSlots.get(totalWrittenRow) + cardSlot.get(i).cardProductionCost(widthColumn));
                        totalWrittenRow++;
                    }
                    if (!cardSlot.get(i).getEffects().get(0).getResourcesAfter().isEmpty()) {

                        rowOfCardSlots.set(totalWrittenRow, rowOfCardSlots.get(totalWrittenRow) + cardProductionEarnHeader());
                        totalWrittenRow++;

                        rowOfCardSlots.set(totalWrittenRow, rowOfCardSlots.get(totalWrittenRow) + cardSlot.get(i).cardProductionEarn(widthColumn));
                        totalWrittenRow++;
                    }
                    rowOfCardSlots.set(totalWrittenRow, rowOfCardSlots.get(totalWrittenRow) + cardSlot.get(i).cardEnd(widthColumn));
                    totalWrittenRow++;
                }
                rowOfCardSlots.set(totalWrittenRow, rowOfCardSlots.get(totalWrittenRow) + cardLastRow());
                totalWrittenRow++;
                //totalWrittenRow += totalWrittenRow;
            }
            rowOfCardSlots.set(totalWrittenRow, rowOfCardSlots.get(totalWrittenRow)+cardEndRow());
            totalWrittenRow ++;
            fillRestOfRows(rowOfCardSlots,totalWrittenRow);
        }
    }

    public void printCardSlots(boolean minimize){
        printTitle(username+"'s Developments");

        widthColumn = lengthInChar/3;

        printCardSlotLegend();

        printCardSlotHeader();

        ArrayList<String> rowOfCardSlots=new ArrayList<>(); //parte dalla prima riga utile!!!

        for (int i = 0; i < cardSlots.size(); i++) {
            cardSlotToCli(rowOfCardSlots,i,cardSlots.get(i),minimize);
        }

        PrintAssistant.instance.printfMultipleString(rowOfCardSlots);
    }

    //METHODS FOR LEADER
    private void printLeaderLegend(){
        PrintAssistant.instance.printf(PrintAssistant.instance.stringBetweenChar("Legend leader:", ' ', lengthInChar, ' ', ' '));
        String row=PrintAssistant.ANSI_GREEN_BACKGROUND+" Active Leader "+PrintAssistant.ANSI_RESET+" "+PrintAssistant.ANSI_WHITE_BACKGROUND+" Not Active Leader "+PrintAssistant.ANSI_RESET+PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
        row = PrintAssistant.instance.stringBetweenChar(row, ' ', lengthInChar, ' ', ' ');
        PrintAssistant.instance.printf(row);
    }

    private String leaderCostHeader(){
        return PrintAssistant.instance.fitToWidth("To activate you have to own:", widthColumn, ' ', ' ', ' ');
    }

    private String leaderEffectHeader(){
        return PrintAssistant.instance.fitToWidth("Effects:", widthColumn, ' ', ' ', ' ');
    }

    private void setUpForLeader(ArrayList<String> rowsOfLeaders, int leaderIndex){
        String row = PrintAssistant.instance.generateAStringOf(' ', (leaderIndex % 2 * widthColumn) - 1);
        int DIM_OF_LEADER = 7;
        while(rowsOfLeaders.size()<= DIM_OF_LEADER + leaders.get(leaderIndex).getEffects().size()){
            rowsOfLeaders.add(row);
        }
    }

    private void leaderToCli(ArrayList<String> rowsOfLeaders,int leaderIndex, int leadersSize){
        int writtenRow = 0;
        setUpForLeader(rowsOfLeaders,leaderIndex);

        rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+leaders.get(leaderIndex).leaderHeader(widthColumn, leaderIndex, leadersSize));
        writtenRow++;

        if(!leaders.get(leaderIndex).getCardReq().isEmpty()){
            rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+leaderCostHeader());
            writtenRow++;

            rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+leaders.get(leaderIndex).leaderCardReq(widthColumn,leaderIndex,leadersSize));
            writtenRow++;
        }

        if(!leaders.get(leaderIndex).getResourceReq().isEmpty()){
            rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+leaderCostHeader());
            writtenRow++;

            rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+leaders.get(leaderIndex).leaderResReq(widthColumn,leaderIndex,leadersSize));
            writtenRow++;
        }

        if(!leaders.get(leaderIndex).getEffects().isEmpty()){
            rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+leaderEffectHeader());
            writtenRow++;

            writtenRow = leaders.get(leaderIndex).leaderEffect(widthColumn,leaderIndex,leadersSize,rowsOfLeaders, writtenRow);
        }

        rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+leaders.get(leaderIndex).leaderEnd(widthColumn,leaderIndex,leadersSize));
    }

    public void printLeaders(){
        if(leaders.isEmpty())
            return;

        printTitle(username+"'s Leaders");

        widthColumn = (lengthInChar/2);

        printLeaderLegend();

        int leadersSize = leaders.size();
        ArrayList<String> rowsOfLeaders = new ArrayList<>();

        for (int i = 0; i < leadersSize; i++) {
            leaderToCli(rowsOfLeaders,i,leadersSize);
            if (i == 1 || i == leadersSize - 1){
                PrintAssistant.instance.printfMultipleString(rowsOfLeaders);
                rowsOfLeaders.clear();
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null)
            return false;
        if(obj==this)
            return true;
        if(obj instanceof String && obj.equals(username))
            return true;
        if(!(obj instanceof ModelClient))
            return false;
        return ((ModelClient) obj).getUsername().equalsIgnoreCase(username);
    }
}
