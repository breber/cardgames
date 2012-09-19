package com.worthwhilegames.cardgames.gameboard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.TextView;

/**
 * The activity that is displayed on the tablet when the game finishes.
 * It will tell the users who won.
 *
 * Activity Results:
 * 		RESULT_OK - Always
 */
public class GameResultsActivity extends Activity{
	/**
	 * The Intent extra indicating which user won the game
	 */
	public static final String WINNER_NAME = "whowon";

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.winlose);
		setResult(RESULT_OK);

		String winner = getIntent().getStringExtra(WINNER_NAME);

		// Display who won in the title bar
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(getResources().getString(R.string.playerNWon).replace("%s", winner));

		// Add a handler to the Main Menu button
		Button mainMenu = (Button) findViewById(R.id.btMainMenu);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
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