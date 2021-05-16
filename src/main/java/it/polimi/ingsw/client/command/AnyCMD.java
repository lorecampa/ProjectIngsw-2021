package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.message.serverMessage.AnyResponse;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;

public class AnyCMD implements Command{
    private final String cmd="ANY";
    private final String param;
    private final Client client;

    public AnyCMD(String param, Client client) {
        this.param = param;
        this.client = client;
    }

    @Override
    public void doCommand() {
        if(CommandsUtility.clientStateNot(client, ClientState.IN_GAME) && CommandsUtility.clientStateNot(client, ClientState.ENTERING_LOBBY)){
            PrintAssistant.instance.invalidStateCommand(cmd);
            return;
        }
        if(CommandsUtility.emptyString(param)){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        String temp = param.trim().replaceAll(",", "");
        String[] split= temp.split(" ");
        ArrayList<ResourceData> resourceToSend=new ArrayList<>();
        for(int i=0;i<split.length; i+=2){
            if(CommandsUtility.isNotAResource(split[i])){
                PrintAssistant.instance.invalidParamCommand(cmd);
                return;
            }
            int num = CommandsUtility.stringToInt(split[i+1]);
            if(num==-1){
                PrintAssistant.instance.invalidParamCommand(cmd);
                return;
            }
            ResourceData rs = CommandsUtility.stringToResource(split[i], num);
            resourceToSend.add(rs);
        }
        client.writeToStream(new AnyResponse(resourceToSend));
    }

    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("Write the command followed by the type of resource you want and the number, if you want multiple resources just separate with comma.");
        rowHelp.add("\t\tco\tcoin");
        rowHelp.add("\t\tse\tservant");
        rowHelp.add("\t\tsh\tshield");
        rowHelp.add("\t\tst\tstone");
        rowHelp.add("ex: "+cmd.toLowerCase()+ " CO 2, SE 2");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"to choose in which Resources you want to convert the ANY");
    }
}
