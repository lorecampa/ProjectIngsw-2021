package it.polimi.ingsw.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "@class")

@JsonSubTypes({
        @JsonSubTypes.Type(value = CommandMessage.class, name = "Command"),
        @JsonSubTypes.Type(value = NormalMessage.class, name = "Normal")
})
public interface  Message {
    public String print();
}
