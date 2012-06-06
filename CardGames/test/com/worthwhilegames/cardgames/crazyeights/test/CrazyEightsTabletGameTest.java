package com.worthwhilegames.cardgames.crazyeights.test;

import static org.junit.Assert.assertEquals;

<<<<<<< HEAD
=======
import java.util.ArrayList;
>>>>>>> Foundation for the game Euchre.
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

<<<<<<< HEAD
import com.worthwhilegames.cardgames.crazyeights.C8Constants;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsTabletGame;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Player;
=======
import com.worthwhilegames.cardgames.crazyeights.CrazyEightGameRules;
import com.worthwhilegames.cardgames.crazyeights.CrazyEightsTabletGame;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Deck;
import com.worthwhilegames.cardgames.shared.Player;
import com.worthwhilegames.cardgames.shared.Rules;

import static com.worthwhilegames.cardgames.shared.CardGame.CRAZY_EIGHTS;
import static com.worthwhilegames.cardgames.crazyeights.C8Constants.NUMBER_OF_CARDS_PER_HAND;
>>>>>>> Foundation for the game Euchre.

/**
 * This class will test the game logic behind the crazy eights game
 */
public class CrazyEightsTabletGameTest {

	private CrazyEightsTabletGame game;
<<<<<<< HEAD
	private Iterator<Card> iter;

=======
	private List<Player> players;
	private Deck deck;
	private Rules rules;
	private Iterator<Card> iter;
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * This method will be performed before every test
	 */
	@Before
	public void setup(){
<<<<<<< HEAD
		Player p1 = new Player();
		Player p2 = new Player();

		game = CrazyEightsTabletGame.getInstance();

		game.addPlayer(p1);
		game.addPlayer(p2);
	}

=======
		players = new ArrayList<Player>();
		Player p1 = new Player();
		Player p2 = new Player();
		players.add(p1);
		players.add(p2);
		
		
		deck = new Deck(CRAZY_EIGHTS);
		
		rules = new CrazyEightGameRules();
		
		game = new CrazyEightsTabletGame(players, deck, rules);
		
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Method to set up the initial game
	 */
	public void setupGame(){
		game.setup();
		iter = game.getShuffledDeck().iterator();
	}
<<<<<<< HEAD

	/**
	 * Test the number of players in the game
	 */
	@Test
=======
	
	/**
	 * Test the number of players in the game
	 */
	@Test 
>>>>>>> Foundation for the game Euchre.
	public void testNumberOfPlayersTwo(){
		setupGame();
		assertEquals("Testing number of players.", 2, game.getPlayers().size());
	}
<<<<<<< HEAD

	/**
	 * Test the number of players in the game
	 */
	@Test
	public void testNumberOfPlayersThree(){
		Player player3 = new Player();
		game.addPlayer(player3);
		setupGame();
		assertEquals("Testing number of players.", 3, game.getPlayers().size());
	}

	/**
	 * Test the number of players in the game
	 */
	@Test
	public void testNumberOfPlayersFour(){
		Player player3 = new Player();
		Player player4 = new Player();
		game.addPlayer(player3);
		game.addPlayer(player4);
		setupGame();
		assertEquals("Testing number of players.", 4, game.getPlayers().size());
	}

	/**
	 * Test the number of cards in the discard pile after the deal
	 */
	@Test
=======
	
	/**
	 * Test the number of players in the game
	 */
	@Test 
	public void testNumberOfPlayersThree(){
		Player player3 = new Player();
		players.add(player3);
		setupGame();
		assertEquals("Testing number of players.", 3, game.getPlayers().size());
	}
	
	/**
	 * Test the number of players in the game
	 */
	@Test 
	public void testNumberOfPlayersFour(){
		Player player3 = new Player();
		Player player4 = new Player();
		players.add(player3);
		players.add(player4);
		setupGame();
		assertEquals("Testing number of players.", 4, game.getPlayers().size());
	}
	
	/**
	 * Test the number of cards in the discard pile after the deal
	 */
	@Test 
>>>>>>> Foundation for the game Euchre.
	public void testSizeOfDiscardPile(){
		setupGame();
		assertEquals("Testing size of the discard pile.", 1,game.getDiscardPile().size());
	}
<<<<<<< HEAD

