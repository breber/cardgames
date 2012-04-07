package cs309.a1.gameboard.activities;

import static cs309.a1.crazyeights.Constants.PLAYER_NAME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import cs309.a1.crazyeights.Constants;
import cs309.a1.gameboard.R;
import cs309.a1.shared.TextView;
import cs309.a1.shared.Util;
import cs309.a1.shared.bluetooth.BluetoothConstants;
import cs309.a1.shared.bluetooth.BluetoothServer;

/**
 * This Activity will show how many players are connected, and
 * will allow them to start the game if there are enough players.
 */
public class ConnectActivity extends Activity {
	/**
	 * The request code to keep track of the Bluetooth request enable intent
	 */
	private static final int REQUEST_ENABLE_BT = Math.abs("REQUEST_BLUETOOTH".hashCode());

	/**
	 * An array of ImageViews. These are the "tablet" images that light up when a player
	 * has connected and is waiting for the game to begin.
	 */
	private ImageView[] playerImageViews = new ImageView[4];

	/**
	 * An array of TextViews.  These are the labels inside the ImageViews that will have
	 * the names of the players.
	 */
	private TextView[] playerTextViews = new TextView[4];

	/**
	 * A reference to the BluetoothAdapter. This allows us to check if Bluetooth is enabled.
	 */
	private BluetoothAdapter mBluetoothAdapter;

	/**
	 * A reference to the BluetoothServer that allows us to keep track of how many devices
	 * are currently connected to this device.
	 */
	private BluetoothServer mBluetoothServer;

	/**
	 * A Map of the MAC address to the names of the players
	 */
	private Map<String, String> playerNames = new HashMap<String, String>();

