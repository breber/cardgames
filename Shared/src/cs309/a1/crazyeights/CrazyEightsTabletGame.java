package cs309.a1.crazyeights;

import static cs309.a1.crazyeights.Constants.NUMBER_OF_CARDS_PER_HAND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cs309.a1.shared.Card;
import cs309.a1.shared.Deck;
import cs309.a1.shared.Game;
import cs309.a1.shared.Player;
import cs309.a1.shared.Rules;

public class CrazyEightsTabletGame implements Game{

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

	public static CrazyEightsTabletGame getInstance(List<Player> players, Deck deck, Rules rules) {
		if (instance == null) {
			instance = new CrazyEightsTabletGame(players, deck, rules);
		}

		return instance;
	}
	
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
	public void setup(){
		//shuffle the card ID's
		this.shuffleDeck();
		
		//deal the initial cards to all the players in the game
		this.deal();
		
		//discard pile first one
		discardPile.add(iter.next());
		
		//display stuff		
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
	 * 
	 */
	public void shuffleDiscardPile(){
		//TODO
		Card card = discardPile.remove(discardPile.size()-1);
		//Make copy of discard pile to be new shuffled deck
		shuffledDeck = (ArrayList<Card>) discardPile.clone();
		discardPile.removeAll(discardPile);
		discardPile.add(card);
		this.shuffleDeck();
	}

		
	/**
	 * This method will deal the initial hand to each player. Each player will receive 
	 * a certain number of cards based on a constant.
	 */
	public void deal(){
		int numberOfPlayers = players.size();
		
		//maybe error check here to make sure can deal more cards than in deck?
		
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
		if(rules.checkCard(card, discardPile.get(discardPile.size()-1))){
			discardPile.add(card);			
		}else{
			//do not allow.
		}
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
		if(!iter.hasNext()){
			this.shuffleDiscardPile();
			//maybe refresh gui or something here
		}
		player.getCards().add(iter.next());			
		player.setNumCards(player.getNumCards() + 1);
		if(shuffledDeck.size()==0){
			shuffleDiscardPile();
		}
	}

	/**
	 * 
	 */
	public void dropPlayer(Player player) {
		if(players.contains(player) && player != null){
			//player is in the list of current players
			players.remove(player);
			discardPile.addAll(player.getCards());
		}
	}
}
