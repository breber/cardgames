package com.worthwhilegames.cardgames.gameboard.activities;

import static com.worthwhilegames.cardgames.shared.Constants.PLAYER_NAME;
import static com.worthwhilegames.cardgames.shared.Constants.PREFERENCES;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.Game;
import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.Player;
import com.worthwhilegames.cardgames.shared.TextView;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;
import com.worthwhilegames.cardgames.shared.connection.ConnectionFactory;
import com.worthwhilegames.cardgames.shared.connection.ConnectionServer;
import com.worthwhilegames.cardgames.shared.connection.ConnectionType;

/**
 * This Activity will show how many players are connected, and
 * will allow them to start the game if there are enough players.
 * 
 * Activity Results:
 * 		RESULT_OK - if the Start Game button was pressed
 * 		RESULT_CANCELLED - if the user backed out of the Activity
 */
public class ConnectActivity extends Activity {

	/**
	 * Logcat debug tag
	 */
	private static final String TAG = ConnectActivity.class.getName();

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
	 * An array of ProgressBars. These are the progress bars inside the ImageViews
	 * that will display when a player is connected, but hasn't submitted a name yet.
	 */
	private ProgressBar[] playerProgressBars = new ProgressBar[4];

	/**
	 * A reference to the ConnectionServer that allows us to keep track of how many devices
	 * are currently connected to this device.
	 */
	private ConnectionServer mConnectionServer;

	/**
	 * The current game
	 */
	private Game mGame = null;

	/**
	 * Are we reconnecting or just connecting?
	 */
	private boolean isReconnect = false;

	/**
	 * The BroadcastReceiver that handles state change messages from the Connection module
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String object = intent.getStringExtra(ConnectionConstants.KEY_MESSAGE_RX);
			int messageType = intent.getIntExtra(ConnectionConstants.KEY_MESSAGE_TYPE, -1);

			String action = intent.getAction();

			if (Util.isDebugBuild()) {
				Log.d(TAG, "onReceive: action: " + action);
				Log.d(TAG, "onReceive: msgType: " + messageType);
			}

			if (ConnectionConstants.MESSAGE_RX_INTENT.equals(action)) {
				if (messageType == Constants.GET_PLAYER_NAME) {
					String deviceAddress = intent.getStringExtra(ConnectionConstants.KEY_DEVICE_ID);

					try {
						JSONObject obj = new JSONObject(object);
						String playerName = obj.getString(PLAYER_NAME);

						if (Util.isDebugBuild()) {
							Log.d(TAG, "onReceive: deviceAddress: " + deviceAddress);
							Log.d(TAG, "onReceive: newPlayerName: " + playerName);
						}

						// Find the player in our game, and update their name
						for (Player p : mGame.getPlayers()) {
							if (p.getId().equalsIgnoreCase(deviceAddress)) {
								p.setName(playerName);
							}
						}
					} catch (JSONException ex) {
						ex.printStackTrace();
					}
				}
			} else if (ConnectionConstants.STATE_CHANGE_INTENT.equals(action)) {
				int state = intent.getIntExtra(ConnectionConstants.KEY_STATE_MESSAGE, ConnectionConstants.STATE_LISTEN);
				String deviceId = intent.getStringExtra(ConnectionConstants.KEY_DEVICE_ID);

				if (Util.isDebugBuild()) {
					Log.d(TAG, "onReceive: [" + deviceId + "]: new state = " + state);
				}

				// If we are now in the LISTEN state, remove the player's name from the list
				if (state == ConnectionConstants.STATE_LISTEN || state == ConnectionConstants.STATE_NONE) {
					if (!mGame.isActive()) {
						// If we haven't started a game yet, just drop the player
						mGame.dropPlayer(deviceId);
					} else {
						// We have started a game, so we want to mark this player as
						// disconnected so that we can potentially replace them
						for (Player p : mGame.getPlayers()) {
							if (p.getId().equalsIgnoreCase(deviceId)) {
								p.setDisconnected(true);
								p.clearName();
							}
						}
					}
				} else if (state == ConnectionConstants.STATE_CONNECTED) {
					boolean needToAdd = true;
					// If a player has been disconnected, their Player object
					// will say so. We first will check to see if any players
					// were disconnected. If so, this connection will take their
					// place. If not, we will add a new player for them
					for (Player p : mGame.getPlayers()) {
						if (p.isDisconnected()) {
							p.setId(deviceId);
							needToAdd = false;
						}
					}

					if (needToAdd) {
						// When we enter into the connected state, add a new player
						// to the game
						Player p = new Player();
						p.setId(deviceId);
						mGame.addPlayer(p);
					}
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
		registerReceiver(receiver, new IntentFilter(ConnectionConstants.MESSAGE_RX_INTENT));
		registerReceiver(receiver, new IntentFilter(ConnectionConstants.STATE_CHANGE_INTENT));

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

		playerProgressBars[0] = (ProgressBar) findViewById(R.id.connectDeviceP1ProgressBar);
		playerProgressBars[1] = (ProgressBar) findViewById(R.id.connectDeviceP2ProgressBar);
		playerProgressBars[2] = (ProgressBar) findViewById(R.id.connectDeviceP3ProgressBar);
		playerProgressBars[3] = (ProgressBar) findViewById(R.id.connectDeviceP4ProgressBar);

		mConnectionServer = ConnectionServer.getInstance(this);
		mGame = GameFactory.getGameInstance(this);
		isReconnect = mGame.getNumPlayers() > 0;


		// TODO: move this connection logic out to connection factory
		ConnectionType currentType = ConnectionFactory.getConnectionType(this);
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		// If Bluetooth isn't enabled, request that it be enabled (if we are currently using Bluetooth)
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled() && currentType == ConnectionType.Bluetooth) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else if (!wifiManager.isWifiEnabled() && currentType == ConnectionType.WiFi) {
			// Wifi is not currently enabled, so try and enable it
			wifiManager.setWifiEnabled(true);

			// Everything is already enabled, so put ourselves in listening mode
			startListeningForDevices();
		} else {
			// Everything is already enabled, so put ourselves in listening mode
			startListeningForDevices();
		}

		// Display this device's name so that users know which device to connect
		// to on their own device.
		TextView tv = (TextView) findViewById(R.id.myName);
		tv.setText(ConnectionFactory.getDeviceDisplayName(this));

		// Set up the start button
		Button startButton = (Button) findViewById(R.id.startButton);
		startButton.setEnabled(false);
		startButton.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				if (canStartGame()) {
					mConnectionServer.stopListening();

					// Send a message to all connected devices saying that the game is beginning
					mConnectionServer.write(ConnectionConstants.MSG_TYPE_INIT, null);

					// Update the position of each player
					int i = 1;
					for (Player p : mGame.getPlayers()) {
						p.setPosition(i);

						// If we have a disconnected player at this point,
						// just mark them as a computer
						if (p.isDisconnected()) {
							p.setIsComputer(true);
						}

						i++;
					}

					// If we aren't reconnecting, start the gameboard
					if (!isReconnect) {
						startActivity(new Intent(ConnectActivity.this, GameboardActivity.class));
					}

					setResult(RESULT_OK);
					finish();
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if (mConnectionServer != null) {
			mConnectionServer.stopListening();
		}

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
	 * - There needs to be at least 2 players
	 * - All connected players need to have submitted a name
	 *
	 * @return whether a game can be started or not
	 */
	private boolean canStartGame() {
		SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, 0);
		int maxComputers = sharedPreferences.getInt(Constants.NUMBER_OF_COMPUTERS, 1);
		boolean namesEntered = mGame.getPlayers().size() > 0;

