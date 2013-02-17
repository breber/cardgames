package com.worthwhilegames.cardgames.shared;

import java.util.List;

/**
 * The interface that will be implemented for each game this
 * application supports. It provides methods for starting a
 * game, drawing cards, shuffling etc.
 */
public abstract class Game {

	/**
	 * This will be 0 to 3 to indicate the spot in the players array for the
	 * player currently taking their turn
	 */
	public int whoseTurn = 0;

	/**
	 * This is the current state of the game
	 * Corresponds to the first round of betting second round of betting
	 * trick leading etc constants in euchre constants and shared constants
	 */
	public int currentState;

	/**
	 * A private variable for a list of players in the current game
	 */
	protected List<Player> players;

	/**
	 * A private variable representing the game deck for the euchre game
	 */
	protected Deck gameDeck;

	/**
	 * A private variable to represent the difficulty of computers in the current game
	 */
	protected String computerDifficulty = Constants.EASY;

	/**
	 * This method will be used to set up the given game to it's initial state
	 */
	public abstract void setup();

	/**
	 * This method deals cards to the players involved in the current game
	 */
	public abstract void deal();

	/**
	 * This method is used to add a card to a players hand from the non-used deck
	 * 
	 * @param player the player that needs another card
	 * @return the card that was added to the players hand
	 */
	public abstract Card draw(Player player);

	/**
	 * This method is used to discard a pile to the discard pile
	 * 
	 * @param player the player that is making the discard
	 * @param card the card that is to be added to the discard pile
	 */
	public abstract void discard(Player player, Card card);

	/**
	 * This method will shuffle the current deck of cards
	 */
	public abstract void shuffleDeck();

	/**
	 * This method will remove a player from the current list of players in the game
	 * 
	 * @param player the id of the player that is to be removed from the current game
	 */
	public abstract void dropPlayer(String playerMacAddress);

	/**
	 * This method will return the card last discarded
	 * 
	 * @return a Card object that represents the top of the discard pile
	 */
	public abstract Card getDiscardPileTop();

	/**
	 * This method will return the card at the position requested
	 * @param position position of the card to get
	 * can be 1 through 4 for the four positions around the game board
	 * 
	 * @return card to be displayed at the position
	 */
	public abstract Card getCardAtPosition(int position);

	/**
	 * This method will return the number of players involved in the current game
	 * 
	 * @return and integer representation of the number of players in the current game
	 */
	public abstract int getNumPlayers();

	/**
	 * The maximum number of players to allow to connect
	 * 
	 * @return the maximum number of players to allow to connect
	 */
	public abstract int getMaxNumPlayers();

	/**
	 * This method will tell if the game is over for a given player
	 * 
	 * @param player the player to check
	 * @return true if the game is over for the player false otherwise
	 */
	public abstract boolean isGameOver(Player player);

	/**
	 * This method will return a list of Player objects that are currently involved in the game
	 * 
	 * @return a List of type Player
	 */
	public abstract List<Player> getPlayers();

	/**
	 * This method is a getter for the shuffled deck pile
	 * 
	 * @return a list of Card objects representing the shuffled deck
	 */
	public abstract List<Card> getShuffledDeck();

	/**
	 * set the default computer difficulty
	 * 
	 * @param diff computer difficulty
	 */
	public abstract void setComputerDifficulty(String diff);

	/**
	 * get the default computer difficulty
	 * 
	 * @return the computer difficulty
	 */
	public abstract String getComputerDifficulty();

	/**
	 * Add a player to the game
	 * 
	 * @param p the player to add
	 */
	public abstract void addPlayer(Player p);

	/**
	 * Returns whether the game has been started or not
	 * 
	 * @return whether a game is currently active
	 */
	public abstract boolean isActive();

	/**
	 * Returns the player state for the given index of the player
	 * @param index
	 * @return player state at given index
	 */
	public abstract PlayerState getPlayerState(int index);

}
