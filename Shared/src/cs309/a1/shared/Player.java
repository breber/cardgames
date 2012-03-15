package cs309.a1.shared;

import java.util.List;

public class Player {
	
	List<Card> Cards;
	int numCards;
	
	public List<Card> getCards() {
		return Cards;
	}
	public void setCards(List<Card> cards) {
		Cards = cards;
	}
	public int getNumCards() {
		return numCards;
	}
	public void setNumCards(int numCards) {
		this.numCards = numCards;
	}
	
}
