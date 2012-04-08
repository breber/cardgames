package cs309.a1.player.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import cs309.a1.crazyeights.Constants;
import cs309.a1.player.R;
import cs309.a1.shared.TextView;
import cs309.a1.shared.bluetooth.BluetoothConstants;

/**
 * The Pause Menu. It isn't really a menu, just a popup
 * telling the user that the game is paused. It will automatically
 * close when the tablet indicates that the game has been resumed.
 */
public class PauseMenuActivity extends Activity{
	
	private static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());


	/**
	 * The BroadcastReceiver for handling messages from the Bluetooth connection
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothConstants.MESSAGE_RX_INTENT.equals(action)) {
				int messageType = intent.getIntExtra(BluetoothConstants.KEY_MESSAGE_TYPE, -1);
				if (messageType == Constants.UNPAUSE || messageType == Constants.REFRESH){
					finish();
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
		
		// Register the receiver for message/state change intents
		registerReceiver(receiver, new IntentFilter(BluetoothConstants.MESSAGE_RX_INTENT));
			
		// Update the text on the dialog
		TextView tv = (TextView) findViewById(R.id.progressDialogText);
		tv.setText(R.string.gamePaused);
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
