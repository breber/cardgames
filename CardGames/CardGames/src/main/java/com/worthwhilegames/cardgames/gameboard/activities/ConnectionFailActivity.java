package com.worthwhilegames.cardgames.gameboard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.AdActivity;
import com.worthwhilegames.cardgames.shared.Button;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;

/**
 * The Activtiy that gets displayed when the connection
 * with a user gets disconnected.
 *
 * Activity Results:
 * 		RESULT_OK - If the user chose to "Reconnect"
 * 		RESULT_CANCELLED - If the player is to be dropped
 *
 * 		In both cases, there will be an Intent passed back with
 * 		the MAC address of the user this is referring to as a string
 * 		extra with key BluetoothConstants.KEY_DEVICE_ID.
 */
public class ConnectionFailActivity extends AdActivity {

	/**
	 * The Intent that is passed back to the Activity that
	 * started this Activity
	 */
	private Intent resultIntent = new Intent();

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disconnectedplayer);

		// Add the MAC address of the player this Activity is referring to
		String playerId = getIntent().getStringExtra(ConnectionConstants.KEY_DEVICE_ID);
		resultIntent.putExtra(ConnectionConstants.KEY_DEVICE_ID, playerId);

		// If they choose to drop the player, finish this activity
		// with a status of RESULT_CANCELLED
		Button dropPlayer = (Button) findViewById(R.id.dropPlayer);
		dropPlayer.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
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
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
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
		setResult(RESULT_CANCELED, resultIntent);
		finish();
	}
}
