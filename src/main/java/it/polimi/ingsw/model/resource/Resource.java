package it.polimi.ingsw.model.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NegativeResourceException;

public class Resource {
    private final ResourceType type;
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

    public void setValueToZero() {
        this.value = 0;
    }

    public void subValue(int value) throws NegativeResourceException {
        int valueAbs = Math.abs(value);
        if (this.value - valueAbs < 0){
            throw new NegativeResourceException("Trying to underestimate resource value");
        }
        this.value -= valueAbs;

    }


    public void addValue(int value){
        this.value += Math.abs(value);
    }

    //manage type
    public ResourceType getType() {
        return type;
    }

    public ResourceData toClient(){
        return new ResourceData(type, value);
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

    @Override
    public String toString() {
        return type+": "+value;
    }
}
