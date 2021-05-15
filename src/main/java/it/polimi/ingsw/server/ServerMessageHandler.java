package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.TurnState;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.serverMessage.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerMessageHandler {
    private Controller controller;
    private final Server server;
    private ClientConnectionHandler client;
    private VirtualClient virtualClient;
    private HandlerState state;

    public ServerMessageHandler(Server server, ClientConnectionHandler client) {
        this.server = server;
        this.client = client;
        this.state = HandlerState.FIRST_CONTACT;
    }


    private boolean areYouAllowed(TurnState turnState){
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

    public void setState(HandlerState state){
        this.state = state;
    }

    public HandlerState getState() { return state; }

    public void handleConnectionMessage(ConnectionMessage message){
        System.out.println(message.getType() + ": " + message.getMessage());
    }

    public void handleFirstContact(){
        if(state != HandlerState.FIRST_CONTACT) return;
        server.putInLobby(client);
    }

    public void handleMatchCreation(ConnectionMessage message){
        if (state != HandlerState.NUM_OF_PLAYER) return;
        server.createMatch(message.getNum(),client);
    }

    public void handleUsernameInput(ConnectionMessage message){
        if (state != HandlerState.USERNAME) return;
        virtualClient.getMatch().setPlayerUsername(virtualClient, message.getMessage());
    }

    public void handleDisconnection(){
        getVirtualClient().ifPresentOrElse(virtualClient -> virtualClient.getMatch().playerDisconnection(virtualClient),
                ()->server.clientDisconnect(client));
    }

    public void handleReconnection(ReconnectionMessage msg){
        server.clientReconnection(msg.getMatchID(), msg.getClientID(), client);
    }

    public void handleLeaderManage(LeaderManage msg){
        if(state == HandlerState.LEADER_SETUP){
            controller.discardLeaderSetUp(msg.getIndex(), virtualClient.getUsername());
            return;
        }
        if(!controlAuthority(TurnState.LEADER_MANAGE_BEFORE)) { return; }

        controller.leaderManage(msg.getIndex(), msg.isDiscard());

    }

    //SINGLE PLAYER
    public void handleSinglePlayer(){

    }

    //NEW METHODS

    public void handleMarketAction(MarketAction msg){
        if(!controlAuthority(TurnState.LEADER_MANAGE_BEFORE)) return;
        controller.marketAction(msg.getSelection(), msg.isRow());
    }
    
    public void handleWhiteMarbleConversion(WhiteMarbleConversionResponse msg){
        if(!controlAuthority(TurnState.WHITE_MARBLE_CONVERSION)) return;
        controller.leaderWhiteMarbleConversion(msg.getLeaderIndex(), msg.getNumOfWhiteMarble());
    }


    public void handleDevelopmentAction(DevelopmentAction msg){
        if(!controlAuthority(TurnState.LEADER_MANAGE_BEFORE)) return;
        controller.developmentAction(msg.getRow(), msg.getColumn(), msg.getLocateSlot());
    }



    public void handleProduction(ProductionAction msg){
        if(!controlAuthority(new TurnState[]{
                TurnState.LEADER_MANAGE_BEFORE, TurnState.PRODUCTION_ACTION})){ return; }
        if (msg.isLeader()){
            controller.leaderProductionAction(msg.getSlotsIndex());
        }else {
            controller.normalProductionAction(msg.getSlotsIndex(), msg.isBaseProduction());
        }
    }

    public void handleEndCardSelection(){
        if(!controlAuthority(TurnState.PRODUCTION_ACTION)) return;
        controller.stopProductionCardSelection();
    }

    public void handleAnyResponse(AnyResponse msg){
        if (msg.getResources() == null) return;

        ArrayList<Resource> resources = msg.getResources().stream()
                .map(x -> ResourceFactory.createResource(x.getType(), x.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));

        if(state == HandlerState.RESOURCE_SETUP){
            controller.insertSetUpResources(resources, virtualClient.getUsername());
            return;
        }
        if(state == HandlerState.IN_MATCH){
            return;
        }

        //crush if controller doesn't exist!
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

    public void handleEndTurn(){
        if(!controlAuthority(TurnState.LEADER_MANAGE_AFTER)) return;
        controller.nextTurn();
    }


    public void handleStrongboxModify(StrongboxModify msg){
        if(!controlAuthority(new TurnState[]{
                TurnState.BUY_DEV_RESOURCE_REMOVING, TurnState.PRODUCTION_RESOURCE_REMOVING})){ return; }

        Resource resource = ResourceFactory.createResource(msg.getResource().getType(), msg.getResource().getValue());
        controller.subToStrongbox(resource);
    }

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

    public void handleSwitch(DepotSwitch msg){
        if(!controlAuthority(new TurnState[]{
                TurnState.BUY_DEV_RESOURCE_REMOVING, TurnState.PRODUCTION_RESOURCE_REMOVING,
        TurnState.MARKET_RESOURCE_POSITIONING})){ return; }

        controller.switchDepots(msg.getFrom(), msg.isFromLeader(), msg.getTo(), msg.isToLeader());
    }




}
