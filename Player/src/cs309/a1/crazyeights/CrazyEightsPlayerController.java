package cs309.a1.crazyeights;

import static cs309.a1.crazyeights.Constants.ID;
import static cs309.a1.crazyeights.Constants.SUIT;
import static cs309.a1.crazyeights.Constants.VALUE;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.Toast;
import cs309.a1.player.activities.GameResultsActivity;
import cs309.a1.player.activities.SelectSuitActivity;
import cs309.a1.player.activities.ShowCardsActivity;
import cs309.a1.shared.Card;
import cs309.a1.shared.CardTranslator;
import cs309.a1.shared.PlayerController;
import cs309.a1.shared.Rules;
import cs309.a1.shared.Util;
import cs309.a1.shared.bluetooth.BluetoothClient;
import cs309.a1.shared.bluetooth.BluetoothConstants;

public class CrazyEightsPlayerController implements PlayerController {

	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = CrazyEightsPlayerController.class.getName();

	/**
	 * intent code for choosing suit
	 */
	private static final int CHOOSE_SUIT = Math.abs("CHOOSE_SUIT".hashCode());

	/**
	 * The request code to keep track of the "Are you sure you want to quit" activity
	 */
	private static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());

	private ArrayList<Card> cardHand;
	
	private ShowCardsActivity playerContext;

	private Card cardSelected;

	private Card cardOnDiscard;

	private Rules gameRules;

	private boolean isTurn = false;
	
	private Button play;
	
	private Button draw;
	
	BluetoothClient btc;
	
	CardTranslator ct;
	
	public CrazyEightsPlayerController(Context context, Button playGiven, Button drawGiven, BluetoothClient btcGiven, ArrayList<Card> cardHandGiven){
		playerContext = (ShowCardsActivity) context;
		play=playGiven;
		draw=drawGiven;
		play.setOnClickListener(this.getPlayOnClickListener());
		draw.setOnClickListener(this.getDrawOnClickListener());
		setButtonsEnabled(false);
		cardHand = cardHandGiven;
		
		gameRules = new CrazyEightGameRules();
		ct = new CrazyEightsCardTranslator();
		btc = btcGiven;
	}

	public void handleBroadcastReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (BluetoothConstants.MESSAGE_RX_INTENT.equals(action)) {
	
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
						playerContext.addCard(new Card(suit, value, ct.getResourceForCardWithId(id), id));
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
					playerContext.addCard(new Card(suit, value, ct.getResourceForCardWithId(id), id));
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				break;
				
				//these need to be in the activity
			case Constants.WINNER:
				Intent Winner = new Intent(playerContext, GameResultsActivity.class);
				Winner.putExtra(GameResultsActivity.IS_WINNER, true);
				playerContext.startActivityForResult(Winner, QUIT_GAME);
				break;
			case Constants.LOSER:
				Intent Loser = new Intent(playerContext, GameResultsActivity.class);
				Loser.putExtra(GameResultsActivity.IS_WINNER, false);
				playerContext.startActivityForResult(Loser, QUIT_GAME);
				break;
			}
		}

	}

	public View.OnClickListener getPlayOnClickListener() {
		return new OnClickListener() {
			public void onClick(View v) {
				if(isTurn && gameRules.checkCard(cardSelected, cardOnDiscard) && cardHand.size()!=0){
					//play card
					if(cardSelected.getValue() == 7){
						Intent selectSuit = new Intent(playerContext, SelectSuitActivity.class);
						playerContext.startActivityForResult(selectSuit, CHOOSE_SUIT);
						//go to the onactivityresult to finish this turn
					}else{
						btc.write(Constants.PLAY_CARD, cardSelected);
						Toast.makeText(playerContext.getApplicationContext(), "playing : " + cardSelected.getValue(), Toast.LENGTH_SHORT).show();
						playerContext.removeFromHand(cardSelected.getIdNum());

						if (Util.isDebugBuild()) {
							Toast.makeText(playerContext.getBaseContext(), "Played: " + cardSelected.getSuit() + " " + cardSelected.getValue(), 100);
						}

						setButtonsEnabled(false);
						isTurn=false;
					}
				}
			}
		};
	}

	public View.OnClickListener getDrawOnClickListener() {
		return new View.OnClickListener(){
			public void onClick(View v) {
				if(isTurn){
					btc.write(Constants.DRAW_CARD, null);
					setButtonsEnabled(false);
					isTurn=false;
				}
			}
		};
	}

	public OnLongClickListener getCardLongClickListener() {
		return new MyLongClickListener();
	}

	public void handleActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == CHOOSE_SUIT) {
			
			//handleActivityResult(requestCode, resultCode, data);
			//TODO add to player controller
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
			playerContext.removeFromHand(cardSelected.getIdNum());
			if (Util.isDebugBuild()) {
				Toast.makeText(playerContext.getBaseContext(), "Played: " + cardSelected.getSuit() + " " + cardSelected.getValue(), 100);
			}
			setButtonsEnabled(false);
			isTurn=false;
		}
	}
	
	
	private void setButtonsEnabled(boolean isEnabled){
		play.setEnabled(isEnabled);
		draw.setEnabled(isEnabled);
	}

	


	private class MyLongClickListener implements View.OnLongClickListener {
		public boolean onLongClick(View v) {
			//so this means they just selected the card.
			//we could remove it from the hand below and place it so
			
			//TODO ASHLEY if you are here and looking to add a boarder or something to a card
			//     just make a method in the ShowCardsActivity and then call it here using
			//     playerContext.methodName(...);
			ScaleAnimation scale = new ScaleAnimation((float)1.2, (float)1.2, (float)1.2, (float)1.2);
			scale.scaleCurrentDuration(5);
			v.startAnimation(scale);
			
			int i;
			for(i=0; i < cardHand.size(); i++){
				if(cardHand.get(i).getIdNum() == v.getId()){
					cardSelected= cardHand.get(i);
				}
			}

			return true;
		}

	}
}

	
