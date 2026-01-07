package com.lux.level;

import com.lux.assets.AssetsManager;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;

public class Drawable extends ImageView {
    private int texture;
    private int group;  // group used to manage drawables easier
    private int layer;  // layer (greater layer value objects will be in front of lower layer value objects) try to keep value lower than 10
    private boolean solid; // drawable solid
    
    public static final String[] TEXTURES  	    = {"block","stone","grass","water","plank","plank_on_water","cobweb","plum","fence","door","door_open","room","shade"};
    public static final int TEXTURE_BLOCK  	    = 0;
    public static final int TEXTURE_STONE  		= 1;
    public static final int TEXTURE_GRASS  		= 2;
    public static final int TEXTURE_WATER  		= 3;
    public static final int TEXTURE_PLANK  		= 4;
    public static final int TEXTURE_PLANKW 		= 5;
    public static final int TEXTURE_COBWEB 		= 6;
    public static final int TEXTURE_PLUM   		= 7;
    public static final int TEXTURE_FENCE		= 8;
    public static final int TEXTURE_DOOR   		= 9;
    public static final int TEXTURE_DOOR_OPEN  	= 10;
    public static final int TEXTURE_ROOM		= 11;
    public static final int TEXTURE_SHADE		= 12;
    public static final int[]    SOLID_IDs  	= {0,3,7,8,9};
    
    public Drawable(int textureid, int group, int layer){
        super(AssetsManager.getImage(TEXTURES[textureid]+".png",4,4));
        this.texture = textureid;
        this.layer = layer;
        this.group  = group;
        solid = isSolid(textureid);
        
        setViewOrder(10 - layer); // entities will mostly have negative view order so 1 is enough for drawables
    }
    
    @Override
    public void relocate(double x, double y){
        setLayoutX(x);
        setLayoutY(y);
    }
    
    public Bounds getHitbox() {
    	return getBoundsInParent();
    }
    
    public double getMiniX(){
        return getLayoutX()/64;
    }
    public double getMiniY(){
        return getLayoutY()/64;
    }
    
    public int getSpecialValue() {
        return layer;
    }
    public int getGroup(){
        return group;
    }
    public int getTexture() {
        return texture;
    }
    public void setTexture(int texture) {
    	this.texture = texture;
    	setImage(AssetsManager.getImage(TEXTURES[texture]+".png", 4, 4));
    }
  
    
    public static boolean isSolid(int txtid){
        for (int b : SOLID_IDs){
            if (b==txtid)
                return true;
        }
        return false;
    }
    public void setSolid(boolean s) {    solid = s;   }
    public boolean isSolid() 		{  return solid;  }
    
    public static BoundingBox createHitbox(double x, double y, double w, double h){
        return new BoundingBox(x, y, w, h);
    }
}
