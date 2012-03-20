package cs309.a1.shared;

import java.util.List;


public class Player {
	
	private List<Card> cards;
	private int numCards;
	private String name;
	private String id;
	
	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public int getNumCards() {
		return numCards;
	}

	public void setNumCards(int numCards) {
		this.numCards = numCards;
	}

	public void addCard(Card card){
		cards.add(card);
		numCards++;
	}
	
	public void removeCard(Card card){
		cards.remove(card);
		numCards--;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
