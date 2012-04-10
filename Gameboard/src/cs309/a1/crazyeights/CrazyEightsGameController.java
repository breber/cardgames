package cs309.a1.crazyeights;

import static cs309.a1.crazyeights.Constants.ID;
import static cs309.a1.crazyeights.Constants.NUMBER_OF_CARDS_PER_HAND;
import static cs309.a1.crazyeights.Constants.RESOURCE_ID;
import static cs309.a1.crazyeights.Constants.SUIT;
import static cs309.a1.crazyeights.Constants.VALUE;
import static cs309.a1.shared.CardGame.CRAZY_EIGHTS;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import cs309.a1.gameboard.R;
import cs309.a1.gameboard.activities.ConnectActivity;
import cs309.a1.gameboard.activities.GameResultsActivity;
import cs309.a1.gameboard.activities.GameboardActivity;
import cs309.a1.gameboard.activities.PlayComputerTurnActivity;
import cs309.a1.shared.Card;
import cs309.a1.shared.CardTranslator;
import cs309.a1.shared.Deck;
import cs309.a1.shared.Game;
import cs309.a1.shared.GameController;
import cs309.a1.shared.Player;
import cs309.a1.shared.Rules;
import cs309.a1.shared.SoundManager;
import cs309.a1.shared.Util;
import cs309.a1.shared.bluetooth.BluetoothServer;
import cs309.a1.shared.connection.ConnectionConstants;
import cs309.a1.shared.connection.ConnectionServer;

/**
 * This is the GameController for the game of Crazy Eights
 */
public class CrazyEightsGameController implements GameController {

	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = CrazyEightsGameController.class.getName();

	/**
	 * The request code to keep track of the "Player N Won!" activity
	 */
	private static final int DECLARE_WINNER = Math.abs("DECLARE_WINNER".hashCode());

	/**
	 * request code to allow the gameboard to choose which player to connect
	 */
	private static final int CHOOSE_PLAYER = Math.abs("CHOOSE_PLAYER".hashCode());

	/**
	 * request code to wait for a little while before the computer plays
	 */
	private static final int PLAY_COMPUTER_TURN = Math.abs("PLAY_COMPUTER_TURN".hashCode());

	/**
	 * The ConnectionServer that sends and receives messages from other devices
	 */
	private ConnectionServer server;

	/**
	 * the list of current active players
	 */
	private List<Player> players;

	/**
	 * This game object will keep track of the current state of the game and be
	 * used to manage player hands and draw and discard piles
	 */
	private static Game game = null;

	/**
	 * This allows the resource ids of cards to be correct
	 */
	private CardTranslator ct = new CrazyEightsCardTranslator();

	/**
	 * This represents the suit chosen when an 8 is played
	 */
	private int suitChosen = 0;

	/**
	 * This is the context of the GameBoardActivity this allows this class to
	 * call methods and activities as if it were in the GameBoardActivity
	 */
	private GameboardActivity gameContext;

	/**
	 * This will be 0 to 3 to indicate the spot in the players array for the
	 * player currently taking their turn
	 */
	private int whoseTurn = 0;

	/**
	 * This is the refresh button that is on the GameBoard The GameController
	 * will handle any button presses
	 */
	private ImageButton refreshButton;

	/**
	 * The implementation of the Game Rules
	 */
	private CrazyEightGameRules gameRules = new CrazyEightGameRules();

	/**
	 * The sound manager
	 */
	private SoundManager mySM;

