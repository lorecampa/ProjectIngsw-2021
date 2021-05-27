package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.controller.Controller;
import java.util.HashMap;

public class ControllerHandler {
    HashMap<Views, Controller> controllers = new HashMap<>();
    private static ControllerHandler instance = new ControllerHandler();

    public static ControllerHandler getInstance(){
        return instance;
    }


    public Controller getController(Views view){
        return controllers.get(view);
    }

    public void changeStage(Views view){
        controllers.get(view).activate();
    }

    public void addController(Views view, Controller controller){
        controllers.put(view, controller);
    }

}
