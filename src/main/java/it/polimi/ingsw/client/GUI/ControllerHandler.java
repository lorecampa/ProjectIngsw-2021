package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.controller.Controller;
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
    private final HashMap<Views, Controller> controllers = new HashMap<>();
    private Views currentView = null;
    private static final ControllerHandler instance = new ControllerHandler();

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
        //TODO change volume 0.2 is the best
        player.setVolume(0);
        player.setOnEndOfMedia(() -> {
            player.seek(Duration.ZERO);
            player.play();
        });
        muted = false;
    }

    public void setVolume(double volume){
        player.setVolume(volume);
    }

    public double getVolume(){
        return player.getVolume();
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
