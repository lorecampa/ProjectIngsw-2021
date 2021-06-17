package it.polimi.ingsw.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private int port;
    private boolean load;
    private final ExecutorService executorService;
    private ServerSocket serverSocket;
    private int nextClientID;
    private int nextMatchID;
    private final ArrayList<ClientConnectionHandler> lobby;
    private final ArrayList<Match> matches;
    private final ArrayList<Match> matchesToFill;
    private final Object lockOpenMatch = new Object();
    private Match openMatch;

    private final HashMap<String,String> argsMap = new HashMap<>();

    public static final String SERVER_DATA_PATH = "ServerData";
    public static final String SERVER_INFO_PATH = SERVER_DATA_PATH + "/serverInfo.txt";
    public static final String MATCH_SAVING_PATH = SERVER_DATA_PATH + "/MatchSaving";


    private void setUpArgs(){
        argsMap.put("-port", "2020");
        argsMap.put("-load", "false");
    }

    public  Server(String[] args){
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

        try{
            port=Integer.parseInt(argsMap.get("-port"));
            if(port<=1024){
                System.out.println("Invalid port number!");
                System.exit(0);
            }
        }
        catch (Exception e){
            System.out.println("Invalid port number!");
            System.exit(0);
        }

        if (argsMap.get("-load").equals("true"))
            load = true;
        else  if (argsMap.get("-load").equals("false"))
            load = false;
        else{
            System.out.println("Invalid param!");
            System.exit(0);
        }

        executorService = Executors.newCachedThreadPool();
        lobby = new ArrayList<>();
        matches = new ArrayList<>();
        matchesToFill = new ArrayList<>();

    }

    public void startServer(){
        try {
            serverSocket = new ServerSocket(port);

            if (load) {
                loadServerData();
                loadMatches();
            }
            else
                deleteFolder(new File(SERVER_DATA_PATH));

            System.out.println("Server ready");
        } catch (IOException e) {
            System.out.println("Error during server setUp: " + e.getMessage());
            System.exit(1);
        }

        new Thread(new ServerInput(this)).start();
        acceptConnection();
    }


    private void deleteFolder(File file) throws IOException {
        if (file.isDirectory()){
            File[] files = file.listFiles();
            if (files != null){
                for (File subFile: files){
                    deleteFolder(subFile);
                }
            }
            //delete empty sub-folder
            boolean result = file.delete();
            if (!result){
                throw new IOException("Can't delete folder");
            }
        }else if (file.isFile()){
            boolean result = file.delete();
            if (!result){
                throw new IOException("Can't delete file");
            }
        }

    }

    private void loadServerData() throws IOException {
        File serverDataDir = new File(SERVER_DATA_PATH);
        if (!serverDataDir.exists()){
            boolean result = serverDataDir.mkdirs();
            if (!result){
                throw new IOException("Can't create server data directory");
            }
            FileWriter serverInfoFile = new FileWriter(SERVER_INFO_PATH);
            serverInfoFile.write("0\n0");
            serverInfoFile.close();
        }

        //if directory exist but file was accidentally deleted
        File serverInfoFile = new File(SERVER_INFO_PATH);
        if (!serverInfoFile.exists()){
            FileWriter file = new FileWriter(serverInfoFile);
            file.write("0\n0");
            file.close();
        }
        Path path = Paths.get(SERVER_INFO_PATH);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        nextMatchID = Integer.parseInt(lines.get(0));
        nextClientID = Integer.parseInt(lines.get(1));
    }

    private void loadMatches() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        File serverMatchesDir = new File(MATCH_SAVING_PATH);
        if (!serverMatchesDir.exists()){
            boolean result = serverMatchesDir.mkdirs();
            if (!result){
                throw new IOException("Can't create matches saving directory");
            }
        }

        File[] matchFiles = new File(MATCH_SAVING_PATH).listFiles((dir, name) -> !name.equals(".DS_Store"));

        if (matchFiles == null || matchFiles.length == 0){
            System.out.println("No files to load");
            return;
        }
        System.out.println(matchFiles.length + " matches to load found");

        for (File file: matchFiles){
            if (file.isFile()){
                MatchData matchData = mapper.readValue(file, MatchData.class);
                matches.add(matchData.createMatch(this));
            }
        }

    }

    public void closeOpenMatch(){
        synchronized (lobby) {
            synchronized (lockOpenMatch) {
                synchronized (matchesToFill) {
                    lobby.subList(0, openMatch.getNumOfPlayers()).clear();
                    openMatch = null;
                    if (matchesToFill.size() == 0) {
                        if (lobby.size() > 0) {
                            lobby.get(0).setState(HandlerState.NUM_OF_PLAYER);
                            lobby.get(0).writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER, "Insert the number of Players: "));
                        }
                    }else{
                        openMatch = matchesToFill.get(0);
                        for (int i = 0; i < openMatch.getNumOfPlayers() && i < lobby.size(); i++) {
                            openMatch.addPlayer(new VirtualClient(
                                    "Quest_".concat(String.valueOf(openMatch.currentNumOfPlayer())),lobby.get(i),openMatch));
                        }
                    }
                }
            }
        }
    }

    public void createMatch(int numOfPlayer, ClientConnectionHandler player) throws InvalidParameterException{

        if (numOfPlayer<1 || numOfPlayer >4)
            throw new InvalidParameterException("Number of players has to be between 1 and 4");

        Match newMatch = new Match(numOfPlayer, this, getNextMatchID());
        synchronized (lobby){
            if (!lobby.contains(player) || !lobby.get(0).equals(player))
                return;
            synchronized (lockOpenMatch) {
                openMatch = newMatch;
                synchronized (matches) {
                    matches.add(newMatch);
                }
                newMatch.addPlayer(new VirtualClient("Quest_".concat(String.valueOf(newMatch.currentNumOfPlayer())), player, openMatch));
            }
            for (int i = 1; i < newMatch.getNumOfPlayers() && i < lobby.size(); i++) {
                newMatch.addPlayer(new VirtualClient("Quest_".concat(String.valueOf(newMatch.currentNumOfPlayer())),lobby.get(i),openMatch));
            }
        }
    }

    public void putInLobby(ClientConnectionHandler client){
        synchronized (lobby){
            if (!lobby.contains(client)){
                lobby.add(client);
                if (lobby.get(0).equals(client)){
                    client.setState(HandlerState.NUM_OF_PLAYER);
                    client.writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER,"Insert the number of Players: "));
                }else{
                    synchronized (lockOpenMatch){
                        if (openMatch != null && openMatch.isOpen()){
                            openMatch.addPlayer(new VirtualClient("Quest_".concat(String.valueOf(openMatch.currentNumOfPlayer())),client,openMatch));
                        }else{
                            client.setState(HandlerState.WAITING_LOBBY);
                            client.writeToStream(new ConnectionMessage(ConnectionType.WAIT_PLAYERS,"Waiting Players!"));
                        }
                    }
                }
            }
        }
    }

    public void singlePlayer(ClientConnectionHandler client){
        Match newMatch = new Match(1, this, getNextMatchID());
        synchronized (matches) {
            matches.add(newMatch);
        }
        newMatch.addForSinglePlayer(new VirtualClient("Quest_".concat(String.valueOf(newMatch.currentNumOfPlayer())), client, newMatch));
    }

    public void clientDisconnect(ClientConnectionHandler client){
        synchronized (lobby){
            client.setExit(true);
            lobby.remove(client);
            if ((client.getState() == HandlerState.NUM_OF_PLAYER)&&(lobby.size() > 0))
            {
                lobby.get(0).setState(HandlerState.NUM_OF_PLAYER);
                lobby.get(0).writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER, "Insert the number of Players: "));
            }
        }
    }

    public void clientReconnection(int matchID, int clientID, ClientConnectionHandler client){
        synchronized (matches){
            boolean reconnected = false;
            for (Match match : matches) {
                if (match.getMatchID() == matchID)
                    reconnected = match.playerReconnection(clientID, client);
            }
            if (!reconnected)
                client.writeToStream(new ErrorMessage(ErrorType.FAIL_RECONNECTION));
            else{
                synchronized (lobby){
                    for (ClientConnectionHandler clientConnectionHandler : lobby ){
                        if (clientConnectionHandler.getClientID() == clientID){
                            lobby.set(lobby.indexOf(clientConnectionHandler), client);
                        }
                    }
                }
            }
        }
    }

    public void putInToFill(Match matchToFill){
        synchronized (matchesToFill) {
            if (matchToFill != openMatch) {
                matchesToFill.add(matchToFill);
            }
        }
    }

    public synchronized int getNextClientID() throws IOException {
        Path path = Paths.get(SERVER_DATA_PATH + "/serverInfo.txt");
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        int lastClientID = Integer.parseInt(lines.get(1));
        nextClientID = lastClientID + 1;
        lines.set(1, Integer.toString(nextClientID));
        Files.write(path, lines, StandardCharsets.UTF_8);
        return  nextClientID;
    }

    public synchronized int getNextMatchID() {
        Path path = Paths.get(SERVER_DATA_PATH + "/serverInfo.txt");
        List<String> lines;
        //FIXME it should throw an exception because it is impossible to change the nextID value
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            int lastMatchID = Integer.parseInt(lines.get(0));
            nextMatchID = lastMatchID + 1;
            lines.set(0, Integer.toString(nextMatchID));
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  nextMatchID;
    }

    public void acceptConnection(){
        while (true){
            try {
                Socket socket = serverSocket.accept();
                //TODO reactivate
                //socket.setSoTimeout(20000);
                System.out.println("Server Socket has accepted a connection");
                ClientConnectionHandler client = new ClientConnectionHandler(socket, this, getNextClientID());
                executorService.submit(client);
            } catch(IOException e) {
                break;
            }
        }
    }

    public static void main(String[] args){
        Server server = new Server(args);
        server.startServer();
    }

    public void matchEnd(Match match){
        matches.remove(match);
    }

    public ArrayList<Match> getMatches(){
        return matches;
    }

    public Match getMatchWithId(int id){
        for(Match match : matches){
            if(id==match.getMatchID()){
                return match;
            }
        }
        return null;
    }

}
