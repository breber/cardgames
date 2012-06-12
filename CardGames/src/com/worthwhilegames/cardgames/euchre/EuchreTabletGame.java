package com.worthwhilegames.cardgames.euchre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.Deck;
import com.worthwhilegames.cardgames.shared.Game;
import com.worthwhilegames.cardgames.shared.Player;
import com.worthwhilegames.cardgames.shared.Rules;
import com.worthwhilegames.cardgames.shared.Util;

import static com.worthwhilegames.cardgames.shared.Constants.SUIT_CLUBS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_DIAMONDS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_HEARTS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_SPADES;
import static com.worthwhilegames.cardgames.shared.Constants.JACK_VALUE;

public class EuchreTabletGame implements Game{
	
	/**
	 * A tag for the class name
	 */
	private static final String TAG = EuchreTabletGame.class.getName();

	/**
	 * A variable for and instance of the euchre game type
	 */
	private static EuchreTabletGame instance = null;

	/**
	 * A private variable for a list of players in the current game
	 */
	private List<Player> players;

	/**
	 * A private variable representing the game deck for the euchre game
	 */
	private Deck gameDeck;

	/**
	 * A private variable for the rules interface for the euchre game
	 */
	private Rules rules;

	/**
	 * A private variable to represent the difficulty of computers in the current game
	 */
	private String computerDifficulty = Constants.EASY;
	
	/**
	 * An integer to represent the trump suit
	 */
	private int trump;
	
	/**
	 * An integer to represent the player who picked up the trump card or named the suit
	 */
	private int playerCalledTrump;
	
	/**
	 * An integer to represent the player who is the dealer
	 */
	private int dealer;

	/**
	 * An iterator for removing cards from the shuffled deck
	 */
	private Iterator<Card> iter;

	/**
	 * A list of all the cards in the shuffle deck
	 */
	private ArrayList<Card> shuffledDeck;
	
	/**
	 * A card to represent the first card turned over for players to bet on
	 */
	private Card topCard;


	/**
	 * Create a new instance of the tablet game so that multiple classes are able to reference
	 * the same card game and only one instance will be made available. This method uses the custom
	 * constructor made in this class.
	 * 
	 * @return an instance of EuchreTabletGame
	 */
	public static EuchreTabletGame getInstance(List<Player> players, Deck deck, Rules rules) {
		instance = new EuchreTabletGame(players, deck, rules);

		return instance;
	}

	/**
	 * Create a new instance of the tablet game so that multiple classes are able to reference
	 * the same card game and only one instance will be made available. This method uses the default
	 * constructor.
	 * 
	 * @return an instance of EuchreTabletGame
	 */
	public static EuchreTabletGame getInstance() {
		if (instance == null) {
			throw new IllegalArgumentException();
		}

		return instance;
	}

	
	/**
	 * A constructor for the crazy eights game type. This constructor will initialize the all the variables
	 * for a game of euchre including the rules, players, deck, shuffled deck pile and the discard pile.
	 * 
	 * @param players a list of Player objects in the current game
	 * @param gameDeck a deck of cards to be used in the game euchre
	 * @param rules a Rules object for the euchre game
	 */
	public EuchreTabletGame(List<Player> players, Deck gameDeck, Rules rules) {
		this.players = players;
		this.gameDeck = gameDeck;
		this.rules = rules;
		shuffledDeck = gameDeck.getCardIDs();
	}
	
	public EuchreTabletGame() {

	}
	
	@Override
	public void setup() {
		
		this.shuffleDeck();
		
		this.deal();
		
	}

	@Override
	public void deal() {
		for(int i = 0; i < 2; i++){
			for (Player p : players) {
				if(i == 0){
					p.addCard(iter.next());
					iter.remove();
					p.addCard(iter.next());
					iter.remove();
					p.addCard(iter.next());
					iter.remove();
				}else{
					p.addCard(iter.next());
					iter.remove();
					p.addCard(iter.next());
					iter.remove();
				}
				
			}
		}
		
		topCard = iter.next();
		iter.remove();
		trump = topCard.getSuit();
		
	}

