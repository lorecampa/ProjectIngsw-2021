package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.model.resource.ResourceType;

public class CommandsUtility {

    public static boolean clientStateNot(Client client, ClientState state){
        return client.getState()!=state;
    }

    public static boolean emptyString(String s){
        return s.isEmpty() || s.isBlank();
    }

    public static ResourceData stringToResource(String s, int value){
        ResourceData rs;
        switch (s){
            case "co":
                rs=new ResourceData(ResourceType.COIN, value);
                break;
            case "se":
                rs=new ResourceData(ResourceType.SERVANT, value);
                break;
            case "sh":
                rs=new ResourceData(ResourceType.SHIELD, value);
                break;
            case "st":
                rs=new ResourceData(ResourceType.STONE, value);
                break;
            default:
                rs=new ResourceData(ResourceType.ANY, value); //will never happen
        }
        return rs;
    }

    public static boolean isNotAResource(String s){
        return !s.equals("co") && !s.equals("se") && !s.equals("sh") && !s.equals("st");
    }

    public static int stringToInt(String s){
        int num=-1;
        try {
            num=Integer.parseInt(s);
        }
        catch(Exception ignored){
        }
        return num;
    }

    public static boolean isADepotIndex(int num){
        return num==1 || num==2 || num==3;
    }

    public static boolean isACardSlotIndex(int num){
        return num==1 || num==2 || num==3;
    }

    public static boolean isALeaderIndex(Client client, int num){
        return client.getModelOf(client.getMyName()).validIndexForLeader(num);
    }

    public static boolean isAValidCardInDeck(Client client, int level, int column){
        return client.getDeckDevData().rowColValid(level, column);
    }

    public static boolean isValidIndexDepotLeader(Client client, int index){
        return client.getModelOf(client.getMyName()).isValidIndexDepotLeader(index);
    }

    public static ResourceData fromTypeAndValueToResource(String type, String value) throws CliException{
        if(CommandsUtility.isNotAResource(type)){
            throw new CliException("Error type");
        }
        int amount=CommandsUtility.stringToInt(value);
        if(amount==-1){
            throw new CliException("Error amount");
        }
        return CommandsUtility.stringToResource(type, amount);
    }
}
