package com.worthwhilegames.cardgames.crazyeights;

import static com.worthwhilegames.cardgames.shared.Constants.PREFERENCES;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.player.activities.GameResultsActivity;
import com.worthwhilegames.cardgames.player.activities.SelectSuitActivity;
import com.worthwhilegames.cardgames.player.activities.ShowCardsActivity;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.PlayerController;
import com.worthwhilegames.cardgames.shared.PlayerStateFull;
import com.worthwhilegames.cardgames.shared.Rules;
import com.worthwhilegames.cardgames.shared.SoundManager;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionClient;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;

/**
 * The PlayerController implementation for Crazy Eights
 */
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
	private List<Card> cardHand;

	private PlayerStateFull playerState;

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
	 * The id of the suggested Card
	 */
	private int cardSuggestedId = -1;

	/**
	 * This is the setting if the player would like to see card suggestions
	 */
	boolean isPlayAssistMode = false;

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
	 * The client that is used to send messages to the GameBoard
	 */
	private ConnectionClient connection;

	/**
	 * This is a SoundManager instance that can do text to speech and other
	 * sounds.
	 */
	private SoundManager mySM;

	/**
	 * The player's name
	 */
	private String playerName;

	/**
	 * The LinearLayout holding all card images
	 */
	private LinearLayout playerHandLayout;

	/**
	 * This will initialize an instance of a CrazyEightsPlayerController
	 * 
	 * @param context This is an instance of the ShowCardsActivity
	 * @param cardHandGiven The list of cards that this player has
	 */
	public CrazyEightsPlayerController(Activity context, List<Card> cardHandGiven) {
		playerContext = (ShowCardsActivity) context;
		// Get the play and draw buttons so that the playerController can
		// do stuff with them
		play = (Button) context.findViewById(R.id.btPlayCard);
		draw = (Button) context.findViewById(R.id.btDrawCard);
		play.setOnClickListener(playClickListener);
		draw.setOnClickListener(drawClickListener);
		setButtonsEnabled(false);
		mySM = SoundManager.getInstance(context);
		cardHand = cardHandGiven;
		playerName = "";
		playerHandLayout = (LinearLayout) playerContext.findViewById(R.id.playerCardContainer);

		// set up play assist mode
		SharedPreferences sharedPreferences = playerContext.getSharedPreferences(PREFERENCES, 0);
		isPlayAssistMode = sharedPreferences.getBoolean(Constants.PREF_PLAY_ASSIST_MODE, false);

		gameRules = new CrazyEightGameRules();
		connection = ConnectionClient.getInstance(context);
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.PlayerController#handleBroadcastReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void handleBroadcastReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (ConnectionConstants.MESSAGE_RX_INTENT.equals(action)) {
			String object = intent.getStringExtra(ConnectionConstants.KEY_MESSAGE_RX);
			int messageType = intent.getIntExtra(ConnectionConstants.KEY_MESSAGE_TYPE, -1);

			if (Util.isDebugBuild()) {
				Log.d(TAG, "message: " + object);
			}

			switch (messageType) {
			case Constants.MSG_SETUP:
				// Parse the Message if it was the original setup
				try {
					JSONArray arr = new JSONArray(object);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject obj = arr.getJSONObject(i);
						playerContext.addCard(Card.createCardFromJSON(obj));
					}
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				setButtonsEnabled(false);
				isTurn = false;
				cardSuggestedId = -1;
				break;
			case Constants.MSG_IS_TURN:
				mySM.sayTurn(playerName);
				try {
					JSONObject obj = new JSONObject(object);
					cardOnDiscard = Card.createCardFromJSON(obj);
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				setButtonsEnabled(true);
				isTurn = true;
				break;
			case Constants.MSG_SUGGESTED_CARD:
				if(isTurn && object != null && isPlayAssistMode){
					try {
						JSONObject obj = new JSONObject(object);
						int id = obj.getInt(Constants.KEY_CARD_ID);
						cardSuggestedId = id;

						// Let the UI know which card was suggested
						int selectedId = -1;
						if(cardSelected != null){
							selectedId = cardSelected.getIdNum();
						}
						playerContext.setSelected(selectedId, cardSuggestedId);
					} catch (JSONException ex) {
						ex.printStackTrace();
					}
				}
				break;
			case Constants.MSG_CARD_DRAWN:
				try {
					JSONObject obj = new JSONObject(object);
					playerContext.addCard(Card.createCardFromJSON(obj));
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				break;
			case Constants.MSG_REFRESH:
				// Parse the refresh Message
				try {
					JSONObject refreshInfo = new JSONObject(object);
					isTurn = refreshInfo.getBoolean(Constants.KEY_TURN);
					playerName = refreshInfo.getString(Constants.KEY_PLAYER_NAME);

					playerContext.removeAllCards();

					JSONObject discardObj = refreshInfo.getJSONObject(Constants.KEY_DISCARD_CARD);

					cardOnDiscard = Card.createCardFromJSON(discardObj);

					// Get the player's hand
					JSONArray arr = refreshInfo.getJSONArray(Constants.KEY_CURRENT_HAND);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject card = arr.getJSONObject(i);
						playerContext.addCard(Card.createCardFromJSON(card));
					}
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				setButtonsEnabled(isTurn);
				cardSelected = null;
				break;
			case Constants.MSG_WINNER:
				playerContext.unregisterReceiver();
				Intent winner = new Intent(playerContext, GameResultsActivity.class);
				winner.putExtra(GameResultsActivity.IS_WINNER, true);
				playerContext.startActivityForResult(winner, QUIT_GAME);
				break;
			case Constants.MSG_LOSER:
				playerContext.unregisterReceiver();
				Intent loser = new Intent(playerContext, GameResultsActivity.class);
				loser.putExtra(GameResultsActivity.IS_WINNER, false);
				playerContext.startActivityForResult(loser, QUIT_GAME);
				break;
			}
		}

	}

	/**
	 * The OnClickListener for the play button
	 */
	private OnClickListener playClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (isTurn && gameRules.checkCard(cardSelected, cardOnDiscard) && cardHand.size() != 0) {
				// play card
				if (cardSelected.getValue() == C8Constants.EIGHT_CARD_NUMBER) {
					Intent selectSuit = new Intent(playerContext, SelectSuitActivity.class);
					playerContext.startActivityForResult(selectSuit, CHOOSE_SUIT);
					// go to the onActivityResult to finish this turn
				} else {
					connection.write(Constants.MSG_PLAY_CARD, cardSelected);

					playerContext.removeFromHand(cardSelected.getIdNum());

					cardSelected = null;
					setButtonsEnabled(false);
					isTurn = false;
					cardSuggestedId = -1;
					playerContext.setSelected(-1, cardSuggestedId);
				}
			}
		}
	};

	/**
	 * The OnClickListener for the draw button
	 */
	private OnClickListener drawClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (isTurn) {
				connection.write(Constants.MSG_DRAW_CARD, null);
				setButtonsEnabled(false);
				isTurn = false;
				cardSuggestedId = -1;
			}
		}
	};

	/* (non-Javadoc)
	 * @see cs309.a1.shared.PlayerController#getCardClickListener()
	 */
	@Override
	public OnClickListener getCardClickListener() {
		return new CardSelectionClickListener();
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.PlayerController#handleActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void handleActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CHOOSE_SUIT) {
			boolean isSuitChosen = true;
			switch (resultCode) {
			case Constants.SUIT_CLUBS:
				connection.write(C8Constants.PLAY_EIGHT_C, cardSelected);
				break;
			case Constants.SUIT_DIAMONDS:
				connection.write(C8Constants.PLAY_EIGHT_D, cardSelected);
				break;
			case Constants.SUIT_HEARTS:
				connection.write(C8Constants.PLAY_EIGHT_H, cardSelected);
				break;
			case Constants.SUIT_SPADES:
				connection.write(C8Constants.PLAY_EIGHT_S, cardSelected);
				break;
			case Activity.RESULT_OK:
				isSuitChosen = false;
				break;
			}

			if (isSuitChosen) {
				playerContext.removeFromHand(cardSelected.getIdNum());
				cardSelected = null;
				setButtonsEnabled(false);
				isTurn = false;
				cardSuggestedId = -1;
			}
		}
	}

	/**
	 * Used to set the play and draw buttons to enable or disabled
	 * Also if it is the player's turn then set the cards to be greyed
	 * out if they are not playable. if it is not the player's turn then
	 * do not grey out any cards
	 * 
	 * @param isEnabled
	 */
	private void setButtonsEnabled(boolean isEnabled) {
		play.setEnabled(isEnabled);
		draw.setEnabled(isEnabled);
		if (isEnabled) {
			// it is your turn grey out cards
			for (Card c : cardHand) {
				boolean isPlayable = gameRules.checkCard(c, cardOnDiscard);
				playerContext.setCardPlayable(c.getIdNum(), isPlayable);
			}
		} else {
			// it is not your turn make cards normal
			if (playerHandLayout != null) {
				for (int i = 0; i < playerHandLayout.getChildCount(); i++) {
					ImageView v = (ImageView) playerHandLayout.getChildAt(i);
					playerContext.setCardPlayable(v.getId(), true);
				}
			}
		}
	}

	/**
	 * Sets the player's name
	 * 
	 * @param name - the player's name
	 */
	@Override
	public void setPlayerName(String name) {
		playerName = name;
	}

	/**
	 * This will be used for each card ImageView and will allow the card to be
	 * selected when it is Clicked
	 */
	private class CardSelectionClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			// Show an animation indicating the card was selected
			ScaleAnimation scale = new ScaleAnimation((float) 1.2, (float) 1.2,	(float) 1.2, (float) 1.2);
			scale.scaleCurrentDuration(5);
			v.startAnimation(scale);

			// Let the UI know which card was selected
			playerContext.setSelected(v.getId(), cardSuggestedId);

			for (int i = 0; i < cardHand.size(); i++) {
				if (cardHand.get(i).getIdNum() == v.getId()) {
					cardSelected = cardHand.get(i);
				}
			}
		}
	}

	@Override
	public PlayerStateFull getPlayerState() {
		return playerState;
	}
}
