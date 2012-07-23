package com.worthwhilegames.cardgames.euchre.test;

import static com.worthwhilegames.cardgames.shared.Constants.SUIT_CLUBS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_DIAMONDS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_HEARTS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_SPADES;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.worthwhilegames.cardgames.euchre.EuchreGameRules;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Player;

public class EuchreRulesTest {

	private EuchreGameRules r;
	private Card cardLed;
	private int trump;
	private Player player = new Player();

	/**
	 * This method will be performed before every test
	 */
	@Before
	public void setup() {
		r = new EuchreGameRules();
	}

	@Test
	public void testFollowSuit1() {
		Card card1 = new Card(SUIT_CLUBS, 0, 0, 0);// Ace of Clubs
		Card card2 = new Card(SUIT_DIAMONDS, 10, 0, 23);// Jack of Diamonds
		Card card3 = new Card(SUIT_CLUBS, 8, 0, 8);// Nine of clubs
		Card card4 = new Card(SUIT_HEARTS, 0, 0, 26);// Ace of Hearts
		Card card5 = new Card(SUIT_SPADES, 11, 0, 50);// Queen of Spades

		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = SUIT_CLUBS;
		cardLed = new Card(SUIT_CLUBS, 0, 0, 0);

		assertEquals("Testing valid play.", true,
				r.checkCard(card1, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card2, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card3, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card4, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card5, trump, cardLed, player.getCards()));

		// card 1
		assertEquals(card1.getIdNum(), player.getCards().get(0).getIdNum());
		assertEquals(card1.getSuit(), player.getCards().get(0).getSuit());
		assertEquals(card1.getValue(), player.getCards().get(0).getValue());
		assertEquals(card1.getResourceId(), player.getCards().get(0)
				.getResourceId());

		// card 2
		assertEquals(card2.getIdNum(), player.getCards().get(1).getIdNum());
		assertEquals(card2.getSuit(), player.getCards().get(1).getSuit());
		assertEquals(card2.getValue(), player.getCards().get(1).getValue());
		assertEquals(card2.getResourceId(), player.getCards().get(1)
				.getResourceId());

		// card 3
		assertEquals(card3.getIdNum(), player.getCards().get(2).getIdNum());
		assertEquals(card3.getSuit(), player.getCards().get(2).getSuit());
		assertEquals(card3.getValue(), player.getCards().get(2).getValue());
		assertEquals(card3.getResourceId(), player.getCards().get(2)
				.getResourceId());

		// card 4
		assertEquals(card4.getIdNum(), player.getCards().get(3).getIdNum());
		assertEquals(card4.getSuit(), player.getCards().get(3).getSuit());
		assertEquals(card4.getValue(), player.getCards().get(3).getValue());
		assertEquals(card4.getResourceId(), player.getCards().get(3)
				.getResourceId());

		// card 5
		assertEquals(card5.getIdNum(), player.getCards().get(4).getIdNum());
		assertEquals(card5.getSuit(), player.getCards().get(4).getSuit());
		assertEquals(card5.getValue(), player.getCards().get(4).getValue());
		assertEquals(card5.getResourceId(), player.getCards().get(4)
				.getResourceId());
	}

