package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.data.*;
import it.polimi.ingsw.exception.InvalidStateActionException;
import it.polimi.ingsw.message.clientMessage.*;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.PlayerState;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.server.HandlerState;
import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.VirtualClient;

import java.util.ArrayList;

public class Controller {
    private final GameMaster gameMaster;
    private final Match match;

    public Controller(GameMaster gameMaster, Match match) {
        this.gameMaster = gameMaster;
        this.match = match;
        registerAllVirtualClientObserver();
        if (getNumberOfPlayer() == 1){
            registerLorenzoIlMagnificoVC();
        }
    }


    public int getNumberOfPlayer(){
        return gameMaster.getNumberOfPlayer();
    }

    private void sendErrorTo(String customMessage, String username){
        match.sendSinglePlayer(username, new ErrorMessage(customMessage));

    }
    private void sendError(String customMessage){
        match.sendSinglePlayer(getCurrentPlayer(), new ErrorMessage(customMessage));
    }


    public PlayerState getPlayerState(){
        return gameMaster.getPlayerState();
    }


    public String getCurrentPlayer(){
        return gameMaster.getCurrentPlayer();
    }

    private void registerAllVirtualClientObserver(){
        for (VirtualClient virtualClient: match.getAllPlayers()){
            gameMaster.attachPlayerVC(virtualClient);
        }

    }

    private void registerLorenzoIlMagnificoVC(){
        VirtualClient lorenzoIlMagnificoVC = new VirtualClient(GameMaster.getNameLorenzo(), match);
        gameMaster.attachLorenzoIlMagnificoVC(lorenzoIlMagnificoVC);
    }

    //UTIL GETTER

    private PersonalBoard getPlayerPB() {
        return gameMaster.getCurrentPlayerPersonalBoard();
    }
    private CardManager getPlayerCM() {
        return getPlayerPB().getCardManager();
    }
    private ResourceManager getPlayerRM() {
        return getPlayerPB().getResourceManager();
    }
    private Market getMarket(){
        return gameMaster.getMarket();
    }

    public boolean isYourTurn(String username){
        if(!username.equals(getCurrentPlayer())){
            sendErrorTo(ErrorType.NOT_YOUR_TURN.getMessage(), username);
            return false;
        }
        return true;
    }


    //UTIL

    public void nextTurn() {
        do {
            try {
                gameMaster.nextPlayer();
            }catch (InvalidStateActionException e){
                sendError(e.getMessage());
            }
        }while(match.isInactive(gameMaster.getCurrentPlayer()));

        getPlayerRM().restoreRM();
        getPlayerCM().restoreCM();

        String currentPlayer = gameMaster.getCurrentPlayer();
        if (match.isReconnected(currentPlayer)){
            match.playerReturnInGame(currentPlayer);
            match.sendSinglePlayer(currentPlayer, reconnectGameMessage());
        }

        if(gameMaster.isGameEnded()){
            endGame();
        }
    }


    public void endGame(){
        match.removeMatchFromServer();
    }

    private ReconnectGameMessage reconnectGameMessage(){
        ArrayList<String> usernames = match.getUsernames();
        MarketData marketData = gameMaster.getMarket().toMarketData();
        DeckDevData deckDevData = gameMaster.toDeckDevData();
        ArrayList<EffectData> baseProdData = gameMaster.toEffectDataBasePro();
        ArrayList<ModelData> models = new ArrayList<>();
        for (String username : usernames){
            models.add(modelData(username));
        }
        return new ReconnectGameMessage(usernames,marketData,deckDevData,baseProdData,models);
    }

    private ModelData modelData(String username){
        return gameMaster.getPlayerModelData(username);
    }

    //LEADER MANAGING
    public void leaderManage(int leaderIndex, boolean discard){
        try{
            if(discard){
                getPlayerCM().discardLeader(leaderIndex);
            }else{
                getPlayerCM().activateLeader(leaderIndex);
                getPlayerRM().restoreRM();
            }
        }catch (Exception e){
            sendError(e.getMessage());
        }
    }


