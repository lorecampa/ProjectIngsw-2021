package it.polimi.ingsw.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.message.ConnectionMessage;
import it.polimi.ingsw.message.ConnectionType;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.clientMessage.MainMenuMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;

public class Client{
    private static final String ipHost ="127.0.0.1";
    private static final int portNumber = 2021;
    private Socket clientSocket;

    private PrintWriter out;
    private BufferedReader in ;
    private Scanner stdIn;
    private ClientMessageHandler clientMessageHandler;
    private ObjectMapper mapper = new ObjectMapper();
    private boolean exit=false;
    private Object lockInput;
    private int chooseAction;



    public static void main(String[] args){
        /*
        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        String ipHost = args[0];
        int portNumber = Integer.parseInt(args[1]);
        */
        Client client = new Client();
        try{
            client.startClient();
            System.out.println("Client Started!");
        }
        catch(Exception e){
            e.printStackTrace();
            //messaggio che si scusa e dice di riaprire
        }

        client.messageToMainMenu();
        while(client.chooseAction!=4){
            switch(client.chooseAction){
                case 1:
                    client.writeToStream(new ConnectionMessage(ConnectionType.CONNECT, ""));
                    while(!client.exit){
                        client.readFromStream();
                    }
                    break;
                case 2:
                    break;
                case 3:
                    client.writeToStream(new ConnectionMessage(ConnectionType.RECONNECTION, ""));
                    break;
                default:
                    //simula un errore in arrivo dal server, avrà un metodo come il messageToMainMenu ma per un errore
                    PrintAssistant.instance.printf("INVALID OPTION CHOOSEN! PLEASE SELECT A VALID ONE!", PrintAssistant.ANSI_BLACK, PrintAssistant.ANSI_RED_BACKGROUND);
            }
            client.messageToMainMenu();
        }
    }

    public void setChooseAction(int chooseAction) {
        this.chooseAction = chooseAction;
    }

    private void startClient() throws IOException {
        try {
            clientSocket = new Socket(ipHost, portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientMessageHandler = new ClientMessageHandler(this);
        exit = false;
        lockInput = new Object();
        out = new PrintWriter(clientSocket.getOutputStream(), true);                //i messaggi che mandi al server
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));      //i messaggi che vengono dal server
        stdIn = new Scanner(new InputStreamReader(System.in));
    }

    public int waitToIntegerInputCLI(){
        synchronized (lockInput) {
            return stdIn.nextInt();
        }
    }

    public String waitToStringInputCLI(){
        synchronized (lockInput) {
            return stdIn.next();
        }
    }

    public void writeToStream(ServerMessage message){
        Optional<String> serializedMessage = Optional.ofNullable(serialize(message));
        serializedMessage.ifPresentOrElse(out::println,
                ()-> out.println("Error in serialization"));
        out.flush();
    }

    public void readFromStream(){
        String serializedMessage = null;
        try{
            serializedMessage = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //da riguardare perchè secondo me non fa quello che dovrebbe
        if (serializedMessage.equalsIgnoreCase("QUIT")){
            exit = true;
            return;
        }
        ClientMessage message = deserialize(serializedMessage);

        message.process(clientMessageHandler);
    }

    public ClientMessage deserialize(String serializedMessage){
        ClientMessage message;
        try {
            message = mapper.readValue(serializedMessage, ClientMessage.class);
        } catch (JsonProcessingException e) {
            return null;
        }
        return message;
    }

    public String serialize(ServerMessage message){
        String serializedMessage;
        try {
            serializedMessage = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            serializedMessage = null;
        }
        return serializedMessage;
    }

    private void messageToMainMenu(){
        ClientMessage mainMenu= new MainMenuMessage();
        mainMenu.process(clientMessageHandler);
    }

}
