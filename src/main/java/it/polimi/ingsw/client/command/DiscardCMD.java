package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.message.serverMessage.DiscardResourcesFromMarket;
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

    /**
     * See {@link Command#doCommand}
     * */
    @Override
    public void doCommand() {

        if(client.getState()!= ClientState.IN_GAME && client.getState()!=ClientState.ENTERING_LOBBY){
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
        if (split[0].equals("market")){
            client.writeToStream(new DiscardResourcesFromMarket());
            return;
        }
        try{
            leaderIndex=Integer.parseInt(split[0]);
        }catch(Exception e){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        leaderIndex--;
        if(client.getModelOf(client.getMyName()).notValidIndexForLeader(leaderIndex)){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        client.writeToStream(new LeaderManage(leaderIndex,true));
    }

    /**
     * See {@link Command#help}
     * */
    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("Write the command followed by the position of the leader on your Personal Board(1-4),");
        rowHelp.add("if you discard an active leader you will loose his effects.");
        rowHelp.add("ex: "+cmd.toLowerCase()+" 2");
        rowHelp.add("Write the command followed market if you have to discard the resources you earn from market!");
        rowHelp.add("The number of resources you discard will increase the position of your opponents in the faith track!");
        rowHelp.add("ex: "+cmd.toLowerCase()+" market");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    /**
     * See {@link Command#description()}
     * */
    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"to discard a leader and the extra resources from market");
    }
}
