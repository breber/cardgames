package cs309.a1.shared;

import java.util.ArrayList;

public class Deck {

	// array of card image resource IDs
	private ArrayList<Integer> cardImages;
	
	// game type
	private CardGame gameName;
	
	public Deck(CardGame name) {
		this.gameName = name;
		cardImages = new ArrayList<Integer>();
		fillArray();
	}
	
	public ArrayList<Integer> getCardIDs() {
		return cardImages;
	}
	
	private void fillArray() {
		
		switch(gameName) {
		
		case CRAZY_EIGHTS:
			cardImages.add(R.drawable.clubs_a);
			cardImages.add(R.drawable.clubs_2);
			cardImages.add(R.drawable.clubs_3);
			cardImages.add(R.drawable.clubs_4);
			cardImages.add(R.drawable.clubs_5);
			cardImages.add(R.drawable.clubs_6);
			cardImages.add(R.drawable.clubs_7);
			cardImages.add(R.drawable.clubs_8);
			cardImages.add(R.drawable.clubs_9);
			cardImages.add(R.drawable.clubs_10_);
			cardImages.add(R.drawable.clubs_j);
			cardImages.add(R.drawable.clubs_q);
			cardImages.add(R.drawable.clubs_k);
			cardImages.add(R.drawable.diamonds_a);
			cardImages.add(R.drawable.diamonds_2);
			cardImages.add(R.drawable.diamonds_3);
			cardImages.add(R.drawable.diamonds_4);
			cardImages.add(R.drawable.diamonds_5);
			cardImages.add(R.drawable.diamonds_6);
			cardImages.add(R.drawable.diamonds_7);
			cardImages.add(R.drawable.diamonds_8);
			cardImages.add(R.drawable.diamonds_9);
			cardImages.add(R.drawable.diamonds_10);
			cardImages.add(R.drawable.diamonds_j);
			cardImages.add(R.drawable.diamonds_q);
			cardImages.add(R.drawable.diamonds_k);
			cardImages.add(R.drawable.hearts_a);
			cardImages.add(R.drawable.hearts_2);
			cardImages.add(R.drawable.hearts_3);
			cardImages.add(R.drawable.hearts_4);
			cardImages.add(R.drawable.hearts_5);
			cardImages.add(R.drawable.hearts_6);
			cardImages.add(R.drawable.hearts_7);
			cardImages.add(R.drawable.hearts_8);
			cardImages.add(R.drawable.hearts_9);
			cardImages.add(R.drawable.hearts_10);
			cardImages.add(R.drawable.hearts_j);
			cardImages.add(R.drawable.hearts_q);
			cardImages.add(R.drawable.hearts_k);
			cardImages.add(R.drawable.spades_a);
			cardImages.add(R.drawable.spades_2);
			cardImages.add(R.drawable.spades_3);
			cardImages.add(R.drawable.spades_4);
			cardImages.add(R.drawable.spades_5);
			cardImages.add(R.drawable.spades_6);
			cardImages.add(R.drawable.spades_7);
			cardImages.add(R.drawable.spades_8);
			cardImages.add(R.drawable.spades_9);
			cardImages.add(R.drawable.spades_10);
			cardImages.add(R.drawable.spades_j);
			cardImages.add(R.drawable.spades_q);
			cardImages.add(R.drawable.spades_k);
			cardImages.add(R.drawable.joker_b);
			cardImages.add(R.drawable.joker_r);
			break;
		}
	}
}
