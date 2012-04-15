package cs309.a1.gameboard.activities;

import static cs309.a1.shared.Constants.CRAZY_EIGHTS;
import static cs309.a1.shared.Constants.DIFFICULTY_OF_COMPUTERS;
import static cs309.a1.shared.Constants.GAME_TYPE;
import static cs309.a1.shared.Constants.LANGUAGE;
import static cs309.a1.shared.Constants.LANGUAGE_CANADA;
import static cs309.a1.shared.Constants.LANGUAGE_FRANCE;
import static cs309.a1.shared.Constants.LANGUAGE_GERMAN;
import static cs309.a1.shared.Constants.LANGUAGE_UK;
import static cs309.a1.shared.Constants.LANGUAGE_US;
import static cs309.a1.shared.Constants.NUMBER_OF_COMPUTERS;
import static cs309.a1.shared.Constants.PREFERENCES;
import static cs309.a1.shared.Constants.SOUND_EFFECTS;
import static cs309.a1.shared.Constants.SPEECH_VOLUME;
import static cs309.a1.shared.Constants.EASY;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cs309.a1.R;
import cs309.a1.shared.Util;

/**
 * This class will be used as an activity for the preferences screen. Within
 * this activity a user will be able to set up their options for the current
 * game including choosing the locale, number of computer players, the difficult
 * of the AI players, volume control and options to enable and disable sounds.
 */
public class PreferencesActivity extends Activity {

	/**
	 * An AudioManager object to be used for controlling the game volume
	 */
	private AudioManager audioManager;

	/**
	 * A SharedPreferences object to get the preferences set by the user
	 */
	private SharedPreferences sharedPref;

	/**
	 * A SharedPreferences editor for changing the game preferences based on user input
	 */
	private SharedPreferences.Editor prefsEditor;

	/**
	 * Radio buttons to be used when selecting the number of computer players in the game
	 * A user has the option to select 1-4
	 */
	private RadioButton myOption1, myOption2, myOption3, myOption4;

	/**
	 * Radio buttons to be used when selecting the difficulty of the computer players
	 * Users will be able to select Easy, Medium and Hard
	 */
	private RadioButton myOptionEasy, myOptionMedium, myOptionHard;

	/**
	 * Radio buttons to select the locale of the text to speech object
	 * Users will have a variety of locale options to select currently 1-5
	 */
	private RadioButton myOptionLang1, myOptionLang2, myOptionLang3, myOptionLang4, myOptionLang5;

	/**
	 * Radio buttons used to select the game type such as Crazy Eights
	 */
	private RadioButton myGameOption1;
	
	private Spinner numComputerSpinner;
	private Spinner localeSpinner;
	private Spinner gameSpinner;
	private Spinner difficultySpinner;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);

		// Get and change the title of the Preferences layout
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.preferencesActivityTitle);

		// create the auidio manager for controlling the volume
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		// create the shared preferences object
		sharedPref = this.getSharedPreferences(PREFERENCES,	MODE_WORLD_WRITEABLE);

		// create the preferences editor for editing the preferences
		prefsEditor = sharedPref.edit();

		// Seek Bar from the layout
		SeekBar volumeBar = (SeekBar) findViewById(R.id.volume);

		// set the volume progress bar to the media volume of the device
		volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

		// set the max volume to the max volume of the device
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		// set the bar
		volumeBar.setMax(maxVolume);

		// create a listener for the volume bar changing
		volumeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			/* (non-Javadoc)
			 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
			 */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }

			/* (non-Javadoc)
			 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
			 */
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {	}


			/* (non-Javadoc)
			 * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar, int, boolean)
			 */
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// if the volume bar is changed then change the device volume to
				// the new level
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
			}
		});

		// Sound effects checkbox object
		CheckBox soundEffects = (CheckBox) findViewById(R.id.checkBoxSoundEffects);

		// set the check box to it's preference
		soundEffects.setChecked(sharedPref.getBoolean(SOUND_EFFECTS, true));

		// speech check box
		CheckBox speechVolume = (CheckBox) findViewById(R.id.checkBoxSpeechVolume);

		// set the check box to it's preference
		speechVolume.setChecked(sharedPref.getBoolean(SPEECH_VOLUME, true));

		// Number of Computers from the shared preferences		
		numComputerSpinner = (Spinner)findViewById(R.id.spinnerNumComputers);
		Integer numberOfComputers = sharedPref.getInt(NUMBER_OF_COMPUTERS, 1);
		ArrayAdapter numCompAdapter = (ArrayAdapter) numComputerSpinner.getAdapter();
		int spinnerPosition = numCompAdapter.getPosition(numberOfComputers.toString());
		numComputerSpinner.setSelection(spinnerPosition);


		// Difficulty of Computers Radio buttons
//		myOptionEasy = (RadioButton) findViewById(R.id.radioEasy);
//		myOptionMedium = (RadioButton) findViewById(R.id.radioMedium);
//		myOptionHard = (RadioButton) findViewById(R.id.radioHard);

		// get the value of the option from the shared preferences
		String difficulty = sharedPref.getString(DIFFICULTY_OF_COMPUTERS, EASY);
		difficultySpinner = (Spinner)findViewById(R.id.difficultyOption);
		ArrayAdapter<String> difficultyAdapter = (ArrayAdapter<String>) difficultySpinner.getAdapter(); //cast to an ArrayAdapter
		int difficultyPosition = difficultyAdapter.getPosition(difficulty);
		System.out.println(difficultyPosition);
		difficultySpinner.setSelection(difficultyPosition);
		
		// based on the value of the preference set the correct radio button to
		// checked
