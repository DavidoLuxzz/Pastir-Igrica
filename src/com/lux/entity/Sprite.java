package com.lux.entity;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends ImageView {
    public Sprite(){
        super();
    }
    public Sprite(Image img){
        super(img);
    }
    public double getCenterX(){
        return getLayoutX()+getBoundsInParent().getWidth()/2;
    }
    public double getCenterY(){
        return getLayoutY()+getBoundsInParent().getHeight()/2;
    }
    public void setCenterX(double v){
        setLayoutX(v-getBoundsInParent().getWidth()/2);
    }
    public void setCenterY(double v){
        setLayoutY(v-getBoundsInParent().getHeight()/2);
    }
    
    
    public double getBottomX(){
        return getLayoutX()+getBoundsInParent().getWidth();
    }
    public double getBottomY(){
        return getLayoutY()+getBoundsInParent().getHeight();
    }
    public void setBottomX(double v){
        setLayoutX(v-getBoundsInParent().getWidth());
    }
    public void setBottomY(double v){
        setLayoutY(v-getBoundsInParent().getHeight());
    }
    @Override
    public void relocate(double x, double y) {
    	setLayoutX(x);
    	setLayoutY(y);
    	updateDepth();
    }
    public void orientate(double dx){
        if (dx==0) return;
        setScaleX(Math.signum(dx));            
    }

    public void move(double dx, double dy){
        setLayoutX(getLayoutX() + dx);
        setLayoutY(getLayoutY() + dy);
    }
    
    public Bounds getHitbox(){
        return getBoundsInParent();
    }
    
    public void updateDepth() {
    	setViewOrder(-getBottomY());
    }
}
