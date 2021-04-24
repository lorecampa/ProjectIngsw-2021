package it.polimi.ingsw.server;

import it.polimi.ingsw.message.CommandMessage;
import it.polimi.ingsw.message.MessageType;
import it.polimi.ingsw.message.NormalMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    int port;
    ExecutorService executorService;
    ServerSocket serverSocket;
    int numOfActivePlayers;

    private Map<String, VirtualClient> userVirtualClientMap;


    public  Server(){
        port = 2020;
        executorService = Executors.newCachedThreadPool();
        userVirtualClientMap = new HashMap<>();
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

    public void acceptConnection(){
        while (true){
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Server Socket has accepted a connection");
                ClientHandler client = new ClientHandler(socket, this);
                executorService.submit(client);

            } catch(IOException e) {
                break;
            }
        }
    }

    public int getNextId(){
        return  numOfActivePlayers++;
    }

    public void addClient(String username, ClientHandler clientHandler){
        int id = getNextId();
        VirtualClient virtualClient = new VirtualClient(id, username, clientHandler);
        userVirtualClientMap.put(username, virtualClient);

        virtualClient.sendMessage(new NormalMessage(MessageType.INFO));

        System.out.println("Added: " + virtualClient);

    }

    public static void main(String[] args){

        Server server = new Server();
        server.startServer();


    }
}
