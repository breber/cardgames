package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cs309.a1.gameboard.R;
import cs309.a1.shared.Button;
import cs309.a1.shared.bluetooth.BluetoothConstants;

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

		String playerId = getIntent().getStringExtra(BluetoothConstants.KEY_DEVICE_ID);

		final Intent resultIntent = new Intent();
		resultIntent.putExtra(BluetoothConstants.KEY_DEVICE_ID, playerId);


		// If they choose to drop the player, finish this activity
		// with a status of RESULT_CANCELLED
		Button dropPlayer = (Button) findViewById(R.id.dropPlayer);
		dropPlayer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED, resultIntent);
				finish();
			}
		});

		// If they choose to reconnect, finish this activity
		// with a status of RESULT_OK
		Button reconnect = (Button) findViewById(R.id.reconnect);
		reconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// If they choose to click the back button, treat
		// it as if they clicked the Drop Player button
		setResult(RESULT_CANCELED);
		finish();
	}
}