	@Test
	public void testFollowSuit2() {
		Card card1 = new Card(SUIT_CLUBS, 0, 0, 0);// Ace of Clubs
		Card card2 = new Card(SUIT_DIAMONDS, 10, 0, 23);// Jack of Diamonds
		Card card3 = new Card(SUIT_CLUBS, 8, 0, 8);// Nine of clubs
		Card card4 = new Card(SUIT_HEARTS, 0, 0, 26);// Ace of Hearts
		Card card5 = new Card(SUIT_SPADES, 11, 0, 50);// Queen of Spades

		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = SUIT_CLUBS;
		cardLed = new Card(SUIT_DIAMONDS, 0, 0, 0);

		assertEquals("Testing valid play.", false,
				r.checkCard(card1, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card2, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card3, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card4, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card5, trump, cardLed, player.getCards()));

		// card 1
		assertEquals(card1.getIdNum(), player.getCards().get(0).getIdNum());
		assertEquals(card1.getSuit(), player.getCards().get(0).getSuit());
		assertEquals(card1.getValue(), player.getCards().get(0).getValue());
		assertEquals(card1.getResourceId(), player.getCards().get(0)
				.getResourceId());

		// card 2
		assertEquals(card2.getIdNum(), player.getCards().get(1).getIdNum());
		assertEquals(card2.getSuit(), player.getCards().get(1).getSuit());
		assertEquals(card2.getValue(), player.getCards().get(1).getValue());
		assertEquals(card2.getResourceId(), player.getCards().get(1)
				.getResourceId());

		// card 3
		assertEquals(card3.getIdNum(), player.getCards().get(2).getIdNum());
		assertEquals(card3.getSuit(), player.getCards().get(2).getSuit());
		assertEquals(card3.getValue(), player.getCards().get(2).getValue());
		assertEquals(card3.getResourceId(), player.getCards().get(2)
				.getResourceId());

		// card 4
		assertEquals(card4.getIdNum(), player.getCards().get(3).getIdNum());
		assertEquals(card4.getSuit(), player.getCards().get(3).getSuit());
		assertEquals(card4.getValue(), player.getCards().get(3).getValue());
		assertEquals(card4.getResourceId(), player.getCards().get(3)
				.getResourceId());

		// card 5
		assertEquals(card5.getIdNum(), player.getCards().get(4).getIdNum());
		assertEquals(card5.getSuit(), player.getCards().get(4).getSuit());
		assertEquals(card5.getValue(), player.getCards().get(4).getValue());
		assertEquals(card5.getResourceId(), player.getCards().get(4)
				.getResourceId());
	}

	@Test
	public void testFollowSuit3() {
		Card card1 = new Card(SUIT_CLUBS, 0, 0, 0);// Ace of Clubs
		Card card2 = new Card(SUIT_DIAMONDS, 10, 0, 23);// Jack of Diamonds
		Card card3 = new Card(SUIT_CLUBS, 8, 0, 8);// Nine of clubs
		Card card4 = new Card(SUIT_HEARTS, 0, 0, 26);// Ace of Hearts
		Card card5 = new Card(SUIT_SPADES, 11, 0, 50);// Queen of Spades

		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = SUIT_CLUBS;
		cardLed = new Card(SUIT_HEARTS, 0, 0, 0);

		assertEquals("Testing valid play.", false,
				r.checkCard(card1, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card2, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card3, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card4, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card5, trump, cardLed, player.getCards()));

		// card 1
		assertEquals(card1.getIdNum(), player.getCards().get(0).getIdNum());
		assertEquals(card1.getSuit(), player.getCards().get(0).getSuit());
		assertEquals(card1.getValue(), player.getCards().get(0).getValue());
		assertEquals(card1.getResourceId(), player.getCards().get(0)
				.getResourceId());

		// card 2
		assertEquals(card2.getIdNum(), player.getCards().get(1).getIdNum());
		assertEquals(card2.getSuit(), player.getCards().get(1).getSuit());
		assertEquals(card2.getValue(), player.getCards().get(1).getValue());
		assertEquals(card2.getResourceId(), player.getCards().get(1)
				.getResourceId());

		// card 3
		assertEquals(card3.getIdNum(), player.getCards().get(2).getIdNum());
		assertEquals(card3.getSuit(), player.getCards().get(2).getSuit());
		assertEquals(card3.getValue(), player.getCards().get(2).getValue());
		assertEquals(card3.getResourceId(), player.getCards().get(2)
				.getResourceId());

		// card 4
		assertEquals(card4.getIdNum(), player.getCards().get(3).getIdNum());
		assertEquals(card4.getSuit(), player.getCards().get(3).getSuit());
		assertEquals(card4.getValue(), player.getCards().get(3).getValue());
		assertEquals(card4.getResourceId(), player.getCards().get(3)
				.getResourceId());

		// card 5
		assertEquals(card5.getIdNum(), player.getCards().get(4).getIdNum());
		assertEquals(card5.getSuit(), player.getCards().get(4).getSuit());
		assertEquals(card5.getValue(), player.getCards().get(4).getValue());
		assertEquals(card5.getResourceId(), player.getCards().get(4)
				.getResourceId());
	}

