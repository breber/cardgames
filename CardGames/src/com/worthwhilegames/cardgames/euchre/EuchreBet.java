package com.worthwhilegames.cardgames.euchre;

import static com.worthwhilegames.cardgames.euchre.EuchreConstants.BET;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.GO_ALONE;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.TRUMP;

import org.json.JSONException;
import org.json.JSONObject;

public class EuchreBet {

	/**
	 * This is the suit of the trump that is being bet on.
	 */
	private int trumpSuit;

	/**
	 * if true the player wishes to place a bet, if false they pass
	 */
	private boolean placeBet;

	/**
	 * Variable if true this means the player wishes to go alone
	 */
	private boolean goAlone;

	/**
	 * Initializes a bet
	 * 
	 * @param givenTrump
	 *            the suit of the trump to be bet
	 * @param givenPlaceBet
	 *            whether or not the player wants to place a bet
	 * @param givenGoAlone
	 *            whether or not the player wants to go alone
	 */
	public EuchreBet(int givenTrump, boolean givenPlaceBet, boolean givenGoAlone) {
		trumpSuit = givenTrump;
		placeBet = givenPlaceBet;
		goAlone = givenGoAlone;
	}

	public int getTrumpSuit() {
		return trumpSuit;
	}

	public boolean getPlaceBet() {
		return placeBet;
	}

	public boolean getGoAlone() {
		return goAlone;
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
			obj.put(TRUMP, trumpSuit);
			obj.put(BET, placeBet);
			obj.put(GO_ALONE, goAlone);

			return obj;
		} catch (JSONException ex) {
			ex.printStackTrace();
			return new JSONObject();
		}
	}

	/**
	 * This is a custom toString method for the transferring of bet data
	 * 
	 * @return a JSON string representation of the card including trump suit,
	 *         whether a bet was placed and whether they want to go alone
	 */
	@Override
	public String toString() {
		return toJSONObject().toString();
	}
}
