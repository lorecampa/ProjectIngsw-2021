package it.polimi.ingsw.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.message.serverMessage.ServerMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;

public class ClientConnectionHandler implements Runnable {
    private final Socket socket;
    private final Server server;
    private final Scanner in;
    private final PrintWriter out;
    ServerMessageHandler serverMessageHandler;
    ObjectMapper mapper = new ObjectMapper();
    private Boolean exit = false;
    private final int clientID;


    public ClientConnectionHandler(Socket socket, Server server, int clientID) throws IOException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        this.socket = socket;
        this.server = server;
        this.clientID = clientID;
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
        serverMessageHandler = new ServerMessageHandler(server,this);
    }

    public int getClientID() {
        return clientID;
    }

    public void setState(HandlerState state){
        serverMessageHandler.setState(state);
    }

    public void setVirtualClient(VirtualClient virtualClient){
        serverMessageHandler.setVirtualClient(virtualClient);
    }

    public void writeToStream(ClientMessage message){
        Optional<String> serializedMessage = Optional.ofNullable(serialize(message));
        serializedMessage.ifPresentOrElse(out::println,
                ()-> out.println("Error in serialization"));
        out.flush();
    }

    public void readFromStream(){
        String serializedMessage = in.nextLine();
        if (serializedMessage.equalsIgnoreCase("QUIT")){
            exit = true;
            return;
        }

        Optional<ServerMessage> message = Optional.
                ofNullable(deserialize(serializedMessage));

        message.ifPresentOrElse(
                x -> x.process(serverMessageHandler),
                () -> writeToStream(new ErrorMessage(ErrorType.INVALID_MESSAGE)));

    }

    public ServerMessage deserialize(String serializedMessage){
        ServerMessage message;
        try {
            message = mapper.readValue(serializedMessage, ServerMessage.class);
        } catch (JsonProcessingException e) {
            return null;
        }
        return message;
    }

    public String serialize(ClientMessage message){
        String serializedMessage;
        try {
            serializedMessage = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            serializedMessage = null;
        }
        return serializedMessage;
    }


    @Override
    public void run() {
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
