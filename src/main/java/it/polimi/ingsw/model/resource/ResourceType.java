package it.polimi.ingsw.model.resource;


public enum ResourceType {
    COIN("Coin"),
    SHIELD("Shield"),
    STONE("Stone"),
    SERVANT("Servant"),
    FAITH("Faith"),
    ANY("Any");

    private final String displayName;

    ResourceType(String displayName) {
        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }
}