	@Test
	public void testFollowSuit4() {
		Card card1 = new Card(SUIT_CLUBS, 0, 0, 0);// Ace of Clubs
		Card card2 = new Card(SUIT_DIAMONDS, 10, 0, 23);// Jack of Diamonds
		Card card3 = new Card(SUIT_CLUBS, 8, 0, 8);// Nine of clubs
		Card card4 = new Card(SUIT_HEARTS, 0, 0, 26);// Ace of Hearts
		Card card5 = new Card(SUIT_SPADES, 11, 0, 50);// Queen of Spades

		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = SUIT_CLUBS;
		cardLed = new Card(SUIT_SPADES, 0, 0, 0);

		assertEquals("Testing valid play.", false,
				r.checkCard(card1, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card2, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card3, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card4, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card5, trump, cardLed, player.getCards()));

		// card 1
		assertEquals(card1.getIdNum(), player.getCards().get(0).getIdNum());
		assertEquals(card1.getSuit(), player.getCards().get(0).getSuit());
		assertEquals(card1.getValue(), player.getCards().get(0).getValue());
		assertEquals(card1.getResourceId(), player.getCards().get(0)
				.getResourceId());

		// card 2
		assertEquals(card2.getIdNum(), player.getCards().get(1).getIdNum());
		assertEquals(card2.getSuit(), player.getCards().get(1).getSuit());
		assertEquals(card2.getValue(), player.getCards().get(1).getValue());
		assertEquals(card2.getResourceId(), player.getCards().get(1)
				.getResourceId());

		// card 3
		assertEquals(card3.getIdNum(), player.getCards().get(2).getIdNum());
		assertEquals(card3.getSuit(), player.getCards().get(2).getSuit());
		assertEquals(card3.getValue(), player.getCards().get(2).getValue());
		assertEquals(card3.getResourceId(), player.getCards().get(2)
				.getResourceId());

		// card 4
		assertEquals(card4.getIdNum(), player.getCards().get(3).getIdNum());
		assertEquals(card4.getSuit(), player.getCards().get(3).getSuit());
		assertEquals(card4.getValue(), player.getCards().get(3).getValue());
		assertEquals(card4.getResourceId(), player.getCards().get(3)
				.getResourceId());

		// card 5
		assertEquals(card5.getIdNum(), player.getCards().get(4).getIdNum());
		assertEquals(card5.getSuit(), player.getCards().get(4).getSuit());
		assertEquals(card5.getValue(), player.getCards().get(4).getValue());
		assertEquals(card5.getResourceId(), player.getCards().get(4)
				.getResourceId());
	}

