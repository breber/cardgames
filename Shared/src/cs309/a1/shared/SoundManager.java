package cs309.a1.shared;

import static cs309.a1.shared.Constants.LANGUAGE;
import static cs309.a1.shared.Constants.LANGUAGE_CANADA;
import static cs309.a1.shared.Constants.LANGUAGE_FRANCE;
import static cs309.a1.shared.Constants.LANGUAGE_GERMAN;
import static cs309.a1.shared.Constants.LANGUAGE_UK;
import static cs309.a1.shared.Constants.LANGUAGE_US;
import static cs309.a1.shared.Constants.PREFERENCES;
import static cs309.a1.shared.Constants.SOUND_EFFECTS;
import static cs309.a1.shared.Constants.SPEECH_VOLUME;

import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;
import android.util.Log;

/**
 * This class is used to control the all of the sound in the game.
 * It has several methods for playing specific sounds and for using 
 * the Text To Speech feature of android
 */
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

	/**
	 * This is how we can tell if the TTS has been initialized
	 */
	boolean isTTSInitialized;

	/**
	 * This will be obtained from the shared preference to see if the player wants sound fx
	 */
	private boolean isSoundFXOn = true;

	/**
	 * This will be obtained from the shared preference to see if the player wants to have TTS
	 */
	private boolean isTTSOn = true;

	/**
	 * This is how we will get the settings that the user has set.
	 */
	private SharedPreferences sharedPreferences;


	/**
	 * These are a joke, we can change them or honestly not have any vocal notification of it being your turn, it was just fun.
	 * "Hey (player) play a card"
	 * "Yo (player) you are needed."
	 * "(player) get a job."
	 * "(player) your turn."
	 * "Wake up (player) and smell the waffles."
	 */
	private static final int NUM_TURN_STRINGS = 5;
	
	/**
	 * Strings to be added before a player's name when announcing his or her turn
	 */
	private final String[] playerTurnBeforeName = {"Hey ","Yo ", " ", " ", "Wake up "};
	
	/**
	 * Strings to be added after a player's name when announcing his or her turn
	 */
	private final String[] playerTurnAfterName = {" play a card!"," you are needed.", " get a job.", ", your turn.", " and smell the waffles."};

	/**
	 * array to store the soundpool IDs for the draw card sounds
	 */
	private int[] drawCardSounds = new int[7];
	
	/**
	 * array to store the soundpool IDs for the play card sounds
	 */
	private int[] playCardSounds = new int[5];
	
	/**
	 * array to store the soundpool IDs for the shuffle card sounds
	 */
	private int[] shuffleCardSounds = new int[2];



	/**
	 * this will initialize the SoundManager by initializing all the sound FX and the TTS object and obtaining user sound preferences
	 * @param context
	 * The context of the class to use the SoundManager
	 */
	public SoundManager(Context context){
		sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_WORLD_WRITEABLE);
		isSoundFXOn = sharedPreferences.getBoolean(SOUND_EFFECTS, true);
		isTTSOn = sharedPreferences.getBoolean(SPEECH_VOLUME, true);

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
		tts = new TextToSpeech(context.getApplicationContext(), mil);
		isTTSInitialized = false;
		testSound = soundpool.load(context, R.raw.sound_test, 1);
		mediaplayer = MediaPlayer.create(context, R.raw.sound_test);


	}

	/**
	 * plays the sound of a card being drawn. plays various sounds.
	 */
	public void drawCardSound(){
		if(isSoundFXOn){
			Random r1 = new Random();
			int i = Math.abs(r1.nextInt() % 7);
			soundpool.play(drawCardSounds[i], 1, 1, 1, 0, 1);
		}
	}

	/**
	 * plays the sound of a card being played. plays various sounds.
	 */
	public void playCardSound(){
		if(isSoundFXOn){
			Random r1 = new Random();
			int i = Math.abs(r1.nextInt() % 5);
			soundpool.play(playCardSounds[i], 1, 1, 1, 0, 1);
		}
	}

	/**
	 * plays the sound of a card being played. plays various sounds.
	 */
	public void shuffleCardsSound(){
		if(isSoundFXOn){
			Random r1 = new Random();
			int i = Math.abs(r1.nextInt() % 2);
			soundpool.play(shuffleCardSounds[i], 1, 1, 1, 0, 1);
		}
	}

	/**
	 * This will use TextToSpeech to say the string out loud
	 * @param words
	 * this string will be read aloud
	 */
	public void speak(String words){
		if(isTTSInitialized && isTTSOn){
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
	public void playMusic(){
		//probably only do this on Tablet? since it would be really annoying to have
		//multiple songs going on
		if(isSoundFXOn){
			mediaplayer.seekTo(0);
			mediaplayer.start();
		}
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

	/**
	 * this class will have the onInit method called when the TTS has been initialized then 
	 * this method will finish the setup of TTS including getting the "dialect" or "Locale" of the voice
	 */
	private class MyInitListener  implements TextToSpeech.OnInitListener{

		@Override
		public void onInit(int status) {
			if(status == TextToSpeech.SUCCESS){
				//get the user preference
				String lang = sharedPreferences.getString(LANGUAGE, LANGUAGE_US);
				int langResult=-1;

				if(lang.equals(LANGUAGE_US)) { //default
					langResult = tts.setLanguage(Locale.US);
				} else if (lang.equals(LANGUAGE_GERMAN)) {
					langResult = tts.setLanguage(Locale.GERMAN);
				}  else if (lang.equals(LANGUAGE_FRANCE)) {
					langResult = tts.setLanguage(Locale.FRANCE);
				} else if (lang.equals(LANGUAGE_CANADA)) {
					langResult = tts.setLanguage(Locale.CANADA);
				} else if (lang.equals(LANGUAGE_UK)) {
					langResult = tts.setLanguage(Locale.UK);
				}

				if(langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED){
					if (Util.isDebugBuild()) {
						Log.d(TAG, "Language not available");
					}
				} else{
					//let us know that it is safe to use it now
					isTTSInitialized = true;
				}
			} else if (Util.isDebugBuild()) {
				Log.d(TAG, "Text To Speech did not initialize correctly");
			}

		}

	}
}
