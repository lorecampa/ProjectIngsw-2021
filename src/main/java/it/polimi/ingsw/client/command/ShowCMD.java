package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInput;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.PrintAssistant;

import java.util.ArrayList;

public class ShowCMD implements Command{
    private final String cmd = "SHOW";
    private final String param;
    private final Client client;

    public ShowCMD(String param, Client client) {
        this.param = param;
        this.client = client;
    }

    @Override
    public void doCommand() {
        if(client.getState()!= ClientState.IN_GAME){
            PrintAssistant.instance.invalidStateCommand(cmd);
            return;
        }

        if(param.isBlank() || param.isEmpty()){
            client.getModelOf(client.getMyName()).printAll();
            return;
        }
        String[] split=param.split(" ");
        //System.out.println("Stampo il nome:"+split[0]);
        if(!client.existAModelOf(split[0])){                    //se non esite user scritto mi fermo
            //System.out.println("dal primo lancio l'errore");
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }

        if(split.length==1){                                            //ha scritto solo il nome dell'user, stampo tutto
            client.getModelOf(split[0]).printAll();
            return;
        }
        for(int i=1; i<split.length; i++){                              //controllo che gli altri campi siano comandi conosciuti
            if(!split[i].equals("f") && !split[i].equals("r") && !split[i].equals("d") && !split[i].equals("l")){
                PrintAssistant.instance.invalidParamCommand(cmd);
                return;
            }
        }

        for(int i=1;i<split.length;i++){
            switch(split[i]){
                case "f":
                    client.getModelOf(split[0]).printFaithTrack();
                    break;
                case "r":
                    client.getModelOf(split[0]).printResource();
                    break;
                case "d":
                    client.getModelOf(split[0]).printCardSlots();
                    break;
                case "l":
                    client.getModelOf(split[0]).printLeader();
                    break;
            }
        }
    }

    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("Write the command followed by the name of player you want to see");
        rowHelp.add("if you don't add the name of the player it'll show all your Personal Board");
        rowHelp.add("you can ask to show only a part of his board adding:");
        rowHelp.add("\t\t\tf -to show the faith track");
        rowHelp.add("\t\t\tr -to show the resource");
        rowHelp.add("\t\t\td -to show the developments");
        rowHelp.add("\t\t\tl -to show the leader");
        rowHelp.add("ex: "+cmd.toLowerCase()+" foo f");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"show on console the Personal Board of a player");
    }
}
