package it.polimi.ingsw.client;

import it.polimi.ingsw.client.data.*;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;

public class ModelClient {
    private final Integer NUMBER_OF_CELL_FAITH=10;
    private final Integer OFFSET_AllIGN = 2; //2 per 12 ====== 3 per 15
    private final Integer DIM_CELL_CHAR = 14;

    private final String username;
    private Integer currentPosOnFaithTrack;
    private ArrayList<FaithTrackData> faithTrack = new ArrayList<>();
    private ArrayList<ResourceData> standardDepot = new ArrayList<>();
    private ArrayList<ResourceData> leaderDepot = new ArrayList<>();
    private ArrayList<ResourceData> strongbox= new ArrayList<>();
    private ArrayList<CardDevData> cardSlotOne=new ArrayList<>();
    private ArrayList<CardDevData> cardSlotTwo=new ArrayList<>();
    private ArrayList<CardDevData> cardSlotThree=new ArrayList<>();
    private ArrayList<CardLeaderData> leader=new ArrayList<>();

    //attributes to CLI
    int lengthInChar = DIM_CELL_CHAR*NUMBER_OF_CELL_FAITH;
    int widthColumn;

    public ModelClient(String username) {
        this.username = username;
        //setUpForDebug();
        realSetUp();
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
    }

    private void setUpForDebug(){
        this.currentPosOnFaithTrack=6;
        for(int i=0;i<3;i++){
            standardDepot.add(new ResourceData(ResourceType.COIN, i+1));
        }
        /*
        for(int i=0;i<3;i++){
            leaderDepot.add(ResourceFactory.createResource(ResourceType.STONE, i+1));
        }*/
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
        EffectData effData2 = new EffectData("Disocunt", cost, null);
        EffectData effData3 = new EffectData("Market", cost, earn1);
        ArrayList<EffectData> effectsD= new ArrayList<>();
        effectsD.add(effData);

        /*
        CardDevData cdd=new CardDevData(1, 2, ColorData.BLUE, resourceReq, cost, earn );
        CardDevData cdd1=new CardDevData(2, 5, ColorData.PURPLE, resourceReq, cost, earn );
         */
        CardDevData cdd=new CardDevData(1, 2, ColorData.BLUE, resourceReq, effectsD);
        CardDevData cdd1=new CardDevData(2, 5, ColorData.PURPLE, resourceReq, effectsD);
        cardSlotOne.add(cdd);
        cardSlotOne.add(cdd1);
        cardSlotTwo.add(cdd);
        cardSlotTwo.add(cdd1);
        cardSlotThree.add(cdd);
        cardSlotThree.add(cdd1);

        ArrayList<EffectData> effectsL= new ArrayList<>();
        effectsL.add(effData2);
        effectsL.add(effData);
        effectsL.add(effData3);

        ArrayList<String> effects=new ArrayList<>();
        effects.add("Extra Depot COIN 2");
        effects.add("Discount SHIELD 1");
        CardLeaderData cl=new CardLeaderData(4, cardSlotOne, cost, effectsL,false);
        CardLeaderData cl2=new CardLeaderData(4, cardSlotOne, cost, effectsL,true);
        leader.add(cl);
        leader.add(cl2);
        //leader.add(cl);
        leader.add(cl);
    }

    public boolean canAddToCardSlotOne(CardDevData card){
        return canAddToCardSlot(cardSlotOne, card);
    }

    public boolean canAddToCardSlotTwo(CardDevData card){
        return canAddToCardSlot(cardSlotTwo, card);
    }

    public boolean canAddToCardSlotThree(CardDevData card){
        return canAddToCardSlot(cardSlotThree, card);
    }

    private boolean canAddToCardSlot(ArrayList<CardDevData> slot, CardDevData cardToAdd){
        return slot.get(slot.size() - 1).getLevel() < cardToAdd.getLevel();
    }

    public void addToCardSlotOne(CardDevData card){
        cardSlotOne.add(card);
    }

    public void addToCardSlotTwo(CardDevData card){
        cardSlotTwo.add(card);
    }

    public void addToCardSlotThree(CardDevData card){
        cardSlotThree.add(card);
    }

    /*
    public void setCardSlotOne(ArrayList<CardDevData> cardSlotOne) {
        this.cardSlotOne = cardSlotOne;
    }

    public void setCardSlotTwo(ArrayList<CardDevData> cardSlotTwo) {
        this.cardSlotTwo = cardSlotTwo;
    }

    public void setCardSlotThree(ArrayList<CardDevData> cardSlotThree) {
        this.cardSlotThree = cardSlotThree;
    }
    */
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