	@Test
	public void testCannotFollowSuit1() {
		Card card1 = new Card(SUIT_CLUBS, 0, 0, 0);// Ace of Clubs
		Card card2 = new Card(SUIT_CLUBS, 10, 0, 10);// Jack of Clubs
		Card card3 = new Card(SUIT_CLUBS, 8, 0, 8);// Nine of clubs
		Card card4 = new Card(SUIT_HEARTS, 0, 0, 26);// Ace of Hearts
		Card card5 = new Card(SUIT_SPADES, 11, 0, 50);// Queen of Spades

		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = SUIT_CLUBS;
		cardLed = new Card(SUIT_DIAMONDS, 0, 0, 0);

		assertEquals("Testing valid play.", true,
				r.checkCard(card1, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card2, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card3, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card4, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card5, trump, cardLed, player.getCards()));

		// card 1
		assertEquals(card1.getIdNum(), player.getCards().get(0).getIdNum());
		assertEquals(card1.getSuit(), player.getCards().get(0).getSuit());
		assertEquals(card1.getValue(), player.getCards().get(0).getValue());
		assertEquals(card1.getResourceId(), player.getCards().get(0)
				.getResourceId());

		// card 2
		assertEquals(card2.getIdNum(), player.getCards().get(1).getIdNum());
		assertEquals(card2.getSuit(), player.getCards().get(1).getSuit());
		assertEquals(card2.getValue(), player.getCards().get(1).getValue());
		assertEquals(card2.getResourceId(), player.getCards().get(1)
				.getResourceId());

		// card 3
		assertEquals(card3.getIdNum(), player.getCards().get(2).getIdNum());
		assertEquals(card3.getSuit(), player.getCards().get(2).getSuit());
		assertEquals(card3.getValue(), player.getCards().get(2).getValue());
		assertEquals(card3.getResourceId(), player.getCards().get(2)
				.getResourceId());

		// card 4
		assertEquals(card4.getIdNum(), player.getCards().get(3).getIdNum());
		assertEquals(card4.getSuit(), player.getCards().get(3).getSuit());
		assertEquals(card4.getValue(), player.getCards().get(3).getValue());
		assertEquals(card4.getResourceId(), player.getCards().get(3)
				.getResourceId());

		// card 5
		assertEquals(card5.getIdNum(), player.getCards().get(4).getIdNum());
		assertEquals(card5.getSuit(), player.getCards().get(4).getSuit());
		assertEquals(card5.getValue(), player.getCards().get(4).getValue());
		assertEquals(card5.getResourceId(), player.getCards().get(4)
				.getResourceId());
	}

	@Test
	public void testJackTrump1() {
		Card card1 = new Card(SUIT_CLUBS, 0, 0, 0);// Ace of Clubs
		Card card2 = new Card(SUIT_CLUBS, 10, 0, 10);// Jack of Clubs
		Card card3 = new Card(SUIT_CLUBS, 8, 0, 8);// Nine of clubs
		Card card4 = new Card(SUIT_HEARTS, 0, 0, 26);// Ace of Hearts
		Card card5 = new Card(SUIT_CLUBS, 11, 0, 11);// Queen of Spades

		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = SUIT_SPADES;
		cardLed = new Card(SUIT_SPADES, 0, 0, 0);

		assertEquals("Testing valid play.", false,
				r.checkCard(card1, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card2, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card3, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card4, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card5, trump, cardLed, player.getCards()));

		// card 1
		assertEquals(card1.getIdNum(), player.getCards().get(0).getIdNum());
		assertEquals(card1.getSuit(), player.getCards().get(0).getSuit());
		assertEquals(card1.getValue(), player.getCards().get(0).getValue());
		assertEquals(card1.getResourceId(), player.getCards().get(0)
				.getResourceId());

		// card 2
		assertEquals(card2.getIdNum(), player.getCards().get(1).getIdNum());
		assertEquals(card2.getSuit(), player.getCards().get(1).getSuit());
		assertEquals(card2.getValue(), player.getCards().get(1).getValue());
		assertEquals(card2.getResourceId(), player.getCards().get(1)
				.getResourceId());

		// card 3
		assertEquals(card3.getIdNum(), player.getCards().get(2).getIdNum());
		assertEquals(card3.getSuit(), player.getCards().get(2).getSuit());
		assertEquals(card3.getValue(), player.getCards().get(2).getValue());
		assertEquals(card3.getResourceId(), player.getCards().get(2)
				.getResourceId());

		// card 4
		assertEquals(card4.getIdNum(), player.getCards().get(3).getIdNum());
		assertEquals(card4.getSuit(), player.getCards().get(3).getSuit());
		assertEquals(card4.getValue(), player.getCards().get(3).getValue());
		assertEquals(card4.getResourceId(), player.getCards().get(3)
				.getResourceId());

		// card 5
		assertEquals(card5.getIdNum(), player.getCards().get(4).getIdNum());
		assertEquals(card5.getSuit(), player.getCards().get(4).getSuit());
		assertEquals(card5.getValue(), player.getCards().get(4).getValue());
		assertEquals(card5.getResourceId(), player.getCards().get(4)
				.getResourceId());
	}

