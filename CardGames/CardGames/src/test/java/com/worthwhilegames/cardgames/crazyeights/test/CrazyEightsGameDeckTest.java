package com.worthwhilegames.cardgames.crazyeights.test;

import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.CardGame;
import com.worthwhilegames.cardgames.shared.Deck;
import com.worthwhilegames.cardgames.shared.Util;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.worthwhilegames.cardgames.shared.Constants.*;
import static org.junit.Assert.assertEquals;


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
	public void setup() {
        Util.isTestSuite = true;
		deck = new Deck(CardGame.CrazyEights);
	}

	/**
	 * Method to set up the initial deck of cards that are valid
	 */
	public void setupDeck() {
		cardDeck.add(new Card(SUIT_CLUBS, ACE_VALUE, 0));
		cardDeck.add(new Card(SUIT_CLUBS, TWO_VALUE, 1));
		cardDeck.add(new Card(SUIT_CLUBS, THREE_VALUE, 2));
		cardDeck.add(new Card(SUIT_CLUBS, FOUR_VALUE, 3));
		cardDeck.add(new Card(SUIT_CLUBS, FIVE_VALUE, 4));
		cardDeck.add(new Card(SUIT_CLUBS, SIX_VALUE, 5));
		cardDeck.add(new Card(SUIT_CLUBS, SEVEN_VALUE, 6));
		cardDeck.add(new Card(SUIT_CLUBS, EIGHT_VALUE, 7));
		cardDeck.add(new Card(SUIT_CLUBS, NINE_VALUE, 8));
		cardDeck.add(new Card(SUIT_CLUBS, TEN_VALUE, 9));
		cardDeck.add(new Card(SUIT_CLUBS, JACK_VALUE, 10));
		cardDeck.add(new Card(SUIT_CLUBS, QUEEN_VALUE, 11));
		cardDeck.add(new Card(SUIT_CLUBS, KING_VALUE, 12));
		cardDeck.add(new Card(SUIT_DIAMONDS, ACE_VALUE, 13));
		cardDeck.add(new Card(SUIT_DIAMONDS, TWO_VALUE, 14));
		cardDeck.add(new Card(SUIT_DIAMONDS, THREE_VALUE, 15));
		cardDeck.add(new Card(SUIT_DIAMONDS, FOUR_VALUE, 16));
		cardDeck.add(new Card(SUIT_DIAMONDS, FIVE_VALUE, 17));
		cardDeck.add(new Card(SUIT_DIAMONDS, SIX_VALUE, 18));
		cardDeck.add(new Card(SUIT_DIAMONDS, SEVEN_VALUE, 19));
		cardDeck.add(new Card(SUIT_DIAMONDS, EIGHT_VALUE, 20));
		cardDeck.add(new Card(SUIT_DIAMONDS, NINE_VALUE, 21));
		cardDeck.add(new Card(SUIT_DIAMONDS, TEN_VALUE, 22));
		cardDeck.add(new Card(SUIT_DIAMONDS, JACK_VALUE, 23));
		cardDeck.add(new Card(SUIT_DIAMONDS, QUEEN_VALUE, 24));
		cardDeck.add(new Card(SUIT_DIAMONDS, KING_VALUE, 25));
		cardDeck.add(new Card(SUIT_HEARTS, ACE_VALUE, 26));
		cardDeck.add(new Card(SUIT_HEARTS, TWO_VALUE, 27));
		cardDeck.add(new Card(SUIT_HEARTS, THREE_VALUE, 28));
		cardDeck.add(new Card(SUIT_HEARTS, FOUR_VALUE, 29));
		cardDeck.add(new Card(SUIT_HEARTS, FIVE_VALUE, 30));
		cardDeck.add(new Card(SUIT_HEARTS, SIX_VALUE, 31));
		cardDeck.add(new Card(SUIT_HEARTS, SEVEN_VALUE, 32));
		cardDeck.add(new Card(SUIT_HEARTS, EIGHT_VALUE, 33));
		cardDeck.add(new Card(SUIT_HEARTS, NINE_VALUE, 34));
		cardDeck.add(new Card(SUIT_HEARTS, TEN_VALUE, 35));
		cardDeck.add(new Card(SUIT_HEARTS, JACK_VALUE, 36));
		cardDeck.add(new Card(SUIT_HEARTS, QUEEN_VALUE, 37));
		cardDeck.add(new Card(SUIT_HEARTS, KING_VALUE, 38));
		cardDeck.add(new Card(SUIT_SPADES, ACE_VALUE, 39));
		cardDeck.add(new Card(SUIT_SPADES, TWO_VALUE, 40));
		cardDeck.add(new Card(SUIT_SPADES, THREE_VALUE, 41));
		cardDeck.add(new Card(SUIT_SPADES, FOUR_VALUE, 42));
		cardDeck.add(new Card(SUIT_SPADES, FIVE_VALUE, 43));
		cardDeck.add(new Card(SUIT_SPADES, SIX_VALUE, 44));
		cardDeck.add(new Card(SUIT_SPADES, SEVEN_VALUE, 45));
		cardDeck.add(new Card(SUIT_SPADES, EIGHT_VALUE, 46));
		cardDeck.add(new Card(SUIT_SPADES, NINE_VALUE, 47));
		cardDeck.add(new Card(SUIT_SPADES, TEN_VALUE, 48));
		cardDeck.add(new Card(SUIT_SPADES, JACK_VALUE, 49));
		cardDeck.add(new Card(SUIT_SPADES, QUEEN_VALUE, 50));
		cardDeck.add(new Card(SUIT_SPADES, KING_VALUE, 51));
		cardDeck.add(new Card(SUIT_JOKER, BLACK_JOKER_VALUE, 52));
		cardDeck.add(new Card(SUIT_JOKER, RED_JOKER_VALUE, 53));
	}

	/**
	 * Test to make sure the right cards are in the deck and all of their information matches
	 */
	@Test
	public void testDeck() {
		setupDeck();

		List<Card> cards = deck.getCardIDs();

		Iterator<Card> iter = cards.iterator();
		int i = 0;

		while (iter.hasNext()) {
			Card c = iter.next();
			Card c1 = cardDeck.get(i);
			assertEquals("Testing card " + i + " idNum", c1.getIdNum(), c.getIdNum());
			assertEquals("Testing card " + i + " resourceId", c1.getResourceId(), c.getResourceId());
			assertEquals("Testing card " + i + " suit", c1.getSuit(), c.getSuit());
			assertEquals("Testing card " + i + " value", c1.getValue(), c.getValue());
			i++;
		}
	}

	/**
	 * Test the size of the deck
	 */
	@Test
	public void testDeckSize() {
		List<Card> cards = deck.getCardIDs();

		assertEquals("Testing the size of the deck", 54, cards.size());
	}
}
