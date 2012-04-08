package cs309.a1.crazyeights;

public class Constants {

	public static final int NUMBER_OF_CARDS_PER_HAND = 5;

	public static final int SUIT_CLUBS = 0;
	public static final int SUIT_DIAMONDS = 1;
	public static final int SUIT_HEARTS = 2;
	public static final int SUIT_SPADES = 3;
	public static final int SUIT_JOKER = 4;

	public static final String PLAYER_1 = "player1";
	public static final String PLAYER_2 = "player2";
	public static final String PLAYER_3 = "player3";
	public static final String PLAYER_4 = "player4";

	public static final String SUIT = "suit";
	public static final String VALUE = "value";
	public static final String RESOURCE_ID = "resourceid";
	public static final String ID = "id";
	public static final String MESSAGE_TYPE = "messagetype";
	public static final String TURN = "isturn";

	public static final int GET_PLAYER_NAME = "getPlayerName".hashCode();

	// these are bluetooth message types that can be sent by the GameBoard
	public static final int SETUP = 0;
	public static final int IS_TURN = 1;
	public static final int CARD_DRAWN = 2;
	public static final int WINNER = 3;
	public static final int LOSER = 4;

	public static final int REFRESH = 5;
	public static final int PAUSE = 6;
	public static final int UNPAUSE = 7;

	// these are bluetooth messages types that can be sent by Player
	public static final int PLAY_CARD = 8;
	public static final int DRAW_CARD = 9;
	public static final int PLAY_EIGHT_C = 10;
	public static final int PLAY_EIGHT_D = 11;
	public static final int PLAY_EIGHT_H = 12;
	public static final int PLAY_EIGHT_S = 13;

	public static final String PLAYER_NAME = "playername";

	/**
	 * the time you wait in between computer turns
	 */
	public static final long COMPUTER_WAIT_TIME = 1500;

}
