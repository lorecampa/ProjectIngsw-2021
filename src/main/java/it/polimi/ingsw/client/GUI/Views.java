package it.polimi.ingsw.client.GUI;

public enum Views {
    MAIN_MENU("/GUI/mainMenu"),
    SETUP("/GUI/setup"),
    PRE_MATCH("/GUI/preGameSelection"),
    WAITING("/GUI/waiting"),
    MARKET("/GUI/market"),
    PERSONAL_BOARD("/GUI/personalBoard"),
    DECK_DEV("/GUI/deckDevelopment");

    private final String name;

    Views(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
