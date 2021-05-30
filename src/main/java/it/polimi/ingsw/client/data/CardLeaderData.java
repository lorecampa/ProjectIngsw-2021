package it.polimi.ingsw.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.PrintAssistant;
import java.net.URL;
import java.util.ArrayList;

public class CardLeaderData {
    private final int id;
    private final int victoryPoint;
    private final ArrayList<CardDevData> cardReq;
    private final ArrayList<ResourceData> resourceReq;

    private final ArrayList<EffectData> effects;

    private boolean active;

    @JsonCreator(mode= JsonCreator.Mode.PROPERTIES)
    public CardLeaderData(@JsonProperty("id") int id,
                          @JsonProperty("victoryPoint")int victoryPoint,
                          @JsonProperty("cardReq")ArrayList<CardDevData> cardReq,
                          @JsonProperty("resourceReq")ArrayList<ResourceData> resourceReq,
                          @JsonProperty("effects")ArrayList<EffectData> effects,
                          @JsonProperty("active")boolean active) {
        this.id = id;
        this.victoryPoint = victoryPoint;
        this.cardReq = cardReq;
        this.resourceReq = resourceReq;
        this.effects = effects;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public String toResourcePath(){
        URL url = null;
        if (active)
            url = this.getClass().getResource("/GUI/front/"+id+".png");
        else {
            url = this.getClass().getResource("/GUI/front/" + id + "-BN.png");
        }
        assert url != null;
        return url.toString();
    }

    public int getVictoryPoint() {
        return victoryPoint;
    }

    public ArrayList<CardDevData> getCardReq() {
        return cardReq;
    }

    public ArrayList<ResourceData> getResourceReq() {
        return resourceReq;
    }

    public ArrayList<EffectData> getEffects() {
        return effects;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String leaderHeader(int width,int index,int size){
        String row = PrintAssistant.instance.stringBetweenChar((index+1)+") LEADER" +" +" + victoryPoint +"VP", ' ', width - 2, ' ', ' ');
        row = (active? PrintAssistant.ANSI_GREEN_BACKGROUND:PrintAssistant.ANSI_WHITE_BACKGROUND)+PrintAssistant.ANSI_BLACK + row +" "+ PrintAssistant.ANSI_RESET+" " ;
        if(index == 1 || index == size-1){
            row = row.concat(PrintAssistant.ANSI_BLACK + "|" + PrintAssistant.ANSI_RESET);
        }
        return row;
    }

    public String leaderEnd(int width,int index,int size){
        StringBuilder row = new StringBuilder(PrintAssistant.instance.stringBetweenChar("END CARD", ' ', width - 2, ' ', ' '));
        row = new StringBuilder((active ? PrintAssistant.ANSI_GREEN_BACKGROUND : PrintAssistant.ANSI_WHITE_BACKGROUND) + PrintAssistant.ANSI_BLACK + row + " " + PrintAssistant.ANSI_RESET + " ");
        if(index == 1 || index == size-1){
            row.append(PrintAssistant.ANSI_BLACK + "|" + PrintAssistant.ANSI_RESET);
        }
        return row.toString();
    }

    public String leaderCardReq(int width, int index, int size){
        StringBuilder row = new StringBuilder();
        for(CardDevData d : cardReq){
            row.append(d.toCLIForLeader());
        }
        row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), width, ' ', ' ', ' '));

        if(index == 1 || index == size-1){
            row.append(PrintAssistant.ANSI_BLACK + "|" + PrintAssistant.ANSI_RESET);
        }
        return row.toString();
    }

    public String leaderResReq(int width, int index, int size){
        StringBuilder row = new StringBuilder();
        for(ResourceData r : resourceReq){
            row.append(r.toCli());
        }
        row = new StringBuilder(PrintAssistant.instance.fitToWidth(row.toString(), width, ' ', ' ', ' '));
        if(index == 1 || index == size-1){
            row.append(PrintAssistant.ANSI_BLACK + "|" + PrintAssistant.ANSI_RESET);
        }
        return row.toString();
    }

    public int leaderEffect(int width, int index, int size, ArrayList<String> rowsOfLeaders, int writtenRow){
        for(EffectData e : effects){
            StringBuilder row = new StringBuilder(PrintAssistant.instance.fitToWidth(e.toString(), width, ' ', ' ', ' '));
            if(index == 1 || index == size - 1 ){
                row.append(PrintAssistant.ANSI_BLACK + "|" + PrintAssistant.ANSI_RESET);
            }
            rowsOfLeaders.set(writtenRow, rowsOfLeaders.get(writtenRow)+row);
            writtenRow++;
        }
        return writtenRow;
    }
}
