package com.lux.controls;

import com.lux.assets.AssetsManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayer {
    private MediaPlayer player;
    private Timeline smoothStop;
    private boolean playReq;
    public MusicPlayer(String song){
        player = new MediaPlayer(AssetsManager.getMusic(song));
        smoothStop = new Timeline(new KeyFrame(Duration.millis(30), evt -> {
            player.setVolume(player.getVolume()-0.05);
        }));
        smoothStop.setCycleCount(20);
        smoothStop.setOnFinished(evt -> {player.stop();});
        
        setLoop(true);
    }
    public void play(){
        player.play();
        player.setVolume(1.0D);
    }
    public void stop(){
        player.stop();
    }
    public void pause(){
        player.pause();
    }
    public void seek(double duration){
        player.seek(Duration.millis(duration));
    }

    public void checkEvents() {
        if (playReq){
            play();
            playReq=false;
        }
    }
    
    public void setLoop(boolean t) {
    	if (t) player.setOnEndOfMedia(() -> { seek(0); play(); });
    	if (!t) player.setOnEndOfMedia(null);
    }
    
    public void fixedStop() {
        smoothStop.play();
    }

    public void playOnReset() {
        playReq=true;
    }
}
