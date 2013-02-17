package com.worthwhilegames.cardgames.euchre;

import static com.worthwhilegames.cardgames.euchre.EuchreConstants.FIRST_ROUND_BETTING;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.PICK_IT_UP;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.PLAY_LEAD_CARD;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.ROUND_OVER;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.SECOND_ROUND_BETTING;
import static com.worthwhilegames.cardgames.shared.Constants.MSG_LOSER;
import static com.worthwhilegames.cardgames.shared.Constants.MSG_PLAYER_STATE_FULL;
import static com.worthwhilegames.cardgames.shared.Constants.MSG_PLAYER_STATE_PARTIAL;
import static com.worthwhilegames.cardgames.shared.Constants.MSG_PLAY_CARD;
import static com.worthwhilegames.cardgames.shared.Constants.MSG_REFRESH;
import static com.worthwhilegames.cardgames.shared.Constants.MSG_SETUP;
import static com.worthwhilegames.cardgames.shared.Constants.MSG_SUGGESTED_CARD;
import static com.worthwhilegames.cardgames.shared.Constants.MSG_WINNER;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_CLUBS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_DIAMONDS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_HEARTS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_SPADES;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.player.activities.GameResultsActivity;
import com.worthwhilegames.cardgames.player.activities.SelectSuitActivity;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.PlayerController;
import com.worthwhilegames.cardgames.shared.PlayerState;
import com.worthwhilegames.cardgames.shared.Util;

public class EuchrePlayerController extends PlayerController {

	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = EuchreGameController.class.getName();

	/**
	 * An instance of the GameRules that is used to check if a card can be
	 * played
	 */
	private EuchreGameRules gameRules;

	/**
	 * The Bet button on the layout
	 */
	private Button bet;

	/**
	 * The Go Alone button on the layout
	 */
	private Button goAlone;

	/**
	 * The choose suit imageview that is used as a button
	 */
	private ImageView chooseSuit;

	/**
	 * This is to keep track if a betting activity is open now or not
	 */
	private boolean isBettingNow = false;

	/**
	 * The suit that is currently Trump
	 */
	private int trumpSuit;


