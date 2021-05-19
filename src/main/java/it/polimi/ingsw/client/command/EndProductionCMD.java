package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.message.serverMessage.EndProductionSelection;

import java.util.ArrayList;

public class EndProductionCMD implements Command{
    private final String cmd="ENDPRODUCTION";
    private final String param;
    private final Client client;

    public EndProductionCMD(String param, Client client) {
        this.param = param;
        this.client = client;
    }

    @Override
    public void doCommand() {
        if(CommandsUtility.clientStateNot(client, ClientState.IN_GAME)){
            PrintAssistant.instance.invalidStateCommand(cmd);
            return;
        }
        if(!CommandsUtility.emptyString(param)){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        client.writeToStream(new EndProductionSelection());
    }

    @Override
    public void help() {
        //TODO modify
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("Write the command to end your turn and let the next player play!");
        rowHelp.add("ex: "+cmd.toLowerCase()+"");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"to end your turn!");
    }
}
