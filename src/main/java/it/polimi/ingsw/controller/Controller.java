package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.model.GameMaster;
import it.polimi.ingsw.model.card.Development;
import it.polimi.ingsw.model.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.server.Match;
import it.polimi.ingsw.server.VirtualClient;

import java.util.ArrayList;

public class Controller {
    private GameMaster gameMaster;
    private Match match;

    public Controller(GameMaster gameMaster, Match match) {
        this.gameMaster = gameMaster;
        this.match = match;
    }

    public Controller(GameMaster gameMaster){
        this.gameMaster = gameMaster;
    }

    private void sendError(ErrorType errorType){
        match.sendSinglePlayer(gameMaster.getCurrentPlayer(), new ErrorMessage(errorType));
    }
    private void sendError(String customMessage){
        match.sendSinglePlayer(gameMaster.getCurrentPlayer(), new ErrorMessage(customMessage));
    }


    public TurnState getTurnState(){
        return gameMaster.getTurnState();
    };

    public boolean isCorrectTurnState(String username, TurnState turnState){
        return isYourTurn(username) && getTurnState() == turnState;
    }

    public boolean isYourTurn(String username){
        return getCurrentPlayer().equals(username);
    }


    private String getCurrentPlayer(){
        return gameMaster.getCurrentPlayer();
    };


    public void registerToAllObservable(VirtualClient virtualClient){
        //model observer
        gameMaster.attachObserver(virtualClient);

        //resource manager observer
        gameMaster.getPlayerPersonalBoard(virtualClient.getUsername())
                .getResourceManager().attachObserver(virtualClient);

        //faith track observer
        gameMaster.getPlayerPersonalBoard(virtualClient.getUsername())
                .getFaithTrack().attachObserver(virtualClient);

        //card manager
        gameMaster.getPlayerPersonalBoard(virtualClient.getUsername())
                .getCardManager().attachObserver(virtualClient);
    }


    public void nextTurn() {
        //TODO check if the next player is active (maybe)
        gameMaster.nextPlayer();
    }


    public void addResourceToStrongbox(Resource res, String username){
        gameMaster.getPlayerPersonalBoard(username)
                .getResourceManager()
                .addToStrongbox(res);
    }

    public void addResourceToWarehouse(Resource res, int index, String username) throws InvalidOrganizationWarehouseException, TooMuchResourceDepotException {

        gameMaster.getPlayerPersonalBoard(username)
                .getResourceManager().addToWarehouse(true, index, res);
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


        if (market.getWhiteMarbleDrew() == 0 || !cardManager.doIHaveMarbleEffects()){
            ResourceManager resourceManager = personalBoard.getResourceManager();
            //bufferInsertion
            resourceManager.resourceFromMarket(market.getResourceToSend());
            market.reset();
        }
    }

    public void leaderWhiteMarbleConversion(int leaderIndex, int numOfWhiteMarble){
        Market market = gameMaster.getMarket();
        market.setWhiteMarbleToTransform(numOfWhiteMarble);

        CardManager cardManager = gameMaster.getPlayerPersonalBoard(getCurrentPlayer()).getCardManager();
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


    //DEVELOP
    public void developmentAction(int row, int col, int locateSlot) throws DeckDevelopmentCardException {
        Development card;
        try{
            card = gameMaster.popDeckDevelopmentCard(row, col);
        }catch (Exception e){
            sendError(e.getMessage());
            return;
        }
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        card.attachCardToUser(personalBoard, gameMaster.getMarket());
        if (!card.checkRequirements()){
            sendError(ErrorType.NOT_ENOUGH_REQUIREMENT);
            return;
        }
        personalBoard.getCardManager().setBufferBuyCard(card);

    }



    //ANY MANAGING

    public void anyReqManage(ArrayList<Resource> resources, boolean isBuyDev){
        int numResponse = resources.stream().mapToInt(Resource::getValue).sum();
        PersonalBoard personalBoard = gameMaster.getPlayerPersonalBoard(getCurrentPlayer());
        int anyToConvert = personalBoard.getResourceManager().getAnyRequired();
        if (anyToConvert < numResponse){
            sendError(ErrorType.INVALID_NUM_ANY_CONVERSION);
            return;
        }
        try {
            personalBoard.getResourceManager().convertAnyRequirement(resources, isBuyDev);
        } catch (Exception e) {
            sendError("Wrong conversion parameters");
        }


    }

    public void anyProductionCostManage(ArrayList<Resource> resources){

    }



    public void anyProductionProfitManage(ArrayList<Resource> resources){

    }



}
