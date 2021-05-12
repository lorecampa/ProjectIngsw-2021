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
        if(client.getState()!= ClientState.IN_GAME){
            PrintAssistant.instance.invalidStateCommand(cmd);
            return;
        }
        if(param.isEmpty() || param.isBlank()){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        String[] split= param.split(" ");
        ArrayList<ResourceData> resourceToSend=new ArrayList<>();
        for(int i=0;i<split.length; i+=3){
            if(!split[i].equals("co") && !split[i].equals("se") && !split[i].equals("sh") && !split[i].equals("st")){
                PrintAssistant.instance.invalidParamCommand(cmd);
                return;
            }
            int num;
            try{
                num= Integer.parseInt(split[i+1]);
            } catch(Exception e){
                PrintAssistant.instance.invalidParamCommand(cmd);
                return;
            }
            ResourceData rs;
            switch (split[i]){
                case "co":
                    rs=new ResourceData(ResourceType.COIN, num);
                    break;
                case "se":
                    rs=new ResourceData(ResourceType.SERVANT, num);
                    break;
                case "sh":
                    rs=new ResourceData(ResourceType.SHIELD, num);
                    break;
                case "st":
                    rs=new ResourceData(ResourceType.STONE, num);
                    break;
                default:
                    rs=new ResourceData(ResourceType.ANY, num); //will never happen
            }
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