    public void setLeader(ArrayList<CardLeaderData> leader) {
        this.leader = leader;
    }

    public void removeLeaderAt(int index){
        leader.remove(index);
    }

    public void setActiveLeaderAt(int index){
        leader.get(index).setActive(true);
    }

    public boolean validIndexForLeader(int index){
        return (index>=0 && index<leader.size());
    }

    public void addToCurrPositionOnFaithTrack(int value){
        currentPosOnFaithTrack+=value;
    }

    public void printAll() {
        printFaithTrack();
        printResource();
        printCardSlots();
        printLeader();
    }

    public void printFaithTrack(){
        String titleFaithTrack=PrintAssistant.instance.stringBetweenChar(username+"'s Faith Track", ' ', lengthInChar, ' ', ' ');
        PrintAssistant.instance.printf(titleFaithTrack, PrintAssistant.ANSI_BLACK, PrintAssistant.ANSI_YELLOW_BACKGROUND);
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

    public void printResource(){
        String titleResource=username+"'s Resources";
        PrintAssistant.instance.printf(String.format("%-" + lengthInChar  + "s", String.format("%" + (titleResource.length() + (lengthInChar - titleResource.length()) / 2) + "s", titleResource)), PrintAssistant.ANSI_BLACK, PrintAssistant.ANSI_YELLOW_BACKGROUND);
        ArrayList<String> rowsOfResources=new ArrayList<>();

        String titleLeaderDepot="";
        if(!leaderDepot.isEmpty()){
            widthColumn  = (Integer)(lengthInChar/3);
            titleLeaderDepot=PrintAssistant.instance.stringBetweenChar("LE.DEPOT", '_', widthColumn, ' ', ' ');
        }
        else{
            widthColumn =(Integer)(lengthInChar/2);
        }
        String title=PrintAssistant.instance.stringBetweenChar("STRONBOX", '_', widthColumn, ' ', ' ');
        title+=PrintAssistant.instance.stringBetweenChar("ST.DEPOT", '_', widthColumn, ' ', ' ');
        title+=titleLeaderDepot;
        PrintAssistant.instance.printf(title);
        String row;
        for(int i=0;i<strongbox.size();i++){
            row=(1+i)+")"+strongbox.get(i).getType()+": "+strongbox.get(i).getValue();
            row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', '|','|', OFFSET_AllIGN);
            rowsOfResources.add(row);
        }
        rowsOfResources.add(PrintAssistant.instance.stringBetweenChar("END_", '_',widthColumn, '|','|'));
        int i;
        for(i=0; i<standardDepot.size(); i++){
            if(standardDepot.get(i).getType()==ResourceType.ANY){
                row=(1+i)+")"+"EMPTY";
            }
            else{
                row=(1+i)+")"+standardDepot.get(i).getType()+": "+standardDepot.get(i).getValue()+"/"+(i+1);
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', '|','|', OFFSET_AllIGN);
            }
            rowsOfResources.set(i, rowsOfResources.get(i)+row);
        }
        rowsOfResources.set(i, rowsOfResources.get(i)+PrintAssistant.instance.stringBetweenChar("END_", '_',widthColumn, '|', '|'));
        if(!leaderDepot.isEmpty()){
            for(i=0; i<leaderDepot.size(); i++){
                row=(1+i)+")"+leaderDepot.get(i).getType()+": "+leaderDepot.get(i).getValue()+"/"+(2);
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', '|','|', OFFSET_AllIGN);
                rowsOfResources.set(i, rowsOfResources.get(i)+row);
            }
            rowsOfResources.set(i, rowsOfResources.get(i)+PrintAssistant.instance.stringBetweenChar("END_", '_',widthColumn, '|', '|'));
        }

        PrintAssistant.instance.printfMultipleString(rowsOfResources);
    }

    public void printCardSlots(){
        String titleResource=username+"'s Developments ";
        PrintAssistant.instance.printf(String.format("%-" + lengthInChar  + "s", String.format("%" + (titleResource.length() + (lengthInChar - titleResource.length()) / 2) + "s", titleResource)), PrintAssistant.ANSI_BLACK, PrintAssistant.ANSI_YELLOW_BACKGROUND);
        widthColumn = (Integer)(lengthInChar/3);

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
            row.append(r.toCli()).append("=").append(r.getType()).append("  ");
        }
        row = new StringBuilder(PrintAssistant.instance.stringBetweenChar(row.toString(), ' ', lengthInChar, ' ', ' '));
        PrintAssistant.instance.printf(row.toString());

        String title=PrintAssistant.instance.stringBetweenChar("CARD SLOT1", '_', widthColumn, ' ', ' ');
        title+=PrintAssistant.instance.stringBetweenChar("CARD SLOT2", '_', widthColumn, ' ', ' ');
        title+=PrintAssistant.instance.stringBetweenChar("CARD SLOT3", '_', widthColumn, ' ', ' ');
        PrintAssistant.instance.printf(title);
        ArrayList<String> rowOfCardSlots=new ArrayList<>(); //parte dalla prima riga utile!!!
        if(cardSlotOne.isEmpty()){
            rowOfCardSlots.add(PrintAssistant.instance.stringBetweenChar("Empty*", ' ',  widthColumn, '|', '|'));
            row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("END*", '_', widthColumn, '|', '|'));
            rowOfCardSlots.add(row.toString());
        }
        else{
            for(int i=0; i<cardSlotOne.size(); i++){
                row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("CARD LV" + cardSlotOne.get(i).getLevel() +" +" + cardSlotOne.get(i).getVictoryPoints()+"VP ", ' ', widthColumn - 2, ' ', ' '));
                row = new StringBuilder("|" + cardSlotOne.get(i).colorToColor() + row + PrintAssistant.ANSI_RESET + "|");
                rowOfCardSlots.add(row.toString());
                if(!cardSlotOne.get(i).getResourceReq().isEmpty()){
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth("To buy you had to pay:", widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.add(row.toString());
                    row = new StringBuilder();
                    for(ResourceData s : cardSlotOne.get(i).getResourceReq()){
                        row.append(s.toCli());
                    }
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.add(row.toString());
                }
                if(!cardSlotOne.get(i).getEffects().get(0).getResourcesBefore().isEmpty()){
                    row = new StringBuilder("To make production you have to pay:");
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.add(row.toString());
                    row = new StringBuilder();
                    /*
                    for(ResourceData s : cardSlotOne.get(i).getProductionCost()){
                        row.append(s.toCli());
                    }*/
                    row=new StringBuilder(cardSlotOne.get(i).getEffects().get(0).resourceBeforeToCli());
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.add(row.toString());
                }
                if(!cardSlotOne.get(i).getEffects().get(0).getResourcesAfter().isEmpty()){
                    row = new StringBuilder("You will earn from production:");
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.add(row.toString());
                    row = new StringBuilder();
                    /*
                    for(ResourceData s : cardSlotOne.get(i).getProductionEarn()){
                        row.append(s.toCli());
                    }
                    */
                    row=new StringBuilder(cardSlotOne.get(i).getEffects().get(0).resourceAfterToCli());
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.add(row.toString());
                }
                row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("END CARD", ' ', widthColumn - 2, ' ', ' '));
                row = new StringBuilder("|" + cardSlotOne.get(i).colorToColor() + row + PrintAssistant.ANSI_RESET + "|");
                rowOfCardSlots.add(row.toString());
                row = new StringBuilder(PrintAssistant.instance.fitToWidth("", widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                rowOfCardSlots.add(row.toString());
            }
            row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("END*", '_', widthColumn, '|', '|'));
            rowOfCardSlots.add(row.toString());
        }
        int writtenRow=0;
        if(cardSlotTwo.isEmpty()){
            row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("Empty*", ' ', widthColumn, '|', '|'));
            rowOfCardSlots.set(0, rowOfCardSlots.get(0)+row);
            row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("END*", '_', widthColumn, '|', '|'));
            rowOfCardSlots.set(1, rowOfCardSlots.get(1)+row);
        }
        else{
            for(int i=0; i<cardSlotTwo.size(); i++){
                writtenRow=0;
                if(rowOfCardSlots.size()<=(i+1)*9){
                    row = new StringBuilder(PrintAssistant.instance.generateAStringOf(' ', widthColumn - 1));
                    while(rowOfCardSlots.size()<=(i+1)*9){
                        rowOfCardSlots.add(row.toString());
                    }
                }
                row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("CARD LV" + cardSlotTwo.get(i).getLevel()+" +" + cardSlotTwo.get(i).getVictoryPoints()+"VP ", ' ', widthColumn - 2, ' ', ' '));
                row = new StringBuilder("|" + cardSlotTwo.get(i).colorToColor() + row + PrintAssistant.ANSI_RESET + "|");
                rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                writtenRow++;
                if(!cardSlotTwo.get(i).getResourceReq().isEmpty()){
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth("To buy you had to pay:", widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                    row = new StringBuilder();
                    for(ResourceData s : cardSlotTwo.get(i).getResourceReq()){
                        row.append(s.toCli());
                    }
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                }
                if(!cardSlotTwo.get(i).getEffects().get(0).getResourcesBefore().isEmpty()){
                    row = new StringBuilder("To make production you have to pay:");
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                    row = new StringBuilder();/*
                    for(ResourceData s : cardSlotTwo.get(i).getProductionCost()){
                        row.append(s.toCli());
                    }*/
                    row=new StringBuilder(cardSlotTwo.get(i).getEffects().get(0).resourceBeforeToCli());
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                }
                if(!cardSlotTwo.get(i).getEffects().get(0).getResourcesAfter().isEmpty()){
                    row = new StringBuilder("You will earn from production:");
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                    row = new StringBuilder();
                    /*
                    for(ResourceData s : cardSlotTwo.get(i).getProductionEarn()){
                        row.append(s.toCli());
                    }*/
                    row=new StringBuilder(cardSlotTwo.get(i).getEffects().get(0).resourceAfterToCli());
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                }
                row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("END CARD", ' ', widthColumn - 2, ' ', ' '));
                row = new StringBuilder("|" + cardSlotTwo.get(i).colorToColor() + row + PrintAssistant.ANSI_RESET + "|");
                rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                writtenRow++;
                row = new StringBuilder(PrintAssistant.instance.fitToWidth("", widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                writtenRow++;
            }
            row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("END*", '_', widthColumn, '|', '|'));
            rowOfCardSlots.set(indexCardSlot(cardSlotTwo.size()-1, writtenRow), rowOfCardSlots.get(indexCardSlot(cardSlotTwo.size()-1, writtenRow))+row);
            writtenRow++;
        }
        if(cardSlotThree.isEmpty()){
            row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("Empty*", ' ', widthColumn, '|', '|'));
            rowOfCardSlots.set(0, rowOfCardSlots.get(0)+row);
            row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("END*", '_', widthColumn, '|', '|'));
            rowOfCardSlots.set(1, rowOfCardSlots.get(1)+row);
        }
        else{
            for(int i=0; i<cardSlotThree.size(); i++){
                writtenRow=0;
                if(rowOfCardSlots.size()<=(i+1)*9){
                    row = new StringBuilder(PrintAssistant.instance.generateAStringOf(' ', (widthColumn - 1) * 2));
                    while(rowOfCardSlots.size()<=(i+1)*9){
                        rowOfCardSlots.add(row.toString());
                    }
                }
                row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("CARD LV" + cardSlotThree.get(i).getLevel()+" +" + cardSlotThree.get(i).getVictoryPoints()+"VP ", ' ', widthColumn - 2, ' ', ' '));
                row = new StringBuilder("|" + cardSlotThree.get(i).colorToColor() + row + PrintAssistant.ANSI_RESET + "|");
                rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                writtenRow++;
                if(!cardSlotThree.get(i).getResourceReq().isEmpty()){
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth("To buy you had to pay:", widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                    row = new StringBuilder();
                    for(ResourceData s : cardSlotThree.get(i).getResourceReq()){
                        row.append(s.toCli());
                    }
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                }
                if(!cardSlotThree.get(i).getEffects().get(0).getResourcesBefore().isEmpty()){
                    row = new StringBuilder("To make production you have to pay:");
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                    row = new StringBuilder();
                    /*
                    for(ResourceData s : cardSlotThree.get(i).getProductionCost()){
                        row.append(s.toCli());
                    }*/
                    row=new StringBuilder(cardSlotThree.get(i).getEffects().get(0).resourceBeforeToCli());
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                }
                if(!cardSlotThree.get(i).getEffects().get(0).getResourcesAfter().isEmpty()){
                    row = new StringBuilder("You will earn from production:");
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                    row = new StringBuilder();
                    /*
                    for(ResourceData s : cardSlotThree.get(i).getProductionEarn()){
                        row.append(s.toCli());
                    }*/
                    row=new StringBuilder(cardSlotThree.get(i).getEffects().get(0).resourceAfterToCli());
                    row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                    rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                    writtenRow++;
                }
                row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("END CARD", ' ', widthColumn - 2, ' ', ' '));
                row = new StringBuilder("|" + cardSlotThree.get(i).colorToColor() + row + PrintAssistant.ANSI_RESET + "|");
                rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                writtenRow++;
                row = new StringBuilder(PrintAssistant.instance.fitToWidth("", widthColumn, ' ', '|', '|', OFFSET_AllIGN));
                rowOfCardSlots.set(indexCardSlot(i, writtenRow), rowOfCardSlots.get(indexCardSlot(i, writtenRow))+row);
                writtenRow++;
            }
            row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("END*", '_', widthColumn, '|', '|'));
            rowOfCardSlots.set(indexCardSlot(cardSlotThree.size()-1, writtenRow), rowOfCardSlots.get(indexCardSlot(cardSlotThree.size()-1, writtenRow))+row);
        }
        PrintAssistant.instance.printfMultipleString(rowOfCardSlots);
    }

    public void printLeader(){
        if(leader.isEmpty())
            return;
        String titleFaithTrack=PrintAssistant.instance.stringBetweenChar(username+"'s Leaders", ' ', lengthInChar, ' ', ' ');
        PrintAssistant.instance.printf(titleFaithTrack, PrintAssistant.ANSI_BLACK, PrintAssistant.ANSI_YELLOW_BACKGROUND);
        widthColumn = (Integer)(lengthInChar/2);
        int myOff;
        if(leader.size()==4)
            myOff=3;
        else
            myOff=2;
        String row="";

        PrintAssistant.instance.printf(PrintAssistant.instance.stringBetweenChar("Legend leader:", ' ', lengthInChar, ' ', ' '));
        row=PrintAssistant.ANSI_GREEN_BACKGROUND+" Active Leader "+PrintAssistant.ANSI_RESET+" "+PrintAssistant.ANSI_WHITE_BACKGROUND+" Not Active Leader "+PrintAssistant.ANSI_RESET+PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
        row = PrintAssistant.instance.stringBetweenChar(row, ' ', lengthInChar, ' ', ' ');
        PrintAssistant.instance.printf(row);

        int writtenRow;
        ArrayList<String> rowsOfLeaders = new ArrayList<>();
        for(int i=0;i<2 && i<leader.size(); i++){
            writtenRow=0;
            row = PrintAssistant.instance.generateAStringOf(' ', (i*widthColumn) - 1);
            while(rowsOfLeaders.size()<=7+leader.get(i).getEffects().size()){
                rowsOfLeaders.add(row);
            }
            //row=(leader.get(i).isActive()? PrintAssistant.ANSI_GREEN_BACKGROUND:PrintAssistant.ANSI_WHITE_BACKGROUND)+PrintAssistant.ANSI_BLACK + (i+1)+") LEADER" + PrintAssistant.ANSI_RESET;
            row =PrintAssistant.instance.stringBetweenChar((i+1)+") LEADER" +" +" + leader.get(i).getVictoryPoint()+"VP", ' ', widthColumn-2, ' ', ' ');
            row = (leader.get(i).isActive()? PrintAssistant.ANSI_GREEN_BACKGROUND:PrintAssistant.ANSI_WHITE_BACKGROUND)+PrintAssistant.ANSI_BLACK + row +" "+ PrintAssistant.ANSI_RESET+" " ;
            if(i == leader.size()-1){
                row+=PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
            }
            rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
            writtenRow++;
            if(!leader.get(i).getCardReq().isEmpty()){
                row="To activate you have to own:";
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', ' ', ' ', myOff);
                rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
                writtenRow++;
                row="";
                for(CardDevData d : leader.get(i).getCardReq()){
                    row+=d.toCLIForLeader();
                }
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', ' ', ' ', myOff);
                if(i == 1){
                    row+=PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
                }
                rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
                writtenRow++;
            }
            if(!leader.get(i).getResourceReq().isEmpty()){
                row="To activate you have to own:";
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', ' ', ' ', myOff);
                rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
                writtenRow++;
                row="";
                for(ResourceData r : leader.get(i).getResourceReq()){
                    row+=r.toCli();
                }
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', ' ', ' ', myOff);
                if(i == 1){
                    row+=PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
                }
                rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
                writtenRow++;
            }
            if(!leader.get(i).getEffects().isEmpty()){
                row="Effects:";
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', ' ', ' ', myOff);
                rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
                writtenRow++;
                row="";
                for(EffectData e : leader.get(i).getEffects()){
                    row=PrintAssistant.instance.fitToWidth(e.toString(), widthColumn, ' ', ' ', ' ', myOff);
                    if(i == 1){
                        row+=PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
                    }
                    rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
                    writtenRow++;
                }
            }
            row =PrintAssistant.instance.stringBetweenChar("END CARD", ' ', widthColumn-2, ' ', ' ');
            row =(leader.get(i).isActive()? PrintAssistant.ANSI_GREEN_BACKGROUND:PrintAssistant.ANSI_WHITE_BACKGROUND)+PrintAssistant.ANSI_BLACK + row +" "+ PrintAssistant.ANSI_RESET+" " ;
            if(i == 1){
                row+=PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
            }
            rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
            writtenRow++;
        }
        PrintAssistant.instance.printfMultipleString(rowsOfLeaders);
        rowsOfLeaders.clear();
        for(int i=2;i<leader.size(); i++){
            writtenRow=0;
            row = PrintAssistant.instance.generateAStringOf(' ', ((i-2)*widthColumn) - 1);
            while(rowsOfLeaders.size()<=7+leader.get(i).getEffects().size()){
                rowsOfLeaders.add(row);
            }
            row =PrintAssistant.instance.stringBetweenChar((i+1)+") LEADER" +" +" + leader.get(i).getVictoryPoint()+"VP", ' ', widthColumn-2, ' ', ' ');
            row = (leader.get(i).isActive()? PrintAssistant.ANSI_GREEN_BACKGROUND:PrintAssistant.ANSI_WHITE_BACKGROUND)+PrintAssistant.ANSI_BLACK + row+" " + PrintAssistant.ANSI_RESET+" ";
            if(i == leader.size()-1){
                row+=PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
            }
            rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
            writtenRow++;
            if(!leader.get(i).getCardReq().isEmpty()){
                row="To activate you have to own:";
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', ' ', ' ', myOff);
                rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
                writtenRow++;
                row="";
                for(CardDevData d : leader.get(i).getCardReq()){
                    row+=d.toCLIForLeader();
                }
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', ' ', ' ', myOff);
                if(i == leader.size()-1){
                    row+=PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
                }
                rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
                writtenRow++;
            }
            if(!leader.get(i).getResourceReq().isEmpty()){
                row="To activate you have to own:";
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', ' ', ' ', myOff);
                rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
                writtenRow++;
                row="";
                for(ResourceData r : leader.get(i).getResourceReq()){
                    row+=r.toCli();
                }
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', ' ', ' ', myOff);
                if(i == leader.size()-1){
                    row+=PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
                }
                rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
                writtenRow++;
            }
            if(!leader.get(i).getEffects().isEmpty()){
                row="Effects:";
                row=PrintAssistant.instance.fitToWidth(row, widthColumn, ' ', ' ', ' ', myOff);
                rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
                writtenRow++;
                row="";
                for(EffectData e : leader.get(i).getEffects()){
                    row=PrintAssistant.instance.fitToWidth(e.toString(), widthColumn, ' ', ' ', ' ', myOff);
                    if(i == leader.size()-1){
                        row+=PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
                    }
                    rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
                    writtenRow++;
                }
            }
            row =PrintAssistant.instance.stringBetweenChar("END CARD", ' ', widthColumn-2, ' ', ' ');
            row =(leader.get(i).isActive()? PrintAssistant.ANSI_GREEN_BACKGROUND:PrintAssistant.ANSI_WHITE_BACKGROUND)+PrintAssistant.ANSI_BLACK + row +" "+ PrintAssistant.ANSI_RESET+" " ;
            if(i == leader.size()-1){
                row+=PrintAssistant.ANSI_BLACK+"|"+PrintAssistant.ANSI_RESET;
            }
            rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
            writtenRow++;
        }
        PrintAssistant.instance.printfMultipleString(rowsOfLeaders);
    }

/*
    public static void main(String[] args){
        ModelClient mc;
        mc=new ModelClient("Teo");

        mc.printFaithTrack();
        mc.printResource();
        mc.printCardSlots();
        mc.printLeader();
    }
*/

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

    //manage index in print card slot
    private int indexCardSlot(int i, int writtenRow){
        return (i*9)+writtenRow;
    }
}
