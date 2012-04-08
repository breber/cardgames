package cs309.a1.shared;

import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class SoundManager {

	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = SoundManager.class.getName();


	/**
	 * This has a bunch of sounds that can be played
	 */
	private static SoundPool soundpool;

	/**
	 * This will play music or other sounds
	 */
	private static MediaPlayer mediaplayer;

	/**
	 * This is the code used to access a test sound
	 */
	private static int testSound;

	/**
	 * This is how we will read the names of the players out loud
	 */
	private static TextToSpeech tts;

	boolean isTTSInitialized;

	//constants for SoundManager

	/**
	 * "Hey (player) play a card"
	 * "Yo (player) you are needed."
	 * "(player) get a job."
	 * "(player) your turn."
	 * "Wake up (player) and smell the waffles."
	 * 
	 */
	public static final int NUM_TURN_STRINGS = 5;
	public static final String[] playerTurnBeforeName = {"Hey ","Yo ", " ", " ", "Wake up "};
	public static final String[] playerTurnAfterName = {" play a card!"," you are needed.", " get a job.", ", your turn.", " and smell the waffles."};



	/**
	 * @param context
	 */
	public SoundManager(Context context){
		soundpool = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
		mediaplayer = new MediaPlayer();
		testSound = soundpool.load(context, R.raw.sound_test, 1);
		mediaplayer = MediaPlayer.create(context, R.raw.sound_test);
		MyInitListener mil = new MyInitListener();
		tts = new TextToSpeech(context, mil);
		isTTSInitialized = false;

		//TODO this is where you add more sounds
	}



	/**
	 * This will play the test sound
	 */
	public static void playTestSound(){
		soundpool.play(testSound, 1, 1, 1, 0, 1);
	}

	public void playTesttts(){
		String test = "Hello, this is your text to speech library. Thank you for including me in your project.";
		speak(test);
	}

	/**
	 * This will use TextToSpeech to say the string out loud
	 * @param words
	 * this string will be read aloud
	 */
	public void speak(String words){
		if(isTTSInitialized){
			tts.speak(words, TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	/**
	 * This function will tell a player it is their turn using various strings
	 * @param name
	 * the name of the player
	 */
	public void sayTurn(String name){
		Random r1 = new Random();
		int i = Math.abs(r1.nextInt() % NUM_TURN_STRINGS);
		if (Util.isDebugBuild()) {
			Log.d(TAG, "Sure " + i);
		}
		String words = playerTurnBeforeName[i] + name + playerTurnAfterName[i];
		tts.speak(words, TextToSpeech.QUEUE_FLUSH, null);
	}



	/**
	 * This will play the theme music
	 */
	public static void playMusic(){
		//probably only do this on Tablet? since it would be really annoying to have
		//multiple songs going on
		//also this could be used to create sound effects for signaling whose turn it is.
		mediaplayer.seekTo(0);
		mediaplayer.start();
	}

	/**
	 * This will stop the music from playing
	 */
	public static void stopMusic(){
		if(mediaplayer.isPlaying()){
			mediaplayer.stop();
		}
	}

	/**
	 * This should make all sounds stop playing
	 */
	public void stopAllSound(){
		if(mediaplayer.isPlaying()){
			mediaplayer.stop();
		}
		soundpool.autoPause();
		tts.stop();
	}

	private class MyInitListener  implements TextToSpeech.OnInitListener{

		@Override
		public void onInit(int status) {
			if(status == TextToSpeech.SUCCESS){
				int langResult = tts.setLanguage(Locale.US);
				//TODO change locale to other things? like uk, french, german?
				if(langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED){
					if (Util.isDebugBuild()) {
						Log.d(TAG, "Language not available");
					}
				} else{
					isTTSInitialized = true;
				}
			} else if (Util.isDebugBuild()) {
				Log.d(TAG, "Text To Speech did not initialize correctly");
			}

		}

	}
}
