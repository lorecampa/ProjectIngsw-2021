package it.polimi.ingsw.client.command;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.model.resource.ResourceType;

public class CommandsUtility {

    /**
     * Return true if the client is not in the state
     * @param client to check
     * @param state i want to know if i'm in or not
     * @return true if the client is not in the state
     * */
    public static boolean clientStateNot(Client client, ClientState state){
        return client.getState()!=state;
    }

    /**
     * Return true if s is empty
     * @param s string to check
     * */
    public static boolean emptyString(String s){
        return s.isEmpty() || s.isBlank();
    }

    /**
     * Return a {@link ResourceData} from the string and the value
     * @param s string that represent a resource type
     * @param value of resources
     * @return a {@link ResourceData} from the string and the value
     * */
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

    /**
     * Return true if s is not a resource type known
     * @param s string to check
     * @return  true if s is not a resource type known
     * */
    public static boolean isNotAResource(String s){
        return !s.equals("co") && !s.equals("se") && !s.equals("sh") && !s.equals("st");
    }

    /**
     * Return the integer from s
     * @param s string that contains the number in character format
     * @return the integer from s
     * */
    public static int stringToInt(String s){
        int num=-1;
        try {
            num=Integer.parseInt(s);
        }
        catch(Exception ignored){
        }
        return num;
    }

    /**
     * Return true if num is not a normal depot index
     * @param num represent the index checking
     * @return true if num is not a normal depot index
     * */
    public static boolean isNotADepotIndex(int num){
        return num != 1 && num != 2 && num != 3;
    }

    /**
     * Return true if num is a card slot index
     * @param num represent the index checking
     * @return true if num is a card slot index
     * */
    public static boolean isACardSlotIndex(int num){
        return num==1 || num==2 || num==3;
    }

    /**
     * Return true if client has not a leader at num index
     * @param client is checking for
     * @param num represent the index
     * @return true if client has not a leader at num index
     * */
    public static boolean isNotALeaderIndex(Client client, int num){
        return client.getModelOf(client.getMyName()).notValidIndexForLeader(num);
    }

    /**
     * Return true if the card selected is a valid input for the deck Dev
     * @param client is checking
     * @param column chosen
     * @param level chosen
     * @return true if the card selected is a valid input for the deck Dev
     * */
    public static boolean isAValidCardInDeck(Client client, int level, int column){
        return client.getDeckDevData().rowColValid(level, column);
    }

    /**
     * Return true if num is not a leader depot index
     * @param index checking
     * @return true if num is not a leader depot index
     * */
    public static boolean isNotValidIndexDepotLeader(Client client, int index){
        return !client.getModelOf(client.getMyName()).isValidIndexDepotLeader(index);
    }

    /**
     * Return the {@link ResourceData} from a type and a value
     * @param value of resource i want to create
     * @param type of resource i want to create
     * @return the {@link ResourceData} from a type and a value
     * */
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
