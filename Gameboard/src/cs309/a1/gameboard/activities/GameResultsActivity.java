package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cs309.a1.gameboard.R;

/**
 * The activity that is displayed on the client when the game finishes.
 * It will tell the user whether they won or lost.
 */
public class GameResultsActivity extends Activity{
	/**
	 * The Intent extra indicating whether the user
	 * won the game or lost the game.
	 */
	public static final String IS_WINNER = "iswinner";

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.winlose);
		
		// TODO: display who won
		// TODO: do we want to remove the "Exit Game" button?
		
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