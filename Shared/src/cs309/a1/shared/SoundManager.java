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
	 * These are a joke, we can change them or honestly not have any vocal notification of it being your turn, it was just fun.
	 * "Hey (player) play a card"
	 * "Yo (player) you are needed."
	 * "(player) get a job."
	 * "(player) your turn."
	 * "Wake up (player) and smell the waffles."
	 */
	private static final int NUM_TURN_STRINGS = 5;
	private final String[] playerTurnBeforeName = {"Hey ","Yo ", " ", " ", "Wake up "};
	private final String[] playerTurnAfterName = {" play a card!"," you are needed.", " get a job.", ", your turn.", " and smell the waffles."};

	private int[] drawCardSounds = new int[7];
	private int[] playCardSounds = new int[5];
	private int[] shuffleCardSounds = new int[2];
	


	/**
	 * @param context
	 */
	public SoundManager(Context context){
		soundpool = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
		
		//draw card sounds
		drawCardSounds[0] = soundpool.load(context, R.raw.draw_card_1, 1);
		drawCardSounds[1] = soundpool.load(context, R.raw.draw_card_2, 1);
		drawCardSounds[2] = soundpool.load(context, R.raw.draw_card_3, 1);
		drawCardSounds[3] = soundpool.load(context, R.raw.draw_card_4, 1);
		drawCardSounds[4] = soundpool.load(context, R.raw.draw_card_5, 1);
		drawCardSounds[5] = soundpool.load(context, R.raw.draw_card_6, 1);
		drawCardSounds[6] = soundpool.load(context, R.raw.draw_card_7, 1);
		
		//card playing sounds
		playCardSounds[0] = soundpool.load(context, R.raw.play_card_1, 1);
		playCardSounds[1] = soundpool.load(context, R.raw.play_card_2, 1);
		playCardSounds[2] = soundpool.load(context, R.raw.play_card_3, 1);
		playCardSounds[3] = soundpool.load(context, R.raw.play_card_4, 1);
		playCardSounds[4] = soundpool.load(context, R.raw.play_card_5, 1);
		
		//card shuffling sounds
		shuffleCardSounds[0] = soundpool.load(context, R.raw.shuffling_cards_1, 1);
		shuffleCardSounds[1] = soundpool.load(context, R.raw.shuffling_cards_2, 1);
		
		
		mediaplayer = new MediaPlayer();
		MyInitListener mil = new MyInitListener();
		tts = new TextToSpeech(context, mil);
		isTTSInitialized = false;
		testSound = soundpool.load(context, R.raw.sound_test, 1);
		mediaplayer = MediaPlayer.create(context, R.raw.sound_test);
		
		
		

		//TODO this is where you add more sounds
	}



	/**
	 * This will play the test sound
	 */
	public void playTestSound(){
		soundpool.play(testSound, 1, 1, 1, 0, 1);
	}
	
	/**
	 * plays the sound of a card being drawn. plays various sounds.
	 */
	public void drawCardSound(){
		Random r1 = new Random();
		int i = Math.abs(r1.nextInt() % 7);
		soundpool.play(drawCardSounds[i], 1, 1, 1, 0, 1);
	}
	
	/**
	 * plays the sound of a card being played. plays various sounds.
	 */
	public void playCardSound(){
		Random r1 = new Random();
		int i = Math.abs(r1.nextInt() % 5);
		soundpool.play(playCardSounds[i], 1, 1, 1, 0, 1);
	}
	
	/**
	 * plays the sound of a card being played. plays various sounds.
	 */
	public void shuffleCardsSound(){
		Random r1 = new Random();
		int i = Math.abs(r1.nextInt() % 2);
		soundpool.play(shuffleCardSounds[i], 1, 1, 1, 0, 1);
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
	public void stopMusic(){
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
