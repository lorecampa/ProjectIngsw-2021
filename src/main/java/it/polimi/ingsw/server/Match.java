package it.polimi.ingsw.server;

import it.polimi.ingsw.client.data.CardLeaderData;
import it.polimi.ingsw.client.data.FaithTrackData;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.clientMessage.*;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.GameSetting;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Match{
    private final Server server;
    private Controller controller;
    private final int numOfPlayers;
    private final ArrayList<VirtualClient> allPlayers = new ArrayList<>();
    private final ArrayList<VirtualClient> activePlayers = new ArrayList<>();
    private final ArrayList<VirtualClient> inactivePlayers = new ArrayList<>();
    private final ArrayList<String> logs;
    private final int matchID;


    public Match(int numOfPlayers, Server server, int matchID) {
        this.server = server;
        this.numOfPlayers = numOfPlayers;
        this.matchID = matchID;
        this.logs = new ArrayList<>();
    }

    public Match(Server server, int matchID, int numOfPlayers,
                 Map<String, Integer> allPlayers,
                 ArrayList<String> logs,
                 GameMaster gameMaster){

        this.server = server;
        this.matchID = matchID;
        this.numOfPlayers = numOfPlayers;
        this.logs = logs;

        for (String username: allPlayers.keySet()){
            VirtualClient vc = new VirtualClient(username, this, allPlayers.get(username));
            this.allPlayers.add(vc);
            this.inactivePlayers.add(vc);
        }
        gameMaster.restoreReferenceAfterServerQuit();
        this.controller = new Controller(gameMaster, this);
    }

    public boolean isOpen(){
        return numOfPlayers > currentNumOfPlayer();
    }

    public int currentNumOfPlayer(){
        synchronized (allPlayers){
            return allPlayers.size();
        }
    }

    public ArrayList<VirtualClient> getAllPlayers() {
        return allPlayers;
    }

    public Optional<VirtualClient> getPlayer(String username){
        return allPlayers.stream().filter(x->x.getUsername().equals(username)).findFirst();
    }

    public int getNumOfPlayers(){ return numOfPlayers; }

    public int getMatchID() { return matchID; }

    public Controller getController() { return controller; }

    public boolean isInactive(String username){
        synchronized (allPlayers){
            synchronized (inactivePlayers){
                return inactivePlayers.stream().anyMatch(x -> x.getUsername().equals(username));
            }
        }
    }

    public boolean isReconnected(String username){
        synchronized (allPlayers){
            return allPlayers.stream().anyMatch(x -> x.getUsername().equals(username) && x.isReconnected());
        }
    }

    public void addPlayer(VirtualClient player){
        synchronized (allPlayers) {
            synchronized (activePlayers) {
                if (!allPlayers.contains(player)) {
                    allPlayers.add(player);
                    activePlayers.add(player);
                    player.getClient().setState(HandlerState.USERNAME);
                    player.getClient().writeToStream(new ReconnectionMessage(matchID, player.getClientID()));
                    player.getClient().writeToStream(new ConnectionMessage(ConnectionType.USERNAME, "Insert your Username"));
                    if (!isOpen())
                        server.closeOpenMatch();
                }
            }
        }
    }

    public void addForSinglePlayer(VirtualClient player){
        synchronized (allPlayers) {
            synchronized (activePlayers) {
                if (!allPlayers.contains(player)) {
                    allPlayers.add(player);
                    activePlayers.add(player);
                    player.getClient().setState(HandlerState.USERNAME);
                    player.getClient().writeToStream(new ReconnectionMessage(matchID, player.getClientID()));
                    player.getClient().writeToStream(new ConnectionMessage(ConnectionType.USERNAME, "Insert your Username"));
                }
            }
        }
    }

    public void setPlayerUsername(VirtualClient player, String username){
        synchronized (allPlayers){

            boolean valid = true;
            if (username.length() == 0)
                valid = false;
            else {
                for (VirtualClient otherPlayer : allPlayers) {
                    if ((!otherPlayer.equals(player) && otherPlayer.getUsername().equals(username))
                            || username.equalsIgnoreCase("LorenzoIlMagnifico")) {
                        valid = false;
                        break;
                    }
                }
            }
            if (valid) {
                player.getClient().setState(HandlerState.WAITING_LOBBY);
                player.getClient().writeToStream(new ConnectionMessage(ConnectionType.WAIT_PLAYERS,"Wait until match starts"));
                player.setUsername(username);
                if (areAllReady())
                    startMatch();
            }
            else
                player.getClient().writeToStream(new ErrorMessage(ErrorType.INVALID_USERNAME));

        }
    }



    public void playerDisconnection(VirtualClient player){
        synchronized (allPlayers){
            synchronized (activePlayers){
                synchronized (inactivePlayers){

                    if (player.isReady()) {
                        player.getClient().setExit(true);

                        inactivePlayers.add(player);
                        activePlayers.remove(player);
                        if(activePlayers.size()==0){
                            removeMatchFromServer();
                        }
                        else if(player.getClient().getState() == HandlerState.IN_MATCH && player.getUsername()
                                .equals(controller.getCurrentPlayer())){
                            controller.nextTurn();
                        }else if (player.getClient().getState() == HandlerState.LEADER_SETUP){
                            controller.autoDiscardLeaderSetUp(player.getUsername());
                        }else if (player.getClient().getState() == HandlerState.RESOURCE_SETUP){
                            controller.autoInsertSetUpResources(player.getUsername());
                        }
                    }
                    else {
                        allPlayers.remove(player);
                        activePlayers.remove(player);
                        server.clientDisconnect(player.getClient());
                        server.putInToFill(this);
                    }

                }
            }
        }
    }

    public boolean playerReconnection(int clientID, ClientConnectionHandler newClientConnHandler){
        synchronized (activePlayers){
            synchronized (inactivePlayers){

                for (VirtualClient virtualClient : inactivePlayers){
                    if (virtualClient.getClientID() == clientID){

                        ServerMessageHandler handler = new ServerMessageHandler(server, newClientConnHandler);
                        handler.setController(getController());
                        handler.setVirtualClient(virtualClient);
                        handler.setServerPhase(HandlerState.IN_MATCH);

                        newClientConnHandler.setServerMessageHandler(handler);
                        newClientConnHandler.setClientID(clientID);
                        virtualClient.setClient(newClientConnHandler);
                        inactivePlayers.remove(virtualClient);
                        virtualClient.setReconnected(true);

                        String username = virtualClient.getUsername();
                        if (getController().getCurrentPlayer().equals(username)){
                            playerReturnInGame(username);
                            sendSinglePlayer(username, controller.reconnectGameMessage(username));
                        }else{
                            virtualClient.getClient().writeToStream(new ConnectionMessage(ConnectionType.RECONNECTION,virtualClient.getUsername()));
                        }

                        return true;
                    }
                }
                return false;

            }
        }
    }

    public void playerReturnInGame(String username){
        Optional<VirtualClient> player = allPlayers.stream().filter(x -> x.getUsername().equals(username)).findFirst();
        if (player.isPresent()){
            activePlayers.add(player.get());
            player.get().setReconnected(false);
        }
    }

    public boolean areAllReady(){
        synchronized (allPlayers) {
            if (!isOpen()) {
                boolean allReady = true;
                for (VirtualClient virtualClient : allPlayers) {
                    allReady = allReady && virtualClient.isReady();
                }
                return allReady;
            }
            return false;
        }
    }

    public void startMatch(){
        synchronized (allPlayers) {
            ArrayList<String> playersUsername = allPlayers.stream().map(VirtualClient::getUsername)
                    .collect(Collectors.toCollection(ArrayList::new));

            GameSetting gameSetting;
            GameMaster gameMaster;
            try {
                gameSetting = new GameSetting(numOfPlayers);
                gameMaster = new GameMaster(gameSetting, playersUsername);
                //added match to controller (Lorenzo)
                controller = new Controller(gameMaster, this);
                for (VirtualClient player: allPlayers){
                    player.getClient().getServerMessageHandler().setController(controller);
                }

                sendSetUp(gameMaster, gameSetting);
                sendLeader(gameMaster);
            } catch ( Exception e) {
                e.printStackTrace();
                sendAllPlayers(new ErrorMessage(ErrorType.FAIL_GAME_LOADING));
            }

        }
    }

    public void sendSetUp(GameMaster gameMaster, GameSetting gameSetting){
        sendAllPlayers(new ConnectionMessage(ConnectionType.INFO,"Match successfully created"));
        ArrayList<String> usernames = getUsernames();
        ArrayList<ArrayList<FaithTrackData>> faithTracks = new ArrayList<>();
        for (int i = 0; i < usernames.size(); i++) {
            faithTracks.add(gameSetting.getFaithTrack().toFaithTrackData());
        }
        if(usernames.size()==1){
            faithTracks.add(gameSetting.getFaithTrack().toFaithTrackData());
        }
        sendAllPlayers(new GameSetup(usernames,gameMaster.getMarket().toMarketData(),
                gameMaster.toDeckDevData(),
                faithTracks,
                gameMaster.toEffectDataBasePro()));
    }

    public ArrayList<String> getUsernames(){
        return allPlayers.stream()
                .map(VirtualClient::getUsername)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void sendLeader(GameMaster gameMaster){
        gameMaster.deliverLeaderCards();
        for (VirtualClient virtualClient : allPlayers){
            virtualClient.getClient().setState(HandlerState.LEADER_SETUP);
            CardManager playerCardMan = gameMaster.getPlayerPersonalBoard(virtualClient.getUsername()).getCardManager();
            ArrayList<CardLeaderData> leaders = playerCardMan.getLeaders()
                    .stream().map(Leader::toCardLeaderData).collect(Collectors.toCollection(ArrayList::new));
            if (inactivePlayers.contains(virtualClient)){
                while(playerCardMan.getLeaders().size() > 2)
                    controller.discardLeaderSetUp(playerCardMan.getLeaders().size() - 1, virtualClient.getUsername());
            }else{
                sendSinglePlayer(virtualClient.getUsername(), new LeaderSetUpMessage(leaders));
            }

        }
    }

    public void sendAllPlayers(ClientMessage message) {
        synchronized (activePlayers) {
            activePlayers.forEach(x -> x.getClient().writeToStream(message));
        }
    }

    public void sendSinglePlayer(String username, ClientMessage message){
        activePlayers.stream().filter(x -> x.getUsername().equals(username))
                .findFirst()
                .ifPresent(y -> y.getClient().writeToStream(message));
    }

    public ArrayList<VirtualClient> getActivePlayers() {
        return activePlayers;
    }

    public ArrayList<VirtualClient> getInactivePlayers() {
        return inactivePlayers;
    }

    public ArrayList<String> getLogs() {
        return logs;
    }

    public void removeMatchFromServer(){
        allPlayers.forEach(x-> x.getClient().setState(HandlerState.FIRST_CONTACT));
        System.out.println("Match with index: "+this.matchID+" deleted!");
        try {
            String fileName = Server.MATCH_SAVING_PATH +"/"+ getMatchID()+ ".txt";
            File file = new File(fileName);
            if(Files.deleteIfExists(file.toPath())){
                System.out.println("Match data file deleted!");
            }
        } catch (IOException e) {
            System.out.println("Match data file not deleted");
        }
        server.matchEnd(this);
    }

    public void addToLog(String s){
        logs.add(s);
    }

    public void printLogs(int numToShow){
        int start;
        if(numToShow>logs.size()-1){
            start=0;
        }
        else{
            start= logs.size()-numToShow;
        }
        for(int i=start; i<logs.size();i++){
            System.out.println(logs.get(i));
        }
    }



}
