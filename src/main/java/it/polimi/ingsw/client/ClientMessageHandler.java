package it.polimi.ingsw.client;


import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.GUI.PrimaryController;
import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.ModelData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.PingPongMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ClientMessageHandler {

    private  Client client;
    private ClientGUI clientGUI;

    public ClientMessageHandler(Client client) {
        this.client =client;
    }

    public ClientMessageHandler(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    public void provaMessaggioDaServer(){
        PrimaryController controller = (PrimaryController) clientGUI.getController("primary");
        controller.prova();
    }

    public void pr2(){
        PrimaryController controller = (PrimaryController) clientGUI.getController("primary");
        controller.prova2();
    }



    public void handlePingPong(){
        client.writeToStream(new PingPongMessage());
    }

    public void handleError(ErrorMessage message){
        if(message.getCustomError()!=null){
            PrintAssistant.instance.errorPrint(message.getCustomError());
        }
        else{
            PrintAssistant.instance.errorPrint(message.getErrorType().getMessage());
        }

    }

    //Connection message handler
    public void connectNewUser(ConnectionMessage message){
        PrintAssistant.instance.printf(message.getMessage());
    }

    public void waitingPeople(ConnectionMessage message){
        PrintAssistant.instance.printf(message.getMessage());
    }

    public void username(ConnectionMessage message){
        PrintAssistant.instance.printf(message.getMessage());
    }

    public void numberOfPlayer(ConnectionMessage message){
        PrintAssistant.instance.printf(message.getMessage());
    }

    public void validReconnect(ConnectionMessage message){
        client.setMyName(message.getMessage());
        PrintAssistant.instance.printf("Reconnected as "+message.getMessage()+"!");
    }

    public void connectInfo(ConnectionMessage message){PrintAssistant.instance.printf(message.getMessage());}

    //Reconnect message handler
    public void reconnect(ReconnectionMessage message){
        try{
            //TODO: riattivare l'if solo se siamo sicuri di avere i jar in cartelle diverse prima dell'esecuzione
            /*
            File myObj = new File(client.getNameFile());
            if (myObj.createNewFile()) {
            */
                FileWriter myWriter = new FileWriter(client.getNameFile());
                myWriter.write(message.getMatchID()+"\n");
                myWriter.write(message.getClientID()+"");
                myWriter.close();
                PrintAssistant.instance.printf("We saved your main data, making sure you can disconnect and reconnect during the game!");
            /*
            } else {
                PrintAssistant.instance.errorPrint("Not able to save the file with your info to reconnect!");
            }
            */
        }
        catch(IOException e){
            PrintAssistant.instance.errorPrint("Not able to save the file with your info to reconnect!");
        }
    }

    public void reconnectGameSetUp(ReconnectGameMessage message){
        client.setModels(message.getUsernames());
        client.setMarketData(message.getMarket());
        client.setDeckDevData(message.getDeckDev());
        client.setBaseProduction(message.getBaseProd());
        client.setInkwell();
        for (ModelData modelData : message.getModels()){
            client.setUpModel(modelData);
        }
        PrintAssistant.instance.printf("It's your turn!");
        client.setState(ClientState.IN_GAME);
    }

    //MainMenu message handler
    public void mainMenu(){
        ArrayList<String> texts = new ArrayList<>();
        texts.add("Main menu");
        texts.add("1)Play multiplayer");
        texts.add("2)Play single player");
        texts.add("3)Reconnect to last game");
        PrintAssistant.instance.printfMultipleString(texts, PrintAssistant.ANSI_RED);
    }

    //StartTurn message handler
    public void newTurn(StarTurn message){
        if(message.getUsername().equals(client.getMyName())){
            client.setState(ClientState.IN_GAME);
            PrintAssistant.instance.printf("It's your turn!");
        }
        else{
            client.setState(ClientState.WAITING);
            PrintAssistant.instance.printf("Turn of: "+message.getUsername());
        }
    }

    //GameSetup message handler
    public void gameSetUp(GameSetup message){
        client.setModels(message.getUsernames());
        client.setMarketData(message.getMarket());
        client.setDeckDevData(message.getDeckDev());
        client.setFaithTrackData(message.getFaithTracks());
        client.setBaseProduction(message.getBaseProd());
        client.setInkwell();
    }

    //leaders message handler
    public void leaderSetUp(LeaderSetUpMessage message){
        client.getModelOf(client.getMyName()).setLeaders(message.getLeaders());
        client.getModelOf(client.getMyName()).printLeaders();
        PrintAssistant.instance.printf("Please choose 2 leader to discard!");
    }

    //start match handler
    public void startGame(){
        PrintAssistant.instance.printf("GL HF! Game starts!");
        client.getModelOf(client.getMyName()).printAll();
        PrintAssistant.instance.printf("Commands in game now available!");
        //da qui sono in game
    }

    //AnyConversionRequest message handler
    public void anyConversionRequest(AnyConversionRequest message){
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

    //DepotUpdate maessage handler
    public void depotUpdate(DepotUpdate message){
        if(message.normalDepot()){
            client.getModelOf(message.getUsername()).setStandardDepotAt(message.getDepotIndex(), message.getDepot());
        }
        else{
            client.getModelOf(message.getUsername()).setLeaderDepotAt(message.getDepotIndex(), message.getDepot());
        }
    }

    //Strongbox message handler
    public void strongboxUpdate(StrongboxUpdate message){
        client.getModelOf(message.getUsername()).setStrongbox(message.getStrongboxUpdated());
    }

    //leader discard message handler
    public void discardUpdate(LeaderDiscard message){
        if (message.getUsername().equals(client.getMyName())){
            client.getModelOf(client.getMyName()).removeLeaderAt(message.getLeaderIndex());
        }else{
            PrintAssistant.instance.printf(message.getUsername() + " has discarded a leader!");
        }
        printLeader(message.getUsername());
    }

    //leaderActivate message handler
    public void activeLeader(LeaderActivate message){
        if(message.getUsername().equals(client.getMyName())){
            client.getModelOf(client.getMyName()).setActiveLeaderAt(message.getLeaderIndex());
        }
        else{
            client.getModelOf(message.getUsername()).putAsActiveInLeader(message.getLeader());
        }
        printLeader(message.getUsername());
    }

    //MarketUpdate message handler
    public void marketUpdate(MarketUpdate message){
        client.setMarketData(message.getMarket());
    }

    //CardSlotUpdate message handler
    public void cardSlotUpdate(CardSlotUpdate message){
        CardDevData card = client.getDeckDevData().getCard(message.getRowDeckDevelopment(), message.getColDeckDevelopment());

        client.getModelOf(message.getUsername()).addToCardSlot(message.getSlotIndex(), card);
        printCardSlots(message.getUsername());
    }

    //BufferUpdate message handler
    public void bufferUpdate(BufferUpdate message){
        StringBuilder resource;
        if(message.getBufferUpdated().isEmpty()){
            resource = new StringBuilder("You have just end your operations with the resources!");
            PrintAssistant.instance.printf(resource.toString());
            printResources(client.getMyName());
        }
        else{
            resource = new StringBuilder("Buffer: ");
            for(ResourceData r : message.getBufferUpdated()){
                resource.append(r.toCli());
            }
            PrintAssistant.instance.printf(resource.toString());
        }
    }

    //ManageResourceRequest message handler
    public void manageResourceRequest(ManageResourcesRequest message){
        if(message.isFromMarket())
            PrintAssistant.instance.printf("Insert those resources in your depots!");
        else
            PrintAssistant.instance.printf("From where you want to take those resources to pay the production?");

        StringBuilder resource= new StringBuilder("Buffer: ");
        for(ResourceData r : message.getResources()){
            resource.append(r.toCli());
        }
        PrintAssistant.instance.printf(resource.toString());
    }

    public void faithTrackPositionIncreased(FaithTrackIncrement message){
        client.getModelOf(message.getUsername()).increaseCurrentPosOnFaithTrack();
        PrintAssistant.instance.printf(message.getUsername() + " faith track pushed ahead!");
    }

    public void popeFavorActivation(PopeFavorActivated message){
        client.getModelOf(message.getUsername()).popeFavorActivation(message.getIdVaticanReport(),message.isDiscard());
        if(message.isDiscard()){
            PrintAssistant.instance.printf("Discarded pope space "+message.getIdVaticanReport()+" of " + message.getUsername());
        }else{
            PrintAssistant.instance.printf("Activated pope space "+message.getIdVaticanReport()+" of " + message.getUsername());
        }
        //printFaith(message.getUsername());
    }

    //WhiteMarbleConversionRequest message handler
    public void whiteMarbleConversion(WhiteMarbleConversionRequest message){
        PrintAssistant.instance.printf("There are "+message.getNumOfWhiteMarbleDrew()+" white marble!");
        Set<Map.Entry<Integer, ArrayList<ResourceData>>> entries = message.getListOfConversion().entrySet();
        for(Map.Entry<Integer, ArrayList<ResourceData>> entry : entries){
            StringBuilder resource= new StringBuilder();
            for(ResourceData r : entry.getValue()){
                resource.append(r.toCli());
            }
            PrintAssistant.instance.printf("Leader "+entry.getKey()+" option: "+resource);
        }

    }

    //DepotLeaderUpdate message handler
    public void depotLeaderUpdate(DepotLeaderUpdate message){
        for(ResourceData rs : message.getDepots()){
            if (message.isDiscard()){
                client.getModelOf(message.getUsername()).removeLeaderDepot(rs);
            }else{
                client.getModelOf(message.getUsername()).addLeaderDepot(rs);
            }

        }
        printResources(message.getUsername());
    }


    //GameOver message handler
    public void gameOver(GameOver message){
        PrintAssistant.instance.printf("GAME OVER");
        TreeMap<Integer, String> matchRanking = new TreeMap<>(Collections.reverseOrder());
        matchRanking.putAll(message.getPlayers());
        Set<Map.Entry<Integer, String>> entries = matchRanking.entrySet();
        for(Map.Entry<Integer, String> entry : entries){
            PrintAssistant.instance.printf(entry.getKey()+": "+entry.getValue());
        }
        client.setState(ClientState.GAME_OVER);
    }

    //WinningCondition message handler
    public void winningCondition(){
        PrintAssistant.instance.printf("Winning condition has been reached, " +
                "all players will play the last turn until the inkwell player !!", PrintAssistant.ANSI_GREEN);
    }

    //single player
    public void handleDeckDevCardRemoving(RemoveDeckDevelopmentCard message){
        client.getDeckDevData().removeCardDevData(message.getRow(), message.getColumn());
        PrintAssistant.instance.printf("I have discarded some development cards");
    }


    //Print
    public void printFaith(String username){
        if(username.equals(client.getMyName()))
            client.getModelOf(client.getMyName()).printFaithTrack();
    }
    public void printCardSlots(String username){
        if(username.equals(client.getMyName()))
            client.getModelOf(client.getMyName()).printCardSlots(true);
    }
    public void printResources(String username){
        if(username.equals(client.getMyName()))
            client.getModelOf(client.getMyName()).printResources();
    }
    public void printLeader(String username){
        if(username.equals(client.getMyName()))
            client.getModelOf(client.getMyName()).printLeaders();
    }
}
