package it.polimi.ingsw.message.clientMessage;

import it.polimi.ingsw.message.ClientMessageHandler;
import it.polimi.ingsw.message.Message;



public interface ClientMessage extends Message{
    void process (ClientMessageHandler handler);
}
