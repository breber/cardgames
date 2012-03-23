package cs309.a1.player.activities;

import static cs309.a1.crazyeights.Constants.ID;
import static cs309.a1.crazyeights.Constants.SUIT;
import static cs309.a1.crazyeights.Constants.VALUE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cs309.a1.crazyeights.CrazyEightsCardTranslator;
import cs309.a1.player.R;
import cs309.a1.shared.Card;
import cs309.a1.shared.CardTranslator;
import cs309.a1.shared.Util;
import cs309.a1.shared.bluetooth.BluetoothClient;
import cs309.a1.shared.bluetooth.BluetoothConstants;

/**
 *
 */
public class ShowCardsActivity extends Activity{
	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = ShowCardsActivity.class.getName();

	/**
	 * The request code to keep track of the "Are you sure you want to quit" activity
	 */
	private static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());

	/**
	 * The request code to keep track of the "You have been disconnected" activity
	 */
	private static final int DISCONNECTED = Math.abs("DISCONNECTED".hashCode());

	private ArrayList<Card> cardHand;

	/**
	 * The BroadcastReceiver for handling messages from the Bluetooth connection
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothConstants.MESSAGE_RX_INTENT.equals(action)) {

				// TODO: we want to get this based on the current game, not
				// always the CrazyEights version
				CardTranslator ct = new CrazyEightsCardTranslator();

				String object = intent.getStringExtra(BluetoothConstants.KEY_MESSAGE_RX);

				if (Util.isDebugBuild()) {
					Log.d(TAG, object);
				}

				// Parse the Message if it was a card
				try {
					JSONArray arr = new JSONArray(object);

					for (int i = 0; i < arr.length(); i++) {
						JSONObject obj = arr.getJSONObject(i);
						int suit = obj.getInt(SUIT);
						int value = obj.getInt(VALUE);
						int id = obj.getInt(ID);
						addCard(new Card(suit, value, ct.getResourceForCardWithId(id), id));
					}
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
			} else if (BluetoothConstants.STATE_CHANGE_INTENT.equals(action)) {
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

		// Register the receiver for message/state change intents
		registerReceiver(receiver, new IntentFilter(BluetoothConstants.MESSAGE_RX_INTENT));
		registerReceiver(receiver, new IntentFilter(BluetoothConstants.STATE_CHANGE_INTENT));
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
			// TODO: handle the intent result from the disconnected activity
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 
	 * @param newCard
	 */
	private void addCard(Card newCard) {

		cardHand.add(newCard);

		Collections.sort(cardHand, new CompareIdNums());

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
			ll.addView(toAdd, lp);
		}
	}

	/**
	 * 
	 * @param idNum
	 */
	private void removeFromHand(int idNum) {

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

	//TODO: should this be moved to like the Util class, since it just compares generic cards?
	private class CompareIdNums implements Comparator<Card> {
		public int compare(Card card1, Card card2) {
			return card1.getIdNum() - card2.getIdNum();
		}
	}
}
