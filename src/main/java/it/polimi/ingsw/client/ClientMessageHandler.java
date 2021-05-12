package it.polimi.ingsw.client;


import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.bothArchitectureMessage.PingPongMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;
import it.polimi.ingsw.model.resource.Resource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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

    //NewTurn message handler
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
        PrintAssistant.instance.printf("setUpArrived!");
        client.setModels(message.getUsernames());
        client.setMarketData(message.getMarket());
        client.setDeckDevData(message.getDeckDev());
        //deve mandare anche il faith track se no come faccio ad iniziallizarlo????
    }

    //leaders message handler
    public void leaderSetUp(LeaderSetUpMessage message){
        PrintAssistant.instance.printf("leaders arrived!");
        client.getModelOf(client.getMyName()).setLeader(message.getLeaders());
        client.getModelOf(client.getMyName()).printLeader();
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
}
