package com.lux;

public class Item {
	
	public static final int KEY = 1;
	
	public static final int NIKE_SHOES = 2; // unlocks sprint ability
	
	public static final int SLASH_ABILITY = 3; // todo
	
	public static final String[] ITEM_NAMES = {"nista","Kljuc","Nike patike","Slash Ability"};
	
	public static String getName(int item) {
		if (item<0 || item>=ITEM_NAMES.length) return "unknown";
		return ITEM_NAMES[item];
	}

}
