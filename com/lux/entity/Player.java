package com.lux.entity;

import com.lux.Item;
import com.lux.Main;
import com.lux.animation.Animation;
import com.lux.assets.AssetsManager;
import com.lux.controls.Inventory;
import com.lux.level.Drawable;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class Player extends Sprite {
    private int w,s,a,d;
    private double speed = 2.0D; // px/gt
    public static final double SPRINT_MULTIPLIER = 1.5; 
    private double speedm = 1.0D;
    private ArrayList<Drawable> cos;
    private ArrayList<Drawable> cws;
    private int spawnX, spawnY;
    
    private double camx, camy;
    
    private double reqx, reqy;
    
    private boolean chad = false;
    private boolean busy = false;
    
    private int direction = 0;
    private Image[] tiles;
    private boolean walking;
    private int _aniIndex; // animation helper flag
    private Timeline walkAnim; // walk animation
    private Animation slashAnim;
    
    public Inventory inventory;
    
    public Player() {
    	super(AssetsManager.getImage("player/w0.png",2,2));
        cos = new ArrayList<>();
        cws = new ArrayList<>();
        inventory = new Inventory();
        loadPlayerTiles(false);
        initWalkAnimation();
        loadSlashAnimation();
    }
    
    public Player(ArrayList<Drawable> objects){
        super(AssetsManager.getImage("player/w0.png",2,2));
        cos = new ArrayList<>();
        cws = new ArrayList<>();
        inventory = new Inventory();
        createCWOS(objects);
        loadPlayerTiles(false);
        initWalkAnimation();
        loadSlashAnimation();
    }
    
   	public Animation getSlashAnimation() {
   		return slashAnim;
   	}
    
    private void loadSlashAnimation() {
    	slashAnim = new Animation("slash", 3);
    	slashAnim.hideAfterFinished(true);
    	slashAnim.initAnimation(40, 3);
    	slashAnim.setVisible(false);
    }
    
    public void slash() {
    	if (!getInventory().contains(Item.SLASH_ABILITY)) return;
    	if (slashAnim.isFinished())
    		slashAnim.playFromStart();
    }
    
    public void loadPlayerTiles(boolean withNikes){
        tiles = new Image[16];
        for (int dir=0; dir<4; dir++){
            for (int i=0; i<4; i++){
                tiles[dir*4 + i] = AssetsManager.getImage((withNikes?"player_w_nike/":"player/")+"wsad".charAt(dir)+i+".png", 2, 2);
            }
        }
    }
    
    public void createCWOS(ArrayList<Drawable> objects){
        for (Drawable drw : objects){
            if (drw.isSolid())
                cos.add(drw);
            else if (drw.getTexture()==6)
                cws.add(drw);
        }
    }
    public void updateCWOS(ArrayList<Drawable> objects){
        cos.clear();
        cws.clear();
        createCWOS(objects);
    }
    public void addStaticObject(Drawable d) {
    	if (!cos.contains(d)) cos.add(d);
    }
    public void removeStaticObject(Drawable d) {
    	if (cos.contains(d)) cos.remove(d);
    }
    private void initWalkAnimation(){
        walkAnim = new Timeline(new KeyFrame(Duration.millis(400), (e) -> {
            setImage(tiles[direction*4 + _aniIndex]);
            _aniIndex++;
        }));
        walkAnim.setCycleCount(2);
        walkAnim.setOnFinished((e) -> {
            if (_aniIndex >= 3) _aniIndex=0;
            if (w+s+a+d > 0) {
                walkAnim.play();
            } else {
                walking = false;
                setImage(tiles[direction*4]);
            }
        });
    }
    public void reqRelocate(double x, double y) {
    	relocate(x, y);
    	reqx = x;
    	reqy = y;
    }
    private void walk(){
        if (walking){
            _aniIndex = (_aniIndex+1)%48;
            setImage(tiles[direction*4 + (int)(_aniIndex/12)]);
        }
        if (w+s+a+d <= 0) {
            if (_aniIndex/12%2 == 0) walking = false;
        } else walking = true;
    }
    
    public void up(int i){
        w=i;
        if (i>0) direction = 0;
    }
    public void down(int i){
        s=i;
        if (i>0) direction = 1;
    }
    public void left(int i){
        a=i;
        if (i>0) direction = 2;
    }
    public void right(int i){
        d=i;
        if (i>0) direction = 3;
    }
    private void orientate(){
        boolean xa = d+a > 0;
        boolean ya = s+w > 0;
        if (ya) {
            if (s-w != 0){
                if (w > 0) direction = 0;
                if (s > 0) direction = 1;
            }
        } else if (xa){
            if (d-a != 0){
                if (a > 0) direction = 2;
                if (d > 0) direction = 3;
            }
        }
    }
    public int getDirection() {
    	return direction;
    }
    
    public void update(){
        if (!busy){ // can't move if in dialog
            moveWithCollisionCheck();
            orientate();
            walk();
            updateDepth();
            slashAnim.setViewOrder(getViewOrder()-0.005);
            slashAnim.orientatePriorToPlayer(this);
        }
    }
    public double getRequestedX() {
    	return reqx;
    }
    public double getRequestedY() {
    	return reqy;
    }
    public BoundingBox getHitboxAt(double x, double y){
        return new BoundingBox(x+8, y+2*getImage().getHeight()/3, getImage().getWidth()-16, getImage().getHeight()/3-4);
    }
    @Override
    public BoundingBox getHitbox(){
        return getHitboxAt(reqx, reqy);
    }
    public BoundingBox getRealHitbox() {
    	return getHitboxAt(getLayoutX(), getLayoutY());
    }
    // user for interactions
    public BoundingBox getExpandedHitbox() {
    	return new BoundingBox(reqx, reqy+getImage().getHeight()/3, getImage().getWidth(), 2*getImage().getHeight()/3);
    }
    public BoundingBox getRealExpandedHitbox() {
    	return new BoundingBox(getLayoutX(), getLayoutY()+getImage().getHeight()/3, getImage().getWidth(), 2*getImage().getHeight()/3);
    }
    public void moveWithCollisionCheck(){
        if (!chad){
        	double dx = (d-a)*speed*speedm;
            double dy = (s-w)*speed*speedm;
            
            boolean canMoveX = true;
            boolean canMoveY = true;
            for (Drawable o : cos){
                if (o.getHitbox().intersects(getHitboxAt(reqx + dx, reqy))){ // X movement check
                    canMoveX = false;
                }
                if (o.getHitbox().intersects(getHitboxAt(reqx, reqy + dy))){ // Y movement check
                    canMoveY = false;
                }
            }
            for (Entity e : Main.getEntities().getNodeGroup()) {
            	if (e.getHitbox().intersects(getHitboxAt(reqx + dx, reqy))) canMoveX = false;
            	if (e.getHitbox().intersects(getHitboxAt(reqx, reqy + dy))) canMoveY = false;
            }

            if (canMoveX) reqx = (getRequestedX() + dx);
            if (canMoveY) reqy = (getRequestedY() + dy);
        }
    }
    public void setSpeed(double s){
        this.speed = s;
    }
    public double getSpeed(){
        return this.speed;
    }
    public void setSpeedMultiplier(double d){
        this.speedm = d;
    }
    public double getSpeedMultiplier(){
        return this.speedm;
    }
    public void configurateSpeed(){
        for (Drawable c : cws){
            if (c.getBounds().intersects(getHitbox())){
                setSpeed(2);
                return;
            }
        }
        if (speed!=4)
            setSpeed(4);
    }
    public void setCameraX(double x) {
    	this.camx = x;
    }
    public void setCameraY(double y) {
    	this.camy = y;
    }
    public void setCameraPos(double x, double y) {
    	this.camx = x;
    	this.camy = y;
    }
    public double getCameraX() {
    	return camx;
    }
    public double getCameraY() {
    	return camy;
    }
    public void respawn(int[] spawn){
        if (spawn != null) reqRelocate(spawn[0],spawn[1]);
    }
    public void setInvincible(boolean t){
        this.chad = t;
    }
    public boolean isBusy(){
        return busy;
    }
    public void setBusy(boolean t){
        busy = t;
    }
    public void setSpawn(int x, int y){
        spawnX = x;
        spawnY = y;
    }
    public int getSpawnX(){
        return spawnX;
    }
    public int getSpawnY(){
        return spawnY;
    }
    public double getWorldX() {
    	return getLayoutX()+camx;
    }
    public double getWorldY() {
    	return getLayoutY()+camy;
    }
    public Inventory getInventory() {
    	return inventory;
    }
}
