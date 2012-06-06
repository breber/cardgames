package com.worthwhilegames.cardgames.euchre.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.worthwhilegames.cardgames.euchre.EuchreGameRules;
import com.worthwhilegames.cardgames.euchre.EuchreTabletGame;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Player;

public class EuchreRulesTest {

	private EuchreGameRules r;
	private EuchreTabletGame t = new EuchreTabletGame();
	private int suitLed;
	private int trump;
	private Player player = new Player();
	
	/**
	 * This method will be performed before every test
	 */
	@Before
	public void setup(){
		 r = new EuchreGameRules();
	}
	
	@Test
	public void testFollowSuit1(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of Clubs
		Card card2 = new Card(1, 10, 0, 23);//Jack of Diamonds
		Card card3 = new Card(0, 8, 0, 8);//Nine of clubs
		Card card4 = new Card(2, 0, 0, 26);//Ace of Hearts
		Card card5 = new Card(3, 11, 0, 50);//Queen of Spades
		
		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = 0;
		suitLed = 0;
		
		t.adjustJacks(player, trump);
		
		assertEquals("Testing valid play.", true, r.checkCard(card1, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card2, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card3, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card4, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card5, trump, suitLed, player));
		
	}
	
	@Test
	public void testFollowSuit2(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of Clubs
		Card card2 = new Card(1, 10, 0, 23);//Jack of Diamonds
		Card card3 = new Card(0, 8, 0, 8);//Nine of clubs
		Card card4 = new Card(2, 0, 0, 26);//Ace of Hearts
		Card card5 = new Card(3, 11, 0, 50);//Queen of Spades
		
		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);
		
		trump = 0;
		suitLed = 1;
		
		t.adjustJacks(player, trump);
		
