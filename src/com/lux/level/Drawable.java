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
    
    public static final String[] TEXTURES = {
        "block","stone", "grass","water", "plank","plank_on_water", "cobweb","plum","fence_full",
        "door","door_open","room","shade","block_corner","block_top","block_topdown","block_end",
        "black", "fence_left","fence_right","fence_no_conn", "grass0","grass1","grass2","grass3",
        "grass4","grass5", "grass6","grass7", "grass8","grass9", "grass10", "grass11", "grass12",
        "grass13", "grass14", "grass15", "dirt0","dirt1","cobble0","cobble1","cobble2","cobble3",
        "cobble4","cobble5","cobble6","cobble7"
    };
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
    public static final int TEXTURE_GRASSX      = 21;
    public static final int GRASS_TEXTURE_COUNT = 16;
    public static final int TEXTURE_DIRT0       = 37;
    public static final int TEXTURE_DIRT1       = 38;
    public static final int TEXTURE_COBBLEX     = 39;
    public static final int COBBLE_TEXTURE_COUNT= 8;
    public static final int[]    SOLID_IDs  	= {TEXTURE_BLOCK, TEXTURE_WATER, TEXTURE_PLUM, TEXTURE_FENCE_FULL, TEXTURE_DOOR,
    											   TEXTURE_BLOCK_CORNER, TEXTURE_BLOCK_TOP, TEXTURE_BLOCK_TOPDOWN, TEXTURE_BLOCK_END,
    											   TEXTURE_BLACK, TEXTURE_FENCE_LEFT, TEXTURE_FENCE_RIGHT, TEXTURE_FENCE_NO_CONN};
    // 0,7,8,9,13,14,15,16,18,19,20
    
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
     * @param data array in format defined at the top of the class
     */
    public Drawable(int[] data) {
    	super(AssetsManager.getImage(data[COMP_TEXTURE_ID]));
    	init(data[COMP_TEXTURE_ID],data[COMP_GROUP],data[COMP_LAYER],data[COMP_SOLID]>0,data[COMP_ANGLE],data[COMP_SCALEX],data[COMP_SCALEY]);
    	relocate(data[COMP_X],data[COMP_Y]);
    	spec = data[COMP_SPECIAL];
    	spec2 = data[COMP_SPECIAL2];
    }
    
    // from level editor
    private void init(int textureid, int group, int layer, boolean solid, int angle, int scalex, int scaley){
        setSmooth(false);
        this.texture = textureid;
        this.layer = layer;
        this.group  = group;
        this.solid = solid;
        
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
        if (layer == Layer.HEIGHTENED) { // has custom height
        	setViewOrder(-(getTranslateY()+y+70)); // 64 + 6 = height + visualy_nice_offset
        }
        // if (texture==TEXTURE_FENCE_FULL) System.out.println("view_order: "+getViewOrder()+", layer="+layer+", texture="+texture);
    }
    public void translate(double x, double y) {
    	setTranslateX(x);
    	setTranslateY(y);
    	
    	if (layer == Layer.HEIGHTENED) {
        	setViewOrder(-(getLayoutY()+y+70)); // 64 + 6 = height + visualy_nice_offset
        }
    }
    
    public int getSpecial() {
    	return spec;
    }
    public int getSpecial2() {
    	return spec2;
    }
    
    // private static final double HEIGHTENED_HITBOX_Y_SCALE = 0.6;
    // private static final double HEIGHTENED_FENCE_Y_SCALE = 0.2;
    private static final double HEIGHTENED_FENCE_X_SCALE = 0.6; // vece od 0.5 ili se prave sranja (zbog TEXTURE_FENCE_NO_CONN)
    /**
     * Call this with (params = 1.0) <=> layer!=heightened
     * or
     * Call this with custom params <= layer==heightened   (not <=>, ajde bre ucio si ovo u prvoj godini)
     * @param HEIGHTENED_HITBOX_Y_SCALE
     * @param HEIGHTENED_FENCE_Y_SCALE
     * @return bounds
     */
    private Bounds getCustomHitboxForThisTexture(double HEIGHTENED_HITBOX_Y_SCALE, double HEIGHTENED_FENCE_Y_SCALE) {
    	switch (texture) {
    	case TEXTURE_FENCE_LEFT -> {
    		double x = getLayoutX();
        	double y = getLayoutY()+(1.0-HEIGHTENED_FENCE_Y_SCALE)*getImage().getHeight();
        	return createHitbox(x, y, getImage().getWidth()*HEIGHTENED_FENCE_X_SCALE, getImage().getHeight()*HEIGHTENED_FENCE_Y_SCALE);
    	}
    	case TEXTURE_FENCE_RIGHT -> {
    		double x = getLayoutX()+(1.0-HEIGHTENED_FENCE_X_SCALE)*getImage().getWidth();
        	double y = getLayoutY()+(1.0-HEIGHTENED_FENCE_Y_SCALE)*getImage().getHeight();
        	return createHitbox(x, y, getImage().getWidth()*HEIGHTENED_FENCE_X_SCALE, getImage().getHeight()*HEIGHTENED_FENCE_Y_SCALE);
    	}
    	case TEXTURE_FENCE_NO_CONN -> {
    		double dx = (1.0-HEIGHTENED_FENCE_X_SCALE)*getImage().getWidth();
    		double x = getLayoutX()+dx;
        	double y = getLayoutY()+(1.0-HEIGHTENED_FENCE_Y_SCALE)*getImage().getHeight();
        	return createHitbox(x, y, getImage().getWidth()-(2*dx), getImage().getHeight()*HEIGHTENED_FENCE_Y_SCALE);
    	}
    	case TEXTURE_FENCE_FULL -> {
    		double x = getLayoutX();
        	double y = getLayoutY()+(1.0-HEIGHTENED_FENCE_Y_SCALE)*getImage().getHeight();
        	return createHitbox(x, y, getImage().getWidth(), getImage().getHeight()*HEIGHTENED_FENCE_Y_SCALE);
    	}
		default -> {
			double x = getLayoutX();
	    	double y = getLayoutY()+(1.0-HEIGHTENED_HITBOX_Y_SCALE)*getImage().getHeight();
	    	return createHitbox(x, y, getImage().getWidth(), getImage().getHeight()*HEIGHTENED_HITBOX_Y_SCALE);
		}
    	}
    }
    
    
    public Bounds getHitbox() {
    	if (layer!=Layer.HEIGHTENED)
    		return getCustomHitboxForThisTexture(1.0,1.0);
    	
    	return getCustomHitboxForThisTexture(0.6, 0.2);
    }
    public Bounds getBounds() {
    	if (layer!=Layer.HEIGHTENED)
    		return getBoundsInParent();
    	return createHitbox(getLayoutX(), getLayoutY(), getImage().getWidth(), getImage().getHeight());
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
    
    public static BoundingBox createHitbox(double minx, double miny, double w, double h){
        return new BoundingBox(minx, miny, w, h);
    }
}
