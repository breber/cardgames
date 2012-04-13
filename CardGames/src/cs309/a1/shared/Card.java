package cs309.a1.shared;

import static cs309.a1.shared.Constants.ID;
import static cs309.a1.shared.Constants.RESOURCE_ID;
import static cs309.a1.shared.Constants.SUIT;
import static cs309.a1.shared.Constants.VALUE;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * This class will be used to represent a card. Each card will have four different fields 
 * and will be identified through the use of the fields. Each card will have an image to represent
 * it and also a suit, value and id number.
 *
 */
public class Card {

	private int suit;
	private int value;
	private int resourceId;
	private int idNum;

	/**
	 * This constructor will create and make a card object based upon the given 
	 * parameters. The suit, value and resource id all must match and follow a pattern
	 * based upon the standard deck.
	 * 
	 * @param suit the suit of the new card object
	 * @param value the value of the card--0-12 based on number
	 * @param resourceId the id of the card image to be used with the card
	 * @param idNum the id number representing the card 0-53
	 */
	public Card(int suit, int value, int resourceId, int idNum) {
		this.suit = suit;
		this.value = value;
		this.resourceId = resourceId;
		this.idNum = idNum;
	}

	/**
	 * This method will return the suit of a card object
	 * 
	 * @return and integer representing the suit of the card
	 */
	public int getSuit() {
		return suit;
	}
	
	/**
	 * This method will set the suit of a given card
	 * 
	 * @param suit the new suit of the card
	 */
	public void setSuit(int suit) {
		this.suit = suit;
	}
	
	/**
	 * This method will return the value of the card object
	 * 
	 * @return an iteger value representing the card object
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * This method will set the value of the card object
	 * 
	 * @param value an integer representing the new value of the card
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * This method will return the resource id of a card object
	 * 
	 * @return an integer representing the resource id of a card object
	 */
	public int getResourceId() {
		return resourceId;
	}
	
	/**
	 * This method will set the resource id of a given card
	 * 
	 * @param resourceId the new resource id for the given card object
	 */
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	
	/**
	 * This method will return the id number of a card object
	 * 
	 * @return an integer representing the id number of a card
	 */
	public int getIdNum() {
		return idNum;
	}
	
	/**
	 * This method will set the id number of a card object
	 * 
	 * @param idNum an integer representing the new id number of the card
	 */
	public void setIdNum(int idNum) {
		this.idNum = idNum;
	}

	/**
	 * This is a custom toString method for the transferring of card data
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
