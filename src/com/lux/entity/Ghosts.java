package com.lux.entity;

import com.lux.Display;
import java.util.ArrayList;

public class Ghosts extends ArrayList<Ghost> {
    private static final long serialVersionUID = 1L;
	private Player tg;
    public Ghosts(){
        super();
    }
    public Ghosts(Player target){
        super();
        tg = target;
    }
    public void init(Player target){
        tg = target;
    }
    /**
     * Updates ghost array.Used every time when room is changed.
     * @param gs Ghost array
     * @param count Ghost count
     */
    public void renew(ArrayList<int[]> gs, int count){
        clear();
        for (int i=0;i<count;i++){
            add(new Ghost(tg,gs.get(i)[1],gs.get(i)[2]));
        }
    }
    public void show(Display root){
        root.addAll(this);
    }
    /**
     * Searches closest ghost to player.
     * @return Closest ghost to player
     */
    public Ghost getClosestGhost(){
        if (!isEmpty()){
            int gID = 0;
            double minLen = 10000;
            for (int i=0;i<this.size();i++){
                if (this.get(i).straightLen()<minLen){
                    minLen=this.get(i).straightLen();
                    gID=i;
                }
            }
            return this.get(gID);
        }
        return null;
    }
    /**
     * Makes ghosts do all their jobs every game tick.
     */
    public void update(){
        for (Ghost g : this) {
            g.update();
        }
    }
}
