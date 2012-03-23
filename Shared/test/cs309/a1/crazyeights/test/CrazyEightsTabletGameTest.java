package cs309.a1.crazyeights.test;

import static cs309.a1.shared.CardGame.CRAZY_EIGHTS;
import static cs309.a1.crazyeights.Constants.NUMBER_OF_CARDS_PER_HAND;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cs309.a1.crazyeights.CrazyEightGameRules;
import cs309.a1.crazyeights.CrazyEightsTabletGame;
import cs309.a1.shared.Card;
import cs309.a1.shared.Deck;
import cs309.a1.shared.Player;
import cs309.a1.shared.Rules;

public class CrazyEightsTabletGameTest {

	private CrazyEightsTabletGame game;
	private List<Player> players;
	private Deck deck;
	private Rules rules;
	private Iterator<Card> iter;
	
	/**
	 * This method will be performed before every test
	 */
	@Before
	public void setup(){
		players = new ArrayList<Player>();
		Player p1 = new Player();
		Player p2 = new Player();
		players.add(p1);
		players.add(p2);
		
		
		deck = new Deck(CRAZY_EIGHTS);
		
		rules = new CrazyEightGameRules();
		
		game = new CrazyEightsTabletGame(players, deck, rules);
	}
	
	/**
	 * Method to set up the initial game
	 */
	public void setupGame(){
		game.setup();
		iter = game.getShuffledDeck().iterator();
	}
	
	/**
	 * Test the number of players in the game
	 */
	@Test 
	public void testNumberOfPlayers(){
		setupGame();
		assertEquals("Testing number of players.", 2, game.getPlayers().size());
	}
	
	/**
	 * Test the number of cards in the discard pile after the deal
	 */
	@Test 
	public void testSizeOfDiscardPile(){
		setupGame();
		assertEquals("Testing size of the discard pile.", 1,game.getDiscardPile().size());
	}
	
	/**
	 * Test the size of the shuffled deck after the deal
	 */
	@Test 
	public void testShuffleDeckSize(){
		setupGame();
		assertEquals("Testing size of the shuffled deck.", 43, game.getShuffledDeck().size());
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
	
	/**
	 * Test the iterator of the shuffled deck
	 */
	@Test
	public void testIterNext(){
		setupGame();
		iter.next();
		iter.remove();
		
		assertEquals("Testing iter.next().", 42, game.getShuffledDeck().size());
	}
	
	/**
	 * Test the discard method
	 */
	@Test
	public void testDiscard(){
		setupGame();
		Player p = game.getPlayers().get(0);
		Card toDiscard = p.getCards().get(0);
		
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
		
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 43, game.getShuffledDeck().size());
		
		game.draw(p);
		
		assertEquals("Testing size of players hand", 6, p.getCards().size());
		assertEquals("Testing size of players hand", 6, p.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 42, game.getShuffledDeck().size());
	}
	
	/**
	 * Test to see if the game is over based on a players hand
	 */
	@Test 
	public void testIsGameOver(){
		setupGame();
		Player p = game.getPlayers().get(0);
		List<Card> cards = p.getCards();
		
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
	 * Test the deal method of the game
	 */
	@Test 
	public void testDeal(){
		setupGame();
		Player p = game.getPlayers().get(0);
		Player p2 = game.getPlayers().get(1);
		
		assertEquals("Testing size of player 1's hand", 5, p.getCards().size());
		assertEquals("Testing size of player 1's hand", 5, p.getNumCards());
		assertEquals("Testing size of player 2'a hand", 5, p2.getCards().size());
		assertEquals("Testing size of player 2's hand", 5, p2.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 43, game.getShuffledDeck().size());
		
	}
	
	/**
	 * Test the shuffle of the discard pile
	 */
	@Test 
	public void testShuffleDiscardPile(){
		setupGame();
		Player p = game.getPlayers().get(0);
		
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 43, game.getShuffledDeck().size());
		
		game.draw(p);
		assertEquals("Testing size of players hand", 6, p.getCards().size());
		assertEquals("Testing size of players hand", 6, p.getNumCards());
		game.discard(p, p.getCards().get(0));
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 2, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 42, game.getShuffledDeck().size());
		
		game.draw(p);
		assertEquals("Testing size of players hand", 6, p.getCards().size());
		assertEquals("Testing size of players hand", 6, p.getNumCards());
		game.discard(p, p.getCards().get(0));
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 3, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 41, game.getShuffledDeck().size());
		
		game.draw(p);
		assertEquals("Testing size of players hand", 6, p.getCards().size());
		assertEquals("Testing size of players hand", 6, p.getNumCards());
		game.discard(p, p.getCards().get(0));
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 4, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 40, game.getShuffledDeck().size());
		
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
	public void testSetup(){
		setupGame();
		assertEquals("Testing number of players.", 2, game.getPlayers().size());
		assertEquals("Testing size of the discard pile.", 1,game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck.", 43, game.getShuffledDeck().size());
	}
	
	/**
	 * Test the drop player method
	 */
	@Test
	public void testDropPlayer(){
		setupGame();
		Player p = game.getPlayers().get(0);
		
		assertEquals("Testing size of players hand", 5, p.getCards().size());
		assertEquals("Testing size of players hand", 5, p.getNumCards());
		assertEquals("Testing size of the discard pile", 1, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 43, game.getShuffledDeck().size());
		
		game.dropPlayer(p);
		
		assertEquals("Testing size of players hand", 0, p.getCards().size());
		assertEquals("Testing size of players hand", 0, p.getNumCards());
		assertEquals("Testing size of the discard pile", 6, game.getDiscardPile().size());
		assertEquals("Testing size of the shuffled deck", 43, game.getShuffledDeck().size());
		assertEquals("Testing size of players", 1, game.getPlayers().size());		
	}
	
	
	/**
	 * Tear down
	 */
	@After
	public void tearDown(){
		
		
	}
}