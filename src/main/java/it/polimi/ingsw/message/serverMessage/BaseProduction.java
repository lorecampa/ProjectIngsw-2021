package it.polimi.ingsw.message.serverMessage;

import it.polimi.ingsw.server.ServerMessageHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class BaseProduction implements ServerMessage{
    @Override
    public void process(ServerMessageHandler handler) {
        handler.handleBaseProduction();

        handler.getVirtualClient().ifPresent(x->x.addToLog(this));
    }
    @Override
    public String toString() {
        return " - Base Production";
    }

}
