package com.lux;

import com.lux.controls.DialogBox;
import com.lux.level.Drawable;
import com.lux.level.NPCLoader;
import com.lux.level.Rooms;
import com.lux.level.SaveManager;
import com.lux.controls.MusicPlayer;
import com.lux.entity.Entities;
import com.lux.entity.Entity;
import com.lux.entity.Player;
import com.lux.level.Trigger;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private Rooms rooms; // all possible rooms stored in one list

    private int LEVEL_WIDTH;
    private int LEVEL_HEIGHT;
    
    private boolean zDown;
    
    private DialogBox dialogBox;
    
    private MusicPlayer music;
    private Player player;
    private Timeline timer;
    public int roomID;
    private SaveManager saveManager;
    
    private Entities entities;
    
    Display root;
    
    SceneType currentScene = SceneType.PLAY_SCREEN;
    
    @Override
    public void start(Stage stage) {     
        root = new Display();
        initKeyboardEvents();
        initComponents();
        
        player = new Player();
        player.relocate(70, 70); // default spawn position
        root.add(player);
        
        int[] saveData = saveManager.loadSave();
        if (saveData != null) {
        	System.out.println("Save data avaiable!");
        	roomID = saveData[0];
        	player.reqRelocate(saveData[1], saveData[2]);
        }
        
        rooms.load();
        rooms.updateNodeGroup(roomID);
        drawObjects();
        applyLevelDimensions();
        player.createCWOS(rooms.get(roomID));
        
        rooms.loadTriggers();
        System.out.println("Loaded triggers: "+rooms.getTriggers().size());
        rooms.loadRemovedTriggers(saveData);
        preexecuteTriggers(rooms.getRemovedTriggerIndices());
        rooms.removeRemovableTriggers();
        System.out.println("Removed triggers: "+rooms.getRemovedTriggerIndices().size());
        
        NPCLoader npcLoader = new NPCLoader();
        entities = npcLoader.load();
        entities.applyRoom(roomID);
        root.addAll(entities.getNodeGroup());
        
        root.addDynamics(rooms, entities);
                
        saveData = null;
        System.gc();
        
        music.play();
        gameLoop();
        
        stage.setTitle(Game.getTitle());
        root.setStage(stage);
        root.initDisplayEvents();
        stage.show();
    }
    
    private void initKeyboardEvents(){
        root.getScene().setOnKeyPressed((evt) -> {
            if (evt.getCode()!=null)switch(evt.getCode()){
                case ESCAPE:
                    timer.stop();
                    System.out.println("See you soon! :)");
                    System.exit(0);
                    break;
                case UP:
                case W:
                    player.up(1);
                    break;
                case DOWN:
                case S:
                    player.down(1);
                    break;
                case LEFT:
                case A:
                    player.left(1);
                    break;
                case RIGHT:
                case D:
                    player.right(1);
                    break;
                case T:
                    dialogBox.setDialog(Dialog.DIALOG_0);
                    dialogBox.show();
                    break;
                case Z:
                case ENTER:
                    if (dialogBox.isFinished() && player.isBusy()){
                        dialogBox.sendCloseRequest();
                    } else {
                        zDown = true;
                        checkEntityInteractions();
                    }
                    break;
                case SHIFT:
                case X:
                    if (!player.isBusy())
                        player.setSpeedMultiplier(1.5);
                    else if (!dialogBox.isFinished()){
                        dialogBox.sendSkipRequest();
                    }
                    break;
                case TAB:
                	if (!player.isBusy()) saveManager.save();
                	break;
                default:
                    break;
            }
        });
        root.getScene().setOnKeyReleased((evt) -> {
            if (evt.getCode()!=null)switch(evt.getCode()){
                case UP:
                case W:
                    player.up(0);
                    break;
                case DOWN:
                case S:
                    player.down(0);
                    break;
                case LEFT:
                case A:
                    player.left(0);
                    break;
                case RIGHT:
                case D:
                    player.right(0);
                    break;
                case Z:
                case ENTER:
                    zDown = false;
                    break;
                case SHIFT:
                case X:
                    player.setSpeedMultiplier(1.0);
                    break;
                default:
                    break;
            }
        });
    }
    
    private void initComponents(){
        music = new MusicPlayer("project.mp3");
        rooms = new Rooms();
        dialogBox = new DialogBox(this);
        saveManager = new SaveManager(this);
    }
    
    private void applyLevelDimensions() {
    	LEVEL_WIDTH  = rooms.getRoomSizes().get(roomID)[0];
    	LEVEL_HEIGHT = rooms.getRoomSizes().get(roomID)[1];
    	root.applyLevelDimensions(LEVEL_WIDTH, LEVEL_HEIGHT);
    }
    private void drawObjects() {
    	root.add(rooms.getNodeGroup());
    }
    
    private void forceChangeRoom(int room){
        root.clear();
        roomID = room;
        rooms.updateNodeGroup(room);
        drawObjects();
        applyLevelDimensions();
        root.add(player);
        player.updateCWOS(rooms.get(room));
        
        entities.applyRoom(room);
        root.addAll(entities.getNodeGroup());
    }
    private void smoothChangeRoom(int room, int[] spawn){
        root.startFade(500, 0.0, 1.0);
        player.setBusy(true);
        root.setOnFadeFinished((e) -> {
            forceChangeRoom(room);
            player.respawn(spawn);
            root.startFade(1000, 1.0, 0.0);
            player.setBusy(false);
        });
    }
    private void preexecuteTriggers(ArrayList<Integer> indices) {
    	for (int i : indices) {
    		Trigger tr = rooms.getTriggers().get(i);
	    	if (tr.getAction() != Event.TEXTURE_CHANGE) return;
	    	executeTrigger(tr);
    	}
    }
    private void executeTrigger(Trigger tr) {
    	switch (tr.getAction()){
	        case Event.DIALOG_INTERRUPT: // dialog interrupt
	            dialogBox.setDialog(Dialog.getDialog(tr.getSpecial()));
	            dialogBox.show();
	            break;
	        case Event.CHANGE_ROOM:      // change room
	            smoothChangeRoom(tr.getSpecial(), tr.getSpecial23());
	            return;
	        case Event.TEXTURE_CHANGE:
	        	for (Drawable drw : rooms.getObjectGroupWithID(tr.getSpecial())) {
	        		drw.setTexture(tr.getSpecial2());
	        		drw.setSolid(tr.getSpecial3() > 0);
	        		
	        		if (tr.getSpecial3() > 0)  player.addStaticObject(drw);
	        		else player.removeStaticObject(drw);
	        	}
	        	break;
	        default:
	            break;
	    }
    }
    private void checkTriggers(){
        if (!player.isBusy()){
            ArrayList<Trigger> toRemove = new ArrayList<>();
            for (Trigger tr : rooms.getTriggers()) {
                if (tr.getRoom() == roomID){  // trigger is in this room thus can be intersected with
                    if (player.getHitbox().intersects(tr.getHitbox())){
                        if (!tr.needsZ() || zDown) {
                            executeTrigger(tr);
                            if (tr.removeAfterInteraction()) {
                            	toRemove.add(tr);
                            	rooms.getRemovedTriggerIndices().add(rooms.getTriggers().indexOf(tr));
                            	tr = null;
                            }
                        }
                    }
                }
                if (player.isBusy()) break;
            }
            if (!toRemove.isEmpty()) rooms.getTriggers().removeAll(toRemove);
        }
    }
    private void checkEntityInteractions() {
    	if (!player.isBusy()) {
    		for (Entity e : entities) {
    			if (e.getRoom() == roomID)
		    		if (player.getExpandedHitbox().intersects(e.getExpandedHitbox())) {
		    			executeTrigger(e.getInteractionTrigger());
		    		}
    		}
    	}
    }
    // try to avoid drawable triggers,  use normal invisible triggers instead and add drawable a group
    
    private void gameLoop(){
        timer = new Timeline(new KeyFrame(Duration.millis(16.66666), (e) -> {
        	if (currentScene == SceneType.PLAY_SCREEN) {
	            // main game timeline
	            player.update(entities.getAllFromRoom(roomID));
	            player.configurateSpeed();
	            checkTriggers();
	            root.moveCamera(player);
	            
	            root.update();
	            //updateStatic();
        	}
        }));
        timer.setCycleCount(-1);
        timer.play();
    }
    
    public Player getPlayer(){
        return player;
    }
    public Pane getRoot(){
        return root.getRoot();
    }
    public Scene getScene(){
        return root.getScene();
    }
    public Display getDisplay(){
        return root;
    }
    public Entities getEntities() {
    	return entities;
    }
    public ArrayList<Integer> getRemovedTriggerIndices(){
    	return rooms.getRemovedTriggerIndices();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}