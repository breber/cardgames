package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cs309.a1.gameboard.R;
import cs309.a1.shared.SoundManager;

/**
 * This is the Main Menu activity. This allows
 * the user to create a new game, view the rules,
 * and view the about text.
 */
public class MainMenu extends Activity {

	/**
	 * The request code to keep track of the "Are you sure you want to quit" activity
	 */
	private static final int QUIT_GAME = "QUIT_GAME".hashCode();

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//initialize the sounds
		SoundManager.initSounds(getApplicationContext());

		// Set up the button handlers on the main menu
		Button play = (Button) findViewById(R.id.btPlay);
		Button about = (Button) findViewById(R.id.btAbout);
		Button rules = (Button) findViewById(R.id.btRules);

		rules.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent ruleButtonClick = new Intent(MainMenu.this, RulesActivity.class);
				startActivity(ruleButtonClick);
			}
		});

		about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent aboutButtonClick = new Intent(MainMenu.this,	AboutActivity.class);
				startActivity(aboutButtonClick);
			}
		});

		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent playButtonClick = new Intent(MainMenu.this, ConnectActivity.class);
				startActivity(playButtonClick);
			}
		});

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// When back is pressed, we will prompt the user
		// to see if they want to quit
		Intent intent = new Intent(this, QuitApplicationActivity.class);
		startActivityForResult(intent, QUIT_GAME);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Handle the result from the Quit Application prompt.
		// If they answered yes, finish this activity. Otherwise, 
		// do nothing.
		if (requestCode == QUIT_GAME && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}
}
