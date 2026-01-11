package com.lux.level;

import java.util.ArrayList;

import com.lux.DynamicObject;
import com.lux.Event;
import com.lux.Main;
import com.lux.controls.TitleDisplayer;

import javafx.scene.layout.Pane;

public class Rooms extends ArrayList<ArrayList<Drawable>> implements DynamicObject {

	private static final long serialVersionUID = 5358619610063430532L;
	private LvlLoader lvlLoader;
	private static ArrayList<Drawable> groupedObjects;
	private static String[] areaNames;
	private static ArrayList<int[]> roomSizes;
	private static ArrayList<Trigger> triggers;
	private static ArrayList<Integer> tr_removedIndices;
	
	private int currentRoom;
	
	public static final int NUM_ROOMS = 5;
	private int maxLayers = 1;
	
	// koristi Pane umesto Group (veruj mi)
	// loaded when loading,changing room
	private Pane backLayer; // objects behind player
	private Pane frontLayer; // objects in front of player
	private ArrayList<Integer> heightenedObjects; // indices of objects with layer = Layer.HEIGHTENED
	
	public void updatePos(double x, double y) {
		backLayer.relocate(x, y);
		frontLayer.relocate(x, y);
		for (int d : heightenedObjects) {
			get(currentRoom).get(d).translate(x, y);
		}
	}

	public Rooms() {
		lvlLoader = new LvlLoader();
		groupedObjects = new ArrayList<>();
		roomSizes = new ArrayList<>();
		tr_removedIndices = new ArrayList<>();
		backLayer = new Pane();
		frontLayer = new Pane();
		heightenedObjects = new ArrayList<Integer>();
		areaNames = new String[NUM_ROOMS];
	}
	
	public void load() {
		for (int i=0; i<NUM_ROOMS; i++) {
			// System.out.println("Room: "+i);
        	add(loadObjects(i)); // loads all objects. does not sort them into layers
        	// numLayers = Integer.max(numLayers, lvlLoader.getNumLayers());
        	areaNames[i] = lvlLoader.getArea();
        }
	}
	
	public void preexecuteTriggers(ArrayList<Integer> indices) {
    	for (int i : indices) {
    		Trigger tr = getTriggers().get(i);
    		// System.out.println("pre-Trigger: "+i+" action="+tr.getAction());
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
                Drawable drw = new Drawable(data);
                maxLayers = Integer.max(maxLayers, drw.getLayer());
                // drw.setManaged(false);
                drawables.add(drw);
                if (drw.getGroup()!=0)
                    groupedObjects.add(drw);
            }
    	}
    	return drawables;
    }
	
	// objects get sorted into layers when loading (changing) ROOM
	public void loadRoom(int room) {
		currentRoom = room;
		// clear all layers
		backLayer.getChildren().clear();
		frontLayer.getChildren().clear();
		heightenedObjects.clear();
		
		// sorting into layers
		for (Drawable d : getAllFromRoom(room)) {
			if (d.getLayer()<=Layer.BACK) backLayer.getChildren().add(d);
			else if (d.getLayer()==Layer.HEIGHTENED) heightenedObjects.add(getAllFromRoom(room).indexOf(d));
			else frontLayer.getChildren().add(d);
		}
		
		// applying view order
		backLayer.setViewOrder(0.0D); // constant for back layer
		frontLayer.setViewOrder(-roomSizes.get(room)[1]*64);
	}
	
	public void addCurrentRoomToRoot() {
		Main.getDisplay().add(backLayer);
		for (int i : heightenedObjects) Main.getDisplay().add(getAllFromRoom(currentRoom).get(i));
		Main.getDisplay().add(frontLayer);
	}
	
    public ArrayList<Drawable> getObjectGroupWithID(int id) {
    	ArrayList<Drawable> list = new ArrayList<Drawable>();
    	
    	for (Drawable drw : groupedObjects) if (drw.getGroup() == id) list.add(drw);
    	
    	return list;
    }
    
    public void loadTriggers() {
    	triggers = TriggerLoader.load();
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
    public String getArea(int room) {
    	if (room<0 || room>=NUM_ROOMS) return "Pickovac";
    	return areaNames[room];
    }
    public void showAreaName(int room) {
    	TitleDisplayer.setText(getArea(room));
    	TitleDisplayer.setTextScale(1.0, 1.4);
    	TitleDisplayer.show();
    }
    public ArrayList<Drawable> getAllFromRoom(int room){
    	return get(room);
    }
    public Pane getBackLayer() {
		return backLayer;
	}
    public Pane getFrontLayer() {
		return frontLayer;
	}
	public ArrayList<Integer> getHeightenedObjects(){
		return heightenedObjects;
	}

	@Override
	public double getWorldX() {
		return -Main.getDisplay().getCameraX();
	}

	@Override
	public double getWorldY() {
		return -Main.getDisplay().getCameraY();
	}
}
