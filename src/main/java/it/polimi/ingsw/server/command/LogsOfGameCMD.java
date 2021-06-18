package it.polimi.ingsw.server.command;

import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.Server;

/**
 * Command to print the logs of a match.
 */
public class LogsOfGameCMD implements ServerCommand{
    private final Server server;
    private final String param;

    /**
     * Construct a Logs of Game Command with specific parameters.
     * @param param the parameters of the command.
     * @param server the reference to the server instance.
     */
    public LogsOfGameCMD(String param, Server server){
        this.param=param;
        this.server=server;
    }

    /**
     * Print the logs of a specific match.
     */
    @Override
    public void doCommand() {
        String[] split=param.split(" ");
        int idGame;
        try{
            idGame=Integer.parseInt(split[0]);
        }catch(Exception e){
            System.out.println("Invalid param!");
            return;
        }
        Match match= server.getMatchWithId(idGame);
        if(match==null){
            System.out.println("Invalid param!");
            return;
        }
        if(split.length!=2){
            match.printLogs(50);
            return;
        }

        int numOfLogToShow;
        try{
            numOfLogToShow=Integer.parseInt(split[1]);
        }catch(Exception e){
            System.out.println("Invalid param!");
            return;
        }
        match.printLogs(numOfLogToShow);
    }
}
