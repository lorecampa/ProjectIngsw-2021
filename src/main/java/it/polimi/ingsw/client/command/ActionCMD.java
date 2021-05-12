package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.message.serverMessage.DevelopmentAction;
import it.polimi.ingsw.message.serverMessage.LeaderManage;
import it.polimi.ingsw.message.serverMessage.MarketAction;
import it.polimi.ingsw.message.serverMessage.ProductionAction;

import java.util.ArrayList;

public class ActionCMD implements Command{
    private final String cmd="ACTION";
    private final String param;
    private final Client client;

    public ActionCMD(String param, Client client) {
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
        switch(split[0]){
            case "produce":
                if(split.length>3 || split.length<2)
                    PrintAssistant.instance.invalidParamCommand(cmd);
                if(!split[1].equals("cs") && !split[1].equals("le") && !split[1].equals("bp"))
                    PrintAssistant.instance.invalidParamCommand(cmd);
                produce(split);
                break;
            case "developer":
                if(split.length!=4)
                    PrintAssistant.instance.invalidParamCommand(cmd);
                developer(split);
                break;
            case "market":
                if(split.length!=3)
                    PrintAssistant.instance.invalidParamCommand(cmd);
                market(split);
                break;
            case "leader":
                if(split.length!=2)
                    PrintAssistant.instance.invalidParamCommand(cmd);
                leader(split);
                break;
            default:
                PrintAssistant.instance.invalidParamCommand(cmd);
        }
    }

    @Override
    public void help() {
        ArrayList<String> rowHelp= new ArrayList<>();
        rowHelp.add("HELP: "+cmd);
        rowHelp.add("Write the command followed by the name of the action you want to do,");
        rowHelp.add("Action available:");
        rowHelp.add("\tproduce\t\t-to produce from a card slot[cs], a leader[le] or the base production[bp].");
        rowHelp.add("\t\t\t\tCard slot have to be followed by a number representing the number of card slot(1-3).");
        rowHelp.add("\t\t\t\tLeader have to be followed by the position of the leader on your Personal Board(1-4).");
        rowHelp.add("\t\t\t\tYou can send multiple action produce during your turn!");
        rowHelp.add("\t\t\t\tex: "+cmd.toLowerCase()+" produce le 1");
        rowHelp.add("\t\t\t\t    "+cmd.toLowerCase()+" produce cs 2");
        rowHelp.add("\tdeveloper\t-to buy a new developer card, followed by the level(1-3) and column(1-4) where it is placed in the DevDeck,");
        rowHelp.add("\t\t\t\tthe last number you write will be the card slot where you want to place the card you are buying.");
        rowHelp.add("\t\t\t\tex: "+cmd.toLowerCase()+" developer 2 3 1");
        rowHelp.add("\tmarket\t\t-to activate the market, followed by 'col' or 'row' and the number of the corresponding line");
        rowHelp.add("\t\t\t\tex: "+cmd.toLowerCase()+" market row 0");
        rowHelp.add("\tleader\t\t-to activate a leader followed by the position of the leader on your Personal Board(1-4)");
        rowHelp.add("\t\t\t\tex: "+cmd.toLowerCase()+" leader 1");
        PrintAssistant.instance.printfMultipleString(rowHelp);
    }

    @Override
    public void description() {
        PrintAssistant.instance.printf(PrintAssistant.instance.fitToWidth(cmd, ClientInput.MAX_CHAR_COMMAND)+"to activate an action at the begging of your turn");
    }

    public void produce(String[] split){
        switch(split[1]){
            case "cs":
                int cardSlot=0;
                try{
                    cardSlot=Integer.parseInt(split[2]);
                }
                catch(Exception e){
                    PrintAssistant.instance.invalidParamCommand(cmd);
                }
                if(cardSlot>3 || cardSlot<1){
                    PrintAssistant.instance.invalidParamCommand(cmd);
                    return;
                }
                cardSlot--;
                client.writeToStream(new ProductionAction(cardSlot,false, false));
                break;
            case "le":
                int leaderIndex=0;
                try{
                    leaderIndex=Integer.parseInt(split[2]);
                }
                catch(Exception e){
                    PrintAssistant.instance.invalidParamCommand(cmd);
                }
                leaderIndex--;
                if(!client.getModelOf(client.getMyName()).validIndexForLeader(leaderIndex)){
                    PrintAssistant.instance.invalidParamCommand(cmd);
                    return;
                }
                client.writeToStream(new ProductionAction(leaderIndex,false, true));
                break;
            case "bs":
                if(split.length>2)
                    PrintAssistant.instance.invalidParamCommand(cmd);
                client.writeToStream(new ProductionAction(0,true, false));
                break;
            default:
                PrintAssistant.instance.invalidParamCommand(cmd); //should never happen
        }
    }

    public void developer(String[] split){
        int level=0;
        try{
            level=Integer.parseInt(split[1]);
        }
        catch(Exception e){
            PrintAssistant.instance.invalidParamCommand(cmd);
        }
        int column=0;
        try{
            column=Integer.parseInt(split[2]);
        }
        catch(Exception e){
            PrintAssistant.instance.invalidParamCommand(cmd);
        }
        int cardSlot=0;
        try{
            cardSlot=Integer.parseInt(split[3]);
        }
        catch(Exception e){
            PrintAssistant.instance.invalidParamCommand(cmd);
        }
        if(cardSlot>3 || cardSlot<1){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }

        level--;
        column--;
        cardSlot--;
        if(!client.getDeckDevData().rowColValid(level, column)){    //need the index value not the real value so i check after decrement
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        client.writeToStream(new DevelopmentAction(level, column,cardSlot));
    }

    public void market(String[] split){
        switch (split[1]){
            case "col":
                int indexCol=0;
                try{
                    indexCol=Integer.parseInt(split[2]);
                }
                catch(Exception e){
                    PrintAssistant.instance.invalidParamCommand(cmd);
                }
                if(client.getMarketData().validCol(indexCol)){
                    PrintAssistant.instance.invalidParamCommand(cmd);
                    return;
                }
                client.writeToStream(new MarketAction(indexCol,false,client.getMyName()));
                break;
            case "row":
                int indexRow=0;
                try{
                    indexRow=Integer.parseInt(split[2]);
                }
                catch(Exception e){
                    PrintAssistant.instance.invalidParamCommand(cmd);
                }
                if(client.getMarketData().validRow(indexRow)){
                    PrintAssistant.instance.invalidParamCommand(cmd);
                    return;
                }
                client.writeToStream(new MarketAction(indexRow,true,client.getMyName()));
                break;
            default:
                PrintAssistant.instance.invalidParamCommand(cmd);
        }
    }

    public void leader(String[] split){
        int leaderIndex=0;
        try{
            leaderIndex=Integer.parseInt(split[1]);
        }
        catch (Exception e){
            PrintAssistant.instance.invalidParamCommand(cmd);
        }
        leaderIndex--;
        if(!client.getModelOf(client.getMyName()).validIndexForLeader(leaderIndex)){
            PrintAssistant.instance.invalidParamCommand(cmd);
            return;
        }
        client.writeToStream(new LeaderManage(leaderIndex, false));
    }
}
