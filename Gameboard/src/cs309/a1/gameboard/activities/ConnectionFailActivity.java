package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.os.Bundle;
import cs309.a1.gameboard.R;

/**
 * The Activtiy that gets displayed when the connection
 * with a user gets disconnected.
 */
public class ConnectionFailActivity extends Activity{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disconnectedplayer);
		
		// TODO: set up buttons
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
