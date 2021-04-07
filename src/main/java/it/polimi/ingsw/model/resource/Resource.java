package it.polimi.ingsw.model.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.exception.NegativeResourceException;

import java.util.ArrayList;

public class Resource {
    private ResourceType type;
    private int value;

    @JsonCreator
    public Resource(@JsonProperty("type") ResourceType type,
                    @JsonProperty("value") int value) {
        this.value = value;
        this.type = type;
    }

    //manage value
    public int getValue() {
        return value;
    }
    /*
    public void setValue(int value) {
        this.value = value;
    }
    */
    public void addValue(int value) throws NegativeResourceException{
        if(this.value+value<0){
            throw new NegativeResourceException("Trying to underestimate resource value");}
        this.value+=value;
    }

    //manage type
    public ResourceType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null)
            return false;
        if(obj==this)
            return true;
        if(!(obj instanceof Resource))
            return false;
        return ((Resource) obj).getType() == type;
    }

}
