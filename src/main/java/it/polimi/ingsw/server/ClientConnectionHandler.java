package it.polimi.ingsw.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.message.bothArchitectureMessage.PingPongMessage;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.message.serverMessage.ServerMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientConnectionHandler implements Runnable {
    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;

    private final ObjectMapper mapper = new ObjectMapper();

    private boolean exit = false;

    private ServerMessageHandler serverMessageHandler;
    private int clientID;


    public ClientConnectionHandler(Socket socket, Server server, int clientID) throws IOException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        this.socket = socket;
        this.clientID = clientID;
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
        serverMessageHandler = new ServerMessageHandler(server,this);

    }

    private void startPinging(){
        Thread ping = new Thread(() -> {
            while (!exit) {
                writeToStream(new PingPongMessage());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ping.start();
    }

    public void setExit(boolean exit) { this.exit = exit; }

    public int getClientID() {
        return clientID;
    }

    public void setState(HandlerState state){
        serverMessageHandler.setState(state);
    }

    public HandlerState getState(){return serverMessageHandler.getState();}

    public void setVirtualClient(VirtualClient virtualClient){
        serverMessageHandler.setVirtualClient(virtualClient);
    }

    public ServerMessageHandler getServerMessageHandler() { return serverMessageHandler; }

    public void setServerMessageHandler(ServerMessageHandler serverMessageHandler) {  this.serverMessageHandler = serverMessageHandler; }

    public void setClientID(int clientID) { this.clientID = clientID; }

    public void writeToStream(ClientMessage message){
        Optional<String> serializedMessage = Optional.ofNullable(serialize(message));
        serializedMessage.ifPresentOrElse(out::println,
                ()-> out.println("Error in serialization"));
        out.flush();
    }

    public void readFromStream(){
        String serializedMessage;
        try {
            serializedMessage = in.nextLine();

            Optional<ServerMessage> message = Optional.
                    ofNullable(deserialize(serializedMessage));

            message.ifPresentOrElse(
                    x -> x.process(serverMessageHandler),
                    () -> writeToStream(new ErrorMessage(ErrorType.INVALID_MESSAGE)));
            //TODO handle quit with a message
        }catch (Exception e){
            System.out.println("client disconnection");
            serverMessageHandler.handleDisconnection();
        }

    }

    public ServerMessage deserialize(String serializedMessage){
        ServerMessage message;
        if (serializedMessage == null) return null;
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

        startPinging();

        while (!exit) {
            readFromStream();
        }

        in.close();
        out.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
