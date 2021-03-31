package it.polimi.ingsw.model.card.creationEffect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @Type(value = DiscountEffect.class, name = "DiscountEffect"),
        @Type(value = WarehouseEffect.class, name = "WarehouseEffect")
})
public interface OnCreationEffect {
    void doCreationEffect();
    void attachResourceManager(ResourceManager resourceManager);
    boolean isUsed();

}

