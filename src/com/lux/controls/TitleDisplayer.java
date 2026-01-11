package com.lux.controls;

import com.lux.Main;
import com.lux.assets.AssetsManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TitleDisplayer {

    public static Text text;
    public static Timeline animation;
    public static Runnable currentAnimFunction, noAnimationFunction, fadeBothAnimationFunction, fadeInAnimationFunction;
    private static final double ANIMATION_TICK_RATE = 16.6667;
	private static int animationFlag = 0;
	// when at full opacity, how much to wait until starting to fade away (Kobe reference )
	private static int waitFullOpacityTicks = 30;
    
	public static void init() {
        text = new Text("Text");
        text.setFont(AssetsManager.KONGTEXT);
        text.setFill(Color.WHITE);
        text.relocate(10, Main.getDisplay().getHeight()-text.getFont().getSize()-10);
        text.setViewOrder(-4000);
        text.setVisible(false);
        text.setOpacity(0.0D);
        
        initAnimation();
        currentAnimFunction = fadeBothAnimationFunction;
	}
	
	public static void setTextScale(double scalex, double scaley) {
		text.setScaleX(scalex);
		text.setScaleY(scaley);
	}
	
	public static void addToRoot() {
		Main.getDisplay().add(text);
	}
	
	public static void setAnimationCycleCount(int cc) {
		animation.setCycleCount(cc);
	}
	
	public static void setAnimationFunction(Runnable run) {
		currentAnimFunction = run;
	}
	
	public static void setText(String t) {
		text.setText(t);
	}
	
	public static void show() {
		if (animation.getStatus()==Status.RUNNING) return;
		animationFlag = 0;
		text.setOpacity(0.0D);
		text.setVisible(true);
		animation.play();
	}
	
	public static void hide() {
		text.setVisible(false);
	}
	
	private static void initAnimation() {
		fadeBothAnimationFunction = new Runnable() {
			@Override
			public void run() {
				// System.out.println(animationFlag+" op "+text.getOpacity()); // log
				if (text.getOpacity()>=0.99 && animationFlag>=0) {
					animationFlag--;
					return;
				} else if (animationFlag==0) {
					text.setOpacity(0.0D);
					animationFlag = waitFullOpacityTicks;
				}
				int N = animation.getCycleCount();
				
				double fade0 = 2.0D / (N-waitFullOpacityTicks);
				
				text.setOpacity(text.getOpacity() +  fade0 * Integer.signum(animationFlag));
			}
		};
		fadeInAnimationFunction = new Runnable() {
			@Override
			public void run() {
				if (animationFlag==0) text.setOpacity(0.0D);
				int N = animation.getCycleCount();
				
				double fade0 = 1.0D / N;
				
				text.setOpacity(text.getOpacity()+fade0);
			}
		};
		noAnimationFunction = new Runnable() {
			@Override
			public void run() {
				text.setOpacity(1.0D);
			}
		};
		animation = new Timeline(new KeyFrame(Duration.millis(ANIMATION_TICK_RATE), (e) -> {
			currentAnimFunction.run();
		}));
		animation.setOnFinished((e)->{
			hide();
		});
		animation.setCycleCount(120); // 2 seconds by default
	}

}
