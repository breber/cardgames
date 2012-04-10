package cs309.a1.gameboard.activities;

import static cs309.a1.crazyeights.Constants.DIFFICULTY_OF_COMPUTERS;
import static cs309.a1.crazyeights.Constants.LANGUAGE;
import static cs309.a1.crazyeights.Constants.LANGUAGE_CANADA;
import static cs309.a1.crazyeights.Constants.LANGUAGE_FRANCE;
import static cs309.a1.crazyeights.Constants.LANGUAGE_GERMAN;
import static cs309.a1.crazyeights.Constants.LANGUAGE_US;
import static cs309.a1.crazyeights.Constants.NUMBER_OF_COMPUTERS;
import static cs309.a1.crazyeights.Constants.PREFERENCES;
import static cs309.a1.crazyeights.Constants.SOUND_EFFECTS;
import static cs309.a1.crazyeights.Constants.SPEECH_VOLUME;
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

public class PreferencesActivity extends Activity{
	
	AudioManager audioManager;
	SharedPreferences sharedPref;
	SharedPreferences.Editor prefsEditor;
	RadioButton myOption1, myOption2, myOption3, myOption4;
	RadioButton myOptionEasy, myOptionMedium, myOptionHard;
	RadioButton myOptionLang1, myOptionLang2, myOptionLang3, myOptionLang4;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.preferencesActivityTitle);
		
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		sharedPref = this.getSharedPreferences(PREFERENCES, MODE_WORLD_WRITEABLE);
		prefsEditor = sharedPref.edit();
		
		//Seek Bar
		SeekBar volumeBar = (SeekBar)findViewById(R.id.volume);

		volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
			
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		volumeBar.setMax(maxVolume);
		
		volumeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()  
        {  
            @Override  
            public void onStopTrackingTouch(SeekBar seekBar)  
            {  
            }  
  
            @Override  
            public void onStartTrackingTouch(SeekBar seekBar)  
            {  
            }  
  
            @Override  
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)  
            {    
            	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);  
            }  
        });  
		
		
		CheckBox soundEffects = (CheckBox)findViewById(R.id.checkBoxSoundEffects);
		soundEffects.setChecked(sharedPref.getBoolean(SOUND_EFFECTS, true));
		
		CheckBox speechVolume = (CheckBox)findViewById(R.id.checkBoxSpeechVolume);
		speechVolume.setChecked(sharedPref.getBoolean(SPEECH_VOLUME, true));
		
		//Number of Computers
		int checkNum = sharedPref.getInt(NUMBER_OF_COMPUTERS, 1);
		myOption1 = (RadioButton)findViewById(R.id.radio1);
		myOption3 = (RadioButton)findViewById(R.id.radio2);
		myOption2 = (RadioButton)findViewById(R.id.radio3);
		myOption4 = (RadioButton)findViewById(R.id.radio4);
		
		if(checkNum == 1)
			myOption1.setChecked(true);
		else if(checkNum == 2)
			myOption2.setChecked(true);
		else if(checkNum == 3)
			myOption3.setChecked(true);
		else if(checkNum == 4)
			myOption4.setChecked(true);
		
		//Difficulty of Computers
		myOptionEasy = (RadioButton)findViewById(R.id.radioEasy);
		myOptionMedium = (RadioButton)findViewById(R.id.radioMedium);
		myOptionHard = (RadioButton)findViewById(R.id.radioHard);
		
		int difficulty = sharedPref.getInt(DIFFICULTY_OF_COMPUTERS, 1);
		if(difficulty == 1)
			myOptionEasy.setChecked(true);
		else if(difficulty == 2)
			myOptionMedium.setChecked(true);
		else if(difficulty == 3)
			myOptionHard.setChecked(true);
		
		//Language
		myOptionLang1 = (RadioButton)findViewById(R.id.langCanada);
		myOptionLang2 = (RadioButton)findViewById(R.id.langUS);
		myOptionLang3 = (RadioButton)findViewById(R.id.langFrance);
		myOptionLang4 = (RadioButton)findViewById(R.id.langGerman);
		
		String language = sharedPref.getString(LANGUAGE, LANGUAGE_US);
		Toast.makeText(this, language, Toast.LENGTH_SHORT).show();
		
		if(language.equals(LANGUAGE_CANADA))
			myOptionLang1.setChecked(true);
		else if(language.equals(LANGUAGE_US)){
			Toast.makeText(this, language, Toast.LENGTH_SHORT).show();
			myOptionLang2.setChecked(true);
		}
		else if(language.equals(LANGUAGE_FRANCE))
			myOptionLang3.setChecked(true);
		else if(language.equals(LANGUAGE_GERMAN))
			myOptionLang4.setChecked(true);
		
		
		Button ok = (Button)findViewById(R.id.ok);
		
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox soundEffects = (CheckBox)findViewById(R.id.checkBoxSoundEffects);
				CheckBox speechVolume = (CheckBox)findViewById(R.id.checkBoxSpeechVolume);
				setResult(RESULT_OK);
				
				prefsEditor.putBoolean(SOUND_EFFECTS, soundEffects.isChecked());
				prefsEditor.putBoolean(SPEECH_VOLUME, speechVolume.isChecked());
				
				//set number of computers
				if(myOption1.isActivated() == true)
					prefsEditor.putInt(NUMBER_OF_COMPUTERS, 1);
				else if(myOption2.isChecked() == true)
					prefsEditor.putInt(NUMBER_OF_COMPUTERS, 2);
				else if(myOption3.isChecked() == true)
					prefsEditor.putInt(NUMBER_OF_COMPUTERS, 3);
				else if(myOption4.isChecked() == true)
					prefsEditor.putInt(NUMBER_OF_COMPUTERS, 4);
				
				//set difficulty of computers
				if(myOptionEasy.isChecked() == true)
					prefsEditor.putInt(DIFFICULTY_OF_COMPUTERS, 1);
				else if(myOptionMedium.isChecked() == true)
					prefsEditor.putInt(DIFFICULTY_OF_COMPUTERS, 2);
				else if(myOptionHard.isChecked() == true)
					prefsEditor.putInt(DIFFICULTY_OF_COMPUTERS, 3);
				
				//set language
				if(myOptionLang1.isChecked())
					prefsEditor.putString(LANGUAGE, LANGUAGE_CANADA);
				else if(myOptionLang2.isChecked())
					prefsEditor.putString(LANGUAGE, LANGUAGE_US);
				else if(myOptionLang3.isChecked())
					prefsEditor.putString(LANGUAGE, LANGUAGE_FRANCE);
				else if(myOptionLang4.isChecked())
					prefsEditor.putString(LANGUAGE, LANGUAGE_GERMAN);
				
				prefsEditor.commit();
				
				finish();
			}
		});
		
	}
}
