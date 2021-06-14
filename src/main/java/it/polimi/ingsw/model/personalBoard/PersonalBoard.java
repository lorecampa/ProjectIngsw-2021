package it.polimi.ingsw.model.personalBoard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.*;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.PlayerState;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.server.VirtualClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PersonalBoard {

    private final FaithTrack faithTrack;
    private final CardManager cardManager;
    private final ResourceManager resourceManager;
    private boolean inkwell;
    private final String username;


    @JsonCreator
    public PersonalBoard(@JsonProperty("username") String username,
                         @JsonProperty("faithTrack")FaithTrack faithTrack,
                         @JsonProperty("resourceManager")ResourceManager resourceManager,
                         @JsonProperty("cardManager")CardManager cardManager) throws IOException {

        this.username = username;
        this.faithTrack = faithTrack;
        this.resourceManager = resourceManager;
        this.cardManager = cardManager;
        this.inkwell=false;

    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public CardManager getCardManager(){
        return cardManager;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public boolean isInkwell() {
        return inkwell;
    }

    public void setInkwell(boolean inkwell) {
        this.inkwell = inkwell;
    }

    public String getUsername() {
        return username;
    }



    public void attachGameMasterObserver(GameMaster gameMaster){
        faithTrack.attachGameMasterObserver(gameMaster);
        resourceManager.attachGameMasterObserver(gameMaster);
        cardManager.attachGameMasterObserver(gameMaster);
    }

    public void attachVirtualClient(VirtualClient virtualClient){
        faithTrack.attachObserver(virtualClient);
        resourceManager.attachObserver(virtualClient);
        cardManager.attachObserver(virtualClient);

    }

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
