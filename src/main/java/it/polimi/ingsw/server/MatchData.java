package it.polimi.ingsw.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.GameMaster;
import java.util.ArrayList;
import java.util.HashMap;


public class MatchData {
    private HashMap<String, Integer> allPlayers;
    private ArrayList<String> logs;
    private int matchID;
    private int numOfPlayers;
    private GameMaster gameMaster;


    @JsonCreator
    public MatchData(){}

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


    public Match createMatch(Server server){
        return new Match(server, matchID, numOfPlayers, allPlayers, logs, gameMaster);

    }


}
