package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.controller.Controller;
import java.util.HashMap;

public class ControllerHandler {
    HashMap<Views, Controller> controllers = new HashMap<>();
    private Views currentView = null;
    private static ControllerHandler instance = new ControllerHandler();

    public static ControllerHandler getInstance(){
        return instance;
    }


    public Controller getController(Views view){
        return controllers.get(view);
    }

    public void changeView(Views view){
        controllers.get(view).activate();
        currentView = view;
    }

    public void addNewView(Views view){
        controllers.get(view).activateOnNew(view);
        currentView = view;
    }

    public void addController(Views view, Controller controller){
        controllers.put(view, controller);
    }

    public Views getCurrentView(){
        return currentView;
    }

    public Controller getCurrentController(){
        return controllers.get(getCurrentView());
    }


}
