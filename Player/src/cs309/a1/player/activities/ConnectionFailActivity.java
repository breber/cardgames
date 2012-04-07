package cs309.a1.player.activities;

import android.app.Activity;
import android.os.Bundle;
import cs309.a1.player.R;
import cs309.a1.shared.TextView;

/**
 * The Activtiy that gets displayed when the connection
 * with the Tablet gets disconnected. All this does is show
 * the waiting to reconnect screen.
 * 
 * Activity Results:
 * 		RESULT_CANCELLED - Always
 */
public class ConnectionFailActivity extends Activity{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_dialog);

		// Update the title of the progress dialog
		TextView tv = (TextView) findViewById(R.id.progressDialogText);
		tv.setText(R.string.waiting);

		setResult(RESULT_CANCELED);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}
}
