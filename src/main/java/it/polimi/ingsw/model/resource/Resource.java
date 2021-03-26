package it.polimi.ingsw.model.resource;

//first class written <3
public class Resource {
    private int value;
    private String type;


    public Resource( String type, int value) {
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
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
