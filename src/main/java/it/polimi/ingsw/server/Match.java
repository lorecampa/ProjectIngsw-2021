package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exception.JsonFileModificationError;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.GameSetting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Match {
    private final Server server;
    private Controller controller;
    private final int numOfPlayers;

    private final ArrayList<VirtualClient> allPlayers;
    private final ArrayList<VirtualClient> activePlayers;
    private final ArrayList<VirtualClient> inactivePlayers;

    private final int matchID;

    public Match(int numOfPlayers, Server server, int matchID) {
        this.server = server;
        this.numOfPlayers = numOfPlayers;

        this.allPlayers = new ArrayList<>();
        this.activePlayers = new ArrayList<>();
        this.inactivePlayers = new ArrayList<>();
        this.matchID = matchID;
    }

    public boolean isOpen(){
        return numOfPlayers > currentNumOfPLayer();
    }

    public int currentNumOfPLayer(){
        synchronized (allPlayers){
            return allPlayers.size();
        }
    }

    public ArrayList<VirtualClient> getAllPlayers() {
        return allPlayers;
    }

    public int getNumOfPlayers(){ return numOfPlayers; }

    public int getMatchID() { return matchID; }

    public Controller getController() { return controller; }

    public void addPlayer(VirtualClient client){
        synchronized (allPlayers) {
            synchronized (activePlayers) {
                if (!allPlayers.contains(client)) {
                    allPlayers.add(client);
                    activePlayers.add(client);
                    client.getClient().setState(HandlerState.USERNAME);
                    client.getClient().writeToStream(new ConnectionMessage(ConnectionType.USERNAME, "Insert your Username"));
                    if (!isOpen())
                        server.closeOpenMatch();
                }
            }
        }
    }

    public void setPlayerUsername(VirtualClient player, String username){
        synchronized (allPlayers){

            boolean valid = true;
            for (VirtualClient otherPlayer : allPlayers){
                if ((!otherPlayer.equals(player) && otherPlayer.getUsername().equals(username))
                        ||username.equalsIgnoreCase("LorenzoIlMagnifico")) {
                    valid = false;
                    break;
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
                    if (virtualClient.getId() == clientID){

                        newClientConnHandler.setServerMessageHandler(virtualClient.getClient().getServerMessageHandler());
                        newClientConnHandler.getServerMessageHandler().setClient(newClientConnHandler);
                        newClientConnHandler.setClientID(virtualClient.getClient().getClientID());

                        virtualClient.setClient(newClientConnHandler);

                        inactivePlayers.remove(virtualClient);
                        activePlayers.add(virtualClient);

                        virtualClient.getClient().writeToStream(new ConnectionMessage(ConnectionType.RECONNECTION,"Reconnected"));

                        return true;
                    }
                }
                return false;

            }
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
                sendAllPlayers(new ConnectionMessage(ConnectionType.INFO,"Match successfully created"));
            } catch (IOException | JsonFileModificationError e) {
                e.printStackTrace();
                sendAllPlayers(new ErrorMessage(ErrorType.FAIL_GAME_LOADING));
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
}
