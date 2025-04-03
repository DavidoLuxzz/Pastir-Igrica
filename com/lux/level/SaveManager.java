package com.lux.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.lux.Main;

public class SaveManager {
	private Main main;
	public SaveManager(Main main) {
		this.main = main;
	}
	public String data() {
		int room = main.roomID;
		int px = (int) main.getPlayer().getLayoutX();
		int py = (int) main.getPlayer().getLayoutY();
		
		return room+" "+px+" "+py;
	}
	public String rmtrData() {
		String ret = "";
		
		for (int i : main.getRemovedTriggerIndices()) {
			ret += (i+" ");
		}
		
		return ret;
	}
	public void save() {
		try {
			File file = new File("src/save.txt");
			if (!file.exists()) file.createNewFile();
			
			FileWriter fw = new FileWriter(file);
			
			String fullData = data() +"\n"+ rmtrData();
			
			fw.write(fullData);
			fw.close();
			
			System.out.println("FILE SAVE.TXT SAVED!");
		} catch (IOException e) {
			System.err.println("Could not save file;  "+e.toString());
		}
	}
	
	
	public int[] loadSave() {
		int[] ret = null;
		try {
            File myObj = new File("src/save.txt");
            try (Scanner myReader = new Scanner(myObj)) {
            	if (myReader.hasNextLine()) {
	                String fullData = myReader.nextLine() + " ";
	                if (myReader.hasNextLine()) fullData += myReader.nextLine();
	                myReader.close();
	                                
	                ret = saToIa(fullData.split(" "));
            	}
            }
        } catch (FileNotFoundException e) {
            System.err.println("Save file not found");
        }
		return ret;
	}
	
	private int[] saToIa(String[] numberStrs) {
		int[] numbers = new int[numberStrs.length];
		for(int i = 0;i < numberStrs.length;i++) {
		    try {
		        numbers[i] = Integer.parseInt(numberStrs[i]);
		    }
		    catch (NumberFormatException nfe) {};
		}
		return numbers;
	}
}
