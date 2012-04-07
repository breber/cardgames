package cs309.a1.player.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cs309.a1.player.R;

/**
 * The activity that is displayed on the client when the game finishes.
 * It will tell the user whether they won or lost.
 * 
 * Activity Results:
 * 		RESULT_OK - Always
 */
public class GameResultsActivity extends Activity {
	/**
	 * The Intent extra indicating whether the user
	 * won the game or lost the game.
	 */
	public static final String IS_WINNER = "isWinner";

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.winlose);
		setResult(RESULT_OK);

		TextView title = (TextView) findViewById(R.id.title);
		Intent isWinner = getIntent();

		// Update the title to display the proper title
		if (isWinner.getBooleanExtra(IS_WINNER, false)) {
			title.setText(R.string.winner);
		} else {
			title.setText(R.string.loser);
		}

		// Add a handler to the Main Menu button
		Button mainMenu = (Button) findViewById(R.id.btMainMenu);
		mainMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// When they click the main menu button, finish this activity,
				// which will allow the ShowCardsActivity to finish leaving them back
				// at the MainMenu
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}
}