	/**
	 * This will initialize a CrazyEightsGameController
	 * 
	 * @param context Context of the GameBoardActivity
	 * @param btsGiven The BluetoothServer that will be used
	 * @param playersGiven The players that will be used in the game
	 * @param refreshGiven The refresh button which will be handled by this
	 *            GameController
	 */
	public CrazyEightsGameController(GameboardActivity context,	BluetoothServer btsGiven,
			List<Player> playersGiven, ImageButton refreshGiven) {
		gameContext = context;
		server = btsGiven;
		players = playersGiven;
		refreshButton = refreshGiven;

		refreshButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setEnabled(false);
				refreshPlayers();
				v.setEnabled(true);
			}
		});

		mySM = new SoundManager(gameContext);

		Deck deck = new Deck(CRAZY_EIGHTS);
		Rules rules = new CrazyEightGameRules();
		game = CrazyEightsTabletGame.getInstance(players, deck, rules);
		game.setup();
		
		gameContext.highlightPlayer(1);

		for (Player p : players) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, p.getName() + ": " + p);
			}

			server.write(Constants.SETUP, p, p.getId());
		}

		placeInitialCards();
		Card onDiscard = game.getDiscardPileTop();
		server.write(Constants.IS_TURN, onDiscard, players.get(whoseTurn).getId());
	}

	/**
	 * This will place the initial cards of each player
	 */
	private void placeInitialCards() {
		mySM.shuffleCardsSound();

		// If it is a debug build, show the cards face up so that we can
		// verify that the tablet has the same cards as the player
		if (Util.isDebugBuild()) {
			for (Player p : players) {
				for (Card c : p.getCards()) {
					gameContext.placeCard(p.getPosition(), c);
				}
			}

			gameContext.placeCard(0, game.getDiscardPileTop());
		} else {
			// Otherwise just show the back of the cards for all players
			for (int i = 0; i < NUMBER_OF_CARDS_PER_HAND * players.size(); i++) {
				gameContext.placeCard(i % 4 + 1, new Card(5, 0,	R.drawable.back_blue_1, 54));
			}

			gameContext.placeCard(0, game.getDiscardPileTop());
		}
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.GameController#handleBroadcastReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void handleBroadcastReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (ConnectionConstants.MESSAGE_RX_INTENT.equals(action)) {
			String object = intent.getStringExtra(ConnectionConstants.KEY_MESSAGE_RX);
			int messageType = intent.getIntExtra(ConnectionConstants.KEY_MESSAGE_TYPE, -1);

			switch (messageType) {
			case Constants.PLAY_CARD:
				discardReceivedCard(object);
				advanceTurn();
				break;
			case Constants.PLAY_EIGHT_C:
				suitChosen = Constants.SUIT_CLUBS;
				discardReceivedCard(object);
				advanceTurn();
				break;
			case Constants.PLAY_EIGHT_D:
				suitChosen = Constants.SUIT_DIAMONDS;
				discardReceivedCard(object);
				advanceTurn();
				break;
			case Constants.PLAY_EIGHT_H:
				suitChosen = Constants.SUIT_HEARTS;
				discardReceivedCard(object);
				advanceTurn();
				break;
			case Constants.PLAY_EIGHT_S:
				suitChosen = Constants.SUIT_SPADES;
				discardReceivedCard(object);
				advanceTurn();
				break;
			case Constants.DRAW_CARD:
				drawCard();
				advanceTurn();
				break;
			case Constants.REFRESH:
				refreshPlayers();
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.GameController#handleActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GameboardActivity.DISCONNECTED) {
			if (resultCode == Activity.RESULT_CANCELED) {
				// We chose to drop the player, so let the Game know to do that
				String playerId = data.getStringExtra(ConnectionConstants.KEY_DEVICE_ID);
				game.dropPlayer(playerId);

				// TODO: should the UI be updated?
				// TODO: if this was the last player, should we go to the main menu?
			} else if (resultCode == Activity.RESULT_OK) {
				// We chose to add a new player, so start the ConnectActivity
				Intent i = new Intent(gameContext, ConnectActivity.class);
				gameContext.startActivityForResult(i, CHOOSE_PLAYER);

				// Unregister the receiver so that we don't get an annoying
				// popup when we are on the activity
				gameContext.unregisterReceiver();
			}

			return true;
		} else if (requestCode == CHOOSE_PLAYER) {
			gameContext.registerReceiver();
			// TODO: update the player with the new id
		} else if (requestCode == PLAY_COMPUTER_TURN) {
			playComputerTurn();
			advanceTurn();
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.GameController#pause()
	 */
	@Override
	public void pause() {
		for (int i = 0; i < game.getNumPlayers(); i++) {
			server.write(Constants.PAUSE, null, players.get(i).getId());
		}
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.GameController#unpause()
	 */
	@Override
	public void unpause() {
		for (int i = 0; i < game.getNumPlayers(); i++) {
			server.write(Constants.UNPAUSE, null, players.get(i).getId());
		}
	}

	/**
	 * This function will be called when a players turn is over It will change
	 * whoseTurn to the next player and send them the message that it is their
	 * turn
	 */
	private void advanceTurn() {
		if (game.isGameOver(players.get(whoseTurn))) {
			declareWinner(whoseTurn);
			return;
		}

		if (Util.isDebugBuild()) {
			// for debugging to see number of cards
			Toast.makeText(
					gameContext.getApplicationContext(),
					"player "
							+ game.getPlayers().get(whoseTurn).getCards()
							.size() + " cards.", Toast.LENGTH_SHORT)
							.show();
		}

		if (whoseTurn < game.getNumPlayers() - 1) {
			whoseTurn++;
		} else {
			whoseTurn = 0;
		}

		gameContext.highlightPlayer(whoseTurn+1);
		
		Card onDiscard = game.getDiscardPileTop();
		if (onDiscard.getValue() == 7) {
			onDiscard = new Card(suitChosen, onDiscard.getValue(),
					onDiscard.getResourceId(), onDiscard.getIdNum());
		}

		if (players.get(whoseTurn).getIsComputer()) {
			Intent computerTurn = new Intent(gameContext, PlayComputerTurnActivity.class);
			gameContext.startActivityForResult(computerTurn, PLAY_COMPUTER_TURN);
		} else {
			// tell the player it is their turn.
			server.write(Constants.IS_TURN, onDiscard, players.get(whoseTurn)
					.getId());
		}
	}

	/**
	 * This will send winner and loser messages to all the players depending if
	 * they won or not
	 * 
	 * @param whoWon The player that won
	 */
	private void declareWinner(int whoWon) {
		if (Util.isDebugBuild()) {
			Toast.makeText(gameContext, "Sending winner and loser info", Toast.LENGTH_SHORT).show();
		}

		for (int i = 0; i < game.getNumPlayers(); i++) {
			if (i == whoWon) {
				server.write(Constants.WINNER, null, players.get(i).getId());
			} else {
				server.write(Constants.LOSER, null, players.get(i).getId());
			}
		}

		mySM.speak("Congratulations " + game.getPlayers().get(whoWon).getName()	+ "! You Won!");
		Intent gameResults = new Intent(gameContext, GameResultsActivity.class);
		gameResults.putExtra(GameResultsActivity.WINNER_NAME, game.getPlayers().get(whoWon).getName());
		gameContext.startActivityForResult(gameResults, DECLARE_WINNER);
		gameContext.unregisterReceiver();
	}

	/**
	 * This draws a card in the tablet game instance and sends that card to the
	 * player
	 */
	private void drawCard() {
		//play draw card sound
		mySM.drawCardSound();
		
		Card tmpCard = game.draw(players.get(whoseTurn));
		
		if (Util.isDebugBuild()) {
			gameContext.placeCard(players.get(whoseTurn).getPosition(), tmpCard);
		} else {
			//generic back of card
			gameContext.placeCard(players.get(whoseTurn).getPosition(), new Card(5, 0,	R.drawable.back_blue_1, 54));
		}
		
		server.write(Constants.CARD_DRAWN, tmpCard, players.get(whoseTurn).getId());
	}

	/**
	 * This will take in the received card and discard it
	 * @param object
	 */
	private void discardReceivedCard(String object) {
		Card tmpCard = new Card(0, 0, 0, 0);
		try {
			JSONObject obj = new JSONObject(object);
			int suit = obj.getInt(SUIT);
			int value = obj.getInt(VALUE);
			int id = obj.getInt(ID);
			tmpCard = new Card(suit, value, ct.getResourceForCardWithId(id), id);

			gameContext.removeCard(players.get(whoseTurn).getPosition());
			game.discard(players.get(whoseTurn), tmpCard);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}

		mySM.playCardSound();
		
		gameContext.placeCard(0, tmpCard);
	}

	/**
	 * This will refresh the state of all the players by sending them their
	 * cards and if it is their turn via bluetooth
	 */
	private void refreshPlayers() {
		Player pTurn = players.get(whoseTurn);
		
		JSONObject discardObj = new JSONObject();
		try {
			//send the card on the discard pile
			Card discard = game.getDiscardPileTop();
			discardObj.put(SUIT, discard.getSuit());
			discardObj.put(VALUE, discard.getValue());
			discardObj.put(RESOURCE_ID, discard.getResourceId());
			discardObj.put(ID, discard.getIdNum());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		for (Player p : players) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, p.getName() + " refreshed : " + p);
			}

			try {
				JSONArray arr = new JSONArray();
				JSONObject refreshInfo = new JSONObject();
				refreshInfo.put(Constants.TURN, pTurn.equals(p));
				refreshInfo.put(Constants.PLAYER_NAME, p.getName());
				// Maybe add more refresh info here

				arr.put(refreshInfo);
				
				//send the card on the discard pile
				arr.put(discardObj);
				
				//send all the cards in the players hand
				for (Card c : p.getCards()) {
					JSONObject obj = new JSONObject();
					obj.put(SUIT, c.getSuit());
					obj.put(VALUE, c.getValue());
					obj.put(RESOURCE_ID, c.getResourceId());
					obj.put(ID, c.getIdNum());

					arr.put(obj);
				}

				server.write(Constants.REFRESH, arr.toString(), p.getId());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	private void playComputerTurn() {
		Card onDiscard = game.getDiscardPileTop();
		if (onDiscard.getValue() == 7) {
			onDiscard = new Card(suitChosen, onDiscard.getValue(), onDiscard.getResourceId(), onDiscard.getIdNum());
		}
		List<Card> cards = players.get(whoseTurn).getCards();
		Card cardSelected = null;
		
		//computer with difficulty 0
		if(players.get(whoseTurn).getComputerDifficulty() < 0){
			for (Card c : cards) {
				if (gameRules.checkCard(c, onDiscard)) {
					cardSelected = c;
					break;
				}
			}
			if (cardSelected != null && cardSelected.getValue() == 7) {
				int[] suits = new int[4];
				int maxSuitIndex = 0;
				for (Card c : cards) {
					if (!c.equals(cardSelected) ) {
						suits[c.getSuit()]++;
						if(suits[c.getSuit()] > suits[maxSuitIndex]){
							maxSuitIndex = c.getSuit();
						}
					}
				}
				suitChosen = maxSuitIndex;
			}
		} else if (players.get(whoseTurn).getComputerDifficulty() == 1){
			
		}


		if (cardSelected != null) {
			//play sound for playing a card
			mySM.playCardSound();

			gameContext.removeCard(players.get(whoseTurn).getPosition());
			game.discard(players.get(whoseTurn), cardSelected);
			gameContext.placeCard(0, cardSelected);
		} else {
			//play sound for drawing a card
			mySM.drawCardSound();
			
			Card tmpCard = game.draw(players.get(whoseTurn));
			
			if (Util.isDebugBuild()) {
				gameContext.placeCard(players.get(whoseTurn).getPosition(), tmpCard);
			} else {
				//generic back of card
				gameContext.placeCard(players.get(whoseTurn).getPosition(), new Card(5, 0,	R.drawable.back_blue_1, 54));
			}
		}
	}
}
