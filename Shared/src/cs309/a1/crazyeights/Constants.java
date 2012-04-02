package cs309.a1.crazyeights;

public class Constants {
	
	public static final int NUMBER_OF_CARDS_PER_HAND = 5;
	
	public static final int SUIT_CLUBS = 0;
	public static final int SUIT_DIAMONDS = 1;
	public static final int SUIT_HEARTS = 2;
	public static final int SUIT_SPADES = 3;
	public static final int SUIT_JOKER = 4;
	
	public static final String SUIT = "suit";
	public static final String VALUE = "value";
	public static final String RESOURCE_ID = "resourceid";
	public static final String ID = "id";
	public static final String MESSAGE_TYPE = "messagetype";
	public static final String TURN = "isturn";
	
	public static final int GET_PLAYER_NAME = "getPlayerName".hashCode();
	
	//these are bluetooth message types that can be sent by the GameBoard
	public static final int SETUP = 0;
	public static final int IS_TURN = 1;
	public static final int CARD_DRAWN = 2;
	public static final int WINNER = 3;
	public static final int LOSER = 4;
	
	public static final int REFRESH = 5;
	
	//these are bluetooth messages types that can be sent by Player
	public static final int PLAY_CARD = 6;
	public static final int DRAW_CARD = 7;
	public static final int PLAY_EIGHT_C = 8;
	public static final int PLAY_EIGHT_D = 9;
	public static final int PLAY_EIGHT_H = 10;
	public static final int PLAY_EIGHT_S = 11;
	public static final String PLAYER_NAME = "playername";
	
	
	

}
