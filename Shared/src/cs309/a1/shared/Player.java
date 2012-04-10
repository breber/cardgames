package cs309.a1.shared;

import static cs309.a1.shared.Constants.ID;
import static cs309.a1.shared.Constants.RESOURCE_ID;
import static cs309.a1.shared.Constants.SUIT;
import static cs309.a1.shared.Constants.VALUE;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Player {

	private List<Card> cards;
	private String name;
	private String id;
	private int position;
	private boolean isComputer = false;
	private int computerDifficulty = 0;

	public Player() {
		this.cards = new ArrayList<Card>();
		this.name = null;
		this.id = null;
	}

	public List<Card> getCards() {
		return cards;
	}

	public int getNumCards() {
		return cards.size();
	}

	public void addCard(Card card){
		cards.add(card);
	}
	
	public boolean getIsComputer(){
		return this.isComputer;
	}
	
	public void setIsComputer(boolean isComp){
		isComputer = isComp;
	}
	
	public int getComputerDifficulty(){
		return computerDifficulty;
	}

	public void setComputerDifficulty(int dif){
		computerDifficulty = dif;
	}
	
	/**
	 * This method will remove a card from a players hand by object matching using a loop
	 * that checks every card in the players hand
	 * 
	 * @param card the card to be removed from the players hand
	 */
	public void removeCard(Card card){
		for(Card c : cards){
			if(c.getIdNum() == card.getIdNum()){
				cards.remove(c);
				return;
			}
		}
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
	
	public int getPosition(){
		return position;
	}
	
	public void setPosition(int position){
		this.position = position;
	}

	/**
	 * This toString() method will override the default toString() method. This will return a 
	 * JSON object that is in the form of a string which will be easy for decoding
	 * 
	 * @return a string representation of a player object
	 */
	@Override
	public String toString() {
		// Encode the cards into a JSONArray
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
	}
}
