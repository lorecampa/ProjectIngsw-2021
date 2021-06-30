package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.message.serverMessage.WhiteMarbleConversionResponse;

import java.util.ArrayList;

public class MarbleCMD implements Command {
    private final String cmd="MARBLE";
    private final String param;
    private final Client client;


    public MarbleCMD(String param, Client client) {
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
        if(split.length!=2){
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
        if(client.getModelOf(client.getMyName()).notValidIndexForLeader(leaderIndex)){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        int numOfMarble;
        try{
            numOfMarble=Integer.parseInt(split[1]);
        }catch(Exception e){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        client.writeToStream(new WhiteMarbleConversionResponse(leaderIndex, numOfMarble));
    }

    /**
     * See {@link Command#help}
     * */
    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("Write the command followed by the position of the marble leader on your Personal Board(1-4),");
        rowHelp.add("and the number of white marble you want to convert into!");
        rowHelp.add("ex: "+cmd.toLowerCase()+" 2 1");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    /**
     * See {@link Command#description()}
     * */
    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"to select the leader with the marble you want to convert");
    }
}
