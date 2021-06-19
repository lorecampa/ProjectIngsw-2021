package it.polimi.ingsw.server.command;

import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.Server;
/**
 * Command to cheat
 * */
public class ResourcesCMD implements ServerCommand{
    private final Server server;
    private final String param;

    /**
     * Construct a Resources Command with specific parameters.
     * @param param the parameters of the command.
     * @param server the reference to the server instance.
     */
    public ResourcesCMD(String param, Server server){
        this.param=param;
        this.server=server;
    }

   /**
    * Give 20 resource in strongbox to all player in a index match.
    */
    @Override
    public void doCommand() {
        String[] split = param.split(" ");
        if(split.length!=1){
            System.out.println("Invalid param!");
            return;
        }
        int indexMatch;
        try{
            indexMatch=Integer.parseInt(split[0]);
        }   catch(Exception e){
            System.out.println("Invalid param, need a number!");
            return;
        }
        Match match=server.getMatchWithId(indexMatch);
        if(match==null){
            System.out.println("Invalid param, no match with id: "+indexMatch+"!");
            return;
        }
        server.getMatchWithId(indexMatch).getController().cheat();
        System.out.println("Added 20 coin, servant, stone, shield to all strongboxes of match "+indexMatch+"!");
    }
}
