package com.lux.controls.audio;

public class AudioPlayer {
	
	private static Song[] songs = null;
	private static Sound[] sounds = null;
	
	public static void loadAll() {
		loadSongs();
		loadSounds();
	}

	private static void loadSongs() {
		songs = new Song[Song.SONG_NAMES.length];
		int i = 0;
		for (String name : Song.SONG_NAMES) {
			songs[i++] = new Song(name);
		}
	}
	
	private static void loadSounds() {
		sounds = new Sound[Sound.SOUND_NAMES.length];
		int i = 0;
		for (String name : Sound.SOUND_NAMES) {
			sounds[i++] = new Sound("snd/"+name);
		}
	}
	
	public static void stopAll() {
		stopAllSongs();
		stopAllSounds();
	}
	
	// songs
	public static Song getSong(int songID) {
		if (songID<0 || songID>=songs.length) System.err.println("Invalid song ID.");
		return songs[songID];
	}
	public static void playSong(int songID) {
		getSong(songID).play();
	}
	public static void playSong(int songID, boolean loop) {
		getSong(songID).setLoop(loop);
		getSong(songID).play();
	}
	public static void switchSong(int songID) {
		stopAllSongs();
		playSong(songID);
	}
	public static void stopSong(int songID) {
		getSong(songID).stop();
	}
	public static void stopAllSongs() {
		for (Song s : songs) s.stop();
	}
	
	// sounds
	public static Sound getSound(int soundID) {
		if (soundID<0 || soundID>=sounds.length) System.err.println("Invalid sound ID.");
		return sounds[soundID];
	}
	public static void playSound(int soundID) {
		getSound(soundID).play();
	}
	public static void playSound(int soundID, boolean loop) {
		getSound(soundID).play(loop);
	}
	public static void switchSound(int soundID) {
		stopAllSounds();
		playSound(soundID);
	}
	public static void stopSound(int soundID) {
		getSound(soundID).stop();
	}
	public static void stopAllSounds() {
		for (Sound s : sounds) s.stop();
	}
}
