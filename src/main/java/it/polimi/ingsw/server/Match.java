package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothMessage.ConnectionType;

import java.util.ArrayList;

public class Match {
    private final Server server;
    private Controller controller;
    private int numOfPlayers;
    private ArrayList<VirtualClient> players;

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
            client.getClient().writeToStream(new ConnectionMessage(ConnectionType.USERNAME,"Insert your Username"));
            if (!isOpen())
                server.closeOpenMatch();
        }
    }
}
