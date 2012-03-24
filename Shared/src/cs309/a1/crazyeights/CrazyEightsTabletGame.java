package cs309.a1.crazyeights;

import static cs309.a1.crazyeights.Constants.NUMBER_OF_CARDS_PER_HAND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.util.Log;
import cs309.a1.shared.Card;
import cs309.a1.shared.Deck;
import cs309.a1.shared.Game;
import cs309.a1.shared.Player;
import cs309.a1.shared.Rules;
import cs309.a1.shared.Util;

public class CrazyEightsTabletGame implements Game{
	private static final String TAG = CrazyEightsTabletGame.class.getName();

	private static CrazyEightsTabletGame instance = null;

	private List<Player> players;
	private Deck gameDeck;
	private Rules rules;

	private Iterator<Card> iter;

	private ArrayList<Card> shuffledDeck;
	private ArrayList<Card> discardPile;

	public CrazyEightsTabletGame(List<Player> players, Deck gameDeck, Rules rules) {
		super();
		this.players = players;
		this.gameDeck = gameDeck;
		this.rules = rules;
		shuffledDeck = gameDeck.getCardIDs();
		discardPile = new ArrayList<Card>();
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Deck getGameDeck() {
		return gameDeck;
	}

	public void setGameDeck(Deck gameDeck) {
		this.gameDeck = gameDeck;
	}

	public List<Card> getDiscardPile() {
		return discardPile;
	}

	public void setDiscardPile(ArrayList<Card> discardPile) {
		this.discardPile = discardPile;
	}

	public Rules getRules() {
		return rules;
	}

	public void setRules(Rules rules) {
		this.rules = rules;
	}


	public ArrayList<Card> getShuffledDeck() {
		return shuffledDeck;
	}

	public void setShuffledDeck(ArrayList<Card> shuffledDeck) {
		this.shuffledDeck = shuffledDeck;
	}


	/**
	 * Create a new instance of the tablet game so that multiple classes are able to reference
	 * the same card game and only one instance will be made available. This method uses the custom
	 * constructor made in this class.
	 * 
	 * @return an instance of CrazyEightsTabletGame
	 */
	public static CrazyEightsTabletGame getInstance(List<Player> players, Deck deck, Rules rules) {
		if (instance == null) {
			instance = new CrazyEightsTabletGame(players, deck, rules);
		}

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
	 * This method will setup the game by calling shuffleDeck, deal and setting up
	 * the initial GUI state.
	 */
	@Override
	public void setup(){
		//shuffle the card ID's
		this.shuffleDeck();

		//deal the initial cards to all the players in the game
		this.deal();

		//discard pile first one
		discardPile.add(iter.next());

		//remove the last card returned by iter.next()
		iter.remove();

		//display stuff
	}

	/**
	 * This method will shuffle the deck of cards using the Collections.shuffle() method.
	 * Upon completion the cards should be shuffled and ready to deal
	 */
	@Override
	public void shuffleDeck(){
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
	public void shuffleDiscardPile(){
		//TODO
		Card card = discardPile.remove(discardPile.size()-1);

		//Make copy of discard pile to be new shuffled deck
		//add to the shuffled deck in case there are still some cards left that are unaccounted for
		Collections.copy(shuffledDeck, discardPile);

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
	public void deal(){
		int numberOfPlayers = players.size();

		if (Util.isDebugBuild()) {
			Log.d(TAG, "deal: numberOfPlayers: " + numberOfPlayers);
		}

		//maybe error check here to make sure can deal more cards than in deck?

		//Deal the given number of cards to each player
		//NUMBER_OF_CARDS_PER_HAND can be found in cs309.a1.crazyeights
		for(int i = 0; i < NUMBER_OF_CARDS_PER_HAND; i++){
			for(int j = 0; j < numberOfPlayers; j++){
				//get a player
				Player p = players.get(j);

				//give them a card
				p.addCard(iter.next());

				//remove the last card returned by iter.next()
				iter.remove();
			}
		}

		if (Util.isDebugBuild()) {
			for (Player p : players) {
				Log.d(TAG, "deal: player[" + p.getId() + "] has " + p.getNumCards() + " cards");
				Log.d(TAG, "      player[" + p.getId() + "]: " + p);
			}
		}
	}

	/**
	 * This method allows a player to discard a card object on the discard pile
	 * @param player the player who is going to make the discard
	 * @param card the card the player chooses to discard
	 */
	@Override
	public void discard(Player player, Card card){
		//add the given card to the discard pile
		//if(rules.checkCard(card, discardPile.get(discardPile.size()-1))){
		discardPile.add(card);
		player.getCards().remove(card);
		player.setNumCards(player.getNumCards() - 1);
		//}else{
		//do not allow.
		//}
	}

	/**
	 * This method will return true if the player has run out of cards.
	 * @param player the player to check
	 * @return true if the player has 0 cards and false otherwise
	 */
	public boolean isGameOver(Player player){

		//check to see if the player has any cards left
		if(player.getNumCards() == 0){
			return true;
		}

		return false;
	}

	/**
	 * This method will allow the player to draw a card from the draw pile
	 * @param player the player who chooses to draw the card
	 */
	@Override
	public void draw(Player player){
		if(!iter.hasNext()){
			this.shuffleDiscardPile();
			//maybe refresh gui or something here
		}

		//get a card out of the shuffled pile and add to the players hand
		player.getCards().add(iter.next());

		//remove the last card returned by iter.next()
		iter.remove();

		//increase the number of cards the player has
		player.setNumCards(player.getNumCards() + 1);

		//shuffle the deck if the player drew the last card
		if(shuffledDeck.size()==0){
			shuffleDiscardPile();
		}
	}

	/**
	 * This method will remove a player from the game
	 * @param player the player to be dropped from the game
	 */
	@Override
	public void dropPlayer(Player player) {
		if(players.contains(player) && player != null){
			List<Card> cards= player.getCards();

			//player is in the list of current players
			players.remove(player);

			//add all of the players cards to the discard pile
			discardPile.addAll(cards);

			//remove all of the cards from the players hand and set the number of cards to 0
			cards.removeAll(cards);
			player.setNumCards(0);
		}
	}
}
