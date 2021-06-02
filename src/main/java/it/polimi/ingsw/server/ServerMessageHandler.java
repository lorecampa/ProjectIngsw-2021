package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.bothArchitectureMessage.ConnectionMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;
import it.polimi.ingsw.message.clientMessage.ErrorType;
import it.polimi.ingsw.message.bothArchitectureMessage.ReconnectionMessage;
import it.polimi.ingsw.message.serverMessage.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;

import java.util.ArrayList;
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
    private HandlerState serverPhase;
    /**
     * ServerMessageHandler constructor creates a new class instance
     * @param server is the reference to the server
     * @param client is the handler of the client socket
     */
    public ServerMessageHandler(Server server, ClientConnectionHandler client) {
        this.server = server;
        this.client = client;
        this.serverPhase = HandlerState.FIRST_CONTACT;
    }


    private boolean isServerPhaseCorrect(HandlerState state){
        if(serverPhase != state){
            client.writeToStream(new ErrorMessage(ErrorType.INVALID_ACTION));
            return false;
        }else{
            return state != HandlerState.IN_MATCH || controller.isYourTurn(virtualClient.getUsername());
        }
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

    public void setServerPhase(HandlerState serverPhase){
        this.serverPhase = serverPhase;
    }

    public HandlerState getServerPhase() { return serverPhase; }

    public void handleConnectionMessage(ConnectionMessage message){
        System.out.println(message.getType() + ": " + message.getMessage());
    }

    public void handleFirstContact(){
        if(!isServerPhaseCorrect(HandlerState.FIRST_CONTACT)) return;
        server.putInLobby(client);
    }

    public void handleMatchCreation(ConnectionMessage message){
        if (!isServerPhaseCorrect(HandlerState.NUM_OF_PLAYER)) return;
        server.createMatch(message.getNum(),client);
    }

    public void handleUsernameInput(ConnectionMessage message){
        if (!isServerPhaseCorrect(HandlerState.USERNAME)) return;
        virtualClient.getMatch().setPlayerUsername(virtualClient, message.getMessage());
    }

    public void handleDisconnection(){
        getVirtualClient().ifPresentOrElse(virtualClient -> virtualClient.getMatch().playerDisconnection(virtualClient),
                ()->server.clientDisconnect(client));
    }



    public void handleReconnection(ReconnectionMessage msg){
        server.clientReconnection(msg.getMatchID(), msg.getClientID(), client);
    }



    //UTIL
    public void handleEndTurn(){
        if(!isServerPhaseCorrect(HandlerState.IN_MATCH)) return;
        controller.nextTurn();
    }



    //LEADER MANAGE
    public void handleLeaderManage(LeaderManage msg){
        if(serverPhase == HandlerState.LEADER_SETUP){
            controller.discardLeaderSetUp(msg.getIndex(), virtualClient.getUsername());

        }else if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.leaderManage(msg.getIndex(), msg.isDiscard());
        }


    }


    //MARKET
    public void handleMarketAction(MarketAction msg){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.marketAction(msg.getSelection(), msg.isRow());
        }
    }

    public void handleWhiteMarbleConversion(WhiteMarbleConversionResponse msg){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.leaderWhiteMarbleConversion(msg.getLeaderIndex(), msg.getNumOfWhiteMarble());
        }
    }

    public void handleDiscardResourcesFromMarket(){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.clearBufferFromMarket();
        }
    }

    //BUY DEVELOPMENT

    public void handleDevelopmentAction(DevelopmentAction msg){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.developmentAction(msg.getRow(), msg.getColumn(), msg.getLocateSlot());
        }
    }

    //PRODUCTION

    /**
     * handleProduction method handle the normal production action
     * @param msg of type ProductionAction - the message that contains the information
     */
    public void handleProduction(ProductionAction msg){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            if (msg.isLeader()){
                controller.leaderProductionAction(msg.getSlotsIndex());
            }else {
                controller.normalProductionAction(msg.getSlotsIndex());
            }
        }
    }

    /**
     * handleBaseProduction method handle the base production action
     */
    public void handleBaseProduction(){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.baseProduction();
        }
    }

    /**
     * handleEndCardSelection method handle the stop of production in order to continue with the
     * resources positioning
     */
    public void handleEndCardSelection(){
        if(isServerPhaseCorrect(HandlerState.IN_MATCH)){
            controller.stopProductionCardSelection();
        }
    }

    //ANY
    /**
     * handleAnyResponse method handle the response of a specific any conversion request sent to the user
     * @param msg of type AnyResponse - the message that contains the information
     */
    public void handleAnyResponse(AnyResponse msg){
        if (msg.getResources() == null){
            client.writeToStream(new ErrorMessage(ErrorType.INVALID_ACTION));
            return;
        }
        ArrayList<Resource> resources = msg.getResources().stream()
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
     * handleStrongboxModify method handle the strongbox resources removing
     * @param msg of type StrongboxModify - the message that contains the information
     */
    public void handleStrongboxModify(StrongboxModify msg){
        if(!isServerPhaseCorrect(HandlerState.IN_MATCH) || msg.getResource() == null) return;

        Resource resource = ResourceFactory.createResource(msg.getResource().getType(), msg.getResource().getValue());
        controller.subToStrongbox(resource);
    }

    /**
     * handleDepotModify method handle the depot resources insertion and deletion
     * @param msg of type DepotModify - the message that contains the information
     */
    public void handleDepotModify(DepotModify msg){
        if(!isServerPhaseCorrect(HandlerState.IN_MATCH) || msg.getResource() == null) return;

        Resource resource = ResourceFactory.createResource(msg.getResource().getType(), msg.getResource().getValue());
        controller.depotModify(resource, msg.getDepotIndex(), msg.isNormalDepot());
    }

    /**
     * handleSwitch method handle the depot switch
     * @param msg of type DepotSwitch - the message that contains the information
     */
    public void handleSwitch(DepotSwitch msg){
        if(!isServerPhaseCorrect(HandlerState.IN_MATCH)) return;

        controller.switchDepots(msg.getFrom(), msg.isFromNormal(), msg.getTo(), msg.isToNormal());
    }




}
