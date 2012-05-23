package cs309.a1.gameboard.activities;

import static cs309.a1.shared.Constants.PREFERENCES;

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
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import cs309.a1.R;
import cs309.a1.shared.Card;
import cs309.a1.shared.Constants;
import cs309.a1.shared.Game;
import cs309.a1.shared.GameController;
import cs309.a1.shared.GameFactory;
import cs309.a1.shared.Player;
import cs309.a1.shared.TextView;
import cs309.a1.shared.Util;
import cs309.a1.shared.activities.QuitGameActivity;
import cs309.a1.shared.connection.ConnectionConstants;
import cs309.a1.shared.connection.ConnectionFactory;
import cs309.a1.shared.connection.ConnectionServer;

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
	 * The TextView that represents the player whose turn it currently is
	 */
	private TextView highlightedPlayer;

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
	 * The height of each card
	 */
	private static int cardHeight;

	/**
	 * The height of each button
	 */
	private static int buttonHeight;

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

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameboard);

		highlightedPlayer = null;

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

		// Set up the scale factors for the card images
		int screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
		cardHeight = screenHeight / 4;
		buttonHeight = screenHeight / 6;

		for (TextView tv : playerTextViews) {
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenHeight / 15);
		}

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

		// Update the refresh button image
		ImageView refresh = (ImageView) findViewById(R.id.gameboard_refresh);
		refresh.setImageBitmap(scaleButton(R.drawable.refresh_button));

		// Register the BroadcastReceiver to handle all
		// messages from the Connection module
		registerReceiver();

		connection = ConnectionFactory.getServerInstance(this);

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
		ImageView suitView = (ImageView) findViewById(R.id.gameboard_suit);

		// Based on the suit change the image
		if (suit == 0) {
			suitView.setImageBitmap(scaleButton(R.drawable.clubsuitimage));
			suitView.setVisibility(View.VISIBLE);
		} else if (suit == 1) {
			suitView.setImageBitmap(scaleButton(R.drawable.diamondsuitimage));
			suitView.setVisibility(View.VISIBLE);
		} else if (suit == 2) {
			suitView.setImageBitmap(scaleButton(R.drawable.heartsuitimage));
			suitView.setVisibility(View.VISIBLE);
		} else if (suit == 3) {
			suitView.setImageBitmap(scaleButton(R.drawable.spadesuitimage));
			suitView.setVisibility(View.VISIBLE);
		} else {
			suitView.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Places a card in the specified location on the game board
	 *
	 * @param location Location to place the card
	 * @param newCard Card to be placed on the game board
	 */
	public void updateUi() {
		Game game = GameFactory.getGameInstance(this);
		List<Player> players = game.getPlayers();
		int i = 0;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, cardHeight);

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
				if (Util.isDebugBuild()) {
					resId = c.getResourceId();
				}

				Bitmap scaledCard = scaleCard(resId);

				if (j == cards.size() - 1) {
					// Draw full card
					image.setImageBitmap(scaledCard);
				} else {
					// Draw half card
					Bitmap halfCard = Bitmap.createBitmap(scaledCard, 0, 0,
							scaledCard.getWidth() / 2,
							scaledCard.getHeight(), null, true);

					image.setImageBitmap(halfCard);
				}

				// Check for max displayed
				boolean display = true;
				if (i == 1 || i == 3) {
					display = playerLinearLayouts[i].getChildCount() < Constants.MAX_DISPLAYED;
				} else {
					display = playerLinearLayouts[i].getChildCount() < Constants.MAX_DIS_SIDES;
				}

				if (display) {
					playerLinearLayouts[i].addView(image, params);
				} else {
					// TODO: add count of how many cards are not shown
				}
			}

			i++;
		}

		// Place Discard Image
		Bitmap discardImage = scaleCard(game.getDiscardPileTop().getResourceId());
		discard.setImageBitmap(discardImage);

		// Place Draw Image
		Bitmap drawImage = scaleCard(R.drawable.back_blue_1);// TODO: customize back of card
		draw.setImageBitmap(drawImage);
	}

	/**
	 * Scale a card image with the given resource
	 * 
	 * @param resId the resource id of the card to scale
	 * @return a scaled card image
	 */
	private Bitmap scaleCard(int resId) {
		Bitmap fullCard = BitmapFactory.decodeResource(getResources(), resId);
		float scaleFactor = (cardHeight + 0.0f) / fullCard.getHeight();
		Matrix tempMatrix = new Matrix();
		tempMatrix.setScale(scaleFactor, scaleFactor);

		return Bitmap.createBitmap(fullCard, 0, 0,
				fullCard.getWidth(),
				fullCard.getHeight(), tempMatrix, true);
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
				fullImage.getWidth(),
				fullImage.getHeight(), tempMatrix, true);
	}

	/**
	 * Highlight the name of the person whose turn it is
	 *
	 * @param playerNumber the player whose turn it is
	 */
	public void highlightPlayer(int playerNumber) {
		if (highlightedPlayer != null) {
			highlightedPlayer.setTextColor(Color.BLACK);
		}

		if (playerNumber == 1) {
			highlightedPlayer = (TextView) findViewById(R.id.player1text);
		} else if (playerNumber == 2) {
			highlightedPlayer = (TextView) findViewById(R.id.player2text);
		} else if (playerNumber == 3) {
			highlightedPlayer = (TextView) findViewById(R.id.player3text);
		} else if (playerNumber == 4) {
			highlightedPlayer = (TextView) findViewById(R.id.player4text);
		}

		highlightedPlayer.setTextColor(getResources().getColor(R.color.gold));
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

}
