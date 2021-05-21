package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.TurnState;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.serverMessage.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ServerMessageHandler class
 */
public class ServerMessageHandler {
    private Controller controller;
    private final Server server;
    private ClientConnectionHandler client;
    private VirtualClient virtualClient;
    private HandlerState setupState;

    /**
     * ServerMessageHandler constructor creates a new class instance
     * @param server is the reference to the server
     * @param client is the handler of the client socket
     */
    public ServerMessageHandler(Server server, ClientConnectionHandler client) {
        this.server = server;
        this.client = client;
        this.setupState = HandlerState.FIRST_CONTACT;
    }

    private boolean isSetUpPhaseFinished(){
        if (setupState != HandlerState.IN_MATCH){
            client.writeToStream(new ErrorMessage(ErrorType.ACTION_NOT_PERMITTED));
            return false;
        }
        return true;
    }

    private boolean isSinglePlayerGame(){
        if(!isSetUpPhaseFinished()) return false;

        int numOfPlayer = controller.getNumberOfPlayer();
        if (numOfPlayer == 1){
            client.writeToStream(new ErrorMessage(ErrorType.NOT_SINGLE_PLAYER_MODE));
        }
        return numOfPlayer == 1;
    }

    private boolean areYouAllowed(TurnState turnState){
        if(!isSetUpPhaseFinished()) return false;

        boolean result;
        if (getController().isPresent() && getVirtualClient().isPresent()){
            result = controller.getTurnState() == turnState;
        }else{
            result = false;
        }
        if (!result){
            client.writeToStream(new ErrorMessage(ErrorType.ACTION_NOT_PERMITTED));
        }
        return result;
    }

    private boolean areYouAllowed(TurnState[] turnStates){
        if(!isSetUpPhaseFinished()) return false;
        boolean result;
        if (getController().isPresent() && getVirtualClient().isPresent()){
            result = Arrays.stream(turnStates).anyMatch(x -> x == controller.getTurnState());
        }else{
            result = false;
        }
        if (!result){
            client.writeToStream(new ErrorMessage(ErrorType.ACTION_NOT_PERMITTED));
        }
        return result;
    }

    private boolean isYourTurn(){
        if(!isSetUpPhaseFinished()) return false;
        boolean result;
        if (getController().isPresent() && getVirtualClient().isPresent()){
            result = controller.isYourTurn(virtualClient.getUsername());
        }else{
            result = false;
        }
        if (!result){
            client.writeToStream(new ErrorMessage(ErrorType.NOT_YOUR_TURN));
        }
        return result;
    }

    private boolean controlSetUpPhase(HandlerState state){
        if(setupState != state){
            client.writeToStream(new ErrorMessage(ErrorType.ACTION_NOT_PERMITTED));
            return false;
        }
        return true;
    }

    private boolean controlAuthority(TurnState turnState){
        return isYourTurn() && areYouAllowed(turnState);
    }

    private boolean controlAuthority(TurnState[] turnStates){
        return isYourTurn() && areYouAllowed(turnStates);
    }


    public void setVirtualClient(VirtualClient virtualClient) {
        this.virtualClient = virtualClient;
    }

    public Optional<VirtualClient> getVirtualClient() {
        return Optional.ofNullable(virtualClient);
    }

    public void setClient(ClientConnectionHandler client) { this.client = client; }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Optional<Controller> getController() {
        return Optional.ofNullable(controller);
    }

    public void setSetupState(HandlerState setupState){
        this.setupState = setupState;
    }

    public HandlerState getSetupState() { return setupState; }

    public void handleConnectionMessage(ConnectionMessage message){
        System.out.println(message.getType() + ": " + message.getMessage());
    }

    public void handleFirstContact(){
        if(!controlSetUpPhase(HandlerState.FIRST_CONTACT)) return;
        server.putInLobby(client);
    }

    public void handleMatchCreation(ConnectionMessage message){
        if (!controlSetUpPhase(HandlerState.NUM_OF_PLAYER)) return;
        server.createMatch(message.getNum(),client);
    }

    public void handleUsernameInput(ConnectionMessage message){
        if (!controlSetUpPhase(HandlerState.USERNAME)) return;
        virtualClient.getMatch().setPlayerUsername(virtualClient, message.getMessage());
    }

    public void handleDisconnection(){
        getVirtualClient().ifPresentOrElse(virtualClient -> virtualClient.getMatch().playerDisconnection(virtualClient,true),
                ()->server.clientDisconnect(client));
    }

    public void handleGameQuit(){
        getVirtualClient().ifPresentOrElse(virtualClient -> virtualClient.getMatch().playerDisconnection(virtualClient,false),
                ()->server.clientDisconnect(client));

    }


    public void handleReconnection(ReconnectionMessage msg){
        server.clientReconnection(msg.getMatchID(), msg.getClientID(), client);
    }



    //UTIL
    public void handleEndTurn(){
        //TODO uncheck
        //if(!controlAuthority(TurnState.LEADER_MANAGE_AFTER)) return;
        controller.nextTurn();
    }



    //LEADER MANAGE
    public void handleLeaderManage(LeaderManage msg){
        if(setupState == HandlerState.LEADER_SETUP){
            controller.discardLeaderSetUp(msg.getIndex(), virtualClient.getUsername());
            return;
        }
        if(!controlAuthority(new TurnState[]{
                TurnState.LEADER_MANAGE_BEFORE, TurnState.LEADER_MANAGE_AFTER})){ return; }

        controller.leaderManage(msg.getIndex(), msg.isDiscard());
    }


