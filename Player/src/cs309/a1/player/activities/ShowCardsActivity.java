package cs309.a1.player.activities;

import static cs309.a1.crazyeights.Constants.ID;
import static cs309.a1.crazyeights.Constants.RESOURCE_ID;
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
import cs309.a1.player.R;
import cs309.a1.shared.Card;
import cs309.a1.shared.Util;
import cs309.a1.shared.bluetooth.BluetoothConstants;

public class ShowCardsActivity extends Activity{

	private static final int QUIT_GAME = "QUIT_GAME".hashCode();

	private ArrayList<Card> cardHand;

	/**
	 * 
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String sender = intent.getStringExtra(BluetoothConstants.KEY_DEVICE_ID);
			String object = intent.getStringExtra(BluetoothConstants.KEY_MESSAGE_RX);

			if (Util.isDebugBuild()) {
				Log.d("cs309", object);
			}

			// Another possible implementation without having to come up
			// with our own parsing code...see Player.java for the encoding part...
			try {
				JSONArray arr = new JSONArray(object);

				for (int i = 0; i < arr.length(); i++) {
					JSONObject obj = arr.getJSONObject(i);
					int suit = obj.getInt(SUIT);
					int value = obj.getInt(VALUE);
					int resourceId = obj.getInt(RESOURCE_ID);
					int id = obj.getInt(ID);
					addCard(new Card(suit, value, resourceId, id));
				}
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
	};

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_hand);
		cardHand = new ArrayList<Card>();
		registerReceiver(receiver, new IntentFilter(BluetoothConstants.MESSAGE_RX_INTENT));
	}

	/**
	 * 
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, QuitGameActivity.class);
		startActivityForResult(intent, QUIT_GAME);
	}

	/**
	 * 
	 */
	@Override
	protected void onDestroy() {
		// TODO: disconnect from bluetooth...
		super.onDestroy();
	}

	/**
	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == QUIT_GAME) {
			if (resultCode == RESULT_OK) {
				// Finish this activity - if everything goes right, we
				// should be back at the main menu
				setResult(RESULT_OK);
				finish();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 
	 * @param newCard
	 */
	public void addCard(Card newCard) {

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

	private class CompareIdNums implements Comparator<Card> {

		public int compare(Card card1, Card card2) {
			return card1.getIdNum() - card2.getIdNum();
		}
	}
}
