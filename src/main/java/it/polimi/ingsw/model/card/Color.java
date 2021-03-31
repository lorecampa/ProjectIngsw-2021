package it.polimi.ingsw.model.card;

public enum Color {
    GREEN("Green"),
    BLUE("Blue"),
    YELLOW("Yellow"),
    PURPLE("Purple");

    private String displayName;

    Color(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
