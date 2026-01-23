package com.lux;

import com.lux.assets.AssetsManager;
import com.lux.controls.DialogBox;
import com.lux.entity.Entity;
import com.lux.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Display {
    private Pane root;
    private Scene scene;
    private Stage stage;
    
    private ImageView staticBlur;
    private Rectangle opacityEffect;
    private boolean fadingFinished = true;
    private double fadeDelta;
    private int fadeDuration;
    private double fadeStart, fadeEnd;
    private EventHandler<?> fadeOnFinish;
    public static final int DEFAULT_WIDTH  = 1024;
    public static final int DEFAULT_HEIGHT = 768;
    
    private int LEVEL_WIDTH, LEVEL_HEIGHT;
    
    private double camX, camY;
    
    private ArrayList<DynamicObject> dynamicObjects;

    public Display(){
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Display(int width, int height){
        root = new Pane();
        root.setBackground(new Background(new BackgroundFill(null, null, null)));
        scene = new Scene(root, width, height, Color.BLACK);
        staticBlur = new ImageView(AssetsManager.getImage("static.gif",2.0D,2.0D));
        staticBlur.setOpacity(0);
        opacityEffect = new Rectangle(0, 0, 1056, 800);
        opacityEffect.setOpacity(0);
        
        staticBlur.setViewOrder(-9998); 	// put in front
        opacityEffect.setViewOrder(-9999);  //   - || -
        root.getChildren().addAll(staticBlur, opacityEffect);
        
        dynamicObjects = new ArrayList<>();
    }
    public Scene getScene(){
        return scene;
    }
    public Pane getRoot(){
        return root;
    }
    public void setStage(Stage stage){
        this.stage = stage;
        stage.setScene(scene);
        stage.setRenderScaleX(2.0);
    }
    
    public boolean contains(Node n){
        return root.getChildren().contains(n);
    }
    public void addAll(Node... e){
        root.getChildren().addAll(e);
    }
    public void addAll(Collection<? extends Node> e){
        root.getChildren().addAll(e);
    }
    public void add(Node n){
        root.getChildren().add(n);
    }
    public void clear(){
        root.getChildren().clear();
        root.getChildren().addAll(staticBlur, opacityEffect);
    }
    public void remove(Node n){
        root.getChildren().remove(n);
    }
    public int indexOf(Node n){
        return root.getChildren().indexOf(n);
    }
    public void addEntities(Collection<? extends Entity> es, int currentRoom) {
    	for (Entity e : es) {
    		if (e.getRoom() == currentRoom) root.getChildren().add(e);
    	}
    }
    
    public double getWidth(){
        return scene.getWidth();
    }
    public double getHeight(){
        return scene.getHeight();
    }
    
    public void setBlurLevel(double value){
        staticBlur.setOpacity(value);
    }
    
    public void update(){
        if (!fadingFinished) fadeAnimation();
    }

    public void fadeAnimation(){
        opacityEffect.setOpacity(opacityEffect.getOpacity()+fadeDelta);
        if (opacityEffect.getOpacity()==fadeEnd || (opacityEffect.getOpacity()<0.0 || opacityEffect.getOpacity()>1.0)){
            opacityEffect.setOpacity(fadeEnd);
            fadingFinished = true;
            if (fadeOnFinish != null){
                fadeOnFinish.handle(null);
                fadeOnFinish = null;
            }
        }
    }
    public boolean isFadingFinished(){
        return fadingFinished;
    }
    public void startFade(int duration, double start, double end){
        fadeDuration = duration;
        fadeStart = start;
        fadeEnd = end;
        int cycles = fadeDuration/20;  // 20 - interval milliseconds
        fadeDelta = (fadeEnd-fadeStart)/cycles;
        fadingFinished = false;
    }
    public void setOnFadeFinished(EventHandler<?> eh){
        fadeOnFinish = eh;
    }

    public void initDisplayEvents() {
        stage.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.floatValue()!=oldValue.floatValue()) {
                root.setLayoutX( (newValue.floatValue()-1024)/2 );
            }
        });
    }
    
    public void addDynamics(DynamicObject... os) {
    	for (DynamicObject o : os) addDynamic(o);
    }
    public void addDynamic(DynamicObject o) {
    	if (!dynamicObjects.contains(o)) dynamicObjects.add(o);
    }
    public void removeDynamic(DynamicObject o) {
    	if (dynamicObjects.contains(o))  dynamicObjects.remove(o);
    }
    public ArrayList<DynamicObject> getStatics() {
    	return dynamicObjects;
    }
    
    public void applyLevelDimensions(int width, int height) {
    	LEVEL_WIDTH = width;
    	LEVEL_HEIGHT = height;
    }
    
    public void moveCamera(Player player){
    	// camera coordinates
        double x = player.getRequestedX()-root.getWidth()/2;
        double y = player.getRequestedY()-root.getHeight()/2;
        // Fix x coords
        if ((x+root.getWidth())>LEVEL_WIDTH) x=LEVEL_WIDTH-root.getWidth();
        if (x<=0) x=0;
        // Fix y coords
        if ((y+root.getHeight())>LEVEL_HEIGHT) y=LEVEL_HEIGHT-root.getHeight();
        if (y<=0) y=0;
        
        camX = x;
        camY = y;
        player.setCameraPos(x, y);
        player.relocate(player.getRequestedX() - x, player.getRequestedY() - y);
        for (DynamicObject o : dynamicObjects) o.updatePos(-x, -y);
        DialogBox.updatePos(this, 0, 0);
    }
    public double getCameraX() {
    	return camX;
    }
    public double getCameraY() {
    	return camY;
    }
}
