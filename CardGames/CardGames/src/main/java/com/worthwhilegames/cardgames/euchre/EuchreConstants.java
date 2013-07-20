package com.worthwhilegames.cardgames.euchre;

/**
 * This has constants that are specific to Euchre
 */
public class EuchreConstants {

	// TODO: update message type constants to start at 2000

	/**
	 * The maximum number of players allowed
	 */
	public static final int MAX_NUM_PLAYERS = 4;

	/**
	 * The number of cards in a euchre game
	 */
	public static final int EUCHRE_NUM_CARDS = 5;

	/* JSON constants for EuchreBet */
	/**
	 * JSON message code for placing a bet in EuchreBet
	 */
	public static final String BET = "bet";

	/**
	 * JSON message code for go alone in EuchreBet
	 */
	public static final String GO_ALONE = "goalone";

	/**
	 * JSON message code for trump suit declared in EuchreBet
	 */
	public static final String TRUMP = "trump";

	/**
	 * message code for betting round
	 */
	public static final String BET_ROUND = "betround";

	/**
	 * This is the score that a team must get to win the game
	 */
	public static final int EUCHRE_SCORE_LIMIT = 10;

	/* All Game specific bluetooth message constants will be >= 30  */
	/**
	 * Message type for telling a player to bet in the first round
	 */
	public static final int FIRST_ROUND_BETTING = 30;

	/**
	 * Message type for telling a player to bet in the second round
	 */
	public static final int SECOND_ROUND_BETTING = 31;

	/**
	 * Message type for asking a player to lead a trick
	 */
	public static final int LEAD_TRICK = 32;

	/**
	 * Message type for telling the dealer to take this card and keep it in his hand or discard it.
	 */
	public static final int PICK_IT_UP = 33;

	/**
	 * Message type for a player playing the first card of a trick
	 */
	public static final int PLAY_LEAD_CARD = 34;

	/**
	 * Message type for a player playing the first card of a trick
	 */
	public static final int ROUND_OVER = 36;

	/**
	 * Message type for the gameboard sending a player the trump suit
	 */
	public static final int TRUMP_SUIT = 37;

	/**
	 * A constant to tell that the suit value of a jack has been changed
	 */
	public static final int CHANGED_JACK_SUIT_LEFT = 99;

	/**
	 * A constant for the adjusted ace value
	 */
	public static final int ADJUSTED_ACE_VALUE = 14;

	/**
	 * A constant for the adjusted L Jack value
	 */
	public static final int ADJUSTED_L_VALUE = 15;

	/**
	 * A constant for the adjusted R Jack value
	 */
	public static final int ADJUSTED_R_VALUE = 16;

	/* Betting Computer Logic Constants */

	/**
	 * Multiplier for trump card values when analyzing a computer's turn
	 */
	public static final int TRUMP_CARD_VALUE_FACTOR = 2;

	public static final int EASY_COMP_R1_BET_THRESHOLD = 77;

	public static final int EASY_COMP_R2_BET_THRESHOLD = 76;

	public static final int EASY_COMP_GO_ALONE_THRESHOLD = 20;

	public static final int EASY_COMP_DEALER_TEAM_ADVANTAGE = 0;

	public static final int MEDIUM_COMP_R1_BET_THRESHOLD = 80;

	public static final int MEDIUM_COMP_R2_BET_THRESHOLD = 80;

	public static final int MEDIUM_COMP_GO_ALONE_THRESHOLD = 25;

	public static final int MEDIUM_COMP_DEALER_TEAM_ADVANTAGE = 5;

	public static final int HARD_COMP_R1_BET_THRESHOLD = 80;

	public static final int HARD_COMP_R2_BET_THRESHOLD = 78;

	public static final int HARD_COMP_GO_ALONE_THRESHOLD = 30;

	public static final int HARD_COMP_DEALER_TEAM_ADVANTAGE = 7;



	/**
	 * These are some random terms
	 * 
	 * Trick - when each player has played a card then the trick is over.
	 * Round - a series of 5 tricks that allow a team to score
	 * Match - all rounds until a team scores
	 * up card - the card that is bet on during the first round of betting
	 * Trump - the suit that is higher than any card of any other suit
	 * left bower - the second highest card in the round ( the Jack of the
	 * 				The jack of the same colored suit as trump but not the trump suit
	 * right bower - The highest card in the round. The jack of Trump suit
	 */

}
