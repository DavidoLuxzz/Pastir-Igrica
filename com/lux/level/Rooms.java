package com.lux.level;

import java.util.ArrayList;

import com.lux.DynamicObject;

import javafx.scene.Group;

public class Rooms extends ArrayList<ArrayList<Drawable>> implements DynamicObject {

	private static final long serialVersionUID = 5358619610063430532L;
	private LvlLoader lvlLoader;
	private ArrayList<Drawable> groupedObjects;
	private ArrayList<int[]> roomSizes;
	private ArrayList<Trigger> triggers;
	private ArrayList<Integer> tr_removedIndices;
	
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
    public void loadRemovedTriggers(int[] saveData) {
    	if (saveData != null)
            for (int i=3; i<saveData.length; i++) {
            	tr_removedIndices.add(saveData[i]);
            }
    }
    public ArrayList<Integer> getRemovedTriggerIndices() {
    	return tr_removedIndices;
    }
    	
    public void removeRemovableTriggers() {
    	for (int i : tr_removedIndices) {
        	triggers.remove(triggers.get(i));
        }
    }
    
    public ArrayList<Trigger> getTriggers() {
    	return triggers;
    }
    public ArrayList<int[]> getRoomSizes() {
    	return roomSizes;
    }
}
