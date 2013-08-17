package com.worthwhilegames.cardgames.shared.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.AdActivity;

/**
 * This activity will be started when the user performs
 * an action that will cause the application to close if the action
 * is completed. We ask them if they actually want to quit
 * the application.
 *
 * Activity Results:
 * 		RESULT_OK - if the user chose the affirmative option
 * 		RESULT_CANCELLED - if the user chose the negative option
 */
public class QuitApplicationActivity extends AdActivity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prompt_dialog);

		// Update the title to the prompt "Are you sure you want to
		// exit the application?"
		TextView body = (TextView) findViewById(R.id.promptDialogTitle);
		body.setText(R.string.exit_application);

		// Add handlers to the affirmative and negative buttons
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

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// Do nothing. They should have to choose one
		// of the options in order to leave this activity
	}
}
