package com.worthwhilegames.cardgames.shared;

import static com.worthwhilegames.cardgames.shared.Constants.ACE_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.BLACK_JOKER_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.EIGHT_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.FIVE_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.FOUR_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.JACK_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.KING_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.NINE_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.QUEEN_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.RED_JOKER_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.SEVEN_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.SIX_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_CLUBS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_DIAMONDS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_HEARTS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_JOKER;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_SPADES;
import static com.worthwhilegames.cardgames.shared.Constants.TEN_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.THREE_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.TWO_VALUE;

import java.util.ArrayList;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsCardTranslator;
import com.worthwhilegames.cardgames.euchre.EuchreCardTranslator;

/**
 * Represents a deck of cards
 */
public class Deck {

	/**
	 * Array of card image resource IDs
	 */
	private ArrayList<Card> cardImages;

	/**
	 * The type of game being played
	 */
	private CardGame gameName;

	/**
	 * Constructor to create a deck for the given card name
	 *
	 * @param name the name of the card game based on the enum value
	 */
	public Deck(CardGame name) {
		this.gameName = name;
		cardImages = new ArrayList<Card>();
		fillArray();
	}

	/**
	 * This method will return an ArrayList representing all of the cars
	 * in the deck for the given game
	 * @return an ArrayList of card objects for the given value
	 */
	public ArrayList<Card> getCardIDs() {
		return cardImages;
	}

	/**
	 * This method is used to retrieve the back of a card image
	 *
	 * @return the id of the image representing the back of a card
	 */
	public int getCardBackID() {
		int cardId = 0;

		switch(gameName) {

		case CrazyEights:
			cardId = R.drawable.back_blue_1;
			break;
		case Euchre:
			cardId = R.drawable.back_blue_1;
			break;
		}

		return cardId;
	}

