package com.lux.controls.audio;

import com.lux.assets.AssetsManager;
import javafx.scene.media.AudioClip;

public class Sound implements Audio {
	
	public static final String[] SOUND_NAMES = {"walk_on_grass0.mp3"};
	
	public static final int WALK_ON_GRASS0 = 0;
	
	
    private AudioClip player;
    /**
     * No loop by default
     * @param sound filename
     */
    public Sound(String sound){
        player = new AudioClip(AssetsManager.getMusic(sound).getSource());
        
        setLoop(false);
    }
    @Override
    public void play(){
        player.play();
        player.setVolume(1.0D);
    }
    @Override
    public void stop(){
        player.stop();
    }

    @Override
    public void setLoop(boolean t) {
    	if (t) player.setCycleCount(AudioClip.INDEFINITE);
    	else player.setCycleCount(1);
    	/*
    	if (t) player.setOnEndOfMedia(() -> { seek(0); play(); });
    	else player.setOnEndOfMedia(null);
    	*/
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
