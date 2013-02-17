package com.worthwhilegames.cardgames.euchre;

import static com.worthwhilegames.cardgames.euchre.EuchreConstants.BET;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.FIRST_ROUND_BETTING;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.GO_ALONE;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.PICK_IT_UP;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.PLAY_LEAD_CARD;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.ROUND_OVER;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.SECOND_ROUND_BETTING;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.TRUMP;
import static com.worthwhilegames.cardgames.shared.Constants.MSG_PLAY_CARD;
import static com.worthwhilegames.cardgames.shared.Constants.PREFERENCES;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.worthwhilegames.cardgames.gameboard.activities.GameboardActivity;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.GameController;
import com.worthwhilegames.cardgames.shared.Player;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.activities.RoundScoresActivity;
import com.worthwhilegames.cardgames.shared.connection.ConnectionServer;

public class EuchreGameController extends GameController {

	/**
	 * request code to allow the gameboard to choose which player to connect
	 */
	private static final int DECLARE_ROUND_SCORES = Math.abs("DECLARE_ROUND_SCORES".hashCode());

	/**
	 * This game object will keep track of the current state of the game and be
	 * used to manage player hands and draw and discard piles
	 * 
	 * specific for Euchre since there are a lot of specific methods
	 */
	protected static EuchreTabletGame game = null;

	/**
	 * This is how to tell if the gameboard is in the waiting phase to clear the
	 * cards played on the last trick
	 */
	private boolean isWaitingToClearCards = false;

	/**
	 * Computer player that makes moves for the computer players
	 */
	private EuchreComputerPlayer computerPlayer;

	/**
	 * Computer player that suggests moves for the human players
	 */
	private EuchreComputerPlayer cardSuggestor;

