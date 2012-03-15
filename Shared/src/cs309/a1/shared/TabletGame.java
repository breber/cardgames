package cs309.a1.shared;

import java.util.List;

public class TabletGame {

	
	List<Player> Players;
	Deck gameDeck;

	public TabletGame(List<Player> players, Deck gameDeck) {
		super();
		Players = players;
		this.gameDeck = gameDeck;
	}
	
	public List<Player> getPlayers() {
		return Players;
	}
	public void setPlayers(List<Player> players) {
		Players = players;
	}
	public Deck getDeck() {
		return gameDeck;
	}
	public void setDeck(Deck deck) {
		this.gameDeck = deck;
	}
	//TODO 
	//initialize function 
	// this will get all the stuff ready to play game
	//shuffle deck create cards and players etc. 
	public void setup(){
		//gameDeck.shuffle();
		//hand out cards
		//display stuff
		//discard pile first one
		//
		
	}
		
}
