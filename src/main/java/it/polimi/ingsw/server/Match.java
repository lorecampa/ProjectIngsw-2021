package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothMessage.ConnectionType;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Match {
    private final Server server;
    private Controller controller;
    private final int numOfPlayers;
    private final ArrayList<VirtualClient> players;
    private final ArrayList<VirtualClient> inactivePlayers;

    private final int matchID;

    public Match(int numOfPlayers, Server server, int matchID) {
        this.server = server;
        this.numOfPlayers = numOfPlayers;
        this.players = new ArrayList<>();
        this.inactivePlayers = new ArrayList<>();
        this.matchID = matchID;
    }

    public boolean isOpen(){
        return numOfPlayers > players.size();
    }

    public int currentNumOfPLayer(){
        return players.size();
    }

    public int getNumOfPlayers(){return numOfPlayers;}

    public int getMatchID() { return matchID; }

    public void addPlayer(VirtualClient client){
        synchronized (players) {
            if (!players.contains(client)) {
                players.add(client);
                client.getClient().setState(HandlerState.USERNAME);
                client.getClient().writeToStream(new ConnectionMessage(ConnectionType.USERNAME, "Insert your Username"));
                if (!isOpen())
                    server.closeOpenMatch();
            }
        }
    }

    public void setPlayerUsername(VirtualClient player, String username){
        synchronized (players){
            boolean valid = true;
            for (VirtualClient otherPlayer : players){
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
            }
            else
                player.getClient().writeToStream(new ErrorMessage(ErrorType.INVALID_USERNAME));
        }
    }

    public void playerDisconnection(VirtualClient player){
        synchronized (players) {
            synchronized (inactivePlayers) {
                if (player.isReady())
                    inactivePlayers.add(player);
                else {
                    players.remove(player);
                    server.clientDisconnect(player.getClient());
                    server.putInToFill(this);
                }
            }
        }
    }

    public boolean playerReconnection(int clientID, Socket socket){
        synchronized (players){
            synchronized (inactivePlayers){
                for (VirtualClient virtualClient : inactivePlayers){
                    if (virtualClient.getId() == clientID){
                        try {
                            virtualClient.getClient().setSocket(socket);
                        } catch (IOException e) {
                            return false;
                        }
                        inactivePlayers.remove(virtualClient);
                        return true;
                    }
                }
                return false;
            }
        }
    }
}
