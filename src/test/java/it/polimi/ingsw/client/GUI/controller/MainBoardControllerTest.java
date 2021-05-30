package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.ModelClient;
import javafx.application.Platform;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class PersonalBoardControllerTest {

    @Test
    void personalBoardTest() {
        Client.getInstance().setMyName("davide");
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add("davide");
        Client.getInstance().setModels(usernames);
        ClientGUI.main(new String[0]);
    }
}