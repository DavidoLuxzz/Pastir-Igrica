package com.lux.level;

import com.lux.assets.AssetsManager;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;

//WARNING: this format is new, and thus do not rage quit
//	indices:	  0      1  2    3      4      5      6        7        8        9        10     
//format:  [ textureID  x  y  solid  layer  group  angle˚  scalex%  scaley%  special  special2 ]
public class Drawable extends ImageView {
	private int texture;
    private int group;  // group used to manage drawables easier
    private int layer;  // layer (greater layer value objects will be in front of lower layer value objects) try to keep value lower than 10
    private boolean solid; // drawable solid
    private int spec, spec2;
    
    public static final int MAX_COMPONENTS = 11; // last component id (spec2) + 1
    
    public static final int COMP_TEXTURE_ID = 0;
    public static final int COMP_X = 1;
    public static final int COMP_Y = 2;
    public static final int COMP_SOLID = 3;
    public static final int COMP_LAYER = 4;
    public static final int COMP_GROUP = 5;
    public static final int COMP_ANGLE = 6;
    public static final int COMP_SCALEX = 7;
    public static final int COMP_SCALEY = 8;
    public static final int COMP_SPECIAL = 9;
    public static final int COMP_SPECIAL2 = 10;
    
    public static final String[] TEXTURES = {"block","stone","grass","water","plank","plank_on_water","cobweb","plum","fence_full","door","door_open",
			 "room","shade","block_corner","block_top","block_topdown","block_end","black","fence_left","fence_right","fence_no_conn"};
    public static final int TEXTURE_BLOCK  	    = 0;
    public static final int TEXTURE_STONE  		= 1;
    public static final int TEXTURE_GRASS  		= 2;
    public static final int TEXTURE_WATER  		= 3;
    public static final int TEXTURE_PLANK  		= 4;
    public static final int TEXTURE_PLANKW 		= 5;
    public static final int TEXTURE_COBWEB 		= 6;
    public static final int TEXTURE_PLUM   		= 7;
    public static final int TEXTURE_FENCE_FULL	= 8;
    public static final int TEXTURE_DOOR   		= 9;
    public static final int TEXTURE_DOOR_OPEN  	= 10;
    public static final int TEXTURE_ROOM		= 11;
    public static final int TEXTURE_SHADE		= 12;
    public static final int TEXTURE_BLOCK_CORNER= 13;
    public static final int TEXTURE_BLOCK_TOP	= 14;
    public static final int TEXTURE_BLOCK_TOPDOWN=15;
    public static final int TEXTURE_BLOCK_END   = 16;
    public static final int TEXTURE_BLACK       = 17;
    public static final int TEXTURE_FENCE_LEFT	= 18;
    public static final int TEXTURE_FENCE_RIGHT	= 19;
    public static final int TEXTURE_FENCE_NO_CONN=20;
    public static final int[]    SOLID_IDs  	= {TEXTURE_BLOCK, TEXTURE_WATER, TEXTURE_PLUM, TEXTURE_FENCE_FULL, TEXTURE_DOOR,
    											   TEXTURE_BLOCK_CORNER, TEXTURE_BLOCK_TOP, TEXTURE_BLOCK_TOPDOWN, TEXTURE_BLOCK_END,
    											   TEXTURE_BLACK, TEXTURE_FENCE_LEFT, TEXTURE_FENCE_RIGHT, TEXTURE_FENCE_NO_CONN};
    
    /**
     * Old constructor
     * @param textureid
     * @param group
     * @param layer
     */
    public Drawable(int textureid, int group, int layer){
        super(AssetsManager.getImage(textureid));
        this.texture = textureid;
        this.layer = layer;
        this.group  = group;
        solid = isSolid(textureid);
    }
    
    public Drawable(int textureid, int group, int layer, boolean solid, int angle, int scalex, int scaley){
        super(AssetsManager.getImage(textureid));
        init(textureid, group, layer, solid, angle, scalex, scaley);
    }
    
    /**
     * Preferred constructor
     * @param data
     */
    public Drawable(int[] data) {
    	super(AssetsManager.getImage(data[COMP_TEXTURE_ID]));
    	init(data[COMP_TEXTURE_ID],data[COMP_GROUP],data[COMP_LAYER],data[COMP_SOLID]>0,data[COMP_ANGLE],data[COMP_SCALEX],data[COMP_SCALEY]);
    	relocate(data[COMP_X],data[COMP_Y]);
    	spec = data[COMP_SPECIAL];
    	spec2 = data[COMP_SPECIAL2];
    }
    
    // from level editor
    public void init(int textureid, int group, int layer, boolean solid, int angle, int scalex, int scaley){
        this.texture = textureid;
        this.layer = layer;
        this.group  = group;
        this.solid = solid;
        // solid = isSolid(textureid);
        
        setScaleX(scalex/100.0);
        setScaleY(scaley/100.0);
        
        setRotate(angle);
        // setManaged(false);
    }
    
    @Override
    public void relocate(double x, double y){
        setLayoutX(x);
        setLayoutY(y);
        // view order is defined in Layer object to which this belongs
        setViewOrder(-y*100);
        // if (texture==TEXTURE_FENCE_FULL) System.out.println("view_order: "+getViewOrder()+", layer="+layer+", texture="+texture);
    }
    
    public int getSpecial() {
    	return spec;
    }
    public int getSpecial2() {
    	return spec2;
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
    
    public int getLayer() {
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
