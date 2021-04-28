package it.polimi.ingsw.message;


import it.polimi.ingsw.message.serverMessage.ActivateProductionMessage;
import it.polimi.ingsw.message.serverMessage.BuyDevelopmentCardMessage;
import it.polimi.ingsw.message.serverMessage.ErrorMessage;

public class ServerMessageHandler {
    public ServerMessageHandler() {}

    public void handleBuyDevelopmentCard(BuyDevelopmentCardMessage message){
        System.out.println("[Row: " + message.getRow() + " Column: " + message.getColumn() + "]");
    }

    public void handleActivateProductionMessage(ActivateProductionMessage message){
        System.out.println("[CardSlot: " + message.getCardSlot() + " CardIndex: " + message.getIndexCard() + "]");
    }


    public void handleErrorMessage(ErrorMessage message){
        System.out.println("[ErrorMessage: " + message.getMessage() + "]");
    }
}
