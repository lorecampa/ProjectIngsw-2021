package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.message.serverMessage.DepotSwitch;

import java.util.ArrayList;

public class SwitchCMD implements Command{
    private final String cmd="SWITCH";
    private final String param;
    private final Client client;

    public SwitchCMD(String param, Client client) {
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
        String[] split= param.split(" ");
        int currIndexUsed;
        if(split.length<2 || split.length>4){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        int indexFrom=CommandsUtility.stringToInt(split[0]);
        boolean fromIsLeader=false;
        if(split[1].equals("leader")){
            fromIsLeader=true;
            currIndexUsed=2;
        }
        else{
            currIndexUsed=1;
        }
        int indexTo=CommandsUtility.stringToInt(split[currIndexUsed]);
        currIndexUsed++;
        boolean toIsLeader= split.length == currIndexUsed + 1 && split[currIndexUsed].equals("leader");
        if(!fromIsLeader && CommandsUtility.isNotADepotIndex(indexFrom)){
           PrintAssistant.instance.invalidParamCommand(cmd);
           return;
        }else {
            indexFrom--;
        }
        if(!toIsLeader && CommandsUtility.isNotADepotIndex(indexTo)){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        } else {
            indexTo--;
        }

        if(fromIsLeader){
            //indexFrom--;
            if(CommandsUtility.isNotValidIndexDepotLeader(client, indexFrom)){
                PrintAssistant.instance.invalidParamCommand(cmd);
                return;
            }
        }
        if(toIsLeader){
            //indexTo--;
            if(CommandsUtility.isNotValidIndexDepotLeader(client, indexTo)){
                PrintAssistant.instance.invalidParamCommand(cmd);
                return;
            }
        }

        client.writeToStream(new DepotSwitch(indexFrom,  !fromIsLeader, indexTo, !toIsLeader));
    }

    /**
     * See {@link Command#help}
     * */
    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("Write the command followed by the depot index you want to switch.");
        rowHelp.add("If a depot you are considering is a depot Leader add the word leader after the index.");
        rowHelp.add("ex: "+cmd.toLowerCase()+" 1 2");
        rowHelp.add("ex: "+cmd.toLowerCase()+" 1 leader 3");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    /**
     * See {@link Command#description()}
     * */
    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"to switch 2 depots resources");
    }
}
