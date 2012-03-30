package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cs309.a1.gameboard.R;
import cs309.a1.shared.TextView;

/**
 * This is the Pause Menu. It will be started when a user
 * clicks on the pause button on the Gameboard.
 */
public class PauseMenuActivity extends Activity {
	/**
	 * The request code to keep track of the "Are you sure?" activity
	 */
	private static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pausemenu);

		// Update the title of the view
		TextView tv = (TextView) findViewById(R.id.title);
		tv.setText(R.string.pauseGameTitle);

		// Set up the button handler for the rules button
		Button rules = (Button) findViewById(R.id.btRules);
		rules.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent ruleButtonClick = new Intent(PauseMenuActivity.this, RulesActivity.class);
				startActivity(ruleButtonClick);
			}
		});

		// Set up the button handler for the "resume" button
		Button resume = (Button) findViewById(R.id.btResume);
		resume.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});

		// Set up the handler for the "Main menu" button
		Button mainMenu = (Button) findViewById(R.id.btMainMenu);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Start the "Are you sure you want to quit the game?" activity
				Intent mainMenu = new Intent(PauseMenuActivity.this, QuitGameActivity.class);
				startActivityForResult(mainMenu, QUIT_GAME);
			}
		});

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// If the user clicked yes in the quit game activity, finish this activity
		if (requestCode == QUIT_GAME && resultCode == RESULT_OK) {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

}
