package cs309.a1.shared;

import java.util.ArrayList;
import java.util.List;


public class Player {

	private List<Card> cards;
	private int numCards;
	private String name;
	private String id;

	public Player(List<Card> cards, int numCards, String name, String id) {
		super();
		this.cards = cards;
		this.numCards = numCards;
		this.name = name;
		this.id = id;
	}

	public Player() {
		super();
		this.cards = new ArrayList<Card>();
		this.numCards = 0;
		this.name = null;
		this.id = null;
	}

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

	@Override
	public String toString() {
		// Another possible implementation without having to come up
		// with our own parsing code...see ShowCardsActivity.java for the decoding part...
		//		try {
		//			JSONArray arr = new JSONArray();
		//			for (Card c : cards) {
		//				JSONObject obj = new JSONObject();
		//				obj.put("suit", c.getSuit());
		//				obj.put("value", c.getValue());
		//				obj.put("resourceId", c.getResourceId());
		//				obj.put("id", c.getIdNum());
		//
		//				arr.put(obj);
		//			}
		//
		//			return arr.toString();
		//		} catch (JSONException e) {
		//			e.printStackTrace();
		//			return "";
		//		}

		StringBuilder toReturn = new StringBuilder();

		for(int i = 0; i < cards.size(); i++){
			toReturn.append(cards.get(i).toString());
		}

		return toReturn.toString();
	}

}
