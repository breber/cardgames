package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cs309.a1.gameboard.R;
import cs309.a1.shared.TextView;

/**
 * This activity will be started when the user performs an action that will
 * cause the game to end if the action is completed. We ask them if they
 * actually want to quit the application.
 * 
 * Activity Results: RESULT_OK - if the user chose the affirmative option
 * RESULT_CANCELLED - if the user chose the negative option
 */
public class QuitGameActivity extends Activity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prompt_dialog);

		// Update the prompt dialog title text
		TextView tv = (TextView) findViewById(R.id.promptDialogTitle);
		tv.setText(R.string.quit);

		// Add handlers to affirmative and negative buttons
		Button affirmative = (Button) findViewById(R.id.affirmative);
		affirmative.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});

		Button negative = (Button) findViewById(R.id.negative);
		negative.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// Do nothing. They should have to choose one
		// of the options in order to leave this activity
	}
}
