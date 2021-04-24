package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.NormalMessage;
import it.polimi.ingsw.util.GsonUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;
    private Scanner in;
    private PrintWriter out;


    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }

    public void writeToStream(String message){
        out.println(message);
        out.flush();
    }

    public String readFromStream(){
        String serializedMessage = in.nextLine();
        Message message = GsonUtil.deserialize(serializedMessage);
        return message.toString();
    }

    public void registerClient(){

        // TODO: chiedere al server se è il primo

        writeToStream("Insert Username: ");

        String username = in.nextLine();
        writeToStream("Received: " + username);
        server.addClient(username, this);
    }

    @Override
    public void run() {
        registerClient();
        while (true) {
            String message = readFromStream();
            if (message.equals("quit")) {
                break;
            } else {
                out.println("Received: " + message);
                out.flush();
            }
        }

        // Chiudo gli stream e il socket
        in.close();
        out.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
