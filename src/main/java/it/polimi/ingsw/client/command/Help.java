package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.PrintAssistant;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

public class Help implements Command{
    private final String param;
    private final Client client;

    public Help(String param, Client client) {
        this.param = param;
        this.client = client;
    }

    private void onlyHelp(){

        PrintAssistant.instance.printf("To know more about a single command type help NAMECOMMAND");
        //helpLines.add(PrintAssistant.instance.fitToWidth("MAINMENU", ClientInput.MAX_CHAR_COMMAND)+"Used to select an option in the main menu");

        for(Map.Entry<String, Class<? extends Command>> obj : ClientInput.getCommandsMap().entrySet()){
            try{
                Constructor<? extends Command> constructor = obj.getValue().getConstructor(String.class, Client.class);
                Command commandToRun = constructor.newInstance("", null);
                commandToRun.description();
            }
            catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doCommand() {
        if(param.isEmpty()||param.isBlank()){
            onlyHelp();
            return;
        }
        String[] commandPart = param.split(" ", 2);
        String keyCommand=commandPart[0];
        //String argumentsCommand;
        if(commandPart.length>1) {
            if(!(commandPart[1].isBlank() || commandPart[1].isEmpty())){
                PrintAssistant.instance.printf(ClientInput.ERROR_MSG);
                return;
            }
        }
        if(ClientInput.containsKeyMap(keyCommand)){
            Class<? extends Command> classToInstantiate = ClientInput.getFromMap(keyCommand);
            try{
                Constructor<? extends Command> constructor = classToInstantiate.getConstructor(String.class, Client.class);
                Command commandToRun = constructor.newInstance(keyCommand, null);
                commandToRun.help();
            }
            catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
                e.printStackTrace();
            }
        }
        else{
            PrintAssistant.instance.printf(ClientInput.ERROR_MSG);
        }
    }

    @Override
    public void help() {
        PrintAssistant.instance.printf("No help for the help command! :)");
    }

    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth("HELP", ClientInput.MAX_CHAR_COMMAND)+"to have a list of all the commands available");
    }
}
