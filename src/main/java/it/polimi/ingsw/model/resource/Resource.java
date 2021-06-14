package it.polimi.ingsw.model.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.client.data.ResourceData;
import it.polimi.ingsw.exception.NegativeResourceException;

/**
 * Resource define the a resource entity.
 */
public class Resource {
    private final ResourceType type;
    private int value;

    /**
     * Construct a resource of a specific value and type.
     * @param type the type of the resource.
     * @param value the value of the resource.
     */
    @JsonCreator
    public Resource(@JsonProperty("type") ResourceType type,
                    @JsonProperty("value") int value) {
        this.value = value;
        this.type = type;
    }

    /**
     * Return the value of the resource.
     * @return the value of the resource.
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value of the resource to 0.
     */
    public void setValueToZero() {
        this.value = 0;
    }

    /**
     * Subtract value to the resource value.
     * @param value the value to subtract.
     * @throws NegativeResourceException if the resource value will be < 0.
     */
    public void subValue(int value) throws NegativeResourceException {
        int valueAbs = Math.abs(value);
        if (this.value - valueAbs < 0){
            throw new NegativeResourceException("Trying to underestimate resource value");
        }
        this.value -= valueAbs;
    }

    /**
     * Add value to the resource value.
     * @param value the value to add.
     */
    public void addValue(int value){
        this.value += Math.abs(value);
    }

    /**
     * Return the type of the resource.
     * @return the type of the resource.
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Return a Resource Data based on the resource.
     * @return a Resource Data based on the resource.
     */
    public ResourceData toClient(){
        return new ResourceData(type, value);
    }


    /**
     * Return true if obj is equal to the resource, two resources are equal if they are of the same type.
     * @param obj the obj to compare to the resource.
     * @return true if obj is equal to the resource, two resources are equal if they are of the same type.
     */
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

    /**
     * Return a string that describe the resource.
     * @return a string that describe the resource.
     */
    @Override
    public String toString() {
        return type+": "+value;
    }
}
