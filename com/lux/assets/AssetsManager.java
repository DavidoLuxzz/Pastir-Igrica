package com.lux.assets;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

public class AssetsManager {
    public static Image getImage(String asset, double scalex, double scaley){
        Image img = null;
        Image instance;
        try {
            instance = new Image(AssetsManager.class.getResource("/assets/"+asset).toExternalForm());
            img = new Image(AssetsManager.class.getResource("/assets/"+asset).toExternalForm(), scalex*instance.getWidth(), scaley*instance.getHeight(), false, false);
        } catch (Exception e) {
            System.err.println("ups");
        }
        instance = null;
        return img;
    }
    public static Image getImage(String asset, int scalex, int scaley){ // Needed to be getImage(String,double,double) but JFX ducked up
        Image img = null;
        Image instance;
        try {
            instance = new Image(AssetsManager.class.getResource("/assets/"+asset).toExternalForm());
            img = new Image(AssetsManager.class.getResource("/assets/"+asset).toExternalForm(), scalex*instance.getWidth(), scaley*instance.getHeight(), false, false);
        } catch (Exception e) {
            System.err.println("ups");
        }
        instance = null;
        return img;
    }
    public static Media getMusic(String song){
        Media mus = null;
        try {
            mus = new Media(AssetsManager.class.getResource("/assets/music/"+song).toExternalForm());
        } catch (Exception e) {
            System.err.println("song oopsie");
        }
        return mus;
    }
    public static Font getFont(String font, double size){
        Font f = null;
        try {
            f = Font.loadFont(AssetsManager.class.getResource("/assets/"+font).toExternalForm(), size);
        } catch (Exception e) {
            System.err.println("font upsei");
        }
        return f;
    }
}
