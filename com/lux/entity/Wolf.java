package com.lux.entity;

import com.lux.assets.AssetsManager;
import com.lux.level.Drawable;
import java.util.ArrayList;

public class Wolf extends Sprite {
    private Sprite tg;
    private double speed = 2.0D;
    private double lastX, lastY;
    private ArrayList<Drawable> cos, cws;
    
    public Wolf(){
        super(AssetsManager.getImage("entity/wolf.gif",0.5,0.5));
    }
    public Wolf(Sprite target){
        super(AssetsManager.getImage("entity/wolf.gif",0.5,0.5));
        tg=target;
    }
    public Wolf(Player target){
        super(AssetsManager.getImage("entity/wolf.gif",0.5,0.5));
        tg=target;
    }
    public Wolf(Player target, int xc, int yc){
        super(AssetsManager.getImage("entity/wolf.gif",0.5,0.5));
        tg=target;
        relocate(xc,yc);
        lastX = xc;
        lastY = yc;
        cos = new ArrayList<>();
        cws = new ArrayList<>();
    }
    
    public void createCWOS(ArrayList<Drawable> objects){
        for (Drawable drw : objects){
            if (Drawable.isSolid(drw.getTexture()))
                cos.add(drw);
            else if (drw.getTexture()==6)
                cws.add(drw);
        }
    }
    public void updateCWOS(ArrayList<Drawable> objects){
        cos.clear();
        cws.clear();
        createCWOS(objects);
    }
    /**
     * Sets new target to follow
     * @param target The target to follow
     */
    public void setTarget(Sprite target){
        this.tg=target;
    }
    /**
     * Does all jobs ghost needs to do in a game tick.
     */
    public void update(){
        follow(tg.getCenterX(),tg.getCenterY());
    }
    /**
     * Checks if there is collision. Used for wolf's A.I.
     * @param c Last (x/y) coordinate.
     */
    public void cc(int c){ // Check collision
        for (Drawable o : cos){
            if (o.getBoundsInParent().intersects(getBoundsInParent())){
                if (c==0)
                    setLayoutX(lastX);
                else
                    setLayoutY(lastY);
                return;
            }
        }
        if (c==0)
            lastX = getLayoutX();
        else
            lastY = getLayoutY();
    }
    public void smartMove(double x, double y){
        setCenterX(x);
        setCenterY(y);
        cc(0);
        cc(1);
    }
    /**
     * Better, faster and lighter version of <code>follow(x,y)</code> function.
     * Uses Pythagoras theoreme instead of heavy math (sin,cos,tan,...)
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
            smartMove(getCenterX()+dx*speed,getCenterY()+dy*speed);
            orientate(dx);
        }
    }
    /**
     * Calculates hypotenuse of the triangle, then checks if it is greater than 10.
     * @param a the delta x length
     * @param b the delta y length
     * @return if straight length if greater then 10
     */
    @SuppressWarnings("unused")
	private boolean isOutOfReach(double a, double b){ // Pitagorina teorema
        return Math.sqrt(a*a+b*b)>10;
    }
    public double xLen(){
        return tg.getCenterX()-this.getCenterX();
    }
    public double yLen(){
        return tg.getCenterY()-this.getCenterY();
    }
    public double straightLen(){
        return Math.sqrt(xLen()*xLen()+yLen()*yLen());
    }
}
