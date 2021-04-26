package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
                ClientConnectionHandler client = new ClientConnectionHandler(socket, this);
                executorService.submit(client);

            } catch(IOException e) {
                break;
            }
        }
    }

    public int getNextId(){
        return  numOfActivePlayers++;
    }

    public void addClient(String username, ClientConnectionHandler clientConnectionHandler){
        int id = getNextId();
        VirtualClient virtualClient = new VirtualClient(id, username, clientConnectionHandler);
        userVirtualClientMap.put(username, virtualClient);

        virtualClient.sendMessage("[Client] We are happy to have you join the game: " + username);

        System.out.println("[Server] Added: " + virtualClient);

    }

    public static void main(String[] args){

        Server server = new Server();
        server.startServer();


    }
}
