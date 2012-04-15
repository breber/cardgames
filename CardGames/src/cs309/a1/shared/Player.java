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

/**
 * This class will be used to represent a player. Each player will have a list of cards, a name,
 * an id for easy identification in the game, a position, whether or not they are a computer
 * and a difficulty if they are a computer.
 */
public class Player {

	/**
	 * A list of card objects to represent the cards a player has
	 */
	private List<Card> cards;

	/**
	 * A string to store the name of the user
	 */
	private String name;

	/**
	 * A string to store the id of the user which will be represented by a MAC Address
	 */
	private String id;

	/**
	 * The position of the current user on the gameboard
	 */
	private int position;

	/**
	 * A boolean to set whether or not this player is a computer
	 */
	private boolean isComputer = false;

	/**
	 * A variable to set the difficulty of the player if they are a computer
	 */
	private String computerDifficulty = Constants.EASY;

	/**
	 * A default player constructor. A fields will be set with getters and setters
	 */
	public Player() {
		this.cards = new ArrayList<Card>();
		this.name = null;
		this.id = null;
	}

	/**
	 * A method to get a list of cards that a player currently has
	 * 
	 * @return a list of card objects that a player has in their hand
	 */
	public List<Card> getCards() {
		return cards;
	}

	/**
	 * A method to get the number of cards that a player has
	 * 
	 * @return an integer representing the number of cards in a players hand
	 */
	public int getNumCards() {
		return cards.size();
	}

	/**
	 * A method to add a card to a players hand
	 * 
	 * @param card the card to be added to the list of cards a player has
	 */
	public void addCard(Card card) {
		cards.add(card);
	}

	/**
	 * A method to get a players computer status
	 * 
	 * @return true if the current player is a computer, false otherwise
	 */
	public boolean getIsComputer() {
		return this.isComputer;
	}

	/**
	 * A method to set if a player is a computer or not
	 * 
	 * @param isComp true if the player is a computer, false otherwise
	 */
	public void setIsComputer(boolean isComp) {
		isComputer = isComp;
	}

	/**
	 * A method to get the difficulty of a computer player
	 * 
	 * @return an String representing easy, medium or hard
	 */
	public String getComputerDifficulty() {
		return computerDifficulty;
	}

	/**
	 * A method to set the difficulty of a computer player
	 * 
	 * @param dif an String representing easy, medium or hard
	 */
	public void setComputerDifficulty(String dif) {
		computerDifficulty = dif;
	}

	/**
	 * This method will remove a card from a players hand by object matching
	 * using a loop that checks every card in the players hand
	 * 
	 * @param card
	 *            the card to be removed from the players hand
	 */
	public void removeCard(Card card) {
		for (Card c : cards) {
			if (c.getIdNum() == card.getIdNum()) {
				cards.remove(c);
				return;
			}
		}
	}

	/**
	 * A method to return the name of a player
	 * 
	 * @return a string representing the name of the player
	 */
	public String getName() {
		return name;
	}

	/**
	 * A method to set the name of a player object
	 * 
	 * @param name the new name of the player
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * This method will return the MAC Address of a player
	 * 
	 * @return a string representing the MAC Address of a player
	 */
	public String getId() {
		return id;
	}

	/**
	 * This method will set the id of a player
	 * 
	 * @param id a string representing the new ID of a player
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * This method will get the current position of the player based on the game board
	 * 
	 * @return an integer to represent the player's location on the game board
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * This method will set the position of the player on the game board
	 * 
	 * @param position an integer representing the new player position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * This toString() method will override the default toString() method. This
	 * will return a JSON object that is in the form of a string which will be
	 * easy for decoding
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
