package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cs309.a1.shared.R;

/**
 * This activity will be started when the user performs
 * an action that will cause the application to close if the action
 * is completed. We ask them if they actually want to quit
 * the application.
 */
public class QuitApplicationActivity extends Activity{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quitgameprompt);

		// Update the title to the prompt "Are you sure you want to 
		// exit the application?"
		TextView body = (TextView) findViewById(R.id.quitGameTitle);
		body.setText(R.string.exit_application);

		// Add handlers to the affirmative and negative buttons
		Button affirmative = (Button) findViewById(R.id.affirmative);
		affirmative.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});

		Button negative = (Button) findViewById(R.id.negative);
		negative.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// Do nothing. They should have to choose one
		// of the options in order to leave this activity
	}
}
