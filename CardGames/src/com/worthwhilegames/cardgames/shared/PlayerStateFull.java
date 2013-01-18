package com.worthwhilegames.cardgames.shared;

import static com.worthwhilegames.cardgames.shared.Constants.KEY_CURRENT_STATE;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is the complete information for a player's state
 */
public class PlayerStateFull {

	/**
	 * This must be set up for the specific game being played
	 */
	public static CardTranslator ct;

	/**
	 * Number of cards that each player has
	 */
	public int[] numCards;

	/**
	 * index of the player receiving this object
	 */
	public int playerIndex;

	/**
	 * Is it the players turn?
	 */
	public boolean isTurn = false;

	/**
	 * the current state of the game
	 * Example: first round of betting, playing lead card, player's turn ...
	 */
	public int currentState;

	/**
	 * List of player names for players playing the game
	 */
	public String[] playerNames;

	/**
	 * This is the card that the player must play on
	 */
	public Card onDiscard;

	/**
	 * A list of card objects to represent the cards a player has
	 */
	private List<Card> cards;

	/*
	 * These are extra information slots for specific games
	 */

	/** extraInfo1:
	 * 		C8 - none
	 * 		Euchre - trump suit
	 */
	public int extraInfo1 = 0;

	/** extraInfo2:
	 * 		C8 - none
	 * 		Euchre - none
	 */
	public int extraInfo2 = 0;

	/**
	 * Takes the current PlayerState object and converts it into a JSON string
	 * @return JSON string representation of this object
	 */
	public String toJSONObject(){
		String jsonOut = "";

		try {
			// Create the base refresh info object
			JSONObject refreshInfo = new JSONObject();

			// send the number of cards for each player
			JSONArray arrCardCount = new JSONArray();
			for (int numCards: this.numCards) {
				JSONObject numCardsobj = new JSONObject();
				numCardsobj.put(Constants.KEY_NUM_CARDS, numCards);
				arrCardCount.put(numCardsobj);
			}
			refreshInfo.put(Constants.KEY_NUM_CARDS_ARRAY, arrCardCount);

			// send isTurn, player index, and current state
			refreshInfo.put(Constants.KEY_TURN, this.isTurn);
			refreshInfo.put(Constants.KEY_PLAYER_INDEX, this.playerIndex);
			refreshInfo.put(Constants.KEY_CURRENT_STATE, this.currentState);

			// send the name for each player
			JSONArray arrPlayerNames = new JSONArray();
			for (String pName: this.playerNames) {
				JSONObject name = new JSONObject();
				name.put(Constants.KEY_PLAYER_NAME, pName);
				arrPlayerNames.put(name);
			}
			refreshInfo.put(Constants.KEY_PLAYER_NAMES, arrPlayerNames);


			// Extra information that a game may need
			refreshInfo.put(Constants.KEY_EXTRA_INFO_1, this.extraInfo1);
			refreshInfo.put(Constants.KEY_EXTRA_INFO_2, this.extraInfo2);

			// send the card on the discard pile
			JSONObject discardObj = this.onDiscard.toJSONObject();
			refreshInfo.put(Constants.KEY_DISCARD_CARD, discardObj);

			// send all the cards in the players hand
			JSONArray arr = new JSONArray();
			for (Card c : this.cards) {
				arr.put(c.toJSONObject());
			}
			refreshInfo.put(Constants.KEY_CURRENT_HAND, arr);

		} catch (JSONException e) {
			e.printStackTrace();
		}



		return jsonOut;
	}

	/**
	 * Converts the input JSON string to a new PlayerState object
	 * @param jsonIn
	 * @return new object that represents the JSON object PlayerState
	 */
	public static PlayerStateFull createStateFromJSON(String jsonIn){
		PlayerStateFull newState = new PlayerStateFull();

		try {
			JSONObject refreshInfo = new JSONObject(jsonIn);

			JSONArray arrCardCount = refreshInfo.getJSONArray(Constants.KEY_NUM_CARDS_ARRAY);
			newState.numCards = new int[arrCardCount.length()];
			for (int i = 0; i < arrCardCount.length(); i++) {
				JSONObject count = arrCardCount.getJSONObject(i);
				newState.numCards[i] = count.getInt(Constants.KEY_NUM_CARDS);
			}

			newState.playerIndex = refreshInfo.getInt(Constants.KEY_PLAYER_INDEX);
			newState.isTurn = refreshInfo.getBoolean(Constants.KEY_TURN);
			newState.currentState = refreshInfo.getInt(KEY_CURRENT_STATE);

			// Player Names to be displayed
			JSONArray arrPlayerNames = refreshInfo.getJSONArray(Constants.KEY_PLAYER_NAMES);
			newState.playerNames = new String[arrPlayerNames.length()];
			for (int i = 0; i < arrPlayerNames.length(); i++) {
				JSONObject pName = arrPlayerNames.getJSONObject(i);
				newState.playerNames[0] = pName.getString(Constants.KEY_PLAYER_NAME);
			}

			// Extra information that a game may need
			newState.extraInfo1 = refreshInfo.getInt(Constants.KEY_EXTRA_INFO_1);
			newState.extraInfo2 = refreshInfo.getInt(Constants.KEY_EXTRA_INFO_2);

			JSONObject discardObj = refreshInfo.getJSONObject(Constants.KEY_DISCARD_CARD);
			int suit = discardObj.getInt(Constants.KEY_SUIT);
			int value = discardObj.getInt(Constants.KEY_VALUE);
			int id = discardObj.getInt(Constants.KEY_CARD_ID);
			newState.onDiscard = new Card(suit, value, ct.getResourceForCardWithId(id), id);

			// Get the player's hand
			JSONArray arr = refreshInfo.getJSONArray(Constants.KEY_CURRENT_HAND);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject card = arr.getJSONObject(i);
				suit = card.getInt(Constants.KEY_SUIT);
				value = card.getInt(Constants.KEY_VALUE);
				id = card.getInt(Constants.KEY_CARD_ID);
				newState.cards.add(new Card(suit, value, ct.getResourceForCardWithId(id), id));
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}


		return newState;
	}
}
