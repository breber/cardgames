package com.worthwhilegames.cardgames.shared.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageButton;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.gameboard.activities.ConnectActivity;
import com.worthwhilegames.cardgames.player.activities.ShowCardsActivity;
import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.SoundManager;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionServer;

/**
 * The Main menu of the application
 */
public class MainMenu extends Activity {

	/**
	 * The request code to handle the result of the Connect Activity
	 */
	private static final int CONNECT_ACTIVITY = Math.abs("CONNECT_ACTIVITY".hashCode());

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Initialize the SoundManager on a separate thread, so that we don't
		// don't have to wait for it to initialize when starting the game
		new Thread(new Runnable() {
			@Override
			public void run() {
				SoundManager.getInstance(MainMenu.this);
			}
		}).start();

		// Set the listener for the Create Game button if it exists
		Button create = (Button) findViewById(R.id.btCreate);
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
				if (Util.isDebugBuild()) {
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
		}
	}
}
