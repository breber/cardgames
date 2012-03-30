package cs309.a1.player.activities;

import android.app.Activity;
import android.os.Bundle;
import cs309.a1.player.R;
import cs309.a1.shared.TextView;

public class PauseMenuActivity extends Activity{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_dialog);

		// Update the text on the dialog
		TextView tv = (TextView) findViewById(R.id.progressDialogText);
		tv.setText(R.string.gamePaused);
	}

	@Override
	public void onBackPressed() {
		// Do nothing. They can't leave from here until the tablet says so.
	}
}
