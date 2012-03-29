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
import cs309.a1.crazyeights.CrazyEightsPlayerController;
import cs309.a1.player.R;
import cs309.a1.shared.Card;
import cs309.a1.shared.PlayerController;
import cs309.a1.shared.bluetooth.BluetoothClient;
import cs309.a1.shared.bluetooth.BluetoothConstants;
import cs309.a1.shared.Util;

/**
 *
 */
public class ShowCardsActivity extends Activity{
	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = ShowCardsActivity.class.getName();

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

	BluetoothClient btc;
	
	private PlayerController playerController;

	/**
	 * The BroadcastReceiver for handling messages from the Bluetooth connection
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			//give it up to the player controller to deal with
			playerController.handleBroadcastReceive(context, intent);
			
			if (BluetoothConstants.STATE_CHANGE_INTENT.equals(action)) {
				// Handle a state change
				int newState = intent.getIntExtra(BluetoothConstants.KEY_STATE_MESSAGE, BluetoothConstants.STATE_NONE);

				// If the new state is anything but connected, display the "You have been disconnected" screen
				if (newState != BluetoothConstants.STATE_CONNECTED) {
					Intent i = new Intent(ShowCardsActivity.this, ConnectionFailActivity.class);
					startActivityForResult(i, DISCONNECTED);
				}
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
		
		cardHand = new ArrayList<Card>();

		//check which game we are playing then set rules
		
		// Register the receiver for message/state change intents
		registerReceiver(receiver, new IntentFilter(BluetoothConstants.MESSAGE_RX_INTENT));

		btc = BluetoothClient.getInstance(this);
		Button play = (Button) findViewById(R.id.btPlayCard);
		Button draw = (Button) findViewById(R.id.btDrawCard);
		
		//TODO if crazyeights
		playerController = (PlayerController) new CrazyEightsPlayerController(this,play,draw,btc, cardHand);
		

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

		// Unregister the receiver
		unregisterReceiver(receiver);
		//unregisterReceiver(playerController.getBroadcastReceiver());

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
			// TODO: does anything need to be done when coming back from the device connection activity?

			// If the user cancelled the device list, then bring them back to the main menu
			if (resultCode == RESULT_CANCELED) {
				setResult(RESULT_CANCELED);
				finish();
			} else {
				// Register the state change receiver
				registerReceiver(receiver, new IntentFilter(BluetoothConstants.STATE_CHANGE_INTENT));
			}
		} 
		
		playerController.handleActivityResult(requestCode, resultCode, data);
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

		LinearLayout ll = (LinearLayout) findViewById(R.id.playerCardContainer);
		ll.removeAllViews();
		// convert dip to pixels
		final float dpsToPixScale = getApplicationContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (125 * dpsToPixScale + 0.5f);

		// edit layout attributes
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pixels, LinearLayout.LayoutParams.WRAP_CONTENT);

		for(int i = 0; i < cardHand.size(); i++) {

			// create ImageView to hold Card
			ImageView toAdd = new ImageView(this);
			toAdd.setImageResource(cardHand.get(i).getResourceId());
			toAdd.setId(cardHand.get(i).getIdNum());
			toAdd.setAdjustViewBounds(true);
			toAdd.setOnLongClickListener(playerController.getCardLongClickListener());
			ll.addView(toAdd, lp);
		}
	}

	/**
	 * Removes card from player's hand
	 * 
	 * @param idNum ID number of the card to be removed
	 */
	public void removeFromHand(int idNum) {

		LinearLayout ll = (LinearLayout) findViewById(R.id.playerCardContainer);
		ll.removeView(findViewById(idNum));

		// remove card from list
		Card current = cardHand.get(0);
		int i = 0;

		while(current.getIdNum() != idNum) {
			i++;
			current = cardHand.get(i);
		}

		cardHand.remove(current);
	}

}
