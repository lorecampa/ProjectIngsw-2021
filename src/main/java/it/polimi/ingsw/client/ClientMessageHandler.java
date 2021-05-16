package it.polimi.ingsw.client;


import it.polimi.ingsw.client.data.CardDevData;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ClientMessageHandler {

    private final Client client;
    public ClientMessageHandler(Client client) {
        this.client =client;
    }

    public void handlePingPong(){
        //client.writeToStream(new PingPongMessage());
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
                PrintAssistant.instance.errorPrint("We saved your main data, making sure you can disconnect and reconnect during the game!");
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
        client.setFaithTrackData(message.getFaithTrack());
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
        client.getModelOf(client.getMyName()).setStrongbox(message.getStrongboxUpdated());
    }

    //leader discard message handler
    public void discardUpdate(LeaderDiscard message){
        if (message.getUsername().equals(client.getMyName())){
            client.getModelOf(client.getMyName()).removeLeaderAt(message.getLeaderIndex());
            client.getModelOf(client.getMyName()).printLeaders();
        }else{
            PrintAssistant.instance.printf(message.getUsername() + " has discarded a leader!");
        }
    }

    //leaderActivate message handler
    public void activeLeader(LeaderActivate message){
        if(message.getUsername().equals(client.getMyName())){
            client.getModelOf(client.getMyName()).setActiveLeaderAt(message.getLeaderIndex());
        }
        else{
            client.getModelOf(message.getUsername()).putAsActiveInLeader(message.getLeader());
        }
    }

    //MarketUpdate message handler
    public void marketUpdate(MarketUpdate message){
        client.setMarketData(message.getMarket());
    }

    //CardSlotUpdate message handler
    public void cardSlotUpdate(CardSlotUpdate message){
        CardDevData card = client.getDeckDevData().getCard(message.getRowDeckDevelopment(), message.getColDeckDevelopment());
        int cardSlotIndex = message.getSlotIndex();
        if(message.getUsername().equals(client.getMyName())){
            if (cardSlotIndex >= 0 && cardSlotIndex < 3)
                client.getModelOf(client.getMyName()).addToCardSlot(message.getSlotIndex(), card);
        }
    }

    //BufferUpdate message handler
    public void bufferUpdate(BufferUpdate message){
        String resource;
        if(message.getBufferUpdated().isEmpty()){
            resource="You have just end your operations with the resources!";

        }
        else{
            resource="Buffer: ";
            for(ResourceData r : message.getBufferUpdated()){
                resource+=r.toCli();
            }
        }
        PrintAssistant.instance.printf(resource);
    }

    //ManageResourceRequest message handler
    public void manageResourceRequest(ManageResourcesRequest message){
        if(message.isFromMarket())
            PrintAssistant.instance.printf("Insert those resources in your depots!");
        else
            PrintAssistant.instance.printf("From where you want to take those resources to pay the production?");

        String resource="Buffer: ";
        for(ResourceData r : message.getResources()){
            resource+=r.toCli();
        }
        PrintAssistant.instance.printf(resource);
    }

    //WhiteMarbleConversionRequest message handler
    public void whiteMarbleConversion(WhiteMarbleConversionRequest message){
        //TODO:da stampare un messaggio un po piu decente
        PrintAssistant.instance.printf("there are "+message.getNumOfWhiteMarbleDrew()+" white marble");
    }
}
