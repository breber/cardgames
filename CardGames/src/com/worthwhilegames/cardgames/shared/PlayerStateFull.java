package com.worthwhilegames.cardgames.shared;

import static com.worthwhilegames.cardgames.shared.Constants.KEY_CURRENT_STATE;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is the complete information for a player's state
 */
public class PlayerStateFull {

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
	 * This is the card that the player must play on
	 */
	public Card[] cardsPlayed;

	/**
	 * A list of card objects to represent the cards a player has
	 */
	public List<Card> cards;

	/*
	 * These are extra information slots for specific games
	 */

	/**
	 * The suit to display in the lower left corner
	 * 		C8 - suit on deck or suit chosen for 8 played
	 * 		Euchre - trump suit
	 */
	public int suitDisplay = 0;

	/** extraInfo1:
	 * 		C8 - none
	 * 		Euchre - trump suit
	 */
	public int extraInfo1 = 0;

	/**
	 * 
	 */
	public PlayerStateFull(){
		this.cards = new ArrayList<Card>();
		this.cardsPlayed = new Card[Constants.DEFAULT_MAX_PLAYERS];
		this.numCards = new int[Constants.DEFAULT_MAX_PLAYERS];
		this.playerNames = new String[Constants.DEFAULT_MAX_PLAYERS];
		for(int i = 0; i < Constants.DEFAULT_MAX_PLAYERS; i++){
			this.cardsPlayed[i] = Card.getNullCard();
			this.numCards[i] = 0;
			this.playerNames[i] = "";
		}
		this.currentState = 0;
		this.isTurn = false;
		this.onDiscard = Card.getNullCard();
		this.playerIndex = 0;

	}

	/**
	 * Converts the input JSON string to a new PlayerState object
	 * @param jsonIn
	 * @return new object that represents the JSON object PlayerState
	 */
	public void updateFromPartialState(String jsonIn){
		PlayerStateFull newState = this;

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

			newState.suitDisplay = refreshInfo.getInt(Constants.KEY_SUIT_DISPLAY);

			// Extra information that a game may need
			newState.extraInfo1 = refreshInfo.getInt(Constants.KEY_EXTRA_INFO_1);

			JSONObject discardObj = refreshInfo.getJSONObject(Constants.KEY_DISCARD_CARD);
			newState.onDiscard = Card.createCardFromJSON(discardObj);

			// Get the cards Played
			JSONArray cPlayedArr = refreshInfo.getJSONArray(Constants.KEY_CARDS_PLAYED);
			for (int i = 0; i < cPlayedArr.length(); i++) {
				JSONObject card = cPlayedArr.getJSONObject(i);
				newState.cardsPlayed[i] = Card.createCardFromJSON(card);
			}


			// Get the player's hand
			JSONArray arr = refreshInfo.getJSONArray(Constants.KEY_CURRENT_HAND);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject card = arr.getJSONObject(i);
				newState.cards.add(Card.createCardFromJSON(card));
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

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

			refreshInfo.put(Constants.KEY_SUIT_DISPLAY, this.suitDisplay);

			// Extra information that a game may need
			refreshInfo.put(Constants.KEY_EXTRA_INFO_1, this.extraInfo1);

			// send the card on the discard pile
			JSONObject discardObj = this.onDiscard.toJSONObject();
			refreshInfo.put(Constants.KEY_DISCARD_CARD, discardObj);

			JSONArray arrPlayedCards = new JSONArray();
			for (Card cPlayed: this.cardsPlayed) {
				if(cPlayed == null){
					arrPlayedCards.put(Card.getNullCard().toJSONObject());
				} else {
					arrPlayedCards.put(cPlayed.toJSONObject());
				}
			}
			refreshInfo.put(Constants.KEY_CARDS_PLAYED, arrPlayedCards);


			// send all the cards in the players hand
			JSONArray arr = new JSONArray();
			for (Card c : this.cards) {
				arr.put(c.toJSONObject());
			}
			refreshInfo.put(Constants.KEY_CURRENT_HAND, arr);

			jsonOut = refreshInfo.toString();
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
				newState.playerNames[i] = pName.getString(Constants.KEY_PLAYER_NAME);
			}

			newState.suitDisplay = refreshInfo.getInt(Constants.KEY_SUIT_DISPLAY);

			// Extra information that a game may need
			newState.extraInfo1 = refreshInfo.getInt(Constants.KEY_EXTRA_INFO_1);

			JSONObject discardObj = refreshInfo.getJSONObject(Constants.KEY_DISCARD_CARD);
			newState.onDiscard = Card.createCardFromJSON(discardObj);

			// Get the cards Played
			JSONArray cPlayedArr = refreshInfo.getJSONArray(Constants.KEY_CARDS_PLAYED);
			for (int i = 0; i < cPlayedArr.length(); i++) {
				JSONObject card = cPlayedArr.getJSONObject(i);
				newState.cardsPlayed[i] = Card.createCardFromJSON(card);
			}


			// Get the player's hand
			JSONArray arr = refreshInfo.getJSONArray(Constants.KEY_CURRENT_HAND);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject card = arr.getJSONObject(i);
				newState.cards.add(Card.createCardFromJSON(card));
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}


		return newState;
	}
}
