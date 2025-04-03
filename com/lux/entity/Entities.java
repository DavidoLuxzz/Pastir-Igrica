package com.lux.entity;

import java.util.ArrayList;

import com.lux.DynamicObject;

public class Entities extends ArrayList<Entity> implements DynamicObject {

	private static final long serialVersionUID = 1L;
	
	// use ArrayLists instead of javafx Groups
	private ArrayList<Entity> nodeGroup;

	public Entities() {
		super();
		nodeGroup = new ArrayList<>();
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
		nodeGroup.addAll(getAllFromRoom(room));
	}
	
	@Override
	public void updatePos(double x, double y) {
		for (Entity e : nodeGroup) e.updatePos(x, y);
	}
}
