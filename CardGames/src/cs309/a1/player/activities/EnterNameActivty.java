package cs309.a1.player.activities;

import static cs309.a1.shared.Constants.PLAYER_NAME;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cs309.a1.R;

/**
 * The Activtiy that gets displayed when the user is waiting
 * for the game to begin. This prompts the user to enter their
 * name so that the tablet knows what to display.
 *
 * Activity Results:
 * 		RESULT_OK - If the user entered a name
 * 					Activity result will have an intent with a String
 * 					extra containing the chosen name.
 *
 * 		RESULT_CANCELLED - If the user didn't choose a name
 */
public class EnterNameActivty extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entername);

		//create button for the view
		Button ok = (Button) findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText name = (EditText) findViewById(R.id.name);
				String playerName;

				// If the user didn't enter anything, just use the
				// default name
				if (name.getText().length() == 0) {
					playerName = getResources().getString(R.string.default_name);
				} else {
					playerName = name.getText().toString();
				}

				// Return the name the player chose with the result of this Activity
				Intent nameToSend = new Intent();
				nameToSend.putExtra(PLAYER_NAME, playerName);
				setResult(RESULT_OK, nameToSend);
				finish();
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// They chose not to enter a name
		setResult(RESULT_CANCELED);
		finish();
	}
}
