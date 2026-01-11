package com.lux.entity;

import java.util.ArrayList;
import java.util.LinkedList;

import com.lux.DynamicObject;
import com.lux.Main;

public class Entities extends ArrayList<Entity> implements DynamicObject {

	private static final long serialVersionUID = 1L;
	
	// use ArrayLists instead of javafx Groups
	private ArrayList<Entity> nodeGroup;
	private LinkedList<Integer> toRemove;
	private LinkedList<Integer> removedEntityIndices;

	public Entities() {
		super();
		nodeGroup = new ArrayList<>(); // sve vidljivo u trenutnoj sobu
		toRemove = new LinkedList<>(); // sama rec kaze
		removedEntityIndices = new LinkedList<>(); // sama rec kaze
	}

	public ArrayList<Entity> getNodeGroup() {
		return nodeGroup;
	}
	
	public ArrayList<Entity> getAllFromRoom(int room){
		ArrayList<Entity> ret = new ArrayList<Entity>();
		
		for (Entity e : this) {
			if (e.getRoom() == room) ret.add(e);
		}
		
		return ret;
	}

	public void applyRoom(int room) {
		nodeGroup.clear();
		for (Entity e : getAllFromRoom(room)) {
			if (!removedEntityIndices.contains(indexOf(e))) {
				nodeGroup.add(e);
			}
		}
		// nodeGroup.addAll(getAllFromRoom(room));
	}
	
	public void addToRemoveQueue(Entity e) {
		toRemove.addLast(indexOf(e));
	}
	
	public void removeRemovable() {
		for (int i : toRemove) {
			Entity e = get(i);
			removedEntityIndices.add(i);
			Main.getDisplay().removeDynamic(e);
			Main.getDisplay().remove(e);
			nodeGroup.remove(e);
			// this.remove(i);
		}
		toRemove.clear();
	}
	
	/**
	 * NOTE: that entities found in this list won't be accessible (data, triggers)
	 * And it created for just one purpose: save files (get track of which entities have been disappeared)
	 * @return list
	 */
	public LinkedList<Integer> getRemovedEntities(){
		return removedEntityIndices;
	}
	
	public void loadRemovedEntities(String save) {
// LOG: System.out.println("Removed entities: {"+save+"}");
    	for (String numberButString : save.split(" ")) {
    		if (numberButString.length()<1) continue;
    		int ent = Integer.valueOf(numberButString);
    		toRemove.add(ent);
    	}
	}
	
	@Override
	public void updatePos(double x, double y) {
		for (Entity e : nodeGroup) {
			if (removedEntityIndices.contains(nodeGroup.indexOf(e))) continue;
			e.updatePos(x, y);
		}
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
