package com.lux;

import com.lux.controls.DialogBox;
import com.lux.level.NPCLoader;
import com.lux.level.Rooms;
import com.lux.level.SaveManager;
import com.lux.level.Trigger;
import com.lux.controls.MusicPlayer;
import com.lux.entity.Entities;
import com.lux.entity.Entity;
import com.lux.entity.Player;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    public static Rooms rooms; // all possible rooms stored in one list

    private static int LEVEL_WIDTH;
    private static int LEVEL_HEIGHT;
    
    public static boolean zDown;
    
    private static MusicPlayer music;
    public static Player player;
    private static Timeline timer;
    public static int roomID;
    // private SaveManager saveManager;
    
    private static Entities entities;
    
    static Display root;
    
    static SceneType currentScene = SceneType.PLAY_SCREEN;
    
    @Override
    public void start(Stage stage) {     
        root = new Display();
        initKeyboardEvents();
        initComponents();
        
        player = new Player();
        player.relocate(70, 70); // default spawn position
        //root.add(player);
        //root.add(player.getSlashAnimation());
        
        loadNPCs();
        loadSaveData();
        initRooms();
        
        

        System.gc();
        
        forceChangeRoom(roomID);
        
        music.play();
        gameLoop();
        
        stage.setTitle(Game.getTitle());
        root.setStage(stage);
        root.initDisplayEvents();
        stage.show();
    }
    
    
    private static void addAllToRoot() {
    	drawObjects();
        root.add(player);
        root.add(player.getSlashAnimation());
        
        root.addAll(entities.getNodeGroup());
        DialogBox.addToRoot(root);
    }
    
    
    private static int[] loadSaveData() {
    	int[] saveData = SaveManager.loadSave();
        if (saveData != null) {
        	System.out.println("Save data avaiable!");
        	roomID = saveData[0];
        	player.reqRelocate(saveData[1], saveData[2]);
        	if (player.getInventory().contains(Item.NIKE_SHOES))
        		player.loadPlayerTiles(true);
        	entities.removeRemovable();
        }
        return saveData;
    }
    
    private static void initRooms() {
    	rooms.load();
        rooms.updateNodeGroup(roomID);
        //drawObjects();
        applyLevelDimensions();
        player.createCWOS(rooms.get(roomID));
        
        rooms.loadTriggers();
        System.out.println("Loaded triggers: "+rooms.getTriggers().size());
        // rooms.loadRemovedTriggers(saveData); // this will be called in SaveManager.loadSave
        rooms.preexecuteTriggers(rooms.getRemovedTriggerIndices()); // this will be removed
        // trigger preexecution will be removed, and map change properties (saveData) will be added
        System.out.println("Removed triggers: "+rooms.getRemovedTriggerIndices().size());
    }
    
    private static void loadNPCs() {
        entities = NPCLoader.load();
        entities.applyRoom(roomID);
        // root.addAll(entities.getNodeGroup());
        
        root.addDynamics(rooms, entities);
    }
    
    private static void initKeyboardEvents(){
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
                    DialogBox.setDialog(Dialog.DIALOG_0);
                    DialogBox.show();
                case Z:
                case ENTER:
                    if (DialogBox.isFinished() && player.isBusy()){
                        DialogBox.sendCloseRequest();
                    } else {
                        zDown = true;
                        checkEntityInteractions();
                    }
                    break;
                case SHIFT:
                case X:
                    if (!player.isBusy()) {
                    	player.slash(); // will check first if has an ability to slash
            		} else if (!DialogBox.isFinished()){
                        DialogBox.sendSkipRequest();
                    }
                    break;
                case C:
                	if (!player.isBusy() && player.inventory.contains(Item.NIKE_SHOES))
                		player.setSpeedMultiplier(Player.SPRINT_MULTIPLIER);
                	break;
                case TAB:
                	if (!player.isBusy()) SaveManager.save();
                	break;
                case I:
                	if (!player.isBusy()) player.inventory.showInventory();
                	break;
                case P:
                	System.out.println("----------------------------------");
                	for (Trigger t : rooms.getTriggers()) {
                		if (rooms.isTriggerRemoved(t)) System.out.print("(removed)");
                    	System.out.println("Trigger: room: "+t.getRoom()+", action: " + t.getAction()+ ", x: "+t.getX()+", y: "+t.getY());
                    }
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
                case C:
                    player.setSpeedMultiplier(1.0);
                    break;
                default:
                    break;
            }
        });
    }
    
    private static void initComponents(){
        music = new MusicPlayer("project.mp3");
        rooms = new Rooms();
        DialogBox.init();
    }
    
    private static void applyLevelDimensions() {
    	LEVEL_WIDTH  = Rooms.getRoomSizes().get(roomID)[0];
    	LEVEL_HEIGHT = Rooms.getRoomSizes().get(roomID)[1];
    	root.applyLevelDimensions(LEVEL_WIDTH, LEVEL_HEIGHT);
    }
    private static void drawObjects() {
    	root.add(rooms.getNodeGroup());
    }
    
    public static void forceChangeRoom(int room){
        root.clear();
        roomID = room;
        rooms.updateNodeGroup(room);
        player.updateCWOS(rooms.get(room));
        entities.applyRoom(room);
        addAllToRoot();
        applyLevelDimensions();
    }
    public static void smoothChangeRoom(int room, int[] spawn){
        root.startFade(500, 0.0, 1.0);
        player.setBusy(true);
        root.setOnFadeFinished((e) -> {
            forceChangeRoom(room);
            player.respawn(spawn);
            root.startFade(1000, 1.0, 0.0);
            player.setBusy(false);
        });
    }
    
    private static void checkEntityInteractions() {
    	if (!player.isBusy()) {
    		for (Entity e : entities.getNodeGroup()) {
	    		if (player.getExpandedHitbox().intersects(e.getExpandedHitbox())) {
	    			e.getInteractionTrigger().execute(e);
	    		}
    		}
    		entities.removeRemovable();
    	}
    }
    // try to avoid drawable triggers,  use normal invisible triggers instead and add drawable a group
    
    private static void gameLoop(){
        timer = new Timeline(new KeyFrame(Duration.millis(16.66666), (e) -> {
        	if (currentScene == SceneType.PLAY_SCREEN) {
	            // main game timeline
	            player.update(entities.getNodeGroup());
	            player.configurateSpeed();
	            rooms.checkTriggers();
	            root.moveCamera(player);
	            
	            root.update();
	            //updateStatic();
        	}
        }));
        timer.setCycleCount(-1);
        timer.play();
    }
    
    public static Player getPlayer(){
        return player;
    }
    public static Pane getRoot(){
        return root.getRoot();
    }
    public static Scene getScene(){
        return root.getScene();
    }
    public static Display getDisplay(){
        return root;
    }
    public static Entities getEntities() {
    	return entities;
    }
    public static ArrayList<Integer> getRemovedTriggerIndices(){
    	return rooms.getRemovedTriggerIndices();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}