package com.lux;

import com.lux.assets.AssetsManager;
import com.lux.controls.DialogBox;
import com.lux.controls.TitleDisplayer;
import com.lux.controls.audio.AudioPlayer;
import com.lux.controls.audio.Song;
import com.lux.entity.Entities;
import com.lux.entity.Entity;
import com.lux.entity.Player;
import com.lux.level.NPCLoader;
import com.lux.level.Rooms;
import com.lux.level.SaveManager;
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
    public static Rooms rooms; // all possible rooms stored in one list

    private static int LEVEL_WIDTH;
    private static int LEVEL_HEIGHT;
    
    public static boolean zDown;
    
    // use static AudioPlayer.play<Song/Sound>(AudioPlayer.AUDIO_NAME)
    // private static AudioPlayer audio;
    public static Player player;
    private static Timeline timer;
    public static int roomID;
    public static String currentArea = "";
    // private SaveManager saveManager;
    
    private static Entities entities;
    
    static Display root;
    
    static SceneType currentScene = SceneType.PLAY_SCREEN;
    
    @Override
    public void start(Stage stage) {
        System.out.println("Starting game...");
    	AssetsManager.init();
    	
        root = new Display();
        
        AssetsManager.loadEntityImages();
        AssetsManager.loadImages(4,4);
        
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
        
        AudioPlayer.getSong(Song.BOZIC_JE).setVolume(0.0);
        AudioPlayer.playSong(Song.BOZIC_JE);
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
        TitleDisplayer.addToRoot();
    }
    
    
    private static int[] loadSaveData() {
    	int[] saveData = SaveManager.loadSave();
        if (saveData != null) {
        	System.out.println("Save data avaiable!");
        	roomID = saveData[0];
        	player.reqRelocate(saveData[1], saveData[2]);
        	entities.removeRemovable();
        }
        return saveData;
    }
    
    private static void initRooms() {
    	rooms.load();
        rooms.loadRoom(roomID);
        //drawObjects();
        applyLevelDimensions();
        player.createCWOS(rooms.get(roomID));
        
        rooms.loadTriggers();
// LOG: System.out.println("Loaded triggers: "+rooms.getTriggers().size());
        // rooms.loadRemovedTriggers(saveData); // this will be called in SaveManager.loadSave
        rooms.preexecuteTriggers(rooms.getRemovedTriggerIndices()); // this will be removed
        // trigger preexecution will be removed, and map change properties (saveData) will be added
// LOG: System.out.println("Removed triggers: "+rooms.getRemovedTriggerIndices().size());
    }
    
    private static void loadNPCs() {
        entities = NPCLoader.load();
        entities.applyRoom(roomID);
        // root.addAll(entities.getNodeGroup());
        
        root.addDynamics(rooms, entities);
    }
    
    private static void initKeyboardEvents(){
        root.getScene().setOnKeyPressed((evt) -> {
            if (evt.getCode()!=null) switch (evt.getCode()) {
                case ESCAPE -> {
                    timer.stop();
                    AudioPlayer.stopAll();
                    System.exit(0);
                }
                case UP, W -> player.up(1);
                case DOWN, S -> player.down(1);
                case LEFT, A -> player.left(1);
                case RIGHT, D -> player.right(1);
                case T -> {
                    DialogBox.setDialog(Dialog.DIALOG_0);
                    DialogBox.show();
                }
                case V -> {
                    System.out.println("Player view order: "+player.getViewOrder());
                    for (Entity e : entities.getAllFromRoom(roomID)) {
                        System.out.println("Entity view order: "+e.getViewOrder());
                    }
                }
                case Z, ENTER -> {
                    if (DialogBox.isFinished() && player.isBusy()){
                        DialogBox.sendCloseRequest();
                    } else {
                        zDown = true;
                        checkEntityInteractions();
                    }
                }
                case SHIFT, X -> {
                    if (!player.isBusy()) {
                        player.slash();
                    } else if (!DialogBox.isFinished()){
                        DialogBox.sendSkipRequest();
                    }
                }
                case C -> {
                    if (!player.isBusy() && player.inventory.contains(Item.NIKE_SHOES))
                        player.setSpeedMultiplier(Player.SPRINT_MULTIPLIER);
                }
                case TAB -> { if (!player.isBusy()) SaveManager.save(); }
                case I -> { if (!player.isBusy()) player.inventory.showInventory(); }
                case P -> {
                    System.out.println("----------------------------------");
                    for (Trigger t : rooms.getTriggers()) {
                        if (rooms.isTriggerRemoved(t)) System.out.print("(removed)");
                        System.out.println("Trigger: room: "+t.getRoom()+", action: " + t.getAction()+ ", x: "+t.getX()+", y: "+t.getY());
                    }
                }
                default -> {}
            }
        });
        root.getScene().setOnKeyReleased((evt) -> {
            if (evt.getCode()!=null) switch (evt.getCode()) {
                case UP, W -> player.up(0);
                case DOWN, S -> player.down(0);
                case LEFT, A -> player.left(0);
                case RIGHT, D -> player.right(0);
                case Z, ENTER -> zDown = false;
                case SHIFT, C -> player.setSpeedMultiplier(1.0);
                default -> {}
            }
        });
    }
    
    private static void initComponents(){
        rooms = new Rooms();
        AudioPlayer.loadAll();
        DialogBox.init();
        TitleDisplayer.init();
    }
    
    private static void applyLevelDimensions() {
    	LEVEL_WIDTH  = Rooms.getRoomSizes().get(roomID)[0];
    	LEVEL_HEIGHT = Rooms.getRoomSizes().get(roomID)[1];
    	root.applyLevelDimensions(LEVEL_WIDTH, LEVEL_HEIGHT);
    }
    private static void drawObjects() {
    	rooms.addCurrentRoomToRoot();
    }
    
    public static void forceChangeRoom(int room){
        root.clear();
        roomID = room;
        rooms.loadRoom(room);
        if (rooms.getArea(room).length()>0 && !rooms.getArea(room).equals(currentArea)) {
        	currentArea = rooms.getArea(room);
        	rooms.showAreaName(room);
        }
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
    
    private static void gameLoop() {
        timer = new Timeline(new KeyFrame(Duration.millis(16.66666), (e) -> {
            switch(currentScene) {
                case PLAY_SCREEN-> {
                    // main game timeline
                    player.update();
                    player.configurateSpeed();
                    rooms.checkTriggers();
                    root.moveCamera(player);
                    
                    root.update();
                    //updateStatic();
                }
                default -> {}
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