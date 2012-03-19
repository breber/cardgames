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
	private Card[] deck;
	private ArrayList<Card> shuffledDeck;
	private Iterator<Card> iter;
	private ArrayList<Card> discardPile;
	private Rules rules;

	public CrazyEightsTabletGame(List<Player> players, Deck gameDeck) {
		super();
		this.players = players;
		this.gameDeck = gameDeck;
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
	public void setup(){
		//shuffle the card ID's
		this.shuffleDeck();
		
		//deal the initial cards to all the players in the game
		this.deal();
		
		//display stuff
		//discard pile first one
		//
		
	}
	
	public void shuffleDeck(){
		int i = 0;
		int card;
		int card2;
		
		Random generator = new Random();
		while(i<10000){
			card = generator.nextInt();
			card2 = generator.nextInt();
			card = card%52;
			card2 = card2%52;
			
			swap(deck, card, card2);
			
		}
		
		shuffledDeck = new ArrayList<Card>(Arrays.asList(deck));
		iter = shuffledDeck.iterator();
		
	}
	
	private void swap(Card[] deck, int card, int card2){
		Card temp;
		
		//swap
		temp = deck[card];
		deck[card] = deck[card2];
		deck[card2] = temp;
	}
		
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
	
	public void discard(Player player, Card card){
		//add the given card to the discard pile
		discardPile.add(card);
		
		//check to see if the player won
		if(player.getCards().size() == 0){
			//player won
			//TODO
		}
	}
	
	public void draw(Player player){
		//TODO
		player.getCards().add(iter.next());
		player.setNumCards(player.getNumCards() + 1);
	}
}
