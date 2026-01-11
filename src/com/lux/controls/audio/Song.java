package com.lux.controls.audio;

import com.lux.assets.AssetsManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Song implements Audio {
	
	public static final String[] SONG_NAMES = {"project.mp3","Bozic_je_narodna.mp3"};
	
	public static final int PROJECT = 0;
	public static final int BOZIC_JE = 1;
	
	
	
    private MediaPlayer player;
    private Timeline smoothStop;
    private boolean playReq;
    /**
     * Loop by default
     * @param song filename
     */
    public Song(String song){
        player = new MediaPlayer(AssetsManager.getMusic(song));
        
        smoothStop = new Timeline(new KeyFrame(Duration.millis(30), evt -> {
            player.setVolume(player.getVolume()-0.05);
        }));
        smoothStop.setCycleCount(20);
        smoothStop.setOnFinished(evt -> {player.stop();});
        
        setLoop(true);
    }
    @Override
    public void play(){
        player.play();
    }
    @Override
    public void stop(){
        player.stop();
        seek(0);
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
    @Override
    public void setLoop(boolean t) {
    	if (t) player.setCycleCount(MediaPlayer.INDEFINITE);
    	else player.setCycleCount(1);
    	/*
    	if (t) player.setOnEndOfMedia(() -> { seek(0); play(); });
    	else player.setOnEndOfMedia(null);
    	*/
    }
    
    public void fixedStop() {
        smoothStop.play();
    }

    public void playOnReset() {
        playReq=true;
    }
	@Override
	public void setVolume(double v) {
		player.setVolume(v);
	}
	@Override
	public double getVolume() {
		return player.getVolume();
	}
}
