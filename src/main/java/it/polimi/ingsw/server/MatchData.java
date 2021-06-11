package it.polimi.ingsw.server;

import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.Strongbox;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MatchData {
    private final ArrayList<String> allPlayers;
    private final ArrayList<String> activePlayers;
    private final ArrayList<String> inactivePlayers;
    private final ArrayList<String> logs;
    private final int matchID;
    private final int numOfPlayers;
    private final GameMaster gameMaster;

    public MatchData(Match match){
        allPlayers = match.getAllPlayers().stream().map(VirtualClient::getUsername)
                .collect(Collectors.toCollection(ArrayList::new));
        activePlayers = match.getActivePlayers().stream().map(VirtualClient::getUsername)
                .collect(Collectors.toCollection(ArrayList::new));
        inactivePlayers = match.getInactivePlayers().stream().map(VirtualClient::getUsername)
                .collect(Collectors.toCollection(ArrayList::new));
        logs = match.getLogs();
        matchID = match.getMatchID();
        numOfPlayers = match.getNumOfPlayers();

        gameMaster = match.getController().getGameMaster();
    }
}
