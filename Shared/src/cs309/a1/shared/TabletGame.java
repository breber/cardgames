package cs309.a1.shared;

import java.util.List;

public class TabletGame {

	private List<Player> players;
	private Deck gameDeck;
	private List<Card> discardPile;
	private CrazyEightGameRules rules;

	public TabletGame(List<Player> players, Deck gameDeck) {
		super();
		this.players = players;
		this.gameDeck = gameDeck;
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

	public void setDiscardPile(List<Card> discardPile) {
		this.discardPile = discardPile;
	}

	public CrazyEightGameRules getRules() {
		return rules;
	}

	public void setRules(CrazyEightGameRules rules) {
		this.rules = rules;
	}

	//TODO 
	//initialize function 
	// this will get all the stuff ready to play game
	//shuffle deck create cards and players etc. 
	public void setup(){
		//gameDeck.shuffle();
		
		//deal the initial cards to all the players in the game
		this.deal();
		
		//display stuff
		//discard pile first one
		//
		
	}
	
	public void shuffleDeck(Deck gameDeck){
		
		
	}
		
	public void deal(){
		int numberOfPlayers = players.size();
		
		//TODO: replace hard coded value with constant NUMBER_OF_CARDS_PER_HAND
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < numberOfPlayers; j++){
				Player p = players.get(j);
				List<Card> cards = p.getCards();
				//TODO
				//p.addCard(cards, gameDeck.getTopCard());
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
		//player.getCards().add(gameDeck.getTopCard());
		player.setNumCards(player.getNumCards() + 1);
	}
}
