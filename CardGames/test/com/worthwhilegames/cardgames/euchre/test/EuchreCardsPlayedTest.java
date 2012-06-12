package com.worthwhilegames.cardgames.euchre.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.worthwhilegames.cardgames.euchre.EuchreTabletGame;
import com.worthwhilegames.cardgames.shared.Card;

public class EuchreCardsPlayedTest {

	private EuchreTabletGame t;
	private int suitLed;
	private List<Card> cards = new ArrayList<Card>();
	
	/**
	 * This method will be performed before every test
	 */
	@Before
	public void setup(){
		 t = new EuchreTabletGame();
	}
	
	@Test
	public void testWinner(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of Clubs
		Card card2 = new Card(1, 10, 0, 23);//Jack of Diamonds
		Card card3 = new Card(0, 8, 0, 8);//Nine of clubs
		Card card4 = new Card(2, 0, 0, 26);//Ace of Hearts
		
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);

		t.setTrump(0);
		suitLed = 0;
		
		assertEquals("Testing winner.", 0, t.determineTrickWinner(cards, suitLed));
		
	}
	
	@Test
	public void testWinner2(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of clubs
		Card card2 = new Card(0, 10, 0, 10);//Jack of clubs
		Card card3 = new Card(3, 10, 0, 49);//Jack of spades
		Card card4 = new Card(0, 12, 0, 12);//King of clubs
		
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);

		t.setTrump(0);
		suitLed = 0;
		
		assertEquals("Testing winner.", 10, t.determineTrickWinner(cards, suitLed));
		
	}
	
	@Test
	public void testWinner3(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of clubs
		Card card2 = new Card(0, 10, 0, 10);//Jack of clubs
		Card card3 = new Card(3, 10, 0, 49);//Jack of spades
		Card card4 = new Card(0, 12, 0, 12);//King of clubs
		
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);

		t.setTrump(2);
		suitLed = 3;
		
		assertEquals("Testing winner.", 49, t.determineTrickWinner(cards, suitLed));
		
	}
	
	@Test
	public void testWinner4(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of clubs
		Card card2 = new Card(0, 10, 0, 10);//Jack of clubs
		Card card3 = new Card(3, 8, 0, 49);//Nine of spades
		Card card4 = new Card(0, 12, 0, 12);//King of clubs
		
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);

		t.setTrump(3);
		suitLed = 0;
		
		assertEquals("Testing winner.", 10, t.determineTrickWinner(cards, suitLed));
		
	}
	
	@Test
	public void testWinner5(){
		
		Card card1 = new Card(0, 0, 0, 0);//Ace of clubs
		Card card2 = new Card(0, 10, 0, 10);//Jack of clubs
		Card card3 = new Card(3, 10, 0, 49);//Jack of spades
		Card card4 = new Card(0, 12, 0, 12);//King of clubs
		
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);

		t.setTrump(1);
		suitLed = 0;
		
		assertEquals("Testing winner.", 0, t.determineTrickWinner(cards, suitLed));
		
	}
}