	/**
	 * This method will fill the cardImages ArrayList with card objects for the
	 * given game.
	 *
	 * 0 - Clubs
	 * 1 - Diamond
	 * 2 - Heart
	 * 3 - Spade
	 * 4 - Joker
	 *
	 * Each card has a number, 0-12 associated with it to represent which card it is.
	 * Ace is number 0 and king is 12.
	 */
	private void fillArray() {

		switch(gameName) {
		case CrazyEights:
			cardImages.add(new Card(SUIT_CLUBS, ACE_VALUE, R.drawable.clubs_a, 0));
			cardImages.add(new Card(SUIT_CLUBS, TWO_VALUE, R.drawable.clubs_2, 1));
			cardImages.add(new Card(SUIT_CLUBS, THREE_VALUE, R.drawable.clubs_3, 2));
			cardImages.add(new Card(SUIT_CLUBS, FOUR_VALUE, R.drawable.clubs_4, 3));
			cardImages.add(new Card(SUIT_CLUBS, FIVE_VALUE, R.drawable.clubs_5, 4));
			cardImages.add(new Card(SUIT_CLUBS, SIX_VALUE, R.drawable.clubs_6, 5));
			cardImages.add(new Card(SUIT_CLUBS, SEVEN_VALUE, R.drawable.clubs_7, 6));
			cardImages.add(new Card(SUIT_CLUBS, EIGHT_VALUE, R.drawable.clubs_8, 7));
			cardImages.add(new Card(SUIT_CLUBS, NINE_VALUE, R.drawable.clubs_9, 8));
			cardImages.add(new Card(SUIT_CLUBS, TEN_VALUE, R.drawable.clubs_10, 9));
			cardImages.add(new Card(SUIT_CLUBS, JACK_VALUE, R.drawable.clubs_j, 10));
			cardImages.add(new Card(SUIT_CLUBS, QUEEN_VALUE, R.drawable.clubs_q, 11));
			cardImages.add(new Card(SUIT_CLUBS, KING_VALUE, R.drawable.clubs_k, 12));
			cardImages.add(new Card(SUIT_DIAMONDS, ACE_VALUE, R.drawable.diamonds_a, 13));
			cardImages.add(new Card(SUIT_DIAMONDS, TWO_VALUE, R.drawable.diamonds_2, 14));
			cardImages.add(new Card(SUIT_DIAMONDS, THREE_VALUE, R.drawable.diamonds_3, 15));
			cardImages.add(new Card(SUIT_DIAMONDS, FOUR_VALUE, R.drawable.diamonds_4, 16));
			cardImages.add(new Card(SUIT_DIAMONDS, FIVE_VALUE, R.drawable.diamonds_5, 17));
			cardImages.add(new Card(SUIT_DIAMONDS, SIX_VALUE, R.drawable.diamonds_6, 18));
			cardImages.add(new Card(SUIT_DIAMONDS, SEVEN_VALUE, R.drawable.diamonds_7, 19));
			cardImages.add(new Card(SUIT_DIAMONDS, EIGHT_VALUE, R.drawable.diamonds_8, 20));
			cardImages.add(new Card(SUIT_DIAMONDS, NINE_VALUE, R.drawable.diamonds_9, 21));
			cardImages.add(new Card(SUIT_DIAMONDS, TEN_VALUE, R.drawable.diamonds_10, 22));
			cardImages.add(new Card(SUIT_DIAMONDS, JACK_VALUE, R.drawable.diamonds_j, 23));
			cardImages.add(new Card(SUIT_DIAMONDS, QUEEN_VALUE, R.drawable.diamonds_q, 24));
			cardImages.add(new Card(SUIT_DIAMONDS, KING_VALUE, R.drawable.diamonds_k, 25));
			cardImages.add(new Card(SUIT_HEARTS, ACE_VALUE, R.drawable.hearts_a, 26));
			cardImages.add(new Card(SUIT_HEARTS, TWO_VALUE, R.drawable.hearts_2, 27));
			cardImages.add(new Card(SUIT_HEARTS, THREE_VALUE, R.drawable.hearts_3, 28));
			cardImages.add(new Card(SUIT_HEARTS, FOUR_VALUE, R.drawable.hearts_4, 29));
			cardImages.add(new Card(SUIT_HEARTS, FIVE_VALUE, R.drawable.hearts_5, 30));
			cardImages.add(new Card(SUIT_HEARTS, SIX_VALUE, R.drawable.hearts_6, 31));
			cardImages.add(new Card(SUIT_HEARTS, SEVEN_VALUE, R.drawable.hearts_7, 32));
			cardImages.add(new Card(SUIT_HEARTS, EIGHT_VALUE, R.drawable.hearts_8, 33));
			cardImages.add(new Card(SUIT_HEARTS, NINE_VALUE, R.drawable.hearts_9, 34));
			cardImages.add(new Card(SUIT_HEARTS, TEN_VALUE, R.drawable.hearts_10, 35));
			cardImages.add(new Card(SUIT_HEARTS, JACK_VALUE, R.drawable.hearts_j, 36));
			cardImages.add(new Card(SUIT_HEARTS, QUEEN_VALUE, R.drawable.hearts_q, 37));
			cardImages.add(new Card(SUIT_HEARTS, KING_VALUE, R.drawable.hearts_k, 38));
			cardImages.add(new Card(SUIT_SPADES, ACE_VALUE, R.drawable.spades_a, 39));
			cardImages.add(new Card(SUIT_SPADES, TWO_VALUE, R.drawable.spades_2, 40));
			cardImages.add(new Card(SUIT_SPADES, THREE_VALUE, R.drawable.spades_3, 41));
			cardImages.add(new Card(SUIT_SPADES, FOUR_VALUE, R.drawable.spades_4, 42));
			cardImages.add(new Card(SUIT_SPADES, FIVE_VALUE, R.drawable.spades_5, 43));
			cardImages.add(new Card(SUIT_SPADES, SIX_VALUE, R.drawable.spades_6, 44));
			cardImages.add(new Card(SUIT_SPADES, SEVEN_VALUE, R.drawable.spades_7, 45));
			cardImages.add(new Card(SUIT_SPADES, EIGHT_VALUE, R.drawable.spades_8, 46));
			cardImages.add(new Card(SUIT_SPADES, NINE_VALUE, R.drawable.spades_9, 47));
			cardImages.add(new Card(SUIT_SPADES, TEN_VALUE, R.drawable.spades_10, 48));
			cardImages.add(new Card(SUIT_SPADES, JACK_VALUE, R.drawable.spades_j, 49));
			cardImages.add(new Card(SUIT_SPADES, QUEEN_VALUE, R.drawable.spades_q, 50));
			cardImages.add(new Card(SUIT_SPADES, KING_VALUE, R.drawable.spades_k, 51));
			cardImages.add(new Card(SUIT_JOKER, BLACK_JOKER_VALUE, R.drawable.joker_b, 52));
			cardImages.add(new Card(SUIT_JOKER, RED_JOKER_VALUE, R.drawable.joker_r, 53));
			break;

		case Euchre:
			cardImages.add(new Card(SUIT_CLUBS, ACE_VALUE, R.drawable.clubs_a, 0));
			cardImages.add(new Card(SUIT_CLUBS, NINE_VALUE, R.drawable.clubs_9, 8));
			cardImages.add(new Card(SUIT_CLUBS, TEN_VALUE, R.drawable.clubs_10, 9));
			cardImages.add(new Card(SUIT_CLUBS, JACK_VALUE, R.drawable.clubs_j, 10));
			cardImages.add(new Card(SUIT_CLUBS, QUEEN_VALUE, R.drawable.clubs_q, 11));
			cardImages.add(new Card(SUIT_CLUBS, KING_VALUE, R.drawable.clubs_k, 12));
			cardImages.add(new Card(SUIT_DIAMONDS, ACE_VALUE, R.drawable.diamonds_a, 13));
			cardImages.add(new Card(SUIT_DIAMONDS, NINE_VALUE, R.drawable.diamonds_9, 21));
			cardImages.add(new Card(SUIT_DIAMONDS, TEN_VALUE, R.drawable.diamonds_10, 22));
			cardImages.add(new Card(SUIT_DIAMONDS, JACK_VALUE, R.drawable.diamonds_j, 23));
			cardImages.add(new Card(SUIT_DIAMONDS, QUEEN_VALUE, R.drawable.diamonds_q, 24));
			cardImages.add(new Card(SUIT_DIAMONDS, KING_VALUE, R.drawable.diamonds_k, 25));
			cardImages.add(new Card(SUIT_HEARTS, ACE_VALUE, R.drawable.hearts_a, 26));
			cardImages.add(new Card(SUIT_HEARTS, NINE_VALUE, R.drawable.hearts_9, 34));
			cardImages.add(new Card(SUIT_HEARTS, TEN_VALUE, R.drawable.hearts_10, 35));
			cardImages.add(new Card(SUIT_HEARTS, JACK_VALUE, R.drawable.hearts_j, 36));
			cardImages.add(new Card(SUIT_HEARTS, QUEEN_VALUE, R.drawable.hearts_q, 37));
			cardImages.add(new Card(SUIT_HEARTS, KING_VALUE, R.drawable.hearts_k, 38));
			cardImages.add(new Card(SUIT_SPADES, ACE_VALUE, R.drawable.spades_a, 39));
			cardImages.add(new Card(SUIT_SPADES, NINE_VALUE, R.drawable.spades_9, 47));
			cardImages.add(new Card(SUIT_SPADES, TEN_VALUE, R.drawable.spades_10, 48));
			cardImages.add(new Card(SUIT_SPADES, JACK_VALUE, R.drawable.spades_j, 49));
			cardImages.add(new Card(SUIT_SPADES, QUEEN_VALUE, R.drawable.spades_q, 50));
			cardImages.add(new Card(SUIT_SPADES, KING_VALUE, R.drawable.spades_k, 51));
			break;
		}
	}

	/**
	 * Get the CardTranslator for the given game.
	 *
	 * @param game the game to get the translator for
	 * @return the CardTranslator that will properly return the
	 * resource id for the right cards.
	 */
	public static CardTranslator getCardTranslatorForGame(CardGame game) {
		switch(game) {
		case CrazyEights:
			return new CrazyEightsCardTranslator();
		case Euchre:
			return new EuchreCardTranslator();
		default:
			return null;
		}
	}
}

