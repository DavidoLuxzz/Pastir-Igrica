package com.lux.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LvlLoader {
    private ArrayList<int[]> l;
    private ArrayList<int[]> t;
    private ArrayList<int[]> gs; // ghosts
    private ArrayList<int[]> ws; // wolves
    private int gsCounter;
    private int wsCounter;
    private int maxw,maxh;
    public LvlLoader(){
        t  = new ArrayList<>();
        l  = new ArrayList<>();  
        gs = new ArrayList<>();
        ws = new ArrayList<>();
    }
    public boolean successLoad(int stage){
        l.clear();
        gs.clear();
        gsCounter=0;
        ws.clear();
        wsCounter=0;
        try {
            File myObj = new File(stage+".txt");
            try (Scanner myReader = new Scanner(myObj)) {
                while (myReader.hasNextLine()) {
                    String[] data = myReader.nextLine().split(" ");
                    if (data.length!=0)
                        switch (data[0]) {
                            case "g":
                                gs.add(new int[]{Integer.parseInt(data[1]),Integer.parseInt(data[2])});
                                gsCounter++;
                                break;
                            case "w":
                                ws.add(new int[]{Integer.parseInt(data[1]),Integer.parseInt(data[2])});
                                wsCounter++;
                                break;
                            case "box":
                            	maxw = Integer.parseInt(data[1]);
                            	maxh = Integer.parseInt(data[2]);
                                break;
                            case "":
                            case "tr":  // triggers (loaded in other function)
                                break;
                            default:  // format:  [ textureID  interactionEventID  specialValue  x  y ]
                                l.add(new int[]{Integer.parseInt(data[0]),Integer.parseInt(data[1]),Integer.parseInt(data[2]),Integer.parseInt(data[3]),Integer.parseInt(data[4])});
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
    public ArrayList<int[]> loadTriggers(){
        t.clear();
        _loadTriggers(0);
        return t;
    }
    public void _loadTriggers(int zero){
        try {
            File myObj = new File(zero+".txt");
            try (Scanner myReader = new Scanner(myObj)) {
                while (myReader.hasNextLine()) {
                    String[] data = myReader.nextLine().split(" ");
                    if (data[0].equals("tr")){
                        //           index:         0               1           2           3     4  5    6      7       8       9           10
                        // triggers format:  [ interactionEventID  needsZ  specialValue  rmAfInt  x  y  width  height  room  (special2)  (special3) ]
                        int speci2 = 0, speci3 = 0;
                        if (data.length > 10) {
                            speci2 = Integer.parseInt(data[9]);
                            speci3 = Integer.parseInt(data[10]);
                        }
                        t.add(new int[]{Integer.parseInt(data[1]),Integer.parseInt(data[2]),Integer.parseInt(data[3]),Integer.parseInt(data[4]),
                                        Integer.parseInt(data[5]),Integer.parseInt(data[6]),Integer.parseInt(data[7]),Integer.parseInt(data[8]), zero, speci2, speci3});
                    }  
                }
                myReader.close();
            }
            _loadTriggers(zero+1);
        } catch (FileNotFoundException __) {
        }
    }
    public ArrayList<int[]> getLevel(){
        return l;
    }
    public int getMaxWidth(){
        return maxw;
    }
    public int getMaxHeight(){
        return maxh;
    }
    public ArrayList<int[]> getGhostArray(){
        return gs;
    }
    public ArrayList<int[]> getWolfArray(){
        return ws;
    }
    public int getGhostCounter(){
        return gsCounter;
    }
    public int getWolfCounter(){
        return wsCounter;
    }
    public ArrayList<int[]> getTriggers(){
        return t;
    }
}
