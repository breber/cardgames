package com.worthwhilegames.cardgames.player.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.TextView;
import com.worthwhilegames.cardgames.shared.activities.QuitGameActivity;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;

/**
 * The Pause Menu. It isn't really a menu, just a popup
 * telling the user that the game is paused. It will automatically
 * close when the tablet indicates that the game has been resumed.
 *
 * Activity Result:
 * 		RESULT_OK - if the game is resumed
 * 		RESULT_CANCELLED - if the game is ending
 */
public class PauseMenuActivity extends Activity {

	/**
	 * The request code to keep track of the Quit Game activity
	 */
	private static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());

	/**
	 * The BroadcastReceiver for handling messages from the Bluetooth connection
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		/* (non-Javadoc)
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// Handle the end game, unpause and refresh messages from Bluetooth
			if (ConnectionConstants.MESSAGE_RX_INTENT.equals(action)) {
				int messageType = intent.getIntExtra(ConnectionConstants.KEY_MESSAGE_TYPE, -1);

				if (messageType == Constants.MSG_END_GAME) {
					PauseMenuActivity.this.setResult(RESULT_CANCELED);
					PauseMenuActivity.this.finish();
				} else if (messageType == Constants.MSG_UNPAUSE || messageType == Constants.MSG_REFRESH) {
					PauseMenuActivity.this.setResult(RESULT_OK);
					PauseMenuActivity.this.finish();
				}
			}
		}
	};

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_dialog);
		setResult(RESULT_OK);

		// Register the receiver for message/state change intents
		registerReceiver(receiver, new IntentFilter(ConnectionConstants.MESSAGE_RX_INTENT));

		// Update the text on the dialog
		TextView tv = (TextView) findViewById(R.id.progressDialogText);
		tv.setText(R.string.gamePaused);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(receiver);
		} catch (IllegalArgumentException e) {
			// We didn't get far enough to register the receiver
		}

		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// Start the "Are you sure you want to quit the game?" activity
		Intent mainMenu = new Intent(PauseMenuActivity.this, QuitGameActivity.class);
		startActivityForResult(mainMenu, QUIT_GAME);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// If the user clicked yes in the quit game activity, finish this activity
		if (requestCode == QUIT_GAME && resultCode == RESULT_OK) {
			setResult(RESULT_CANCELED);
			finish();
		}
	}
}
