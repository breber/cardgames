package com.worthwhilegames.cardgames.player.activities;

import static com.worthwhilegames.cardgames.shared.Constants.PREFERENCES;
import static com.worthwhilegames.cardgames.shared.Constants.fourthCard;
import static com.worthwhilegames.cardgames.shared.Constants.fullCard;
import static com.worthwhilegames.cardgames.shared.Constants.halfCard;
import static com.worthwhilegames.cardgames.shared.Constants.halfCardVertCut;

import java.util.ArrayList;
import java.util.Collections;
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
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.gameboard.activities.PauseMenuActivity;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.Game;
import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.PlayerController;
import com.worthwhilegames.cardgames.shared.PlayerStateFull;
import com.worthwhilegames.cardgames.shared.TextView;
import com.worthwhilegames.cardgames.shared.activities.QuitGameActivity;
import com.worthwhilegames.cardgames.shared.connection.ConnectionClient;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;

/**
 * This is the Activity that handles Game Play
 */
public class ShowCardsActivity extends Activity {
	/**
	 * The request code to keep track of the connect device activity
	 */
	private static final int CONNECT_DEVICE = Math.abs("CONNECT_DEVICE".hashCode());

	/**
	 * The request code to keep track of the "Are you sure you want to quit" activity
	 */
	private static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());

	/**
	 * The request code to keep track of the "You have been disconnected" activity
	 */
	private static final int DISCONNECTED = Math.abs("DISCONNECTED".hashCode());

	/**
	 * The request code to pause the game
	 */
	private static final int PAUSE_GAME = Math.abs("PAUSE_GAME".hashCode());

	/**
	 * LayoutParams for adding a card to a player on the long edge of the screen
	 * 
	 * width  = WRAP_CONTENT
	 * height = cardHeight
	 */
	private static LinearLayout.LayoutParams cardParams;

	/**
	 * The height of each card
	 */
	private static int cardHeight;

	/**
	 * The height of each button
	 */
	private static int buttonHeight;

	/**
	 * Represents the resource id to use for the back of the cards
	 */
	private static int CARD_BACK;

	/**
	 * The maximum number of cards displayed for each player
	 */
	private static int[] maxDisplayed = new int[] { Constants.MAX_DISPLAYED, Constants.MAX_DIS_SIDES, Constants.MAX_DISPLAYED, Constants.MAX_DIS_SIDES };

	/**
	 * Holds the scaled Bitmaps of the suit images
	 */
	private static Bitmap[] scaledSuitImages = new Bitmap[4];

	/**
	 * The ConnectionClient used to send messages to the server
	 */
	private ConnectionClient connection;

	/**
	 * The PlayerController for handling a lot of the Game-Specific logic
	 */
	private PlayerController playerController;

	/**
	 * The game instance
	 */
	private Game mGame;

	/**
	 * These are the TextViews for all the player names
	 */
	private TextView[] playerTextViews = new TextView[4];

	/**
	 * These are the LinearLayouts for all the player cards
	 */
	private LinearLayout[] playerLinearLayouts = new LinearLayout[4];

	/**
	 * These are the TextViews for the count of remaining cards not being displayed
	 */
	private TextView[] playerRemainingCards = new TextView[4];

	/**
	 * The LinearLayout holding all card images
	 */
	private LinearLayout playerHandLayout;

	/**
	 * The ImageViews for the cards in the center of the screen
	 * 
	 * For games that don't use 4 cards in the middle:
	 * Position 2 = discard pile
	 * Position 4 = draw pile
	 */
	private ImageView[] centerCards = new ImageView[4];

	/**
	 * The current suit ImageView
	 */
	private ImageView suitView;

	/**
	 * The SharedPreferences used to store preferences for the game
	 */
	private SharedPreferences sharedPreferences;

	/**
	 * The BroadcastReceiver for handling messages from the connection module
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			int messageType = intent.getIntExtra(ConnectionConstants.KEY_MESSAGE_TYPE, -1);
			if (ConnectionConstants.STATE_CHANGE_INTENT.equals(action)) {
				// Handle a state change
				int newState = intent.getIntExtra(ConnectionConstants.KEY_STATE_MESSAGE, ConnectionConstants.STATE_NONE);

				// If the new state is anything but connected, display the "You have been disconnected" screen
				if (newState != ConnectionConstants.STATE_CONNECTED) {
					Intent i = new Intent(ShowCardsActivity.this, ConnectionFailActivity.class);
					startActivityForResult(i, DISCONNECTED);
				}
			} else if (messageType == Constants.MSG_PAUSE) {
				Intent pause = new Intent(ShowCardsActivity.this, PauseMenuActivity.class);
				ShowCardsActivity.this.startActivityForResult(pause, PAUSE_GAME);
			} else if (messageType == Constants.MSG_END_GAME) {
				unregisterReceiver();
				ShowCardsActivity.this.setResult(RESULT_OK);
				finish();
			} else {
				if (playerController == null) {
					setupGame();
				}

				// Give it up to the player controller to deal with
				playerController.handleBroadcastReceive(context, intent);
			}
		}
	};

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameboardplayer);
		initUIElements();

		sharedPreferences = getSharedPreferences(PREFERENCES, 0);

		// Update the refresh button image
		ImageView refresh = (ImageView) findViewById(R.id.gameboard_refresh);
		refresh.setImageBitmap(scaleButton(R.drawable.refresh_button));

		// Get the image to use for the back of a card
		CARD_BACK = sharedPreferences.getInt(Constants.PREF_CARD_BACK, R.drawable.back_blue_1);

		// Register the receiver for message/state change intents
		registerReceiver(receiver, new IntentFilter(ConnectionConstants.MESSAGE_RX_INTENT));

		// Get an instance of the ConnectionClient so that we can
		// send messages back to the tablet
		connection = ConnectionClient.getInstance(this);

		// Set up the Layout for the cards
		playerHandLayout = (LinearLayout) findViewById(R.id.playerCardContainer);

		// Start the connection screen from here so that we can register the message receive
		// broadcast receiver so that we don't miss any messages
		Intent i = new Intent(this, ConnectActivity.class);
		startActivityForResult(i, CONNECT_DEVICE);
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

		playerLinearLayouts[0] = (LinearLayout) findViewById(R.id.playerCardContainer);
		playerLinearLayouts[1] = (LinearLayout) findViewById(R.id.player2ll);
		playerLinearLayouts[2] = (LinearLayout) findViewById(R.id.player3ll);
		playerLinearLayouts[3] = (LinearLayout) findViewById(R.id.player4ll);

		playerRemainingCards[0] = (TextView) findViewById(R.id.player1RemainingCount);
		playerRemainingCards[1] = (TextView) findViewById(R.id.player2RemainingCount);
		playerRemainingCards[2] = (TextView) findViewById(R.id.player3RemainingCount);
		playerRemainingCards[3] = (TextView) findViewById(R.id.player4RemainingCount);

		centerCards[0] = (ImageView) findViewById(R.id.cardPosition1);
		centerCards[1] = (ImageView) findViewById(R.id.cardPosition2);
		centerCards[2] = (ImageView) findViewById(R.id.cardPosition3);
		centerCards[3] = (ImageView) findViewById(R.id.cardPosition4);

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
		cardParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, cardHeight / 2);

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
				//TODO what do we do when the player pauses it?
				/*playerController.pause();
				Intent pauseButtonClick = new Intent(GameboardActivity.this, PauseMenuActivity.class);
				startActivityForResult(pauseButtonClick, PAUSE_GAME);*/
			}
		});

		//TODO add this back, textview.setrotation is causing issues
		//setupGoogleTv();
	}

	/*@TargetApi(Build.VERSION_CODES.HONEYCOMB)		// If this is a Google TV, rotate the text of player 3 so that it isn't upside down
		if (Util.isGoogleTv(this)) {
			if (null != playerTextViews[2]) {
				playerTextViews[2].setRotation(180);//TODO why is this not working?
			}
		}
	}*/

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
		// Disconnect connection
		if (connection != null) {
			connection.disconnect();
		}

		// Unregister broadcast receivers
		unregisterReceiver();

		super.onDestroy();
	}

	/**
	 * Unregister the broadcast receiver
	 */
	public void unregisterReceiver() {
		// Unregister all the receivers we may have registered
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
			// Finish this activity - if everything goes right, we
			// should be back at the main menu
			this.unregisterReceiver();
			setResult(RESULT_OK);
			finish();
		} else if (requestCode == DISCONNECTED) {
			// Whatever result we get from the disconnected activity,
			// just finish this activity since they will need to reconnect anyways.
			setResult(RESULT_CANCELED);
			finish();
		} else if (requestCode == CONNECT_DEVICE) {
			// If the user cancelled the device list, then bring them back to the main menu
			if (resultCode == RESULT_CANCELED) {
				setResult(RESULT_CANCELED);
				finish();
			} else {
				setupGame();

				String playerName = data.getStringExtra(Constants.KEY_PLAYER_NAME);
				playerController.setPlayerName(playerName);

				// Register the state change receiver
				registerReceiver(receiver, new IntentFilter(ConnectionConstants.STATE_CHANGE_INTENT));
			}
		} else if (requestCode == PAUSE_GAME && resultCode == RESULT_CANCELED ) {
			// Finish this activity - if everything goes right, we
			// should be back at the main menu
			setResult(RESULT_OK);
			finish();
		} else {
			// If it isn't anything we know how to handle, pass it on to the
			// playerController to try and handle it
			// Quit game and replay will be handled here
			playerController.handleActivityResult(requestCode, resultCode, data);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Setup the game information
	 */
	private void setupGame() {
		synchronized (this) {
			if (playerController == null) {
				// Initialize the buttons
				ViewStub buttonLayout = (ViewStub) findViewById(R.id.playerHandButtonView);
				buttonLayout.setLayoutResource(GameFactory.getPlayerButtonViewLayout(this));
				buttonLayout.inflate();

				// Get the player controller instance
				playerController = GameFactory.getPlayerControllerInstance(this, new ArrayList<Card>());
			}
		}
	}

	/**
	 * Adds and displays a card in the player's hand
	 *
	 * @param newCard Card to be added to the hand
	 */
	public void addCard(Card newCard) {
		List<Card> Cards = playerController.getPlayerState().cards;

		// Make sure the hand is sorted
		Collections.sort(Cards);

		// Remove all cards from the display
		playerHandLayout.removeAllViews();

		// edit layout attributes
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, cardHeight);

		for (Card c : Cards) {
			// create ImageView to hold Card
			ImageView toAdd = new ImageView(this);
			toAdd.setImageBitmap(scaleCard(c.getResourceId()));
			toAdd.setId(c.getIdNum());
			toAdd.setOnClickListener(playerController.getCardClickListener());

			// Add a 5px border around the image
			toAdd.setPadding(5, 5, 5, 5);

			playerHandLayout.addView(toAdd, params);
		}
	}

	/**
	 * Scale a card image with the given resource
	 * 
	 * This is for cards in the players hand
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
				fullCard.getWidth(), fullCard.getHeight(), tempMatrix, true);
	}

	/**
	 * Update the names that are displayed on the Gameboard.
	 *
	 * This data is pulled from the Game instance
	 */
	public void updateNamesOnGameboard() {
		PlayerStateFull pStateFull = playerController.getPlayerState();

		int curPlayerIndex = 0;
		for (int i = 0; i < 4; i++) {
			curPlayerIndex = (4 + i - pStateFull.playerIndex) % 4;
			if (pStateFull.playerNames[curPlayerIndex] != null) {
				playerTextViews[i].setVisibility(View.VISIBLE);
				playerTextViews[i].setText(pStateFull.playerNames[curPlayerIndex]);
			} else {
				playerTextViews[i].setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * Set the selected card. This will highlight the selected
	 * card, and clear the highlight from any other cards.
	 *
	 * @param cardId - the currently selected card
	 */
	public void setSelected(int cardId, int suggestedId) {
		for (Card c : playerController.getPlayerState().cards) {
			if (c.getIdNum() == cardId && c.getIdNum() == suggestedId) {
				ImageView iv = (ImageView) findViewById(c.getIdNum());
				iv.setBackgroundColor(getResources().getColor(R.color.suggested_selected_card_color));
			} else if (c.getIdNum() == cardId) {
				ImageView iv = (ImageView) findViewById(c.getIdNum());
				iv.setBackgroundColor(getResources().getColor(R.color.gold));
			} else if(c.getIdNum() == suggestedId){
				ImageView iv = (ImageView) findViewById(c.getIdNum());
				iv.setBackgroundColor(getResources().getColor(R.color.suggested_card_color));
			} else {
				ImageView iv = (ImageView) findViewById(c.getIdNum());
				iv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			}
		}
	}

	/**
	 * Set the selected card. This will highlight the selected
	 * card, and clear the highlight from any other cards.
	 *
	 * @param cardId - the currently selected card
	 */
	public void setSuggested(int cardId) {
		for (Card c : playerController.getPlayerState().cards) {
			if (c.getIdNum() == cardId) {
				ImageView iv = (ImageView) findViewById(c.getIdNum());
				iv.setBackgroundColor(getResources().getColor(R.color.gold));
			}
		}
	}

	/**
	 * Set the card as greyed out, or not greyed out.
	 *
	 * @param cardImageViewId - This is the id of the image view of the card
	 * 							that is being greyed out or not greyed out
	 * @param isPlayable  - This is whether or not the card should be greyed
	 * 						out based on whether it is legal to play it
	 */
	public void setCardPlayable(int cardImageViewId, boolean isPlayable) {
		ImageView iv = (ImageView) findViewById(cardImageViewId);
		if (isPlayable) {
			iv.setColorFilter(Color.TRANSPARENT);
		} else {
			iv.setColorFilter(getResources().getColor(R.color.transparent_grey));
		}
	}

	/**
	 * This will remove all cards from the players hand and from the screen
	 * used for refreshing the player and syncing with game board
	 */
	// TODO why is this here? when do we use it?
	public void removeAllCards() {
		// Remove all cards from our hand
		playerController.getPlayerState().cards.clear();

		// Remove all layouts from our view
		playerHandLayout.removeAllViews();
	}

	/**
	 * Removes card from player's hand
	 *
	 * @param idNum ID number of the card to be removed
	 */
	public void removeFromHand(int idNum) {
		playerHandLayout.removeView(findViewById(idNum));

		List<Card> cards = playerController.getPlayerState().cards;

		// remove card from list
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).getIdNum() == idNum) {
				cards.remove(i);
				return;
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
		PlayerStateFull pStateFull = playerController.getPlayerState();

		// Place images for all player's cards
		for (int i = 0; i < Constants.DEFAULT_MAX_PLAYERS; i++) {

			playerLinearLayouts[i].removeAllViews();

			/*
			 * curPlayerIndex is the index of the player in pStateFull based on this players perspective
			 * i is the view that we are updating
			 */
			int curPlayerIndex = (i + 4 - pStateFull.playerIndex) % 4;

			if(i > 0){
				// Display for other players
				for (int j = 0; j < pStateFull.numCards[curPlayerIndex]; j++) {
					ImageView image = new ImageView(this);
					image.setId(CARD_BACK);
					image.setScaleType(ScaleType.FIT_CENTER);
					int resId = CARD_BACK;

					int cardsToDisplay = pStateFull.cards.size();
					if (cardsToDisplay > maxDisplayed[i]) {
						cardsToDisplay = maxDisplayed[i];
					}

					// Scale card
					Bitmap scaledCard = scaleCard(resId, (j < (cardsToDisplay - 1)) ? fourthCard : halfCard);
					image.setImageBitmap(scaledCard);

					// Check for max displayed
					if (j < maxDisplayed[i]) {
						playerLinearLayouts[i].addView(image, cardParams);
						playerRemainingCards[i].setVisibility(View.INVISIBLE);
					} else {
						// Display how many cards are remaining that aren't displayed
						playerRemainingCards[i].setText("+" + Math.abs(maxDisplayed[i] - pStateFull.cards.size()));
						playerRemainingCards[i].setVisibility(View.VISIBLE);
						break;
					}
				}
			} else {
				// Sort the cards
				Collections.sort(pStateFull.cards);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, cardHeight);
				int j = 0;
				// Display for this player
				for (Card c : pStateFull.cards) {
					ImageView image = new ImageView(this);
					image.setId(c.getIdNum());
					image.setImageBitmap(scaleCard(c.getResourceId()));
					c.getResourceId();

					// Set the onClick Listener for selecting this card
					image.setOnClickListener(playerController.getCardClickListener());

					// Add a 5px border around the image
					image.setPadding(5, 5, 5, 5);

					int cardsToDisplay = pStateFull.cards.size();
					if (cardsToDisplay > maxDisplayed[i]) {
						cardsToDisplay = maxDisplayed[i];
					}

					// Check for max displayed
					if (j < maxDisplayed[i]) {
						playerLinearLayouts[i].addView(image, params);
						playerRemainingCards[i].setVisibility(View.INVISIBLE);
					} else {
						// Display how many cards are remaining that aren't displayed
						playerRemainingCards[i].setText("+" + Math.abs(maxDisplayed[i] - pStateFull.cards.size()));
						playerRemainingCards[i].setVisibility(View.VISIBLE);
						break;
					}
					j++;
				}
			}
		}

		// Set all the cards in the center of the screen
		for (int j = 0; j < 4; j++) {
			int curPlayerIndex = (j + 4 - pStateFull.playerIndex) % 4;

			Card c = pStateFull.cardsPlayed[curPlayerIndex];
			if (!c.isNullCard()) {
				Bitmap scaledCard = scaleCard(c.getResourceId(), fullCard);

				centerCards[j].setImageBitmap(scaledCard);
				centerCards[j].setVisibility(View.VISIBLE);
			} else {
				centerCards[j].setVisibility(View.INVISIBLE);
			}
		}

		updateSuit(pStateFull.suitDisplay);

		// TODO Set up buttons enabled or not
	}

	/**
	 * Scale a card image with the given resource
	 * 
	 * @param resId the resource id of the card to scale
	 * @param cardPortion the amount of the card to show
	 * 
	 * @return a scaled card image
	 */
	private Bitmap scaleCard(int resId, int cardPortion) {
		Bitmap fullCard = BitmapFactory.decodeResource(getResources(), resId);
		float scaleFactor = (cardHeight + 0.0f) / fullCard.getHeight();
		Matrix tempMatrix = new Matrix();
		tempMatrix.setScale(scaleFactor, scaleFactor);

		// Draw fourth card
		if (cardPortion == fourthCard) {
			return Bitmap.createBitmap(fullCard, 0, 0,
					fullCard.getWidth() / 2, fullCard.getHeight() / 2, tempMatrix, true);
		} else if (cardPortion == halfCard) {
			return Bitmap.createBitmap(fullCard, 0, 0,
					fullCard.getWidth(), fullCard.getHeight() / 2, tempMatrix, true);
		} else if (cardPortion == halfCardVertCut) {
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
				playerTextViews[i].setTextColor(getResources().getColor(android.R.color.black));
			}
		}
	}

	/**
	 * Bold the specified player text
	 * @param playerNumber player whose name will be bolded
	 */
	public void boldPlayerText(int playerNumber){
		playerTextViews[playerNumber].setTypeface(null, Typeface.BOLD);
	}

	/**
	 * Sets all the players text to normal
	 */
	public void unboldAllPlayerText(){
		for (int i = 0; i < 4; i++) {
			playerTextViews[i].setTypeface(null, Typeface.NORMAL);
		}
	}
}
