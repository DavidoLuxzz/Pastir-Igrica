package com.lux.controls;

import com.lux.DynamicObject;
import com.lux.Main;
import com.lux.assets.AssetsManager;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;



/**
 * For now, unused
 */
public class Menu implements DynamicObject {

	private ImageView boxView;
    private Text text;
    private int yOffset = 16; // offset between box and screen bottom
	
	public Menu(Main main) {
		
		boxView = new ImageView(AssetsManager.DIALOGBOX);
        boxView.setLayoutX(Main.getScene().getWidth()/2 - boxView.getImage().getWidth()/2);
        boxView.setLayoutY(Main.getScene().getHeight() - boxView.getImage().getHeight() - yOffset);
        
        text = new Text("");
        text.setFont(AssetsManager.KONGTEXT);
        text.setFill(Color.WHITE);
        text.relocate(boxView.getLayoutX() + 50, boxView.getLayoutY() + 50);
        
        boxView.setViewOrder(-3998);
        text.setViewOrder(-3999);
		
	}
	
	public void updatePos(double x, double y){
        if (Main.getRoot().getChildren().contains(boxView)){
            boxView.setLayoutX(x + Main.getDisplay().DEFAULT_WIDTH/2 - boxView.getImage().getWidth()/2);
            boxView.setLayoutY(y + Main.getDisplay().getHeight()  - boxView.getImage().getHeight() - yOffset);
            if (Main.getPlayer().getCenterY()+30 > boxView.getLayoutY())
                boxView.setLayoutY(y + yOffset);
            text.relocate(boxView.getLayoutX() + 50, boxView.getLayoutY() + 50);
        }
    }

}