		for (Player p : mGame.getPlayers()) {
			namesEntered &= p.hasName();
		}

		return namesEntered && ((mGame.getPlayers().size() + maxComputers) > 1);
	}

	/**
	 * Start the ConnectionServer server listening for connections.
	 */
	private void startListeningForDevices() {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "startListeningForDevices");
		}

		mConnectionServer.startListening();
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
		} else if (resultCode != RESULT_OK && requestCode == REQUEST_ENABLE_BT) {
			// The user didn't enable bluetooth - send them back to main menu
			setResult(RESULT_CANCELED);
			finish();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * Update the image views for the players indicating which ones
	 * are currently connected and display the names of players if
	 * they have set their name.
	 */
	private void updatePlayersConnected() {
		int i = 0;

		for (Player p : mGame.getPlayers()) {
			if (p.hasName()) {
				playerTextViews[i].setText(p.getName());
				playerImageViews[i].setImageResource(R.drawable.on_device);
				playerTextViews[i].setVisibility(View.VISIBLE);
				playerProgressBars[i].setVisibility(View.INVISIBLE);
			} else {
				if (p.isDisconnected()) {
					playerImageViews[i].setImageResource(R.drawable.off_device);
				} else {
					playerImageViews[i].setImageResource(R.drawable.on_device);
				}
				playerTextViews[i].setVisibility(View.INVISIBLE);
				playerProgressBars[i].setVisibility(View.VISIBLE);
			}

			i++;
		}

		// For the rest of the devices, set them to off
		while (i < 4) {
			playerImageViews[i].setImageResource(R.drawable.off_device);
			playerTextViews[i].setVisibility(View.INVISIBLE);
			playerProgressBars[i].setVisibility(View.INVISIBLE);

			i++;
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
