package it.polimi.ingsw.message;

import it.polimi.ingsw.message.clientMessage.ActivateProductionMessage;
import it.polimi.ingsw.message.clientMessage.BuyDevelopmentCardMessage;
import it.polimi.ingsw.message.clientMessage.ErrorMessage;

public class ClientMessageHandler extends Handler {

    public ClientMessageHandler() {}

    public void handleBuyDevelopmentCard(BuyDevelopmentCardMessage message){
        System.out.println("Row: " + message.getRow());
        System.out.println("Column: " + message.getColumn());
    }

    public void handleActivateProductionMessage(ActivateProductionMessage message){
        System.out.println("CardSlot: " + message.getCardSlot());
        System.out.println("CardIndex: " + message.getIndexCard());
    }


    public void handleErrorMessage(ErrorMessage message){
        System.out.println("ErrorMessage: " + message.getMessage());
    }
}