    //MARKET
    public void handleMarketAction(MarketAction msg){
        if(!controlAuthority(TurnState.LEADER_MANAGE_BEFORE)) return;
        controller.marketAction(msg.getSelection(), msg.isRow());
    }

    public void handleWhiteMarbleConversion(WhiteMarbleConversionResponse msg){
        if(!controlAuthority(TurnState.WHITE_MARBLE_CONVERSION)) return;
        controller.leaderWhiteMarbleConversion(msg.getLeaderIndex(), msg.getNumOfWhiteMarble());
    }

    public void handleDiscardResourcesFromMarket(){
        if(!controlAuthority(TurnState.MARKET_RESOURCE_POSITIONING)) return;
        controller.clearBufferFromMarket();

    }

    //BUY DEVELOPMENT

    public void handleDevelopmentAction(DevelopmentAction msg){
        if(!controlAuthority(TurnState.LEADER_MANAGE_BEFORE)) return;
        controller.developmentAction(msg.getRow(), msg.getColumn(), msg.getLocateSlot());
    }

    //PRODUCTION

    /**
     * handleProduction method handle the normal production action
     * @param msg of type ProductionAction - the message that contains the information
     */
    public void handleProduction(ProductionAction msg){
        if(!controlAuthority(new TurnState[]{
                TurnState.LEADER_MANAGE_BEFORE, TurnState.PRODUCTION_ACTION})){ return; }

        if (msg.isLeader()){
            controller.leaderProductionAction(msg.getSlotsIndex());
        }else {
            controller.normalProductionAction(msg.getSlotsIndex());
        }
    }

    /**
     * handleBaseProduction method handle the base production action
     */
    public void handleBaseProduction(){
        if(!controlAuthority(new TurnState[]{
                TurnState.LEADER_MANAGE_BEFORE, TurnState.PRODUCTION_ACTION})){ return; }
        controller.baseProduction();
    }

    /**
     * handleEndCardSelection method handle the stop of production in order to continue with the
     * resources positioning
     */
    public void handleEndCardSelection(){
        if(!controlAuthority(TurnState.PRODUCTION_ACTION)) return;
        controller.stopProductionCardSelection();
    }

    //ANY
    /**
     * handleAnyResponse method handle the response of a specific any conversion request sent to the user
     * @param msg of type AnyResponse - the message that contains the information
     */
    public void handleAnyResponse(AnyResponse msg){
        if (msg.getResources() == null) return;
        ArrayList<Resource> resources = msg.getResources().stream()
                .map(x -> ResourceFactory.createResource(x.getType(), x.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));

        if(setupState == HandlerState.RESOURCE_SETUP){
            controller.insertSetUpResources(resources, virtualClient.getUsername());
            return;
        }

        if(!isYourTurn()){ return; }

        switch (controller.getTurnState()){
            case ANY_PRODUCE_COST_CONVERSION:
                controller.anyRequirementResponse(resources, false);
                break;
            case ANY_PRODUCE_PROFIT_CONVERSION:
                controller.anyProductionProfitResponse(resources);
                break;
            case ANY_BUY_DEV_CONVERSION:
                controller.anyRequirementResponse(resources, true);
                break;
            default:
                client.writeToStream(new ErrorMessage(ErrorType.ACTION_NOT_PERMITTED));
                break;
        }
    }


    //WAREHOUSE

    /**
     * handleStrongboxModify method handle the strongbox resources removing
     * @param msg of type StrongboxModify - the message that contains the information
     */
    public void handleStrongboxModify(StrongboxModify msg){
        if(!controlAuthority(new TurnState[]{
                TurnState.BUY_DEV_RESOURCE_REMOVING, TurnState.PRODUCTION_RESOURCE_REMOVING})){ return; }

        Resource resource = ResourceFactory.createResource(msg.getResource().getType(), msg.getResource().getValue());
        controller.subToStrongbox(resource);
    }

    /**
     * handleDepotModify method handle the depot resources insertion and deletion
     * @param msg of type DepotModify - the message that contains the information
     */
    public void handleDepotModify(DepotModify msg){
        if(!isYourTurn()) { return; }

        Resource resource = ResourceFactory.createResource(msg.getResource().getType(), msg.getResource().getValue());
        switch (controller.getTurnState()){
            case MARKET_RESOURCE_POSITIONING:
                controller.addToWarehouse(resource, msg.getDepotIndex(), msg.isNormalDepot());
                break;
            case BUY_DEV_RESOURCE_REMOVING:
            case PRODUCTION_RESOURCE_REMOVING:
                controller.subToWarehouse(resource, msg.getDepotIndex(), msg.isNormalDepot());
                break;
            default:
                client.writeToStream(new ErrorMessage(ErrorType.ACTION_NOT_PERMITTED));
                break;
        }
    }

    /**
     * handleSwitch method handle the depot switch
     * @param msg of type DepotSwitch - the message that contains the information
     */
    public void handleSwitch(DepotSwitch msg){
        if(!controlAuthority(new TurnState[]{
                TurnState.BUY_DEV_RESOURCE_REMOVING, TurnState.PRODUCTION_RESOURCE_REMOVING,
        TurnState.MARKET_RESOURCE_POSITIONING})){ return; }

        controller.switchDepots(msg.getFrom(), msg.isFromNormal(), msg.getTo(), msg.isToNormal());
    }




}
