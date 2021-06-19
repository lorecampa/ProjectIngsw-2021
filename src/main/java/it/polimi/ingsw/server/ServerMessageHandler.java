package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.serverMessage.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Manage all the messages that arrive at the Server.
 */
public class ServerMessageHandler {
    private Controller controller;
    private final Server server;
    private ClientConnectionHandler client;
    private VirtualClient virtualClient;
    private HandlerState serverPhase;
    /**
     * Construct a ServerMessageHandler of a specific client.
     * @param server is the reference to the server
     * @param client is the handler of the client socket
     */
    public ServerMessageHandler(Server server, ClientConnectionHandler client) {
        this.server = server;
        this.client = client;
        this.serverPhase = HandlerState.FIRST_CONTACT;
    }

    /**
     * Return true if the state of the handler is equal to state and not equal to IN_MATCH or is the turn of the player.
     * @param state the state to check.
     * @return true if the state of the handler is equal to state and not equal to IN_MATCH or is the turn of the player.
     */
    private boolean isServerPhaseCorrect(HandlerState state){
        if(serverPhase != state){
            client.writeToStream(new ErrorMessage(ErrorType.INVALID_ACTION));
            return false;
        }else{
            return state != HandlerState.IN_MATCH || controller.isYourTurn(virtualClient.getUsername());
        }
    }

    /**
     * Set the virtual client.
     * @param virtualClient the new virtual client.
     */
    public void setVirtualClient(VirtualClient virtualClient) {
        this.virtualClient = virtualClient;
    }

    /**
     * Return the virtual client.
     * @return the virtual client.
     */
    public Optional<VirtualClient> getVirtualClient() {
        return Optional.ofNullable(virtualClient);
    }

    /**
     * Set the client connection handler.
     * @param client the new client connection handler.
     */
    public void setClient(ClientConnectionHandler client) { this.client = client; }

    /**
     * Set the controller.
     * @param controller the new controller.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Return the controller.
     * @return the controller.
     */
    public Optional<Controller> getController() {
        return Optional.ofNullable(controller);
    }

    /**
     * Set the handler state.
     * @param serverPhase the new handler state.
     */
    public void setServerPhase(HandlerState serverPhase){
        this.serverPhase = serverPhase;
    }

    /**
     * Return the handler state.
     * @return the handler state.
     */
    public HandlerState getServerPhase() { return serverPhase; }

    /**
     * Handle the Connection Message by printing his message.
     * @param message the Connection Message to handle.
     */
    public void handleConnectionMessage(ConnectionMessage message){
        System.out.println(message.getType() + ": " + message.getMessage());
    }

    /**
     * Handle the first contact between the client and the server.
     */
    public void handleFirstContact(){
        if(!isServerPhaseCorrect(HandlerState.FIRST_CONTACT)) return;
        server.putInLobby(client);
    }

    /**
     * Handle the request for a single player match.
     */
    public void handleSinglePlayer(){
        if(!isServerPhaseCorrect(HandlerState.FIRST_CONTACT)) return;
        server.singlePlayer(client);
    }

    /**
     * Handle the match creation request of a set number of player.
     * @param message the Message with the number of player.
     */
    public void handleMatchCreation(ConnectionMessage message){
        if (!isServerPhaseCorrect(HandlerState.NUM_OF_PLAYER)) return;
        try {server.createMatch(message.getNum(),client);}
        catch (InvalidParameterException exception){
            client.writeToStream(new ErrorMessage(exception.getMessage()));
        }
    }

    /**
     * Handle the set of a player username request.
     * @param message the message with the player username.
     */
    public void handleUsernameInput(ConnectionMessage message){
        if (!isServerPhaseCorrect(HandlerState.USERNAME)) return;
        virtualClient.getMatch().setPlayerUsername(virtualClient, message.getMessage());
    }

    /**
     * Handle the disconnection of a player.
     */
    public void handleDisconnection(){
        getVirtualClient().ifPresentOrElse(virtualClient -> virtualClient.getMatch().playerDisconnection(virtualClient),
                ()->server.clientDisconnect(client));
    }

    /**
     * Handle the reconnection of a player.
     * @param message the reconnection message with all the information needed.
     */
    public void handleReconnection(ReconnectionMessage message){
        server.clientReconnection(message.getMatchID(), message.getClientID(), client);
    }

