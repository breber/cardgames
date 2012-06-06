package com.worthwhilegames.cardgames.shared;

import java.util.ArrayList;
import java.util.List;

/**
 * The interface that will be implemented for each game this
 * application supports. It provides methods for starting a
 * game, drawing cards, shuffling etc.
 */
public interface Game {

	/**
	 * This method will be used to set up the given game to it's initial state
	 */
	public void setup();

	/**
	 * This method deals cards to the players involved in the current game
	 */
	public void deal();

	/**
	 * This method is used to add a card to a players hand from the non-used deck
	 * 
	 * @param player the player that needs another card
	 * @return the card that was added to the players hand
	 */
	public Card draw(Player player);

	/**
	 * This method is used to discard a pile to the discard pile
	 * 
	 * @param player the player that is making the discard
	 * @param card the card that is to be added to the discard pile
	 */
	public void discard(Player player, Card card);

	/**
	 * This method will shuffle the current deck of cards
	 */
	public void shuffleDeck();

	/**
	 * This method will remove a player from the current list of players in the game
	 * 
	 * @param player the id of the player that is to be removed from the current game
	 */
	public void dropPlayer(String playerMacAddress);

	/**
	 * This method will return the card last discarded
	 * 
	 * @return a Card object that represents the top of the discard pile
	 */
	public Card getDiscardPileTop();

	/**
	 * This method will return the number of players involved in the current game
	 * 
	 * @return and integer representation of the number of players in the current game
	 */
	public int getNumPlayers();

	/**
	 * The maximum number of players to allow to connect
	 * 
	 * @return the maximum number of players to allow to connect
	 */
	public int getMaxNumPlayers();

	/**
	 * This method will tell if the game is over for a given player
	 * 
	 * @param player the player to check
	 * @return true if the game is over for the player false otherwise
	 */
	public boolean isGameOver(Player player);

	/**
	 * This method will return a list of Player objects that are currently involved in the game
	 * 
	 * @return a List of type Player
	 */
	public List<Player> getPlayers();

	/**
	 * This method is a getter for the shuffled deck pile
	 * 
	 * @return a list of Card objects representing the shuffled deck
	 */
	public ArrayList<Card> getShuffledDeck();

	/**
	 * set the default computer difficulty
	 * @param diff computer difficulty
	 */
	public void setComputerDifficulty(String diff);

	/**
	 * get the default computer difficulty
	 * @return the computer difficulty
	 */
	public String getComputerDifficulty();

}
