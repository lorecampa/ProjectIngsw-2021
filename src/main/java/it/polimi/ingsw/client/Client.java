package it.polimi.ingsw.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.GUI.GUIMessageHandler;
import it.polimi.ingsw.client.data.*;
import it.polimi.ingsw.message.clientMessage.ClientMessage;
import it.polimi.ingsw.message.clientMessage.MainMenuMessage;
import it.polimi.ingsw.message.serverMessage.ServerMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Client{
    private String ipHost;
    private int portNumber;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in ;
    private ClientMessageHandler clientMessageHandler;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Object streamLock = new Object();
    private ClientState state;
    private static final Client clientInstance = new Client();
    private String myName;
    private final ArrayList<ModelClient> models = new ArrayList<>();
    private MarketData marketData;
    private DeckDevData deckDevData;
    public final String DATA_LAST_GAME = "MasterOfRenaissance_dataLastGame.txt";

    private final HashMap<String,String> argsMap = new HashMap<>();

    private void setUpArgs(){
        argsMap.put("-interface", "cli");
        argsMap.put("-address", "127.0.0.1");
        argsMap.put("-port", "2020");
    }

    /**
     *Set up client with arguments
     * */
    private void setUpClient(String[] args) throws IOException {
        setUpArgs();
        for (int i = 0; i < args.length; i++) {
            if (argsMap.containsKey(args[i])){
                try {
                    argsMap.replace(args[i], args[i + 1]);
                    i++;
                }catch (Exception e){
                    System.out.println("Invalid param!");
                    System.exit(0);
                }
            }else{
                System.out.println("Invalid param!");
                System.exit(0);
            }
        }

        ipHost = argsMap.get("-address");
        try {
            portNumber = Integer.parseInt(argsMap.get("-port"));
        }catch (Exception e){
            System.out.println("Invalid param to start client!");
            System.exit(0);
        }

        try {
            clientSocket = new Socket(ipHost, portNumber);
            //clientSocket.setSoTimeout(20000);
        } catch (IOException e) {
            System.out.println("Error during socket set up: " + e.getMessage());
            System.exit(0);
        }
        state = ClientState.MAIN_MENU;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public static Client getInstance(){
        return clientInstance;
    }



    public static void main(String[] args){
        try{
            clientInstance.setUpClient(args);
        }catch (Exception e){
            PrintAssistant.instance.errorPrint("There's no server ready to answer you! Try again later! Bye :)");
            System.exit(0);
        }

        String clientInterface = clientInstance.argsMap.get("-interface");
        if (clientInterface.equals("cli")){
            clientInstance.startCLI();
        }
        else if (clientInterface.equals("gui")) {
            try{
                new Thread(() -> ClientGUI.main(args)).start();
            }catch(Exception e){
                System.out.println("BIG LOL");
            }

            clientInstance.startGUI();
        }
        else{
            System.out.println("Invalid param to start client!");
            System.exit(0);
        }

    }

    /**
     * Start client with CLI
     * */
    public void startCLI(){
        System.out.println("Client Started!");
        clientMessageHandler = new CLIMessageHandler();
        new Thread(new ClientInput()).start();
        clientInstance.messageToMainMenu();
        startListening();
    }

    /**
     * Start client with GUI
     * */
    public void startGUI(){
        clientMessageHandler = new GUIMessageHandler();
        startListening();
    }

    /**
     * Start listening messages from server
     * */
    private void startListening() {
        while(state!=ClientState.QUIT){
            readFromStream();
        }
        System.exit(0);
    }

    /**
     * Write to server the message
     * @param message to send to server
     * */
    public void writeToStream(ServerMessage message){
        synchronized (streamLock) {
            Optional<String> serializedMessage = Optional.ofNullable(serialize(message));
            serializedMessage.ifPresentOrElse(out::println,
                    () -> PrintAssistant.instance.errorPrint("Error in serialization"));
            out.flush();
        }
    }
    /**
     * Read from stream the messages from server
     * */
    public void readFromStream(){
        String serializedMessage;
        try{
            serializedMessage = in.readLine();
            ClientMessage message = deserialize(serializedMessage);
            message.process(clientMessageHandler);
        } catch (Exception e) {
            PrintAssistant.instance.errorPrint("Server disconnected, " +
                    "even Google sometimes went down! Wait until the host re-set up the server please!");
            System.exit(0);
        }
    }

    /**
     * Return ClientMessage after deserialization
     * @param serializedMessage to deserialize
     * @return message deserialized
     * */
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

    /**
     * Return serialized message before send it to server
     * @param message to serialize
     * @return message serialized
     * */
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
    /**
     * Simulated message from server to show mainmenu
     * */
    public void messageToMainMenu(){
        ClientMessage mainMenu= new MainMenuMessage();
        mainMenu.process(clientMessageHandler);
    }

    //getter and setter of attributes of Client
    public ModelClient getModelOf(String username) {
        return models.stream().filter(x-> x.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
    }

    public ArrayList<ModelClient> getModels() {
        return models;
    }

    public ModelClient getMyModel(){
        return models.stream().filter(x-> x.getUsername().equalsIgnoreCase(myName)).findFirst().orElse(null);
    }

    /**
     * Return true if exist a model with username as nickname
     * @param username we are looking for
     * */
    public boolean existAModelOf(String username){
        return models.stream().map(ModelClient::getUsername).anyMatch(x->x.equalsIgnoreCase(username));
    }

    public void setModels(ArrayList<String> usernames) {
        for(String s : usernames){
            this.models.add(new ModelClient(s));
        }
        if(usernames.size()==1){
            this.models.add(new ModelClient("LorenzoIlMagnifico"));
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

    /**
     * Cleat all the models stored in client
     * */
    public void clearModels(){
        models.clear();
    }
}