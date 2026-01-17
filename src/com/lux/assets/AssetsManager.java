package com.lux.assets;

import com.lux.level.Drawable;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

public class AssetsManager {
	
	public static String ASSETS_DIRECTORY = "file:"+System.getProperty("user.home")+"/eclipse-workspace/Pastir-igrica/assets/";
	
	public static void init() {}
	
	// todo When building JAR executable
	// public static URL getResource(String res);
	
	public static String getAbsoluteAssetURIPath(String asset) {
		return ASSETS_DIRECTORY+asset;
	}
	
	
	public static Image[] images;
	
	public static void loadImages(int scalex, int scaley) {
		images = new Image[Drawable.TEXTURES.length];
		int i = 0;
		for (String name : Drawable.TEXTURES) {
			images[i++] = getImage(name+".png", scalex, scaley);
		}
	}
	public static Image getImage(int i) {
		return images[i];
	}
	
	public static Image[] ENTITY_IMAGES = null;
	
	public static final int ENTITY_IMAGE_SHEEP = 0;
	public static final int ENTITY_IMAGE_NIKE_SHOES = 1;
	
	public static void loadEntityImages() {
		if (ENTITY_IMAGES != null) {
			System.err.println("Tried to regenerate entity image list.");
			return;
		}
		ENTITY_IMAGES = new Image[] {getImage("sheep.png",3,3),
									getImage("nike_shoes.png",1,1)};
	}
	public static Image getEntityImage(int img) {
		return ENTITY_IMAGES[img];
	}
	
	public static final Image DIALOGBOX = AssetsManager.getImage("dialog_box.png", 4, 2.5);
    public static Image getImage(String asset, double scalex, double scaley){
        Image img = null;
        Image instance;
        try {
            instance = new Image(getAbsoluteAssetURIPath(asset));
            img = new Image(getAbsoluteAssetURIPath(asset), scalex*instance.getWidth(), scaley*instance.getHeight(), false, false);
        } catch (Exception e) {
            System.err.println("ups: "+asset);
        }
        instance = null;
        return img;
    }
    public static Media getMusic(String song){
        Media mus = null;
        try {
            mus = new Media(getAbsoluteAssetURIPath("audio/"+song));
        } catch (Exception e) {
            System.err.println("song oopsie");
        }
        return mus;
    }
    public static final Font KONGTEXT = AssetsManager.getFont("kongtext.ttf", 28);
    public static Font getFont(String font, double size){
        Font f = null;
        try {
            f = Font.loadFont(getAbsoluteAssetURIPath(font), size);
        } catch (Exception e) {
            System.err.println("font upsei");
        }
        return f;
    }
}
