package com.worthwhilegames.cardgames.shared;


/**
 * This class will be used to represent constants in a common join_game. Each of the
 * constants below are classified by a section of the join_game in which they will be
 * used.
 */
public class Constants {

    /**
     * A constant to limit the number of characters for a players name
     */
    public static final int NAME_MAX_CHARS = 10;

    /**
     * The maximum number of cards to be displayed on longest sides of tablet
     */
    public static final int MAX_DISPLAYED = 12;

    /**
     * The maximum number of cards to be displayed on shortest sides of tablet
     */
    public static final int MAX_DIS_SIDES = 6;

    /**
     * A constant to represent the name of the font to use
     */
    public static final String FONT_NAME = "hammersmithone.ttf";

    /**
     * A constant to represent the Clubs suit
     */
    public static final int SUIT_CLUBS = 0;

    /**
     * A constant to represent the Diamonds suit
     */
    public static final int SUIT_DIAMONDS = 1;

    /**
     * A constant to represent the Spades suit
     */
    public static final int SUIT_SPADES = 3;

    /**
     * A constant to represent the Hearts suit
     */
    public static final int SUIT_HEARTS = 2;

    /**
     * A constant to represent the jokers
     */
    public static final int SUIT_JOKER = 4;

    /**
     * A constant for the ace card value
     */
    public static final int ACE_VALUE = 0;

    /**
     * A constant for the two card value
     */
    public static final int TWO_VALUE = 1;

    /**
     * A constant for the three card value
     */
    public static final int THREE_VALUE = 2;

    /**
     * A constant for the four card value
     */
    public static final int FOUR_VALUE = 3;

    /**
     * A constant for the five card value
     */
    public static final int FIVE_VALUE = 4;

    /**
     * A constant for the six card value
     */
    public static final int SIX_VALUE = 5;

    /**
     * A constant for the seven card value
     */
    public static final int SEVEN_VALUE = 6;

    /**
     * A constant for the eight card value
     */
    public static final int EIGHT_VALUE = 7;

    /**
     * A constant for the nine card value
     */
    public static final int NINE_VALUE = 8;

    /**
     * A constant for the ten card value
     */
    public static final int TEN_VALUE = 9;

    /**
     * A constant for the jack card value
     */
    public static final int JACK_VALUE = 10;

    /**
     * A constant for the queen card value
     */
    public static final int QUEEN_VALUE = 11;

    /**
     * A constant for the king card value
     */
    public static final int KING_VALUE = 12;

    /**
     * A constant for the king card value
     */
    public static final int BLACK_JOKER_VALUE = 0;

    /**
     * A constant for the king card value
     */
    public static final int RED_JOKER_VALUE = 1;

    /* Preferences Options */
    /**
     * The name of the shared preferences to be used when getting the object
     */
    public static final String PREFERENCES = "PREFERENCES";

    /**
     * A constant to represent a key in the shared preferences for the sound
     * effects option
     */
    public static final String PREF_SOUND_EFFECTS = "SOUND EFFECTS";

    /**
     * A constant to represent a key in the shared preferences for the speech
     * volume option
     */
    public static final String PREF_SPEECH_VOLUME = "SPEECH VOLUME";

    /**
     * A constant to represent a key in the shared preferences for the
     * language/locale option
     */
    public static final String PREF_LANGUAGE = "LANGUAGE";

    /**
     * Does this device have TTS?
     */
    public static final String PREF_HAS_TTS = "PREF_HAS_TTS";

    /**
     * A constant to represent a key in the shared preferences for the image
     * resource to use as the back of the card
     */
    public static final String PREF_CARD_BACK = "BACKOFCARD";

    /**
     * A constant to represent cheater mode
     */
    public static final String PREF_CHEATER_MODE = "CHEATERMODE";
}
