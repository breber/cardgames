package com.worthwhilegames.cardgames.shared;

import static com.worthwhilegames.cardgames.shared.Constants.KEY_CARD_ID;
import static com.worthwhilegames.cardgames.shared.Constants.KEY_VALUE;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class will be used to represent a card. Each card will have four different fields
 * and will be identified through the use of the fields. Each card will have an image to represent
 * it and also a suit, value and id number.
 */
public class Card implements Comparable<Card> {
	/**
	 * The suit of this card
	 */
	private int suit;

	/**
	 * The value of the card, 0 - 12
	 */
	private int value;

	/**
	 * The resource id used to display as an image
	 */
	private int resourceId;

	/**
	 * The unique card id, 0 - 53
	 */
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
	 * Copy Constructor
	 * 
	 * @param c
	 */
	public Card(Card c) {
		this.suit = c.suit;
		this.value = c.value;
		this.resourceId = c.resourceId;
		this.idNum = c.idNum;
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
	 * @return an integer value representing the card object
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
	 * Generate a JSONObject based on the necessary fields of this class.
	 * 
	 * @return a JSONObject representation of this instance
	 */
	public JSONObject toJSONObject() {
		// Encode the cards into a JSONArray
		try {
			JSONObject obj = new JSONObject();

			obj.put(KEY_VALUE, getValue());
			obj.put(KEY_CARD_ID, getIdNum());

			return obj;
		} catch (JSONException ex) {
			ex.printStackTrace();
			return new JSONObject();
		}
	}

	/**
	 * This is a custom toString method for the transferring of card data
	 * 
	 * @return a JSON string representation of the card including suit, value, resourceId, idNum
	 */
	@Override
	public String toString() {
		return toJSONObject().toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Card another) {
		return this.idNum - another.idNum;
	}
}
