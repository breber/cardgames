package cs309.a1.crazyeights.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cs309.a1.crazyeights.CrazyEightsCardTranslator;
import cs309.a1.shared.R;

public class CrazyEightsCardTranslatorTest {

	private CrazyEightsCardTranslator t;
	
	/**
	 * This method will be performed before every test
	 */
	@Before
	public void setup(){
		 t = new CrazyEightsCardTranslator();
	}
	
	/**
	 * Test the diamonds
	 */
	@Test
	public void testDiamonds(){
		
		assertEquals("Testing Diamonds ace.", R.drawable.diamonds_a, t.getResourceForCardWithId(13));
		assertEquals("Testing Diamonds 2.", R.drawable.diamonds_2, t.getResourceForCardWithId(14));
		assertEquals("Testing Diamonds 3.", R.drawable.diamonds_3, t.getResourceForCardWithId(15));
		assertEquals("Testing Diamonds 4.", R.drawable.diamonds_4, t.getResourceForCardWithId(16));
		assertEquals("Testing Diamonds 5.", R.drawable.diamonds_5, t.getResourceForCardWithId(17));
		assertEquals("Testing Diamonds 6.", R.drawable.diamonds_6, t.getResourceForCardWithId(18));
		assertEquals("Testing Diamonds 7.", R.drawable.diamonds_7, t.getResourceForCardWithId(19));
		assertEquals("Testing Diamonds 8.", R.drawable.diamonds_8, t.getResourceForCardWithId(20));
		assertEquals("Testing Diamonds 9.", R.drawable.diamonds_9, t.getResourceForCardWithId(21));
		assertEquals("Testing Diamonds 10.", R.drawable.diamonds_10, t.getResourceForCardWithId(22));
		assertEquals("Testing Diamonds jack.", R.drawable.diamonds_j, t.getResourceForCardWithId(23));
		assertEquals("Testing Diamonds queen.", R.drawable.diamonds_q, t.getResourceForCardWithId(24));
		assertEquals("Testing Diamonds king.", R.drawable.diamonds_k, t.getResourceForCardWithId(25));
		
	}
	
	/**
	 * Test the hearts
	 */
	@Test
	public void testHearts(){
		
		assertEquals("Testing Hearts ace.", R.drawable.hearts_a, t.getResourceForCardWithId(26));
		assertEquals("Testing Hearts 2.", R.drawable.hearts_2, t.getResourceForCardWithId(27));
		assertEquals("Testing Hearts 3.", R.drawable.hearts_3, t.getResourceForCardWithId(28));
		assertEquals("Testing Hearts 4.", R.drawable.hearts_4, t.getResourceForCardWithId(29));
		assertEquals("Testing Hearts 5.", R.drawable.hearts_5, t.getResourceForCardWithId(30));
		assertEquals("Testing Hearts 6.", R.drawable.hearts_6, t.getResourceForCardWithId(31));
		assertEquals("Testing Hearts 7.", R.drawable.hearts_7, t.getResourceForCardWithId(32));
		assertEquals("Testing Hearts 8.", R.drawable.hearts_8, t.getResourceForCardWithId(33));
		assertEquals("Testing Hearts 9.", R.drawable.hearts_9, t.getResourceForCardWithId(34));
		assertEquals("Testing Hearts 10.", R.drawable.hearts_10, t.getResourceForCardWithId(35));
		assertEquals("Testing Hearts jack.", R.drawable.hearts_j, t.getResourceForCardWithId(36));
		assertEquals("Testing Hearts queen.", R.drawable.hearts_q, t.getResourceForCardWithId(37));
		assertEquals("Testing Hearts king.", R.drawable.hearts_k, t.getResourceForCardWithId(38));
		
	}

	/**
	 * Test the spades
	 */
	@Test
	public void testSpades(){
		
		assertEquals("Testing Spades ace.", R.drawable.spades_a, t.getResourceForCardWithId(39));
		assertEquals("Testing Spades 2.", R.drawable.spades_2, t.getResourceForCardWithId(40));
		assertEquals("Testing Spades 3.", R.drawable.spades_3, t.getResourceForCardWithId(41));
		assertEquals("Testing Spades 4.", R.drawable.spades_4, t.getResourceForCardWithId(42));
		assertEquals("Testing Spades 5.", R.drawable.spades_5, t.getResourceForCardWithId(43));
		assertEquals("Testing Spades 6.", R.drawable.spades_6, t.getResourceForCardWithId(44));
		assertEquals("Testing Spades 7.", R.drawable.spades_7, t.getResourceForCardWithId(45));
		assertEquals("Testing Spades 8.", R.drawable.spades_8, t.getResourceForCardWithId(46));
		assertEquals("Testing Spades 9.", R.drawable.spades_9, t.getResourceForCardWithId(47));
		assertEquals("Testing Spades 10.", R.drawable.spades_10, t.getResourceForCardWithId(48));
		assertEquals("Testing Spades jack.", R.drawable.spades_j, t.getResourceForCardWithId(49));
		assertEquals("Testing Spades queen.", R.drawable.spades_q, t.getResourceForCardWithId(50));
		assertEquals("Testing Spades king.", R.drawable.spades_k, t.getResourceForCardWithId(51));
		
	}
	
	/**
	 * Test the clubs
	 */
	@Test
	public void testClubs(){
		
		assertEquals("Testing Clubs ace.", R.drawable.clubs_a, t.getResourceForCardWithId(0));
		assertEquals("Testing Clubs 2.", R.drawable.clubs_2, t.getResourceForCardWithId(1));
		assertEquals("Testing Clubs 3.", R.drawable.clubs_3, t.getResourceForCardWithId(2));
		assertEquals("Testing Clubs 4.", R.drawable.clubs_4, t.getResourceForCardWithId(3));
		assertEquals("Testing Clubs 5.", R.drawable.clubs_5, t.getResourceForCardWithId(4));
		assertEquals("Testing Clubs 6.", R.drawable.clubs_6, t.getResourceForCardWithId(5));
		assertEquals("Testing Clubs 7.", R.drawable.clubs_7, t.getResourceForCardWithId(6));
		assertEquals("Testing Clubs 8.", R.drawable.clubs_8, t.getResourceForCardWithId(7));
		assertEquals("Testing Clubs 9.", R.drawable.clubs_9, t.getResourceForCardWithId(8));
		assertEquals("Testing Clubs 10.", R.drawable.clubs_10_, t.getResourceForCardWithId(9));
		assertEquals("Testing Clubs jack.", R.drawable.clubs_j, t.getResourceForCardWithId(10));
		assertEquals("Testing Clubs queen.", R.drawable.clubs_q, t.getResourceForCardWithId(11));
		assertEquals("Testing Clubs king.", R.drawable.clubs_k, t.getResourceForCardWithId(12));
		
	}
	
	/**
	 * Test the jokers
	 */
	@Test
	public void testJokers(){
		
		assertEquals("Testing Joker black.", R.drawable.joker_b, t.getResourceForCardWithId(52));
		assertEquals("Testing Joker red.", R.drawable.joker_r, t.getResourceForCardWithId(53));
		
	}
}
