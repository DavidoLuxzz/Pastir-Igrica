package com.lux.controls;

import com.lux.Main;
import com.lux.DynamicObject;
import com.lux.assets.AssetsManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class DialogBox implements DynamicObject {
    private ImageView boxView;
    private Main main;
    private Font font;
    private Text text;
    private int yOffset = 16; // offset between box and screen bottom
    
    private boolean textAnimFinished = false;
    private Timeline anim;
    
    private String[] currentDialog = {"* ..."};
    private int currentIndex;
    
    private int currentCharacterID = 0;

    public DialogBox(Main main){
        this.main = main;

        boxView = new ImageView(AssetsManager.getImage("dialog_box.png", 4, 2.5));
        boxView.setLayoutX(main.getScene().getWidth()/2 - boxView.getImage().getWidth()/2);
        boxView.setLayoutY(main.getScene().getHeight() - boxView.getImage().getHeight() - yOffset);
        
        font = AssetsManager.getFont("kongtext.ttf", 28);
        text = new Text("");
        text.setFont(font);
        text.setFill(Color.WHITE);
        text.relocate(boxView.getLayoutX() + 50, boxView.getLayoutY() + 50);
        
        boxView.setViewOrder(-3998);
        text.setViewOrder(-3999);
    }
    public void relocate(double x, double y){
        boxView.relocate(x, y);
    }
    public void setDialog(String[] log){
        currentDialog = log;
    }
    public void show(){
        if (!main.getRoot().getChildren().contains(boxView)){
            main.getRoot().getChildren().add(boxView);
            main.getRoot().getChildren().add(text);
            typingAnimation(currentDialog[currentIndex], 40).play();
        }
        main.getPlayer().setBusy(true);
    }
    public void hide(){
        if (main.getRoot().getChildren().contains(boxView)){
            main.getRoot().getChildren().remove(boxView);
            main.getRoot().getChildren().remove(text);
            currentIndex = 0;
            text.setText("");
        }
        main.getPlayer().setBusy(false);
    }
    
    private Timeline typingAnimation(String string, double millis){
        text.setText("");
        currentCharacterID = 0;
        textAnimFinished = false;
        anim = new Timeline(new KeyFrame(Duration.millis(millis), (e) -> {
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
    
    public void updatePos(double x, double y){
        if (main.getRoot().getChildren().contains(boxView)){
            boxView.setLayoutX(x + main.getDisplay().DEFAULT_WIDTH/2 - boxView.getImage().getWidth()/2);
            boxView.setLayoutY(y + main.getDisplay().getHeight()  - boxView.getImage().getHeight() - yOffset);
            if (main.getPlayer().getCenterY()+30 > boxView.getLayoutY())
                boxView.setLayoutY(y + yOffset);
            text.relocate(boxView.getLayoutX() + 50, boxView.getLayoutY() + 50);
        }
    }
    public void sendCloseRequest(){
        if (textAnimFinished) {
            if (currentIndex+1 < currentDialog.length){
                currentIndex++;
                typingAnimation(currentDialog[currentIndex], 40).play();
            } else
                hide();
        }
    }
    public void sendSkipRequest(){
        if (!textAnimFinished) {
            anim.stop();
            textAnimFinished = true;
            text.setText(currentDialog[currentIndex].replaceAll("ƒ", ""));
        }
    }
    public boolean isFinished(){
        return textAnimFinished;
    }
}
