package cs309.a1.crazyeights;

import static cs309.a1.crazyeights.Constants.CARD_DRAWN;
import static cs309.a1.crazyeights.Constants.ID;
import static cs309.a1.crazyeights.Constants.IS_TURN;
import static cs309.a1.crazyeights.Constants.LOSER;
import static cs309.a1.crazyeights.Constants.REFRESH;
import static cs309.a1.crazyeights.Constants.SETUP;
import static cs309.a1.crazyeights.Constants.SUIT;
import static cs309.a1.crazyeights.Constants.VALUE;
import static cs309.a1.crazyeights.Constants.WINNER;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import cs309.a1.shared.SoundManager;
import cs309.a1.shared.Util;
import cs309.a1.shared.connection.ConnectionClient;
import cs309.a1.shared.connection.ConnectionConstants;

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
	 * The request code to keep track of the "Are you sure you want to quit"
	 * activity
	 */
	private static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());

	/**
	 * The cards of this player
	 */
	private ArrayList<Card> cardHand;

	/**
	 * An instance of the ShowCardsActivity that can be used to display cards
	 * and do other things as if this class was the ShowCardsActivity.
	 */
	private ShowCardsActivity playerContext;

	/**
	 * The current selected Card
	 */
	private Card cardSelected;

	/**
	 * The card that is on the discard pile
	 */
	private Card cardOnDiscard;

	/**
	 * An instance of the GameRules that is used to check if a card can be
	 * played
	 */
	private Rules gameRules;

	/**
	 * This is true if it is the players turn
	 */
	private boolean isTurn = false;

	/**
	 * The play button on the layout
	 */
	private Button play;

	/**
	 * The draw button on the layout
	 */
	private Button draw;

	/**
	 * The bluetooth client that is used to send messages to the GameBoard
	 */
	private ConnectionClient connection;

	/**
	 * This is how we can make sure that the card resource IDs are correct
	 */
	private CardTranslator ct;

	/**
	 * This is a soundmanager instance that can do text to speech and other
	 * sounds.
	 */
	private SoundManager mySM;

	/**
	 * The player's name
	 */
	private String playerName;

	/**
	 * This will initialize an instance of a CrazyEightsPlayerController
	 * 
	 * @param context This is an instance of the ShowCardsActivity
	 * @param playGiven The Play button
	 * @param drawGiven The Draw button
	 * @param btcGiven The bluetooth client
	 * @param cardHandGiven The list of cards that this player has
	 */
	public CrazyEightsPlayerController(Context context, Button playGiven,
			Button drawGiven, ConnectionClient connectionGiven,	ArrayList<Card> cardHandGiven) {
		playerContext = (ShowCardsActivity) context;
		play = playGiven;
		draw = drawGiven;
		play.setOnClickListener(getPlayOnClickListener());
		draw.setOnClickListener(getDrawOnClickListener());
		setButtonsEnabled(false);
		mySM = new SoundManager(context);
		cardHand = cardHandGiven;
		playerName = "";

		gameRules = new CrazyEightGameRules();
		ct = new CrazyEightsCardTranslator();
		connection = connectionGiven;
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.PlayerController#handleBroadcastReceive(android.content.Context, android.content.Intent)
	 */
	public void handleBroadcastReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (ConnectionConstants.MESSAGE_RX_INTENT.equals(action)) {
			String object = intent.getStringExtra(ConnectionConstants.KEY_MESSAGE_RX);
			int messageType = intent.getIntExtra(ConnectionConstants.KEY_MESSAGE_TYPE, -1);

			if (Util.isDebugBuild()) {
				Log.d(TAG, "message: " + object);
			}

			switch (messageType) {
			case SETUP:
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
				isTurn = false;
				break;
			case IS_TURN:
				mySM.sayTurn(playerName);
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
			case CARD_DRAWN:
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
			case REFRESH:
				// Parse the refresh Message
				try {
					JSONArray arr = new JSONArray(object);
					JSONObject refreshInfo = arr.getJSONObject(0);
					isTurn = refreshInfo.getBoolean(Constants.TURN);
					// if add more refresh info add more here

					playerContext.removeAllCards();

					for (int i = 1; i < arr.length(); i++) {
						JSONObject obj = arr.getJSONObject(i);
						int suit = obj.getInt(SUIT);
						int value = obj.getInt(VALUE);
						int id = obj.getInt(ID);
						playerContext.addCard(new Card(suit, value, ct.getResourceForCardWithId(id), id));
					}
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				setButtonsEnabled(isTurn);
				cardSelected = null;
				break;
			case WINNER:
				Intent Winner = new Intent(playerContext, GameResultsActivity.class);
				Winner.putExtra(GameResultsActivity.IS_WINNER, true);
				playerContext.startActivityForResult(Winner, QUIT_GAME);
				break;
			case LOSER:
				Intent Loser = new Intent(playerContext, GameResultsActivity.class);
				Loser.putExtra(GameResultsActivity.IS_WINNER, false);
				playerContext.startActivityForResult(Loser, QUIT_GAME);
				break;
			}
		}

	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.PlayerController#getPlayOnClickListener()
	 */
	public View.OnClickListener getPlayOnClickListener() {
		return new OnClickListener() {
			public void onClick(View v) {
				if (isTurn && gameRules.checkCard(cardSelected, cardOnDiscard) && cardHand.size() != 0) {
					// play card
					if (cardSelected.getValue() == 7) {
						Intent selectSuit = new Intent(playerContext, SelectSuitActivity.class);
						playerContext.startActivityForResult(selectSuit, CHOOSE_SUIT);
						// go to the onactivityresult to finish this turn
					} else {
						connection.write(Constants.PLAY_CARD, cardSelected);
						Toast.makeText(playerContext.getApplicationContext(),
								"playing : " + cardSelected.getValue(),
								Toast.LENGTH_SHORT).show();
						playerContext.removeFromHand(cardSelected.getIdNum());

						if (Util.isDebugBuild()) {
							Toast.makeText(playerContext.getApplicationContext(),
									"Played: " + cardSelected.getSuit() + " " + cardSelected.getValue(),
									Toast.LENGTH_SHORT);
						}

						cardSelected = null;
						setButtonsEnabled(false);
						isTurn = false;
					}
				}
			}
		};
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.PlayerController#getDrawOnClickListener()
	 */
	public View.OnClickListener getDrawOnClickListener() {
		return new View.OnClickListener() {
			public void onClick(View v) {
				if (isTurn) {
					connection.write(Constants.DRAW_CARD, null);
					setButtonsEnabled(false);
					isTurn = false;
				}
			}
		};
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.PlayerController#getCardLongClickListener()
	 */
	public OnLongClickListener getCardLongClickListener() {
		return new CardSelectionClickListener();
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.PlayerController#handleActivityResult(int, int, android.content.Intent)
	 */
	public void handleActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CHOOSE_SUIT) {
			boolean isSuitChosen = true;
			switch (resultCode) {
			case Constants.SUIT_CLUBS:
				connection.write(Constants.PLAY_EIGHT_C, cardSelected);
				break;
			case Constants.SUIT_DIAMONDS:
				connection.write(Constants.PLAY_EIGHT_D, cardSelected);
				break;
			case Constants.SUIT_HEARTS:
				connection.write(Constants.PLAY_EIGHT_H, cardSelected);
				break;
			case Constants.SUIT_SPADES:
				connection.write(Constants.PLAY_EIGHT_S, cardSelected);
				break;
			case Activity.RESULT_OK:
				isSuitChosen = false;
				break;
			}
			
			if(isSuitChosen){
				playerContext.removeFromHand(cardSelected.getIdNum());
				if (Util.isDebugBuild()) {
					Toast.makeText(playerContext.getApplicationContext(),
							"Played: " + cardSelected.getSuit() + " "
									+ cardSelected.getValue(), 100);
				}
	
				cardSelected = null;
				setButtonsEnabled(false);
				isTurn = false;
			}
		} 
	}

	/**
	 * Used to set the play and draw buttons to enable or disabled
	 * 
	 * @param isEnabled
	 */
	private void setButtonsEnabled(boolean isEnabled) {
		play.setEnabled(isEnabled);
		draw.setEnabled(isEnabled);
	}

	/**
	 * Sets the player's name
	 * 
	 * @param name - the player's name
	 */
	public void setPlayerName(String name) {
		playerName = name;
	}

	/**
	 * This will be used for each card imageview and will allow the card to be
	 * selected when it is LongClicked
	 */
	private class CardSelectionClickListener implements View.OnLongClickListener {
		public boolean onLongClick(View v) {
			// so this means they just selected the card.
			// we could remove it from the hand below and place it so

			// Show an animation indicating the card was selected
			ScaleAnimation scale = new ScaleAnimation((float) 1.2, (float) 1.2,	(float) 1.2, (float) 1.2);
			scale.scaleCurrentDuration(5);
			v.startAnimation(scale);

			// Let the UI know which card was selected
			playerContext.setSelected(v.getId());

			for (int i = 0; i < cardHand.size(); i++) {
				if (cardHand.get(i).getIdNum() == v.getId()) {
					cardSelected = cardHand.get(i);
				}
			}

			return true;
		}
	}
}
