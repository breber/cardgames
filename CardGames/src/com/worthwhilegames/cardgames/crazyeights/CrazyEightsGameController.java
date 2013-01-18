package com.worthwhilegames.cardgames.crazyeights;

import static com.worthwhilegames.cardgames.shared.Constants.PREFERENCES;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.worthwhilegames.cardgames.gameboard.activities.GameboardActivity;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.GameController;
import com.worthwhilegames.cardgames.shared.Player;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;
import com.worthwhilegames.cardgames.shared.connection.ConnectionServer;

/**
 * This is the GameController for the game of Crazy Eights.
 * 
 * Responsible for communicating game info, advancing turns, and handling game
 * state
 */
public class CrazyEightsGameController extends GameController {

	/**
	 * This represents the suit chosen when an 8 is played
	 */
	private int suitChosen = -1;

	/**
	 * Computer player that makes moves for the computer players
	 */
	private CrazyEightsComputerPlayer computerPlayer;

	/**
	 * Computer player that suggests moves for the human players
	 */
	private CrazyEightsComputerPlayer cardSuggestor;

	/**
	 * This will initialize a CrazyEightsGameController
	 * 
	 * @param context
	 *            Context of the GameBoardActivity
	 * @param connectionGiven
	 *            The ConnectionServer that will be used
	 */
	public CrazyEightsGameController(GameboardActivity context,	ConnectionServer connectionGiven) {
		super.initGameController(context, connectionGiven);

		// Crazy Eights specific setup
		gameRules = new CrazyEightGameRules();
		SharedPreferences sharedPreferences = gameContext.getSharedPreferences(PREFERENCES, 0);
		String difficulty = sharedPreferences.getString(Constants.PREF_DIFFICULTY, Constants.EASY);
		computerPlayer = new CrazyEightsComputerPlayer(difficulty);
		cardSuggestor = new CrazyEightsComputerPlayer(Constants.MEDIUM);

		game = CrazyEightsTabletGame.getInstance();
		game.setup();
		players = game.getPlayers();
		gameContext.highlightPlayer(1);

		for (Player p : players) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, p.getName() + ": " + p);
			}

			server.write(Constants.MSG_SETUP, p, p.getId());
		}

		Card onDiscard = game.getDiscardPileTop();

		mySM.shuffleCardsSound();
		gameContext.updateUi();
		// Update the indicator on the gameboard with the current suit
		gameContext.updateSuit(onDiscard.getSuit());

		server.write(Constants.MSG_IS_TURN, onDiscard, players.get(whoseTurn).getId());
		sendCardSuggestion();
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.GameController#handleBroadcastReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void handleBroadcastReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (ConnectionConstants.MESSAGE_RX_INTENT.equals(action)) {
			int messageSender = getWhoSentMessage(context, intent);
			String object = intent.getStringExtra(ConnectionConstants.KEY_MESSAGE_RX);
			int messageType = intent.getIntExtra(ConnectionConstants.KEY_MESSAGE_TYPE, -1);

			// Only perform actions if it is the sender's turn
			if (messageSender == whoseTurn) {
				switch (messageType) {
				case Constants.MSG_PLAY_CARD:
					playReceivedCard(object);
					advanceTurn();
					break;
				case C8Constants.PLAY_EIGHT_C:
					suitChosen = Constants.SUIT_CLUBS;
					playReceivedCard(object);
					advanceTurn();
					break;
				case C8Constants.PLAY_EIGHT_D:
					suitChosen = Constants.SUIT_DIAMONDS;
					playReceivedCard(object);
					advanceTurn();
					break;
				case C8Constants.PLAY_EIGHT_H:
					suitChosen = Constants.SUIT_HEARTS;
					playReceivedCard(object);
					advanceTurn();
					break;
				case C8Constants.PLAY_EIGHT_S:
					suitChosen = Constants.SUIT_SPADES;
					playReceivedCard(object);
					advanceTurn();
					break;
				case Constants.MSG_DRAW_CARD:
					drawCard();
					advanceTurn();
					break;
				case Constants.MSG_REFRESH:
					refreshPlayers();
					break;
				}
			} else {
				Log.d(TAG, "It isn't " + messageSender + "'s turn - ignoring message");
				Log.w(TAG, "messageSender: " + messageSender + " whoseTurn: " + whoseTurn);
				// refresh players to get everyone to the same state.
				refreshPlayers();
			}
		}
	}

	/**
	 * This function will be called when a players turn is over It will change
	 * whoseTurn to the next player and send them the message that it is their
	 * turn
	 */
	private void advanceTurn() {
		// If the game is over, proceed to the declare winner section of the
		// code
		if (game.isGameOver(players.get(whoseTurn))) {
			declareWinner(whoseTurn);
			return;
		}

		// Figure out whose turn it is now
		if (whoseTurn < game.getNumPlayers() - 1) {
			whoseTurn++;
		} else {
			whoseTurn = 0;
		}

		// Highlight the name of the current player
		gameContext.highlightPlayer(whoseTurn + 1);

		// Get the top discard pile card, so that we can tell the user which
		// cards
		// are valid moves
		Card onDiscard = game.getDiscardPileTop();

		// If the top card is an 8, we need to do some special logic
		// to figure out the actual suit of the card
		if (onDiscard.getValue() == C8Constants.EIGHT_CARD_NUMBER) {
			// If this is the first card to be chosen, just set the chosen
			// suit to be the suit of the 8. Otherwise the suitChosen variable
			// should already have the value of the new suit
			if (suitChosen == -1) {
				suitChosen = onDiscard.getSuit();
			}

			// Create a temporary card for sending to the players
			onDiscard = new Card(suitChosen, onDiscard.getValue(),
					onDiscard.getResourceId(), onDiscard.getIdNum());
		}

		// Update the Game board display with an indication of the current suit
		gameContext.updateSuit(onDiscard.getSuit());

		// If this is a computer, start having the computer play
		if (players.get(whoseTurn).getIsComputer()) {
			// play turn for computer player if not already
			if (!isComputerPlaying) {
				startComputerTurn();
			}
		} else {
			// tell the player it is their turn
			server.write(Constants.MSG_IS_TURN, onDiscard, players.get(whoseTurn).getId());
			sendCardSuggestion();
		}

		// Update the UI
		gameContext.updateUi();
	}

	/**
	 * This will send winner and loser messages to all the players depending on
	 * if they won or not
	 * 
	 * @param whoWon
	 *            The player that won
	 */
	private void declareWinner(int whoWon) {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "Sending winner and loser info");
		}

		// Let each player know whether they won or lost
		for (int i = 0; i < game.getNumPlayers(); i++) {
			if (i == whoWon) {
				server.write(Constants.MSG_WINNER, null, players.get(i).getId());
			} else {
				server.write(Constants.MSG_LOSER, null, players.get(i).getId());
			}
		}

		String winnerName = players.get(whoWon).getName();

		super.declareWinner(winnerName);
	}

	/**
	 * This draws a card in the tablet game instance and sends that card to the
	 * player
	 */
	private void drawCard() {
		// Play draw card sound
		mySM.drawCardSound();

		Card tmpCard = game.draw(players.get(whoseTurn));

		if (tmpCard != null) {
			// And send the card to the player
			server.write(Constants.MSG_CARD_DRAWN, tmpCard, players.get(whoseTurn).getId());
		} else {
			// there are no cards to draw so make it no longer that players turn
			// and refresh the players
			advanceTurn();
			refreshPlayers();
		}
	}


	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.GameController#refreshPlayers()
	 */
	@Override
	protected void refreshPlayers() {
		// Unpause the game
		unpause();

		// Send users information
		Player pTurn = players.get(whoseTurn);

		// send the card on the discard pile
		Card discard = game.getDiscardPileTop();
		JSONObject discardObj = discard.toJSONObject();

		for (Player p : players) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, p.getName() + " refreshed : " + p);
			}

			try {
				// Create the base refresh info object
				JSONObject refreshInfo = new JSONObject();
				refreshInfo.put(Constants.KEY_TURN, pTurn.equals(p));
				refreshInfo.put(Constants.KEY_PLAYER_NAME, p.getName());

				// send the card on the discard pile
				refreshInfo.put(Constants.KEY_DISCARD_CARD, discardObj);

				// send all the cards in the players hand
				JSONArray arr = new JSONArray();
				for (Card c : p.getCards()) {
					arr.put(c.toJSONObject());
				}

				// send the card in their hand
				refreshInfo.put(Constants.KEY_CURRENT_HAND, arr);

				server.write(Constants.MSG_REFRESH, refreshInfo.toString(), p.getId());
				if(pTurn.equals(p) && !p.getIsComputer()){
					sendCardSuggestion();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// If the next player is a computer, and the computer isn't currently
		// playing, have the computer initiate a move
		if (players.get(whoseTurn).getIsComputer() && !isComputerPlaying) {
			startComputerTurn();
		}
	}

	/**
	 * This will asynchronously figure out which card a player should play
	 * and send it to them as a suggestion
	 */
	protected void sendCardSuggestion() {
		final int currentTurn = whoseTurn;
		final int currentSuitChosen = suitChosen;

		new Thread(new Runnable() {
			@Override
			public void run() {
				Card cardSelected = null;

				cardSelected = cardSuggestor.getCardOnTurn(currentTurn, currentSuitChosen);

				server.write(Constants.MSG_SUGGESTED_CARD, cardSelected, players.get(currentTurn).getId());
				if (Util.isDebugBuild()) {
					Log.d(TAG, "Sent suggestion to player");
				}
			}
		}).start();
	}

	/**
	 * This will play for a computer player based on the difficulty level. either play or draw a card.
	 * This will be called after the PlayComputerTurnActivity has waited for the appropriate amount of time.
	 * level 0 	should just loop through the cards to find one that it is allowed to play
	 * 			very basic, randomly play a card if able or draw if not able
	 * level 1 	chooses first the cards of the same suit, then cards of the same index of a different suit,
	 * 			then a special card as a last resort. if a suit of the same index
	 *
	 * level 2 	nothing yet
	 */
	@Override
	protected void playComputerTurn() {
		Card cardSelected = null;

		cardSelected = computerPlayer.getCardOnTurn(whoseTurn, suitChosen);

		if (cardSelected != null && cardSelected.getValue() == C8Constants.EIGHT_CARD_NUMBER) {
			suitChosen = computerPlayer.getSuitChosen(whoseTurn, players.get(whoseTurn).getCards());
		}

		if (cardSelected != null) {
			//Play Card
			mySM.playCardSound();

			game.discard(players.get(whoseTurn), cardSelected);
		} else {
			// Draw Card
			Card tmpCard = game.draw(players.get(whoseTurn));

			// If card is null then there are no cards to draw so just move on and allow the turn to advance
			if (tmpCard != null) {
				mySM.drawCardSound();
			}
		}

		//the computer has finished, advance the turn
		advanceTurn();
	}

}
