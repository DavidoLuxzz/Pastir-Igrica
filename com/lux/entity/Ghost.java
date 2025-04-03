package com.lux.entity;

import com.lux.assets.AssetsManager;

public class Ghost extends Sprite {
    private Sprite tg; // target
    private double speed = 2.0D;
    private boolean chasing;
    private String[] interactionText = {"It's just a ghost..."};
    
    public Ghost(){
        super(AssetsManager.getImage("entity/ghost.png",4,4));
    }
    public Ghost(Sprite target){
        super(AssetsManager.getImage("entity/ghost.png",4,4));
        tg=target;
    }
    public Ghost(Player target){
        super(AssetsManager.getImage("entity/ghost.png",4,4));
        tg=target;
    }
    public Ghost(Player target, int xc, int yc){
        super(AssetsManager.getImage("entity/ghost.png",4,4));
        tg=target;
        relocate(xc,yc);
    }
    public Ghost(Player target, int xc, int yc, String[] interactionText){
        super(AssetsManager.getImage("entity/ghost.png",4,4));
        tg=target;
        relocate(xc,yc);
        this.interactionText = interactionText;
    }
    /**
     * Sets new target to follow
     * @param target The target to follow
     */
    public void setTarget(Sprite target){
        this.tg=target;
    }
    public Sprite getTarget(){
        return tg;
    }
    /**
     * Does all jobs ghost needs to do in a game tick.
     */
    public void update(){
        if (chasing) follow(tg.getCenterX(),tg.getCenterY());
    }
    //  Removed  followHeavy(x, y)
    /**
     * Better, faster and lighter version of <code>follow(x,y)</code> function.
     * Uses Pythagoras theoreme instead of heavy math (sin,cos,tan,...)
     * The only heavy math function is Math.sqrt().
     * @param x The x coordinate of the target
     * @param y The y coordinate of the target
     */
    private void follow(double x, double y){
        double a  = x-getCenterX();  // triangle width
        double b = y-getCenterY(); // triangle height
        double c = Math.sqrt(a*a+b*b);
        double dx = 0;
        if (a!=0) dx = a/c;
        double dy = 0;
        if (b!=0) dy = b/c;
        
        if (c>10){
            setCenterX(getCenterX()+dx*speed);
            setCenterY(getCenterY()+dy*speed);
            orientate(dx);
        }
    }
    public double xLen(){
        return tg.getCenterX()-this.getCenterX();
    }
    public double yLen(){
        return tg.getCenterY()-this.getCenterY();
    }
    public double straightLen(){
        double xl = xLen();
        double yl = yLen();
        return Math.sqrt(xl*xl + yl*yl);
    }
    public void setChasing(boolean c){
        chasing = c;
    }
    public boolean isChasing(){
        return chasing;
    }
    public void setInteractionText(String[] interactionText){
        this.interactionText = interactionText;
    }
    public String[] getInteractionText(){
        return interactionText;
    }
}
