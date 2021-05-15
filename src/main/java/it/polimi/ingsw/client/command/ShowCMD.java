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

        if(CommandsUtility.clientStateNot(client, ClientState.IN_GAME) && CommandsUtility.clientStateNot(client, ClientState.WAITING)){
            PrintAssistant.instance.invalidStateCommand(cmd);
            return;
        }

        if(CommandsUtility.emptyString(param)){
            client.getModelOf(client.getMyName()).printAll();
            return;
        }
        String[] split=param.split(" ");
        if(split[0].equals("market")){
            if(split.length==1)
                PrintAssistant.instance.printf(client.getMarketData().toString());
            else
                PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        if(split[0].equals("deckdev")){
            if(split.length==1)
                PrintAssistant.instance.printf(client.getDeckDevData().toString());
            else
                PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
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
                    client.getModelOf(split[0]).printCardSlots(false);
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
        rowHelp.add("Write the command followed by the name of player you want to see,");
        rowHelp.add("if you don't add the name of the player it'll show all your Personal Board.");
        rowHelp.add("You can ask to show only a part of board adding:");
        rowHelp.add("\t\t\tf -to show the faith track");
        rowHelp.add("\t\t\tr -to show the resource");
        rowHelp.add("\t\t\td -to show the developments");
        rowHelp.add("\t\t\tl -to show the leader");
        rowHelp.add("Write the command followed by those word to show market and the decks of developer:");
        rowHelp.add("\t\t\tmarket\t-to show the market");
        rowHelp.add("\t\t\tdeckdev\t-to show all the deck of developer cards");
        rowHelp.add("ex: "+cmd.toLowerCase()+" foo f");
        rowHelp.add("ex: "+cmd.toLowerCase()+" market");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"show on console the Personal Board of a player");
    }
}
