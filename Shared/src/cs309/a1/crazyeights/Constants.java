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
	public static final String PLAYER_NAME = "playername";

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
	public static final int END_GAME = 8;

	// these are bluetooth messages types that can be sent by Player
	public static final int PLAY_CARD = 9;
	public static final int DRAW_CARD = 10;
	public static final int PLAY_EIGHT_C = 11;
	public static final int PLAY_EIGHT_D = 12;
	public static final int PLAY_EIGHT_H = 13;
	public static final int PLAY_EIGHT_S = 14;
	
	public static final String LANGUAGE_US = "US"; 
	public static final String LANGUAGE_GERMAN = "GERMAN"; 
	public static final String LANGUAGE_FRANCE = "FRANCE"; 
	public static final String LANGUAGE_CANADA = "CANADA"; 
	
	public static final String PREFERENCES = "PREFERENCES"; 
	public static final String DIFFICULTY_OF_COMPUTERS = "DIFFICULTY OF COMPUTERS"; 
	public static final String SOUND_EFFECTS = "SOUND EFFECTS"; 
	public static final String SPEECH_VOLUME = "SPEECH VOLUME"; 
	public static final String LANGUAGE = "LANGUAGE"; 
	public static final String NUMBER_OF_COMPUTERS = "NUMBER OF COMPUTERS";
	
	/**
	 * the time you wait in between computer turns
	 */
	public static final long COMPUTER_WAIT_TIME = 1500;

}
