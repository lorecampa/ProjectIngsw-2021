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

/**
 * Manage all interaction of a match.
 */
public class Match{
    private final Server server;
    private Controller controller;
    private final int numOfPlayers;
    private final ArrayList<VirtualClient> allPlayers = new ArrayList<>();
    private final ArrayList<VirtualClient> activePlayers = new ArrayList<>();
    private final ArrayList<VirtualClient> inactivePlayers = new ArrayList<>();
    private final ArrayList<String> logs;
    private final int matchID;

    private final Object playersLock = new Object();

    /**
     * Construct a Match with specific parameters.
     * @param numOfPlayers the number of players.
     * @param server the reference of the server.
     * @param matchID the id of the match.
     */
    public Match(int numOfPlayers, Server server, int matchID) {
        this.server = server;
        this.numOfPlayers = numOfPlayers;
        this.matchID = matchID;
        this.logs = new ArrayList<>();
    }

    /**
     * Construct a Match with specific parameters.
     * @param server the reference of the server.
     * @param matchID the id of the match.
     * @param numOfPlayers the number of player.
     * @param allPlayers the players.
     * @param logs the logs of the match.
     * @param gameMaster the gameMaster of the match.
     */
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

    /**
     * Return true is the there is room for more players.
     * @return true is the there is room for more players.
     */
    public boolean isOpen(){
        return numOfPlayers > currentNumOfPlayer();
    }

    /**
     * Return the current number of players.
     * @return the current number of players.
     */
    public int currentNumOfPlayer(){
        synchronized (playersLock){
            return allPlayers.size();
        }
    }

    /**
     * Return the ArrayList of all players.
     * @return the ArrayList of all players.
     */
    public ArrayList<VirtualClient> getAllPlayers() {
        synchronized (playersLock) {
            return allPlayers;
        }
    }

    /**
     * Return an Optional of Virtual Client of a specific player.
     * @param username the username of the player.
     * @return an Optional of Virtual Client of a specific player.
     */
    public Optional<VirtualClient> getPlayer(String username){
        synchronized (playersLock) {
            return allPlayers.stream().filter(x -> x.getUsername().equals(username)).findFirst();
        }
    }

    /**
     * Return the number of players.
     * @return the number of players.
     */
    public int getNumOfPlayers(){ return numOfPlayers; }

    /**
     * Return the id of the match.
     * @return the id of the match.
     */
    public int getMatchID() { return matchID; }

    /**
     * Return the Controller of the match
     * @return the Controller of the match
     */
    public Controller getController() { return controller; }

    /**
     * Return true if the specific player is inactive.
     * @param username the username of the player.
     * @return true if the specific player is inactive.
     */
    public boolean isInactive(String username){
        synchronized (playersLock){
            return inactivePlayers.stream().anyMatch(x -> x.getUsername().equals(username));
        }
    }

    /**
     * Return true if the specific player is reconnected.
     * @param username the username of the player.
     * @return true if the specific player is reconnected.
     */
    public boolean isReconnected(String username){
        synchronized (playersLock){
            return allPlayers.stream().anyMatch(x -> x.getUsername().equals(username) && x.isReconnected());
        }
    }

