package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.data.*;
import it.polimi.ingsw.message.clientMessage.*;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Effect.Activation.MarbleEffect;
import it.polimi.ingsw.model.card.Effect.Creation.DiscountEffect;
import it.polimi.ingsw.model.card.Leader;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.server.HandlerState;
import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.VirtualClient;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Controller {
    private final GameMaster gameMaster;
    private Match match;

    public Controller(GameMaster gameMaster, Match match) {
        this.gameMaster = gameMaster;
        this.match = match;
        registerAllVirtualClientObserver();
    }



    public void changeTurnState(TurnState state){
        gameMaster.onTurnStateChange(state);
    }

    public int getNumberOfPlayer(){
        return gameMaster.getNumberOfPlayer();
    }

    private void sendError(ErrorType errorType){
        match.sendSinglePlayer(gameMaster.getCurrentPlayer(), new ErrorMessage(errorType));

    }
    private void sendError(String customMessage){
        match.sendSinglePlayer(gameMaster.getCurrentPlayer(), new ErrorMessage(customMessage));

    }

    private void sendErrorTo(String customMessage, String username){
        match.sendSinglePlayer(username, new ErrorMessage(customMessage));

    }


    public TurnState getTurnState(){
        return gameMaster.getTurnState();
    }


    public boolean isYourTurn(String username){
        return getCurrentPlayer().equals(username);
    }


    private String getCurrentPlayer(){
        return gameMaster.getCurrentPlayer();
    }


    private void registerAllVirtualClientObserver(){
        for (VirtualClient virtualClient: match.getAllPlayers()){
            String username = virtualClient.getUsername();
            //model observer
            gameMaster.attachObserver(virtualClient);

            //resource manager observer
            gameMaster.getPlayerPersonalBoard(username)
                    .getResourceManager().attachObserver(virtualClient);

            //faith track observer
            gameMaster.getPlayerPersonalBoard(username)
                    .getFaithTrack().attachObserver(virtualClient);

            //card manager
            gameMaster.getPlayerPersonalBoard(username)
                    .getCardManager().attachObserver(virtualClient);

            //market
            gameMaster.getMarket().attachObserver(virtualClient);
        }

    }



    //SINGLE PLAYER

    public void drawTokenSinglePlayer(){
        gameMaster.drawToken();
    }

    //UTIL

    public void nextTurn() {
        do {
            gameMaster.nextPlayer();
            PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
            personalBoard.getResourceManager().restoreRM();
            personalBoard.getCardManager().restoreCM();
        }while(match.isInactive(gameMaster.getCurrentPlayer()));

        if (match.isReconnected(gameMaster.getCurrentPlayer())){
            match.playerReturnInGame(gameMaster.getCurrentPlayer());
            match.sendSinglePlayer(gameMaster.getCurrentPlayer(), reconnectGameMessage());
        }

        if(gameMaster.isGameEnded()){
            match.getAllPlayers().forEach(x->x.getClient().setState(HandlerState.FIRST_CONTACT));
            match.removeMatchFromServer();
        }
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
        PersonalBoard playerPB = gameMaster.getPlayerPersonalBoard(username);
        ArrayList<FaithTrackData> playerFaithTrack = playerPB.getFaithTrack().toFaithTrackData();
        int playerCurrentPos = playerPB.getFaithTrack().getCurrentPositionOnTrack();
        ArrayList<ResourceData> standardDepots = playerPB.getResourceManager().getWarehouse().toStandardDepotData();
        ArrayList<ResourceData> leaderDepots = playerPB.getResourceManager().getWarehouse().toLeaderDepotData();
        ArrayList<Integer> maxStorageLeaderDepots = playerPB.getResourceManager().getWarehouse().toLeaderDepotMax();
        ArrayList<ResourceData> strongbox = playerPB.getResourceManager().getStrongbox().toStrongboxData();
        ArrayList<ArrayList<CardDevData>> cardSlots = playerPB.getCardManager().toCardSlotsData();
        ArrayList<CardLeaderData> leadersData = playerPB.getCardManager().toLeadersData();

        if (!username.equals(gameMaster.getCurrentPlayer()))
            leadersData = leadersData.stream().filter(CardLeaderData::isActive).collect(Collectors.toCollection(ArrayList::new));

        return new ModelData(username,playerFaithTrack,playerCurrentPos,standardDepots,leaderDepots,maxStorageLeaderDepots,strongbox,cardSlots,leadersData);
    }



    //LEADER MANAGING

    public void leaderManage(int leaderIndex, boolean discard){
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        CardManager cardManager = personalBoard.getCardManager();
        ResourceManager resourceManager = personalBoard.getResourceManager();
        try{
            if(discard){
                cardManager.discardLeader(leaderIndex);
            }else{
                cardManager.activateLeader(leaderIndex);
                resourceManager.restoreRM();
            }
        }catch (Exception e){
            sendError(e.getMessage());
        }
    }


    //MARKET ACTION

    public void marketAction(int selection, boolean isRow){
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        Market market = gameMaster.getMarket();
        CardManager cardManager = personalBoard.getCardManager();
        try{
            if (isRow)
                market.insertMarbleInRow(selection);
            else
                market.insertMarbleInCol(selection);
        }catch (Exception e){
            sendError(e.getMessage());
            return;
        }

        int numOfMarbleEffects = cardManager.howManyMarbleEffects();
        int whiteMarbleDrew = market.getWhiteMarbleDrew();

        if (!(numOfMarbleEffects >= 2 && whiteMarbleDrew > 0)){

            if (numOfMarbleEffects == 1 && whiteMarbleDrew > 0){
                market.setWhiteMarbleToTransform(market.getWhiteMarbleDrew());
                cardManager.getLeaders().stream()
                        .filter(Leader::isActive)
                        .filter(x -> x.getOnActivationEffects().stream()
                                .anyMatch(effect -> effect instanceof MarbleEffect))
                        .mapToInt(x -> cardManager.getLeaders().indexOf(x))
                        .findFirst()
                        .ifPresent(x -> {
                            try {
                                cardManager.activateLeaderEffect(x, getTurnState());
                            } catch (Exception e) {
                                //it will never occur
                                e.printStackTrace();
                            }
                        });
            }

            ResourceManager resourceManager = personalBoard.getResourceManager();
            resourceManager.resourceFromMarket(market.getResourceToSend());
            market.reset();
        }else{
            match.sendSinglePlayer(getCurrentPlayer(),
                    new WhiteMarbleConversionRequest(whiteMarbleDrew, cardManager.listOfMarbleEffect()));

        }


    }


    public void leaderWhiteMarbleConversion(int leaderIndex, int numOfWhiteMarble){
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        CardManager cardManager = personalBoard.getCardManager();
        int numOfMarbleEffects = cardManager.howManyMarbleEffects();
        if(numOfMarbleEffects <= 0){
            sendError("You don't have leader with marble effects");
            return;
        }
        Market market = gameMaster.getMarket();
        market.setWhiteMarbleToTransform(numOfWhiteMarble);

        try{
            cardManager.activateLeaderEffect(leaderIndex, getTurnState());
        }catch (Exception e){
            sendError(e.getMessage());
            return;
        }

        if(market.getWhiteMarbleDrew() == 0){
            ResourceManager resourceManager = personalBoard.getResourceManager();
            resourceManager.resourceFromMarket(market.getResourceToSend());
            market.reset();
        }
    }

    public void clearBufferFromMarket(){
        ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getResourceManager();
        resourceManager.discardResourcesFromMarket();
        controlBufferStatus();
    }





    //BUY DEVELOPMENT CARD
    public void developmentAction(int row, int col, int locateSlot){
        Development card;
        try{
            card = gameMaster.getDeckDevelopmentCard(row, col);
        }catch (Exception e){
            sendError(e.getMessage());
            return;
        }

        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        CardManager cardManager = personalBoard.getCardManager();
        try {
            cardManager.addDevCardTo(card, locateSlot);
            cardManager.setDeckBufferInfo(row, col);
        } catch (Exception e) {
            sendError(e.getMessage());
            return;
        }

        try{
            card.checkRequirements();
        }catch (Exception e){
            sendError(e.getMessage());
            return;
        }


        //control in case you can buy instantly the card without paying (buffer is empty)
        controlBufferStatus();
    }


    //PRODUCTION ACTION

    public void normalProductionAction(int cardSlot){
        CardManager cardManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getCardManager();
        try {
            cardManager.developmentProduce(cardSlot);

        } catch (Exception e) {
            sendError(e.getMessage());
        }
    }

    public void baseProduction(){
        CardManager cardManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getCardManager();
        try {
            cardManager.baseProductionProduce();

        } catch (Exception e) {
            sendError(e.getMessage());
        }
    }


    public void leaderProductionAction(int leaderIndex){
        CardManager cardManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getCardManager();
        int numOfProductionEffects = cardManager.howManyProductionEffects();
        if (numOfProductionEffects <= 0){
            sendError("You don't have leader with production effect");
            return;
        }
        try {
            cardManager.activateLeaderEffect(leaderIndex, getTurnState());
        } catch (Exception e) {
            sendError(e.getMessage());
        }


    }


    public void stopProductionCardSelection(){
        ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getResourceManager();
        resourceManager.sendBufferUpdate();
        changeTurnState(TurnState.PRODUCTION_RESOURCE_REMOVING);

    }

    //ANY

    public void anyRequirementResponse(ArrayList<Resource> resources, boolean isFromBuyDevelopment){
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        try {
            personalBoard.getResourceManager().convertAnyRequirement(resources, isFromBuyDevelopment);
        } catch (Exception e) {
            sendError(e.getMessage());
        }

    }

    public void anyProductionProfitResponse(ArrayList<Resource> resources){
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        try {
            personalBoard.getResourceManager().convertAnyProductionProfit(resources);
        } catch (Exception e) {
            sendError(e.getMessage());
        }
    }


    //WAREHOUSE

    private void controlBufferStatus(){
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        ResourceManager resourceManager = personalBoard.getResourceManager();
        CardManager cardManager = personalBoard.getCardManager();

        if (resourceManager.getBufferSize() != 0) return;

        switch (getTurnState()){
            case MARKET_RESOURCE_POSITIONING:
                resourceManager.applyFaithPoints();
                break;
            case BUY_DEV_RESOURCE_REMOVING:
                cardManager.emptyCardSlotBuffer();
                break;
            case PRODUCTION_RESOURCE_REMOVING:
                resourceManager.doProduction();
                resourceManager.applyFaithPoints();
                cardManager.restoreCM();
                break;

        }
        resourceManager.restoreRM();
        changeTurnState(TurnState.LEADER_MANAGE_AFTER);
    }



    public void subToStrongbox(Resource resource){
        ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getResourceManager();
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

    public void addToWarehouse(Resource resource, int index, boolean isNormalDepot) {
        ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getResourceManager();
        try {
            resourceManager.subToBuffer(resource);
        } catch (Exception e) {
            sendError(e.getMessage());
            return;
        }
        try {
            resourceManager.addToWarehouse(isNormalDepot, index, resource);
        } catch (Exception e) {
            resourceManager.addToBuffer(resource);
            sendError(e.getMessage());
        }
        controlBufferStatus();
    }

    public void subToWarehouse(Resource resource, int index, boolean isNormalDepot){
        ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getResourceManager();
        try {
            resourceManager.subToBuffer(resource);
        } catch (Exception e) {
            sendError(e.getMessage());
            return;
        }
        try {
            resourceManager.subToWarehouse(isNormalDepot, index, resource);
        } catch (Exception e) {
            resourceManager.addToBuffer(resource);
            sendError(e.getMessage());
        }
        controlBufferStatus();

    }


    public void switchDepots(int from, boolean isFromLeaderDepot, int to, boolean isToLeaderDepot){
        ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getResourceManager();
        try {
            resourceManager.switchResourceFromDepotToDepot(from, isFromLeaderDepot, to, isToLeaderDepot);
        } catch (Exception e) {
            sendError(e.getMessage());
        }
    }


    //SETUP

    private boolean hasFinishedLeaderSetUp(String username){
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(username);
        CardManager cardManager = personalBoard.getCardManager();
        return cardManager.getLeaders().size() == 2;
    }

    public void discardLeaderSetUp(int leaderIndex, String username){
        CardManager playerCardManager = gameMaster.getPlayerPersonalBoard(username).getCardManager();
        try {
            playerCardManager.discardLeaderSetUp(leaderIndex);
            if (hasFinishedLeaderSetUp(username)){
                FaithTrack playerFaithTrack = gameMaster.getPlayerPersonalBoard(username).getFaithTrack();
                switch (gameMaster.getPlayerPosition(username)){
                    case 0:
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

    //TODO ask: should only be active players due to disconnections?
    private boolean isFinishedSetup(){
        for(VirtualClient player : match.getAllPlayers()){
            if(player.getClient().getState()!=HandlerState.WAITING_TO_BE_IN_MATCH)
                return false;
        }
        return true;
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


}
