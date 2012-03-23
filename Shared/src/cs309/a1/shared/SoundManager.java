package cs309.a1.shared;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundManager {
	
	private static SoundPool soundpool;
	private static MediaPlayer mediaplayer;
	private static int testSound;
	
	
	public static void initSounds(Context context){
		soundpool = new SoundPool(5, AudioManager.STREAM_MUSIC, 100); 
		mediaplayer = new MediaPlayer();
		testSound = soundpool.load(context, R.raw.sound_test, 1);
		mediaplayer = MediaPlayer.create(context, R.raw.sound_test);
		
		//TODO this is where you add more sounds
	}
	
	//make a method like this for each sound we want to play
	//this may only work with shorter sounds. it did not play the whole test
	public static void playTestSound(){
		soundpool.play(testSound, 1, 1, 1, 0, (float) 1);
	}
	
	public static void playMusic(){
		//probably only do this on Tablet? since it would be really annoying to have 
		//multiple songs going on
		//also this could be used to create sound effects for signaling whose turn it is.
		mediaplayer.seekTo(0);
		mediaplayer.start();
	}
	
	public static void stopMusic(){
		if(mediaplayer.isPlaying()){
			mediaplayer.stop();
		}
	}
}
