package it.polimi.ingsw.model.card.Effect.Activation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.EffectData;
import it.polimi.ingsw.client.data.EffectType;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.model.PlayerState;
import it.polimi.ingsw.model.card.Effect.Effect;
import it.polimi.ingsw.model.personalBoard.market.Market;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceFactory;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * MarbleEffect class defines the effect that concern marbles.
 */
public class MarbleEffect implements Effect {
    @JsonIgnore
    private Market market = null;

    private final ArrayList<Resource> transformIn;

    /**
     * Construct a marble effect with specific resources to transform each white marble.
     * @param transformIn the resources that each white marble drew will be transform into.
     */
    @JsonCreator
    public MarbleEffect(@JsonProperty("transformIn") ArrayList<Resource> transformIn) {
        this.transformIn = transformIn;
    }

    /**
     * Return the resources that each white marble drew will be transform into.
     * @return the resources that each white marble drew will be transform into.
     */
    public ArrayList<Resource> getTransformIn() {
        return transformIn;
    }

    /**
     * Pass all the resources to the market based on how many white marble the user haw drawn.
     * @param playerState the state of the turn, in this case must be MARKET_STATE.
     */
    @Override
    public void doEffect(PlayerState playerState) {
        if (playerState == PlayerState.WHITE_MARBLE_CONVERSION){
            int whiteMarble = market.getWhiteMarbleToTransform();
            market.insertLeaderResources(transformIn.stream()
                    .map(x -> ResourceFactory.createResource(x.getType(), x.getValue()*whiteMarble))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
    }

    /**
     * See {@link Effect#attachMarket(Market)}.
     */
    @Override
    public void attachMarket(Market market) {
        this.market = market;
    }

    /**
     * See {@link Effect#attachResourceManager(ResourceManager)}.
     */
    @Override
    public void attachResourceManager(ResourceManager resourceManager) {}

    /**
     * See {@link Effect#toEffectData()}.
     */
    @Override
    public EffectData toEffectData() {
        String description = "Marble effect: ";
        ArrayList<ResourceData> whiteMarble = new ArrayList<>();
        whiteMarble.add(new ResourceData(ResourceType.ANY));

        ArrayList<ResourceData> transformInto = transformIn.stream().map(Resource::toClient).collect(Collectors.toCollection(ArrayList::new));
        return new EffectData(EffectType.MARBLE,description,whiteMarble,transformInto);
    }
}
