package it.polimi.ingsw.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.client.data.DeckDevData;
import it.polimi.ingsw.client.data.MarketData;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.clientMessage.MainMenuMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

public class Client{
    private static final String ipHost ="127.0.0.1";
    private static final int portNumber = 2020;
    private Socket clientSocket;

    private PrintWriter out;
    private BufferedReader in ;
    private ClientMessageHandler clientMessageHandler;
    private final ObjectMapper mapper = new ObjectMapper();

    private final Object streamLock = new Object();

    private ClientState state;
    private ClientGameState gameState;

    private String myName;

    private ArrayList<ModelClient> models= new ArrayList<>();
    private MarketData marketData;
    private DeckDevData deckDevData;

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
        ArrayList<String> users=new ArrayList<>();
        client.setMyName("Teo");
        users.add("Teo");
        users.add("Davide");
        users.add("Lollo");
        client.setModels(users);
        client.messageToMainMenu();
        while(client.state!=ClientState.QUIT){
            client.readFromStream();
        }
        /*
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
                    while(!client.exit){
                        client.readFromStream();
                    }
                    break;
                default:
                    //simula un errore in arrivo dal server, avrà un metodo come il messageToMainMenu ma per un errore

                    PrintAssistant.instance.printf("INVALID OPTION CHOOSEN! PLEASE SELECT A VALID ONE!", PrintAssistant.ANSI_BLACK, PrintAssistant.ANSI_RED_BACKGROUND);
            }
            client.messageToMainMenu();
        }
         */
    }

    public ModelClient getModelOf(String username) {
        return models.stream().filter(x-> x.getUsername().equalsIgnoreCase(username)).findFirst().get();
    }

    public boolean existAModelOf(String username){
        return models.stream().map(ModelClient::getUsername).anyMatch(x->x.equalsIgnoreCase(username));
    }

    public void setModels(ArrayList<String> usernames) {
        for(String s : usernames){
            this.models.add(new ModelClient(s));
        }
    }

    private void startClient() throws IOException {
        try {
            clientSocket = new Socket(ipHost, portNumber);
            clientSocket.setSoTimeout(20000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        state=ClientState.MAIN_MENU;
        gameState=ClientGameState.NOT_IN_GAME;
        clientMessageHandler = new ClientMessageHandler(this);
        new Thread(new ClientInput(this)).start();

        out = new PrintWriter(clientSocket.getOutputStream(), true);                //i messaggi che mandi al server
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));      //i messaggi che vengono dal server


        //stdIn = new Scanner(new InputStreamReader(System.in));
    }

    public void writeToStream(ServerMessage message){
        synchronized (streamLock) {
            Optional<String> serializedMessage = Optional.ofNullable(serialize(message));
            serializedMessage.ifPresentOrElse(out::println,
                    () -> out.println("Error in serialization"));
            out.flush();
        }
    }

    public void readFromStream(){
        String serializedMessage = null;
        try{
            serializedMessage = in.readLine();
        } catch (Exception e) {
            System.out.println("server disconnesso");
            e.printStackTrace();
        }
        /*
        //da riguardare perchè secondo me non fa quello che dovrebbe
        if (serializedMessage.equalsIgnoreCase("QUIT")){
            return;
        }
        */
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

    public ClientState getState() {
        return state;
    }

    public ClientGameState getGameState() {
        return gameState;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    public void setGameState(ClientGameState gameState) {
        this.gameState = gameState;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public MarketData getMarketData() {
        return marketData;
    }

    public void setMarketData(MarketData marketData) {
        this.marketData = marketData;
    }

    public DeckDevData getDeckDevData() {
        return deckDevData;
    }

    public void setDeckDevData(DeckDevData deckDevData) {
        this.deckDevData = deckDevData;
    }
}
