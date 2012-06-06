package com.worthwhilegames.cardgames.gameboard.activities;

import static com.worthwhilegames.cardgames.shared.Constants.PREFERENCES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.worthwhilegames.cardgames.shared.TextView;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.activities.QuitGameActivity;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;
import com.worthwhilegames.cardgames.shared.connection.ConnectionServer;

/**
 * The Activity that the user will spend the most time in.  This
 * is where the Game logic happens, and each player will be able to
 * play a turn.
 */
public class GameboardActivity extends Activity {

	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = GameboardActivity.class.getName();

	/**
	 * The request code to keep track of the Pause Menu activity
	 */
	private static final int PAUSE_GAME = Math.abs("PAUSE_GAME".hashCode());

	/**
	 * The request code to keep track of the "Are you sure you want to quit"
	 * activity
	 */
	private static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());

	/**
	 * The request code to keep track of the "You have been disconnected"
	 * activity
	 */
	public static final int DISCONNECTED = Math.abs("DISCONNECTED".hashCode());

	/**
	 * The request code to keep track of the "Player N Won!" activity
	 */
	private static final int DECLARE_WINNER = Math.abs("DECLARE_WINNER".hashCode());

	/**
	 * LayoutParams for adding a card to a player on the long edge of the screen
	 * 
	 * width  = WRAP_CONTENT
	 * height = cardHeight
	 */
	private static LinearLayout.LayoutParams paramsH;

	/**
	 * LayoutParams for adding a card to a player on the short edge of the screen
	 * 
	 * width  = cardHeight
	 * height = WRAP_CONTENT
	 */
	private static LinearLayout.LayoutParams paramsV;

	/**
	 * The height of each card
	 */
	private static int cardHeight;

	/**
	 * The height of each button
	 */
	private static int buttonHeight;

	/**
	 * Parameters specific to a players position are stored here so that
	 * we can just reference them by their position, instead of having a bunch
	 * of if-elseif-else logic in the card placement code
	 */
	private static PlayerLayoutParams[] playerParams = new PlayerLayoutParams[4];

	/**
	 * Holds the scaled Bitmaps of the suit images
	 */
	private static Bitmap[] scaledSuitImages = new Bitmap[4];

	/**
	 * The ConnectionServer that sends and receives messages from other devices
	 */
	private ConnectionServer connection;

	/**
	 * This will handle the specific logic of the game chosen, it will follow
	 * the turn logic and Connection communication with players and also control
	 * the current game board state
	 */
	private GameController gameController;

	/**
	 * These are the TextViews for all the player names
	 */
	private TextView[] playerTextViews = new TextView[4];

	/**
	 * These are the LinearLayouts for all the player cards
	 */
	private LinearLayout[] playerLinearLayouts = new LinearLayout[4];

	/**
	 * The discard pile ImageView
	 */
	private ImageView discard;

	/**
	 * The draw pile ImageView
	 */
	private ImageView draw;

	/**
	 * The current suit ImageView
	 */
	private ImageView suitView;

	/**
	 * The SharedPreferences used to store preferences for the game
	 */
	private SharedPreferences sharedPreferences;

	/**
	 * The BroadcastReceiver for handling messages from the Connection connection
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
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
					Intent i = new Intent(GameboardActivity.this, ConnectionFailActivity.class);
					i.putExtra(ConnectionConstants.KEY_DEVICE_ID, intent.getStringExtra(ConnectionConstants.KEY_DEVICE_ID));
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

	// Set up playerParams on initial Class load
	static {
		playerParams[0] = new GameboardActivity.PlayerLayoutParams(Constants.MAX_DISPLAYED, 0) {
			@Override
			public boolean displayHalfCard(int numCards, int totalInHand) {
				return numCards != totalInHand - 1;
			}
		};

		playerParams[1] = new GameboardActivity.PlayerLayoutParams(Constants.MAX_DIS_SIDES, -90) {
			@Override
			public boolean displayHalfCard(int numCards, int totalInHand) {
				return numCards != 0;
			}
		};

		playerParams[2] = new GameboardActivity.PlayerLayoutParams(Constants.MAX_DISPLAYED, 0) {
			@Override
			public boolean displayHalfCard(int numCards, int totalInHand) {
				return numCards != totalInHand - 1;
			}
		};

		playerParams[3] = new GameboardActivity.PlayerLayoutParams(Constants.MAX_DIS_SIDES, 90) {
			@Override
			public boolean displayHalfCard(int numCards, int totalInHand) {
				return numCards != totalInHand - 1;
			}
		};
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameboard);
		initUIElements();

		// Update the refresh button image
		ImageView refresh = (ImageView) findViewById(R.id.gameboard_refresh);
		refresh.setImageBitmap(scaleButton(R.drawable.refresh_button));

		// Register the BroadcastReceiver to handle all
		// messages from the Connection module
		registerReceiver();

		connection = ConnectionServer.getInstance(this);

		sharedPreferences = getSharedPreferences(PREFERENCES, MODE_WORLD_READABLE);

		int numOfConnections = connection.getConnectedDeviceCount();
		List<Player> players = new ArrayList<Player>();
		List<String> devices = connection.getConnectedDevices();

		// Get the list of players and their addresses from the
		// Intent data (from the ConnectActivity)
		NameDevWrapper[] playerNames = getPlayerNames(getIntent());
		int i;
		for (i = 0; i < numOfConnections; i++) {
			Player p = new Player();
			int deviceIndex = devices.indexOf(playerNames[i].deviceId);

			// If we can't find the device in the device list,
			// then set it as a computer
			if (deviceIndex == -1) {
				p.setIsComputer(true);
			} else {
				p.setId(devices.get(deviceIndex));
			}
			p.setName(playerNames[i].name);
			p.setPosition(i + 1);
			players.add(p);

			// Show the user names we got back
			if (Util.isDebugBuild()) {
				Log.d(TAG, "Player" + (i + 1) + ": " + playerNames[i]);
			}
		}

		// Setup the rest of the Computer players based on the preferences
		int numComputers = sharedPreferences.getInt(Constants.NUMBER_OF_COMPUTERS, 1);
		String computerDifficulty = sharedPreferences.getString(Constants.DIFFICULTY_OF_COMPUTERS, Constants.EASY);
		for (int j = i; j < 4 && (j - i < numComputers); j++) {
			Player p = new Player();
			p.setName("Computer " + (j - i + 1));
			p.setId("Computer" + (j - i + 1));
			p.setPosition(j + 1);
			p.setIsComputer(true);
			p.setComputerDifficulty(computerDifficulty);

			players.add(p);
		}

		// the GameController now handles the setup of the game.
		gameController = GameFactory.getGameControllerInstance(this, connection, players, refresh);
		Game game = GameFactory.getGameInstance(this);
		game.setComputerDifficulty(computerDifficulty);

		// Draw the names from the Game on the gameboard
		updateNamesOnGameboard();
	}

	/**
	 * Set up all the references to UI elements
	 */
	private void initUIElements() {
		// Get references to commonly used UI elements
		playerTextViews[0] = (TextView) findViewById(R.id.player1text);
		playerTextViews[1] = (TextView) findViewById(R.id.player2text);
		playerTextViews[2] = (TextView) findViewById(R.id.player3text);
		playerTextViews[3] = (TextView) findViewById(R.id.player4text);

		playerLinearLayouts[0] = (LinearLayout) findViewById(R.id.player1ll);
		playerLinearLayouts[1] = (LinearLayout) findViewById(R.id.player2ll);
		playerLinearLayouts[2] = (LinearLayout) findViewById(R.id.player3ll);
		playerLinearLayouts[3] = (LinearLayout) findViewById(R.id.player4ll);

		discard = (ImageView) findViewById(R.id.discardpile);
		draw = (ImageView) findViewById(R.id.drawpile);
		suitView = (ImageView) findViewById(R.id.gameboard_suit);

		// Set up the scale factors for the card images
		int screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
		cardHeight = screenHeight / 4;
		buttonHeight = screenHeight / 6;

		// Update the size of the text in the name TextViews
		for (TextView tv : playerTextViews) {
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenHeight / 15);
		}

		// Set up the layout params for the cards
		paramsH = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, cardHeight);
		paramsV = new LinearLayout.LayoutParams(cardHeight, LinearLayout.LayoutParams.WRAP_CONTENT);

		// Create the scaled suit images
		scaledSuitImages[0] = scaleButton(R.drawable.clubsuitimage);
		scaledSuitImages[1] = scaleButton(R.drawable.diamondsuitimage);
		scaledSuitImages[2] = scaleButton(R.drawable.heartsuitimage);
		scaledSuitImages[3] = scaleButton(R.drawable.spadesuitimage);

		// Add the handler for the pause button
		ImageView pause = (ImageView) findViewById(R.id.gameboard_pause);
		pause.setImageBitmap(scaleButton(R.drawable.pause_button));
		pause.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				gameController.pause();
				Intent pauseButtonClick = new Intent(GameboardActivity.this, PauseMenuActivity.class);
				startActivityForResult(pauseButtonClick, PAUSE_GAME);
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, QuitGameActivity.class);
		startActivityForResult(intent, QUIT_GAME);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// Disconnect Connection
		if (connection != null) {
			connection.disconnect();
		}

		// Unregister the receiver
		try {
			unregisterReceiver(receiver);
		} catch (IllegalArgumentException e) {
			// We didn't get far enough to register the receiver
		}

		super.onDestroy();
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

	/**
	 * Unregister the BroadcastReceiver from all messages
	 */
	public void unregisterReceiver() {
		// Unregister the receiver
		try {
			unregisterReceiver(receiver);
		} catch (IllegalArgumentException e) {
			// We didn't get far enough to register the receiver
		}
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

	/**
	 * This method will get all the player names from the intent and set them up
	 * on the gameboard.xml with the text views
	 *
	 * @return List of player names
	 */
	public NameDevWrapper[] getPlayerNames(Intent intent) {
		NameDevWrapper[] playerNames = new NameDevWrapper[4];

		playerNames[0] = new NameDevWrapper(intent.getStringArrayExtra(Constants.PLAYER_1));
		playerNames[1] = new NameDevWrapper(intent.getStringArrayExtra(Constants.PLAYER_2));
		playerNames[2] = new NameDevWrapper(intent.getStringArrayExtra(Constants.PLAYER_3));
		playerNames[3] = new NameDevWrapper(intent.getStringArrayExtra(Constants.PLAYER_4));

		if (Util.isDebugBuild()) {
			Log.d(TAG, Arrays.toString(playerNames));
		}

		return playerNames;
	}

	/**
	 * Update the names that are displayed on the Gameboard.
	 *
	 * This data is pulled from the Game instance
	 */
	public void updateNamesOnGameboard() {
		// TODO: can we do this without adding spaces?
		List<Player> players = GameFactory.getGameInstance(this).getPlayers();
		for (int i = 0; i < 4; i++) {
			if (i < players.size()) {
				playerTextViews[i].setVisibility(View.VISIBLE);
				int blankSpaces = (Constants.NAME_MAX_CHARS - players.get(i).getName().length())/2;
				String spaces = "";
				for (int x = 0; x < blankSpaces; x++) {
					spaces += " ";
				}
				playerTextViews[i].setText(spaces + players.get(i).getName() + spaces);
			} else {
				playerTextViews[i].setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * This method will update the suit on the gameboard message center to show the player
	 * the current suit of the last card played
	 * 
	 * @param suit the suit of the card in which to change the picture to
	 */
	public void updateSuit(int suit) {
		if (suit >= 0 && suit < 4) {
			suitView.setImageBitmap(scaledSuitImages[suit]);
			suitView.setVisibility(View.VISIBLE);
		} else {
			suitView.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Updates the User Interface
	 * 
	 * Places all cards in the users' hands
	 * Updates the discard image
	 * Updates the draw card image
	 */
	public void updateUi() {
		Game game = GameFactory.getGameInstance(this);
		List<Player> players = game.getPlayers();
		int i = 0;

		// Place images for all player's cards
		for (Player p : players) {
			List<Card> cards = p.getCards();
			playerLinearLayouts[i].removeAllViews();

			for (int j = 0; j < cards.size(); j++) {
				Card c = cards.get(j);
				ImageView image = new ImageView(this);
				image.setId(c.getIdNum());
				image.setScaleType(ScaleType.FIT_CENTER);

				// TODO: customizable back
				int resId = R.drawable.back_blue_1;

				// If we are in debug mode, show the face
				// Otherwise stick with the back of the card
				if (Util.isDebugBuild()) {
					resId = c.getResourceId();
				}

				// Scale card
				Bitmap scaledCard = scaleCard(resId, i, playerParams[i].displayHalfCard(j, cards.size()));;

				// Rotate if necessary
				Matrix tempMatrix = new Matrix();
				tempMatrix.postRotate(playerParams[i].rotate);
				scaledCard = Bitmap.createBitmap(scaledCard, 0, 0,
						scaledCard.getWidth(), scaledCard.getHeight(), tempMatrix, true);

				image.setImageBitmap(scaledCard);

				// Check for max displayed
				boolean display = playerLinearLayouts[i].getChildCount() < playerParams[i].maxDisplayed;
				if (display) {
					if (i == 1 || i == 3) {
						playerLinearLayouts[i].addView(image, paramsV);
					} else {
						playerLinearLayouts[i].addView(image, paramsH);
					}
				} else {
					// TODO: add count of how many cards are not shown
				}
			}

			i++;
		}

		// Place Discard Image
		Bitmap discardImage = scaleCard(game.getDiscardPileTop().getResourceId(), 0, false);
		discard.setImageBitmap(discardImage);

		// Place Draw Image
		Bitmap drawImage = scaleCard(R.drawable.back_blue_1, 0, false);// TODO: customize back of card
		draw.setImageBitmap(drawImage);
	}

	/**
	 * Scale a card image with the given resource
	 * 
	 * @param resId the resource id of the card to scale
	 * @return a scaled card image
	 */
	private Bitmap scaleCard(int resId, int position, boolean halfCard) {
		Bitmap fullCard = BitmapFactory.decodeResource(getResources(), resId);
		float scaleFactor = (cardHeight + 0.0f) / fullCard.getHeight();
		Matrix tempMatrix = new Matrix();
		tempMatrix.setScale(scaleFactor, scaleFactor);

		// Draw half card
		if (halfCard) {
			return Bitmap.createBitmap(fullCard, 0, 0,
					fullCard.getWidth() / 2, fullCard.getHeight(), tempMatrix, true);
		} else {
			return Bitmap.createBitmap(fullCard, 0, 0,
					fullCard.getWidth(), fullCard.getHeight(), tempMatrix, true);
		}
	}

	/**
	 * Scale a button image with the given resource
	 * 
	 * @param resId the resource id of the card to scale
	 * @return a scaled button image
	 */
	private Bitmap scaleButton(int resId) {
		Bitmap fullImage = BitmapFactory.decodeResource(getResources(), resId);
		float scaleFactor = (buttonHeight + 0.0f) / fullImage.getHeight();
		Matrix tempMatrix = new Matrix();
		tempMatrix.setScale(scaleFactor, scaleFactor);

		return Bitmap.createBitmap(fullImage, 0, 0,
				fullImage.getWidth(), fullImage.getHeight(), tempMatrix, true);
	}

	/**
	 * Highlight the name of the person whose turn it is
	 *
	 * @param playerNumber the player whose turn it is
	 */
	public void highlightPlayer(int playerNumber) {
		for (int i = 0; i < 4; i++) {
			if ((i + 1) == playerNumber) {
				playerTextViews[i].setTextColor(getResources().getColor(R.color.gold));
			} else {
				playerTextViews[i].setTextColor(getResources().getColor(R.color.black));
			}
		}
	}


	/**
	 * A class that contains a device name and id
	 */
	private class NameDevWrapper {
		/**
		 * Create a NameDevWrapper from a string array
		 * 
		 * 		playerName[0] = deviceId
		 * 		playerName[1] = name
		 * 
		 * @param playerName an array of a device id and a name
		 * 			as defined above
		 */
		public NameDevWrapper(String[] playerName) {
			if (playerName != null) {
				name = playerName[1];
				deviceId = playerName[0];
			}
		}

		/**
		 * The name of this NameDevWrapper
		 */
		public String name;

		/**
		 * The device ID of this NameDevWrapper
		 */
		public String deviceId;
	}

	/**
	 * Information needed for a player's layout
	 */
	private abstract static class PlayerLayoutParams {
		/**
		 * The maximum number of cards to display for this user
		 */
		public int maxDisplayed;

		/**
		 * The amount to rotate each card
		 */
		public int rotate;

		/**
		 * @param maxDisplayed
		 * @param rotate
		 */
		public PlayerLayoutParams(int maxDisplayed, int rotate) {
			this.maxDisplayed = maxDisplayed;
			this.rotate = rotate;
		}

		/**
		 * Should we create a half card?
		 * 
		 * @param numCards the number of cards currently displayed
		 * @param totalInHand the total number of cards in the user's hand
		 * @return whether to generate a half card for this card
		 */
		public abstract boolean displayHalfCard(int numCards, int totalInHand);
	}

}