	@Test
	public void testJackTrump2() {
		Card card1 = new Card(SUIT_CLUBS, 0, 0, 0);// Ace of Clubs
		Card card2 = new Card(SUIT_SPADES, 10, 0, 49);// Jack of Spades
		Card card3 = new Card(SUIT_CLUBS, 8, 0, 8);// Nine of clubs
		Card card4 = new Card(SUIT_HEARTS, 0, 0, 26);// Ace of Hearts
		Card card5 = new Card(SUIT_CLUBS, 11, 0, 11);// Queen of clubs

		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = SUIT_CLUBS;
		cardLed = new Card(SUIT_CLUBS, 0, 0, 0);

		assertEquals("Testing valid play.", true,
				r.checkCard(card1, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card2, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card3, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card4, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card5, trump, cardLed, player.getCards()));

		// card 1
		assertEquals(card1.getIdNum(), player.getCards().get(0).getIdNum());
		assertEquals(card1.getSuit(), player.getCards().get(0).getSuit());
		assertEquals(card1.getValue(), player.getCards().get(0).getValue());
		assertEquals(card1.getResourceId(), player.getCards().get(0)
				.getResourceId());

		// card 2
		assertEquals(card2.getIdNum(), player.getCards().get(1).getIdNum());
		assertEquals(card2.getSuit(), player.getCards().get(1).getSuit());
		assertEquals(card2.getValue(), player.getCards().get(1).getValue());
		assertEquals(card2.getResourceId(), player.getCards().get(1)
				.getResourceId());

		// card 3
		assertEquals(card3.getIdNum(), player.getCards().get(2).getIdNum());
		assertEquals(card3.getSuit(), player.getCards().get(2).getSuit());
		assertEquals(card3.getValue(), player.getCards().get(2).getValue());
		assertEquals(card3.getResourceId(), player.getCards().get(2)
				.getResourceId());

		// card 4
		assertEquals(card4.getIdNum(), player.getCards().get(3).getIdNum());
		assertEquals(card4.getSuit(), player.getCards().get(3).getSuit());
		assertEquals(card4.getValue(), player.getCards().get(3).getValue());
		assertEquals(card4.getResourceId(), player.getCards().get(3)
				.getResourceId());

		// card 5
		assertEquals(card5.getIdNum(), player.getCards().get(4).getIdNum());
		assertEquals(card5.getSuit(), player.getCards().get(4).getSuit());
		assertEquals(card5.getValue(), player.getCards().get(4).getValue());
		assertEquals(card5.getResourceId(), player.getCards().get(4)
				.getResourceId());
	}

