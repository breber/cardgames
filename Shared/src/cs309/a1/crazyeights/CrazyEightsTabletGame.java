package cs309.a1.crazyeights;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	private Card[] deck;
	private Iterator<Card> iter;
	
	private ArrayList<Card> shuffledDeck;
	private ArrayList<Card> discardPile;
	
	public CrazyEightsTabletGame(List<Player> players, Deck gameDeck, Rules rules) {
		super();
		this.players = players;
		this.gameDeck = gameDeck;
		this.rules = rules;
		deck = new Card[gameDeck.getCardIDs().size()];
		gameDeck.getCardIDs().toArray(deck);
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
	 * This method will shuffle the deck of cards. This method will swap array positions
	 * a 1000 times to shuffle the cards.
	 */
	public void shuffleDeck(){
		
		//local variables that contain two array indicies
		int i = 0;
		int card;
		int card2;
		
		//create a random number generator
		Random generator = new Random();
		while(i<10000){
			
			//generate two random numbers of array positions to swap
			card = generator.nextInt();
			card2 = generator.nextInt();
			card = card%52;
			card2 = card2%52;
			
			//call swap
			swap(deck, card, card2);
			
		}
		
		//create a list of shuffled cards
		shuffledDeck = new ArrayList<Card>(Arrays.asList(deck));
		iter = shuffledDeck.iterator();
		
	}
	
	/**
	 * This method will swap two array locations
	 * 
	 * @param deck array of card objects from which to swap
	 * @param card the first location in the array to swap
	 * @param card2 the second location to swap
	 */
	private void swap(Card[] deck, int card, int card2){
		Card temp;
		
		//swap
		temp = deck[card];
		deck[card] = deck[card2];
		deck[card2] = temp;
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
		
		//check to see if the player won
		if(player.getCards().size() == 0){
			//player won
			//TODO
		}
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
