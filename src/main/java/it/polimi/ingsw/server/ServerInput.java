package it.polimi.ingsw.server;

import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.server.command.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manage the command line input of the server.
 */
public class ServerInput implements Runnable{
    private final Server server;
    private final BufferedReader stdIn;
    private final Map<String, Class<? extends ServerCommand>> commands = new HashMap<>() {
        {
            put("resources", ResourcesCMD.class);
            put("numofmatch", numMatchCMD.class);
            put("quit", ExitServerCMD.class);
            put("listmatch", ListMatchCMD.class);
            put("logs", LogsOfGameCMD.class);
        }
    };

    /**
     * Construct a Server Input of the server.
     * @param server the reference to the server.
     */
    public ServerInput(Server server){
        this.server=server;
        this.stdIn = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Read the input from the command line.
     */
    @Override
    public void run() {
        String command="";
        while(!server.isExit()){
            try{
                command=stdIn.readLine();
                command = command.toLowerCase();
                command = command.trim().replaceAll(" +", " ");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            manageInput(command);
        }
        System.out.println("Hope you enjoyed MastersOfRenaissance! See you soon!");
        System.exit(0);
    }

    /**
     * Handle the input and execute the command.
     * @param command the input from the command line.
     */
    private void manageInput(String command){
        String[] commandPart = command.split(" ", 2);
        String keyCommand=commandPart[0];
        String argumentsCommand;
        if(commandPart.length>1)
            argumentsCommand=commandPart[1];
        else
            argumentsCommand="";
        if(commands.containsKey(keyCommand)){
            Class<? extends ServerCommand> classToInstantiate = commands.get(keyCommand);
            try{
                Constructor<? extends ServerCommand> constructor = classToInstantiate.getConstructor(String.class, Server.class);
                ServerCommand commandToRun = constructor.newInstance(argumentsCommand, server);
                commandToRun.doCommand();
            }
            catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
                e.printStackTrace();
            }
        }
        else{
            PrintAssistant.instance.printf("The command you write doesn't exist!");
        }
    }
}