	/**
	 * Test the size of the shuffled deck after the deal with two players
	 */
	@Test
=======
	
	/**
	 * Test the size of the shuffled deck after the deal with two players
	 */
	@Test 
>>>>>>> Foundation for the game Euchre.
	public void testShuffleDeckSizeTwoPlayers(){
		setupGame();
		assertEquals("Testing size of the shuffled deck.", 43, game.getShuffledDeck().size());
	}
<<<<<<< HEAD

	/**
	 * Test the size of the shuffled deck after the deal with three players
	 */
	@Test
	public void testShuffleDeckSizeThreePlayers(){
		Player player3 = new Player();
		game.addPlayer(player3);
		setupGame();
		assertEquals("Testing size of the shuffled deck.", 38, game.getShuffledDeck().size());
	}

	/**
	 * Test the size of the shuffled deck after the deal with four players
	 */
	@Test
	public void testShuffleDeckSizeFourPlayers(){
		Player player3 = new Player();
		Player player4 = new Player();
		game.addPlayer(player3);
		game.addPlayer(player4);
		setupGame();
		assertEquals("Testing size of the shuffled deck.", 33, game.getShuffledDeck().size());
	}

	/**
	 * Test the size of player 1's hand after the deal
	 */
	@Test
	public void testPlayer1NumberOfCard(){
		setupGame();
		assertEquals("Testing size of Player 1's hand.", C8Constants.NUMBER_OF_CARDS_PER_HAND, game.getPlayers().get(0).getCards().size());
	}

	/**
	 * Test the size of player 2's hand after the deal
	 */
	@Test
	public void testPlayer2NumberOfCard(){
		setupGame();
		assertEquals("Testing size of Player 2's hand.", C8Constants.NUMBER_OF_CARDS_PER_HAND, game.getPlayers().get(1).getCards().size());
	}

=======
	
	/**
	 * Test the size of the shuffled deck after the deal with three players
	 */
	@Test 
	public void testShuffleDeckSizeThreePlayers(){
		Player player3 = new Player();
		players.add(player3);
		setupGame();
		assertEquals("Testing size of the shuffled deck.", 38, game.getShuffledDeck().size());
	}
	
	/**
	 * Test the size of the shuffled deck after the deal with four players
	 */
	@Test 
	public void testShuffleDeckSizeFourPlayers(){
		Player player3 = new Player();
		Player player4 = new Player();
		players.add(player3);
		players.add(player4);
		setupGame();
		assertEquals("Testing size of the shuffled deck.", 33, game.getShuffledDeck().size());
	}
	
	/**
	 * Test the size of player 1's hand after the deal
	 */
	@Test 
	public void testPlayer1NumberOfCard(){
		setupGame();
		assertEquals("Testing size of Player 1's hand.", NUMBER_OF_CARDS_PER_HAND, game.getPlayers().get(0).getCards().size());
	}
	
	/**
	 * Test the size of player 2's hand after the deal
	 */
	@Test 
	public void testPlayer2NumberOfCard(){
		setupGame();
		assertEquals("Testing size of Player 2's hand.", NUMBER_OF_CARDS_PER_HAND, game.getPlayers().get(1).getCards().size());
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test the iterator of the shuffled deck
	 */
	@Test
	public void testIterNextAndRemove(){
		setupGame();
		iter.next();
		iter.remove();
<<<<<<< HEAD

		assertEquals("Testing iter.next().", 42, game.getShuffledDeck().size());
	}

=======
		
		assertEquals("Testing iter.next().", 42, game.getShuffledDeck().size());
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test the discard method
	 */
	@Test
	public void testDiscard(){
		setupGame();
		Player p = game.getPlayers().get(0);
		Card toDiscard = p.getCards().get(0);
<<<<<<< HEAD

		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());

		game.discard(p, toDiscard);


		assertEquals("Testing size of players hand", 4, p.getNumCards());
		assertEquals("Testing size of players hand", 4, p.getCards().size());
		assertEquals("Testing size of the discard pile", 2, game.getDiscardPile().size());

	}

