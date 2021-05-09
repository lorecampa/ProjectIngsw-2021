package it.polimi.ingsw.client;

import it.polimi.ingsw.client.command.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ClientInput implements Runnable{
    public static final int MAX_CHAR_COMMAND=15;
    public static final String ERROR_MSG ="The command you write doesn't exist! Type help to get the list of commands!";
    private final Client client;
    private final BufferedReader stdIn;

    private final static Map<String, Class<? extends Command>> commands = new HashMap<>() {
        {
            put("action", ActionCMD.class);
            put("show", ShowCMD.class);
            put("username", UsernameCMD.class);
            put("numofplayer", NumOfPlayerCMD.class);
            put("mainmenu", MainMenuCMD.class);
            put("help", Help.class);
        }
    };

    public ClientInput(Client client) {
        this.client = client;
        this.stdIn = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        String command="";
        while(!command.contains("quit")){
            try{
                command=stdIn.readLine();
                command = command.toLowerCase();
                command = command.trim().replaceAll(" +", " "); //togli i multipli spazi e ne mette solo 1
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            manageInput(command);
        }
        client.setState(ClientState.QUIT);
    }

    public void manageInput(String command){
        String[] commandPart = command.split(" ", 2);
        //ArrayList<String> mieipar = Arrays.stream(commandPart).collect(Collectors.toCollection(ArrayList::new));
        String keyCommand=commandPart[0];
        String argumentsCommand;
        if(commandPart.length>1)
            argumentsCommand=commandPart[1];
        else
            argumentsCommand="";
        if(commands.containsKey(keyCommand)){
            Class<? extends Command> classToInstantiate = commands.get(keyCommand);
            try{
                Constructor<? extends Command> constructor = classToInstantiate.getConstructor(String.class, Client.class);
                Command commandToRun = constructor.newInstance(argumentsCommand, client);
                commandToRun.doCommand();
            }
            catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
                e.printStackTrace();
            }
        }
        else{
            PrintAssistant.instance.printf(ERROR_MSG);
        }

    }

    public static boolean containsKeyMap(String s){
        return commands.containsKey(s);
    }
    public static Class<? extends Command> getFromMap(String s){
        return commands.get(s);
    }
    public static Map<String, Class<? extends Command>> getCommandsMap(){
        return commands;
    }

}
