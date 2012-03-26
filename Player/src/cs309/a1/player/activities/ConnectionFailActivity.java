package cs309.a1.player.activities;

import android.app.Activity;
import android.os.Bundle;
import cs309.a1.player.R;

/**
 * The Activtiy that gets displayed when the connection
 * with the Tablet gets disconnected. All this does is show
 * the waiting to reconnect screen.
 */
public class ConnectionFailActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.waitingtoreconnect);
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}
}
