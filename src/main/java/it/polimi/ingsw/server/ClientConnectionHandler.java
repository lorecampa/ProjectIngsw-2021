package it.polimi.ingsw.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.message.ServerMessageHandler;
import it.polimi.ingsw.message.serverMessage.ServerMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnectionHandler implements Runnable {
    private Socket socket;
    private Server server;
    private Scanner in;
    private PrintWriter out;
    ServerMessageHandler serverMessageHandler = new ServerMessageHandler();
    ObjectMapper mapper = new ObjectMapper();


    private Boolean exit = false;


    public ClientConnectionHandler(Socket socket, Server server) throws IOException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        this.socket = socket;
        this.server = server;
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }



    public void writeToStream(String message){
        out.println(message);
        out.flush();
    }

    public void readFromStream(){
        String serializedMessage = in.nextLine();
        if (serializedMessage.equalsIgnoreCase("QUIT")){
            exit = true;
            return;
        }

        writeToStream("Message received correctly\n.\n.");
        ServerMessage message = deserialize(serializedMessage);
        if (message == null){
            writeToStream("Message doesn't make sense\n");
        }else{
            writeToStream("Message make sense\n");
            message.process(serverMessageHandler);
        }
    }

    public void registerClient(){

        // TODO: chiedere al server se è il primo

        writeToStream("Insert Username: ");

        String username = in.nextLine();
        server.addClient(username, this);
    }

    public ServerMessage deserialize(String serializedMessage){
        ServerMessage message;
        try {
            message = mapper.readValue(serializedMessage, ServerMessage.class);
        } catch (JsonProcessingException e) {
            //TODO change how to send an error
            return null;
        }
        return message;
    }

    public String serialize(ServerMessage message){
        String serializedMessage;
        try {
            serializedMessage = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            serializedMessage =  "Error in serializing";
        }
        return serializedMessage;
    }


    @Override
    public void run() {
        registerClient();
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
