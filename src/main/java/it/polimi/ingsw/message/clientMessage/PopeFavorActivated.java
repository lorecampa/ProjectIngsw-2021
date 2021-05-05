package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;

public class PopeFavorActivated implements ClientMessage{
    private final int idVaticanReport;
    private final boolean isDiscard;
    private final String username;


    @JsonCreator

    public PopeFavorActivated(@JsonProperty("idVaticanReport") int idVaticanReport,
                              @JsonProperty("isDiscard") boolean isDiscard,
                              @JsonProperty("username") String username) {

        this.idVaticanReport = idVaticanReport;
        this.isDiscard = isDiscard;
        this.username = username;
    }


    public int getIdVaticanReport() {
        return idVaticanReport;
    }

    public boolean isDiscard() {
        return isDiscard;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("PopeFavorActivated Handler");
    }
}