	/**
	 * Test the draw method
	 */
	@Test
	public void testDraw(){
		setupGame();
		Player p = game.getPlayers().get(0);

=======
		
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		
		game.discard(p, toDiscard);
		
		
		assertEquals("Testing size of players hand", 4, p.getNumCards());
		assertEquals("Testing size of players hand", 4, p.getCards().size());
		assertEquals("Testing size of the discard pile", 2, game.getDiscardPile().size());
		
	}
	
	/**
	 * Test the draw method
	 */
	@Test 
	public void testDraw(){
		setupGame();
		Player p = game.getPlayers().get(0);
		
>>>>>>> Foundation for the game Euchre.
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 43, game.getShuffledDeck().size());
<<<<<<< HEAD

		Card c = game.draw(p);

=======
		
		Card c = game.draw(p);
		
>>>>>>> Foundation for the game Euchre.
		assertEquals("Testing size of players hand", 6, p.getCards().size());
		assertEquals("Testing size of players hand", 6, p.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 42, game.getShuffledDeck().size());
		assertEquals("Testing card returned", c, p.getCards().get(5));
	}
<<<<<<< HEAD

	/**
	 * Test to see if the game is over based on a players hand
	 */
	@Test
=======
	
	/**
	 * Test to see if the game is over based on a players hand
	 */
	@Test 
>>>>>>> Foundation for the game Euchre.
	public void testIsGameOver(){
		setupGame();
		Player p = game.getPlayers().get(0);
		List<Card> cards = p.getCards();
<<<<<<< HEAD

		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());

		p.removeCard(cards.get(0));
		assertEquals("Testing size of players hand", 4, p.getCards().size());

		p.removeCard(cards.get(0));
		assertEquals("Testing size of players hand", 3, p.getCards().size());

		p.removeCard(cards.get(0));
		assertEquals("Testing size of players hand", 2, p.getCards().size());

		p.removeCard(cards.get(0));
		assertEquals("Testing size of players hand", 1, p.getCards().size());

		p.removeCard(cards.get(0));
		assertEquals("Testing size of players hand", 0, p.getCards().size());
		assertEquals("Testing size of players hand", 0, p.getNumCards());

		boolean isGameOver = game.isGameOver(p);

		assertEquals("Testing if the game is over", true, isGameOver);

	}

	/**
	 * Test the deal method of the game with two players
	 */
	@Test
=======
		
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		
		p.removeCard(cards.get(0));
		assertEquals("Testing size of players hand", 4, p.getCards().size());
		
		p.removeCard(cards.get(0));
		assertEquals("Testing size of players hand", 3, p.getCards().size());
		
		p.removeCard(cards.get(0));
		assertEquals("Testing size of players hand", 2, p.getCards().size());
		
		p.removeCard(cards.get(0));
		assertEquals("Testing size of players hand", 1, p.getCards().size());
		
		p.removeCard(cards.get(0));
		assertEquals("Testing size of players hand", 0, p.getCards().size());
		assertEquals("Testing size of players hand", 0, p.getNumCards());
		
		boolean isGameOver = game.isGameOver(p);
		
		assertEquals("Testing if the game is over", true, isGameOver);
		
	}
	
	/**
	 * Test the deal method of the game with two players
	 */
	@Test 
