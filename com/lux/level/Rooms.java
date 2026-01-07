package com.lux.level;

import java.util.ArrayList;

import com.lux.DynamicObject;
import com.lux.Event;
import com.lux.Main;

import javafx.scene.Group;

public class Rooms extends ArrayList<ArrayList<Drawable>> implements DynamicObject {

	private static final long serialVersionUID = 5358619610063430532L;
	private LvlLoader lvlLoader;
	private static ArrayList<Drawable> groupedObjects;
	private static ArrayList<int[]> roomSizes;
	private static ArrayList<Trigger> triggers;
	private static ArrayList<Integer> tr_removedIndices;
	
	private Group nodeGroup;
	
	public void updatePos(double x, double y) {
		nodeGroup.relocate(x, y);
	}

	public Rooms() {
		lvlLoader = new LvlLoader();
		groupedObjects = new ArrayList<>();
		roomSizes = new ArrayList<>();
		tr_removedIndices = new ArrayList<>();
		nodeGroup = new Group();
	}
	
	public void load() {
		for (int i=0; i<4; i++) {
        	add(loadObjects(i));
        }
	}
	
	public void preexecuteTriggers(ArrayList<Integer> indices) {
    	for (int i : indices) {
    		Trigger tr = getTriggers().get(i);
    		System.out.println("pre-Trigger: "+i+" action="+tr.getAction());
	    	if (tr.getAction() != Event.TEXTURE_CHANGE) continue;
	    	tr.execute(null);
    	}
    }
    public void checkTriggers(){
        if (!Main.getPlayer().isBusy()){
            ArrayList<Trigger> toRemove = new ArrayList<>();
            for (Trigger tr : getTriggers()) {
            	if (isTriggerRemoved(tr)) continue; // trigger is removed, no need for checking
                if (tr.getRoom() == Main.roomID){  // trigger is in this room thus can be intersected with
                    if (Main.getPlayer().getHitbox().intersects(tr.getHitbox())){
                        if (!tr.needsZ() || Main.zDown) {
                            tr.execute(null);
                            if (tr.removeAfterInteraction()) {
                            	toRemove.add(tr);
                            	getRemovedTriggerIndices().add(getTriggers().indexOf(tr));
                            	tr = null;
                            }
                        }
                    }
                }
                if (Main.getPlayer().isBusy()) break;
            }
            // if (!toRemove.isEmpty()) getTriggers().removeAll(toRemove);
        }
    }
	
	public ArrayList<Drawable> loadObjects(int room) {
    	ArrayList<Drawable> drawables = new ArrayList<>();
    	if (lvlLoader.successLoad(room)) {
    		roomSizes.add(new int[] {lvlLoader.getMaxWidth(), lvlLoader.getMaxHeight()});
    		for (int[] data : lvlLoader.getLevel()) {
                Drawable drw = new Drawable(data[0],data[1],data[2]);
                drw.relocate(data[3],data[4]);
                drawables.add(drw);
                if (drw.getGroup()!=0)
                    groupedObjects.add(drw);
            }
    	}
    	return drawables;
    }
	
	public void updateNodeGroup(int room) {
		nodeGroup.getChildren().clear();
		nodeGroup.getChildren().addAll(get(room));
	}
	public Group getNodeGroup() {
		return nodeGroup;
	}
	
    public ArrayList<Drawable> getObjectGroupWithID(int id) {
    	ArrayList<Drawable> list = new ArrayList<Drawable>();
    	
    	for (Drawable drw : groupedObjects) if (drw.getGroup() == id) list.add(drw);
    	
    	return list;
    }
    
    public void loadTriggers() {
    	triggers = Trigger.loadTriggerArray(lvlLoader.loadTriggers());
    }
    public static void loadRemovedTriggers(String removedTriggers) {
    	for (String numberButString : removedTriggers.split(" ")) {
    		if (numberButString.length()<1) continue;
    		int tr = Integer.valueOf(numberButString);
    		tr_removedIndices.add(tr);
    	}
    	/*
    	if (saveData != null)
            for (int i=3; i<saveData.length; i++) {
            	tr_removedIndices.add(saveData[i]);
            }
            */
    }
    public ArrayList<Integer> getRemovedTriggerIndices() {
    	return tr_removedIndices;
    }
    
    /**
     * This function returns all triggers
     * @return
     */
    public ArrayList<Trigger> getTriggers() {
    	return triggers;
    }
    public boolean isTriggerRemoved(Trigger t) {
    	return getRemovedTriggerIndices().contains(getTriggers().indexOf(t));
    }
    public static ArrayList<int[]> getRoomSizes() {
    	return roomSizes;
    }
}
