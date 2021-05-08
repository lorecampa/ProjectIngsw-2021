package it.polimi.ingsw.message.clientMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.ClientMessageHandler;
import it.polimi.ingsw.client.data.ResourceData;

import java.util.ArrayList;

public class WhiteMarbleConverted implements ClientMessage{
    private final ArrayList<ResourceData> whiteMarbleConversion;

    @JsonCreator
    public WhiteMarbleConverted(@JsonProperty("whiteMarbleConversion") ArrayList<ResourceData> whiteMarbleConversion) {
        this.whiteMarbleConversion = whiteMarbleConversion;
    }

    public ArrayList<ResourceData> getWhiteMarbleConversion() {
        return whiteMarbleConversion;
    }

    @Override
    public void process(ClientMessageHandler handler) {
        System.out.println("WhiteMarbleConvertedHandler");
    }
}
