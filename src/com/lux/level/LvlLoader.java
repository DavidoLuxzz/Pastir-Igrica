package com.lux.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LvlLoader {
    private ArrayList<int[]> l;
    private String area;
    private int maxw,maxh;
    public LvlLoader(){
        l  = new ArrayList<>();
    }
    public boolean successLoad(int stage){
        l.clear();
        // default values (if not described in file)
        area = "";
        maxw = maxh = 0;
        try {
            File myObj = new File(stage+".txt");
            try (Scanner myReader = new Scanner(myObj)) {
                while (myReader.hasNextLine()) {
                	String line = myReader.nextLine();
                    String[] data = line.split(" ");
                    if (data.length!=0)
                        switch (data[0]) {
                        	/**
                        	 * Describes room dimensions
                        	 */
                            case "box":
                            	maxw = Integer.parseInt(data[1]);
                            	maxh = Integer.parseInt(data[2]);
                                break;
                            /**
                             * This command tells to which area does this room belong.
                             * This command is of course case sensitive. ("valjevo"!="Valjevo")
                             * TODO: Dodaj area teller
                             * TODO: Dodaj prikazivac mape
                             * Example: "area Valjevo" means that this room belongs to Valjevo
                             */
                            case "area":
                            	area = line.substring(5);
                            	while (area.startsWith(" ") && area.length()>0) {
                            		area = area.substring(1);
                            	}
                            	while (area.endsWith(" ") && area.length()>0) {
                            		area = area.substring(0, area.length()-1);
                            	}
                            	// System.out.println("Area: \""+area+"\"");
                            	break;
                            default:  // format:  [ textureID  x  y  solid  layer  group  angle  scalex%  scaley%  special  special2 ]
                            	int[] obj = new int[Drawable.MAX_COMPONENTS];
                                int index = 0;
                                for (String comp : data) {
                                	if (comp.length()<1) continue;
                                	obj[index++] = Integer.parseInt(comp);
                                }
                                l.add(obj);
                                break;
                        }
                }
                myReader.close();
            }
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Level empty!");
        }
        return false;
    }
    public ArrayList<int[]> getLevel(){
        return l;
    }
    public String getArea() {
    	return area;
    }
    public int getMaxWidth(){
        return maxw;
    }
    public int getMaxHeight(){
        return maxh;
    }
}
