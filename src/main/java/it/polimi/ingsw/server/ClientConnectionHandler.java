package it.polimi.ingsw.server;


import it.polimi.ingsw.message.ClientMessageHandler;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.util.JacksonSerializer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnectionHandler implements Runnable {
    private Socket socket;
    private Server server;
    private Scanner in;
    private PrintWriter out;
    ClientMessageHandler clientMessageHandler = new ClientMessageHandler();
    JacksonSerializer<ClientMessageHandler> jacksonSerializer = new JacksonSerializer<>();
    private Boolean exit = false;


    public ClientConnectionHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;

        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }


    private void quitter(){
        String message;
        while(true){
            message = in.nextLine();
            if (message.equals("quit")){
                exit  = true;
                break;
            }
        }

    }

    public void writeToStream(String message){
        out.println(message);
        out.flush();
    }

    public void readFromStream(){
        String serializedMessage = in.nextLine();
        writeToStream("Message received correctly\n.\n.\n.");
        Message<ClientMessageHandler> message = jacksonSerializer.deserializeMessage(serializedMessage);
        if (message == null){
            writeToStream("Message doesn't make sense");
        }else{
            message.process(clientMessageHandler);
        }
    }

    public void registerClient(){

        // TODO: chiedere al server se è il primo

        writeToStream("Insert Username: ");

        String username = in.nextLine();
        server.addClient(username, this);
    }

    @Override
    public void run() {
        registerClient();
        //new Thread(this::quitter).start();

        while (!exit) {
            readFromStream();
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
