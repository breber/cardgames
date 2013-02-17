package com.worthwhilegames.cardgames.gameboard.activities;

import static com.worthwhilegames.cardgames.shared.Constants.GET_PLAYER_NAME;
import static com.worthwhilegames.cardgames.shared.Constants.KEY_PLAYER_NAME;
import static com.worthwhilegames.cardgames.shared.Constants.PREFERENCES;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.player.activities.EnterNameActivty;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.Game;
import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.Player;
import com.worthwhilegames.cardgames.shared.TextView;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;
import com.worthwhilegames.cardgames.shared.connection.ConnectionFactory;
import com.worthwhilegames.cardgames.shared.connection.ConnectionServer;

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
	 * An array of ImageViews. These are the "tablet" images that light up when a player
	 * has connected and is waiting for the game to begin.
	 */
	private RelativeLayout[] playerImageViews = new RelativeLayout[4];

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
	 * Represents that we are starting
	 */
	private boolean isStarting = false;

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
			String deviceAddress = intent.getStringExtra(ConnectionConstants.KEY_DEVICE_ID);

			String action = intent.getAction();
			if (ConnectionConstants.MESSAGE_RX_INTENT.equals(action)) {
				if (messageType == Constants.MSG_PLAYER_NAME) {
					try {
						JSONObject obj = new JSONObject(object);
						String playerName = obj.getString(KEY_PLAYER_NAME);

						if (Util.isDebugBuild()) {
							Log.d(TAG, "onReceive: deviceAddress: " + deviceAddress);
							Log.d(TAG, "onReceive: newPlayerName: " + playerName);
						}

						// Find the player in our game, and update their name
						boolean foundPlayer = false;
						for (Player p : mGame.getPlayers()) {
							if (p.getId().equalsIgnoreCase(deviceAddress)) {
								p.setName(playerName);
								foundPlayer = true;
								break;
							}
						}

						if (Util.isDebugBuild() && !foundPlayer) {
							Log.e(TAG, "Received request to update name, but didn't find player...");
						}
					} catch (JSONException ex) {
						ex.printStackTrace();
					}
				}
			} else if (ConnectionConstants.STATE_CHANGE_INTENT.equals(action)) {
				int state = intent.getIntExtra(ConnectionConstants.KEY_STATE_MESSAGE, ConnectionConstants.STATE_LISTEN);

				if (Util.isDebugBuild()) {
					Log.d(TAG, "onReceive: [" + deviceAddress + "]: new state = " + state);
				}

				// If we are now in the LISTEN state, remove the player's name from the list
				if (state == ConnectionConstants.STATE_LISTEN || state == ConnectionConstants.STATE_NONE) {
					if (!mGame.isActive()) {
						// If we haven't started a game yet, just drop the player
						mGame.dropPlayer(deviceAddress);
					} else {
						// We have started a game, so we want to mark this player as
						// disconnected so that we can potentially replace them
						for (Player p : mGame.getPlayers()) {
							if (p.getId().equalsIgnoreCase(deviceAddress)) {
								p.setDisconnected(true);
								p.clearName();
							}
						}
					}
				} else if (state == ConnectionConstants.STATE_CONNECTED) {
					boolean needToAdd = true;
					for (Player p : mGame.getPlayers()) {
						if (p.getId().equalsIgnoreCase(deviceAddress)) {
							// Set deviceId again to make sure they aren't listed
							// as disconnected
							p.setConnectedId(deviceAddress);
							needToAdd = false;
							break;
						}
					}

					if (needToAdd) {
						// If a player has been disconnected, their Player object
						// will say so. We first will check to see if any players
						// were disconnected. If so, this connection will take their
						// place. If not, we will add a new player for them
						for (Player p : mGame.getPlayers()) {
							if (p.isDisconnected()) {
								p.setConnectedId(deviceAddress);
								needToAdd = false;
							}
						}
					}

					if (needToAdd && !isReconnect) {
						// If we didn't already have a Player object for the given deviceId
						// (they were trying to reconnect), or there aren't any disconnected
						// players, and we are not on the reconnect screen, add a new player to the
						// current game.
						Player p = new Player();
						p.setConnectedId(deviceAddress);
						mGame.addPlayer(p);
					}
				}
			} else if (ConnectionFactory.CONNECTION_ENABLED.equals(action)) {
				// If we get a notification that the connection has been
				// enabled (a Wifi connection has been started), update
				// the name we display on the connection screen, and
				// start listening for connections
				updateName();
				startListeningForDevices();
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
		registerReceiver(receiver, new IntentFilter(ConnectionFactory.CONNECTION_ENABLED));

		// Get the ImageView and TextView references so that we can display different
		// states for connected/disconnected devices
		playerImageViews[0] = (RelativeLayout) findViewById(R.id.connectDeviceP1);
		playerImageViews[1] = (RelativeLayout) findViewById(R.id.connectDeviceP2);
		playerImageViews[2] = (RelativeLayout) findViewById(R.id.connectDeviceP3);
		playerImageViews[3] = (RelativeLayout) findViewById(R.id.connectDeviceP4);

		playerTextViews[0] = (TextView) findViewById(R.id.connectDeviceP1TextView);
		playerTextViews[1] = (TextView) findViewById(R.id.connectDeviceP2TextView);
		playerTextViews[2] = (TextView) findViewById(R.id.connectDeviceP3TextView);
		playerTextViews[3] = (TextView) findViewById(R.id.connectDeviceP4TextView);

		playerProgressBars[0] = (ProgressBar) findViewById(R.id.connectDeviceP1ProgressBar);
		playerProgressBars[1] = (ProgressBar) findViewById(R.id.connectDeviceP2ProgressBar);
		playerProgressBars[2] = (ProgressBar) findViewById(R.id.connectDeviceP3ProgressBar);
		playerProgressBars[3] = (ProgressBar) findViewById(R.id.connectDeviceP4ProgressBar);


		// Update ImageView sizes
		int screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
		int screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		int deviceHeight = screenHeight / 3 - 10;
		int deviceWidth = screenWidth / 4;

		RelativeLayout connectingDevice = (RelativeLayout) findViewById(R.id.connectDeviceTablet);
		connectingDevice.setLayoutParams(new RelativeLayout.LayoutParams(deviceWidth, deviceHeight));
		connectingDevice.setEnabled(true);

		// Update the device name size
		TextView tv = (TextView) findViewById(R.id.myName);
		tv.setMaxHeight(deviceHeight);

		for (RelativeLayout v : playerImageViews) {
			v.setLayoutParams(new RelativeLayout.LayoutParams(deviceWidth, deviceHeight));
			v.setEnabled(false);
		}

		mConnectionServer = ConnectionServer.getInstance(this);
		mGame = GameFactory.getGameInstance(this);
		isReconnect = mGame.getNumPlayers() > 0;

		if(Util.isPlayerHost()){
			Player p = new Player();
			p.setPlayerHost(true);
			mGame.addPlayer(p);

			// Start Intent to get player host name
			Intent getName = new Intent(ConnectActivity.this, EnterNameActivty.class);
			startActivityForResult(getName, GET_PLAYER_NAME);
		}

		// Start listening for devices
		startListeningForDevices();
		updateName();

		// Update the UI to indicate how many players are connected
		updatePlayersConnected();

		// Set up the start button
		final Button startButton = (Button) findViewById(R.id.startButton);
		startButton.setEnabled(false);
		startButton.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				if (canStartGame()) {
					if (!isStarting) {
						isStarting = true;
						startButton.setEnabled(false);
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

		// Finish the get player name activity if it has been started
		finishActivity(GET_PLAYER_NAME);

		try {
			unregisterReceiver(receiver);
		} catch (IllegalArgumentException e) {
			// We didn't get far enough to register the receiver
		}

		super.onDestroy();
	}

	/**
	 * Update the name displayed on the device
	 */
	private void updateName() {
		// Display this device's name so that users know which device to connect
		// to on their own device.
		TextView tv = (TextView) findViewById(R.id.myName);
		tv.setText(ConnectionFactory.getDeviceDisplayName(this));
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
		int maxComputers = sharedPreferences.getInt(Constants.PREF_NUMBER_OF_COMPUTERS, 3);
		boolean namesEntered = mGame.getPlayers().size() > 0;

		for (Player p : mGame.getPlayers()) {
			namesEntered &= (p.hasName() || p.isDisconnected());
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

		if (requestCode == GET_PLAYER_NAME && resultCode == RESULT_OK) {
			String playerName = data.getStringExtra(KEY_PLAYER_NAME);

			for(Player p : mGame.getPlayers()) {
				if(p.getIsPlayerHost()){
					p.setName(playerName);
					updatePlayersConnected();
					break;
				}
			}
		} else {
			// If we are coming back from the Bluetooth Enable request, and
			// it was successful, start listening for device connections
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
				playerImageViews[i].setEnabled(true);
				playerTextViews[i].setVisibility(View.VISIBLE);
				playerProgressBars[i].setVisibility(View.INVISIBLE);
			} else {
				if (p.isDisconnected()) {
					playerImageViews[i].setEnabled(false);
				} else {
					playerImageViews[i].setEnabled(true);
				}
				playerTextViews[i].setVisibility(View.INVISIBLE);
				playerProgressBars[i].setVisibility(View.VISIBLE);
			}

			i++;
		}

		// For the rest of the devices, set them to off
		while (i < 4) {
			playerImageViews[i].setEnabled(false);
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
