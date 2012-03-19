package cs309.a1.shared;

import java.util.ArrayList;

public class Deck {

	// array of card image resource IDs
	private ArrayList<Card> cardImages;
	
	// game type
	private CardGame gameName;
	
	/**
	 * Constructor to create a deck for the given card name
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
	 * This method will fill the cardImages ArrayList with card objects for the
	 * given game
	 */
	private void fillArray() {
		
		switch(gameName) {
		
		case CRAZY_EIGHTS:
			cardImages.add(new Card("Club", 0, R.drawable.clubs_a));
			cardImages.add(new Card("Club", 1, R.drawable.clubs_2));
			cardImages.add(new Card("Club", 2, R.drawable.clubs_3));
			cardImages.add(new Card("Club", 3, R.drawable.clubs_4));
			cardImages.add(new Card("Club", 4, R.drawable.clubs_5));
			cardImages.add(new Card("Club", 5, R.drawable.clubs_6));
			cardImages.add(new Card("Club", 6, R.drawable.clubs_7));
			cardImages.add(new Card("Club", 7, R.drawable.clubs_8));
			cardImages.add(new Card("Club", 8, R.drawable.clubs_9));
			cardImages.add(new Card("Club", 9, R.drawable.clubs_10_));
			cardImages.add(new Card("Club", 10, R.drawable.clubs_j));
			cardImages.add(new Card("Club", 11, R.drawable.clubs_q));
			cardImages.add(new Card("Club", 12, R.drawable.clubs_k));
			cardImages.add(new Card("Diamond", 0, R.drawable.diamonds_a));
			cardImages.add(new Card("Diamond", 1, R.drawable.diamonds_2));
			cardImages.add(new Card("Diamond", 2, R.drawable.diamonds_3));
			cardImages.add(new Card("Diamond", 3, R.drawable.diamonds_4));
			cardImages.add(new Card("Diamond", 4, R.drawable.diamonds_5));
			cardImages.add(new Card("Diamond", 5, R.drawable.diamonds_6));
			cardImages.add(new Card("Diamond", 6, R.drawable.diamonds_7));
			cardImages.add(new Card("Diamond", 7, R.drawable.diamonds_8));
			cardImages.add(new Card("Diamond", 8, R.drawable.diamonds_9));
			cardImages.add(new Card("Diamond", 9, R.drawable.diamonds_10));
			cardImages.add(new Card("Diamond", 10, R.drawable.diamonds_j));
			cardImages.add(new Card("Diamond", 11, R.drawable.diamonds_q));
			cardImages.add(new Card("Diamond", 12, R.drawable.diamonds_k));
			cardImages.add(new Card("Heart", 0, R.drawable.hearts_a));
			cardImages.add(new Card("Heart", 1, R.drawable.hearts_2));
			cardImages.add(new Card("Heart", 2, R.drawable.hearts_3));
			cardImages.add(new Card("Heart", 3, R.drawable.hearts_4));
			cardImages.add(new Card("Heart", 4, R.drawable.hearts_5));
			cardImages.add(new Card("Heart", 5, R.drawable.hearts_6));
			cardImages.add(new Card("Heart", 6, R.drawable.hearts_7));
			cardImages.add(new Card("Heart", 7, R.drawable.hearts_8));
			cardImages.add(new Card("Heart", 8, R.drawable.hearts_9));
			cardImages.add(new Card("Heart", 9, R.drawable.hearts_10));
			cardImages.add(new Card("Heart", 10, R.drawable.hearts_j));
			cardImages.add(new Card("Heart", 11, R.drawable.hearts_q));
			cardImages.add(new Card("Heart", 12, R.drawable.hearts_k));
			cardImages.add(new Card("Spade", 0, R.drawable.spades_a));
			cardImages.add(new Card("Spade", 1, R.drawable.spades_2));
			cardImages.add(new Card("Spade", 2, R.drawable.spades_3));
			cardImages.add(new Card("Spade", 3, R.drawable.spades_4));
			cardImages.add(new Card("Spade", 4, R.drawable.spades_5));
			cardImages.add(new Card("Spade", 5, R.drawable.spades_6));
			cardImages.add(new Card("Spade", 6, R.drawable.spades_7));
			cardImages.add(new Card("Spade", 7, R.drawable.spades_8));
			cardImages.add(new Card("Spade", 8, R.drawable.spades_9));
			cardImages.add(new Card("Spade", 9, R.drawable.spades_10));
			cardImages.add(new Card("Spade", 10, R.drawable.spades_j));
			cardImages.add(new Card("Spade", 11, R.drawable.spades_q));
			cardImages.add(new Card("Spade", 12, R.drawable.spades_k));
			cardImages.add(new Card("Joker", 0, R.drawable.joker_b));
			cardImages.add(new Card("Joker", 1, R.drawable.joker_r));
			break;
		}
	}
}

