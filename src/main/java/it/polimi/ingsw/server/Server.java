package it.polimi.ingsw.server;

import it.polimi.ingsw.message.ConnectionMessage;
import it.polimi.ingsw.message.ConnectionType;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private final int port;
    private final ExecutorService executorService;
    private ServerSocket serverSocket;
    private int nextClientID;
    private int nextMatchID;

    private final ArrayList<ClientConnectionHandler> lobby;

    private final ArrayList<Match> matches;
    private final ArrayList<Match> matchesToFill;

    private final Object lockOpenMatch = new Object();
    private Match openMatch;


    public  Server(){
        port = 2020;
        executorService = Executors.newCachedThreadPool();
        lobby = new ArrayList<>();
        matches = new ArrayList<>();
        matchesToFill = new ArrayList<>();
        nextClientID = 0;
        nextMatchID = 0;

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
                synchronized (matchesToFill) {
                    lobby.subList(0, openMatch.getNumOfPlayers()).clear();
                    openMatch = null;
                    if (matchesToFill.size() == 0) {
                        if (lobby.size() > 0) {
                            lobby.get(0).setState(HandlerState.NUM_OF_PLAYER);
                            lobby.get(0).writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER, "Insert the number of Players: "));
                        }
                    }else{
                        openMatch = matchesToFill.get(0);
                        for (int i = 0; i < openMatch.getNumOfPlayers() && i < lobby.size(); i++) {
                            openMatch.addPlayer(new VirtualClient(lobby.get(i).getClientID(),
                                    "Quest_".concat(String.valueOf(openMatch.currentNumOfPLayer())),lobby.get(i),openMatch));
                        }
                    }
                }
            }
        }
    }

    public void createMatch(int numOfPlayer, ClientConnectionHandler player){
        Match newMatch = new Match(numOfPlayer, this, getNextMatchID());
        synchronized (lobby){
            if (!lobby.contains(player) || !lobby.get(0).equals(player))
                return;
            synchronized (lockOpenMatch) {
                openMatch = newMatch;
                matches.add(newMatch);
                newMatch.addPlayer(new VirtualClient(player.getClientID(),"Quest_".concat(String.valueOf(newMatch.currentNumOfPLayer())),player,openMatch));
            }
            for (int i = 1; i < newMatch.getNumOfPlayers() && i < lobby.size(); i++) {
                newMatch.addPlayer(new VirtualClient(lobby.get(i).getClientID(),"Quest_".concat(String.valueOf(newMatch.currentNumOfPLayer())),lobby.get(i),openMatch));
            }
        }
    }

    public void putInLobby(ClientConnectionHandler client){
        synchronized (lobby){
            if (!lobby.contains(client)){
                lobby.add(client);
                if (lobby.get(0).equals(client)){
                    client.setState(HandlerState.NUM_OF_PLAYER);
                    client.writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER,"Insert the number of Players: "));
                }else{
                    synchronized (lockOpenMatch){
                        if (openMatch != null && openMatch.isOpen()){
                            openMatch.addPlayer(new VirtualClient(client.getClientID(),"Quest_".concat(String.valueOf(openMatch.currentNumOfPLayer())),client,openMatch));
                        }else{
                            client.setState(HandlerState.WAITING_LOBBY);
                            client.writeToStream(new ConnectionMessage(ConnectionType.WAIT_PLAYERS,"Waiting Players!"));
                        }
                    }
                }
            }
        }
    }

    public void clientDisconnect(ClientConnectionHandler client){
        synchronized (lobby){
            client.setExit();
            lobby.remove(client);
            if ((client.getState() == HandlerState.NUM_OF_PLAYER)&&(lobby.size() > 0))
            {
                lobby.get(0).setState(HandlerState.NUM_OF_PLAYER);
                lobby.get(0).writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER, "Insert the number of Players: "));
            }
        }
    }

    public void clientReconnection(int matchID, int clientID, ClientConnectionHandler client){
        synchronized (matches){
            boolean reconnected = false;
            for (Match match : matches){
                if (match.getMatchID() == matchID)
                    reconnected = match.playerReconnection(clientID, client.getSocket());
            }
            if (reconnected)
                client.writeToStream(new ConnectionMessage(ConnectionType.RECONNECTION,"Reconnected"));
            else
                client.writeToStream(new ErrorMessage(ErrorType.FAIL_RECONNECTION));
        }
    }

    public void putInToFill(Match matchToFill){
        synchronized (matchesToFill) {
            if (matchToFill != openMatch) {
                matchesToFill.add(matchToFill);
            }
        }
    }

    public int getNextClientID(){
        return  nextClientID++;
    }

    public int getNextMatchID(){
        return nextMatchID++;
    }

    public void acceptConnection(){
        while (true){
            try {
                Socket socket = serverSocket.accept();
                //socket.setSoTimeout(5000);
                System.out.println("Server Socket has accepted a connection");
                ClientConnectionHandler client = new ClientConnectionHandler(socket, this, getNextClientID());
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
