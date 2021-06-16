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

public class ServerInput implements Runnable{
    private final Server server;
    private final BufferedReader stdIn;
    private boolean exit=false;
    private final Map<String, Class<? extends ServerCommand>> commands = new HashMap<>() {
        {
            put("numofmatch", numMatchCMD.class);
            put("quit", ExitServerCMD.class);
            put("listmatch", ListMatchCMD.class);
            put("logs", LogsOfGameCMD.class);

        }
    };

    public ServerInput(Server server){
        this.server=server;
        this.stdIn = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        String command="";
        while(!exit){
            try{
                command=stdIn.readLine();
                command = command.toLowerCase();
                command = command.trim().replaceAll(" +", " ");//togli i multipli spazi e ne mette solo 1
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            manageInput(command);
        }

    }

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
            PrintAssistant.instance.printf("Invalid Input!");
        }
    }
}
