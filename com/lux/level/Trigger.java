package com.lux.level;

// triggers format:  [ interactionEventID  needsZ  specialValue  removeAfterInteraction  x  y  width  height  (special2)  (special3) ]

import java.util.ArrayList;

import com.lux.Dialog;
import com.lux.Event;
import com.lux.Main;
import com.lux.controls.DialogBox;
import com.lux.entity.Entity;

import javafx.geometry.BoundingBox;

public class Trigger {
    private int action;
    private boolean needsZ;
    private int special, special2, special3;
    private boolean rmai;
    private int x, y, width, height;
    
    private int room;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public boolean needsZ() {
        return needsZ;
    }

    public void setNeedsZ(boolean needsZ) {
        this.needsZ = needsZ;
    }

    public int getSpecial() {
        return special;
    }
    public int getSpecial2() {
        return special2;
    }
    public int getSpecial3() {
        return special3;
    }
    public int[] getSpecial23(){
        return new int[]{special2, special3};
    }
    
    public void setSpecial(int special) {
        this.special = special;
    }

    public boolean removeAfterInteraction() {
        return rmai;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public Trigger(int action, boolean needsZ, int special, boolean rmai, int x, int y, int w, int h){
    	init(0, action, needsZ, special, rmai, x, y, w, h, 0, 0);
    }
    public Trigger(int room, int action, boolean needsZ, int special, boolean rmai, int x, int y, int w, int h){
    	init(room, action, needsZ, special, rmai, x, y, w, h, 0, 0);
    }
    public Trigger(int action, boolean needsZ, int special, boolean rmai, int x, int y, int w, int h, int spec2, int spec3){
    	init(0, action, needsZ, special, rmai, x, y, w, h, spec2, spec3);
    }
    public Trigger(int room, int action, boolean needsZ, int special, boolean rmai, int x, int y, int w, int h, int spec2, int spec3){
    	init(room, action, needsZ, special, rmai, x, y, w, h, spec2, spec3);
    }
    public Trigger(int[] list, int room){
        init(list, room);
    }
    
    public void init(int room, int action, boolean needsZ, int special, boolean rmai, int x, int y, int w, int h, int spec2, int spec3) {
    	this.room = room;
        this.action = action;
        this.needsZ = needsZ;
        this.special = special;
        this.rmai = rmai;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.special2 = spec2;
        this.special3 = spec3;
    }
    
    public void init(int[] list, int room) {
    	this.action     = list[0];
        this.needsZ     = list[1]>0;
        this.special    = list[2];
        this.rmai       = list[3]>0;
        this.x          = list[4];
        this.y          = list[5];
        this.width      = list[6];
        this.height     = list[7];
        this.special2   = list[9];
        this.special3   = list[10];
        
        this.room = room;
    }
    
    public void execute(Entity triggerOwner) {
    	switch (getAction()){
	        case Event.DIALOG_INTERRUPT: // dialog interrupt
	            DialogBox.setDialog(Dialog.getDialog(getSpecial()));
	            DialogBox.show();
	            break;
	        case Event.DIALOG_INTERRUPT_THEN_SUDDEN_DISAPPEAR:
	        	DialogBox.setDialog(Dialog.getDialog(getSpecial()));
	        	DialogBox.show();
	        	break;
	        case Event.CHANGE_ROOM:      // change room
	            Main.smoothChangeRoom(getSpecial(), getSpecial23());
	            return;
	        case Event.TEXTURE_CHANGE:
	        	textureChange();
	        	break;
	        case Event.GIVE_ITEM:
	        	giveItem();
	        	break;
	        case Event.GIVEN_ITEM_THEN_DISAPPEAR:
	        	giveItem();
	        	triggerOwner.disappear();
	        	break;
	        default:
	            break;
	    }
    }
    
    private void textureChange() {
    	for (Drawable drw : Main.rooms.getObjectGroupWithID(getSpecial())) {
    		drw.setTexture(getSpecial2());
    		drw.setSolid(getSpecial3() > 0);
    		
    		if (getSpecial3() > 0)  Main.getPlayer().addStaticObject(drw);
    		else Main.getPlayer().removeStaticObject(drw);
    	}
    }
    
    
    private void giveItem() {
    	Main.getPlayer().inventory.addWithDialog(getSpecial());
    }
    
    
    
    public static ArrayList<Trigger> loadTriggerArray(ArrayList<int[]> t){
        ArrayList<Trigger> ret = new ArrayList<>();
        for (int[] tr : t) ret.add(new Trigger(tr, tr[8]));
        return ret;
    }
    public BoundingBox getHitbox(){
        return new BoundingBox(x, y, width, height);
    }

    public int getRoom() {
        return room;
    }
    
    public void neutralize() {
    	init(new int[11], -1);
    }
}
