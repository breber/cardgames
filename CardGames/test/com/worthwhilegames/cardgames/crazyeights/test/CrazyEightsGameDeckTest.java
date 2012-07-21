package test.com.worthwhilegames.cardgames.crazyeights.test;

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
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.CardGame;
import com.worthwhilegames.cardgames.shared.Deck;


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

		deck = new Deck(CardGame.CrazyEights);

	}

	/**
	 * Method to set up the initial deck of cards that are valid
	 */
	public void setupDeck(){
		cardDeck.add(new Card(SUIT_CLUBS, ACE_VALUE, R.drawable.clubs_a, 0));
		cardDeck.add(new Card(SUIT_CLUBS, TWO_VALUE, R.drawable.clubs_2, 1));
		cardDeck.add(new Card(SUIT_CLUBS, THREE_VALUE, R.drawable.clubs_3, 2));
		cardDeck.add(new Card(SUIT_CLUBS, FOUR_VALUE, R.drawable.clubs_4, 3));
		cardDeck.add(new Card(SUIT_CLUBS, FIVE_VALUE, R.drawable.clubs_5, 4));
		cardDeck.add(new Card(SUIT_CLUBS, SIX_VALUE, R.drawable.clubs_6, 5));
		cardDeck.add(new Card(SUIT_CLUBS, SEVEN_VALUE, R.drawable.clubs_7, 6));
		cardDeck.add(new Card(SUIT_CLUBS, EIGHT_VALUE, R.drawable.clubs_8, 7));
		cardDeck.add(new Card(SUIT_CLUBS, NINE_VALUE, R.drawable.clubs_9, 8));
		cardDeck.add(new Card(SUIT_CLUBS, TEN_VALUE, R.drawable.clubs_10, 9));
		cardDeck.add(new Card(SUIT_CLUBS, JACK_VALUE, R.drawable.clubs_j, 10));
		cardDeck.add(new Card(SUIT_CLUBS, QUEEN_VALUE, R.drawable.clubs_q, 11));
		cardDeck.add(new Card(SUIT_CLUBS, KING_VALUE, R.drawable.clubs_k, 12));
		cardDeck.add(new Card(SUIT_DIAMONDS, ACE_VALUE, R.drawable.diamonds_a, 13));
		cardDeck.add(new Card(SUIT_DIAMONDS, TWO_VALUE, R.drawable.diamonds_2, 14));
		cardDeck.add(new Card(SUIT_DIAMONDS, THREE_VALUE, R.drawable.diamonds_3, 15));
		cardDeck.add(new Card(SUIT_DIAMONDS, FOUR_VALUE, R.drawable.diamonds_4, 16));
		cardDeck.add(new Card(SUIT_DIAMONDS, FIVE_VALUE, R.drawable.diamonds_5, 17));
		cardDeck.add(new Card(SUIT_DIAMONDS, SIX_VALUE, R.drawable.diamonds_6, 18));
		cardDeck.add(new Card(SUIT_DIAMONDS, SEVEN_VALUE, R.drawable.diamonds_7, 19));
		cardDeck.add(new Card(SUIT_DIAMONDS, EIGHT_VALUE, R.drawable.diamonds_8, 20));
		cardDeck.add(new Card(SUIT_DIAMONDS, NINE_VALUE, R.drawable.diamonds_9, 21));
		cardDeck.add(new Card(SUIT_DIAMONDS, TEN_VALUE, R.drawable.diamonds_10, 22));
		cardDeck.add(new Card(SUIT_DIAMONDS, JACK_VALUE, R.drawable.diamonds_j, 23));
		cardDeck.add(new Card(SUIT_DIAMONDS, QUEEN_VALUE, R.drawable.diamonds_q, 24));
		cardDeck.add(new Card(SUIT_DIAMONDS, KING_VALUE, R.drawable.diamonds_k, 25));
		cardDeck.add(new Card(SUIT_HEARTS, ACE_VALUE, R.drawable.hearts_a, 26));
		cardDeck.add(new Card(SUIT_HEARTS, TWO_VALUE, R.drawable.hearts_2, 27));
		cardDeck.add(new Card(SUIT_HEARTS, THREE_VALUE, R.drawable.hearts_3, 28));
		cardDeck.add(new Card(SUIT_HEARTS, FOUR_VALUE, R.drawable.hearts_4, 29));
		cardDeck.add(new Card(SUIT_HEARTS, FIVE_VALUE, R.drawable.hearts_5, 30));
		cardDeck.add(new Card(SUIT_HEARTS, SIX_VALUE, R.drawable.hearts_6, 31));
		cardDeck.add(new Card(SUIT_HEARTS, SEVEN_VALUE, R.drawable.hearts_7, 32));
		cardDeck.add(new Card(SUIT_HEARTS, EIGHT_VALUE, R.drawable.hearts_8, 33));
		cardDeck.add(new Card(SUIT_HEARTS, NINE_VALUE, R.drawable.hearts_9, 34));
		cardDeck.add(new Card(SUIT_HEARTS, TEN_VALUE, R.drawable.hearts_10, 35));
		cardDeck.add(new Card(SUIT_HEARTS, JACK_VALUE, R.drawable.hearts_j, 36));
		cardDeck.add(new Card(SUIT_HEARTS, QUEEN_VALUE, R.drawable.hearts_q, 37));
		cardDeck.add(new Card(SUIT_HEARTS, KING_VALUE, R.drawable.hearts_k, 38));
		cardDeck.add(new Card(SUIT_SPADES, ACE_VALUE, R.drawable.spades_a, 39));
		cardDeck.add(new Card(SUIT_SPADES, TWO_VALUE, R.drawable.spades_2, 40));
		cardDeck.add(new Card(SUIT_SPADES, THREE_VALUE, R.drawable.spades_3, 41));
		cardDeck.add(new Card(SUIT_SPADES, FOUR_VALUE, R.drawable.spades_4, 42));
		cardDeck.add(new Card(SUIT_SPADES, FIVE_VALUE, R.drawable.spades_5, 43));
		cardDeck.add(new Card(SUIT_SPADES, SIX_VALUE, R.drawable.spades_6, 44));
		cardDeck.add(new Card(SUIT_SPADES, SEVEN_VALUE, R.drawable.spades_7, 45));
		cardDeck.add(new Card(SUIT_SPADES, EIGHT_VALUE, R.drawable.spades_8, 46));
		cardDeck.add(new Card(SUIT_SPADES, NINE_VALUE, R.drawable.spades_9, 47));
		cardDeck.add(new Card(SUIT_SPADES, TEN_VALUE, R.drawable.spades_10, 48));
		cardDeck.add(new Card(SUIT_SPADES, JACK_VALUE, R.drawable.spades_j, 49));
		cardDeck.add(new Card(SUIT_SPADES, QUEEN_VALUE, R.drawable.spades_q, 50));
		cardDeck.add(new Card(SUIT_SPADES, KING_VALUE, R.drawable.spades_k, 51));
		cardDeck.add(new Card(SUIT_JOKER, BLACK_JOKER_VALUE, R.drawable.joker_b, 52));
		cardDeck.add(new Card(SUIT_JOKER, RED_JOKER_VALUE, R.drawable.joker_r, 53));
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
