package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.CLIMessageHandler;
import it.polimi.ingsw.client.ClientMessageHandler;

public class PopeFavorActivated implements ClientMessage{

    private final int idVaticanReport;
    private final boolean discard;
    private final String username;


    @JsonCreator
    public PopeFavorActivated(@JsonProperty("idVaticanReport") int idVaticanReport,
                              @JsonProperty("discard") boolean discard,
                              @JsonProperty("username") String username) {

        this.idVaticanReport = idVaticanReport;
        this.discard = discard;
        this.username = username;
    }


    public int getIdVaticanReport() {
        return idVaticanReport;
    }

    public boolean isDiscard() {
        return discard;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        handler.popeFavorActivation(this);
    }
}
