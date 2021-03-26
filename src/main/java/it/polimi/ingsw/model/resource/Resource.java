package it.polimi.ingsw.model.resource;

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

    public void setValue(int value) {
        this.value = value;
    }


    //manage type
    public ResourceType getType() {
        return type;
    }

}
