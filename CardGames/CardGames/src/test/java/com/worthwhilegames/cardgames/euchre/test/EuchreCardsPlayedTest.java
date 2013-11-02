package com.worthwhilegames.cardgames.euchre.test;

import com.worthwhilegames.cardgames.euchre.EuchreTabletGame;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.worthwhilegames.cardgames.shared.Constants.*;
import static org.junit.Assert.assertEquals;

public class EuchreCardsPlayedTest {

    private EuchreTabletGame t;

    /**
     * This method will be performed before every test
     */
    @Before
    public void setup() {
        Util.isTestSuite = true;
        t = EuchreTabletGame.getInstance();
    }

    @Test
    public void testWinner() {
        Card card1 = new Card(SUIT_CLUBS, ACE_VALUE, 0);//Ace of Clubs
        Card card2 = new Card(SUIT_DIAMONDS, JACK_VALUE, 23);//Jack of Diamonds
        Card card3 = new Card(SUIT_CLUBS, NINE_VALUE, 8);//Nine of clubs
        Card card4 = new Card(SUIT_HEARTS, ACE_VALUE, 26);//Ace of Hearts

        t.cardsPlayed[0] = card1;
        t.cardsPlayed[1] = card2;
        t.cardsPlayed[2] = card3;
        t.cardsPlayed[3] = card4;

        t.setTrump(SUIT_CLUBS);
        t.setCardLead(card2);

        assertEquals("Testing winner.", 0, t.determineTrickWinner());
    }

    @Test
    public void testWinner2() {
        Card card1 = new Card(SUIT_CLUBS, ACE_VALUE, 0);//Ace of clubs
        Card card2 = new Card(SUIT_CLUBS, JACK_VALUE, 10);//Jack of clubs
        Card card3 = new Card(SUIT_SPADES, JACK_VALUE, 49);//Jack of spades
        Card card4 = new Card(SUIT_CLUBS, KING_VALUE, 12);//King of clubs

        t.cardsPlayed[0] = card1;
        t.cardsPlayed[1] = card2;
        t.cardsPlayed[2] = card3;
        t.cardsPlayed[3] = card4;

        t.setTrump(SUIT_CLUBS);
        t.setCardLead(card2);

        assertEquals("Testing winner.", 1, t.determineTrickWinner());
    }

    @Test
    public void testWinner3() {
        Card card1 = new Card(SUIT_CLUBS, ACE_VALUE, 0);//Ace of clubs
        Card card2 = new Card(SUIT_CLUBS, JACK_VALUE, 10);//Jack of clubs
        Card card3 = new Card(SUIT_SPADES, JACK_VALUE, 49);//Jack of spades
        Card card4 = new Card(SUIT_CLUBS, KING_VALUE, 12);//King of clubs

        t.cardsPlayed[0] = card1;
        t.cardsPlayed[1] = card2;
        t.cardsPlayed[2] = card3;
        t.cardsPlayed[3] = card4;

        t.setTrump(SUIT_CLUBS);
        t.setCardLead(card4);

        assertEquals("Testing winner.", 1, t.determineTrickWinner());
    }

    @Test
    public void testWinner4() {
        Card card1 = new Card(SUIT_CLUBS, ACE_VALUE, 0);//Ace of clubs
        Card card2 = new Card(SUIT_CLUBS, JACK_VALUE, 10);//Jack of clubs
        Card card3 = new Card(SUIT_SPADES, NINE_VALUE, 49);//Nine of spades
        Card card4 = new Card(SUIT_CLUBS, KING_VALUE, 12);//King of clubs

        t.cardsPlayed[0] = card1;
        t.cardsPlayed[1] = card2;
        t.cardsPlayed[2] = card3;
        t.cardsPlayed[3] = card4;

        t.setTrump(SUIT_SPADES);
        t.setCardLead(card3);

        assertEquals("Testing winner.", 1, t.determineTrickWinner());
    }

    @Test
    public void testWinner5() {
        Card card1 = new Card(SUIT_CLUBS, ACE_VALUE, 0);//Ace of clubs
        Card card2 = new Card(SUIT_CLUBS, JACK_VALUE, 10);//Jack of clubs
        Card card3 = new Card(SUIT_SPADES, JACK_VALUE, 49);//Jack of spades
        Card card4 = new Card(SUIT_CLUBS, KING_VALUE, 12);//King of clubs

        t.cardsPlayed[0] = card1;
        t.cardsPlayed[1] = card2;
        t.cardsPlayed[2] = card3;
        t.cardsPlayed[3] = card4;

        t.setTrump(SUIT_DIAMONDS);
        t.setCardLead(card3);

        assertEquals("Testing winner.", 2, t.determineTrickWinner());
    }

    @After
    public void tearDown() {
        EuchreTabletGame.clearInstance();
    }
}