	/**
	 * Handler to handle showing all 4 cards played
	 */
	@SuppressLint("HandlerLeak")
	private Handler cardPlayingHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, "handleMessage: waiting to remove cards");
			}

			if (!isPaused && isWaitingToClearCards) {
				isWaitingToClearCards = false;
				endTurnRound();
			} else {
				if (Util.isDebugBuild()) {
					Log.d(TAG, "handleMessage: game paused. not going to play now");
				}
			}
		}
	};

	/**
	 * @param context
	 * @param connectionGiven
	 * @param playersGiven
	 */
	public EuchreGameController(GameboardActivity context, ConnectionServer connectionGiven) {
		super.initGameController(context, connectionGiven);

		// Euchre specific setup
		gameRules = new EuchreGameRules();
		SharedPreferences sharedPreferences = gameContext.getSharedPreferences(PREFERENCES, 0);
		String difficulty = sharedPreferences.getString(Constants.PREF_DIFFICULTY, Constants.EASY);
		computerPlayer = new EuchreComputerPlayer(difficulty);
		cardSuggestor = new EuchreComputerPlayer(Constants.MEDIUM);

		game = EuchreTabletGame.getInstance();
		genericGame = game;

		game.setup();
		players = game.getPlayers();
		startRound();
	}

	private void startRound() {
		game.startRound();
		game.whoseTurn = game.getTrickLeader();

		for (Player p : players) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, p.getName() + ": " + p);
			}

			server.write(Constants.MSG_SETUP, p, p.getId());
		}


		mySM.shuffleCardsSound();
		gameContext.updateUi();
		// Update the indicator on the gameboard with the current suit
		gameContext.updateSuit(game.getDiscardPileTop().getSuit());

		updateGameStateFull();

		//unbold the players names
		gameContext.unboldAllPlayerText();

		// If this is a computer, start having the computer play
		game.currentState = FIRST_ROUND_BETTING;

		// tell first person to bet
		sendNextTurn(FIRST_ROUND_BETTING);
	}


	@Override
	public void handleMessage(int messageType, String object) {
		switch (messageType) {
		case FIRST_ROUND_BETTING:
			handleBetting(FIRST_ROUND_BETTING, object);
			break;
		case SECOND_ROUND_BETTING:
			handleBetting(SECOND_ROUND_BETTING, object);
			break;
		case PLAY_LEAD_CARD:
			Card tmpCard = playReceivedCard(object);
			game.setCardLead(tmpCard);
			advanceTurn();
			break;
		case PICK_IT_UP:
			//Get which card they discarded and discard it.
			game.currentState = PLAY_LEAD_CARD;
			playReceivedCard(object);
			game.clearCardsPlayed();

			//start the first turn of the round by going one person the the right of the dealer.
			game.whoseTurn = game.getTrickLeader();

			refreshPlayers();
			sendNextTurn(game.currentState);
			break;
		case MSG_PLAY_CARD:
			playReceivedCard(object);
			advanceTurn();
			break;
		case Constants.MSG_REFRESH:
			refreshPlayers();
			updateGameStateFull();
			break;
		}

	}


	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.GameController#handleActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
		if (super.handleActivityResult(requestCode, resultCode, data)){
			// This activity result was handled by GameController
			return true;

			// addition of Euchre specific handling
		} else if (requestCode == DECLARE_ROUND_SCORES){
			//They have seen the score move on with game.
			startRound();
			return true;
		} else if(playerController != null){
			return playerController.handleActivityResult(requestCode, resultCode, data);
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.GameController#unpause()
	 */
	@Override
	public void unpause() {
		// send message to players
		super.unpause();

		// If the game is paused while the cards were waiting to
		// be removed then we should remove them now
		if (isWaitingToClearCards) {
			cardPlayingHandler.sendEmptyMessage(0);
		}
	}

	/**
	 * This will update the Game State completely for all players
	 */
	private void updateGameStateFull(){

		for(int i = 0; i < players.size(); i++){
			Player p = game.getPlayers().get(i);
			if(p.getIsPlayerHost() && playerController != null){
				playerController.handleMessage(Constants.MSG_PLAYER_STATE_FULL, game.getPlayerState(i).toJSONObject());
			} else if(i == game.whoseTurn){
				server.write(Constants.MSG_PLAYER_STATE_FULL, game.getPlayerState(i).toJSONObject(), players.get(i).getId());
			} else {
				server.write(Constants.MSG_PLAYER_STATE_PARTIAL, game.getPlayerState(i).toPartialJSONObject(), players.get(i).getId());
			}
		}
	}

	/**
	 * This function will be called when a players turn is over It will change
	 * whoseTurn to the next player and send them the message that it is their
	 * turn
	 */
	private void advanceTurn() {
		game.incrementWhoseTurn();
		gameContext.updateUi();

		if (game.whoseTurn == game.getTrickLeader()) {
			updateGameStateFull();
			startTrickEndWait();
			return;
		}

		game.currentState = MSG_PLAY_CARD;

		//tell the next person to play
		sendNextTurn(game.currentState);

	}


	/**
	 * This will end a trick, determine who won the trick,
	 * add scores if round over, and clear off the cards played.
	 */
	private void endTurnRound(){
		game.determineTrickWinner();

		// Round is over if a player from both teams is out of cards
		if (players.get(0).getCards().size() == 0 && players.get(1).getCards().size() == 0) {
			game.endRound();
			if(game.isGameOver(players.get(0))){
				declareWinner(game.getWinningTeam());
				return;
			}
			declareRoundScores();
		} else {
			//round not over just the current trick

			//TODO update scores on gameboard
			gameContext.updateUi();

			//make the trick leader to play next
			game.whoseTurn = game.getTrickLeader();
			game.currentState = PLAY_LEAD_CARD;

			sendNextTurn(game.currentState);
		}
	}

	/**
	 * This method will send players info about the score
	 */
	private void declareRoundScores() {
		Intent intent = new Intent(gameContext, RoundScoresActivity.class);
		gameContext.startActivityForResult(intent, DECLARE_ROUND_SCORES);

		// TODO send score info

		for (int i = 0; i < game.getNumPlayers(); i++) {
			server.write(ROUND_OVER, null, players.get(i).getId());
		}
	}

	/**
	 * This will send winner and loser messages to all the players depending on
	 * if they won or not
	 * 
	 * @param whoWon
	 *            The player that won
	 */
	private void declareWinner(int teamWhoWon) {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "Sending winner and loser info");
		}

		// Let each player know whether they won or lost
		for (int i = 0; i < game.getNumPlayers(); i++) {
			if (i % 2 == teamWhoWon) {
				server.write(Constants.MSG_WINNER, null, players.get(i).getId());
			} else {
				server.write(Constants.MSG_LOSER, null, players.get(i).getId());
			}
		}

		String winner1Name = players.get(teamWhoWon).getName();
		String winner2Name = players.get(teamWhoWon + 2).getName();

		String winningTeam = winner1Name + " and " + winner2Name;

		super.declareWinner(winningTeam);
	}


	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.GameController#refreshPlayers()
	 */
	@Override
	protected void refreshPlayers() {

		updateGameStateFull();

		// If the next player is a computer, and the computer isn't currently
		// playing, have the computer initiate a move
		// TODO: test this vs method used in crazy eights?
		if (players.get(game.whoseTurn).getIsComputer()) {
			if (isComputerPlaying) {
				// If a computer was playing before the game was refreshed
				// let them know that they can play now
				computerHandler.sendEmptyMessage(0);
			} else {
				//the computer turn has not even begun
				startComputerTurn();
			}
		}
	}

	/**
	 * This will asynchronously figure out which card a player should play
	 * and send it to them as a suggestion
	 */
	protected void sendCardSuggestion() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Card cardSelected = null;
				int currentTurn = game.whoseTurn;

				switch(game.currentState){
				case PICK_IT_UP:
					cardSelected = cardSuggestor.pickItUp(currentTurn);
					break;
				case PLAY_LEAD_CARD:
					cardSelected = cardSuggestor.getLeadCard(currentTurn);
					break;
				case MSG_PLAY_CARD:
					cardSelected = cardSuggestor.getCardOnTurn(currentTurn);
					break;
				default:
					// basically no card is suggested
					cardSelected = new Card(-1, -1, -1, -1);
					break;
				}

				server.write(Constants.MSG_SUGGESTED_CARD, cardSelected, players.get(currentTurn).getId());
				if (Util.isDebugBuild()) {
					Log.d(TAG, "Sent suggestion to player");
				}
			}
		}).start();
	}


	/**
	 * Start a computer's turn.
	 * 
	 * Starts another thread that waits, and then posts a message to the
	 * mHandler letting it know it can play.
	 */
	private void startTrickEndWait() {
		isWaitingToClearCards = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(Constants.COMPUTER_WAIT_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (Util.isDebugBuild()) {
					Log.d(TAG, "startTrickEndWait: showing the cards that have been played at the end of a trick");
				}

				cardPlayingHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	/**
	 * This will play the computer's turn based on computer difficulty in preferences
	 */
	@Override
	protected void playComputerTurn() {
		if (game.currentState == FIRST_ROUND_BETTING || game.currentState == SECOND_ROUND_BETTING) {
			EuchreBet compBet = computerPlayer.getComputerBet(game.whoseTurn, game.currentState, game.getCardLead().getSuit());
			this.handleBetting(game.currentState, compBet.toString());
		} else {
			Card cardSelected = null;

			switch(game.currentState){
			case PICK_IT_UP:
				cardSelected = computerPlayer.pickItUp(game.whoseTurn);
				break;
			case PLAY_LEAD_CARD:
				cardSelected = computerPlayer.getLeadCard(game.whoseTurn);
				break;
			case MSG_PLAY_CARD:
				cardSelected = computerPlayer.getCardOnTurn(game.whoseTurn);
				break;
			default:
				cardSelected = null;
				break;
			}

			if (game.currentState == PICK_IT_UP) {
				// Get which card they discarded and discard it.
				game.currentState = PLAY_LEAD_CARD;
				game.discard(players.get(game.whoseTurn), cardSelected);
				game.clearCardsPlayed();

				// start the first turn of the round
				game.whoseTurn = game.getTrickLeader();
				sendNextTurn(game.currentState);
				return;
			}

			if (game.currentState == PLAY_LEAD_CARD) {
				// set card lead
				game.setCardLead(cardSelected);
			}
			mySM.playCardSound();
			game.discard(players.get(game.whoseTurn), cardSelected);
			advanceTurn();
		}
	}

	/**
	 * This will handle the betting that is done by the players
	 * @param round - which round of betting it is
	 * @param object - JSON object that represents the betting
	 */
	private void handleBetting(int round, String object) {
		EuchreBet bet = null;
		try {
			JSONObject obj = new JSONObject(object);
			int trumpSuit = obj.getInt(TRUMP);
			boolean placeBet = obj.getBoolean(BET);
			boolean goAlone = obj.getBoolean(GO_ALONE);

			bet = new EuchreBet(trumpSuit, placeBet, goAlone);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}

		//first round of betting
		if (round == FIRST_ROUND_BETTING) {
			if (bet.getPlaceBet()) {
				if (bet.getTrumpSuit() == game.getCardLead().getSuit()) {
					//set trump and trump caller
					game.setTrump(bet.getTrumpSuit());
					game.setPlayerCalledTrump(game.whoseTurn);
					this.boldBettingTeam();

					// Update the Game board display with an indication of the current suit
					gameContext.updateSuit(game.getTrump());

					game.setPlayerGoingAlone(bet.getGoAlone());
					if (game.isPlayerGoingAlone() && game.getTrickLeader() == game.getPlayerBeingSkipped()) {
						game.setTrickLeader(game.getTrickLeader() + 1);
					}
					if (!game.isPlayerGoingAlone() || game.getDealer() != game.getPlayerBeingSkipped()) {
						game.whoseTurn = game.getDealer();
						game.currentState = PICK_IT_UP;
						players.get(game.getDealer()).addCard(game.getCardLead());
					} else {
						game.whoseTurn = game.getTrickLeader();
						game.currentState = PLAY_LEAD_CARD;
					}

					//tell the dealer to "pick it up"
					mySM.drawCardSound();
					game.clearCardsPlayed();
				} else {
					// Don't let player choose suit other than card lead suit
					refreshPlayers();
					return;
				}
			} else {
				if (game.whoseTurn == game.getDealer()) {
					// start betting round 2
					game.currentState = SECOND_ROUND_BETTING;
				}
				game.incrementWhoseTurn();
			}

			//second round of betting
		} else if (round == SECOND_ROUND_BETTING) {
			if (bet.getPlaceBet()) {
				game.setTrump(bet.getTrumpSuit());
				game.setPlayerCalledTrump(game.whoseTurn);
				this.boldBettingTeam();
				gameContext.updateSuit(game.getTrump());

				game.setPlayerGoingAlone(bet.getGoAlone());
				if (game.isPlayerGoingAlone() && game.getTrickLeader() == game.getPlayerBeingSkipped()) {
					game.setTrickLeader(game.getTrickLeader() + 1);
				}

				//set the turn to the first player to play and go
				game.whoseTurn = game.getTrickLeader();

				game.clearCardsPlayed();

				game.currentState = PLAY_LEAD_CARD;
				refreshPlayers();
			} else {
				if (game.whoseTurn != game.getDealer()) {
					game.incrementWhoseTurn();
				}
				//if it is the second round and the dealer
				//	passes we just keep telling him/her to bet. they must.
				//  AKA "stick it to the dealer."
			}
		}

		//all of the cases above should come to this statement.
		sendNextTurn(game.currentState);
	}

	/**
	 * Sends the next turn information to either the computer or player.
	 * @param state the message type to send to the player or computer
	 * @param card the card to give the player or computer
	 */
	private void sendNextTurn(int state) {
		game.currentState = state;
		updateGameStateFull();
		if (players.get(game.whoseTurn).getIsComputer()) {
			if (!isComputerPlaying) {
				startComputerTurn();
			}
		} else {
			sendCardSuggestion();
		}

		// Highlight the name of the current player
		gameContext.highlightPlayer(game.whoseTurn);

		gameContext.updateUi();
	}

	/**
	 * Bolds the betting team's names
	 */
	private void boldBettingTeam() {
		gameContext.unboldAllPlayerText();
		for (int i = 0; i < 4; i++) {
			if (game.getPlayerCalledTrump() % 2 == i % 2) {
				gameContext.boldPlayerText(i);
			}
		}
	}
}
