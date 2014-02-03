package com.worthwhilegames.cardgames.shared;

import com.worthwhilegames.cardgames.R;
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
     * The unique card id, 0 - 53
     */
    private int idNum;

    /**
     * this is a JSON key for the suit of a card
     */
    public static final String KEY_SUIT = "suit";

    /**
     * this is a JSON key for the value of a card
     */
    public static final String KEY_VALUE = "value";

    /**
     * this is a JSON key for a card id
     */
    public static final String KEY_CARD_ID = "id";

    /**
     * This constructor will create and make an invalid card
     */
    public Card() {
        this.suit = -1;
        this.value = -1;
        this.idNum = -1;
    }

    /**
     * This constructor will create and make a card object based upon the given
     * parameters. The suit, value and resource id all must match and follow a pattern
     * based upon the standard deck.
     *
     * @param suit the suit of the new card object
     * @param value the value of the card--0-12 based on number
     * @param idNum the id number representing the card 0-53
     */
    public Card(int suit, int value, int idNum) {
        this.suit = suit;
        this.value = value;
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
        this.idNum = c.idNum;
    }

    public Card(JSONObject obj) {
        try {
            suit = obj.getInt(KEY_SUIT);
            value = obj.getInt(KEY_VALUE);
            idNum = obj.getInt(KEY_CARD_ID);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
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
        if (idNum == 0) {
            return R.drawable.clubs_a;
        } else if (idNum == 1) {
            return R.drawable.clubs_2;
        } else if (idNum == 2) {
            return R.drawable.clubs_3;
        } else if (idNum == 3) {
            return R.drawable.clubs_4;
        } else if (idNum == 4) {
            return R.drawable.clubs_5;
        } else if (idNum == 5) {
            return R.drawable.clubs_6;
        } else if (idNum == 6) {
            return R.drawable.clubs_7;
        } else if (idNum == 7) {
            return R.drawable.clubs_8;
        } else if (idNum == 8) {
            return R.drawable.clubs_9;
        } else if (idNum == 9) {
            return R.drawable.clubs_10;
        } else if (idNum == 10) {
            return R.drawable.clubs_j;
        } else if (idNum == 11) {
            return R.drawable.clubs_q;
        } else if (idNum == 12) {
            return R.drawable.clubs_k;
        } else if (idNum == 13) {
            return R.drawable.diamonds_a;
        } else if (idNum == 14) {
            return R.drawable.diamonds_2;
        } else if (idNum == 15) {
            return R.drawable.diamonds_3;
        } else if (idNum == 16) {
            return R.drawable.diamonds_4;
        } else if (idNum == 17) {
            return R.drawable.diamonds_5;
        } else if (idNum == 18) {
            return R.drawable.diamonds_6;
        } else if (idNum == 19) {
            return R.drawable.diamonds_7;
        } else if (idNum == 20) {
            return R.drawable.diamonds_8;
        } else if (idNum == 21) {
            return R.drawable.diamonds_9;
        } else if (idNum == 22) {
            return R.drawable.diamonds_10;
        } else if (idNum == 23) {
            return R.drawable.diamonds_j;
        } else if (idNum == 24) {
            return R.drawable.diamonds_q;
        } else if (idNum == 25) {
            return R.drawable.diamonds_k;
        } else if (idNum == 26) {
            return R.drawable.hearts_a;
        } else if (idNum == 27) {
            return R.drawable.hearts_2;
        } else if (idNum == 28) {
            return R.drawable.hearts_3;
        } else if (idNum == 29) {
            return R.drawable.hearts_4;
        } else if (idNum == 30) {
            return R.drawable.hearts_5;
        } else if (idNum == 31) {
            return R.drawable.hearts_6;
        } else if (idNum == 32) {
            return R.drawable.hearts_7;
        } else if (idNum == 33) {
            return R.drawable.hearts_8;
        } else if (idNum == 34) {
            return R.drawable.hearts_9;
        } else if (idNum == 35) {
            return R.drawable.hearts_10;
        } else if (idNum == 36) {
            return R.drawable.hearts_j;
        } else if (idNum == 37) {
            return R.drawable.hearts_q;
        } else if (idNum == 38) {
            return R.drawable.hearts_k;
        } else if (idNum == 39) {
            return R.drawable.spades_a;
        } else if (idNum == 40) {
            return R.drawable.spades_2;
        } else if (idNum == 41) {
            return R.drawable.spades_3;
        } else if (idNum == 42) {
            return R.drawable.spades_4;
        } else if (idNum == 43) {
            return R.drawable.spades_5;
        } else if (idNum == 44) {
            return R.drawable.spades_6;
        } else if (idNum == 45) {
            return R.drawable.spades_7;
        } else if (idNum == 46) {
            return R.drawable.spades_8;
        } else if (idNum == 47) {
            return R.drawable.spades_9;
        } else if (idNum == 48) {
            return R.drawable.spades_10;
        } else if (idNum == 49) {
            return R.drawable.spades_j;
        } else if (idNum == 50) {
            return R.drawable.spades_q;
        } else if (idNum == 51) {
            return R.drawable.spades_k;
        } else if (idNum == 52) {
            return R.drawable.joker_b;
        } else if (idNum == 53) {
            return R.drawable.joker_r;
        }

        return R.drawable.back_blue_1;
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
