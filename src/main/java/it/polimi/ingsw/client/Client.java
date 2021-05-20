package it.polimi.ingsw.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.client.data.*;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.clientMessage.MainMenuMessage;
import it.polimi.ingsw.message.serverMessage.LeaderManage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Client{
    private static final String ipHost ="127.0.0.1";
    private static final int portNumber = 1010;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in ;
    private ClientMessageHandler clientMessageHandler;
    private final ObjectMapper mapper = new ObjectMapper();

    private final Object streamLock = new Object();

    private ClientState state;

    private String nameFile;
    private String myName;
    private final ArrayList<ModelClient> models = new ArrayList<>();
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
            PrintAssistant.instance.errorPrint("There's no server ready to answer you! Try again later! Bye :)");
            System.exit(0);
        }
        if (args.length == 1) {
            switch (args[0]) {
                case "primo":
                    client.writeToStream(new ConnectionMessage(ConnectionType.CONNECT, ""));
                    client.setState(ClientState.ENTERING_LOBBY);
                    client.writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER, 2));
                    client.writeToStream(new ConnectionMessage(ConnectionType.USERNAME, "Teo"));
                    client.setMyName("Teo");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    client.writeToStream(new LeaderManage(0, true));
                    client.writeToStream(new LeaderManage(0, true));
                    break;
                case "secondo":
                    client.writeToStream(new ConnectionMessage(ConnectionType.CONNECT, ""));
                    client.setState(ClientState.ENTERING_LOBBY);
                    client.writeToStream(new ConnectionMessage(ConnectionType.USERNAME, "Lollo"));
                    client.setMyName("Lollo");
                    client.writeToStream(new LeaderManage(0, true));
                    client.writeToStream(new LeaderManage(0, true));
                    break;
                case "singlePlayer":
                    client.writeToStream(new ConnectionMessage(ConnectionType.CONNECT, ""));
                    client.setState(ClientState.ENTERING_LOBBY);
                    client.writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER, 1));
                    client.writeToStream(new ConnectionMessage(ConnectionType.USERNAME, "Teo"));
                    client.setMyName("Teo");
                    client.writeToStream(new LeaderManage(0, true));
                    client.writeToStream(new LeaderManage(0, true));
                    break;
            }
        }
        //to simulate 3 player
        /*
        ArrayList<String> users=new ArrayList<>();
        client.setMyName("Teo");
        users.add("Teo");
        users.add("Davide");
        users.add("Lollo");
        client.setModels(users);
        */
        client.messageToMainMenu();
        while(client.state!=ClientState.QUIT){
            client.readFromStream();
        }
        System.exit(0);
    }

    private void startClient() throws IOException {
        try {
            clientSocket = new Socket(ipHost, portNumber);
            //clientSocket.setSoTimeout(20000);
        } catch (IOException e) {
            //e.printStackTrace(); //non voglio che venga stamapato la stacktrace
        }
        state=ClientState.MAIN_MENU;
        clientMessageHandler = new ClientMessageHandler(this);
        new Thread(new ClientInput(this)).start();
        nameFile="MasterOfRenaissance_dataLastGame.txt";
        out = new PrintWriter(clientSocket.getOutputStream(), true);                //i messaggi che mandi al server
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));      //i messaggi che vengono dal server

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
            PrintAssistant.instance.errorPrint("Server disconnected, even Google sometimes went down! Wait until the host re-set up the server please!");
            System.exit(0);
        }

        ClientMessage message = deserialize(serializedMessage);

        message.process(clientMessageHandler);
    }

    public ClientMessage deserialize(String serializedMessage){
        ClientMessage message;
        try {
            message = mapper.readValue(serializedMessage, ClientMessage.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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

    //"message" simulated from server to client
    public void messageToMainMenu(){
        ClientMessage mainMenu= new MainMenuMessage();
        mainMenu.process(clientMessageHandler);
    }

    //getter and setter of attributes of Client
    public ModelClient getModelOf(String username) {
        return models.stream().filter(x-> x.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
    }

    public boolean existAModelOf(String username){
        return models.stream().map(ModelClient::getUsername).anyMatch(x->x.equalsIgnoreCase(username));
    }

    public void setModels(ArrayList<String> usernames) {
        for(String s : usernames){
            this.models.add(new ModelClient(s));
        }
        if(usernames.size()==1){
            this.models.add(new ModelClient("lorenzoilmagnifico"));
        }
    }

    public void setUpModel(ModelData model){
        ModelClient playerModel = getModelOf(model.getUsername());
        playerModel.setFaithTrack(model.getFaithTrack());
        playerModel.setCurrentPosOnFaithTrack(model.getCurrentPosOnFaithTrack());

        playerModel.setStandardDepot(model.getStandardDepot());
        playerModel.setLeaderDepot(model.getLeaderDepot());
        playerModel.setMaxStoreLeaderDepot(model.getMaxStoreLeaderDepot());

        playerModel.setStrongbox(model.getStrongbox());

        playerModel.setCardSlots(model.getCardSlots());

        playerModel.setLeaders(model.getLeaders());
    }

    public ClientState getState() {
        return state;
    }


    public void setState(ClientState state) {
        this.state = state;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getNameFile(){
        return nameFile;
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

    public void setFaithTrackData(ArrayList<ArrayList<FaithTrackData>> faithTrackData){
        int i = 0;
        for (ModelClient modelClient: models){
            modelClient.setFaithTrack(faithTrackData.get(i));
            i++;
        }
    }

    public void setBaseProduction(ArrayList<EffectData> baseProduction){
        for(ModelClient m : models){
            m.setBaseProduction(baseProduction);
        }
    }

    public void setInkwell(){
        for(int i=0; i<models.size(); i++){
            models.get(i).setInkwell(i == 0);
        }
    }

    public void clearModels(){
        models.clear();
    }
}
