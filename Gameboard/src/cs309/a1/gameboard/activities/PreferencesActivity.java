package cs309.a1.gameboard.activities;

import static cs309.a1.shared.Constants.DIFFICULTY_OF_COMPUTERS;
import static cs309.a1.shared.Constants.LANGUAGE;
import static cs309.a1.shared.Constants.LANGUAGE_CANADA;
import static cs309.a1.shared.Constants.LANGUAGE_FRANCE;
import static cs309.a1.shared.Constants.LANGUAGE_GERMAN;
import static cs309.a1.shared.Constants.LANGUAGE_US;
import static cs309.a1.shared.Constants.LANGUAGE_UK;
import static cs309.a1.shared.Constants.NUMBER_OF_COMPUTERS;
import static cs309.a1.shared.Constants.PREFERENCES;
import static cs309.a1.shared.Constants.SOUND_EFFECTS;
import static cs309.a1.shared.Constants.SPEECH_VOLUME;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cs309.a1.gameboard.R;
import cs309.a1.shared.Util;

public class PreferencesActivity extends Activity{
	
	private AudioManager audioManager;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor prefsEditor;
	private RadioButton myOption1, myOption2, myOption3, myOption4;
	private RadioButton myOptionEasy, myOptionMedium, myOptionHard;
	private RadioButton myOptionLang1, myOptionLang2, myOptionLang3, myOptionLang4, myOptionLang5;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		
		//Get and change the title of the Preferences layout
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.preferencesActivityTitle);
		
		//create the auidio manager for controlling  the volume
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		//create the shared preferences object
		sharedPref = this.getSharedPreferences(PREFERENCES, MODE_WORLD_WRITEABLE);
		
		//create the preferences editor for editing the preferences
		prefsEditor = sharedPref.edit();
		
		//Seek Bar from the layout
		SeekBar volumeBar = (SeekBar)findViewById(R.id.volume);

		//set the volume progress bar to the media volume of the device
		volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
			
		//set the max volume to the max volume of the device
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		//set the bar
		volumeBar.setMax(maxVolume);
		
		//create a listener for the volume bar changing
		volumeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()  
        {  
			
            /* (non-Javadoc)
             * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
             */
            @Override  
            public void onStopTrackingTouch(SeekBar seekBar)  
            {  
            }  
  
            /* (non-Javadoc)
             * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
             */
            @Override  
            public void onStartTrackingTouch(SeekBar seekBar)  
            {  
            }  
  
            /* (non-Javadoc)
             * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar, int, boolean)
             */
            @Override  
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)  
            {    
            	//if the volume bar is changed then change the device volume to the new level
            	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);  
            }  
        });  
		
		//Sound effects checkbox object
		CheckBox soundEffects = (CheckBox)findViewById(R.id.checkBoxSoundEffects);
		
		//set the check box to it's preference
		soundEffects.setChecked(sharedPref.getBoolean(SOUND_EFFECTS, true));
		
		//speech check box
		CheckBox speechVolume = (CheckBox)findViewById(R.id.checkBoxSpeechVolume);
		
		//set the check box to it's preference
		speechVolume.setChecked(sharedPref.getBoolean(SPEECH_VOLUME, true));
		
		//Number of Computers from the shared preferences
		int checkNum = sharedPref.getInt(NUMBER_OF_COMPUTERS, 1);
		
		//Radio button options for the number of computers
		myOption1 = (RadioButton)findViewById(R.id.radio1);
		myOption3 = (RadioButton)findViewById(R.id.radio2);
		myOption2 = (RadioButton)findViewById(R.id.radio3);
		myOption4 = (RadioButton)findViewById(R.id.radio4);
		
		//based on which value the shared preferences returns then set the radio button to checked
		switch(checkNum){
			case 1: myOption1.setChecked(true);
				break;
			case 2: myOption2.setChecked(true);
				break;
			case 3: myOption3.setChecked(true);
				break;
			case 4: myOption4.setChecked(true);
				break;
		}

		
		//Difficulty of Computers Radio buttons
		myOptionEasy = (RadioButton)findViewById(R.id.radioEasy);
		myOptionMedium = (RadioButton)findViewById(R.id.radioMedium);
		myOptionHard = (RadioButton)findViewById(R.id.radioHard);
		
		//get the value of the option from the shared preferences
		int difficulty = sharedPref.getInt(DIFFICULTY_OF_COMPUTERS, 1);
		
		//based on the value of the preference set the correct radio button to checked
		switch(difficulty){
			case 1: myOptionEasy.setChecked(true);
				break;
			case 2: myOptionMedium.setChecked(true);
				break;
			case 3: myOptionHard.setChecked(true);
				break;
		}
		
		//Language Radio button options
		myOptionLang1 = (RadioButton)findViewById(R.id.langCanada);
		myOptionLang2 = (RadioButton)findViewById(R.id.langUS);
		myOptionLang3 = (RadioButton)findViewById(R.id.langFrance);
		myOptionLang4 = (RadioButton)findViewById(R.id.langGerman);
		myOptionLang5 = (RadioButton)findViewById(R.id.langUK);
		
		//Get the current language that the user has set in the preferences
		String language = sharedPref.getString(LANGUAGE, LANGUAGE_US);
		
		//display the message
		if(Util.isDebugBuild()){			
			Toast.makeText(this, language, Toast.LENGTH_SHORT).show();
		}
		
		//based on the value of the language from the preferences set the correct radio button
		if(language.equals(LANGUAGE_CANADA)){
			myOptionLang1.setChecked(true);
		}else if(language.equals(LANGUAGE_US)){
			myOptionLang2.setChecked(true);
		}else if(language.equals(LANGUAGE_FRANCE)){
			myOptionLang3.setChecked(true);
		}else if(language.equals(LANGUAGE_GERMAN)){
			myOptionLang4.setChecked(true);
		}else if(language.equals(LANGUAGE_UK)){
			myOptionLang5.setChecked(true);
		}
		
		//OK button on the preferences screen
		Button ok = (Button)findViewById(R.id.ok);
		
		//On click listener for the ok button
		ok.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				
				//get the values for the sound options from the preferences
				CheckBox soundEffects = (CheckBox)findViewById(R.id.checkBoxSoundEffects);
				CheckBox speechVolume = (CheckBox)findViewById(R.id.checkBoxSpeechVolume);
				
				//set the result of the activity
				setResult(RESULT_OK);
				
				//put the new preferences in the shared preferences
				prefsEditor.putBoolean(SPEECH_VOLUME, speechVolume.isChecked());
				prefsEditor.putBoolean(SOUND_EFFECTS, soundEffects.isChecked());
				
				//set number of computers				//set number of computers based on the radio button checked
				//update the shared preferences with the value checked by the user
				if(myOption1.isActivated() == true){
					prefsEditor.putInt(NUMBER_OF_COMPUTERS, 1);
				}else if(myOption2.isChecked() == true){
					prefsEditor.putInt(NUMBER_OF_COMPUTERS, 2);
				}else if(myOption3.isChecked() == true){
					prefsEditor.putInt(NUMBER_OF_COMPUTERS, 3);
				}else if(myOption4.isChecked() == true){
					prefsEditor.putInt(NUMBER_OF_COMPUTERS, 4);
				}
				
				//set difficulty of computers to preferences
				if(myOptionEasy.isChecked() == true){
					prefsEditor.putInt(DIFFICULTY_OF_COMPUTERS, 1);
				}else if(myOptionMedium.isChecked() == true){
					prefsEditor.putInt(DIFFICULTY_OF_COMPUTERS, 2);
				}else if(myOptionHard.isChecked() == true){
					prefsEditor.putInt(DIFFICULTY_OF_COMPUTERS, 3);
				}
				
				//set language to preferences
				if(myOptionLang1.isChecked()){
					prefsEditor.putString(LANGUAGE, LANGUAGE_CANADA);
				}else if(myOptionLang2.isChecked()){
					prefsEditor.putString(LANGUAGE, LANGUAGE_US);
				}else if(myOptionLang3.isChecked()){
					prefsEditor.putString(LANGUAGE, LANGUAGE_FRANCE);
				}else if(myOptionLang4.isChecked()){
					prefsEditor.putString(LANGUAGE, LANGUAGE_GERMAN);
				}else if(myOptionLang5.isChecked()){
					prefsEditor.putString(LANGUAGE, LANGUAGE_UK);
				}
				
				//commit the changes to the shared preferences
				prefsEditor.commit();
				
				//finish the activity
				finish();
			}
		});
		
	}
}
