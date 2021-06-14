package it.polimi.ingsw.model.resource;

/**
 * Types of resources
 */
public enum ResourceType {
    COIN("Coin"),
    SHIELD("Shield"),
    STONE("Stone"),
    SERVANT("Servant"),
    FAITH("Faith"),
    ANY("Any");

    private final String displayName;

    /**
     * Create a ResourceType with a specific display name.
     * @param displayName the string that describe the type.
     */
    ResourceType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Return the display name.
     * @return the display name.
     */
    public String getDisplayName() {
        return displayName;
    }
}
