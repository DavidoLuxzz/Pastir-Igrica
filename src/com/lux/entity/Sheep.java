package com.lux.entity;

import com.lux.Event;
import com.lux.assets.AssetsManager;
import com.lux.level.Trigger;

import javafx.geometry.BoundingBox;

public class Sheep extends Entity {
    public Sheep(){
        super(AssetsManager.getImage("sheep.png", 3, 3));
        setInteractionTrigger(new Trigger(Event.DIALOG_INTERRUPT, false, 2, false, 0, 0, 0, 0, 0, 0));
        room = 0;
    }
    @Override
    public BoundingBox getHitbox(){
        return new BoundingBox(getLayoutX()+8, getLayoutY()+getImage().getHeight()/2, getImage().getWidth()-16, getImage().getHeight()/2-8);
    }
    @Override
    public BoundingBox getExpandedHitbox() {
    	return new BoundingBox(getLayoutX(), getLayoutY()+getImage().getHeight()/3, getImage().getWidth(), 2*getImage().getHeight()/3);
    }
}
