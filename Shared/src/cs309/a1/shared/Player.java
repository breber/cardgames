package cs309.a1.shared;

import static cs309.a1.crazyeights.Constants.ID;
import static cs309.a1.crazyeights.Constants.RESOURCE_ID;
import static cs309.a1.crazyeights.Constants.SUIT;
import static cs309.a1.crazyeights.Constants.VALUE;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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

	/**
	 * 
	 */
	@Override
	public String toString() {
		// Another possible implementation without having to come up
		// with our own parsing code...see ShowCardsActivity.java for the decoding part...
		try {
			JSONArray arr = new JSONArray();
			for (Card c : cards) {
				JSONObject obj = new JSONObject();
				obj.put(SUIT, c.getSuit());
				obj.put(VALUE, c.getValue());
				obj.put(RESOURCE_ID, c.getResourceId());
				obj.put(ID, c.getIdNum());

				arr.put(obj);
			}

			return arr.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}

//		StringBuilder toReturn = new StringBuilder();
//
//		for(int i = 0; i < cards.size(); i++){
//			toReturn.append(cards.get(i).toString());
//		}
//
//		return toReturn.toString();
	}

}
