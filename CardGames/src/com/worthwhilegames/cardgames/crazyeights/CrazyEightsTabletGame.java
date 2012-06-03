package com.worthwhilegames.cardgames.crazyeights;

import static com.worthwhilegames.cardgames.crazyeights.C8Constants.NUMBER_OF_CARDS_PER_HAND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.Deck;
import com.worthwhilegames.cardgames.shared.Game;
import com.worthwhilegames.cardgames.shared.Player;
import com.worthwhilegames.cardgames.shared.Rules;
import com.worthwhilegames.cardgames.shared.Util;

import android.util.Log;

/**
 * A class for keeping track of the logic and game state for the game type crazy eights
 */
public class CrazyEightsTabletGame implements Game {

	/**
	 * A tag for the class name
	 */
	private static final String TAG = CrazyEightsTabletGame.class.getName();

	/**
	 * A variable for and instance of the crazy eights game type
	 */
	private static CrazyEightsTabletGame instance = null;

	/**
	 * A private variable for a list of players in the current game
	 */
	private List<Player> players;

	/**
	 * A private variable representing the game deck for the crazy eights game
	 */
	private Deck gameDeck;

	/**
	 * A private variable for the rules interface for the crazy eights game
	 */
	private Rules rules;

	/**
	 * A private variable to represent the difficulty of computers in the current game
	 */
	private String computerDifficulty = Constants.EASY;

	/**
	 * An iterator for removing cards from the shuffled deck
	 */
	private Iterator<Card> iter;

	/**
	 * A list of all the cards in the shuffle deck
	 */
	private ArrayList<Card> shuffledDeck;

	/**
	 * A list of all the cards in the discard pile
	 */
	private ArrayList<Card> discardPile;

	/**
	 * Create a new instance of the tablet game so that multiple classes are able to reference
	 * the same card game and only one instance will be made available. This method uses the custom
	 * constructor made in this class.
	 * 
	 * @return an instance of CrazyEightsTabletGame
	 */
	public static CrazyEightsTabletGame getInstance(List<Player> players, Deck deck, Rules rules) {
		instance = new CrazyEightsTabletGame(players, deck, rules);

		return instance;
	}

	/**
	 * Create a new instance of the tablet game so that multiple classes are able to reference
	 * the same card game and only one instance will be made available. This method uses the default
	 * constructor.
	 * 
	 * @return an instance of CrazyEightsTabletGame
	 */
	public static CrazyEightsTabletGame getInstance() {
		if (instance == null) {
			throw new IllegalArgumentException();
		}

		return instance;
	}

	/**
	 * A constructor for the crazy eights game type. This constructor will initialize the all the variables
	 * for a game of crazy eights including the rules, players, deck, shuffled deck pile and the discard pile.
	 * 
	 * @param players a list of Player objects in the current game
	 * @param gameDeck a deck of cards to be used in the game crazy eights
	 * @param rules a Rules object for the crazy eights game
	 */
	public CrazyEightsTabletGame(List<Player> players, Deck gameDeck, Rules rules) {
		this.players = players;
		this.gameDeck = gameDeck;
		this.rules = rules;
		shuffledDeck = gameDeck.getCardIDs();
		discardPile = new ArrayList<Card>();
	}

	/**
	 * This method is a getter for the players list
	 * 
	 * @return a list of Player objects in the game
	 */
	@Override
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * This method is a setter for the list of players in the current game
	 * 
	 * @param players a list of players that will be playing the game
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	/**
	 * This method is a getter for the deck object
	 * 
	 * @return a Deck object for the game type of crazy eights
	 */
	public Deck getGameDeck() {
		return gameDeck;
	}

	/**
	 * This method is a setter for the game deck to be used with the game
	 * 
	 * @param gameDeck a Deck object to represent the cards in the crazy eights game
	 */
	public void setGameDeck(Deck gameDeck) {
		this.gameDeck = gameDeck;
	}

	/**
	 * This method is a getter for the discard pile list
	 * 
	 * @return a list of Card objects in the discard pile
	 */
	public List<Card> getDiscardPile() {
		return discardPile;
	}

	/**
	 * This method is a setter for the current discard pile
	 * 
	 * @param discardPile a list of Card objects currently in the discard pile
	 */
	public void setDiscardPile(ArrayList<Card> discardPile) {
		this.discardPile = discardPile;
	}

	/**
	 * This method is a getter for the current game's Rules object
	 * 
	 * @return a Rules object to represent the current rules of the game
	 */
	public Rules getRules() {
		return rules;
	}

	/**
	 * This method is a setter for the Rules object
	 * 
	 * @param rules a Rules object from the rules interface to be used with the current game
	 */
	public void setRules(Rules rules) {
		this.rules = rules;
	}

	/**
	 * This method is a getter for the shuffled deck pile
	 * 
	 * @return a list of Card objects representing the shuffled deck
	 */
	public ArrayList<Card> getShuffledDeck() {
		return shuffledDeck;
	}

	/**
	 * This method is a setter for the shuffled deck
	 * 
	 * @param shuffledDeck an array list of cards to represent the shuffled deck
	 */
	public void setShuffledDeck(ArrayList<Card> shuffledDeck) {
		this.shuffledDeck = shuffledDeck;
	}

	/**
	 * This method is a setter for the computer difficulty
	 * 
	 * @param diff the difficulty of the computers for the current game
	 */
	@Override
	public void setComputerDifficulty(String diff){
		this.computerDifficulty = diff;
	}

	/**
	 * This method is a getter for the computer difficulty variable
	 * 
	 * @return a String representing the difficulty of the computers in the game
	 */
	@Override
	public String getComputerDifficulty(){
		return this.computerDifficulty;
	}

