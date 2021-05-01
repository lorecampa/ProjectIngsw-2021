package it.polimi.ingsw.server;

import it.polimi.ingsw.message.bothMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothMessage.ConnectionType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private int port;
    private ExecutorService executorService;
    private ServerSocket serverSocket;
    private int numOfActivePlayers;

    private final ArrayList<ClientConnectionHandler> lobby;

    private final ArrayList<Match> matches;

    private final Object lockOpenMatch = new Object();
    private Match openMatch;


    public  Server(){
        port = 2020;
        executorService = Executors.newCachedThreadPool();
        lobby = new ArrayList<>();
        matches = new ArrayList<>();
        numOfActivePlayers = 0;
    }

    public void startServer(){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server ready");
        acceptConnection();

    }

    public void closeOpenMatch(){
        synchronized (lobby) {
            synchronized (lockOpenMatch) {
                lobby.subList(0, openMatch.getNumOfPlayers()).clear();
                openMatch = null;
                if (lobby.size() > 0)
                    lobby.get(0).writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER, "Insert the number of Players"));
            }
        }
    }

    public void createMatch(int numOfPlayer, ClientConnectionHandler player){
        Match newMatch = new Match(numOfPlayer, this);
        synchronized (lobby){
            if (!lobby.contains(player) || !lobby.get(0).equals(player))
                return;
            synchronized (lockOpenMatch) {
                openMatch = newMatch;
                matches.add(newMatch);
                newMatch.addPlayer(new VirtualClient(player.getClientID(),"Quest".concat(String.valueOf(newMatch.currentNumOfPLayer())),player));
            }
            for (int i = 1; i < newMatch.getNumOfPlayers() && i < lobby.size(); i++) {
                newMatch.addPlayer(new VirtualClient(lobby.get(i).getClientID(),"Quest".concat(String.valueOf(newMatch.currentNumOfPLayer())),lobby.get(i)));
            }
        }
    }

    public void putInLobby(ClientConnectionHandler client){
        synchronized (lobby){
            if (!lobby.contains(client)){
                lobby.add(client);
                if (lobby.get(0).equals(client)){
                    client.writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER,"Insert the number of Players"));
                }else{
                    synchronized (lockOpenMatch){
                        if (openMatch != null && openMatch.isOpen()){
                            openMatch.addPlayer(new VirtualClient(client.getClientID(),"Quest".concat(String.valueOf(openMatch.currentNumOfPLayer())),client));
                        }else client.writeToStream(new ConnectionMessage(ConnectionType.WAIT_PLAYERS,"Insert the number of Players"));
                    }
                }
            }
        }
    }

    public int getNextId(){
        return  numOfActivePlayers++;
    }

    public void acceptConnection(){
        while (true){
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Server Socket has accepted a connection");
                ClientConnectionHandler client = new ClientConnectionHandler(socket, this, getNextId());
                executorService.submit(client);
            } catch(IOException e) {
                break;
            }
        }
    }

    public static void main(String[] args){
        Server server = new Server();
        server.startServer();
    }
}
