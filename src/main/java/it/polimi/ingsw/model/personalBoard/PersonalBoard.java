package it.polimi.ingsw.model.personalBoard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.*;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.server.VirtualClient;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Personal Board is a class that contains all the part that compose the player's board.
 */
public class PersonalBoard {

    private final FaithTrack faithTrack;
    private final CardManager cardManager;
    private final ResourceManager resourceManager;
    private boolean inkwell;
    private final String username;

    /**
     * Construct a Personal Board of a specific player.
     * @param username the username of the player.
     * @param faithTrack the player's faith track.
     * @param resourceManager the player's resource manager.
     * @param cardManager the player's card manager.
     */
    @JsonCreator
    public PersonalBoard(@JsonProperty("username") String username,
                         @JsonProperty("faithTrack")FaithTrack faithTrack,
                         @JsonProperty("resourceManager")ResourceManager resourceManager,
                         @JsonProperty("cardManager")CardManager cardManager){

        this.username = username;
        this.faithTrack = faithTrack;
        this.resourceManager = resourceManager;
        this.cardManager = cardManager;
        this.inkwell=false;
    }

    /**
     * Return the player's faith track.
     * @return the player's faith track.
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Return the player's card manager.
     * @return the player's card manager.
     */
    public CardManager getCardManager(){
        return cardManager;
    }

    /**
     * Return the player's resource manager.
     * @return the player's resource manager.
     */
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    /**
     * Return true if the player has the inkwell.
     * @return true if the player has the inkwell.
     */
    public boolean isInkwell() {
        return inkwell;
    }

    /**
     * Set the inkwell to the value of the parameter inkwell.
     * @param inkwell the new value of inkwell.
     */
    public void setInkwell(boolean inkwell) {
        this.inkwell = inkwell;
    }

    /**
     * Return the player's username.
     * @return the player's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Attach the gameMaster to all the attribute that's observable by the Game Master.
     * @param gameMaster the match Game Master.
     */
    public void attachGameMasterObserver(GameMaster gameMaster){
        faithTrack.attachGameMasterObserver(gameMaster);
        resourceManager.attachGameMasterObserver(gameMaster);
        cardManager.attachGameMasterObserver(gameMaster);
    }

    /**
     * Attach the virtualClient to all the attribute that's observable by the Virtual Client.
     * @param virtualClient the player's virtual client.
     */
    public void attachVirtualClient(VirtualClient virtualClient){
        faithTrack.attachObserver(virtualClient);
        resourceManager.attachObserver(virtualClient);
        cardManager.attachObserver(virtualClient);
    }

    /**
     * Return a ModelData of the player based on his personal board.
     * @param isCurrentPlayer true if the player is the current player.
     * @return a ModelData of the player based on his personal board.
     */
    public ModelData toClient(boolean isCurrentPlayer){
        ArrayList<FaithTrackData> playerFaithTrack = getFaithTrack().toFaithTrackData();
        int playerCurrentPos = getFaithTrack().getCurrentPositionOnTrack();
        ArrayList<ResourceData> standardDepots = getResourceManager().getWarehouse().toStandardDepotData();
        ArrayList<ResourceData> leaderDepots = getResourceManager().getWarehouse().toLeaderDepotData();
        ArrayList<Integer> maxStorageLeaderDepots = getResourceManager().getWarehouse().toLeaderDepotMax();
        ArrayList<ResourceData> strongbox = getResourceManager().getStrongbox().toStrongboxData();
        ArrayList<ArrayList<CardDevData>> cardSlots = getCardManager().toCardSlotsData();
        ArrayList<CardLeaderData> leadersData = getCardManager().toLeadersData();

        if (!isCurrentPlayer){
            leadersData = leadersData.stream()
                    .filter(CardLeaderData::isActive)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return new ModelData(username,playerFaithTrack,playerCurrentPos,
                standardDepots,leaderDepots,maxStorageLeaderDepots,
                strongbox,cardSlots,leadersData);
    }
}
