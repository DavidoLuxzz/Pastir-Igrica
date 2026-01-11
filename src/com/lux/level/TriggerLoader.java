package com.lux.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TriggerLoader {
	//						0			 1			   2		   3				  4			   5  6    7      8         9			10
	// triggers format:  [ room  interactionEventID  needsZ  specialValue  removeAfterInteraction  x  y  width  height  special2  special3 ]
	//   other names:	  level			action		 			spec				rmai		   x  y    w	  h		  spec2	    spec3
	public static ArrayList<Trigger> load(){
		ArrayList<Trigger> triggers = new ArrayList<Trigger>();
        try {
            File myObj = new File("triggers.txt");
            try (Scanner myReader = new Scanner(myObj)) {
				while (myReader.hasNextLine()) {
					String line = myReader.nextLine();
					if (line.replaceAll(" ", "").startsWith("#")) continue; // pass comments
                    String[] data = line.split(" ");
                // System.out.println("Trigger: "+line+"; data.length="+data.length);
                    if (data.length>2) {
                    	int room = Integer.parseInt(data[0]);
                    	int action = Integer.parseInt(data[1]);
                    	boolean needsZ = Integer.parseInt(data[2])>0;
                    	int spec = Integer.parseInt(data[3]);
                    	boolean rmai = Integer.parseInt(data[4])>0;
                    	int x = Integer.parseInt(data[5]);
                    	int y = Integer.parseInt(data[6]);
                    	int width = Integer.parseInt(data[7]);
                    	int height = Integer.parseInt(data[8]);
                    	int spec2 = 0, spec3 = 0;
                    	if (data.length>9)
                    		spec2 = Integer.parseInt(data[9]);
                    	if (data.length>10)
                    		spec3 = Integer.parseInt(data[10]);
                    	Trigger tr = new Trigger(room, action, needsZ, spec, rmai, x, y, width, height, spec2, spec3);
                    	
                    	triggers.add(tr);
                    }    
                }
				myReader.close();
			}
            
        } catch (FileNotFoundException e) {
            System.err.println("Could not load default ent.txt file: "+e.toString());
            return null;
        }
        return triggers;
    }

}
