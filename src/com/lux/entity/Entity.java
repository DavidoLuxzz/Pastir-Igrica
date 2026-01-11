package com.lux.entity;

import com.lux.DynamicObject;
import com.lux.Main;
import com.lux.assets.AssetsManager;
import com.lux.level.Trigger;

import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;

public class Entity extends Sprite implements DynamicObject {
	Trigger interactionTrigger;
	int room = 0;
	
	private double motherX, motherY;
	
	public Entity() {
		super();
		interactionTrigger = new Trigger(0, false, 0, false, 0, 0, 0, 0, 0, 0);
	}
	public Entity(int x, int y, int room, int texture) {
		super();
		interactionTrigger = new Trigger(0, false, 0, false, 0, 0, 0, 0, 0, 0);
		relocate(x, y);
		setRoom(room);
		setImage(AssetsManager.getEntityImage(texture));
	}

	public Entity(Image img) {
		super(img);
		interactionTrigger = new Trigger(0, false, 0, false, 0, 0, 0, 0, 0, 0);
	}
	
	@Override
    public void relocate(double x, double y) {
    	motherX = x;
    	motherY = y;
    }
	
	public void disappear() {
		Main.getEntities().addToRemoveQueue(this); // unlink
	}
	
	public void setInteractionTrigger(Trigger tr) {
		interactionTrigger = tr;
	}
	public void setInteractionTrigger(int action, int special, int spec2, int spec3) {
		interactionTrigger = new Trigger(action, false, special, false, 0,0,0,0, spec2, spec3);
	}
	public Trigger getInteractionTrigger() {
		return interactionTrigger;
	}
	
	@Override
    public BoundingBox getHitbox(){
        return new BoundingBox(motherX+8, motherY+getImage().getHeight()/2, getImage().getWidth()-16, getImage().getHeight()/2-8);
    }
	
	public BoundingBox getExpandedHitbox() {
    	return new BoundingBox(motherX, motherY+getImage().getHeight()/3, getImage().getWidth(), 2*getImage().getHeight()/3);
    }

	public int getRoom() {
		return room;
	}
	public void setRoom(int room) {
		this.room = room;
	}
	@Override
	public void updatePos(double x, double y) {
		setLayoutX(motherX + x);
		setLayoutY(motherY + y);
		updateDepth();
	}
	@Override
	public double getWorldX() {
		return getLayoutX()-Main.getDisplay().getCameraX();
	}
	@Override
	public double getWorldY() {
		return getLayoutY()-Main.getDisplay().getCameraY();
	}
}