	@Override
	public Card draw(Player player) {
		//No drawing in this game
		return null;
	}

	@Override
	public void discard(Player player, Card card) {
		player.getCards().remove(card);
	}

	@Override
	public void shuffleDeck() {
		//create a random number generator
		Random generator = new Random();

		//shuffle the deck
		Collections.shuffle(shuffledDeck, generator);
		
		//set the iterator to go through the shuffled deck
		iter = shuffledDeck.iterator();
		
	}
	
	public void pickItUp(Player player){
		playerCalledTrump = player.getPosition();
		
		//start the game
		//.
		//.
		//.
	}

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

	@Override
	public Card getDiscardPileTop() {
		//no discard pile
		return null;
	}

	@Override
	public int getNumPlayers() {
		return players.size();
	}

	@Override
	public boolean isGameOver(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Player> getPlayers() {
		return players;
	}

	@Override
	public ArrayList<Card> getShuffledDeck() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setComputerDifficulty(String diff) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getComputerDifficulty() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int determineTrickWinner(List<Card> cards, int suitLed){
		
		Card winningCard = cards.get(0);
		adjustCards(winningCard);
		
		for(int i = 1; i < cards.size(); i++){
			Card card = cards.get(i);
			adjustCards(card);
			winningCard = compareCards(winningCard, card, suitLed);	
		}
		
		return winningCard.getIdNum();
	}
	
	public Card compareCards(Card card, Card card2, int suitLed){
		
		if(card.getSuit() == trump && card2.getSuit() != trump){
			return card;
		}else if(card.getSuit() != trump && card2.getSuit() == trump){
			return card2;
		}else if(card.getSuit() == suitLed && card2.getSuit() != suitLed){
			return card;
		}else if(card.getSuit() != suitLed && card2.getSuit() == suitLed){
			return card2;
		}else{
			if(card.getValue() >= card2.getValue()){
				return card;
			}else{
				return card2;
			}
		}
		
	}
	
public void adjustCards(Card card){
					
		switch(trump){
			case SUIT_CLUBS:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_SPADES){
					card.setSuit(SUIT_CLUBS);
					card.setValue(15);
				}else if(card.getValue() == JACK_VALUE&& card.getSuit() == SUIT_CLUBS){
					card.setValue(16);
				}
				break;
				
			case SUIT_DIAMONDS:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_HEARTS){
					card.setSuit(SUIT_DIAMONDS);
					card.setValue(15);
				}else if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_DIAMONDS){
					card.setValue(16);
				}
				break;
			
			case SUIT_HEARTS:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_DIAMONDS){
					card.setSuit(SUIT_HEARTS);
					card.setValue(15);
				}else if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_HEARTS){
					card.setValue(16);
				}
				break;
			
			case SUIT_SPADES:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_CLUBS){
					card.setSuit(SUIT_SPADES);
					card.setValue(15);
				}else if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_SPADES){
					card.setValue(16);
				}
				break;
		}
		
		if(card.getValue() == 0){
			card.setValue(14);
		}
		
	}

	public Deck getGameDeck() {
		return gameDeck;
	}

	public void setGameDeck(Deck gameDeck) {
		this.gameDeck = gameDeck;
	}

	public Rules getRules() {
		return rules;
	}

	public void setRules(Rules rules) {
		this.rules = rules;
	}

	public int getTrump() {
		return trump;
	}

	public void setTrump(int trump) {
		this.trump = trump;
	}

	public Iterator<Card> getIter() {
		return iter;
	}

	public void setIter(Iterator<Card> iter) {
		this.iter = iter;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public void setShuffledDeck(ArrayList<Card> shuffledDeck) {
		this.shuffledDeck = shuffledDeck;
	}

	public int getPlayerCalledTrump() {
		return playerCalledTrump;
	}

	public void setPlayerCalledTrump(int playerCalledTrump) {
		this.playerCalledTrump = playerCalledTrump;
	}

	public int getDealer() {
		return dealer;
	}

	public void setDealer(int dealer) {
		this.dealer = dealer;
	}

}
