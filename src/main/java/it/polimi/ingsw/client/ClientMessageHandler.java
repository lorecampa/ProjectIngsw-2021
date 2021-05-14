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
        //TODO no client yet
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
        //deve mandare anche il faith track se no come faccio ad iniziallizarlo????
    }

    //leaders message handler
    public void leaderSetUp(LeaderSetUpMessage message){
        client.getModelOf(client.getMyName()).setLeader(message.getLeaders());
        client.getModelOf(client.getMyName()).printLeader();
        PrintAssistant.instance.printf("Please choose 2 leader to discard!");
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

    //LeaderStatusUpdate message handler
    /*
    public void leaderUpdateState(LeaderDiscard message){
        if(message.isDiscard()){
            client.getModelOf(message.getUsername()).removeLeaderAt(message.getLeaderIndex());
            PrintAssistant.instance.printf("!");
        }
        else {
            client.getModelOf(message.getUsername()).setActiveLeaderAt(message.getLeaderIndex());
        }
    }

     */

    //leader discard message handler
    public void discardUpdate(LeaderDiscard message){
        if (message.getUsername().equals(client.getMyName())){
            client.getModelOf(client.getMyName()).removeLeaderAt(message.getLeaderIndex());
            client.getModelOf(client.getMyName()).printLeader();
        }else{
            PrintAssistant.instance.printf(message.getUsername() + " has discarded a leader!");
        }
    }




    //MarketUpdate message handler
    public void marketUpdate(MarketUpdate message){
        client.setMarketData(message.getMarket());
    }

    //CardSlotUpdate message handler
    public void cardSlotUpdate(CardSlotUpdate message){
        CardDevData c=client.getDeckDevData().getCard(message.getRowDeckDevelopment(), message.getColDeckDevelopment());
        if(message.getUsername().equals(client.getMyName())){
            switch (message.getSlotIndex()){
                case 0:
                    client.getModelOf(client.getMyName()).addToCardSlotOne(c);
                    break;
                case 1:
                    client.getModelOf(client.getMyName()).addToCardSlotTwo(c);
                    break;
                case 2:
                    client.getModelOf(client.getMyName()).addToCardSlotThree(c);
                    break;
            }
        }
    }
}