	@Test
	public void testJackTrump3() {
		Card card1 = new Card(SUIT_CLUBS, 0, 0, 0);// Ace of Clubs
		Card card2 = new Card(SUIT_HEARTS, 10, 0, 36);// Jack of Hearts
		Card card3 = new Card(SUIT_CLUBS, 8, 0, 8);// Nine of clubs
		Card card4 = new Card(SUIT_HEARTS, 0, 0, 26);// Ace of Hearts
		Card card5 = new Card(SUIT_DIAMONDS, 10, 0, 23);// Jack of Diamonds

		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = SUIT_DIAMONDS;
		cardLed = new Card(SUIT_HEARTS, 0, 0, 0);

		assertEquals("Testing valid play.", false,
				r.checkCard(card1, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card2, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card3, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card4, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card5, trump, cardLed, player.getCards()));

		// card 1
		assertEquals(card1.getIdNum(), player.getCards().get(0).getIdNum());
		assertEquals(card1.getSuit(), player.getCards().get(0).getSuit());
		assertEquals(card1.getValue(), player.getCards().get(0).getValue());
		assertEquals(card1.getResourceId(), player.getCards().get(0)
				.getResourceId());

		// card 2
		assertEquals(card2.getIdNum(), player.getCards().get(1).getIdNum());
		assertEquals(card2.getSuit(), player.getCards().get(1).getSuit());
		assertEquals(card2.getValue(), player.getCards().get(1).getValue());
		assertEquals(card2.getResourceId(), player.getCards().get(1)
				.getResourceId());

		// card 3
		assertEquals(card3.getIdNum(), player.getCards().get(2).getIdNum());
		assertEquals(card3.getSuit(), player.getCards().get(2).getSuit());
		assertEquals(card3.getValue(), player.getCards().get(2).getValue());
		assertEquals(card3.getResourceId(), player.getCards().get(2)
				.getResourceId());

		// card 4
		assertEquals(card4.getIdNum(), player.getCards().get(3).getIdNum());
		assertEquals(card4.getSuit(), player.getCards().get(3).getSuit());
		assertEquals(card4.getValue(), player.getCards().get(3).getValue());
		assertEquals(card4.getResourceId(), player.getCards().get(3)
				.getResourceId());

		// card 5
		assertEquals(card5.getIdNum(), player.getCards().get(4).getIdNum());
		assertEquals(card5.getSuit(), player.getCards().get(4).getSuit());
		assertEquals(card5.getValue(), player.getCards().get(4).getValue());
		assertEquals(card5.getResourceId(), player.getCards().get(4)
				.getResourceId());
	}

	@Test
	public void testJacks1() {
		Card card1 = new Card(SUIT_CLUBS, 10, 0, 10);// Jack of Clubs
		Card card2 = new Card(SUIT_HEARTS, 10, 0, 36);// Jack of Hearts
		Card card3 = new Card(SUIT_CLUBS, 8, 0, 8);// Nine of clubs
		Card card4 = new Card(SUIT_SPADES, 10, 0, 49);// Jack of Spades
		Card card5 = new Card(SUIT_DIAMONDS, 10, 0, 23);// Jack of Diamonds

		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = SUIT_DIAMONDS;
		cardLed = new Card(SUIT_HEARTS, 0, 0, 0);

		assertEquals("Testing valid play.", true,
				r.checkCard(card1, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card2, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card3, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card4, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card5, trump, cardLed, player.getCards()));

		// card 1
		assertEquals(card1.getIdNum(), player.getCards().get(0).getIdNum());
		assertEquals(card1.getSuit(), player.getCards().get(0).getSuit());
		assertEquals(card1.getValue(), player.getCards().get(0).getValue());
		assertEquals(card1.getResourceId(), player.getCards().get(0)
				.getResourceId());

		// card 2
		assertEquals(card2.getIdNum(), player.getCards().get(1).getIdNum());
		assertEquals(card2.getSuit(), player.getCards().get(1).getSuit());
		assertEquals(card2.getValue(), player.getCards().get(1).getValue());
		assertEquals(card2.getResourceId(), player.getCards().get(1)
				.getResourceId());

		// card 3
		assertEquals(card3.getIdNum(), player.getCards().get(2).getIdNum());
		assertEquals(card3.getSuit(), player.getCards().get(2).getSuit());
		assertEquals(card3.getValue(), player.getCards().get(2).getValue());
		assertEquals(card3.getResourceId(), player.getCards().get(2)
				.getResourceId());

		// card 4
		assertEquals(card4.getIdNum(), player.getCards().get(3).getIdNum());
		assertEquals(card4.getSuit(), player.getCards().get(3).getSuit());
		assertEquals(card4.getValue(), player.getCards().get(3).getValue());
		assertEquals(card4.getResourceId(), player.getCards().get(3)
				.getResourceId());

		// card 5
		assertEquals(card5.getIdNum(), player.getCards().get(4).getIdNum());
		assertEquals(card5.getSuit(), player.getCards().get(4).getSuit());
		assertEquals(card5.getValue(), player.getCards().get(4).getValue());
		assertEquals(card5.getResourceId(), player.getCards().get(4)
				.getResourceId());
	}

