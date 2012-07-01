package com.worthwhilegames.cardgames.shared;

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
			cardImages.add(new Card(0, 0, R.drawable.clubs_a, 0));
			cardImages.add(new Card(0, 1, R.drawable.clubs_2, 1));
			cardImages.add(new Card(0, 2, R.drawable.clubs_3, 2));
			cardImages.add(new Card(0, 3, R.drawable.clubs_4, 3));
			cardImages.add(new Card(0, 4, R.drawable.clubs_5, 4));
			cardImages.add(new Card(0, 5, R.drawable.clubs_6, 5));
			cardImages.add(new Card(0, 6, R.drawable.clubs_7, 6));
			cardImages.add(new Card(0, 7, R.drawable.clubs_8, 7));
			cardImages.add(new Card(0, 8, R.drawable.clubs_9, 8));
			cardImages.add(new Card(0, 9, R.drawable.clubs_10, 9));
			cardImages.add(new Card(0, 10, R.drawable.clubs_j, 10));
			cardImages.add(new Card(0, 11, R.drawable.clubs_q, 11));
			cardImages.add(new Card(0, 12, R.drawable.clubs_k, 12));
			cardImages.add(new Card(1, 0, R.drawable.diamonds_a, 13));
			cardImages.add(new Card(1, 1, R.drawable.diamonds_2, 14));
			cardImages.add(new Card(1, 2, R.drawable.diamonds_3, 15));
			cardImages.add(new Card(1, 3, R.drawable.diamonds_4, 16));
			cardImages.add(new Card(1, 4, R.drawable.diamonds_5, 17));
			cardImages.add(new Card(1, 5, R.drawable.diamonds_6, 18));
			cardImages.add(new Card(1, 6, R.drawable.diamonds_7, 19));
			cardImages.add(new Card(1, 7, R.drawable.diamonds_8, 20));
			cardImages.add(new Card(1, 8, R.drawable.diamonds_9, 21));
			cardImages.add(new Card(1, 9, R.drawable.diamonds_10, 22));
			cardImages.add(new Card(1, 10, R.drawable.diamonds_j, 23));
			cardImages.add(new Card(1, 11, R.drawable.diamonds_q, 24));
			cardImages.add(new Card(1, 12, R.drawable.diamonds_k, 25));
			cardImages.add(new Card(2, 0, R.drawable.hearts_a, 26));
			cardImages.add(new Card(2, 1, R.drawable.hearts_2, 27));
			cardImages.add(new Card(2, 2, R.drawable.hearts_3, 28));
			cardImages.add(new Card(2, 3, R.drawable.hearts_4, 29));
			cardImages.add(new Card(2, 4, R.drawable.hearts_5, 30));
			cardImages.add(new Card(2, 5, R.drawable.hearts_6, 31));
			cardImages.add(new Card(2, 6, R.drawable.hearts_7, 32));
			cardImages.add(new Card(2, 7, R.drawable.hearts_8, 33));
			cardImages.add(new Card(2, 8, R.drawable.hearts_9, 34));
			cardImages.add(new Card(2, 9, R.drawable.hearts_10, 35));
			cardImages.add(new Card(2, 10, R.drawable.hearts_j, 36));
			cardImages.add(new Card(2, 11, R.drawable.hearts_q, 37));
			cardImages.add(new Card(2, 12, R.drawable.hearts_k, 38));
			cardImages.add(new Card(3, 0, R.drawable.spades_a, 39));
			cardImages.add(new Card(3, 1, R.drawable.spades_2, 40));
			cardImages.add(new Card(3, 2, R.drawable.spades_3, 41));
			cardImages.add(new Card(3, 3, R.drawable.spades_4, 42));
			cardImages.add(new Card(3, 4, R.drawable.spades_5, 43));
			cardImages.add(new Card(3, 5, R.drawable.spades_6, 44));
			cardImages.add(new Card(3, 6, R.drawable.spades_7, 45));
			cardImages.add(new Card(3, 7, R.drawable.spades_8, 46));
			cardImages.add(new Card(3, 8, R.drawable.spades_9, 47));
			cardImages.add(new Card(3, 9, R.drawable.spades_10, 48));
			cardImages.add(new Card(3, 10, R.drawable.spades_j, 49));
			cardImages.add(new Card(3, 11, R.drawable.spades_q, 50));
			cardImages.add(new Card(3, 12, R.drawable.spades_k, 51));
			cardImages.add(new Card(4, 0, R.drawable.joker_b, 52));
			cardImages.add(new Card(4, 1, R.drawable.joker_r, 53));
			break;
			
		case Euchre:
			cardImages.add(new Card(0, 0, R.drawable.clubs_a, 0));
			cardImages.add(new Card(0, 8, R.drawable.clubs_9, 8));
			cardImages.add(new Card(0, 9, R.drawable.clubs_10, 9));
			cardImages.add(new Card(0, 10, R.drawable.clubs_j, 10));
			cardImages.add(new Card(0, 11, R.drawable.clubs_q, 11));
			cardImages.add(new Card(0, 12, R.drawable.clubs_k, 12));
			cardImages.add(new Card(1, 0, R.drawable.diamonds_a, 13));
			cardImages.add(new Card(1, 8, R.drawable.diamonds_9, 21));
			cardImages.add(new Card(1, 9, R.drawable.diamonds_10, 22));
			cardImages.add(new Card(1, 10, R.drawable.diamonds_j, 23));
			cardImages.add(new Card(1, 11, R.drawable.diamonds_q, 24));
			cardImages.add(new Card(1, 12, R.drawable.diamonds_k, 25));
			cardImages.add(new Card(2, 0, R.drawable.hearts_a, 26));
			cardImages.add(new Card(2, 8, R.drawable.hearts_9, 34));
			cardImages.add(new Card(2, 9, R.drawable.hearts_10, 35));
			cardImages.add(new Card(2, 10, R.drawable.hearts_j, 36));
			cardImages.add(new Card(2, 11, R.drawable.hearts_q, 37));
			cardImages.add(new Card(2, 12, R.drawable.hearts_k, 38));
			cardImages.add(new Card(3, 0, R.drawable.spades_a, 39));
			cardImages.add(new Card(3, 8, R.drawable.spades_9, 47));
			cardImages.add(new Card(3, 9, R.drawable.spades_10, 48));
			cardImages.add(new Card(3, 10, R.drawable.spades_j, 49));
			cardImages.add(new Card(3, 11, R.drawable.spades_q, 50));
			cardImages.add(new Card(3, 12, R.drawable.spades_k, 51));
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

