package com.worthwhilegames.cardgames.gameboard.activities;

import static com.worthwhilegames.cardgames.shared.Constants.PREFERENCES;
import static com.worthwhilegames.cardgames.shared.Constants.fourthCard;
import static com.worthwhilegames.cardgames.shared.Constants.fullCard;
import static com.worthwhilegames.cardgames.shared.Constants.halfCard;

import java.util.Collections;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.Game;
import com.worthwhilegames.cardgames.shared.GameController;
import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.Player;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.activities.GameViewActivity;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;
import com.worthwhilegames.cardgames.shared.connection.ConnectionServer;

/**
 * The Activity that the user will spend the most time in.  This
 * is where the Game logic happens, and each player will be able to
 * play a turn.
 */
public class GameboardActivity extends GameViewActivity {

	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = GameboardActivity.class.getName();

	/**
	 * The request code to keep track of the "Player N Won!" activity
	 */
	private static final int DECLARE_WINNER = Math.abs("DECLARE_WINNER".hashCode());

	/**
	 * This will handle the specific logic of the game chosen, it will follow
	 * the turn logic and Connection communication with players and also control
	 * the current game board state
	 */
	private GameController gameController;

	/**
	 * The game instance
	 */
	private Game mGame;

	/**
	 * Returns BroadcastReceiver for handling messages from the connection module
	 * @return BroadcastReceiver for handling messages from the connection module
	 */
	private BroadcastReceiver getBroadCastReceiver(){

		return new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

				if (Util.isDebugBuild()) {
					Log.d(TAG, "onReceive: " + action);
				}

				if (ConnectionConstants.STATE_CHANGE_INTENT.equals(action)) {
					// Handle a state change
					int newState = intent.getIntExtra(ConnectionConstants.KEY_STATE_MESSAGE, ConnectionConstants.STATE_NONE);

					// If the new state is anything but connected, display the
					// "You have been disconnected" screen
					if (newState != ConnectionConstants.STATE_CONNECTED) {
						String deviceId = intent.getStringExtra(ConnectionConstants.KEY_DEVICE_ID);
						for (Player p : mGame.getPlayers()) {
							if (p.getId().equalsIgnoreCase(deviceId)) {
								p.clearName();
								p.setDisconnected(true);
							}
						}

						Intent i = new Intent(GameboardActivity.this, ConnectionFailActivity.class);
						i.putExtra(ConnectionConstants.KEY_DEVICE_ID, deviceId);
						startActivityForResult(i, DISCONNECTED);

						// Pause the players
						gameController.pause();
					}
				} else {
					// We didn't handle the Broadcast message here, so pass it on to
					// the GameController
					gameController.handleBroadcastReceive(context, intent);
				}
			}
		};
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		receiver = getBroadCastReceiver();

		sharedPreferences = getSharedPreferences(PREFERENCES, 0);
		// Get the image to use for the back of a card
		CARD_BACK = sharedPreferences.getInt(Constants.PREF_CARD_BACK, R.drawable.back_blue_1);

		setContentView(R.layout.gameboardplayerhost);

		initUIElements();

		// Register the BroadcastReceiver to handle all
		// messages from the Connection module
		registerReceiver();

		connection = ConnectionServer.getInstance(this);
		mGame = GameFactory.getGameInstance(this);

		// Setup the rest of the Computer players based on the preferences
		int currentNumPlayers = mGame.getNumPlayers();
		int numComputers = sharedPreferences.getInt(Constants.PREF_NUMBER_OF_COMPUTERS, 3);
		int requiredNumPlayers = GameFactory.getRequiredNumPlayers(this);
		String computerDifficulty = sharedPreferences.getString(Constants.PREF_DIFFICULTY, Constants.EASY);

		// Add a new computer as long as we have less than 4 players AND
		//   -we have less than the required number of players for the game OR
		//   -we have less than the number of computers specified in the preferences
		for (int j = currentNumPlayers; j < 4 && ((j < requiredNumPlayers) || (j - currentNumPlayers < numComputers)); j++) {
			Player p = new Player();
			p.setName("Computer " + (j - currentNumPlayers + 1));
			p.setConnectedId("Computer" + (j - currentNumPlayers + 1));
			p.setPosition(j + 1);
			p.setIsComputer(true);
			p.setComputerDifficulty(computerDifficulty);

			mGame.addPlayer(p);
		}

		setupGame();

		// Set the computer Difficulty
		mGame.setComputerDifficulty(computerDifficulty);

		// Draw the names from the Game on the gameboard
		updateNamesOnGameboard();
	}

	/**
	 * Setup the game information
	 */
	private void setupGame() {
		synchronized (this) {
			// the GameController now handles the setup of the game.
			if (gameController == null) {

				if(Util.isPlayerHost()){
					// Initialize the buttons
					ViewStub buttonLayout = (ViewStub) findViewById(R.id.playerHandButtonView);
					buttonLayout.setLayoutResource(GameFactory.getPlayerButtonViewLayout(this));
					buttonLayout.inflate();

					// Set up the game controller to point to the player controller for message sending
					playerController =  GameFactory.getPlayerControllerInstance(this);
				}
				gameController = GameFactory.getGameControllerInstance(this, (ConnectionServer) connection);

				if(Util.isPlayerHost()){
					gameController.playerController = playerController;

					// Set up the player controller to point to the game controller for message sending
					//TODO I think this breaks all the laws of layering we should think of a better way to do this
					playerController.gameController = gameController;
				}
			}
		}
	}

	/**
	 * Register the BroadcastReceiver for MESSAGE_RX_INTENTs and
	 * STATE_CHANGE_INTENTs.
	 */
	public void registerReceiver() {
		// Register the receiver for message/state change intents
		registerReceiver(receiver, new IntentFilter(ConnectionConstants.MESSAGE_RX_INTENT));
		registerReceiver(receiver, new IntentFilter(ConnectionConstants.STATE_CHANGE_INTENT));
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == QUIT_GAME && resultCode == RESULT_OK) {
			// Let the users know that the game is over
			unregisterReceiver();
			gameController.sendGameEnd();


			// Finish this activity
			setResult(RESULT_OK);
			finish();
		} else if (requestCode == PAUSE_GAME) {
			if (resultCode == RESULT_CANCELED) {
				// On the Pause Menu, they selected something that will end
				// the game, so Finish this activity
				unregisterReceiver();

				// Let the users know that the game is over
				gameController.sendGameEnd();

				// And finish this activity
				setResult(RESULT_OK);
				finish();
			} else {
				gameController.unpause();
			}
		} else if (requestCode == DECLARE_WINNER) {
			// We are coming back from the winner screen, so just go back to the
			// main menu no matter what the result is.
			setResult(RESULT_OK);
			finish();
		} else {
			// If we didn't handle the result here, try handling it in the
			// GameController
			gameController.handleActivityResult(requestCode, resultCode, data);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}


	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.activities.GameViewActivity#updateNamesOnGameboard()
	 */
	@Override
	public void updateNamesOnGameboard() {
		List<Player> players = GameFactory.getGameInstance(this).getPlayers();
		for (int i = 0; i < 4; i++) {
			if (i < players.size()) {
				playerTextViews[i].setVisibility(View.VISIBLE);
				playerTextViews[i].setText(players.get(i).getName());
			} else {
				playerTextViews[i].setVisibility(View.INVISIBLE);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.activities.GameViewActivity#updateUi()
	 */
	@Override
	public void updateUi() {
		Game game = GameFactory.getGameInstance(this);
		List<Player> players = game.getPlayers();
		int i = 0;

		// Place images for all player's cards
		for (Player p : players) {
			List<Card> cards = p.getCards();
			Collections.sort(cards);
			playerLinearLayouts[i].removeAllViews();

			for (int j = 0; j < cards.size(); j++) {
				Card c = cards.get(j);
				ImageView image = new ImageView(this);
				image.setId(c.getIdNum());
				image.setScaleType(ScaleType.FIT_CENTER);

				int resId = CARD_BACK;

				// If we are in debug mode, show the face
				// Otherwise stick with the back of the card
				if (Util.isCheaterMode(this) || p.getIsPlayerHost()) {
					resId = c.getResourceId();
				}

				int cardsToDisplay = cards.size();
				if (cardsToDisplay > maxDisplayed[i] && !p.getIsPlayerHost()) {
					cardsToDisplay = maxDisplayed[i];
				}

				if(p.getIsPlayerHost()){
					image.setId(c.getIdNum());
					image.setImageBitmap(scaleCard(c.getResourceId(), fullCard));
					// Set the onClick Listener for selecting this card
					image.setOnClickListener(playerController.getCardClickListener());

					// Add a 5px border around the image
					image.setPadding(5, 5, 5, 5);
				} else {
					// Scale card
					Bitmap scaledCard = scaleCard(resId, (j < (cardsToDisplay - 1)) ? fourthCard : halfCard);
					image.setImageBitmap(scaledCard);
				}

				// Check for max displayed
				if (j < maxDisplayed[i]) {
					LinearLayout.LayoutParams params = cardParams;
					if(p.getIsPlayerHost()){
						params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, cardHeight);
					}
					playerLinearLayouts[i].addView(image, params);
					playerRemainingCards[i].setVisibility(View.INVISIBLE);
				} else {
					// Display how many cards are remaining that aren't displayed
					playerRemainingCards[i].setText("+" + Math.abs(maxDisplayed[i] - cards.size()));
					playerRemainingCards[i].setVisibility(View.VISIBLE);
					break;
				}
			}

			i++;
		}

		// Highlight whose turn it is
		highlightPlayer(game.whoseTurn);

		// Set all the cards in the center of the screen
		for (int j = 0; j < 4; j++) {
			Card c = game.getCardAtPosition(j + 1);
			if (c != null) {
				Bitmap scaledCard = scaleCard(c.getResourceId(), fullCard);

				centerCards[j].setImageBitmap(scaledCard);
				centerCards[j].setVisibility(View.VISIBLE);
			} else {
				centerCards[j].setVisibility(View.INVISIBLE);
			}
		}
	}


}
