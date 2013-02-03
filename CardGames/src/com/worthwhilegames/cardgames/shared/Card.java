package com.worthwhilegames.cardgames.shared;

import static com.worthwhilegames.cardgames.shared.Constants.KEY_CARD_ID;
import static com.worthwhilegames.cardgames.shared.Constants.KEY_SUIT;
import static com.worthwhilegames.cardgames.shared.Constants.KEY_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.NULL_CARD_VALUE;

import org.json.JSONException;
import org.json.JSONObject;

import com.worthwhilegames.cardgames.R;

/**
 * This class will be used to represent a card. Each card will have four different fields
 * and will be identified through the use of the fields. Each card will have an image to represent
 * it and also a suit, value and id number.
 */
public class Card implements Comparable<Card> {

	/**
	 * This is a shared pointer to a null card
	 */
	private static Card nullCard = new Card(NULL_CARD_VALUE, NULL_CARD_VALUE, NULL_CARD_VALUE, NULL_CARD_VALUE);

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

			obj.put(KEY_SUIT, getSuit());
			obj.put(KEY_VALUE, getValue());
			obj.put(KEY_CARD_ID, getIdNum());

			return obj;
		} catch (JSONException ex) {
			ex.printStackTrace();
			return new JSONObject();
		}
	}

	/**
	 * Generate a Card based on the JSON object input
	 * 
	 * @return a Card representation of this JSON object
	 */
	public static Card createCardFromJSON(JSONObject jsonIn) {
		// Decode the JSON object into a Card
		try {
			int suit = jsonIn.getInt(KEY_SUIT);
			int value = jsonIn.getInt(KEY_VALUE);
			int id = jsonIn.getInt(KEY_CARD_ID);
			return new Card(suit, value, getResourceForCardWithId(id), id);
		} catch (JSONException ex) {
			ex.printStackTrace();
			// TODO: this is a major error what should we do here?
			return null;
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

	public static Card getNullCard(){
		if(nullCard == null){
			nullCard = new Card(NULL_CARD_VALUE, NULL_CARD_VALUE, NULL_CARD_VALUE, NULL_CARD_VALUE);
		}
		return nullCard;
	}

	public boolean isNullCard(){
		return this.idNum == NULL_CARD_VALUE;
	}

	/**
	 * Get the appropriate resource id for the card with the
	 * given id.
	 * 
	 * @param id the id of the card
	 * @return the resource id to display
	 */
	public static int getResourceForCardWithId(int cardId) {
		if (cardId == 0) {
			return R.drawable.clubs_a;
		} else if (cardId == 1) {
			return R.drawable.clubs_2;
		} else if (cardId == 2) {
			return R.drawable.clubs_3;
		} else if (cardId == 3) {
			return R.drawable.clubs_4;
		} else if (cardId == 4) {
			return R.drawable.clubs_5;
		} else if (cardId == 5) {
			return R.drawable.clubs_6;
		} else if (cardId == 6) {
			return R.drawable.clubs_7;
		} else if (cardId == 7) {
			return R.drawable.clubs_8;
		} else if (cardId == 8) {
			return R.drawable.clubs_9;
		} else if (cardId == 9) {
			return R.drawable.clubs_10;
		} else if (cardId == 10) {
			return R.drawable.clubs_j;
		} else if (cardId == 11) {
			return R.drawable.clubs_q;
		} else if (cardId == 12) {
			return R.drawable.clubs_k;
		} else if (cardId == 13) {
			return R.drawable.diamonds_a;
		} else if (cardId == 14) {
			return R.drawable.diamonds_2;
		} else if (cardId == 15) {
			return R.drawable.diamonds_3;
		} else if (cardId == 16) {
			return R.drawable.diamonds_4;
		} else if (cardId == 17) {
			return R.drawable.diamonds_5;
		} else if (cardId == 18) {
			return R.drawable.diamonds_6;
		} else if (cardId == 19) {
			return R.drawable.diamonds_7;
		} else if (cardId == 20) {
			return R.drawable.diamonds_8;
		} else if (cardId == 21) {
			return R.drawable.diamonds_9;
		} else if (cardId == 22) {
			return R.drawable.diamonds_10;
		} else if (cardId == 23) {
			return R.drawable.diamonds_j;
		} else if (cardId == 24) {
			return R.drawable.diamonds_q;
		} else if (cardId == 25) {
			return R.drawable.diamonds_k;
		} else if (cardId == 26) {
			return R.drawable.hearts_a;
		} else if (cardId == 27) {
			return R.drawable.hearts_2;
		} else if (cardId == 28) {
			return R.drawable.hearts_3;
		} else if (cardId == 29) {
			return R.drawable.hearts_4;
		} else if (cardId == 30) {
			return R.drawable.hearts_5;
		} else if (cardId == 31) {
			return R.drawable.hearts_6;
		} else if (cardId == 32) {
			return R.drawable.hearts_7;
		} else if (cardId == 33) {
			return R.drawable.hearts_8;
		} else if (cardId == 34) {
			return R.drawable.hearts_9;
		} else if (cardId == 35) {
			return R.drawable.hearts_10;
		} else if (cardId == 36) {
			return R.drawable.hearts_j;
		} else if (cardId == 37) {
			return R.drawable.hearts_q;
		} else if (cardId == 38) {
			return R.drawable.hearts_k;
		} else if (cardId == 39) {
			return R.drawable.spades_a;
		} else if (cardId == 40) {
			return R.drawable.spades_2;
		} else if (cardId == 41) {
			return R.drawable.spades_3;
		} else if (cardId == 42) {
			return R.drawable.spades_4;
		} else if (cardId == 43) {
			return R.drawable.spades_5;
		} else if (cardId == 44) {
			return R.drawable.spades_6;
		} else if (cardId == 45) {
			return R.drawable.spades_7;
		} else if (cardId == 46) {
			return R.drawable.spades_8;
		} else if (cardId == 47) {
			return R.drawable.spades_9;
		} else if (cardId == 48) {
			return R.drawable.spades_10;
		} else if (cardId == 49) {
			return R.drawable.spades_j;
		} else if (cardId == 50) {
			return R.drawable.spades_q;
		} else if (cardId == 51) {
			return R.drawable.spades_k;
		} else if (cardId == 52) {
			return R.drawable.joker_b;
		} else if (cardId == 53) {
			return R.drawable.joker_r;
		} else if (cardId == NULL_CARD_VALUE){
			return R.drawable.back_blue_1;
		}

		return 0;
	}

}