	/**
	 * The BroadcastReceiver that handles state change messages from the Bluetooth module
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String object = intent.getStringExtra(BluetoothConstants.KEY_MESSAGE_RX);
			int messageType = intent.getIntExtra(BluetoothConstants.KEY_MESSAGE_TYPE, -1);

			String action = intent.getAction();

			if (BluetoothConstants.MESSAGE_RX_INTENT.equals(action)) {
				if (messageType == Constants.GET_PLAYER_NAME) {
					String deviceAddress = intent.getStringExtra(BluetoothConstants.KEY_DEVICE_ID);
					List<String> deviceIDs = mBluetoothServer.getConnectedDevices();

					// TODO: what is this loop for?
					for (int i = 0; i < mBluetoothServer.getConnectedDeviceCount(); i++) {
						if (deviceIDs.get(i).equals(deviceAddress)) {
							break;
						}
					}

					//TODO make game not able to start without all players reporting names.
					// 		I think making the game unable to start without all players names
					//		could go down where we enable/disable the start button in updatePlayersConnected()
					try {
						JSONObject obj = new JSONObject(object);
						String playerName = obj.getString(PLAYER_NAME);
						playerNames.put(deviceAddress, playerName);
					} catch (JSONException ex) {
						ex.printStackTrace();
					}
				}
			} else if (BluetoothConstants.STATE_CHANGE_INTENT.equals(action)) {
				int state = intent.getIntExtra(BluetoothConstants.KEY_STATE_MESSAGE, BluetoothConstants.STATE_LISTEN);

				// If we are now in the LISTEN state, remove the player's name from the list
				if (state == BluetoothConstants.STATE_LISTEN) {
					String deviceID = intent.getStringExtra(BluetoothConstants.KEY_DEVICE_ID);
					playerNames.remove(deviceID);
				}
			}

			// Update the UI to indicate how many players are connected
			updatePlayersConnected();
		}
	};

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);

		// Register the BroadcastReceiver for receiving state change messages from
		// the Bluetooth module
		registerReceiver(receiver, new IntentFilter(BluetoothConstants.MESSAGE_RX_INTENT));
		registerReceiver(receiver, new IntentFilter(BluetoothConstants.STATE_CHANGE_INTENT));

		// Get the ImageView and TextView references so that we can display different
		// states for connected/disconnected devices
		playerImageViews[0] = (ImageView) findViewById(R.id.connectDeviceP1);
		playerImageViews[1] = (ImageView) findViewById(R.id.connectDeviceP2);
		playerImageViews[2] = (ImageView) findViewById(R.id.connectDeviceP3);
		playerImageViews[3] = (ImageView) findViewById(R.id.connectDeviceP4);

		playerTextViews[0] = (TextView) findViewById(R.id.connectDeviceP1TextView);
		playerTextViews[1] = (TextView) findViewById(R.id.connectDeviceP2TextView);
		playerTextViews[2] = (TextView) findViewById(R.id.connectDeviceP3TextView);
		playerTextViews[3] = (TextView) findViewById(R.id.connectDeviceP4TextView);

		// Get the BluetoothAdapter for doing operations with Bluetooth
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If Bluetooth isn't enabled, request that it be enabled
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			// Bluetooth is already enabled, so put ourselves in listening mode
			startListeningForDevices();
		}

		// Display this device's name so that users know which device to connect
		// to on their own device.
		TextView tv = (TextView) findViewById(R.id.myName);
		tv.setText(getResources().getString(R.string.deviceName) + "\n" + mBluetoothAdapter.getName());

		// Set up the start button
		Button startButton = (Button) findViewById(R.id.startButton);
		startButton.setEnabled(false);
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (canStartGame()) {
					mBluetoothServer.stopListening();

					// Send a message to all connected devices saying that the game is beginning
					mBluetoothServer.write(BluetoothConstants.MSG_TYPE_INIT, null);

					// Start the Gameboard activity
					Intent gameIntent = new Intent(ConnectActivity.this, GameboardActivity.class);
					int i = 0;
					for (String s : playerNames.keySet()) {
						if (i == 0) {
							gameIntent.putExtra(Constants.PLAYER_1, playerNames.get(s));
						} else if (i == 1) {
							gameIntent.putExtra(Constants.PLAYER_2, playerNames.get(s));
						} else if (i == 2) {
							gameIntent.putExtra(Constants.PLAYER_3, playerNames.get(s));
						} else if (i == 3) {
							gameIntent.putExtra(Constants.PLAYER_4, playerNames.get(s));
						}
						i++;
					}

					startActivity(gameIntent);

					// Finish this activity so we can't get back here when pressing the back button
					setResult(RESULT_OK);
					finish();
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		mBluetoothServer.stopListening();

		try {
			unregisterReceiver(receiver);
		} catch (IllegalArgumentException e) {
			// We didn't get far enough to register the receiver
		}

		super.onDestroy();
	}

	/**
	 * Returns whether or not a game can be started or not
	 * 
	 * - There needs to be at least 2 devices connected
	 * 
	 * - If this is a debug build, allow just one connection
	 * 
	 * @return whether a game can be started or not
	 */
	private boolean canStartGame() {
		if (Util.isDebugBuild()) {
			return mBluetoothServer.getConnectedDeviceCount() > 0;
		} else {
			return mBluetoothServer.getConnectedDeviceCount() > 1;
		}
	}

	/**
	 * Start the Bluetooth server listening for connections.
	 */
	private void startListeningForDevices() {
		mBluetoothServer = BluetoothServer.getInstance(this);
		Util.ensureDiscoverable(this, mBluetoothAdapter);
		mBluetoothServer.startListening();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// If we are coming back from the Bluetooth Enable request, and
		// it was successful, start listening for device connections
		if (resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT) {
			startListeningForDevices();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * Update the image views for the players indicating which ones
	 * are currently connected.
	 */
	private void updatePlayersConnected() {
		int i = 0;

		for (String s : mBluetoothServer.getConnectedDevices()) {
			if (Util.isDebugBuild()) {
				Toast.makeText(this, mBluetoothServer.getConnectedDevices().size() + "", Toast.LENGTH_SHORT).show();
				Toast.makeText(this, playerNames.get(s), Toast.LENGTH_SHORT).show();
			}

			// Set this user's device as the "on" screen
			playerImageViews[i].setImageResource(R.drawable.on_device);
			playerTextViews[i].setVisibility(View.VISIBLE);

			// Show either the Default name, or the player chosen
			// name on their device
			if (playerNames.get(s) == null) {
				playerTextViews[i].setText(R.string.default_name);
			} else{
				playerTextViews[i].setText(playerNames.get(s));
			}

			i++;
		}

		// Update the rest of the players with an off device and no name
		for ( ; i < 4; i++) {
			playerImageViews[i].setImageResource(R.drawable.off_device);
			playerTextViews[i].setVisibility(View.INVISIBLE);
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