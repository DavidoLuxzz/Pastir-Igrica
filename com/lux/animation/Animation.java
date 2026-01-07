package com.lux.animation;

import java.util.LinkedList;

import com.lux.assets.AssetsManager;
import com.lux.entity.Player;
import com.lux.entity.Sprite;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class Animation extends Sprite {

	String animationFolder;
	int numImages;
	LinkedList<Image> images;
	
	int currentImage = 0;
	
	Timeline animation;
	boolean hideWhenFinished = false;
	
	public Animation() {
		super();
	}
	public Animation(String animFolder, int nimgs) {
		super();
		loadImages(animFolder, nimgs);
	}
	public void loadImages(String animFolder, int nimgs) {
		animationFolder = animFolder;
		numImages = nimgs;
		
		images = new LinkedList<>();
		for (int i=0; i<nimgs; i++) {
			images.addLast(AssetsManager.getImage(animFolder+"/"+i+".png", 4.0, 4.0));
		}
		setImage(images.get(currentImage));
	}
	
	// direction wsad -> 0123
	public void orientatePriorToPlayer(Player p) {
		int direction = p.getDirection();
		switch (direction) {
		case 0:{ // up
			double optionalOffset = 10;
			setCenterX(p.getCenterX()-optionalOffset);
			setCenterY(p.getCenterY()-50);
			setRotate(-90);
			setScaleX(1);
			break;
		}
		case 1:{ // down
			double optionalOffset = 10;
			setCenterX(p.getCenterX()-optionalOffset);
			setCenterY(p.getCenterY()+80);
			setRotate(90);
			setScaleX(1);
			break;
		}
		case 2:{ // left
			setCenterX(p.getCenterX()-70);
			setCenterY(p.getCenterY());
			setRotate(0);
			setScaleX(-1);
			break;
		}
		case 3:{ // right
			setCenterX(p.getCenterX()+70);
			setCenterY(p.getCenterY());
			setRotate(0);
			setScaleX(1);
			break;
		}
		default:
			break;
		}
	}
	
	public void playFromStart() {
		setCurrentImage(0);
		setVisible(true);
		animation.playFromStart();
	}
	
	public boolean isFinished() {
		return animation.getStatus() == Status.STOPPED;
	}
	
	public void initAnimation(double rate_ms, int cycleCount) {
		animation = new Timeline(new KeyFrame(Duration.millis(rate_ms), (e) -> {
			nextImage();
		}));
		animation.setCycleCount(cycleCount);
		animation.setOnFinished((e)->{
			if (hideWhenFinished) setVisible(false);
		});
	}
	
	public void setCurrentImage(int t) {
		currentImage = t;
		setImage(images.get(t));
	}
	
	public void hideAfterFinished(boolean t) {
		hideWhenFinished = t;
	}
	
	
	public void nextImage() {
		if (++currentImage>=numImages) {
			currentImage = 0;
		}
		setImage(images.get(currentImage));
	}
	
	
	

}
