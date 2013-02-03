package com.worthwhilegames.cardgames.shared;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.gameboard.activities.ConnectActivity;
import com.worthwhilegames.cardgames.gameboard.activities.GameResultsActivity;
import com.worthwhilegames.cardgames.gameboard.activities.GameboardActivity;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;
import com.worthwhilegames.cardgames.shared.connection.ConnectionServer;

/**
 * The GameController should be used by the GameBoardActivity to do all of the
 * game specific operations, such as controlling whose turn it is, handling
 * cards played, declaring winner, sending pause and end game messages, and
 * managing the state of the players. specific GameControllers should be created
 * for each specific game that is played
 */
public abstract class GameController {

	/**
	 * The Logcat Debug tag
	 */
	protected final String TAG = this.getClass().getName();

	/**
	 * The request code to keep track of the "Player N Won!" activity
	 */
	protected static final int DECLARE_WINNER = Math.abs("DECLARE_WINNER".hashCode());

	/**
	 * request code to allow the gameboard to choose which player to connect
	 */
	protected static final int CHOOSE_PLAYER = Math.abs("CHOOSE_PLAYER".hashCode());

	/**
	 * The ConnectionServer that sends and receives messages from other devices
	 */
	protected ConnectionServer server;

	/**
	 * This is the refresh button that is on the GameBoard The GameController
	 * will handle any button presses
	 */
	private ImageView refreshButton;

	/**
	 * the list of current active players
	 */
	protected List<Player> players;

	/**
	 * This game object will keep track of the current state of the game and be
	 * used to manage player hands and draw and discard piles
	 */
	protected static Game game = null;

	/**
	 * This player state object will be used to update each player on their state completely
	 */
	protected PlayerStateFull pStateFull = new PlayerStateFull();

	/**
	 * This will be 0 to 3 to indicate the spot in the players array for the
	 * player currently taking their turn
	 */
	protected int whoseTurn = 0;

	/**
	 * This is the context of the GameBoardActivity this allows this class to
	 * call methods and activities as if it were in the GameBoardActivity
	 */
	protected GameboardActivity gameContext;

	/**
	 * The implementation of the Game Rules
	 * needs to be initialized in constructor.
	 */
	protected Rules gameRules;

	/**
	 * The sound manager
	 */
	protected SoundManager mySM;

	/**
	 * This is how to tell if a play computer turn activity is currently running
	 */
	protected boolean isComputerPlaying = false;

	/**
	 * Represents whether the game is paused or not
	 */
	protected boolean isPaused = false;

