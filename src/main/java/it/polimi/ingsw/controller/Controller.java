package it.polimi.ingsw.controller;

import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.card.Effect.Activation.MarbleEffect;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
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

    private void sendError(ErrorType errorType){
        System.out.println("SingleErrorMessage: " + errorType);
        //match.sendSinglePlayer(gameMaster.getCurrentPlayer(), new ErrorMessage(errorType));
    }
    private void sendError(String customMessage){
        System.out.println("SingleErrorMessage: " + customMessage);
        //match.sendSinglePlayer(gameMaster.getCurrentPlayer(), new ErrorMessage(customMessage));
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
        }

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
            System.out.println("WhiteMarbleConversionRequest sent to " + getCurrentPlayer());
            /*
            match.sendSinglePlayer(getCurrentPlayer(),
                    new WhiteMarbleConversionRequest(whiteMarbleDrew, cardManager.listOfMarbleEffect()));

             */
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
        changeTurnState(TurnState.PRODUCTION_RESOURCE_REMOVING);
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
            playerCardManager.discardLeaderNoNotify(leaderIndex);
        }catch (IndexOutOfBoundsException e) {
            sendError(e.getMessage());
        }
    }


}
