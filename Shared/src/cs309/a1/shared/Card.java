package cs309.a1.shared;

import static cs309.a1.crazyeights.Constants.ID;
import static cs309.a1.crazyeights.Constants.RESOURCE_ID;
import static cs309.a1.crazyeights.Constants.SUIT;
import static cs309.a1.crazyeights.Constants.VALUE;

import org.json.JSONException;
import org.json.JSONObject;


public class Card {

	private int suit;
	private int value;
	private int resourceId;
	private int idNum;

	public Card(int suit, int value, int resourceId, int idNum) {
		this.suit = suit;
		this.value = value;
		this.resourceId = resourceId;
		this.idNum = idNum;
	}

	public int getSuit() {
		return suit;
	}
	public void setSuit(int suit) {
		this.suit = suit;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getResourceId() {
		return resourceId;
	}
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	public int getIdNum() {
		return idNum;
	}
	public void setIdNum(int idNum) {
		this.idNum = idNum;
	}

	/**
	 * This is a custom toString method for the transfering of card data
	 * 
	 * @return a JSON string representation of the card including suit, value, resourceId, idNum
	 */
	@Override
	public String toString(){
		// Encode the cards into a JSONArray
		try {
			JSONObject obj = new JSONObject();
			obj.put(SUIT, getSuit());
			obj.put(VALUE, getValue());
			obj.put(RESOURCE_ID, getResourceId());
			obj.put(ID, getIdNum());

			return obj.toString();
		} catch (JSONException ex) {
			ex.printStackTrace();
			return "";
		}
	}
}
