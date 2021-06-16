package it.polimi.ingsw.client;

import it.polimi.ingsw.client.data.*;
import it.polimi.ingsw.model.resource.ResourceType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class ModelClient {
    private final Integer NUMBER_OF_CELL_FAITH=10;
    private final Integer DIM_CELL_CHAR = 14;

    private final String username;
    private boolean inkwell;
    private Integer currentPosOnFaithTrack;
    private ArrayList<FaithTrackData> faithTrack = new ArrayList<>();

    private ArrayList<ResourceData> standardDepot = new ArrayList<>();
    private ArrayList<ResourceData> leaderDepot = new ArrayList<>();
    private ArrayList<Integer> maxStoreLeaderDepot= new ArrayList<>();

    private ArrayList<ResourceData> strongbox= new ArrayList<>();

    private ArrayList<ArrayList<CardDevData>> cardSlots = new ArrayList<>();
    private ArrayList<EffectData> baseProduction;
    private ArrayList<CardLeaderData> leaders = new ArrayList<>();
    private final ArrayList<ResourceData> discounts = new ArrayList<>();

    private final LinkedHashMap<String, String> errorMessagesLog = new LinkedHashMap<>();

    //attributes to CLI
    int lengthInChar = DIM_CELL_CHAR*NUMBER_OF_CELL_FAITH;
    int widthColumn;

    public ModelClient(String username) {
        this.username = username;
        //setUpForDebug();
        realSetUp();
    }

    /**
     * Set up to start a real client
     * */
    private void realSetUp(){
        this.currentPosOnFaithTrack=0;
        for(int i=0;i<3;i++){
            standardDepot.add(new ResourceData(ResourceType.ANY, 0));
        }
        //TODO change to standard values
        strongbox.add(new ResourceData(ResourceType.COIN, 20));
        strongbox.add(new ResourceData(ResourceType.SERVANT, 20));
        strongbox.add(new ResourceData(ResourceType.STONE, 20));
        strongbox.add(new ResourceData(ResourceType.SHIELD, 20));

        for (int i = 0; i < 3; i++) {
            cardSlots.add(new ArrayList<>());
        }
    }

    /**
     * Set up to test and debug client side
     * @deprecated
     * */
    private void setUpForDebug(){
        this.currentPosOnFaithTrack=8;
        for(int i=0;i<3;i++){
            standardDepot.add(new ResourceData(ResourceType.STONE, i));
        }

        for(int i=0; i<25; i++){
            faithTrack.add(new FaithTrackData(i+1, (i%3==0? i: -1), i == 3 || i == 4 || i == 5 || i == 15 || i == 16 || i == 17, i == 5 || i == 17, i,false));
        }
        strongbox.add(new ResourceData(ResourceType.COIN, 12));
        strongbox.add(new ResourceData(ResourceType.SERVANT, 2));
        strongbox.add(new ResourceData(ResourceType.STONE, 1));
        strongbox.add(new ResourceData(ResourceType.SHIELD, 50));
        ArrayList<ResourceData> resourceReq=new ArrayList<>();

        maxStoreLeaderDepot.add(2);
        maxStoreLeaderDepot.add(2);
        maxStoreLeaderDepot.add(2);

        ArrayList<ResourceData> cost=new ArrayList<>();
        ArrayList<ResourceData> earn=new ArrayList<>();
        ArrayList<ResourceData> cost1=new ArrayList<>();
        ArrayList<ResourceData> cost2=new ArrayList<>();
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

        cost2.add(new ResourceData(ResourceType.COIN,  2));


        EffectData effData = new EffectData(EffectType.PRODUCTION,"Prod", cost, earn);
        EffectData effData2 = new EffectData(EffectType.DISCOUNT,"Discount", cost, null);
        EffectData effData3 = new EffectData(EffectType.MARBLE,"Market", cost1, earn1);
        EffectData effData4 = new EffectData(EffectType.WAREHOUSE,"Warehouse", cost2, null);
        ArrayList<EffectData> effectsD= new ArrayList<>();
        effectsD.add(effData);

        baseProduction = new ArrayList<>();
        baseProduction.add(effData);

        for (int i = 0; i < 3; i++) {
            cardSlots.add(new ArrayList<>());
        }

        CardDevData cdd=new CardDevData(1,1, 2, ColorData.BLUE, resourceReq, effectsD);
        CardDevData cdd1=new CardDevData(2,2, 5, ColorData.PURPLE, resourceReq, effectsD);
        cardSlots.get(0).add(cdd);
        cardSlots.get(0).add(cdd1);
        cardSlots.get(1).add(cdd);
        cardSlots.get(1).add(cdd1);
        cardSlots.get(2).add(cdd);
        cardSlots.get(2).add(cdd1);

        ArrayList<EffectData> effectsL= new ArrayList<>();
        effectsL.add(effData);

        ArrayList<EffectData> effectsL2= new ArrayList<>();
        effectsL2.add(effData);


        CardLeaderData cl=new CardLeaderData(57,4, cardSlots.get(0), cost, effectsL,false);
        CardLeaderData cl2=new CardLeaderData(58, 4, cardSlots.get(0), cost, effectsL2,true);
        leaders.add(cl);
        leaders.add(cl2);
        //leaders.add(cl);
        //leaders.add(cl);

        leaderDepot.add(new ResourceData(ResourceType.COIN,2));
    }

    /**
     * Add a CardDev to index card slot
     * @param index of card slot i want to insert into
     * @param card i want to insert
     * */
    public void addToCardSlot(int index, CardDevData card){
        cardSlots.get(index).add(card);
    }

    /**
     * Return username of curr client
     * @return username of curr client
     * */
    public String getUsername() {
        return username;
    }

    public void setFaithTrack(ArrayList<FaithTrackData> faithTrack) {
        this.faithTrack = faithTrack;
    }

    public void setCurrentPosOnFaithTrack(Integer currentPosOnFaithTrack) {
        this.currentPosOnFaithTrack = currentPosOnFaithTrack;
    }

    /**
     * Increase current position on faith track  by one
     * */
    public void increaseCurrentPosOnFaithTrack(){currentPosOnFaithTrack++;}

    /**
     * Activate a vatican report
     * @param idVaticanReport activated
     * @param discard true if i have to discard it, false otherwise
     * */
    public void popeFavorActivation(int idVaticanReport, boolean discard){
        int num = 0;
        if (!discard) {
            for (FaithTrackData cell : faithTrack) {
                if (cell.isPopeFavor()) {
                    if (num == idVaticanReport){
                        cell.setAcquired(true); return;
                    }
                    num++;
                }
            }
        }
    }

    public void setStandardDepot(ArrayList<ResourceData> standardDepot) {
        this.standardDepot = standardDepot;
    }

    public void setCardSlots(ArrayList<ArrayList<CardDevData>> cardSlots) {
        this.cardSlots = cardSlots;
    }

    public void setLeaderDepot(ArrayList<ResourceData> leaderDepot) {
        this.leaderDepot = leaderDepot;
    }

    public void setMaxStoreLeaderDepot(ArrayList<Integer> maxStoreLeaderDepot) {
        this.maxStoreLeaderDepot = maxStoreLeaderDepot;
    }

    public void setStandardDepotAt(int index, ResourceData newDepot) {
        standardDepot.set(index, newDepot);
    }

    public void setBaseProduction(ArrayList<EffectData> baseProduction) {
        this.baseProduction = baseProduction;
    }

    public void setInkwell(boolean inkwell) {
        this.inkwell = inkwell;
    }

    public void setLeaderDepotAt(int index, ResourceData newDepot) {
        leaderDepot.set(index, newDepot);
    }

    /**
     * Add a leader depot to model data
     * @param depotToAdd as resourceData
     * */
    public void addLeaderDepot(ResourceData depotToAdd){
        leaderDepot.add(new ResourceData(depotToAdd.getType(), 0));
        maxStoreLeaderDepot.add(depotToAdd.getValue());
    }

    /**
     * Remove a leader depot to model data
     * @param depotToRemove as ResourceData
     * @deprecated
     * */
    public void removeLeaderDepot(ResourceData depotToRemove){
        OptionalInt depotIndex =  leaderDepot.stream()
                .filter(x -> x.getType() == depotToRemove.getType() &&
                maxStoreLeaderDepot.get(leaderDepot.indexOf(x)) == depotToRemove.getValue())
                .mapToInt(leaderDepot::indexOf).findFirst();

        depotIndex.ifPresent(leaderDepot::remove);
        depotIndex.ifPresent(maxStoreLeaderDepot::remove);

    }

    /**
     * Add a discount
     * @param discountToAdd as resourceData
     * */
    public void addDiscount(ResourceData discountToAdd){
        discounts.add(discountToAdd);
    }
    /**
     * Remove a discount
     * @param discountToRemove as resourceData
     * @deprecated
     * */
    public void removeDiscount(ResourceData discountToRemove){
        OptionalInt discountIndex = discounts.stream()
                .filter(x -> x.getType() == discountToRemove.getType() &&
                        x.getValue() == discountToRemove.getValue()).mapToInt(discounts::indexOf).findFirst();
        discountIndex.ifPresent(discounts::remove);
    }

    /**
     * Return true if exist a leader depot at index
     * @param index i'm looking for
     * @return true if exist a leader depot at index
     * */
    public boolean isValidIndexDepotLeader(int index){
        return index>=0 && index<leaderDepot.size();
    }

    public void setStrongbox(ArrayList<ResourceData> strongbox) {
        this.strongbox = strongbox;
    }

    public void setLeaders(ArrayList<CardLeaderData> leaders) {
        this.leaders = leaders;
    }

    /**
     * Remove a leader card from your model
     * @param index of leader to remove
     * */
    public void removeLeaderAt(int index){
        leaders.remove(index);
    }

    /**
     * Set active leader at index
     * @param index of leader to activate
     * */
    public void setActiveLeaderAt(int index){
        leaders.get(index).setActive(true);
    }

    /**
     * Set list of current active leader in these model data
     * @param cards leader i have active
     * */
    public void putAsActiveInLeader(ArrayList<CardLeaderData> cards){
        leaders.clear();
        leaders.addAll(cards.stream().filter(CardLeaderData::isActive).collect(Collectors.toCollection(ArrayList::new)));
    }

    /**
     * Return true if exist a leader at index
     * @param index i'm looking for
     * @return true if exist a leader at index
     * */
    public boolean validIndexForLeader(int index){
        return (index>=0 && index< leaders.size());
    }

    /**
     * Add a value to faith track
     * @param value to add to currPosition
     * @deprecated
     * */
    public void addToCurrPositionOnFaithTrack(int value){
        currentPosOnFaithTrack+=value;
    }

    /**
     * Print all the personal board of curr player
     * */
    public void printAll() {
        printFaithTrack();
        if(!username.equalsIgnoreCase("lorenzoilmagnifico")){
            PrintAssistant.instance.printf("\n");
            printResources();
            PrintAssistant.instance.printf("\n");
            printCardSlots(true);
            PrintAssistant.instance.printf("\n");
            printLeaders();
        }
    }

    /**
     * Print the title in black with yellow background
     * @param title to print
     * */
    private void printTitle(String title){
        StringBuilder newTitle= new StringBuilder(title);
        if(inkwell){
            newTitle.insert(0, "Inkwell | ");
        }
        String titleToPrint =PrintAssistant.instance.stringBetweenChar(newTitle.toString(), ' ', lengthInChar, ' ', ' ');
        PrintAssistant.instance.printf(titleToPrint, PrintAssistant.ANSI_BLACK, PrintAssistant.ANSI_YELLOW_BACKGROUND);
    }

    /**
     * Print faith track of curr player
     * */
    public void printFaithTrack(){
        printTitle(username+"'s Faith Track");

        int CELL_HEIGHT = 7;
        int num = faithTrack.size();
        int cellAlreadyDraw=0;
        String colorBorderOut;
        String colorBorderIn;
        String victoryPoint;
        String numCell;
        String popeFavor;
        ArrayList<String> rowOfFaith=new ArrayList<>();
        for (int i = 0; i < CELL_HEIGHT; i++) {
            rowOfFaith.add("");
        }

        while (num!=0){
            for (int i = 0; i < CELL_HEIGHT; i++) {
                rowOfFaith.set(i,"");
            }
            int cellToDraw;
            if(num>NUMBER_OF_CELL_FAITH){
                cellToDraw=NUMBER_OF_CELL_FAITH;
                num-=NUMBER_OF_CELL_FAITH;
            }
            else{
                cellToDraw = num;
                num = 0;
            }
            for(int i=0; i<cellToDraw; i++){
                FaithTrackData cell = faithTrack.get(i+cellAlreadyDraw);

                victoryPoint = cell.cardVictoryPoint();

                colorBorderIn = cell.cardPopeFavorColor();

                popeFavor = cell.cardPopeFavor();

                colorBorderOut = cell.cardVaticanReportColor();

                if(cell.getNumberOfCell()==currentPosOnFaithTrack)
                    numCell=PrintAssistant.ANSI_PURPLE_BACKGROUND +PrintAssistant.ANSI_BLACK + PrintAssistant.instance.padRight(username.substring(0,1), 2) +PrintAssistant.ANSI_RESET+ colorBorderIn;
                else
                    numCell=PrintAssistant.instance.padRight(faithTrack.get(i+cellAlreadyDraw).getNumberOfCell()+"", 2);

                rowOfFaith.set(0, rowOfFaith.get(0)+colorBorderOut+" ____________ ");
                rowOfFaith.set(1, rowOfFaith.get(1)+colorBorderOut+"| "+colorBorderIn+" ________ "+colorBorderOut+" |");
                rowOfFaith.set(2, rowOfFaith.get(2)+colorBorderOut+"| "+colorBorderIn+"| "+victoryPoint+" |"+colorBorderOut+" |");
                rowOfFaith.set(3, rowOfFaith.get(3)+colorBorderOut+"| "+colorBorderIn+"|   "+numCell+"   |"+colorBorderOut+" |");
                rowOfFaith.set(4, rowOfFaith.get(4)+colorBorderOut+"| "+colorBorderIn+"|"+ popeFavor+colorBorderIn+"|"+colorBorderOut+" |");
                rowOfFaith.set(5, rowOfFaith.get(5)+colorBorderOut+"| "+colorBorderIn+"|________|"+colorBorderOut+" |");
                rowOfFaith.set(6, rowOfFaith.get(6)+colorBorderOut+"|____________|");
            }
            cellAlreadyDraw+=cellToDraw;
            PrintAssistant.instance.printfMultipleString(rowOfFaith);
        }
    }

    //METHODS FOR PRINT RESOURCE
    /**
     * Print the header of resources, name of container of resources
     * */
    private void printResourceHeader(){
        String titleLeaderDepot="";
        if(!leaderDepot.isEmpty()){
            widthColumn  = lengthInChar/3;
            titleLeaderDepot=PrintAssistant.instance.stringBetweenChar("LE.DEPOT", '_', widthColumn, ' ', ' ');
        }
        else{
            widthColumn = (lengthInChar/2);
        }

        String title=PrintAssistant.instance.stringBetweenChar("STRONGBOX", '_', widthColumn, ' ', ' ');
        title+=PrintAssistant.instance.stringBetweenChar("ST.DEPOT", '_', widthColumn, ' ', ' ');
        title+=titleLeaderDepot;
        PrintAssistant.instance.printf(title);
    }

    /**
     * Return the last line of resource box
     * */
    private String resourceBoxEnd(){
        return PrintAssistant.instance.stringBetweenChar("END_", '_',widthColumn, '|','|');
    }

    /**
     * Add to rowsOfResources the strongbox in strings
     * @param rowsOfResources where i add strings
     * */
    private void strongboxToCli(ArrayList<String> rowsOfResources){
        String row;
        for(int i=0;i<strongbox.size();i++){
            row=(1+i)+")"+strongbox.get(i).getType()+": "+strongbox.get(i).getValue();
            row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', '|','|');
            rowsOfResources.add(row);
        }
        rowsOfResources.add(resourceBoxEnd());
    }

    /**
     * Add to rowsOfResources the standard depot in strings
     * @param rowsOfResources where i add strings
     * */
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

    /**
     * Add to rowsOfResources the leader depot in strings
     * @param rowsOfResources where i add strings
     * */
    private void leaderDepotToCli(ArrayList<String> rowsOfResources){
        String row;
        int i;
        if(!leaderDepot.isEmpty()){
            for(i=0; i<leaderDepot.size(); i++){
                row=(1+i)+")"+leaderDepot.get(i).getType()+": "+leaderDepot.get(i).getValue()+"/"+maxStoreLeaderDepot.get(i);
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', '|','|');
                rowsOfResources.set(i, rowsOfResources.get(i)+row);
            }
            rowsOfResources.set(i, rowsOfResources.get(i)+resourceBoxEnd());
        }

        PrintAssistant.instance.printfMultipleString(rowsOfResources);
    }

    /**
     * Print resources of curr player
     * */
    public void printResources(){
        printTitle(username+"'s Resources");

        ArrayList<String> rowsOfResources=new ArrayList<>();

        printResourceHeader();

        strongboxToCli(rowsOfResources);

        standardDepotToCli(rowsOfResources);

        leaderDepotToCli(rowsOfResources);
    }

    // METHODS FOR CARD SLOT
    /**
     * Print the card slot legends, and the base production
     * */
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

        //print base production
        row = new StringBuilder("Base Production");
        row = new StringBuilder(PrintAssistant.instance.stringBetweenChar(row.toString(), ' ', lengthInChar, ' ', ' '));
        PrintAssistant.instance.printf(row.toString());
        for(EffectData ef: baseProduction){
            row = new StringBuilder(PrintAssistant.instance.stringBetweenChar(ef.resourceBeforeToCli()+">>> "+ef.resourceAfterToCli(), ' ', lengthInChar, ' ', ' '));
            PrintAssistant.instance.printf(row.toString());
        }

    }

    /**
     * Print the header for card slots
     * */
    private void printCardSlotHeader(){
        String title=PrintAssistant.instance.stringBetweenChar("CARD SLOT1", '_', widthColumn, ' ', ' ');
        title+=PrintAssistant.instance.stringBetweenChar("CARD SLOT2", '_', widthColumn, ' ', ' ');
        title+=PrintAssistant.instance.stringBetweenChar("CARD SLOT3", '_', widthColumn, ' ', ' ');
        PrintAssistant.instance.printf(title);
    }

    /**
     * Return sentence before the cost in each card
     * @return sentence before the cost in each card
     * */
    private String cardCostHeader(){
        return PrintAssistant.instance.fitToWidth("To buy you had to pay:", widthColumn, ' ', '|', '|');
    }
    /**
     * Return sentence before the production cost in developer cards
     * @return sentence before the production cost in developer cards
     * */
    private String cardProductionCostHeader(){
        return PrintAssistant.instance.fitToWidth("To make production you have to pay:", widthColumn, ' ', '|', '|');
    }
    /**
     * Return sentence before the production earn in developer cards
     * @return sentence before the production earn in developer cards
     * */
    private String cardProductionEarnHeader(){
        return PrintAssistant.instance.fitToWidth("You will earn from production:", widthColumn, ' ', '|', '|');
    }

    /**
     *Return the last row of a card
     * @return the last row of a card
     * */
    private String cardLastRow(){return PrintAssistant.instance.fitToWidth("", widthColumn, ' ', '|', '|');}
    /**
     *Return the last row of a card slot
     * @return the last row of a card slot
     * */
    private String cardEndRow(){return PrintAssistant.instance.stringBetweenChar("END*", '_', widthColumn, '|', '|');}

    /**
     * Add to rowOfCardSlots the empty card slot info
     * @param rowOfCardSlots where i add strings
     * */
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

    /**
     * Add to rowOfCardSlots the empty rows
     * @param rowOfCardSlots where i add strings
     * @param start num
     * */
    private void fillRestOfRows(ArrayList<String> rowOfCardSlots, int start){
        for (int i = start; i < rowOfCardSlots.size(); i++) {
            rowOfCardSlots.set(i, rowOfCardSlots.get(i) + PrintAssistant.instance.generateAStringOf(' ', widthColumn));
        }
    }

    /**
     * Set up rowOfCardSlots for cards slot
     * @param rowOfCardSlots array we use to contain the strings of cards slot
     * @param indexOfCard number used to know in which card slot we are writing
     * @param indexOfCardSlot number used to know which card we are writing
     * @param size of rowOfCardSlots
     * @param minimize true if u want to see the card slots minimized
     * */
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

    /**
     * Start convert card slot to cli
     * @param rowOfCardSlots used to write card slots
     * @param indexOfCardSlot we are writing
     * @param cardSlot we are writing
     * @param minimize true if u want to see the card slots minimized
     * */
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

    /**
     * Print the card Slots
     * */
    public void printCardSlots(boolean minimize){
        printTitle(username+"'s Developments");

        widthColumn = lengthInChar/3;

        printCardSlotLegend();

        printCardSlotHeader();

        ArrayList<String> rowOfCardSlots=new ArrayList<>(); //start from first useful line!!!

        for (int i = 0; i < cardSlots.size(); i++) {
            cardSlotToCli(rowOfCardSlots,i,cardSlots.get(i),minimize);
        }

        PrintAssistant.instance.printfMultipleString(rowOfCardSlots);
    }

    //METHODS FOR LEADER
    /**
     * Print leader legends
     * */
    private void printLeaderLegend(){
        PrintAssistant.instance.printf(PrintAssistant.instance.stringBetweenChar("Legend leader:", ' ', lengthInChar, ' ', ' '));
        String row=PrintAssistant.ANSI_GREEN_BACKGROUND+" Active Leader "+PrintAssistant.ANSI_RESET+" "+PrintAssistant.ANSI_WHITE_BACKGROUND+" Not Active Leader "+PrintAssistant.ANSI_RESET+PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
        row = PrintAssistant.instance.stringBetweenChar(row, ' ', lengthInChar, ' ', ' ');
        PrintAssistant.instance.printf(row);
    }

    /**
     * Return the sentence before the leader cost
     * @return the sentence before the leader cost
     * */
    private String leaderCostHeader(){
        return PrintAssistant.instance.fitToWidth("To activate you have to own:", widthColumn, ' ', ' ', ' ');
    }

    /**
     * Return the sentence before effect in leader card
     * @return the sentence before effect in leader card
     * */
    private String leaderEffectHeader(){
        return PrintAssistant.instance.fitToWidth("Effects:", widthColumn, ' ', ' ', ' ');
    }

    /**
     * Set up rowOfLeader to be ready to write in the leader card
     * @param rowsOfLeaders array used to store info
     * @param leaderIndex we are writing
     * */
    private void setUpForLeader(ArrayList<String> rowsOfLeaders, int leaderIndex){
        String row = PrintAssistant.instance.generateAStringOf(' ', (leaderIndex % 2 * widthColumn) - 1);
        int DIM_OF_LEADER = 7;
        while(rowsOfLeaders.size()<= DIM_OF_LEADER + leaders.get(leaderIndex).getEffects().size()){
            rowsOfLeaders.add(row);
        }
    }

    /**
     * Put a leader in rowsOfLeader
     * @param rowsOfLeaders array used to store info
     * @param leaderIndex we are writing
     * @param leadersSize we are writing
     * */
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

    /**
     * Print leaders
     * */
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

    /**
     * Convert a modelClient to ModelData
     * */
    public ModelData toModelData(){
        return new ModelData(username,faithTrack,currentPosOnFaithTrack,standardDepot,leaderDepot,maxStoreLeaderDepot,strongbox,cardSlots,leaders);
    }

    public boolean isInkwell() {
        return inkwell;
    }

    /**
     * add error in log of curr user
     * @param error to add to logs
     * */
    public void addErrorInLog(String error){
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        errorMessagesLog.put(timeStamp, error);
    }

    public LinkedHashMap<String, String> getErrorMessagesLog() {
        return errorMessagesLog;
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