	/**
	 * Initialize the playerController with the necessary setup
	 * @param context ShowCardsActivity context
	 */
	public EuchrePlayerController(Activity context) {

		super.initPlayerController(context);

		play = (Button) context.findViewById(R.id.passOption);
		bet = (Button) context.findViewById(R.id.betOption);
		goAlone = (Button) context.findViewById(R.id.goAloneOption);
		chooseSuit = (ImageView) context.findViewById(R.id.betTrumpSuit);

		play.setOnClickListener(playClickListener);
		bet.setOnClickListener(betClickListener);
		goAlone.setOnClickListener(goAloneClickListener);
		chooseSuit.setOnClickListener(chooseSuitClickListener);

		setButtonsEnabled(false);
		noButtonView();
		gameRules = new EuchreGameRules();
	}


	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.PlayerController#handleMessage(int, java.lang.String)
	 */
	@Override
	public void handleMessage(int messageType, String object) {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "message: " + object);
		}

		// Get either full state or partial state.
		switch(messageType){
		case MSG_PLAYER_STATE_FULL:
			playerState = PlayerState.createStateFromJSON(object);
			playerContext.updateUi();
			playerContext.updateNamesOnGameboard();
			messageType = playerState.currentState;
			break;
		case MSG_PLAYER_STATE_PARTIAL:
			playerState.updateFromPartialState(object);
			playerContext.updateUi();
			messageType = playerState.currentState;
			break;
		}

		// Handle view changes based on current state.
		switch (messageType) {
		case MSG_SETUP:
			setButtonsEnabled(false);
			break;
		case FIRST_ROUND_BETTING: /* purposely have both here */
		case SECOND_ROUND_BETTING:
			if(isMyTurn()){
				mySM.sayBet(playerState.playerNames[playerState.playerIndex]);
				if(playerState.currentState == SECOND_ROUND_BETTING) {
					// Not chosen yet
					trumpSuit = -1;
				} else {
					trumpSuit = playerState.extraInfo1;
				}

				// start select bet activity for round 1
				// start select bet activity to let the player bet
				isBettingNow = true;
				bettingView();
				setButtonsEnabled(true);
			}
			break;
		case PLAY_LEAD_CARD:
			if(isMyTurn()){
				playingView();
				mySM.sayTurn(playerState.playerNames[playerState.playerIndex]);
				setButtonsEnabled(isMyTurn());
			}
			break;
		case PICK_IT_UP:
			if(isMyTurn()){
				mySM.sayPickItUp(playerState.playerNames[playerState.playerIndex]);
				dealerDiscardView();
			}
			break;
		case MSG_PLAY_CARD:
			if(isMyTurn()){
				playingView();
				mySM.sayTurn(playerState.playerNames[playerState.playerIndex]);
				setButtonsEnabled(isMyTurn());
			}
			break;
		case MSG_SUGGESTED_CARD:
			if (isMyTurn() && object != null && isPlayAssistMode) {
				try {
					JSONObject obj = new JSONObject(object);
					int id = obj.getInt(Constants.KEY_CARD_ID);
					cardSuggestedId = id;

					// Let the UI know which card was suggested
					int selectedId = -1;
					if (cardSelected != null) {
						selectedId = cardSelected.getIdNum();
					}
					playerContext.setSelected(selectedId, cardSuggestedId);
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
			}
		case MSG_REFRESH:
			//TODO remove
			// This should be covered in gamestatefull reception.
			break;
		case ROUND_OVER:
			// TODO display score
			break;
		case MSG_WINNER:
			playerContext.unregisterReceiver();
			Intent winner = new Intent(playerContext, GameResultsActivity.class);
			winner.putExtra(GameResultsActivity.IS_WINNER, true);
			playerContext.startActivityForResult(winner, QUIT_GAME);
			break;
		case MSG_LOSER:
			playerContext.unregisterReceiver();
			Intent loser = new Intent(playerContext, GameResultsActivity.class);
			loser.putExtra(GameResultsActivity.IS_WINNER, false);
			playerContext.startActivityForResult(loser, QUIT_GAME);
			break;
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.PlayerController#getPlayerState()
	 */
	@Override
	public PlayerState getPlayerState() {
		return playerState;
	}

	@Override
	public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CHOOSE_SUIT) {
			trumpSuit = resultCode;
			updateTrumpSuit();
			return true;
		}

		return false;
	}

	/**
	 * The OnClickListener for the play button
	 */
	private OnClickListener playClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if ((isMyTurn() && cardSelected != null)
					&& playerState.currentState != FIRST_ROUND_BETTING
					&& playerState.currentState != SECOND_ROUND_BETTING
					&& (playerState.currentState == PICK_IT_UP
					|| playerState.currentState == PLAY_LEAD_CARD || gameRules.checkCard(cardSelected, playerState.suitDisplay, playerState.onDiscard,
							playerState.cards)) && playerState.cards.size() != 0) {
				// play card or discard if it is pick_it_up mode

				sendMessage(playerState.currentState, cardSelected.toString());

				playerContext.removeFromHand(cardSelected.getIdNum());

				if (playerState.currentState == PICK_IT_UP) {
					play.setText(R.string.Play);
				} else {
					playerState.cardsPlayed[playerState.playerIndex] = cardSelected;
					playerContext.updateUi();
				}

				cardSelected = null;
				setButtonsEnabled(false);
				noButtonView();
				playerState.whoseTurn++;
				cardSuggestedId = -1;
				playerContext.setSelected(-1, cardSuggestedId);
			} else if (playerState.currentState == FIRST_ROUND_BETTING || playerState.currentState == SECOND_ROUND_BETTING) {
				EuchreBet bet = new EuchreBet(trumpSuit, false, false);

				sendMessage(playerState.currentState, bet.toString());

				isBettingNow = false;

				setButtonsEnabled(false);
				noButtonView();
				playerState.whoseTurn++;
				cardSuggestedId = -1;
				playerContext.setSelected(-1, cardSuggestedId);
			}
		}
	};


	/**
	 * The OnClickListener for the bet button
	 */
	private OnClickListener betClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (playerState.currentState == FIRST_ROUND_BETTING || playerState.currentState == SECOND_ROUND_BETTING) {
				if (playerState.currentState == SECOND_ROUND_BETTING && trumpSuit == playerState.onDiscard.getSuit()) {
					trumpSuit = -1;
					updateTrumpSuit();
					//TODO tell player they can't choose the suit that was offered to bet on in the first round
				}

				if (trumpSuit == -1) {
					ScaleAnimation scale = new ScaleAnimation((float) 1.1, (float) 1.1,	(float) 1.2, (float) 1.2);
					scale.scaleCurrentDuration(5);
					chooseSuit.startAnimation(scale);
					return;
				}

				EuchreBet bet = new EuchreBet(trumpSuit, true, false);

				sendMessage(playerState.currentState, bet.toString());

				isBettingNow = false;

				setButtonsEnabled(false);
				noButtonView();
				playerState.whoseTurn++;
				cardSuggestedId = -1;
			}
		}
	};

	/**
	 * The OnClickListener for the go alone button
	 */
	private OnClickListener goAloneClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (playerState.currentState == FIRST_ROUND_BETTING || playerState.currentState == SECOND_ROUND_BETTING) {
				if (playerState.currentState == SECOND_ROUND_BETTING && trumpSuit == playerState.onDiscard.getSuit()) {
					trumpSuit = -1;
					updateTrumpSuit();
					//TODO tell player they can't choose the suit that was offered to bet on in the first round
				}
				if (trumpSuit == -1) {
					ScaleAnimation scale = new ScaleAnimation((float) 1.1, (float) 1.1,	(float) 1.2, (float) 1.2);
					scale.scaleCurrentDuration(5);
					chooseSuit.startAnimation(scale);
					return;
				}
				EuchreBet bet = new EuchreBet(trumpSuit, true, true);

				sendMessage(playerState.currentState, bet.toString());

				isBettingNow = false;

				setButtonsEnabled(false);
				noButtonView();
				playerState.whoseTurn++;
				cardSuggestedId = -1;
			}
		}
	};

	/**
	 * The OnClickListener for the choose suit button
	 */
	private OnClickListener chooseSuitClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (playerState.currentState == FIRST_ROUND_BETTING) {
				//do nothing
			} else if (playerState.currentState == SECOND_ROUND_BETTING) {
				v.setEnabled(false);
				Intent selectSuit = new Intent(playerContext, SelectSuitActivity.class);
				playerContext.startActivityForResult(selectSuit, CHOOSE_SUIT);
				v.setEnabled(true);
			}
		}
	};


	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.PlayerController#getCardClickListener()
	 */
	@Override
	public OnClickListener getCardClickListener() {
		return new CardSelectionClickListener();
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
		bet.setEnabled(isEnabled);
		goAlone.setEnabled(isEnabled);
		if (isEnabled && playerState.currentState == MSG_PLAY_CARD) {
			// it is your turn grey out cards
			for (Card c : playerState.cards) {
				boolean isPlayable = gameRules.checkCard(c, playerState.suitDisplay, playerState.onDiscard, playerState.cards) ;
				playerContext.setCardPlayable(c.getIdNum(), isPlayable || playerState.currentState == PICK_IT_UP || playerState.currentState == PLAY_LEAD_CARD);
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

	private void dealerDiscardView(){
		play.setText(R.string.Discard);
		play.setVisibility(View.VISIBLE);
		play.setEnabled(true);
		bet.setVisibility(View.INVISIBLE);
		bet.setEnabled(false);
		goAlone.setVisibility(View.INVISIBLE);
		goAlone.setEnabled(false);
		chooseSuit.setVisibility(View.INVISIBLE);
		chooseSuit.setEnabled(false);
	}

	private void bettingView(){
		play.setText(R.string.pass_option);
		play.setVisibility(View.VISIBLE);
		play.setEnabled(true);
		bet.setVisibility(View.VISIBLE);
		bet.setEnabled(true);
		goAlone.setVisibility(View.VISIBLE);
		goAlone.setEnabled(true);
		chooseSuit.setVisibility(View.VISIBLE);
		chooseSuit.setEnabled(playerState.currentState == SECOND_ROUND_BETTING);
		updateTrumpSuit();
	}

	private void playingView(){
		play.setText(R.string.Play);
		play.setVisibility(View.VISIBLE);
		play.setEnabled(true);
		bet.setVisibility(View.INVISIBLE);
		bet.setEnabled(false);
		goAlone.setVisibility(View.INVISIBLE);
		goAlone.setEnabled(false);
		chooseSuit.setVisibility(View.INVISIBLE);
		chooseSuit.setEnabled(false);
	}

	private void noButtonView(){
		play.setVisibility(View.INVISIBLE);
		play.setEnabled(false);
		bet.setVisibility(View.INVISIBLE);
		bet.setEnabled(false);
		goAlone.setVisibility(View.INVISIBLE);
		goAlone.setEnabled(false);
		chooseSuit.setVisibility(View.INVISIBLE);
		chooseSuit.setEnabled(false);
	}

	/**
	 * Update the displayed Trump suit
	 */
	private void updateTrumpSuit() {
		switch (trumpSuit) {
		case SUIT_CLUBS:
			chooseSuit.setBackgroundResource(R.drawable.clubsuitimage);
			break;
		case SUIT_DIAMONDS:
			chooseSuit.setBackgroundResource(R.drawable.diamondsuitimage);
			break;
		case SUIT_HEARTS:
			chooseSuit.setBackgroundResource(R.drawable.heartsuitimage);
			break;
		case SUIT_SPADES:
			chooseSuit.setBackgroundResource(R.drawable.spadesuitimage);
			break;
		case (-1):
		default:
			//TODO set this to  ? image
			chooseSuit.setBackgroundResource(R.drawable.spadesuitimage);
			break;
		}
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

			for (int i = 0; i < playerState.cards.size(); i++) {
				if (playerState.cards.get(i).getIdNum() == v.getId()) {
					cardSelected = playerState.cards.get(i);
				}
			}
		}
	}

}