    /**
     * Add a player to the match.
     * @param player the player to add.
     */
    public void addPlayer(VirtualClient player){
        synchronized (playersLock) {
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

    /**
     * Add a player to the match when it's a single player game.
     * @param player the player to add.
     */
    public void addForSinglePlayer(VirtualClient player){
        synchronized (playersLock) {
            if (!allPlayers.contains(player)) {
                allPlayers.add(player);
                activePlayers.add(player);
                player.getClient().setState(HandlerState.USERNAME);
                player.getClient().writeToStream(new ReconnectionMessage(matchID, player.getClientID()));
                player.getClient().writeToStream(new ConnectionMessage(ConnectionType.USERNAME, "Insert your Username"));
            }
        }
    }

    /**
     * Set the username of a specific player.
     * @param player the player that username has to be set.
     * @param username the username to set.
     */
    public void setPlayerUsername(VirtualClient player, String username){
        synchronized (playersLock){
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

    /**
     * Handle the disconnection of the player.
     * @param player the player that has been disconnected.
     */
    public void playerDisconnection(VirtualClient player){
        synchronized (playersLock){
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

    /**
     * Handle the reconnection of a player, return true if was able to reconnect the player.
     * @param clientID the client id of the player previously disconnected.
     * @param newClientConnHandler the handler of the new client connection.
     * @return true if was able to reconnect the player.
     */
    public boolean playerReconnection(int clientID, ClientConnectionHandler newClientConnHandler){
        synchronized (playersLock){
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

    /**
     * Add the player to active player when after reconnection the player get the turn.
     * @param username the username of the player.
     */
    public void playerReturnInGame(String username){
        synchronized (playersLock) {
            Optional<VirtualClient> player = allPlayers.stream().filter(x -> x.getUsername().equals(username)).findFirst();
            if (player.isPresent()) {
                activePlayers.add(player.get());
                player.get().setReconnected(false);
            }
        }
    }

    /**
     * Return true if all the players are ready.
     * @return true if all the players are ready.
     */
    public boolean areAllReady(){
        synchronized (playersLock) {
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

    /**
     * Start the game by initialize the controller and send setUp messages to all the player.
     */
    public void startMatch(){
        synchronized (playersLock) {
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

    /**
     * Send the setUp messages to all the players.
     * @param gameMaster the Game Master of the match.
     * @param gameSetting the Game Setting of the match.
     */
    public void sendSetUp(GameMaster gameMaster, GameSetting gameSetting){
        synchronized (playersLock) {
            sendAllPlayers(new ConnectionMessage(ConnectionType.INFO, "Match successfully created"));
            ArrayList<String> usernames = getUsernames();
            ArrayList<ArrayList<FaithTrackData>> faithTracks = new ArrayList<>();
            for (int i = 0; i < usernames.size(); i++) {
                faithTracks.add(gameSetting.getFaithTrack().toFaithTrackData());
            }
            if (usernames.size() == 1) {
                faithTracks.add(gameSetting.getFaithTrack().toFaithTrackData());
            }
            sendAllPlayers(new GameSetup(usernames, gameMaster.getMarket().toMarketData(),
                    gameMaster.toDeckDevData(),
                    faithTracks,
                    gameMaster.toEffectDataBasePro()));
        }
    }

    /**
     * Return all the usernames of the players.
     * @return all the usernames of the players.
     */
    public ArrayList<String> getUsernames(){
        synchronized (playersLock) {
            return allPlayers.stream()
                    .map(VirtualClient::getUsername)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    /**
     * Send the leaders to all the players.
     * @param gameMaster the Game Master of the match.
     */
    public void sendLeader(GameMaster gameMaster){
        synchronized (playersLock) {
            gameMaster.deliverLeaderCards();
            for (VirtualClient virtualClient : allPlayers) {
                virtualClient.getClient().setState(HandlerState.LEADER_SETUP);
                CardManager playerCardMan = gameMaster.getPlayerPersonalBoard(virtualClient.getUsername()).getCardManager();
                ArrayList<CardLeaderData> leaders = playerCardMan.getLeaders()
                        .stream().map(Leader::toCardLeaderData).collect(Collectors.toCollection(ArrayList::new));
                if (inactivePlayers.contains(virtualClient)) {
                    while (playerCardMan.getLeaders().size() > 2)
                        controller.discardLeaderSetUp(playerCardMan.getLeaders().size() - 1, virtualClient.getUsername());
                } else {
                    sendSinglePlayer(virtualClient.getUsername(), new LeaderSetUpMessage(leaders));
                }
            }
        }
    }

    /**
     * Send message to all active players.
     * @param message the message to send.
     */
    public void sendAllPlayers(ClientMessage message) {
        synchronized (playersLock) {
            activePlayers.forEach(x -> x.getClient().writeToStream(message));
        }
    }

    /**
     * Send message to a single active player.
     * @param username the username of the player.
     * @param message the message to send.
     */
    public void sendSinglePlayer(String username, ClientMessage message){
        synchronized (playersLock) {
            activePlayers.stream().filter(x -> x.getUsername().equals(username))
                    .findFirst()
                    .ifPresent(y -> y.getClient().writeToStream(message));
        }
    }

    /**
     * Return the active players.
     * @return the active players.
     */
    public ArrayList<VirtualClient> getActivePlayers() {
        synchronized (playersLock) {
            return activePlayers;
        }
    }

    /**
     * Return the logs of the match.
     * @return the logs of the match.
     */
    public ArrayList<String> getLogs() {
        return logs;
    }

    /**
     * Remove the match from the server.
     */
    public void removeMatchFromServer(){
        synchronized (playersLock) {
            allPlayers.forEach(x -> x.getClient().setState(HandlerState.FIRST_CONTACT));
            System.out.println("Match with index: " + this.matchID + " deleted!");
            try {
                String fileName = Server.MATCH_SAVING_PATH + "/" + getMatchID() + ".txt";
                File file = new File(fileName);
                if (Files.deleteIfExists(file.toPath())) {
                    System.out.println("Match data file deleted!");
                }
            } catch (IOException e) {
                System.out.println("Match data file not deleted");
            }
            server.matchEnd(this);
        }
    }

    /**
     * Add a string to log.
     * @param s the string to add.
     */
    public void addToLog(String s){
        logs.add(s);
    }

    /**
     * Print a set number of logs.
     * @param numToShow the number of log to print.
     */
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
