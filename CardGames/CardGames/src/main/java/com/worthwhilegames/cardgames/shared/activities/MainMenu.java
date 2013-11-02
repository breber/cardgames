package com.worthwhilegames.cardgames.shared.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech.Engine;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageButton;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.gameboard.activities.ConnectActivity;
import com.worthwhilegames.cardgames.player.activities.ShowCardsActivity;
import com.worthwhilegames.cardgames.shared.AdActivity;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.SoundManager;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionServer;

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
        if (create != null) {
            create.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set that we are the gameboard
                    Util.setIsGameboard(true);

                    // Open the gameboard connect activity
                    Intent playButtonClick = new Intent(MainMenu.this, ConnectActivity.class);
                    startActivityForResult(playButtonClick, CONNECT_ACTIVITY);
                }
            });
        }


        Button play = (Button) findViewById(R.id.btJoin);
        if (Util.isGoogleTv(this)) {
            // If this is a Google TV, hide the Join Game button
            play.setVisibility(View.GONE);

            ViewGroup.MarginLayoutParams createParams = (MarginLayoutParams) create.getLayoutParams();
            createParams.setMargins((int)getResources().getDimension(R.dimen.mainMenuLargeButtonPadding),
                    (int) getResources().getDimension(R.dimen.mainMenuButtonSpacing),
                    (int) getResources().getDimension(R.dimen.mainMenuLargeButtonPadding),
                    (int) getResources().getDimension(R.dimen.mainMenuButtonSpacing));
        } else {
            // Set the listener for the Join Game button
            play.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set that we are NOT the gameboard
                    Util.setIsGameboard(false);

                    Intent playButtonClick = new Intent(MainMenu.this, ShowCardsActivity.class);
                    startActivity(playButtonClick);
                }
            });
        }

        // Set the listener for the rules button
        Button rules = (Button) findViewById(R.id.btRules);
        rules.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isDebugBuild() && create == null) {
                    // Open the gameboard connect activity
                    Intent playButtonClick = new Intent(MainMenu.this, ConnectActivity.class);
                    startActivityForResult(playButtonClick, CONNECT_ACTIVITY);
                } else {
                    Intent ruleButtonClick = new Intent(MainMenu.this, RulesActivity.class);
                    startActivity(ruleButtonClick);
                }
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

        // Set the listener for the preferences button
        ImageButton preferences = (ImageButton) findViewById(R.id.titleSettingsButton);
        preferences.setVisibility(View.VISIBLE);
        preferences.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preferencesButtonClick = new Intent(MainMenu.this, PreferencesActivity.class);
                startActivity(preferencesButtonClick);
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
        if (requestCode == CONNECT_ACTIVITY) {
            // Disconnect all users when we are back at the main menu
            ConnectionServer.getInstance(this).disconnect();

            // Clear the game
            GameFactory.clearGameInstance(this);
        } else if (requestCode == CHECK_TTS) {
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