    //MARKET ACTION

    public void marketAction(int selection, boolean isRow){
        Market market = getMarket();
        CardManager cardManager = getPlayerCM();
        ResourceManager resourceManager = getPlayerRM();
        try{
            if (isRow){
                market.insertMarbleInRow(selection);
            }else{
                market.insertMarbleInCol(selection);
            }
        }catch (Exception e){
            sendError(e.getMessage());
            return;
        }

        int numOfMarbleEffects = cardManager.howManyMarbleEffects();
        int whiteMarbleDrew = market.getWhiteMarbleDrew();

        if (!(numOfMarbleEffects >= 2 && whiteMarbleDrew > 0)){
            if (numOfMarbleEffects == 1 && whiteMarbleDrew > 0){
                try{
                    market.setWhiteMarbleToTransform(market.getWhiteMarbleDrew());
                    cardManager.getLeaders().stream()
                            .filter(Leader::isActive)
                            .forEach(x -> {
                                try {
                                    x.doActivationEffects(getPlayerState());
                                } catch (Exception ignored) {}
                            });
                }catch (InvalidStateActionException e){
                    sendError(e.getMessage());
                }
            }
            resourceManager.resourceFromMarket(market.getResourceToSend());
            market.reset();
        }else{
            match.sendSinglePlayer(getCurrentPlayer(),
                    new WhiteMarbleConversionRequest(whiteMarbleDrew, cardManager.listOfMarbleEffect()));
        }
    }


    public void leaderWhiteMarbleConversion(int leaderIndex, int numOfWhiteMarble){
        CardManager cardManager = getPlayerCM();
        Market market = getMarket();
        if(cardManager.howManyMarbleEffects() <= 0){
            sendError("You don't have leader with marble effects");
            return;
        }

        try{
            market.setWhiteMarbleToTransform(numOfWhiteMarble);
            cardManager.activateLeaderInfinite(leaderIndex, getPlayerState());

            if(market.getWhiteMarbleDrew() == 0){
                getPlayerRM().resourceFromMarket(market.getResourceToSend());
                market.reset();
            }
        }catch (Exception e){
            sendError(e.getMessage());
        }


    }

    public void clearBufferFromMarket(){
        try {
            getPlayerRM().discardResourcesFromMarket();
            controlBufferStatus();
        }catch (InvalidStateActionException e){
            sendError(e.getMessage());
        }
    }



    //BUY DEVELOPMENT CARD
    public void developmentAction(int row, int col, int locateSlot){
        Development card;
        CardManager cardManager = getPlayerCM();
        try {
            card = gameMaster.getDeckDevelopmentCard(row, col);
            cardManager.addDevCardTo(card, locateSlot);
            cardManager.setDeckBufferInfo(row, col);
            card.checkRequirements();
        } catch (Exception e) {
            sendError(e.getMessage());
            return;
        }
        controlBufferStatus();
    }


    //PRODUCTION ACTION

    public void normalProductionAction(int cardSlot){
        try {
            getPlayerCM().developmentProduce(cardSlot);
        } catch (Exception e) {
            sendError(e.getMessage());
        }
    }

    public void baseProduction(){
        try {
            getPlayerCM().baseProductionProduce();
        } catch (Exception e) {
            sendError(e.getMessage());
        }
    }


    public void leaderProductionAction(int leaderIndex){
        CardManager cardManager = getPlayerCM();
        if (cardManager.howManyProductionEffects() <= 0){
            sendError("You don't have leader with production effect");
        }else{
            try {
                cardManager.activateLeaderEffect(leaderIndex, getPlayerState());
            } catch (Exception e) {
                sendError(e.getMessage());
            }
        }
    }


    public void stopProductionCardSelection(){
        try {
            getPlayerRM().stopProduction();
        } catch (InvalidStateActionException e) {
            sendError(e.getMessage());
        }
    }

