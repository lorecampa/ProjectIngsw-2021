package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.serverMessage.DepotModify;

import java.util.ArrayList;

public class DepotCMD implements Command{
    private final String cmd="DEPOT";
    private final String param;
    private final Client client;

    public DepotCMD(String param, Client client) {
        this.param = param;
        this.client = client;
    }

    /**
     * See {@link Command#doCommand}
     * */
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
        if(split.length!=3 && split.length!=4){
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

        int indexDepot=CommandsUtility.stringToInt(split[2]);
        if(CommandsUtility.isNotADepotIndex(indexDepot)){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        indexDepot--;
        boolean normalDepot=true;
        if(split.length==4){
            if(split[3].equals("leader")){
                normalDepot=false;
            }
            else{
                PrintAssistant.instance.invalidParamCommand(cmd);
                return;
            }
        }
        client.writeToStream(new DepotModify(indexDepot, resource, normalDepot));
    }

    /**
     * See {@link Command#help}
     * */
    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("Write the command followed by the type of resource you want, the amount and the last number have to be the depot index shown on your Personal Board.");
        rowHelp.add("The command will automatically understand based on the state of the game if you have to subtract or add to depots!");
        rowHelp.add("If u want to use the depot leader just add leader at the end of the command");
        rowHelp.add("ex: "+cmd.toLowerCase()+ " CO 2 1");
        rowHelp.add("ex: "+cmd.toLowerCase()+ " CO 2 1 leader");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    /**
     * See {@link Command#description()}
     * */
    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"manage the depot when u have to add or subtract resources");
    }
}
