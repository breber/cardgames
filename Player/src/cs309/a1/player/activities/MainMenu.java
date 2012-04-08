package cs309.a1.player.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cs309.a1.player.R;
import cs309.a1.shared.SoundManager;

/**
 * The Main menu of the application
 */
public class MainMenu extends Activity {

	/**
	 * The request code to handle the result of the Quit Game Activity
	 */
	private static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());

	/**
	 * Sound manager will allow us to add sounds or music to the main menu
	 */
	private SoundManager mySM;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mySM = new SoundManager(getApplicationContext());

		// Set the listener for the play button
		Button play = (Button) findViewById(R.id.btPlay);
		play.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent playButtonClick = new Intent(MainMenu.this, ShowCardsActivity.class);
				startActivity(playButtonClick);
			}
		});

		// Set the listener for the rules button
		Button rules = (Button) findViewById(R.id.btRules);
		rules.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent ruleButtonClick = new Intent(MainMenu.this, RulesActivity.class);
				startActivity(ruleButtonClick);
			}
		});

		// Set the listener for the about button
		Button about = (Button) findViewById(R.id.btAbout);
		about.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent aboutButtonClick = new Intent(MainMenu.this,	AboutActivity.class);
				startActivity(aboutButtonClick);
			}
		});
	

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
		mySM.stopAllSound();
		// If they clicked yes on the prompt asking if they want to quit the
		// game, finish this activity
		if (requestCode == QUIT_GAME && resultCode == RESULT_OK){
			finish();
		}
	}
}
