package com.worthwhilegames.cardgames.shared;

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
     * This method will return the card last discarded
     *
     * @return a Card object that represents the top of the discard pile
     */
    public Card getDiscardPileTop();

    /**
     * This method will return the card at the position requested
     * @param position position of the card to get
     * can be 1 through 4 for the four positions around the game board
     *
     * @return card to be displayed at the position
     */
    public Card getCardAtPosition(int position);

    /**
     * This method will return the number of players involved in the current game
     *
     * @return and integer representation of the number of players in the current game
     */
    public int getNumPlayers();

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
     * Get the player who is on this device
     */
    public Player getSelf();

    /**
     * Checks if it is the current players turn
     */
    public boolean isMyTurn();

    /**
     * Get the rules
     */
    public IRules getRules();

    /**
     * This method is a getter for the shuffled deck pile
     *
     * @return a list of Card objects representing the shuffled deck
     */
    public List<Card> getShuffledDeck();

    /**
     * Add a player to the game
     *
     * @param p the player to add
     */
    public void addPlayer(Player p);

    /**
     * Returns whether the game has been started or not
     *
     * @return whether a game is currently active
     */
    public boolean isActive();

    /**
     * Persist the game state to a byte array
     *
     * @return the game state as a byte array
     */
    public byte[] persist();

    /**
     * Load the game state from a byte array
     *
     * @param state the game state
     * @return if successful
     */
    public boolean load(byte[] state);

}
