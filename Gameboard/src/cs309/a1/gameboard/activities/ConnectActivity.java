package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import cs309.a1.gameboard.R;
import cs309.a1.shared.TextView;
import cs309.a1.shared.Util;
import cs309.a1.shared.bluetooth.BluetoothConstants;
import cs309.a1.shared.bluetooth.BluetoothServer;

public class ConnectActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 10;
	private int numPlayers = 0;
	private ImageView[] ImageViews = new ImageView[5];

	private Context mContext;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothServer mBluetoothServer;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int tmpNumPlayers = mBluetoothServer.getConnectedDeviceCount();

			if (Util.isDebugBuild()) {
				Toast.makeText(mContext, "Bluetooth change. Players: " + tmpNumPlayers, Toast.LENGTH_LONG).show();
			}

			if (numPlayers != tmpNumPlayers) {
				numPlayers = tmpNumPlayers;
				updatePlayersConnected();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);

		ImageViews[0] = (ImageView) findViewById(R.id.ImageViewTablet);
		// tablet is imageview 0 rest are by player number
		ImageViews[1] = (ImageView) findViewById(R.id.ImageViewP1);
		ImageViews[2] = (ImageView) findViewById(R.id.ImageViewP2);
		ImageViews[3] = (ImageView) findViewById(R.id.ImageViewP3);
		ImageViews[4] = (ImageView) findViewById(R.id.ImageViewP4);

		mContext = this;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		registerReceiver(receiver, new IntentFilter(BluetoothConstants.STATE_CHANGE_INTENT));

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			startListeningForDevices();
		}

		// Display this device's name so that users know which device to connect
		// to on their own device.
		TextView tv = (TextView) findViewById(R.id.myName);
		tv.setText(getResources().getString(R.string.deviceName) + "\n" + mBluetoothAdapter.getName());

		Button startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (canStartGame()) {
					mBluetoothServer.stopListening();

					// Send a message to all connected devices saying that the game is beginning
					mBluetoothServer.write(BluetoothConstants.MSG_TYPE_INIT, null);

					Intent i = new Intent(ConnectActivity.this,	GameboardActivity.class);
					startActivity(i);

					// Finish this activity so we can't get back here when pressing the back button
					setResult(RESULT_OK);
					finish();
				}
			}
		});

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		mBluetoothServer.stopListening();

		super.onDestroy();
	}

	/**
	 * Returns whether or not a game can be started or not
	 * 
	 * - There needs to be at least 2 devices connected
	 * 
	 * @return whether a game can be started or not
	 */
	private boolean canStartGame() {
		return mBluetoothServer.getConnectedDeviceCount() > 0; // TODO: should be 1, changed to 0 for testing...
	}

	private void startListeningForDevices() {
		mBluetoothServer = BluetoothServer.getInstance(this);
		Util.ensureDiscoverable(mContext, mBluetoothAdapter);
		mBluetoothServer.startListening();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT) {
			startListeningForDevices();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void updatePlayersConnected() {
		int i;
		mBluetoothServer.getConnectedDevices();

		if(numPlayers > 0){
			ImageViews[1].setImageResource(R.drawable.on_device_p1);
		}
		if(numPlayers > 1){
			ImageViews[2].setImageResource(R.drawable.on_device_p2);
		}
		if(numPlayers > 2){
			ImageViews[3].setImageResource(R.drawable.on_device_p3);
		}
		if(numPlayers > 3){
			ImageViews[4].setImageResource(R.drawable.on_device_p4);
		}

		// grey out the other players
		for (i=numPlayers+1; i <= 4; i++) {
			ImageViews[i].setImageResource(R.drawable.off_device);
		}

		// Update the state of the button so that it changes colors when there
		// are enough players
		Button b = (Button) findViewById(R.id.startButton);
		if (canStartGame()) {
			b.setEnabled(true);
		} else {
			b.setEnabled(false);
		}
	}
}