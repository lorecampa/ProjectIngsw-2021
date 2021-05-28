package it.polimi.ingsw.client.GUI;

public enum Views {
    MAIN_MENU("mainMenu"),
    SETUP("setup"),
    MARKET("market"),
    PERSONAL_BOARD("personalBoard");

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