	@Test
	public void testJacks2() {
		Card card1 = new Card(SUIT_CLUBS, 10, 0, 10);// Jack of Clubs
		Card card2 = new Card(SUIT_HEARTS, 10, 0, 36);// Jack of Hearts
		Card card3 = new Card(SUIT_CLUBS, 8, 0, 8);// Nine of clubs
		Card card4 = new Card(SUIT_SPADES, 10, 0, 49);// Jack of Spades
		Card card5 = new Card(SUIT_DIAMONDS, 10, 0, 23);// Jack of Diamonds

		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = SUIT_DIAMONDS;
		cardLed = new Card(SUIT_DIAMONDS, 0, 0, 0);

		assertEquals("Testing valid play.", false,
				r.checkCard(card1, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card2, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card3, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card4, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card5, trump, cardLed, player.getCards()));

		// card 1
		assertEquals(card1.getIdNum(), player.getCards().get(0).getIdNum());
		assertEquals(card1.getSuit(), player.getCards().get(0).getSuit());
		assertEquals(card1.getValue(), player.getCards().get(0).getValue());
		assertEquals(card1.getResourceId(), player.getCards().get(0)
				.getResourceId());

		// card 2
		assertEquals(card2.getIdNum(), player.getCards().get(1).getIdNum());
		assertEquals(card2.getSuit(), player.getCards().get(1).getSuit());
		assertEquals(card2.getValue(), player.getCards().get(1).getValue());
		assertEquals(card2.getResourceId(), player.getCards().get(1)
				.getResourceId());

		// card 3
		assertEquals(card3.getIdNum(), player.getCards().get(2).getIdNum());
		assertEquals(card3.getSuit(), player.getCards().get(2).getSuit());
		assertEquals(card3.getValue(), player.getCards().get(2).getValue());
		assertEquals(card3.getResourceId(), player.getCards().get(2)
				.getResourceId());

		// card 4
		assertEquals(card4.getIdNum(), player.getCards().get(3).getIdNum());
		assertEquals(card4.getSuit(), player.getCards().get(3).getSuit());
		assertEquals(card4.getValue(), player.getCards().get(3).getValue());
		assertEquals(card4.getResourceId(), player.getCards().get(3)
				.getResourceId());

		// card 5
		assertEquals(card5.getIdNum(), player.getCards().get(4).getIdNum());
		assertEquals(card5.getSuit(), player.getCards().get(4).getSuit());
		assertEquals(card5.getValue(), player.getCards().get(4).getValue());
		assertEquals(card5.getResourceId(), player.getCards().get(4)
				.getResourceId());
	}

	@Test
	public void testJacks3() {
		Card card1 = new Card(SUIT_CLUBS, 10, 0, 10);// Jack of Clubs
		Card card2 = new Card(SUIT_HEARTS, 10, 0, 36);// Jack of Hearts
		Card card3 = new Card(SUIT_CLUBS, 8, 0, 8);// Nine of clubs
		Card card4 = new Card(SUIT_SPADES, 10, 0, 49);// Jack of Spades
		Card card5 = new Card(SUIT_DIAMONDS, 10, 0, 23);// Jack of Diamonds

		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = SUIT_SPADES;
		cardLed = new Card(SUIT_DIAMONDS, 0, 0, 0);

		assertEquals("Testing valid play.", false,
				r.checkCard(card1, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card2, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card3, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card4, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card5, trump, cardLed, player.getCards()));

		// card 1
		assertEquals(card1.getIdNum(), player.getCards().get(0).getIdNum());
		assertEquals(card1.getSuit(), player.getCards().get(0).getSuit());
		assertEquals(card1.getValue(), player.getCards().get(0).getValue());
		assertEquals(card1.getResourceId(), player.getCards().get(0)
				.getResourceId());

		// card 2
		assertEquals(card2.getIdNum(), player.getCards().get(1).getIdNum());
		assertEquals(card2.getSuit(), player.getCards().get(1).getSuit());
		assertEquals(card2.getValue(), player.getCards().get(1).getValue());
		assertEquals(card2.getResourceId(), player.getCards().get(1)
				.getResourceId());

		// card 3
		assertEquals(card3.getIdNum(), player.getCards().get(2).getIdNum());
		assertEquals(card3.getSuit(), player.getCards().get(2).getSuit());
		assertEquals(card3.getValue(), player.getCards().get(2).getValue());
		assertEquals(card3.getResourceId(), player.getCards().get(2)
				.getResourceId());

		// card 4
		assertEquals(card4.getIdNum(), player.getCards().get(3).getIdNum());
		assertEquals(card4.getSuit(), player.getCards().get(3).getSuit());
		assertEquals(card4.getValue(), player.getCards().get(3).getValue());
		assertEquals(card4.getResourceId(), player.getCards().get(3)
				.getResourceId());

		// card 5
		assertEquals(card5.getIdNum(), player.getCards().get(4).getIdNum());
		assertEquals(card5.getSuit(), player.getCards().get(4).getSuit());
		assertEquals(card5.getValue(), player.getCards().get(4).getValue());
		assertEquals(card5.getResourceId(), player.getCards().get(4)
				.getResourceId());
	}