//		switch (difficulty) {
//		case 1:
//			myOptionEasy.setChecked(true);
//			break;
//		case 2:
//			myOptionMedium.setChecked(true);
//			break;
//		case 3:
//			myOptionHard.setChecked(true);
//			break;
//		}

		// Language Radio button options
//		myOptionLang1 = (RadioButton) findViewById(R.id.langCanada);
//		myOptionLang2 = (RadioButton) findViewById(R.id.langUS);
//		myOptionLang3 = (RadioButton) findViewById(R.id.langFrance);
//		myOptionLang4 = (RadioButton) findViewById(R.id.langGerman);
//		myOptionLang5 = (RadioButton) findViewById(R.id.langUK);

		// Get the current language that the user has set in the preferences


		// based on the value of the language from the preferences set the
		// correct radio button
		localeSpinner = (Spinner)findViewById(R.id.langOption);
		String language = sharedPref.getString(LANGUAGE, LANGUAGE_US);
		
		// display the message
		if (Util.isDebugBuild()) {
			Toast.makeText(this, language, Toast.LENGTH_SHORT).show();
		}
		
		ArrayAdapter<String> localeAdapter = (ArrayAdapter<String>) localeSpinner.getAdapter(); //cast to an ArrayAdapter
		int localePosition = localeAdapter.getPosition(language);
		localeSpinner.setSelection(localePosition);
//		if (language.equals(LANGUAGE_CANADA)) {
//			myOptionLang1.setChecked(true);
//		} else if (language.equals(LANGUAGE_US)) {
//			myOptionLang2.setChecked(true);
//		} else if (language.equals(LANGUAGE_FRANCE)) {
//			myOptionLang3.setChecked(true);
//		} else if (language.equals(LANGUAGE_GERMAN)) {
//			myOptionLang4.setChecked(true);
//		} else if (language.equals(LANGUAGE_UK)) {
//			myOptionLang5.setChecked(true);
//		}

		// value of the game type based upon the radio button
		String game_type = sharedPref.getString(GAME_TYPE, CRAZY_EIGHTS);

		//myGameOption1 = (RadioButton) findViewById(R.id.gameCrazyEights);
		gameSpinner = (Spinner)findViewById(R.id.gameOption);
		
		ArrayAdapter<String> gameAdapter = (ArrayAdapter<String>) gameSpinner.getAdapter(); //cast to an ArrayAdapter
		int gamePosition = localeAdapter.getPosition(game_type);
		gameSpinner.setSelection(gamePosition);

		// OK button on the preferences screen
		Button ok = (Button) findViewById(R.id.ok);

		// On click listener for the ok button
		ok.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {

				// get the values for the sound options from the preferences
				CheckBox soundEffects = (CheckBox) findViewById(R.id.checkBoxSoundEffects);
				CheckBox speechVolume = (CheckBox) findViewById(R.id.checkBoxSpeechVolume);

				// set the result of the activity
				setResult(RESULT_OK);

				// put the new preferences in the shared preferences
				prefsEditor.putBoolean(SPEECH_VOLUME, speechVolume.isChecked());
				prefsEditor.putBoolean(SOUND_EFFECTS, soundEffects.isChecked());

				// set number of computers based on the radio button checked
				// update the shared preferences with the value checked by the user
//				if (myOption1.isChecked()) {
//					prefsEditor.putInt(NUMBER_OF_COMPUTERS, 1);
//				} else if (myOption2.isChecked()) {
//					prefsEditor.putInt(NUMBER_OF_COMPUTERS, 2);
//				} else if (myOption3.isChecked()) {
//					prefsEditor.putInt(NUMBER_OF_COMPUTERS, 3);
//				} else if (myOption4.isChecked()) {
//					prefsEditor.putInt(NUMBER_OF_COMPUTERS, 4);
//				}
				
				prefsEditor.putInt(NUMBER_OF_COMPUTERS, Integer.parseInt((String) numComputerSpinner.getSelectedItem()));

				// set difficulty of computers to preferences
//				if (myOptionEasy.isChecked()) {
//					prefsEditor.putInt(DIFFICULTY_OF_COMPUTERS, 0);
//				} else if (myOptionMedium.isChecked()) {
//					prefsEditor.putInt(DIFFICULTY_OF_COMPUTERS, 1);
//				} else if (myOptionHard.isChecked()) {
//					prefsEditor.putInt(DIFFICULTY_OF_COMPUTERS, 2);
//				}
				
				prefsEditor.putString(DIFFICULTY_OF_COMPUTERS, (String)difficultySpinner.getSelectedItem());

				// set language to preferences
//				if (myOptionLang1.isChecked()) {
//					prefsEditor.putString(LANGUAGE, LANGUAGE_CANADA);
//				} else if (myOptionLang2.isChecked()) {
//					prefsEditor.putString(LANGUAGE, LANGUAGE_US);
//				} else if (myOptionLang3.isChecked()) {
//					prefsEditor.putString(LANGUAGE, LANGUAGE_FRANCE);
//				} else if (myOptionLang4.isChecked()) {
//					prefsEditor.putString(LANGUAGE, LANGUAGE_GERMAN);
//				} else if (myOptionLang5.isChecked()) {
//					prefsEditor.putString(LANGUAGE, LANGUAGE_UK);
//				}
				prefsEditor.putString(LANGUAGE, (String)localeSpinner.getSelectedItem());

//				if (myGameOption1.isChecked()) {
//					prefsEditor.putString(GAME_TYPE, CRAZY_EIGHTS);
//				}
				prefsEditor.putString(GAME_TYPE, (String)gameSpinner.getSelectedItem());
				// commit the changes to the shared preferences
				prefsEditor.commit();

				// finish the activity
				finish();
			}
		});
	}
}
