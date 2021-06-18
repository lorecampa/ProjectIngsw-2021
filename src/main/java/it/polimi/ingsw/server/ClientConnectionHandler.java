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

/**
 * Class that manage the interaction with the client.
 */
public class ClientConnectionHandler implements Runnable {
    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;

    private final Object streamLock = new Object();

    private final ObjectMapper mapper = new ObjectMapper();

    private boolean exit = false;

    private ServerMessageHandler serverMessageHandler;
    private int clientID;

    /**
     * Construct a Client Connection Handler of a specific client.
     * @param socket the socket of the Server-Client connection.
     * @param server the reference of the Server.
     * @param clientID the id of the client.
     * @throws IOException if an error occurs during creation
     */
    public ClientConnectionHandler(Socket socket, Server server, int clientID) throws IOException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.socket = socket;
        this.clientID = clientID;
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
        serverMessageHandler = new ServerMessageHandler(server,this);
    }

    /**
     * Start a pinging thread to the client.
     */
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

    /**
     * Set the value of exit.
     * @param exit the new exit value.
     */
    public void setExit(boolean exit) { this.exit = exit; }

    /**
     * Return the clientID.
     * @return the clientID.
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * Set the HandlerState.
     * @param state the new handlerState value.
     */
    public void setState(HandlerState state){
        serverMessageHandler.setServerPhase(state);
    }

    /**
     * Return the HandlerState.
     * @return the HandlerState.
     */
    public HandlerState getState(){return serverMessageHandler.getServerPhase();}

    /**
     * Set the Virtual client.
     * @param virtualClient the new Virtual Client.
     */
    public void setVirtualClient(VirtualClient virtualClient){
        serverMessageHandler.setVirtualClient(virtualClient);
    }

    /**
     * Return the ServerMessageHandler.
     * @return the ServerMessageHandler.
     */
    public ServerMessageHandler getServerMessageHandler() { return serverMessageHandler; }

    /**
     * Set the ServerMessageHandler.
     * @param serverMessageHandler the ServerMessageHandler.
     */
    public void setServerMessageHandler(ServerMessageHandler serverMessageHandler) {  this.serverMessageHandler = serverMessageHandler; }

    /**
     * Set the Id of the client.
     * @param clientID the new Id of the client.
     */
    public void setClientID(int clientID) { this.clientID = clientID; }

    /**
     * Send a message to the client.
     * @param message the message sent.
     */
    public void writeToStream(ClientMessage message){
        synchronized (streamLock) {
            Optional<String> serializedMessage = Optional.ofNullable(serialize(message));
            serializedMessage.ifPresentOrElse(out::println,
                    () -> out.println("Error in serialization"));
            out.flush();
        }
    }

    /**
     * Read the messages from the client.
     */
    public void readFromStream(){
        String serializedMessage;
        try {
            serializedMessage = in.nextLine();

            Optional<ServerMessage> message = Optional.
                    ofNullable(deserialize(serializedMessage));

            message.ifPresentOrElse(
                    x -> x.process(serverMessageHandler),
                    () -> writeToStream(new ErrorMessage(ErrorType.INVALID_MESSAGE)));
        }catch (Exception e){
            serverMessageHandler.handleDisconnection();
        }
    }

    /**
     * Deserialize the messages from the client.
     * @param serializedMessage the message to deserialize.
     * @return the deserialized message.
     */
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

    /**
     * Serialize the message to the client.
     * @param message the message to serialize.
     * @return the serialized message.
     */
    public String serialize(ClientMessage message){
        String serializedMessage;
        try {
            serializedMessage = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            serializedMessage = null;
        }
        return serializedMessage;
    }


    /**
     * Read the messages from the client until the client is connected.
     */
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
