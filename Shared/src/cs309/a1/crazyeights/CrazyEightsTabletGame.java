package cs309.a1.crazyeights;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cs309.a1.shared.Card;
import cs309.a1.shared.Deck;
import cs309.a1.shared.Game;
import cs309.a1.shared.Player;
import cs309.a1.shared.Rules;

import static cs309.a1.crazyeights.Constants.NUMBER_OF_CARDS_PER_HAND;

public class CrazyEightsTabletGame implements Game{

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

	//TODO 
	//initialize function 
	// this will get all the stuff ready to play game
	//shuffle deck create cards and players etc. 
	
	/**
	 * This method will setup the game by calling shuffleDeck, deal and setting up
	 * the initial GUI state.
	 */
	public void setup(){
		//shuffle the card ID's
		this.shuffleDeck();
		
		//deal the initial cards to all the players in the game
		this.deal();
		
		//display stuff
		//discard pile first one
		//
		
	}
	
	/**
	 * This method will shuffle the deck of cards using the Collections.shuffle() method.
	 */
	public void shuffleDeck(){
		
		//create a random number generator
		Random generator = new Random();
		Collections.shuffle(shuffledDeck, generator);
		iter = shuffledDeck.iterator();
		
	}

		
	/**
	 * This method will deal the initial hand to each player. Each player will receive 
	 * a certain number of cards based on a constant.
	 */
	public void deal(){
		int numberOfPlayers = players.size();
		
		//Deal the given number of cards to each player
		//NUMBER_OF_CARDS_PER_HAND can be found in cs309.a1.crazyeights
		for(int i = 0; i < NUMBER_OF_CARDS_PER_HAND; i++){
			for(int j = 0; j < numberOfPlayers; j++){
				Player p = players.get(j);
				p.addCard(iter.next());
			}
		}
	}
	
	/**
	 * This method allows a player to discard a card object on the discard pile
	 * @param player the player who is going to make the discard
	 * @param card the card the player chooses to discard
	 */
	public void discard(Player player, Card card){
		//add the given card to the discard pile
		discardPile.add(card);
	}
	
	/**
	 * This method will return true if the player has run out of cards.
	 * @param player the player to check
	 * @return true if the player has 0 cards and false otherwise
	 */
	public boolean isGameOver(Player player){
		
		if(player.getNumCards() == 0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * This method will allow the player to draw a card from the draw pile
	 * @param player the player who chooses to draw the card
	 */
	public void draw(Player player){
		//TODO
		player.getCards().add(iter.next());
		player.setNumCards(player.getNumCards() + 1);
	}
}
