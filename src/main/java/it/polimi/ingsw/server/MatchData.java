package it.polimi.ingsw.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.GameMaster;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains all the information to restore a match after a Server disconnection.
 */
public class MatchData {
    private HashMap<String, Integer> allPlayers;
    private ArrayList<String> logs;
    private int matchID;
    private int numOfPlayers;
    private GameMaster gameMaster;


    @JsonCreator
    public MatchData(){}



    /**
     * Construct a Match Data based on a match.
     * @param match the match with the information.
     * @param gameMaster the Game Master of the match.
     */
    public MatchData(Match match, GameMaster gameMaster){
        allPlayers = new HashMap<>();
        for (VirtualClient vc: match.getAllPlayers()){
            allPlayers.put(vc.getUsername(), vc.getClientID());
        }

        logs = match.getLogs();
        matchID = match.getMatchID();
        numOfPlayers = match.getNumOfPlayers();

        this.gameMaster = gameMaster;

    }

    /**
     * Return a Match based on saved information.
     * @param server the reference of the Server.
     * @return a Match based on saved information.
     */
    public Match createMatch(Server server){
        return new Match(server, matchID, numOfPlayers, allPlayers, logs, gameMaster);
    }
}
