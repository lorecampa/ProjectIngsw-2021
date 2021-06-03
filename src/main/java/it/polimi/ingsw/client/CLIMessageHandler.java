package it.polimi.ingsw.client;

import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;
import java.io.IOException;
import java.util.*;

public class CLIMessageHandler extends ClientMessageHandler {
    private final Client client = Client.getInstance();


    @Override
    public void handleError(ErrorMessage message) {
        if(message.getCustomError()!=null){
            PrintAssistant.instance.errorPrint(message.getCustomError());
        }
        else{
            PrintAssistant.instance.errorPrint(message.getErrorType().getMessage());
        }
    }


    @Override
    public void connectNewUser(ConnectionMessage message) {
        PrintAssistant.instance.printf(message.getMessage());
    }

    @Override
    public void waitingPeople(ConnectionMessage message) {
        PrintAssistant.instance.printf(message.getMessage());
    }

    @Override
    public void username(ConnectionMessage message) {
        PrintAssistant.instance.printf(message.getMessage());
    }

    @Override
    public void numberOfPlayer(ConnectionMessage message) {
        PrintAssistant.instance.printf(message.getMessage());
    }

    @Override
    public void validReconnect(ConnectionMessage message) {
        super.validReconnect(message);
        PrintAssistant.instance.printf("Reconnected as "+message.getMessage()+"!");
    }

    @Override
    public void connectInfo(ConnectionMessage message) {
        PrintAssistant.instance.printf(message.getMessage());
    }

    @Override
    public void reconnect(ReconnectionMessage message) {
        try {
            super.reconnect(message);
            PrintAssistant.instance.printf("We saved your main data, making sure you can disconnect and reconnect during the game!");
        }catch (IOException e){
            PrintAssistant.instance.errorPrint("Not able to save the file with your info to reconnect!");
        }
    }

    @Override
    public void reconnectGameSetUp(ReconnectGameMessage message) {
        super.reconnectGameSetUp(message);
        PrintAssistant.instance.printf("It's your turn!");
    }

    @Override
    public void mainMenu() {
        ArrayList<String> texts = new ArrayList<>();
        texts.add("Main menu");
        texts.add("1)Play multiplayer");
        texts.add("2)Play single player");
        texts.add("3)Reconnect to last game");
        PrintAssistant.instance.printfMultipleString(texts, PrintAssistant.ANSI_RED);
    }

    @Override
    public void newTurn(StarTurn message) {
        super.newTurn(message);
        if(message.getUsername().equals(client.getMyName())){
            PrintAssistant.instance.printf("It's your turn!");
        }
        else{
            PrintAssistant.instance.printf("Turn of: "+message.getUsername());
        }
    }

    @Override
    public void leaderSetUp(LeaderSetUpMessage message) {
        super.leaderSetUp(message);
        client.getModelOf(client.getMyName()).printLeaders();
        PrintAssistant.instance.printf("Please choose 2 leader to discard!");
    }

    @Override
    public void startGame() {
        PrintAssistant.instance.printf("GL HF! Game starts!");
        client.getModelOf(client.getMyName()).printAll();
        PrintAssistant.instance.printf("Commands in game now available!");
    }

    @Override
    public void anyConversionRequest(AnyConversionRequest message) {
        ArrayList<String> textToPrint = new ArrayList<>();
        StringBuilder row = new StringBuilder();
        textToPrint.add("You have to convert "+message.getNumOfAny()+" into concrete resource!");
        if(message.isProduction()){
            textToPrint.add("You can convert the any in every concrete resource you want!");
        }
        else{
            textToPrint.add("You can convert the any in: ");
            for(ResourceData res : message.getOptionConversion()){
                row.append(res.toCli());
            }
            textToPrint.add(row.toString());
            row = new StringBuilder();
            if(!message.getOptionOfDiscountNotUsed().isEmpty()){
                textToPrint.add("You can also use the discount: ");
                for(ResourceData res : message.getOptionOfDiscountNotUsed()){
                    row.append(res.toCli());
                }
                textToPrint.add(row.toString());
            }
        }
        PrintAssistant.instance.printfMultipleString(textToPrint);
    }

    @Override
    public void leaderDiscardUpdate(LeaderDiscard message) {
        super.leaderDiscardUpdate(message);
        if (!message.getUsername().equals(client.getMyName())){
            PrintAssistant.instance.printf(message.getUsername() + " has discarded a leader!");
        }
        printLeader(message.getUsername());

    }

    @Override
    public void activeLeader(LeaderActivate message) {
        super.activeLeader(message);
        printLeader(message.getUsername());
    }