>>>>>>> Foundation for the game Euchre.
	public void testDealTwoPlayers(){
		setupGame();
		Player p = game.getPlayers().get(0);
		Player p2 = game.getPlayers().get(1);
<<<<<<< HEAD

=======
		
>>>>>>> Foundation for the game Euchre.
		assertEquals("Testing size of player 1's hand", 5, p.getCards().size());
		assertEquals("Testing size of player 1's hand", 5, p.getNumCards());
		assertEquals("Testing size of player 2'a hand", 5, p2.getCards().size());
		assertEquals("Testing size of player 2's hand", 5, p2.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 43, game.getShuffledDeck().size());
<<<<<<< HEAD

	}

	/**
	 * Test the deal method of the game with three players
	 */
	@Test
	public void testDealThreePlayers(){

		Player player3 = new Player();
		game.addPlayer(player3);

		Player p = game.getPlayers().get(0);
		Player p2 = game.getPlayers().get(1);
		Player p3 = game.getPlayers().get(2);

		setupGame();

=======
		
	}
	
	/**
	 * Test the deal method of the game with three players
	 */
	@Test 
	public void testDealThreePlayers(){
		
		Player player3 = new Player();
		players.add(player3);
		
		Player p = game.getPlayers().get(0);
		Player p2 = game.getPlayers().get(1);
		Player p3 = game.getPlayers().get(2);
		
		setupGame();
		
>>>>>>> Foundation for the game Euchre.
		assertEquals("Testing size of player 1's hand", 5, p.getCards().size());
		assertEquals("Testing size of player 1's hand", 5, p.getNumCards());
		assertEquals("Testing size of player 2'a hand", 5, p2.getCards().size());
		assertEquals("Testing size of player 2's hand", 5, p2.getNumCards());
		assertEquals("Testing size of player 3'a hand", 5, p3.getCards().size());
		assertEquals("Testing size of player 3's hand", 5, p3.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 38, game.getShuffledDeck().size());
<<<<<<< HEAD

	}

	/**
	 * Test the deal method of the game with four players
	 */
	@Test
	public void testDealFourPlayers(){
		Player player3 = new Player();
		Player player4 = new Player();
		game.addPlayer(player3);
		game.addPlayer(player4);

=======
		
	}
	
	/**
	 * Test the deal method of the game with four players
	 */
	@Test 
	public void testDealFourPlayers(){
		
		Player player3 = new Player();
		Player player4 = new Player();
		players.add(player3);
		players.add(player4);
		
>>>>>>> Foundation for the game Euchre.
		Player p = game.getPlayers().get(0);
		Player p2 = game.getPlayers().get(1);
		Player p3 = game.getPlayers().get(2);
		Player p4 = game.getPlayers().get(3);
<<<<<<< HEAD

		setupGame();

=======
		
		setupGame();
		
>>>>>>> Foundation for the game Euchre.
		assertEquals("Testing size of player 1's hand", 5, p.getCards().size());
		assertEquals("Testing size of player 1's hand", 5, p.getNumCards());
		assertEquals("Testing size of player 2'a hand", 5, p2.getCards().size());
		assertEquals("Testing size of player 2's hand", 5, p2.getNumCards());
		assertEquals("Testing size of player 3's hand", 5, p3.getCards().size());
		assertEquals("Testing size of player 3's hand", 5, p3.getNumCards());
		assertEquals("Testing size of player 4'a hand", 5, p4.getCards().size());
		assertEquals("Testing size of player 4's hand", 5, p4.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 33, game.getShuffledDeck().size());
<<<<<<< HEAD

	}

	/**
	 * Test the shuffle of the discard pile
	 */
	@Test
	public void testShuffleDiscardPile(){
		setupGame();
		Player p = game.getPlayers().get(0);

=======
		
	}
	
	/**
	 * Test the shuffle of the discard pile
	 */
	@Test 
	public void testShuffleDiscardPile(){
		setupGame();
		Player p = game.getPlayers().get(0);
		
>>>>>>> Foundation for the game Euchre.
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 43, game.getShuffledDeck().size());
<<<<<<< HEAD

=======
		
>>>>>>> Foundation for the game Euchre.
		game.draw(p);
		assertEquals("Testing size of players hand", 6, p.getCards().size());
		assertEquals("Testing size of players hand", 6, p.getNumCards());
		game.discard(p, p.getCards().get(0));
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 2, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 42, game.getShuffledDeck().size());
<<<<<<< HEAD

=======
		
>>>>>>> Foundation for the game Euchre.
		game.draw(p);
		assertEquals("Testing size of players hand", 6, p.getCards().size());
		assertEquals("Testing size of players hand", 6, p.getNumCards());
		game.discard(p, p.getCards().get(0));
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 3, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 41, game.getShuffledDeck().size());
<<<<<<< HEAD

=======
		
