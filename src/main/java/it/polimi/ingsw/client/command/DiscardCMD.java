package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.message.serverMessage.LeaderManage;

import java.util.ArrayList;

public class DiscardCMD implements Command{
    private final String cmd="DISCARD";
    private final String param;
    private final Client client;

    public DiscardCMD(String param, Client client) {
        this.param = param;
        this.client = client;
    }

    @Override
    public void doCommand() {

        if(client.getState()!= ClientState.IN_GAME){
            PrintAssistant.instance.invalidStateCommand(cmd);
            return;
        }

        if(param.isEmpty() || param.isBlank()){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        String[] split = param.split(" ");
        if(split.length!=1){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        int leaderIndex;
        try{
            leaderIndex=Integer.parseInt(split[0]);
        }catch(Exception e){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        leaderIndex--;
        if(!client.getModelOf(client.getMyName()).validIndexForLeader(leaderIndex)){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        //TODO: send message to tell the serve witch leader i want to discard
        client.writeToStream(new LeaderManage(leaderIndex,true));
    }

    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("Write the command followed by the position of the leader on your Personal Board(1-4),");
        rowHelp.add("if you discard an active leader you will loose his effects.");
        rowHelp.add("ex: "+cmd.toLowerCase()+" 2");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"to discard a leader during your turn or at the beginning of the game");
    }
}