	/**
	 * This method will setup the game by calling shuffleDeck, deal and setting up
	 * the initial GUI state.
	 */
	@Override
	public void setup() {
		//shuffle the card ID's
		this.shuffleDeck();

		//deal the initial cards to all the players in the game
		this.deal();

		//discard pile first one
		discardPile.add(iter.next());

		//remove the last card returned by iter.next()
		iter.remove();
	}

	/**
	 * This method will shuffle the deck of cards using the Collections.shuffle() method.
	 * Upon completion the cards should be shuffled and ready to deal
	 */
	@Override
	public void shuffleDeck() {
		//create a random number generator
		Random generator = new Random();

		//shuffle the deck
		Collections.shuffle(shuffledDeck, generator);

		//set the iterator to go through the shuffled deck
		iter = shuffledDeck.iterator();
	}

	/**
	 * This method will shuffle the discard pile and replace the current draw
	 * pile with the old discard pile. After this method call the shuffled deck
	 * will only have the top card remaining.
	 */
	public void shuffleDiscardPile() {
		Card card = discardPile.remove(discardPile.size() - 1);

		//Make copy of discard pile to be new shuffled deck
		//add to the shuffled deck in case there are still some cards left that are unaccounted for
		shuffledDeck.addAll(discardPile);

		if (Util.isDebugBuild()) {
			Log.d(TAG, "shuffleDiscardPile: shuffledDeck: " + shuffledDeck.size() + " - discardPile: " + discardPile.size());
		}

		//remove all the cards from the discard pile
		discardPile.removeAll(discardPile);

		//place the last card discarded back on the discard pile
		discardPile.add(card);

		//shuffle the deck
		this.shuffleDeck();
	}

	/**
	 * This method will deal the initial hand to each player. Each player will receive
	 * a certain number of cards based on a constant.
	 */
	@Override
	public void deal() {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "deal: numberOfPlayers: " + players.size());
		}

		if (Util.isDebugBuild()) {
			for (Player p : players) {
				Log.d(TAG, "pre deal: player[" + p.getId() + "] has " + p.getNumCards() + " cards");
				Log.d(TAG, "          player[" + p.getId() + "]: " + p);
			}
		}

		//maybe error check here to make sure can deal more cards than in deck?

		//Deal the given number of cards to each player
		//NUMBER_OF_CARDS_PER_HAND can be found in cs309.a1.crazyeights
		for (int i = 0; i < NUMBER_OF_CARDS_PER_HAND; i++) {
			for (Player p : players) {
				//give them a card
				p.addCard(iter.next());

				if (Util.isDebugBuild()) {
					Log.d(TAG, "p.addCard: player[" + p.getId() + "] has " + p.getNumCards() + " cards");
				}

				//remove the last card returned by iter.next()
				iter.remove();
			}
		}

		if (Util.isDebugBuild()) {
			for (Player p : players) {
				Log.d(TAG, "postdeal: player[" + p.getId() + "] has " + p.getNumCards() + " cards");
				Log.d(TAG, "          player[" + p.getId() + "]: " + p);
			}
		}
	}

	/**
	 * This method allows a player to discard a card object on the discard pile
	 * 
	 * @param player the player who is going to make the discard
	 * @param card the card the player chooses to discard
	 */
	@Override
	public void discard(Player player, Card card) {
		discardPile.add(card);
		player.removeCard(card);
	}

	/**
	 * This method will return true if the player has run out of cards.
	 * 
	 * @param player the player to check
	 * @return true if the player has 0 cards and false otherwise
	 */
	@Override
	public boolean isGameOver(Player player) {
		//check to see if the player has any cards left
		if (player.getNumCards() == 0) {
			return true;
		}

		return false;
	}

	/**
	 * This method will allow the player to draw a card from the draw pile
	 * 
	 * @param player the player who chooses to draw the card
	 * @return a Card object that has been drawn from the draw pile
	 */
	@Override
	public Card draw(Player player) {
		if (!iter.hasNext()) {
			this.shuffleDiscardPile();
			//maybe refresh gui or something here
		}

		if (!iter.hasNext()) return null;

		Card card = iter.next();
		//get a card out of the shuffled pile and add to the players hand
		player.addCard(card);

		//remove the last card returned by iter.next()
		iter.remove();

		//shuffle the deck if the player drew the last card
		if (shuffledDeck.isEmpty()) {
			shuffleDiscardPile();
		}

		return card;
	}

	/**
	 * This method will remove a player from the game
	 * 
	 * @param player the id of the player to be dropped from the game
	 */
	@Override
	public void dropPlayer(String playerMacAddress) {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "dropPlayer: " + playerMacAddress);
		}

		Player p = null;

		for (Player player : players) {
			if (player.getId().equals(playerMacAddress)) {
				p = player;
				break;
			}
		}

		if (p != null) {
			p.setIsComputer(true);
			p.setComputerDifficulty(computerDifficulty);
		} else {
			if (Util.isDebugBuild()) {
				Log.d(TAG, "dropPlayer: couldn't find player with id: " + playerMacAddress);
			}
		}
	}

	/**
	 * This method will return the last card added to the discard pile
	 * 
	 * @return a Card object representing the last card added to the discard pile
	 */
	@Override
	public Card getDiscardPileTop() {
		return discardPile.get(discardPile.size() - 1);
	}

	/**
	 * This method will return the number of players in the present game
	 * 
	 * @return an integer representing the number of players
	 */
	@Override
	public int getNumPlayers() {
		return players.size();
	}
}
