package com.lux.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.lux.assets.AssetsManager;
import com.lux.entity.Entities;
import com.lux.entity.Entity;

public class NPCLoader {
	
	public static Entities load(){
		Entities npcs = new Entities();
		AssetsManager.loadEntityImages();
        try {
            File myObj = new File("ent.txt");
            try (Scanner myReader = new Scanner(myObj)) {
				while (myReader.hasNextLine()) {
					String line = myReader.nextLine();
					if (line.replaceAll(" ", "").startsWith("#")) continue; // pass comments
                    String[] data = line.split(" ");
                    if (data.length>2) {
                    	//             0  1   2       3      4    5       6       7
                    	// format:  [  x  y  room  texture  act  spec  (spec2)  (spec3)  ]
                    	// ako entity ima (spec2), pravilo je da mora da ima i (spec3)
                    	Entity ent = new Entity(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                    	// ent.setImage(AssetsManager.getImage("sheep.png", 3, 3));
                    	
                    	if (data.length > 6)
                    		 ent.setInteractionTrigger(Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), Integer.parseInt(data[7]));
                    	else ent.setInteractionTrigger(Integer.parseInt(data[4]), Integer.parseInt(data[5]), 0, 0);
                    	
                    	npcs.add(ent);
                    }    
                }
				myReader.close();
			}
            
        } catch (FileNotFoundException e) {
            System.err.println("Could not load default ent.txt file: "+e.toString());
            return null;
        }
        return npcs;
    }
}
