package it.polimi.ingsw.model.card.requirement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.personalBoard.cardManager.CardManager;
import it.polimi.ingsw.model.personalBoard.resourceManager.ResourceManager;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")

@JsonSubTypes({
        @Type(value = CardReq.class, name = "CardReq"),
        @Type(value = ResourceReq.class, name = "ResourceReq")
})

public interface Requirement {
    boolean checkRequirement();
    void attachResourceManager(ResourceManager resourceManager);
    void attachCardManager(CardManager cardManager);
}
