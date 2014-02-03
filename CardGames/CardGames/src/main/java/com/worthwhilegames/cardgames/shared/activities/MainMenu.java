package com.worthwhilegames.cardgames.shared.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech.Engine;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.worthwhilegames.cardgames.GameActivity;
import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.AdActivity;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.SoundManager;

/**
 * The Main menu of the application
 */
public class MainMenu extends AdActivity {

    /**
     * The request code to handle the result of the Connect Activity
     */
    private static final int CONNECT_ACTIVITY = Math.abs("CONNECT_ACTIVITY".hashCode());

    /**
     * The request code to handle the result of the checking TTS activity
     */
    private static final int CHECK_TTS = Math.abs(Engine.ACTION_CHECK_TTS_DATA.hashCode());

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Check to see if TTS exists
        Intent checkTts = new Intent(Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTts, CHECK_TTS);

        // Set the listener for the Create Game button if it exists
        final Button create = (Button) findViewById(R.id.btCreate);
        create.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the gameboard connect activity
                Intent playButtonClick = new Intent(MainMenu.this, GameActivity.class);
                startActivityForResult(playButtonClick, CONNECT_ACTIVITY);
            }
        });


        Button play = (Button) findViewById(R.id.btJoin);
        // Set the listener for the Join Game button
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playButtonClick = new Intent(MainMenu.this, GameActivity.class);
                startActivity(playButtonClick);
            }
        });

        // Set the listener for the about button
        Button about = (Button) findViewById(R.id.btAbout);
        about.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutButtonClick = new Intent(MainMenu.this, AboutActivity.class);
                startActivity(aboutButtonClick);
            }
        });
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECK_TTS) {
            if (resultCode == Engine.CHECK_VOICE_DATA_PASS) {
                // Indicate that we have TTS
                SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, 0);
                prefs.edit().putBoolean(Constants.PREF_HAS_TTS, true).commit();
            } else {
                // Indicate that we do not have TTS
                SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, 0);
                prefs.edit().putBoolean(Constants.PREF_HAS_TTS, false).commit();

                // TODO: maybe enable this once we have a way to test it
                // Try to install TTS
                // startActivity(new Intent(Engine.ACTION_INSTALL_TTS_DATA));
            }

            // Initialize the SoundManager on a separate thread, so that we don't
            // don't have to wait for it to initialize when starting the game
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SoundManager.getInstance(MainMenu.this);
                }
            }).start();
        }
    }
}
