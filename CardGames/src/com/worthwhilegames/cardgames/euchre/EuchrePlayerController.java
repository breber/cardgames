package com.worthwhilegames.cardgames.euchre;

import static com.worthwhilegames.cardgames.euchre.EuchreConstants.BET;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.FIRST_ROUND_BETTING;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.GO_ALONE;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.LEAD_TRICK;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.PICK_IT_UP;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.PLAY_LEAD_CARD;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.ROUND_OVER;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.SECOND_ROUND_BETTING;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.TRUMP;
import static com.worthwhilegames.cardgames.shared.Constants.CARD_DRAWN;
import static com.worthwhilegames.cardgames.shared.Constants.ID;
import static com.worthwhilegames.cardgames.shared.Constants.IS_TURN;
import static com.worthwhilegames.cardgames.shared.Constants.LOSER;
import static com.worthwhilegames.cardgames.shared.Constants.REFRESH;
import static com.worthwhilegames.cardgames.shared.Constants.SETUP;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT;
import static com.worthwhilegames.cardgames.shared.Constants.VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.WINNER;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightGameRules;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsCardTranslator;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsPlayerController;
import com.worthwhilegames.cardgames.player.activities.GameResultsActivity;
import com.worthwhilegames.cardgames.player.activities.SelectSuitActivity;
import com.worthwhilegames.cardgames.player.activities.ShowCardsActivity;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.CardTranslator;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.PlayerController;
import com.worthwhilegames.cardgames.shared.Rules;
import com.worthwhilegames.cardgames.shared.SoundManager;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionClient;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;

public class EuchrePlayerController implements PlayerController {
	

	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = CrazyEightsPlayerController.class.getName();
	
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
	 * The card that is on the discard pile
	 */
	private Card cardLead;

	/**
	 * An instance of the GameRules that is used to check if a card can be
	 * played
	 */
	private EuchreGameRules gameRules;
	
	/**
	 * The play button on the layout
	 */
	private Button play;

	/**
	 * The draw button on the layout
	 */
	private Button draw;

	/**
	 * This is true if it is the players turn
	 */
	private boolean isTurn = false;
	
	/**
	 * The current selected Card
	 */
	private Card cardSelected;
	
	/**
	 * The client that is used to send messages to the GameBoard
	 */
	private ConnectionClient connection;
	
	/**
	 * This is how we can make sure that the card resource IDs are correct
	 */
	private CardTranslator ct;
	
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
	 * The suit that is currently Trump
	 */
	private int trumpSuit;
	
	/**
	 * current state of play
	 */
	private int currentState;

	/**
	 * The LinearLayout holding all card images
	 */
	private LinearLayout playerHandLayout;
	
	public EuchrePlayerController(Context context, Button playGiven, Button drawGiven, 
			ConnectionClient connectionGiven, ArrayList<Card> cardHandGiven) {
		playerContext = (ShowCardsActivity) context;
		play = playGiven;
		draw = drawGiven;
		play.setOnClickListener(getPlayOnClickListener());
		draw.setOnClickListener(getDrawOnClickListener());
		setButtonsEnabled(false);
		mySM = SoundManager.getInstance(context);
		cardHand = cardHandGiven;
		playerName = "";
		playerHandLayout = (LinearLayout)  playerContext.findViewById(R.id.playerCardContainer);

		gameRules = new EuchreGameRules();
		ct = new EuchreCardTranslator();
		connection = connectionGiven;
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.PlayerController#handleBroadcastReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void handleBroadcastReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
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
			case FIRST_ROUND_BETTING:
				currentState = FIRST_ROUND_BETTING;
				
			case SECOND_ROUND_BETTING:
				currentState = SECOND_ROUND_BETTING;
				
			case PLAY_LEAD_CARD:
				currentState = PLAY_LEAD_CARD;
				
			case PICK_IT_UP:
				
				try {
					JSONObject obj = new JSONObject(object);
					int suit = obj.getInt(SUIT);
					int value = obj.getInt(VALUE);
					int id = obj.getInt(ID);
					playerContext.addCard(new Card(suit, value, ct.getResourceForCardWithId(id), id));
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				dealerDiscard();
				//TODO put in pick it up mode to discard a card
				break;
				
			case IS_TURN:
				mySM.sayTurn(playerName);
				try {
					JSONObject obj = new JSONObject(object);
					int suit = obj.getInt(SUIT);
					int value = obj.getInt(VALUE);
					int id = obj.getInt(ID);
					cardLead = new Card(suit, value, ct.getResourceForCardWithId(id), id);
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				setButtonsEnabled(true);
				isTurn = true;
				break;
			case REFRESH:
				// Parse the refresh Message
				try {
					JSONArray arr = new JSONArray(object);
					JSONObject refreshInfo = arr.getJSONObject(0);
					isTurn = refreshInfo.getBoolean(Constants.TURN);
					playerName = refreshInfo.getString(Constants.PLAYER_NAME);
					// add more refresh info here

					playerContext.removeAllCards();

					JSONObject obj = arr.getJSONObject(1);
					int suit = obj.getInt(SUIT);
					int value = obj.getInt(VALUE);
					int id = obj.getInt(ID);
					cardLead = new Card(suit, value, ct.getResourceForCardWithId(id), id);

					//the 2nd through however many are the cards of the player
					for (int i = 2; i < arr.length(); i++) {
						obj = arr.getJSONObject(i);
						suit = obj.getInt(SUIT);
						value = obj.getInt(VALUE);
						id = obj.getInt(ID);
						playerContext.addCard(new Card(suit, value, ct.getResourceForCardWithId(id), id));
					}
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				setButtonsEnabled(isTurn);
				cardSelected = null;
				break;
			case WINNER://TODO don't disconnect
				playerContext.unregisterReceiver();
				Intent winner = new Intent(playerContext, GameResultsActivity.class);
				winner.putExtra(GameResultsActivity.IS_WINNER, true);
				playerContext.startActivityForResult(winner, QUIT_GAME);
				break;
			case LOSER://TODO don't disconnect
				playerContext.unregisterReceiver();
				Intent loser = new Intent(playerContext, GameResultsActivity.class);
				loser.putExtra(GameResultsActivity.IS_WINNER, false);
				playerContext.startActivityForResult(loser, QUIT_GAME);
				break;
			}
		}

	}

	@Override
	public void handleActivityResult(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OnClickListener getPlayOnClickListener() {
		// TODO Auto-generated method stub
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( ( (isTurn && gameRules.checkCard(cardSelected, trumpSuit, cardLead.getSuit(), cardHand) )
						|| currentState == PICK_IT_UP || currentState == PLAY_LEAD_CARD ) && cardHand.size() != 0) {
					// play card
					
					connection.write(currentState, cardSelected);

					playerContext.removeFromHand(cardSelected.getIdNum());

					cardSelected = null;
					setButtonsEnabled(false);
					isTurn = false;
				}
			}
		};
	}

	@Override
	public OnClickListener getDrawOnClickListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OnClickListener getCardClickListener() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void setPlayerName(String name) {
		// TODO Auto-generated method stub

	}
	
	private void dealerDiscard(){
		setButtonsEnabled(true);
		isTurn = true;
		//TODO make the play button say discard
		//TODO make there a message to say discard a card
		//
	}

}
