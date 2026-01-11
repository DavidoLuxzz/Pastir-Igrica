package com.lux.controls.audio;

public interface Audio {
	
	public void play();
    public void stop();
    
    public void setVolume(double v);
    public double getVolume();
    
    public void setLoop(boolean t);
	
}
