package cs309.a1.crazyeights.test;

import static cs309.a1.shared.CardGame.CRAZY_EIGHTS;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cs309.a1.shared.Card;
import cs309.a1.shared.Deck;
import cs309.a1.R;

/**
 * This class will test the crazy eights game deck
 * @author jamiekujawa
 *
 */
public class CrazyEightsGameDeckTest {

	private Deck deck;
	private ArrayList<Card> cardDeck = new ArrayList<Card>();
	
	/**
	 * This method will be performed before every test
	 */
	@Before
	public void setup(){
		
		deck = new Deck(CRAZY_EIGHTS);
		
	}
	
	/**
	 * Method to set up the initial deck of cards that are valid
	 */
	public void setupDeck(){
		cardDeck.add(new Card(0, 0, R.drawable.clubs_a, 0));
		cardDeck.add(new Card(0, 1, R.drawable.clubs_2, 1));
		cardDeck.add(new Card(0, 2, R.drawable.clubs_3, 2));
		cardDeck.add(new Card(0, 3, R.drawable.clubs_4, 3));
		cardDeck.add(new Card(0, 4, R.drawable.clubs_5, 4));
		cardDeck.add(new Card(0, 5, R.drawable.clubs_6, 5));
		cardDeck.add(new Card(0, 6, R.drawable.clubs_7, 6));
		cardDeck.add(new Card(0, 7, R.drawable.clubs_8, 7));
		cardDeck.add(new Card(0, 8, R.drawable.clubs_9, 8));
		cardDeck.add(new Card(0, 9, R.drawable.clubs_10_, 9));
		cardDeck.add(new Card(0, 10, R.drawable.clubs_j, 10));
		cardDeck.add(new Card(0, 11, R.drawable.clubs_q, 11));
		cardDeck.add(new Card(0, 12, R.drawable.clubs_k, 12));
		cardDeck.add(new Card(1, 0, R.drawable.diamonds_a, 13));
		cardDeck.add(new Card(1, 1, R.drawable.diamonds_2, 14));
		cardDeck.add(new Card(1, 2, R.drawable.diamonds_3, 15));
		cardDeck.add(new Card(1, 3, R.drawable.diamonds_4, 16));
		cardDeck.add(new Card(1, 4, R.drawable.diamonds_5, 17));
		cardDeck.add(new Card(1, 5, R.drawable.diamonds_6, 18));
		cardDeck.add(new Card(1, 6, R.drawable.diamonds_7, 19));
		cardDeck.add(new Card(1, 7, R.drawable.diamonds_8, 20));
		cardDeck.add(new Card(1, 8, R.drawable.diamonds_9, 21));
		cardDeck.add(new Card(1, 9, R.drawable.diamonds_10, 22));
		cardDeck.add(new Card(1, 10, R.drawable.diamonds_j, 23));
		cardDeck.add(new Card(1, 11, R.drawable.diamonds_q, 24));
		cardDeck.add(new Card(1, 12, R.drawable.diamonds_k, 25));
		cardDeck.add(new Card(2, 0, R.drawable.hearts_a, 26));
		cardDeck.add(new Card(2, 1, R.drawable.hearts_2, 27));
		cardDeck.add(new Card(2, 2, R.drawable.hearts_3, 28));
		cardDeck.add(new Card(2, 3, R.drawable.hearts_4, 29));
		cardDeck.add(new Card(2, 4, R.drawable.hearts_5, 30));
		cardDeck.add(new Card(2, 5, R.drawable.hearts_6, 31));
		cardDeck.add(new Card(2, 6, R.drawable.hearts_7, 32));
		cardDeck.add(new Card(2, 7, R.drawable.hearts_8, 33));
		cardDeck.add(new Card(2, 8, R.drawable.hearts_9, 34));
		cardDeck.add(new Card(2, 9, R.drawable.hearts_10, 35));
		cardDeck.add(new Card(2, 10, R.drawable.hearts_j, 36));
		cardDeck.add(new Card(2, 11, R.drawable.hearts_q, 37));
		cardDeck.add(new Card(2, 12, R.drawable.hearts_k, 38));
		cardDeck.add(new Card(3, 0, R.drawable.spades_a, 39));
		cardDeck.add(new Card(3, 1, R.drawable.spades_2, 40));
		cardDeck.add(new Card(3, 2, R.drawable.spades_3, 41));
		cardDeck.add(new Card(3, 3, R.drawable.spades_4, 42));
		cardDeck.add(new Card(3, 4, R.drawable.spades_5, 43));
		cardDeck.add(new Card(3, 5, R.drawable.spades_6, 44));
		cardDeck.add(new Card(3, 6, R.drawable.spades_7, 45));
		cardDeck.add(new Card(3, 7, R.drawable.spades_8, 46));
		cardDeck.add(new Card(3, 8, R.drawable.spades_9, 47));
		cardDeck.add(new Card(3, 9, R.drawable.spades_10, 48));
		cardDeck.add(new Card(3, 10, R.drawable.spades_j, 49));
		cardDeck.add(new Card(3, 11, R.drawable.spades_q, 50));
		cardDeck.add(new Card(3, 12, R.drawable.spades_k, 51));
		cardDeck.add(new Card(4, 0, R.drawable.joker_b, 52));
		cardDeck.add(new Card(4, 1, R.drawable.joker_r, 53));
		cardDeck.add(new Card(5, 0, R.drawable.back_blue_1, 54));
	}
	
	/**
	 * Test to make sure the right cards are in the deck and all of their information matches
	 */
	@Test
	public void testDeck(){
		setupDeck();
		
		List<Card> cards = deck.getCardIDs();
		
		Iterator<Card> iter = cards.iterator();
		int i = 0;
		
		while(iter.hasNext()){
			
			Card c = iter.next();
			Card c1 = cardDeck.get(i);
			assertEquals("Testing card "+i+" idNum", c1.getIdNum(), c.getIdNum());
			assertEquals("Testing card "+i+" resourceId", c1.getResourceId(), c.getResourceId());
			assertEquals("Testing card "+i+" suit", c1.getSuit(), c.getSuit());
			assertEquals("Testing card "+i+" value", c1.getValue(), c.getValue());
			i++;
		}
		
	}
	
	/**
	 * Test the size of the deck
	 */
	@Test
	public void testDeckSize(){
		List<Card> cards = deck.getCardIDs();
		
		assertEquals("Testing the size of the deck", 54, cards.size());
	}
	
	/**
	 * Tear down
	 */
	@After
	public void tearDown(){
		
		
	}

}
