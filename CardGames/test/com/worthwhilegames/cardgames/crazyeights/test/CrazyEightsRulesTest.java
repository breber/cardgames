package com.worthwhilegames.cardgames.crazyeights.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.worthwhilegames.cardgames.crazyeights.CrazyEightGameRules;
import com.worthwhilegames.cardgames.shared.Card;

/**
 * This class will test the rules of the crazy eights game
 */
public class CrazyEightsRulesTest {

	private CrazyEightGameRules r;
	private Card discardTop;
	private Card toDiscard;
<<<<<<< HEAD

=======
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * This method will be performed before every test
	 */
	@Before
	public void setup(){
<<<<<<< HEAD
		r = new CrazyEightGameRules();
	}

=======
		 r = new CrazyEightGameRules();
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of card of the same suit
	 */
	@Test
	public void testValidDiscardSameSuit(){
		discardTop = new Card(0, 10, 12, 0);
		toDiscard = new Card(0, 15, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing same suit discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing same suit discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of card of the same suit
	 */
	@Test
	public void testValidDiscardSameSuit2(){
		discardTop = new Card(1, 10, 12, 0);
		toDiscard = new Card(1, 15, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing same suit discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing same suit discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of card of the same suit
	 */
	@Test
	public void testValidDiscardSameSuit3(){
		discardTop = new Card(2, 10, 12, 0);
		toDiscard = new Card(2, 15, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing same suit discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing same suit discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of card of the same suit
	 */
	@Test
	public void testValidDiscardSameSuit4(){
		discardTop = new Card(3, 10, 12, 0);
		toDiscard = new Card(4, 15, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing same suit discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing same suit discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test an invalid discard
	 */
	@Test
	public void testInvalidDiscard(){
		discardTop = new Card(0, 10, 12, 0);
		toDiscard = new Card(1, 15, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing indiscard.", false, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing indiscard.", false, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of card of the same number
	 */
	@Test
	public void testValidDiscardSameNumber(){
		discardTop = new Card(1, 15, 12, 0);
		toDiscard = new Card(0, 15, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing same value discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing same value discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of card of the same number
	 */
	@Test
	public void testValidDiscardSameNumber2(){
		discardTop = new Card(1, 14, 12, 0);
		toDiscard = new Card(0, 14, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing same value discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing same value discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of card of the same number
	 */
	@Test
	public void testValidDiscardSameNumber3(){
		discardTop = new Card(1, 0, 12, 0);
		toDiscard = new Card(0, 0, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing same value discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing same value discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of a joker
	 */
	@Test
	public void testValidDiscardJoker(){
		discardTop = new Card(0, 0, 12, 0);
		toDiscard = new Card(4, 0, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing joker discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing joker discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of a joker
	 */
	@Test
	public void testValidDiscardJoker2(){
		discardTop = new Card(4, 0, 12, 0);
		toDiscard = new Card(0, 0, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing joker discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing joker discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of an eight
	 */
	@Test
	public void testValidDiscardEight(){
		discardTop = new Card(1, 0, 12, 0);
		toDiscard = new Card(0, 7, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing eight discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing eight discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of an eight
	 */
	@Test
	public void testValidDiscardEight2(){
		discardTop = new Card(1, 0, 12, 0);
		toDiscard = new Card(0, 7, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing eight discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing eight discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of an eight
	 */
	@Test
	public void testValidDiscardEight3(){
		discardTop = new Card(1, 0, 12, 0);
		toDiscard = new Card(2, 7, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing eight discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing eight discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a valid discard of an eight
	 */
	@Test
	public void testValidDiscardEight4(){
		discardTop = new Card(1, 0, 12, 0);
		toDiscard = new Card(3, 7, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing eight discard.", true, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing eight discard.", true, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test a null card
	 */
	@Test
	public void testCardNull(){
		discardTop = null;
		toDiscard = new Card(0, 0, 15, 9);
<<<<<<< HEAD

		assertEquals("Testing null card.", false, r.checkCard(toDiscard, discardTop));
	}

=======
		
		assertEquals("Testing null card.", false, r.checkCard(toDiscard, discardTop));
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test to see if the discard pile is null
	 */
	@Test
	public void testDiscardPileNull(){
		discardTop = new Card(1, 0, 12, 0);
		toDiscard = null;
<<<<<<< HEAD

=======
		
>>>>>>> Foundation for the game Euchre.
		assertEquals("Testing discard pile null.", false, r.checkCard(toDiscard, discardTop));
	}
}
