package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.serverMessage.StrongboxModify;

import java.util.ArrayList;

public class StrongboxCMD implements Command{
    private final String cmd="STRONGBOX";
    private final String param;
    private final Client client;

    public StrongboxCMD(String param, Client client) {
        this.param = param;
        this.client = client;
    }

    @Override
    public void doCommand() {
        if(CommandsUtility.clientStateNot(client, ClientState.IN_GAME)){
            PrintAssistant.instance.invalidStateCommand(cmd);
            return;
        }
        if(CommandsUtility.emptyString(param)){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        String[] split=param.split(" ");
        if(split.length!=2){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        ResourceData resource;
        try{
            resource= CommandsUtility.fromTypeAndValueToResource(split[0], split[1]);
        }catch (CliException e){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        client.writeToStream(new StrongboxModify(resource));
    }

    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("Write the command followed by the type of resource you want and the amount ");
        rowHelp.add("The command will automatically understand based on the state of the game if you have to subtract or add to depots!");
        rowHelp.add("ex: "+cmd.toLowerCase()+ " SE 2");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"manage the strongbox when u have to add or subtract resources");
    }
}
