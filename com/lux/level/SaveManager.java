package com.lux.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import com.lux.Main;

public class SaveManager {
	public static String data() {
		int room = Main.roomID;
		// ne valja staviti getLayoutX() jer to nije prava world-koordinata
		int px = (int) Main.getPlayer().getWorldX();
		int py = (int) Main.getPlayer().getWorldY();
		
		return room+" "+px+" "+py;
	}
	public static String rmtrData() {
		String ret = "";
		
		for (int i : Main.getRemovedTriggerIndices()) {
			ret += (i+" ");
		}
		
		return ret;
	}
	public static String inventoryData() {
		String ret = "";
		
		for (int i : Main.getPlayer().inventory) {
			ret += (i+" ");
		}
		
		return ret;
	}
	public static String rmentData() {
		String ret = "";
		
		for (int i : Main.getEntities().getRemovedEntities()) {
			ret += (i+" ");
		}
		
		return ret;
	}
	public static void save() {
		try {
			File file = new File("save.properties");
			if (!file.exists()) file.createNewFile();
			
			Properties props = new Properties();
            props.setProperty("room", String.valueOf(Main.roomID));
            props.setProperty("x", String.valueOf((int)(Main.getPlayer().getLayoutX()+Main.getDisplay().getCameraX())));
            props.setProperty("y", String.valueOf((int)(Main.getPlayer().getLayoutY()+Main.getDisplay().getCameraY())));
            props.setProperty("removedTriggers", rmtrData());
            props.setProperty("inventory", inventoryData());
            props.setProperty("removedEntities", rmentData());
    		
			FileWriter fw = new FileWriter(file);
			props.store(fw, "Comment");
			fw.close();
			
			System.out.println("FILE SAVE.PROPERTIES SAVED!");
		} catch (IOException e) {
			System.err.println("Could not save file;  "+e.toString());
		}
	}
	
	
	public static int[] loadSave() {
		int[] ret = null;
		try {
            File myObj = new File("save.properties");
            
            Properties props = new Properties();
            FileInputStream is = new FileInputStream(myObj);
    		props.load(is);
    		is.close();
            
            /*try (Scanner myReader = new Scanner(myObj)) {
            	if (myReader.hasNextLine()) {
	                String fullData = myReader.nextLine() + " ";
	                if (myReader.hasNextLine()) fullData += myReader.nextLine();
	                myReader.close();
	                                
	                ret = saToIa(fullData.split(" "));
            	}
            }*/
    		ret = new int[3];
    		ret[0] = Integer.valueOf(props.getProperty("room"));
    		ret[1] = Integer.valueOf(props.getProperty("x"));
    		ret[2] = Integer.valueOf(props.getProperty("y"));
    		Rooms.loadRemovedTriggers(props.getProperty("removedTriggers"));
    		Main.getPlayer().inventory.loadInventory(props.getProperty("inventory"));
    		Main.getEntities().loadRemovedEntities(props.getProperty("removedEntities"));
        } catch (Exception e) {
            System.err.println("Not all save components found!");
            e.printStackTrace();
        }
		return ret;
	}
}
