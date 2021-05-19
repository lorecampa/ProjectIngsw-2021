package it.polimi.ingsw.message.serverMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.server.ServerMessageHandler;

public class QuitGame implements ServerMessage{
    private final boolean inGame;

    @JsonCreator
    public QuitGame(@JsonProperty("inGame") boolean inGame) {
        this.inGame = inGame;
    }

    public boolean isInGame() {
        return inGame;
    }

    @Override
    public void process(ServerMessageHandler handler) {
        if(inGame){
            handler.handleGameQuit();
        }else{
            handler.handleDisconnection();
        }
    }
}
