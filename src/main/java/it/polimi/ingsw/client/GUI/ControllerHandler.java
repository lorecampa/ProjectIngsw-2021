package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.controller.Controller;
import it.polimi.ingsw.client.GUI.controller.PreGameSelectionController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Objects;

public class ControllerHandler {
    private MediaPlayer player;
    private boolean muted = false;
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


    public void addController(Views view, Controller controller){
        controllers.put(view, controller);
    }

    public Views getCurrentView(){
        return currentView;
    }

    public Controller getCurrentController(){
        return controllers.get(getCurrentView());
    }



    public void startSong(String music){
        Media pick = new Media(Objects.requireNonNull(getClass()
                .getResource("/GUI/audio/" + music)).toString());
        player = new MediaPlayer(pick);
        player.setAutoPlay(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(10);
        player.setOnEndOfMedia(() -> {
            player.seek(Duration.ZERO);
            player.play();
        });
        muted = false;
    }

    public boolean isMuted() {
        return muted;
    }

    public void changeMusic(){
        if (muted){
            player.play();
            muted = false;
        }else{
            player.pause();
            muted = true;
        }
    }
    public void stopMusic(){
        if (!muted){
            player.stop();
            muted = true;
        }
    }

    public void setMusicImage(ImageView imageContainer) {
        if (muted){
            imageContainer.setImage(new Image(Objects.requireNonNull(getClass()
                    .getResource("/GUI/icons/mute.png")).toString()));
        }else{
            imageContainer.setImage(new Image(Objects.requireNonNull(getClass()
                    .getResource("/GUI/icons/speaker.png")).toString()));
        }
    }








}