	@Test
	public void testJacks4() {
		Card card1 = new Card(SUIT_CLUBS, 10, 0, 10);// Jack of Clubs
		Card card2 = new Card(SUIT_HEARTS, 10, 0, 36);// Jack of Hearts
		Card card3 = new Card(SUIT_CLUBS, 8, 0, 8);// Nine of clubs
		Card card4 = new Card(SUIT_SPADES, 10, 0, 49);// Jack of Spades
		Card card5 = new Card(SUIT_DIAMONDS, 10, 0, 23);// Jack of Diamonds

		player.addCard(card1);
		player.addCard(card2);
		player.addCard(card3);
		player.addCard(card4);
		player.addCard(card5);

		trump = SUIT_SPADES;
		cardLed = new Card(SUIT_CLUBS, 10, 0, 0);

		assertEquals("Testing valid play.", true,
				r.checkCard(card1, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card2, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card3, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", true,
				r.checkCard(card4, trump, cardLed, player.getCards()));
		assertEquals("Testing valid play.", false,
				r.checkCard(card5, trump, cardLed, player.getCards()));

		// card 1
		assertEquals(card1.getIdNum(), player.getCards().get(0).getIdNum());
		assertEquals(card1.getSuit(), player.getCards().get(0).getSuit());
		assertEquals(card1.getValue(), player.getCards().get(0).getValue());
		assertEquals(card1.getResourceId(), player.getCards().get(0)
				.getResourceId());

		// card 2
		assertEquals(card2.getIdNum(), player.getCards().get(1).getIdNum());
		assertEquals(card2.getSuit(), player.getCards().get(1).getSuit());
		assertEquals(card2.getValue(), player.getCards().get(1).getValue());
		assertEquals(card2.getResourceId(), player.getCards().get(1)
				.getResourceId());

		// card 3
		assertEquals(card3.getIdNum(), player.getCards().get(2).getIdNum());
		assertEquals(card3.getSuit(), player.getCards().get(2).getSuit());
		assertEquals(card3.getValue(), player.getCards().get(2).getValue());
		assertEquals(card3.getResourceId(), player.getCards().get(2)
				.getResourceId());

		// card 4
		assertEquals(card4.getIdNum(), player.getCards().get(3).getIdNum());
		assertEquals(card4.getSuit(), player.getCards().get(3).getSuit());
		assertEquals(card4.getValue(), player.getCards().get(3).getValue());
		assertEquals(card4.getResourceId(), player.getCards().get(3)
				.getResourceId());

		// card 5
		assertEquals(card5.getIdNum(), player.getCards().get(4).getIdNum());
		assertEquals(card5.getSuit(), player.getCards().get(4).getSuit());
		assertEquals(card5.getValue(), player.getCards().get(4).getValue());
		assertEquals(card5.getResourceId(), player.getCards().get(4)
				.getResourceId());
	}
}
