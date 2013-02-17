package com.worthwhilegames.cardgames.shared;

import static com.worthwhilegames.cardgames.shared.Constants.PREFERENCES;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.activities.GameViewActivity;
import com.worthwhilegames.cardgames.shared.connection.ConnectionClient;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;

/**
 * The PlayerController is meant to be used inside the ShowCardsActivity on a player's device
 * This will handle the cards sent to the player and also will allow the player to play cards.
 * This will check if a card can be played and allow the player to draw cards. Basically handles
 * the state of the player.
 */
public abstract class PlayerController {

	/**
	 * intent code for choosing suit
	 */
	protected static final int CHOOSE_SUIT = Math.abs("CHOOSE_SUIT".hashCode());

	/**
	 * The request code to keep track of the "Are you sure you want to quit"
	 * activity
	 */
	protected static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());

	/**
	 * An instance of the ShowCardsActivity that can be used to display cards
	 * and do other things as if this class was the ShowCardsActivity.
	 */
	protected GameViewActivity playerContext;

	/**
	 * The player state associated with this player
	 */
	protected PlayerState playerState = new PlayerState();

	/**
	 * TODO probably shouldn't have this??
	 * This will allow the player host to send messages to the gameController
	 */
	public GameController gameController;

	/**
	 * The play button on the layout also the pass and discard button
	 */
	protected Button play;

	/**
	 * The refresh button in the lower right hand corner
	 */
	protected ImageView refresh;

	/**
	 * The current selected Card
	 */
	protected Card cardSelected;

	/**
	 * The id of the suggested Card
	 */
	protected int cardSuggestedId = -1;

	/**
	 * This is the setting if the player would like to see card suggestions
	 */
	protected boolean isPlayAssistMode = false;

	/**
	 * The client that is used to send messages to the GameBoard
	 */
	protected ConnectionClient connection;

	/**
	 * This is a SoundManager instance that can do text to speech and other
	 * sounds.
	 */
	protected SoundManager mySM;

	/**
	 * The LinearLayout holding all card images
	 */
	protected LinearLayout playerHandLayout;

	public void initPlayerController(Activity context){
		playerContext = (GameViewActivity) context;

		mySM = SoundManager.getInstance(context);
		playerHandLayout = (LinearLayout) playerContext.findViewById(R.id.playerCardContainer);

		// Refresh button press will ask gameboard for updated state.
		refresh = (ImageView) playerContext.findViewById(R.id.gameboard_refresh);
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setEnabled(false);
				sendMessage(Constants.MSG_REFRESH, null);
				v.setEnabled(true);
			}
		});

		// set up play assist mode
		SharedPreferences sharedPreferences = playerContext.getSharedPreferences(PREFERENCES, 0);
		isPlayAssistMode = sharedPreferences.getBoolean(Constants.PREF_PLAY_ASSIST_MODE, false);

		connection = ConnectionClient.getInstance(context);
	}

	/**
	 * This method is handles messages received from the Connection module
	 * 
	 * @param context context of the broadcast
	 * @param intent intent from the broadcast, message is stored here
	 */
	public void handleBroadcastReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (ConnectionConstants.MESSAGE_RX_INTENT.equals(action)) {
			String object = intent.getStringExtra(ConnectionConstants.KEY_MESSAGE_RX);
			int messageType = intent.getIntExtra(ConnectionConstants.KEY_MESSAGE_TYPE, -1);

			// handle message type
			handleMessage(messageType, object);
		}
	}

	/**
	 * This will allow the GameController to handle whenever an activity
	 * finishes and returns to the gameboard
	 * 
	 * @param requestCode the request code used to start the activity that we are
	 *            getting the result of
	 * @param resultCode the result code from the activity
	 * @param data the intent which may contain data from the activity that has finished
	 * @return true if the activity result was handled, false otherwise
	 */
	public abstract boolean handleActivityResult(int requestCode, int resultCode, Intent data);

	/**
	 * Sends message to the gameboard if the player is also the game board, the "player host", then
	 * send message directly to game controller instance on this device
	 * @param messageType message code of the message to be sent
	 * @param jsonObject json data to be sent
	 */
	public void sendMessage(int messageType, String jsonObject){
		if(Util.isPlayerHost() && gameController != null){
			gameController.handleMessage(messageType, jsonObject);
		} else {
			connection.write(messageType, jsonObject);
		}
	}

	/**
	 * This will return the ClickListener for selecting cards
	 * 
	 * @return the click listener for a card ImageView each card will need to be given this
	 */
	public abstract OnClickListener getCardClickListener();

	/**
	 * This will get the full state of the player

	 * @return full player state
	 */
	public PlayerState getPlayerState(){
		return this.playerState;
	}

	/**
	 * Returns if it is this player's turn
	 * @return if it is this player's turn
	 */
	protected boolean isMyTurn(){
		return this.playerState.whoseTurn == this.playerState.playerIndex;
	}

	/**
	 * Handles the message and json object that was received by this player
	 * @param messageType the message code received
	 * @param jsonObject the data received
	 */
	public abstract void handleMessage(int messageType, String jsonObject);

}
