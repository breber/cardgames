package com.worthwhilegames.cardgames.crazyeights.test;

import com.worthwhilegames.cardgames.crazyeights.CrazyEightGameRules;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Util;
import org.junit.Before;
import org.junit.Test;

import static com.worthwhilegames.cardgames.shared.Constants.*;
import static org.junit.Assert.assertEquals;

/**
 * This class will test the rules of the crazy eights game
 */
public class CrazyEightsRulesTest {

	private CrazyEightGameRules r;
	private Card discardTop;
	private Card toDiscard;

	/**
	 * This method will be performed before every test
	 */
	@Before
	public void setup() {
        Util.isTestSuite = true;
		r = new CrazyEightGameRules();
	}

	/**
	 * Test a valid discard of card of the same suit
	 */
	@Test
	public void testValidDiscardSameSuit() {
		discardTop = new Card(SUIT_CLUBS, JACK_VALUE, 0);
		toDiscard = new Card(SUIT_CLUBS, QUEEN_VALUE, 9);

		assertEquals("Testing same suit discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a valid discard of card of the same suit
	 */
	@Test
	public void testValidDiscardSameSuit2() {
		discardTop = new Card(SUIT_DIAMONDS, JACK_VALUE, 0);
		toDiscard = new Card(SUIT_DIAMONDS, QUEEN_VALUE, 9);

		assertEquals("Testing same suit discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a valid discard of card of the same suit
	 */
	@Test
	public void testValidDiscardSameSuit3() {
		discardTop = new Card(SUIT_HEARTS, JACK_VALUE, 0);
		toDiscard = new Card(SUIT_HEARTS, QUEEN_VALUE, 9);

		assertEquals("Testing same suit discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a valid discard of card of the same suit
	 */
	@Test
	public void testValidDiscardSameSuit4() {
		discardTop = new Card(SUIT_SPADES, JACK_VALUE, 0);
		toDiscard = new Card(SUIT_JOKER, QUEEN_VALUE, 9);

		assertEquals("Testing same suit discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test an invalid discard
	 */
	@Test
	public void testInvalidDiscard() {
		discardTop = new Card(SUIT_CLUBS, JACK_VALUE, 0);
		toDiscard = new Card(SUIT_DIAMONDS, QUEEN_VALUE, 9);

		assertEquals("Testing indiscard.", false, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a valid discard of card of the same number
	 */
	@Test
	public void testValidDiscardSameNumber() {
		discardTop = new Card(SUIT_DIAMONDS, JACK_VALUE, 0);
		toDiscard = new Card(SUIT_CLUBS, JACK_VALUE, 9);

		assertEquals("Testing same value discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a valid discard of card of the same number
	 */
	@Test
	public void testValidDiscardSameNumber2() {
		discardTop = new Card(SUIT_DIAMONDS, JACK_VALUE, 0);
		toDiscard = new Card(SUIT_CLUBS, JACK_VALUE, 9);

		assertEquals("Testing same value discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a valid discard of card of the same number
	 */
	@Test
	public void testValidDiscardSameNumber3() {
		discardTop = new Card(SUIT_DIAMONDS, ACE_VALUE, 0);
		toDiscard = new Card(SUIT_CLUBS, ACE_VALUE, 9);

		assertEquals("Testing same value discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a valid discard of a joker
	 */
	@Test
	public void testValidDiscardJoker() {
		discardTop = new Card(SUIT_CLUBS, ACE_VALUE, 0);
		toDiscard = new Card(SUIT_JOKER, ACE_VALUE, 9);

		assertEquals("Testing joker discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a valid discard of a joker
	 */
	@Test
	public void testValidDiscardJoker2() {
		discardTop = new Card(SUIT_JOKER, ACE_VALUE, 0);
		toDiscard = new Card(SUIT_CLUBS, ACE_VALUE, 9);

		assertEquals("Testing joker discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a valid discard of an eight
	 */
	@Test
	public void testValidDiscardEight() {
		discardTop = new Card(SUIT_DIAMONDS, ACE_VALUE, 0);
		toDiscard = new Card(SUIT_CLUBS, EIGHT_VALUE, 9);

		assertEquals("Testing eight discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a valid discard of an eight
	 */
	@Test
	public void testValidDiscardEight2() {
		discardTop = new Card(SUIT_DIAMONDS, ACE_VALUE, 0);
		toDiscard = new Card(SUIT_CLUBS, EIGHT_VALUE, 9);

		assertEquals("Testing eight discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a valid discard of an eight
	 */
	@Test
	public void testValidDiscardEight3() {
		discardTop = new Card(SUIT_DIAMONDS, ACE_VALUE, 0);
		toDiscard = new Card(SUIT_HEARTS, EIGHT_VALUE, 9);

		assertEquals("Testing eight discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a valid discard of an eight
	 */
	@Test
	public void testValidDiscardEight4() {
		discardTop = new Card(SUIT_DIAMONDS, ACE_VALUE, 0);
		toDiscard = new Card(SUIT_SPADES, EIGHT_VALUE, 9);

		assertEquals("Testing eight discard.", true, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test a null card
	 */
	@Test
	public void testCardNull() {
		discardTop = null;
		toDiscard = new Card(SUIT_CLUBS, ACE_VALUE, 9);

		assertEquals("Testing null card.", false, r.checkCard(toDiscard, discardTop));
	}

	/**
	 * Test to see if the discard pile is null
	 */
	@Test
	public void testDiscardPileNull() {
		discardTop = new Card(SUIT_DIAMONDS, ACE_VALUE, 0);
		toDiscard = null;

		assertEquals("Testing discard pile null.", false, r.checkCard(toDiscard, discardTop));
	}
}
