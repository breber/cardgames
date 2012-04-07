package cs309.a1.player.activities;

import android.app.Activity;
import android.os.Bundle;
import cs309.a1.player.R;
import cs309.a1.shared.TextView;

/**
 * The Pause Menu. It isn't really a menu, just a popup
 * telling the user that the game is paused. It will automatically
 * close when the tablet indicates that the game has been resumed.
 */
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

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// Do nothing. They can't leave from here until the tablet says so.
	}
}
