package it.polimi.ingsw.controller;

import it.polimi.ingsw.message.clientMessage.*;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Effect.Activation.MarbleEffect;
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

public class Controller {
    private final GameMaster gameMaster;
    private Match match;

    public Controller(GameMaster gameMaster, Match match) {
        this.gameMaster = gameMaster;
        this.match = match;
        registerAllVirtualClientObserver();
    }

    public Controller(GameMaster gameMaster){
        this.gameMaster = gameMaster;
    }

    public void changeTurnState(TurnState state){
        gameMaster.onTurnStateChange(state);
    }

    public int getNumberOfPlayer(){
        return gameMaster.getNumberOfPlayer();
    }

    private void sendError(ErrorType errorType){
        //System.out.println("SingleErrorMessage: " + errorType);
        match.sendSinglePlayer(gameMaster.getCurrentPlayer(), new ErrorMessage(errorType));
    }
    private void sendError(String customMessage){
        //System.out.println("SingleErrorMessage: " + customMessage);
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


    public void nextTurn() {
        gameMaster.nextPlayer();
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        personalBoard.getResourceManager().newTurn();
        personalBoard.getCardManager().newTurn();
    }



    //LEADER MANAGING
    public void manageLeaderCard(int leaderIndex, boolean discard){
        CardManager cardManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getCardManager();
        try {
            if (discard){
                cardManager.discardLeader(leaderIndex);
            }else{
                cardManager.activateLeader(leaderIndex);
            }
        } catch (Exception e) {
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
                        .filter(x -> x.getOnActivationEffects().stream().anyMatch(effect -> effect instanceof MarbleEffect))
                        .mapToInt(x -> cardManager.getLeaders().indexOf(x))
                        .findFirst()
                        .ifPresent(x -> {
                            try {
                                cardManager.activateLeaderEffect(x, getTurnState());
                            } catch (Exception e) {
                                //it will never occur
                                sendError(e.getMessage());
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
        CardManager cardManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getCardManager();
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
            ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getResourceManager();
            resourceManager.resourceFromMarket(market.getResourceToSend());
            market.reset();
        }
    }


    //BUY DEVELOPMENT CARD ACTION
    public void developmentAction(int row, int col, int locateSlot){
        Development card;
        try{
            card = gameMaster.getDeckDevelopmentCard(row, col);
        }catch (Exception e){
            sendError(e.getMessage());
            return;
        }

        try{
            card.checkRequirements();
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
        }
    }

    public void undoBuyDev(){
        ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getResourceManager();
        gameMaster.onTurnStateChange(TurnState.LEADER_MANAGE_BEFORE);
        resourceManager.newTurn();
    }

    //PRODUCTION ACTION

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

    public void normalProductionAction(int cardSlot, boolean isBaseProduction){
        CardManager cardManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getCardManager();
        try {
            if(isBaseProduction)
                cardManager.baseProductionProduce();
            else
                cardManager.developmentProduce(cardSlot);

        } catch (Exception e) {
            sendError(e.getMessage());
        }
    }

    public void stopProductionCardSelection(){
        ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getResourceManager();
        resourceManager.doProduction();
    }

    //ANY MANAGING

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


    //WAREHOUSE MANAGING
    private void controlBufferStatus(){
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        ResourceManager resourceManager = personalBoard.getResourceManager();
        CardManager cardManager = personalBoard.getCardManager();

        if (resourceManager.getBufferSize() != 0) return;

        switch (getTurnState()){
            case BUY_DEV_RESOURCE_REMOVING:
                cardManager.emptyCardSlotBuffer();
                break;
            case PRODUCTION_RESOURCE_REMOVING:
                resourceManager.doProduction();
                break;

        }
        changeTurnState(TurnState.LEADER_MANAGE_AFTER);
    }

    private boolean hasFinishedLeaderSetUp(String username){
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(username);
        CardManager cardManager = personalBoard.getCardManager();
        return cardManager.getLeaders().size() == 2;
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

    public void discardLeaderSetUp(int leaderIndex, String username){
        CardManager playerCardManager = gameMaster.getPlayerPersonalBoard(username).getCardManager();
        try {
            playerCardManager.discardLeaderSetUp(leaderIndex);

            if (hasFinishedLeaderSetUp(username)){
                FaithTrack playerFaithTrack = gameMaster.getPlayerPersonalBoard(username).getFaithTrack();
                switch (gameMaster.getPlayerPosition(username)){
                    case 0: match.getPlayer(username).ifPresent(x->x.getClient().setState(HandlerState.IN_MATCH));
                            if(isFinishSetUp()){
                                match.sendAllPlayers(new MatchStart());
                                gameMaster.nextPlayer();
                            }
                            break;
                    case 1: match.sendSinglePlayer(username,new AnyConversionRequest(1));
                            match.getPlayer(username).ifPresent(x->x.getClient().setState(HandlerState.RESOURCE_SETUP));
                            break;
                    case 2: match.sendSinglePlayer(username,new AnyConversionRequest(1));
                            playerFaithTrack.movePlayer(1);
                            match.getPlayer(username).ifPresent(x->x.getClient().setState(HandlerState.RESOURCE_SETUP));
                            break;
                    case 3: match.sendSinglePlayer(username,new AnyConversionRequest(2));
                            playerFaithTrack.movePlayer(1);
                            match.getPlayer(username).ifPresent(x->x.getClient().setState(HandlerState.RESOURCE_SETUP));
                            break;
                }
            }
        }catch (Exception e) {
            sendErrorTo(e.getMessage(), username);
        }

    }

    public void leaderManage(int leaderIndex, boolean discard){
        CardManager cardManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getCardManager();
        try{
            if(discard)
                cardManager.discardLeader(leaderIndex);
            else
                cardManager.activateLeader(leaderIndex);
        }catch (Exception e){
            sendError(e.getMessage());
        }
    }

    public void insertSetUpResources(ArrayList<Resource> resources, String username){
        ResourceManager resourceManager = gameMaster.getPlayerPersonalBoard(username).getResourceManager();
        int size = resources.stream().mapToInt(Resource::getValue).sum();

        try{
            switch (gameMaster.getPlayerPosition(username)){
                case 1:
                case 2:
                    if (size == 1){
                        resourceManager.addToWarehouse(true, 0, resources.get(0));
                    }else{
                        sendErrorTo("Too many resources sent", username);
                    }
                    break;
                case 3:
                    if (size == 2 && resources.size() == 1){
                        resourceManager.addToWarehouse(true, 1, resources.get(0));
                    }else if(size == 2 && resources.size() == 2){
                        resourceManager.addToWarehouse(true, 0, resources.get(0));
                        resourceManager.addToWarehouse(true, 1, resources.get(1));
                    }else{
                        sendErrorTo("Too many resources sent", username);
                    }
                    break;
            }
            match.getPlayer(username).ifPresent(x->x.getClient().setState(HandlerState.IN_MATCH));
            if(isFinishSetUp()){
                match.sendAllPlayers(new MatchStart());
                gameMaster.nextPlayer();
            }
        }catch (Exception e){
            sendErrorTo(e.getMessage(), username);
        }

    }

    private boolean isFinishSetUp(){
        for(VirtualClient player : match.getAllPlayers()){
            if(player.getClient().getState()!=HandlerState.IN_MATCH)
                return false;
        }
        return true;
    }

}
