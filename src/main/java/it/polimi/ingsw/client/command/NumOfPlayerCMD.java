package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionType;

import java.util.ArrayList;

public class NumOfPlayerCMD implements Command{
    private final String cmd="NUMOFPLAYER";
    private final String param;
    private final Client client;

    public NumOfPlayerCMD(String param, Client client) {
        this.param = param;
        this.client = client;
    }

    @Override
    public void doCommand() {

        if(param.isEmpty() || param.isBlank()){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        if(client.getState()!= ClientState.ENTERING_LOBBY){
            PrintAssistant.instance.invalidStateCommand(cmd);
            return;
        }

        //System.out.println("--"+param+"---");
        String[] split= param.split(" ", 2);
        int num=Integer.parseInt(split[0]);
        if(split.length>1 || num<2 || num>4){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        //System.out.println("--"+num+"---");
        client.writeToStream(new ConnectionMessage(ConnectionType.NUM_OF_PLAYER, num));
    }

    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("write the command followed by the number of player you want to create the game for(min 2, max4)");
        rowHelp.add("ex: "+cmd.toLowerCase()+ " 3");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"to insert the number of player for a specific game when the client ask for it");
    }
}
