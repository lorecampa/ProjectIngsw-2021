package it.polimi.ingsw.model.card.Effect.Activation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.card.Effect.State;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import java.util.ArrayList;


/**
 * MarbleEffect class defines the effect that concern the marbles
 */
public class MarbleEffect implements Effect {
    private final ArrayList<Resource> transformIn;
    private Market market = null;

    /**
     * Constructor MarbleEffect creates a new MarbleEffect instance
     * @param transformIn of type ArrayList - the resources which we will transform each with marble drew
     */
    @JsonCreator
    public MarbleEffect(@JsonProperty("transformIn") ArrayList<Resource> transformIn) {
        this.transformIn = transformIn;
    }

    /**
     * Method doEffect is in charge of pass all the resources to
     * the market based on how many white marble the user haw drawn
     * @param state of type State - defines the state of the turn
     */
    @Override
    public void doEffect(State state) {
        if (state == State.MARKET_STATE){
            int whiteMarble = market.getWhiteMarbleDrew();
            //add resource in market
            for (Resource res: transformIn){
                market.addInResourcesToSend(ResourceFactory.createResource(res.getType(), res.getValue()*whiteMarble));

            }
        }

    }

    /**
     * Method attachMarket attach the market
     * @param market of type Market is the instance of the market of the game
     */
    @Override
    public void attachMarket(Market market) {
        this.market = market;
    }

    /**
     * Method attachResourceManager does nothing because  MarbleEffect doesn't need
     * any reference to it
     * @param resourceManager of type ResourceManager is an instance of the resource manager of the player
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {}



    @Override
    public String toString() {
        String x = "\ntrasformIn= ";
        for(Resource res: transformIn){
            x += "{"+ res.getType().getDisplayName()+", "+res.getValue()+"}  ";
        }
        return x;
    }

}