    //ANY
    public void anyConversion(ArrayList<Resource> resources){
        PlayerState state = getPlayerState();
        try {
            if (state == PlayerState.ANY_PRODUCE_COST_CONVERSION){
                getPlayerRM().convertAnyRequirement(resources, false);
            }else if (state == PlayerState.ANY_PRODUCE_PROFIT_CONVERSION){
                getPlayerRM().convertAnyProductionProfit(resources);
            }else if(state == PlayerState.ANY_BUY_DEV_CONVERSION){
                getPlayerRM().convertAnyRequirement(resources, true);
            }else{
                sendError(ErrorType.INVALID_ACTION.getMessage());
            }
        }catch (Exception e){
            sendError(e.getMessage());
        }
    }


    //WAREHOUSE
    private void controlBufferStatus(){
        ResourceManager resourceManager = getPlayerRM();
        if(resourceManager.getBufferSize() == 0){
            switch (getPlayerState()){
                case MARKET_RESOURCE_POSITIONING:
                    resourceManager.applyFaithPoints();
                    break;
                case BUY_DEV_RESOURCE_REMOVING:
                    getPlayerCM().emptyCardSlotBuffer();
                    break;
                case PRODUCTION_RESOURCE_REMOVING:
                    resourceManager.doProduction();
                    resourceManager.applyFaithPoints();
                    getPlayerCM().restoreCM();
                    break;
                default:
                    return;
            }
            resourceManager.restoreRM();
            gameMaster.onPlayerStateChange(PlayerState.LEADER_MANAGE_AFTER);
        }
    }



    public void subToStrongbox(Resource resource){
        ResourceManager resourceManager = getPlayerRM();
        try {
            resourceManager.subToBuffer(resource);
        } catch (Exception e) {
            sendError(e.getMessage());
            return;
        }
        try {
            resourceManager.subToStrongbox(resource);
        } catch (Exception e) {
            resourceManager.addToBuffer(resource);
            sendError(e.getMessage());
        }
        controlBufferStatus();
    }

    public void depotModify(Resource resource, int index, boolean isNormalDepot){
        ResourceManager resourceManager = getPlayerRM();
        try {
            resourceManager.subToBuffer(resource);
        } catch (Exception e) {
            sendError(e.getMessage());
            return;
        }
        try{
            switch (getPlayerState()){
                case MARKET_RESOURCE_POSITIONING:
                    resourceManager.addToWarehouse(isNormalDepot, index, resource);
                    break;
                case BUY_DEV_RESOURCE_REMOVING:
                case PRODUCTION_RESOURCE_REMOVING:
                    resourceManager.subToWarehouse(isNormalDepot, index, resource);
                    break;
                default:
                    sendError(ErrorType.INVALID_ACTION.getMessage());
                    break;
            }
        }catch (Exception e){
            resourceManager.addToBuffer(resource);
            sendError(e.getMessage());
        }
        controlBufferStatus();
    }


    public void switchDepots(int from, boolean isFromLeaderDepot, int to, boolean isToLeaderDepot){
        try {
            getPlayerRM().switchResourceFromDepotToDepot(from, isFromLeaderDepot, to, isToLeaderDepot);
        } catch (Exception e) {
            sendError(e.getMessage());
        }
    }


    //SETUP

    private boolean hasFinishedLeaderSetUp(String username){
        CardManager cardManager = gameMaster.getPlayerPersonalBoard(username).getCardManager();
        return cardManager.getLeaders().size() == 2;
    }

    public void autoDiscardLeaderSetUp( String username){
        while (!hasFinishedLeaderSetUp(username)){
            discardLeaderSetUp(0,username);
        }
        autoInsertSetUpResources(username);
    }