    //UTIL

    /**
     * Handle the end of a turn
     */
    public void handleEndTurn(){
        if(!isServerPhaseCorrect(HandlerState.IN_MATCH)) return;
        controller.nextTurn();
    }

    //LEADER MANAGE

    /**
     * Handle the request of a leader discard or activation.
     * @param message the message with the request information.
     */
    public void handleLeaderManage(LeaderManage message){
        if(serverPhase == HandlerState.LEADER_SETUP){
            controller.discardLeaderSetUp(message.getIndex(), virtualClient.getUsername());

        }else if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.leaderManage(message.getIndex(), message.isDiscard());
        }
    }

    //MARKET

    /**
     * Handle the request of a Market action.
     * @param message the message with the action information.
     */
    public void handleMarketAction(MarketAction message){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.marketAction(message.getSelection(), message.isRow());
        }
    }

    /**
     * Handle the request of conversion of white marble by leaders.
     * @param message the message with the conversion information.
     */
    public void handleWhiteMarbleConversion(WhiteMarbleConversionResponse message){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.leaderWhiteMarbleConversion(message.getLeaderIndex(), message.getNumOfWhiteMarble());
        }
    }

    /**
     * Handle the request to discard the resources from from the market.
     */
    public void handleDiscardResourcesFromMarket(){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.clearBufferFromMarket();
        }
    }

    //BUY DEVELOPMENT

    /**
     * Handle the request to buy a card.
     * @param message the message with all the action information.
     */
    public void handleDevelopmentAction(DevelopmentAction message){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.developmentAction(message.getRow(), message.getColumn(), message.getLocateSlot());
        }
    }

    //PRODUCTION

    /**
     * Handle the normal production action.
     * @param message the message that contains the information.
     */
    public void handleProduction(ProductionAction message){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            if (message.isLeader()){
                controller.leaderProductionAction(message.getSlotsIndex());
            }else {
                controller.normalProductionAction(message.getSlotsIndex());
            }
        }
    }

    /**
     * Handle the base production action.
     */
    public void handleBaseProduction(){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.baseProduction();
        }
    }

    /**
     * Handle the stop of production in order to continue with the resources positioning
     */
    public void handleEndCardSelection(){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.stopProductionCardSelection();
        }
    }

    //ANY
    /**
     * Handle the response of a specific any conversion request sent to the user.
     * @param message the message that contains the information.
     */
    public void handleAnyResponse(AnyResponse message){
        if (message.getResources() == null){
            client.writeToStream(new ErrorMessage(ErrorType.INVALID_ACTION));
            return;
        }
        ArrayList<Resource> resources = message.getResources().stream()
                .map(x -> ResourceFactory.createResource(x.getType(), x.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));


        if(serverPhase == HandlerState.RESOURCE_SETUP){
            controller.insertSetUpResources(resources, virtualClient.getUsername());
        }else if (isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.anyConversion(resources);
        }
    }

    //WAREHOUSE

    /**
     * Handle the strongbox resources removing.
     * @param message the message that contains the information.
     */
    public void handleStrongboxModify(StrongboxModify message){
        if(!isServerPhaseCorrect(HandlerState.IN_MATCH) || message.getResource() == null) return;

        Resource resource = ResourceFactory.createResource(message.getResource().getType(), message.getResource().getValue());
        controller.subToStrongbox(resource);
    }

    /**
     * Handle the depot resources insertion and deletion.
     * @param message the message that contains the information.
     */
    public void handleDepotModify(DepotModify message){
        if(!isServerPhaseCorrect(HandlerState.IN_MATCH) || message.getResource() == null) return;

        Resource resource = ResourceFactory.createResource(message.getResource().getType(), message.getResource().getValue());
        controller.depotModify(resource, message.getDepotIndex(), message.isNormalDepot());
    }

    /**
     * Handle the depot switch.
     * @param message the message that contains the information.
     */
    public void handleSwitch(DepotSwitch message){
        if(!isServerPhaseCorrect(HandlerState.IN_MATCH)) return;

        controller.switchDepots(message.getFrom(), message.isFromNormal(), message.getTo(), message.isToNormal());
    }
}
