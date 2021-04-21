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
    int numOfPlayer;

    private Map<String, VirtualClient> userVirtualClientMap;


    public  Server(){
        port = 2020;
        executorService = Executors.newCachedThreadPool();
        userVirtualClientMap = new HashMap<>();
        numOfPlayer = 0;
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
                System.out.println("Client-Server socket created");
                ClientHandler client = new ClientHandler(socket, this);
                executorService.submit(client);

            } catch(IOException e) {
                break;
            }
        }
    }

    public int getNextId(){
        return  numOfPlayer++;
    }

    public void addClient(String username, ClientHandler client){
        int id = getNextId();
        VirtualClient virtualClient = new VirtualClient(id, username, client);
        userVirtualClientMap.put(username, virtualClient);

        virtualClient.sendMessage(new Message("You are added!", 0));
        System.out.println("Added: " + virtualClient);

    }

    public static void main(String[] args){

        Server server = new Server();
        server.startServer();


    }
}