    public void discardLeaderSetUp(int leaderIndex, String username){
        CardManager playerCardManager = gameMaster.getPlayerPersonalBoard(username).getCardManager();
        try {
            playerCardManager.discardLeaderSetUp(leaderIndex);
            if (hasFinishedLeaderSetUp(username)){
                FaithTrack playerFaithTrack = gameMaster.getPlayerPersonalBoard(username).getFaithTrack();
                switch (gameMaster.getPlayerPosition(username)){
                    case 0:
                        match.sendSinglePlayer(username, new AnyConversionRequest(0));
                        match.getPlayer(username).ifPresent(x->x.getClient().setState(HandlerState.WAITING_TO_BE_IN_MATCH));
                        if(isFinishedSetup()){
                            match.getAllPlayers().forEach(x -> x.getClient().setState(HandlerState.IN_MATCH));
                            match.sendAllPlayers(new MatchStart());
                            nextTurn();
                        }
                        break;
                    case 1:
                        match.sendSinglePlayer(username, new AnyConversionRequest(1));
                        match.getPlayer(username).ifPresent(x->x.getClient().setState(HandlerState.RESOURCE_SETUP));
                        break;
                    case 2:
                        match.sendSinglePlayer(username, new AnyConversionRequest(1));
                        playerFaithTrack.movePlayer(1);
                        match.getPlayer(username).ifPresent(x->x.getClient().setState(HandlerState.RESOURCE_SETUP));
                        break;
                    case 3:
                        match.sendSinglePlayer(username,new AnyConversionRequest(2));
                        playerFaithTrack.movePlayer(1);
                        match.getPlayer(username).ifPresent(x->x.getClient().setState(HandlerState.RESOURCE_SETUP));
                        break;
                }
            }

        }catch (Exception e) {
            sendErrorTo(e.getMessage(), username);
        }

    }

    private boolean isFinishedSetup(){
        for(VirtualClient player : match.getAllPlayers()){
            if(player.getClient().getState()!= HandlerState.WAITING_TO_BE_IN_MATCH)
                return false;
        }
        return true;
    }

    public void autoInsertSetUpResources(String username){
        ArrayList<Resource> resources = new ArrayList<>();
        switch (gameMaster.getPlayerPosition(username)){
            case 1:
            case 2:
                resources.add(ResourceFactory.createResource(ResourceType.COIN,1));
                insertSetUpResources(resources,username);
                break;
            case 3:
                resources.add(ResourceFactory.createResource(ResourceType.COIN,2));
                insertSetUpResources(resources,username);
                break;
        }
    }

    public void insertSetUpResources(ArrayList<Resource> resources, String username){
        ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(username).getResourceManager();
        int sizeResponse = resources.stream().mapToInt(Resource::getValue).sum();
        try{
            switch (gameMaster.getPlayerPosition(username)){
                case 1:
                case 2:
                    if (sizeResponse == 1){
                        resourceManager.addToWarehouse(true, 0, resources.get(0));
                    }else{
                        sendErrorTo("Too many resources sent", username);
                        return;
                    }
                    break;
                case 3:
                    if (sizeResponse == 2 && resources.size() == 1){
                        resourceManager.addToWarehouse(true, 1, resources.get(0));
                    }else if(sizeResponse == 2 && resources.size() == 2){
                        resourceManager.addToWarehouse(true, 0, resources.get(0));
                        resourceManager.addToWarehouse(true, 1, resources.get(1));
                    }else{
                        sendErrorTo("Too many resources sent", username);
                        return;
                    }
                    break;
            }
            match.getPlayer(username).ifPresent(y -> y.getClient().setState(HandlerState.WAITING_TO_BE_IN_MATCH));
            if(isFinishedSetup()){
                match.getAllPlayers().forEach(x -> x.getClient().setState(HandlerState.IN_MATCH));
                match.sendAllPlayers(new MatchStart());
                nextTurn();
            }
        }catch (Exception e){
            sendErrorTo(e.getMessage(), username);
        }

    }

    public GameMaster getGameMaster() {
        return gameMaster;
    }
}
