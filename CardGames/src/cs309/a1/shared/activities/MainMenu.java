package cs309.a1.shared.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cs309.a1.R;
import cs309.a1.gameboard.activities.ConnectActivity;
import cs309.a1.gameboard.activities.PreferencesActivity;
import cs309.a1.player.activities.ShowCardsActivity;
import cs309.a1.shared.Util;

/**
 * The Main menu of the application
 */
public class MainMenu extends Activity {

	/**
	 * The request code to handle the result of the Quit Game Activity
	 */
	private static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

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
					startActivity(playButtonClick);
				}
			});
		}

		// Set the listener for the Join Game button
		Button play = (Button) findViewById(R.id.btJoin);
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Set that we are NOT the gameboard
				Util.setIsGameboard(false);

				Intent playButtonClick = new Intent(MainMenu.this, ShowCardsActivity.class);
				startActivity(playButtonClick);
			}
		});

		// Set the listener for the rules button
		Button rules = (Button) findViewById(R.id.btRules);
		rules.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent ruleButtonClick = new Intent(MainMenu.this, RulesActivity.class);
				startActivity(ruleButtonClick);
			}
		});

		// Set the listener for the about button
		Button about = (Button) findViewById(R.id.btAbout);
		about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent aboutButtonClick = new Intent(MainMenu.this,	AboutActivity.class);
				startActivity(aboutButtonClick);
			}
		});

		// Set the listener for the preferences button
		Button preferences = (Button) findViewById(R.id.btPref);

		if (preferences != null) {
			preferences.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent preferencesButtonClick = new Intent(MainMenu.this,	PreferencesActivity.class);
					startActivity(preferencesButtonClick);
				}
			});
		}
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// When the user presses the back button, we will prompt them
		// to make sure they want to quit
		Intent intent = new Intent(this, QuitApplicationActivity.class);
		startActivityForResult(intent, QUIT_GAME);
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// If they clicked yes on the prompt asking if they want to quit the
		// game, finish this activity
		if (requestCode == QUIT_GAME && resultCode == RESULT_OK){
			finish();
		}
	}
}
