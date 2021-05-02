package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothMessage.ConnectionType;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;

import java.util.ArrayList;

public class Match {
    private final Server server;
    private Controller controller;
    private final int numOfPlayers;
    private final ArrayList<VirtualClient> players;

    public Match(int numOfPlayers, Server server) {
        this.server = server;
        this.numOfPlayers = numOfPlayers;
        this.players = new ArrayList<>();
    }

    public boolean isOpen(){
        return numOfPlayers > players.size();
    }

    public int currentNumOfPLayer(){
        return players.size();
    }

    public int getNumOfPlayers(){return numOfPlayers;}

    public void addPlayer(VirtualClient client){
        if (!players.contains(client)) {
            players.add(client);
            client.getClient().setState(HandlerState.USERNAME);
            client.getClient().writeToStream(new ConnectionMessage(ConnectionType.USERNAME,"Insert your Username"));
            if (!isOpen())
                server.closeOpenMatch();
        }
    }

    public void setPlayerUsername(VirtualClient player, String username){
        synchronized (players){
            boolean valid = true;
            for (VirtualClient otherPlayer : players){
                if (!otherPlayer.equals(player) && otherPlayer.getUsername().equals(username)) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                player.getClient().setState(HandlerState.WAITING);
                player.setUsername(username);
            }
            else
                player.getClient().writeToStream(new ErrorMessage(ErrorType.INVALID_USERNAME));
        }
    }
}