		assertEquals("Testing valid play.", false, r.checkCard(card1, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card2, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card3, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card4, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card5, trump, suitLed, player));
		
	}
	
	@Test
	public void testFollowSuit3(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of Clubs
		Card card2 = new Card(1, 10, 0, 23);//Jack of Diamonds
		Card card3 = new Card(0, 8, 0, 8);//Nine of clubs
		Card card4 = new Card(2, 0, 0, 26);//Ace of Hearts
		Card card5 = new Card(3, 11, 0, 50);//Queen of Spades
		
		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);
		
		trump = 0;
		suitLed = 2;
		
		t.adjustJacks(player, trump);
		
		assertEquals("Testing valid play.", false, r.checkCard(card1, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card2, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card3, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card4, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card5, trump, suitLed, player));
		
	}
	
	@Test
	public void testFollowSuit4(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of Clubs
		Card card2 = new Card(1, 10, 0, 23);//Jack of Diamonds
		Card card3 = new Card(0, 8, 0, 8);//Nine of clubs
		Card card4 = new Card(2, 0, 0, 26);//Ace of Hearts
		Card card5 = new Card(3, 11, 0, 50);//Queen of Spades
		
		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = 0;
		suitLed = 3;
		
		t.adjustJacks(player, trump);
		
		assertEquals("Testing valid play.", false, r.checkCard(card1, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card2, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card3, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card4, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card5, trump, suitLed, player));
		
	}
	
	@Test
	public void testCannotFollowSuit1(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of Clubs
		Card card2 = new Card(0, 10, 0, 10);//Jack of Clubs
		Card card3 = new Card(0, 8, 0, 8);//Nine of clubs
		Card card4 = new Card(2, 0, 0, 26);//Ace of Hearts
		Card card5 = new Card(3, 11, 0, 50);//Queen of Spades
		
		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = 0;
		suitLed = 1;
		
		t.adjustJacks(player, trump);
		
		assertEquals("Testing valid play.", true, r.checkCard(card1, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card2, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card3, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card4, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card5, trump, suitLed, player));
		
	}
	
	@Test
	public void testJackTrump1(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of Clubs
		Card card2 = new Card(0, 10, 0, 10);//Jack of Clubs
		Card card3 = new Card(0, 8, 0, 8);//Nine of clubs
		Card card4 = new Card(2, 0, 0, 26);//Ace of Hearts
		Card card5 = new Card(0, 11, 0, 11);//Queen of Spades
		
		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = 3;
		suitLed = 3;
		
		t.adjustJacks(player, trump);
		
		assertEquals("Testing valid play.", false, r.checkCard(card1, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card2, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card3, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card4, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card5, trump, suitLed, player));
		
	}
	
	@Test
	public void testJackTrump2(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of Clubs
		Card card2 = new Card(3, 10, 0, 49);//Jack of Spades
		Card card3 = new Card(0, 8, 0, 8);//Nine of clubs
		Card card4 = new Card(2, 0, 0, 26);//Ace of Hearts
		Card card5 = new Card(0, 11, 0, 11);//Queen of clubs
		
		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = 0;
		suitLed = 0;
		
		t.adjustJacks(player, trump);
		
		assertEquals("Testing valid play.", true, r.checkCard(card1, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card2, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card3, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card4, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card5, trump, suitLed, player));
		
	}
	
	@Test
	public void testJackTrump3(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of Clubs
		Card card2 = new Card(2, 10, 0, 36);//Jack of Hearts
		Card card3 = new Card(0, 8, 0, 8);//Nine of clubs
		Card card4 = new Card(2, 0, 0, 26);//Ace of Hearts
		Card card5 = new Card(1, 10, 0, 23);//Jack of Diamonds
		
		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = 1;
		suitLed = 2;
		
		t.adjustJacks(player, trump);
		
		assertEquals("Testing valid play.", false, r.checkCard(card1, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card2, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card3, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card4, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card5, trump, suitLed, player));
		
	}
	
	@Test
	public void testJacks1(){
		
		Card card1 = new Card(0, 10, 0, 10);//Jack of Clubs
		Card card2 = new Card(2, 10, 0, 36);//Jack of Hearts
		Card card3 = new Card(0, 8, 0, 8);//Nine of clubs
		Card card4 = new Card(3, 10, 0, 49);//Jack of Spades
		Card card5 = new Card(1, 10, 0, 23);//Jack of Diamonds
		
		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = 1;
		suitLed = 2;

		t.adjustJacks(player, trump);
		
		assertEquals("Testing suit.", 0, card1.getSuit());
		assertEquals("Testing suit.", 1, card2.getSuit());
		assertEquals("Testing suit.", 0, card3.getSuit());
		assertEquals("Testing suit.", 3, card4.getSuit());
		assertEquals("Testing suit.", 1, card5.getSuit());
		
		assertEquals("Testing valid play.", true, r.checkCard(card1, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card2, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card3, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card4, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card5, trump, suitLed, player));
		
	}
	
	@Test
	public void testJacks2(){
		
		Card card1 = new Card(0, 10, 0, 10);//Jack of Clubs
		Card card2 = new Card(2, 10, 0, 36);//Jack of Hearts
		Card card3 = new Card(0, 8, 0, 8);//Nine of clubs
		Card card4 = new Card(3, 10, 0, 49);//Jack of Spades
		Card card5 = new Card(1, 10, 0, 23);//Jack of Diamonds
		
		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = 1;
		suitLed = 1;

		t.adjustJacks(player, trump);
		
		assertEquals("Testing suit.", 0, card1.getSuit());
		assertEquals("Testing suit.", 1, card2.getSuit());
		assertEquals("Testing suit.", 0, card3.getSuit());
		assertEquals("Testing suit.", 3, card4.getSuit());
		assertEquals("Testing suit.", 1, card5.getSuit());
		
		assertEquals("Testing valid play.", false, r.checkCard(card1, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card2, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card3, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card4, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card5, trump, suitLed, player));
		
	}
	
	@Test
	public void testJacks3(){
		
		Card card1 = new Card(0, 10, 0, 10);//Jack of Clubs
		Card card2 = new Card(2, 10, 0, 36);//Jack of Hearts
		Card card3 = new Card(0, 8, 0, 8);//Nine of clubs
		Card card4 = new Card(3, 10, 0, 49);//Jack of Spades
		Card card5 = new Card(1, 10, 0, 23);//Jack of Diamonds
		
		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = 3;
		suitLed = 1;

		t.adjustJacks(player, trump);
		
		assertEquals("Testing suit.", 3, card1.getSuit());
		assertEquals("Testing suit.", 2, card2.getSuit());
		assertEquals("Testing suit.", 0, card3.getSuit());
		assertEquals("Testing suit.", 3, card4.getSuit());
		assertEquals("Testing suit.", 1, card5.getSuit());
		
		assertEquals("Testing valid play.", false, r.checkCard(card1, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card2, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card3, trump, suitLed, player));
		assertEquals("Testing valid play.", false, r.checkCard(card4, trump, suitLed, player));
		assertEquals("Testing valid play.", true, r.checkCard(card5, trump, suitLed, player));
		
	}
}
