package it.polimi.ingsw.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.message.bothMessage.PingPongMessage;
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
    private Socket socket;
    private final Server server;
    private Scanner in;
    private PrintWriter out;
    ServerMessageHandler serverMessageHandler;
    ObjectMapper mapper = new ObjectMapper();
    private Boolean exit = false;

    private Thread ping;

    private final int clientID;


    public ClientConnectionHandler(Socket socket, Server server, int clientID) throws IOException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        this.socket = socket;
        this.server = server;
        this.clientID = clientID;
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
        serverMessageHandler = new ServerMessageHandler(server,this);

        this.ping = new Thread(() -> {
            while(true) {
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

    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    public void setExit(){exit = true;}

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
            //TODO handle quit with a message
            Optional<ServerMessage> message = Optional.
                    ofNullable(deserialize(serializedMessage));

            message.ifPresentOrElse(
                    x -> x.process(serverMessageHandler),
                    () -> writeToStream(new ErrorMessage(ErrorType.INVALID_MESSAGE)));
        }catch (Exception e){
            //TODO client disconnection unexpected handle
            serverMessageHandler.handleDisconnection();
        }
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

        // Close stream and socket
        in.close();
        out.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
