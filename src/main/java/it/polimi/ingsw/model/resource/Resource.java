package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.exception.NegativeResourceException;

import java.util.ArrayList;

//first class written <3
public class Resource {
    private int value;
    private ResourceType type;

    public Resource( ResourceType type, int value) {
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
        if(this.value+value<0)
            throw new NegativeResourceException("Trying to understimate resource value");
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
