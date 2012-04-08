package cs309.a1.player.activities;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cs309.a1.crazyeights.Constants;
import cs309.a1.crazyeights.CrazyEightsPlayerController;
import cs309.a1.player.R;
import cs309.a1.shared.Card;
import cs309.a1.shared.PlayerController;
import cs309.a1.shared.Util;
import cs309.a1.shared.bluetooth.BluetoothClient;
import cs309.a1.shared.bluetooth.BluetoothConstants;
import cs309.a1.shared.connection.ConnectionClient;
import cs309.a1.shared.connection.ConnectionConstants;

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
	 * List of cards in player's hand
	 */
	private ArrayList<Card> cardHand;

	/**
	 * The ConnectionClient used to send messages to the server
	 */
	private ConnectionClient connection;

	/**
	 * The PlayerController for handling a lot of the Game-Specific logic
	 */
	private PlayerController playerController;

	/**
	 * The LinearLayout holding all card images
	 */
	private LinearLayout playerHandLayout;

	/**
	 * The BroadcastReceiver for handling messages from the Bluetooth connection
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (ConnectionConstants.STATE_CHANGE_INTENT.equals(action)) {
				// Handle a state change
				int newState = intent.getIntExtra(ConnectionConstants.KEY_STATE_MESSAGE, BluetoothConstants.STATE_NONE);

				// If the new state is anything but connected, display the "You have been disconnected" screen
				if (newState != BluetoothConstants.STATE_CONNECTED) {
					Intent i = new Intent(ShowCardsActivity.this, ConnectionFailActivity.class);
					startActivityForResult(i, DISCONNECTED);
				}
			} else {
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
		setContentView(R.layout.player_hand);

		// Create a new, empty hand
		cardHand = new ArrayList<Card>();

		// Register the receiver for message/state change intents
		registerReceiver(receiver, new IntentFilter(ConnectionConstants.MESSAGE_RX_INTENT));

		// Get an instance of the BluetoothClient so that we can
		// send messages back to the tablet
		connection = BluetoothClient.getInstance(this);

		// Set up the Layout for the cards
		playerHandLayout = (LinearLayout) findViewById(R.id.playerCardContainer);

		// Get the play and draw buttons so that the playerController can
		// do stuff with them
		Button play = (Button) findViewById(R.id.btPlayCard);
		Button draw = (Button) findViewById(R.id.btDrawCard);

		// TODO: if crazyeights
		playerController = new CrazyEightsPlayerController(this, play, draw, connection, cardHand);

		// Start the connection screen from here so that we can register the message receive
		// broadcast receiver so that we don't miss any messages
		Intent i = new Intent(this, ConnectActivity.class);
		startActivityForResult(i, CONNECT_DEVICE);
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
		// Disconnect Bluetooth connection
		BluetoothClient.getInstance(this).disconnect();

		// Unregister all the receivers we may have registered
		try {
			unregisterReceiver(receiver);
		} catch (IllegalArgumentException e) {
			// We didn't get far enough to register the receiver
		}

		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == QUIT_GAME && resultCode == RESULT_OK) {
			// Finish this activity - if everything goes right, we
			// should be back at the main menu
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
				String playerName = data.getStringExtra(Constants.PLAYER_NAME);
				playerController.setPlayerName(playerName);
				// Register the state change receiver
				registerReceiver(receiver, new IntentFilter(ConnectionConstants.STATE_CHANGE_INTENT));
			}
		} else {
			// If it isn't anything we know how to handle, pass it on to the
			// playerController to try and handle it
			playerController.handleActivityResult(requestCode, resultCode, data);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Adds and displays a card in the player's hand
	 * 
	 * @param newCard Card to be added to the hand
	 */
	public void addCard(Card newCard) {

		cardHand.add(newCard);

		Collections.sort(cardHand, new Util.CompareIdNums());

		playerHandLayout.removeAllViews();

		// convert dip to pixels
		final float dpsToPixScale = getApplicationContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (125 * dpsToPixScale + 0.5f);

		// edit layout attributes
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pixels, LinearLayout.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < cardHand.size(); i++) {
			// create ImageView to hold Card
			ImageView toAdd = new ImageView(this);
			toAdd.setImageResource(cardHand.get(i).getResourceId());
			toAdd.setId(cardHand.get(i).getIdNum());
			toAdd.setAdjustViewBounds(true);
			toAdd.setOnLongClickListener(playerController.getCardLongClickListener());

			// Add a 5 px border around the image
			toAdd.setPadding(5, 5, 5, 5);

			playerHandLayout.addView(toAdd, lp);
		}
	}

	/**
	 * Set the selected card. This will highlight the selected
	 * card, and clear the highlight from any other cards.
	 * 
	 * @param cardId - the currently selected card
	 */
	public void setSelected(int cardId) {
		for (Card c : cardHand) {
			if (c.getIdNum() == cardId) {
				ImageView iv = (ImageView) findViewById(c.getIdNum());
				iv.setBackgroundColor(getResources().getColor(R.color.gold));
			} else {
				ImageView iv = (ImageView) findViewById(c.getIdNum());
				iv.setBackgroundColor(getResources().getColor(R.color.transparent));
			}
		}
	}

	/**
	 * This will remove all cards from cardHand and from the screen
	 * used for refreshing the player and syncing with game board
	 */
	public void removeAllCards() {
		// this removes all cards from card
		while (cardHand.size() > 0) {
			cardHand.remove(cardHand.get(0));
		}

		playerHandLayout.removeAllViews();
	}

	/**
	 * Removes card from player's hand
	 * 
	 * @param idNum ID number of the card to be removed
	 */
	public void removeFromHand(int idNum) {
		playerHandLayout.removeView(findViewById(idNum));

		// remove card from list
		Card current = cardHand.get(0);
		int i = 0;

		while (current.getIdNum() != idNum) {
			i++;
			current = cardHand.get(i);
		}

		cardHand.remove(current);
	}

}