	/**
	 * Handler to handle a computer's turn
	 */
	@SuppressLint("HandlerLeak")
	protected Handler computerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, "handleMessage: about to play a card");
			}

			if (!isPaused && players.get(whoseTurn).getIsComputer() && isComputerPlaying) {
				isComputerPlaying = false;
				playComputerTurn();
			} else {
				if (Util.isDebugBuild()) {
					Log.d(TAG, "handleMessage: game paused. not going to play now");
				}
			}
		}
	};

	/**
	 * This will initialize the common parts of the game controller
	 * 
	 * @param context
	 * @param connectionGiven
	 */
	public void initGameController(GameboardActivity context, ConnectionServer connectionGiven){
		gameContext = context;
		server = connectionGiven;
		mySM = SoundManager.getInstance(context);
		refreshButton = (ImageView) context.findViewById(R.id.gameboard_refresh);

		refreshButton.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				v.setEnabled(false);
				refreshPlayers();
				v.setEnabled(true);
			}
		});
	}


	/**
	 * This method is meant to be used inside the broadcast receiver
	 * to handle messages from the connection module
	 * 
	 * @param context context of the broadcast
	 * @param intent intent of the broadcast, message is stored here
	 */
	public abstract void handleBroadcastReceive(Context context, Intent intent);

	/**
	 * This method will return the player index that sent the message to the gameboard.
	 * This will be used to determine if the correct player has sent a message, and
	 * not allow players to play out of turn.
	 * @param context
	 * @param intent
	 * @return - index of the player that sent the message (whoseTurn if we don't know)
	 */
	protected int getWhoSentMessage(Context context, Intent intent) {
		String sender = intent.getStringExtra(ConnectionConstants.KEY_DEVICE_ID);

		if (Util.isDebugBuild()) {
			Log.d(TAG, "Sender: " + sender);
		}

		if (sender != null) {
			for (int i = 0; i < players.size(); i++) {
				Player p = players.get(i);
				if (sender.equalsIgnoreCase(p.getId())) {
					return i;
				}
			}
		}

		if (Util.isDebugBuild()) {
			Log.w(TAG, "Can't figure out sender...: " + sender);
		}

		return whoseTurn;
	}

	/**
	 * This will allow the GameController to handle whenever an activity
	 * finishes and returns to the gameboard
	 * 
	 * @param requestCode the request code used to start the activity that we are
	 *            getting the result of
	 * @param resultCode the result code from the activity
	 * @param data the intent which may contain data from the activity that has finished
	 */
	public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GameboardActivity.DISCONNECTED) {
			if (resultCode == Activity.RESULT_CANCELED) {
				// We chose to drop the player, so let the Game know to do that
				String playerId = data.getStringExtra(ConnectionConstants.KEY_DEVICE_ID);
				game.dropPlayer(playerId);
				refreshPlayers();
				unpause();
			} else if (resultCode == Activity.RESULT_OK) {
				// We chose to add a new player, so start the ConnectActivity
				// with the deviceId and isReconnect parameters
				Intent i = new Intent(gameContext, ConnectActivity.class);
				i.putExtra(ConnectionConstants.KEY_DEVICE_ID, data.getStringExtra(ConnectionConstants.KEY_DEVICE_ID));
				gameContext.startActivityForResult(i, CHOOSE_PLAYER);

				// We will initially drop the player, to handle the case where
				// they don't actually reconnect a player in the Connect Screen.
				game.dropPlayer(data.getStringExtra(ConnectionConstants.KEY_DEVICE_ID));

				// Unregister the receiver so that we don't get an annoying
				// popup when we are on the activity
				gameContext.unregisterReceiver();
			}

			return true;
		} else if (requestCode == CHOOSE_PLAYER) {
			// We are coming back from the reconnect player screen
			if (Util.isDebugBuild()) {
				Log.d(TAG, "onActivityResult: CHOOSE_PLAYER");
			}

			// Send the refresh signal to all players just to make
			// sure everyone has the latest information
			refreshPlayers();

			// Unpause the players
			unpause();

			// Re-register the broadcast receivers
			gameContext.registerReceiver();

			// Update the gameboard with the correct player names
			gameContext.updateNamesOnGameboard();

			// Send the refresh signal (again) to all players just to make
			// sure everyone has the latest information
			refreshPlayers();
			return true;
		}

		return false;
	}

	/**
	 * This will take in the received card and play it
	 * 
	 * @param object
	 *            This object is a JSON object that has been received as a
	 *            played card
	 */
	protected Card playReceivedCard(String object) {
		Card tmpCard = new Card(0, 0, 0, 0);
		try {
			JSONObject obj = new JSONObject(object);
			tmpCard = Card.createCardFromJSON(obj);

			//tell the game what was played
			game.discard(players.get(whoseTurn), tmpCard);

			//update UI
			gameContext.updateUi();
		} catch (JSONException ex) {
			ex.printStackTrace();
		}

		mySM.playCardSound();
		return tmpCard;
	}

	/**
	 * Sends a pause message to all the players so they are not able to play
	 * while the game is paused
	 */
	public void pause() {
		isPaused = true;

		for (int i = 0; i < game.getNumPlayers(); i++) {
			server.write(Constants.MSG_PAUSE, null, players.get(i).getId());
		}
	}

	/**
	 * Un-pauses all the players
	 */
	public void unpause() {
		for (int i = 0; i < game.getNumPlayers(); i++) {
			server.write(Constants.MSG_UNPAUSE, null, players.get(i).getId());
		}

		isPaused = false;

		// If a computer was playing before the game was paused
		// let them know that they can play now
		if (isComputerPlaying) {
			computerHandler.sendEmptyMessage(0);
		}
	}

	/**
	 * Tell the players to end the game, since the gameboard is ending
	 */
	public void sendGameEnd() {
		isComputerPlaying = true; // stop computer from playing
		for (int i = 0; i < game.getNumPlayers(); i++) {
			server.write(Constants.MSG_END_GAME, null, players.get(i).getId());
		}
	}

	/**
	 * This will send winner and loser messages to all the players depending on
	 * if they won or not
	 * 
	 * @param winner
	 *            The name of the winner
	 */
	protected void declareWinner(String winner) {
		// Have the tablet verbally congratulate the winner
		mySM.speak(gameContext.getResources().getString(R.string.congratulationMessage).replace("%s", winner));

		// Start the GameResultsActivity
		Intent gameResults = new Intent(gameContext, GameResultsActivity.class);
		gameResults.putExtra(GameResultsActivity.WINNER_NAME, winner);
		gameContext.startActivityForResult(gameResults, DECLARE_WINNER);

		// Unregister the BroadcastReceiver so that we ignore disconnection
		// messages from users
		gameContext.unregisterReceiver();
	}

	/**
	 * This will refresh the state of all the players by sending them their
	 * cards and if it is their turn
	 */
	protected abstract void refreshPlayers();

	/**
	 * Start a computer's turn.
	 * 
	 * Starts another thread that waits, and then posts a message to the
	 * mHandler letting it know it can play.
	 */
	protected void startComputerTurn() {
		isComputerPlaying = true;

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(Constants.COMPUTER_WAIT_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (Util.isDebugBuild()) {
					Log.d(TAG, "startComputerTurn: letting computer know it can play");
				}

				computerHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	/**
	 * This will play the computer's turn based on computer difficulty in preferences
	 */
	protected abstract void playComputerTurn();

}
