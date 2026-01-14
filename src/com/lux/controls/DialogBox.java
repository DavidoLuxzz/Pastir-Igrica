package com.lux.controls;

import com.lux.Display;
import com.lux.Main;
import com.lux.assets.AssetsManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class DialogBox {
    private static ImageView boxView;
    // private Main main;
    private static Text text;
    private final static int yOffset = 16; // offset between box and screen bottom
    
    private static boolean textAnimFinished = false;
    private static Timeline anim;
    
    private static String[] currentDialog = {"* ..."};
    private static int currentIndex = 0;
    
    private static int currentCharacterID = 0;
    
    public static void init(){
        // this.main = main;

        boxView = new ImageView(AssetsManager.DIALOGBOX);
        boxView.setLayoutX(Main.getScene().getWidth()/2 - boxView.getImage().getWidth()/2);
        boxView.setLayoutY(Main.getScene().getHeight() - boxView.getImage().getHeight() - yOffset);
        
        text = new Text("");
        text.setFont(AssetsManager.KONGTEXT);
        text.setFill(Color.WHITE);
        text.relocate(boxView.getLayoutX() + 50, boxView.getLayoutY() + 50);
        
        boxView.setViewOrder(-3998);
        text.setViewOrder(-3999);
        
        boxView.setVisible(false);
    	text.setVisible(false);
    }
    public void relocate(double x, double y){
        boxView.relocate(x, y);
    }
    public static void setDialog(String[] log){
        currentDialog = log;
    }
    public static void addToRoot(Display root) {
    	root.add(boxView);
    	root.add(text);
    }
    public static void show(){
        boxView.setVisible(true);
        text.setVisible(true);
        currentIndex = 0;
        typingAnimation(currentDialog[currentIndex], 40).play();
        Main.getPlayer().setBusy(true);
    }
    public static void hide(){
    	boxView.setVisible(false);
    	text.setVisible(false);
        currentIndex = 0;
        text.setText("");
        Main.getPlayer().setBusy(false);
        // System.out.println("hide");
    }
    
    private static Timeline typingAnimation(String string, double millis){
        text.setText("");
        currentCharacterID = 0;
        textAnimFinished = false;
        anim = new Timeline(new KeyFrame(Duration.millis(millis), (e) -> {
        	if (string.length()<1) return;
        	if (string.charAt(currentCharacterID) != 'ƒ')
        		text.textProperty().set(text.textProperty().get() + string.charAt(currentCharacterID));
    		currentCharacterID++;
        }));
        anim.setCycleCount(string.length());
        anim.setOnFinished((e) -> {
            textAnimFinished = true;
        });
        return anim;
    }
    
    public static void updatePos(Display root, double x, double y){
        boxView.setLayoutX(x + root.DEFAULT_WIDTH/2 - boxView.getImage().getWidth()/2);
        boxView.setLayoutY(y + root.getHeight()  - boxView.getImage().getHeight() - yOffset);
        if (Main.getPlayer().getCenterY()+30 > boxView.getLayoutY())
            boxView.setLayoutY(y + yOffset);
        text.relocate(boxView.getLayoutX() + 50, boxView.getLayoutY() + 50);
    }
    public static void sendCloseRequest(){
        if (textAnimFinished) {
            if (currentIndex+1 < currentDialog.length){
                currentIndex++;
                typingAnimation(currentDialog[currentIndex], 40).play();
            } else
                hide();
        }
    }
    public static void sendSkipRequest(){
        if (!textAnimFinished) {
            anim.stop();
            textAnimFinished = true;
            text.setText(currentDialog[currentIndex].replaceAll("ƒ", ""));
        }
    }
    public static boolean isFinished(){
        return textAnimFinished;
    }
}