    @Override
    public void cardSlotUpdate(CardSlotUpdate message) {
        super.cardSlotUpdate(message);
        printCardSlots(message.getUsername());
    }

    @Override
    public void bufferUpdate(BufferUpdate message) {
        StringBuilder resource;
        if(message.getBufferUpdated().isEmpty()){
            resource = new StringBuilder("You have just end your operations with the resources!");
            PrintAssistant.instance.printf(resource.toString());
            printResources(client.getMyName());
        }
        else{
            printBuffer(message.getBufferUpdated());
        }
    }

    @Override
    public void handleDepotPositioningRequest(DepotPositioningRequest message) {
        PrintAssistant.instance.printf("Insert those resources in your depots!");
        printBuffer(message.getResources());
    }

    @Override
    public void handleWarehouseRemovingRequest(WarehouseRemovingRequest message) {
        PrintAssistant.instance.printf("Remove those resources from yours depots or strongbox!");
        printBuffer(message.getResources());
    }



    @Override
    public void faithTrackPositionIncreased(FaithTrackIncrement message) {
        super.faithTrackPositionIncreased(message);
        PrintAssistant.instance.printf(message.getUsername() + " faith track pushed ahead!");

    }

    @Override
    public void popeFavorActivation(PopeFavorActivated message) {
        super.popeFavorActivation(message);
        if(message.isDiscard()){
            PrintAssistant.instance.printf("Discarded pope space "+message.getIdVaticanReport()+" of " + message.getUsername());
        }else{
            PrintAssistant.instance.printf("Activated pope space "+message.getIdVaticanReport()+" of " + message.getUsername());
        }
        //printFaith(message.getUsername());
    }

    @Override
    public void whiteMarbleConversion(WhiteMarbleConversionRequest message) {
        PrintAssistant.instance.printf("There are " + message.getNumOfWhiteMarbleDrew()+" white marble!");
        Set<Map.Entry<Integer, ArrayList<ResourceData>>> entries = message.getListOfConversion().entrySet();
        for(Map.Entry<Integer, ArrayList<ResourceData>> entry : entries){
            StringBuilder resource= new StringBuilder();
            for(ResourceData r : entry.getValue()){
                resource.append(r.toCli());
            }
            PrintAssistant.instance.printf("Leader "+(entry.getKey()+1)+" option: "+resource);
        }
    }

    @Override
    public void depotLeaderUpdate(DepotLeaderUpdate message) {
        super.depotLeaderUpdate(message);
        printResources(message.getUsername());
    }

    @Override
    public void gameOver(GameOver message) {
        super.gameOver(message);
        PrintAssistant.instance.printf("GAME OVER");
        TreeMap<Integer, String> matchRanking = new TreeMap<>(Collections.reverseOrder());
        matchRanking.putAll(message.getPlayers());
        Set<Map.Entry<Integer, String>> entries = matchRanking.entrySet();
        for(Map.Entry<Integer, String> entry : entries){
            PrintAssistant.instance.printf(entry.getKey()+": "+entry.getValue());
        }
    }

    @Override
    public void winningCondition() {
        PrintAssistant.instance.printf("Winning condition has been reached, " +
                "all players will play the last turn until the inkwell player !!", PrintAssistant.ANSI_GREEN);
    }

    @Override
    public void handleDeckDevCardRemoving(RemoveDeckDevelopmentCard message) {
        super.handleDeckDevCardRemoving(message);
        PrintAssistant.instance.printf("I have discarded some development cards");
    }

    @Override
    public void handleProductionSelectionCompleted() {
        PrintAssistant.instance.printf("Card inserted in production buffer");
    }

    //Print
    private void printFaith(String username){
        if(username.equals(client.getMyName()))
            client.getModelOf(client.getMyName()).printFaithTrack();
    }
    private void printCardSlots(String username){
        if(username.equals(client.getMyName()))
            client.getModelOf(client.getMyName()).printCardSlots(true);
    }
    private void printResources(String username){
        if(username.equals(client.getMyName()))
            client.getModelOf(client.getMyName()).printResources();
    }
    private void printLeader(String username){
        if(username.equals(client.getMyName()))
            client.getModelOf(client.getMyName()).printLeaders();
    }

    private void printBuffer(ArrayList<ResourceData> resources){
        StringBuilder resource= new StringBuilder("Buffer: ");
        for(ResourceData r : resources){
            resource.append(r.toCli());
        }
        PrintAssistant.instance.printf(resource.toString());
    }
}