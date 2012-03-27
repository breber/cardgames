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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cs309.a1.crazyeights.Constants;
import cs309.a1.crazyeights.CrazyEightGameRules;
import cs309.a1.crazyeights.CrazyEightsCardTranslator;
import cs309.a1.player.R;
import cs309.a1.shared.Card;
import cs309.a1.shared.CardTranslator;
import cs309.a1.shared.Rules;
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
	 * intent code for choosing suit
	 */
	private static final int CHOOSE_SUIT = Math.abs("CHOOSE_SUIT".hashCode());

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

	private ArrayList<Card> cardHand;

	private Card cardSelected;

	private Card cardOnDiscard;

	private Rules gameRules;

	private boolean isTurn = false;

	BluetoothClient btc;

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
				int messageType = intent.getIntExtra(BluetoothConstants.KEY_MESSAGE_TYPE, -1);

				if (Util.isDebugBuild()) {
					Log.d(TAG, "message: " + object);
				}

				switch(messageType){
				case Constants.SETUP:
					// Parse the Message if it was the original setup
					try {
						JSONArray arr = new JSONArray(object);
						arr.getJSONObject(0);
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
					setButtonsEnabled(false);
					isTurn=false;
					break;
				case Constants.IS_TURN:

					try {
						JSONObject obj = new JSONObject(object);
						int suit = obj.getInt(SUIT);
						int value = obj.getInt(VALUE);
						int id = obj.getInt(ID);
						cardOnDiscard = new Card(suit, value, ct.getResourceForCardWithId(id), id);
					} catch (JSONException ex) {
						ex.printStackTrace();
					}
					setButtonsEnabled(true);
					isTurn = true;
					break;
				case Constants.CARD_DRAWN:
					try {
						JSONObject obj = new JSONObject(object);
						int suit = obj.getInt(SUIT);
						int value = obj.getInt(VALUE);
						int id = obj.getInt(ID);
						addCard(new Card(suit, value, ct.getResourceForCardWithId(id), id));
					} catch (JSONException ex) {
						ex.printStackTrace();
					}
					break;
				case Constants.WINNER:
					Intent Winner = new Intent(ShowCardsActivity.this, GameResultsActivity.class);
					Winner.putExtra(GameResultsActivity.IS_WINNER, true);
					startActivityForResult(Winner, QUIT_GAME);
					break;
				case Constants.LOSER:
					Intent Loser = new Intent(ShowCardsActivity.this, GameResultsActivity.class);
					Loser.putExtra(GameResultsActivity.IS_WINNER, false);
					startActivityForResult(Loser, QUIT_GAME);
					break;
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

		//check which game we are playing then set rules
		gameRules = new CrazyEightGameRules();

		// Register the receiver for message/state change intents
		registerReceiver(receiver, new IntentFilter(BluetoothConstants.MESSAGE_RX_INTENT));

		btc = BluetoothClient.getInstance(this);

		Button play = (Button) findViewById(R.id.btPlayCard);
		Button draw = (Button) findViewById(R.id.btDrawCard);

		setButtonsEnabled(false);

		draw.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(isTurn){
					btc.write(Constants.DRAW_CARD, null);
					setButtonsEnabled(false);
					isTurn=false;
				}
			}
		});

		play.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(isTurn && gameRules.checkCard(cardSelected, cardOnDiscard)){
					//play card
					if(cardSelected.getValue() == 7){
						Intent selectSuit = new Intent(ShowCardsActivity.this, SelectSuitActivity.class);
						startActivityForResult(selectSuit, CHOOSE_SUIT);
						//go to the onactivityresult to finish this turn
					}else{
						btc.write(Constants.PLAY_CARD, cardSelected);
						Toast.makeText(getApplicationContext(), "playing : " + cardSelected.getValue(), Toast.LENGTH_SHORT).show();
						removeFromHand(cardSelected.getIdNum());

						if (Util.isDebugBuild()) {
							Toast.makeText(getBaseContext(), "Played: " + cardSelected.getSuit() + " " + cardSelected.getValue(), 100);
						}

						setButtonsEnabled(false);
						isTurn=false;
					}
				}
			}
		});

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
		} else if (requestCode == CHOOSE_SUIT) {
			switch(resultCode){
			case Constants.SUIT_CLUBS:
				btc.write(Constants.PLAY_EIGHT_C, cardSelected);
				break;
			case Constants.SUIT_DIAMONDS:
				btc.write(Constants.PLAY_EIGHT_D, cardSelected);
				break;
			case Constants.SUIT_HEARTS:
				btc.write(Constants.PLAY_EIGHT_H, cardSelected);
				break;
			case Constants.SUIT_SPADES:
				btc.write(Constants.PLAY_EIGHT_S, cardSelected);
				break;
			}
			removeFromHand(cardSelected.getIdNum());
			if (Util.isDebugBuild()) {
				Toast.makeText(getBaseContext(), "Played: " + cardSelected.getSuit() + " " + cardSelected.getValue(), 100);
			}
			setButtonsEnabled(false);
			isTurn=false;
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
			toAdd.setOnLongClickListener(new MyLongClickListener()); //this is a private class below
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

	private void setButtonsEnabled(boolean isEnabled){
		Button play = (Button) findViewById(R.id.btPlayCard);
		Button draw = (Button) findViewById(R.id.btDrawCard);

		play.setEnabled(isEnabled);
		draw.setEnabled(isEnabled);
	}

	//TODO: should this be moved to like the Util class, since it just compares generic cards?
	private class CompareIdNums implements Comparator<Card> {
		public int compare(Card card1, Card card2) {
			return card1.getIdNum() - card2.getIdNum();
		}
	}


	private class MyLongClickListener implements View.OnLongClickListener {

		public boolean onLongClick(View v) {
			//so this means they just selected the card.
			//we could remove it from the hand below and place it so
			ScaleAnimation scale = new ScaleAnimation((float)1.2, (float)1.2, (float)1.2, (float)1.2);
			scale.scaleCurrentDuration(5);

			v.startAnimation(scale);
			int i;
			for(i=0; i < cardHand.size(); i++){
				if(cardHand.get(i).getIdNum() == v.getId()){
					cardSelected= cardHand.get(i);
				}
			}

			//please take out once bluetooth is working with receiving card on discard
			//TODO
			if (Util.isDebugBuild()) {
				//cardOnDiscard = cardSelected;
			}

			return true;
		}

	}
}