>>>>>>> Foundation for the game Euchre.
		game.draw(p);
		assertEquals("Testing size of players hand", 6, p.getCards().size());
		assertEquals("Testing size of players hand", 6, p.getNumCards());
		game.discard(p, p.getCards().get(0));
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 4, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 40, game.getShuffledDeck().size());
<<<<<<< HEAD

		game.shuffleDiscardPile();
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 43, game.getShuffledDeck().size());

	}

	/**
	 * Test the shuffling of the deck
	 */
	@Test
	public void testShuffleDeck(){
		assertEquals("Testing size of the discard pile", 0, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 54, game.getShuffledDeck().size());

		game.shuffleDeck();

		assertEquals("Testing size of the discard pile", 0, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 54, game.getShuffledDeck().size());
	}

	/**
	 * Test the setup method
	 */
	@Test
=======
		
		game.shuffleDiscardPile();
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 43, game.getShuffledDeck().size());
		
	}
	
	/**
	 * Test the shuffling of the deck
	 */
	@Test 
	public void testShuffleDeck(){
		assertEquals("Testing size of the discard pile", 0, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 54, game.getShuffledDeck().size());
		
		game.shuffleDeck();
		
		assertEquals("Testing size of the discard pile", 0, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 54, game.getShuffledDeck().size());
	}
	
	/**
	 * Test the setup method
	 */
	@Test 
>>>>>>> Foundation for the game Euchre.
	public void testSetup(){
		setupGame();
		assertEquals("Testing number of players.", 2, game.getPlayers().size());
		assertEquals("Testing size of the discard pile.", 1,game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck.", 43, game.getShuffledDeck().size());
	}
<<<<<<< HEAD

=======
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Test the drop player method
	 */
	@Test
	public void testDropPlayer(){
		setupGame();
		Player p = game.getPlayers().get(0);
		game.getPlayers().get(0).setId("Mac Address");
<<<<<<< HEAD

=======
		
>>>>>>> Foundation for the game Euchre.
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 43, game.getShuffledDeck().size());
<<<<<<< HEAD

		game.dropPlayer("Mac Address");

=======
		
		game.dropPlayer("Mac Address");
		
>>>>>>> Foundation for the game Euchre.
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 43, game.getShuffledDeck().size());
<<<<<<< HEAD
		assertEquals("Testing size of players", 2, game.getPlayers().size());
	}

=======
		assertEquals("Testing size of players", 2, game.getPlayers().size());		
	}
	
>>>>>>> Foundation for the game Euchre.
	@Test
	public void getTopOfDiscardPile(){
		setupGame();
		Player p = game.getPlayers().get(0);
<<<<<<< HEAD

		Card c = p.getCards().get(0);

		game.discard(p, c);

		assertEquals("Testing discard pile top", c, game.getDiscardPileTop());

	}

	@Test
	public void getNumPlayersTest(){
		setupGame();

		assertEquals("Testing number of players", 2, game.getNumPlayers());
	}

	@Test
	public void testDropGetNumPlayers(){
		setupGame();

		game.getPlayers().get(0).setId("Mac Address");

		game.dropPlayer("Mac Address");

		assertEquals("Testing number of players", 2, game.getNumPlayers());
	}

=======
		
		Card c = p.getCards().get(0);
		
		game.discard(p, c);
		
		assertEquals("Testing discard pile top", c, game.getDiscardPileTop());
		
	}
	
	@Test
	public void getNumPlayersTest(){
		setup();
		
		assertEquals("Testing number of players", 2, game.getNumPlayers());
	}
	
	@Test
	public void testDropGetNumPlayers(){
		setup();
		
		game.getPlayers().get(0).setId("Mac Address");

		
		game.dropPlayer("Mac Address");
		
		assertEquals("Testing number of players", 2, game.getNumPlayers());
		
	}
	
>>>>>>> Foundation for the game Euchre.
	/**
	 * Tear down
	 */
	@After
	public void tearDown(){
<<<<<<< HEAD
		CrazyEightsTabletGame.clearInstance();
=======
		
		
>>>>>>> Foundation for the game Euchre.
	}
}