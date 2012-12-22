package com.worthwhilegames.cardgames.crazyeights;

import java.util.ArrayList;
import java.util.List;

import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.Player;

public class CrazyEightsComputerPlayer {

	/**
	 * the list of current active players
	 */
	private List<Player> players;

	/**
	 * This game object will keep track of the current state of the game and be
	 * used to manage player hands and draw and discard piles
	 */
	private static CrazyEightsTabletGame game = null;

	/**
	 * The implementation of the Game Rules
	 */
	private CrazyEightGameRules gameRules = new CrazyEightGameRules();

	/**
	 * The difficulty of this computer player
	 */
	private String difficulty;

	/**
	 * This stores the suit chosen by the Hard computer Player
	 */
	private int suitChosenHard;

	/**
	 * Calculates card scores for the computer
	 */
	private CardScoreCalculator csc;


	/**
	 * This will initialize a computer player
	 * @param difficulty this is the difficulty of this euchre computer
	 */
	public CrazyEightsComputerPlayer(String difficulty){
		this.difficulty = difficulty;
		game = CrazyEightsTabletGame.getInstance();
		this.players = game.getPlayers();

		//TODO: other setup?
	}


	/**
	 * This will play for a computer player based on the difficulty level. either play or draw a card.
	 * This will be called after the PlayComputerTurnActivity has waited for the appropriate amount of time.
	 * level Easy 	should just loop through the cards to find one that it is allowed to play
	 * 				very basic, randomly play a card if able or draw if not able
	 * level Medium	chooses first the cards of the same suit, then cards of the same index of a different suit,
	 * 				then a special card as a last resort. if a suit of the same index
	 *
	 * level Hard 	Recursive find best card that will allow you to win. The computer will look at all possible
	 * 				cards it can play and then look ahead to see all possible ways players can react and choose
	 * 				the card that benefits them most, it goes about 9 or 10 turns into the future.
	 */
	public Card getCardOnTurn(int whoseTurn, int suitChosen) {
		Card onDiscard = game.getDiscardPileTop();
		if (onDiscard.getValue() == C8Constants.EIGHT_CARD_NUMBER) {
			onDiscard = new Card(suitChosen, onDiscard.getValue(), onDiscard.getResourceId(), onDiscard.getIdNum());
		}
		List<Card> cards = players.get(whoseTurn).getCards();
		Card cardSelected = null;

		//computer with difficulty Easy
		if (difficulty.equals(Constants.EASY)) {
			for (Card c : cards) {
				if (gameRules.checkCard(c, onDiscard)) {
					cardSelected = c;
					break;
				}
			}

			//computer difficulty Medium
		} else if (difficulty.equals(Constants.MEDIUM)) {

			List<Card> sameSuit = new ArrayList<Card>();
			List<Card> sameNum = new ArrayList<Card>();
			List<Card> special = new ArrayList<Card>();

			int suits[] = new int[5];
			int maxSuitIndex = 0;

			for (Card c : cards) {
				//checks for 8s and jokers
				if ((c.getValue() == C8Constants.EIGHT_CARD_NUMBER || c.getSuit() == Constants.SUIT_JOKER) &&
						gameRules.checkCard(c, onDiscard)) {
					special.add(c);
					continue;
				}

				//this gets the number of cards of each suit
				suits[c.getSuit()]++;
				if (suits[c.getSuit()] > suits[maxSuitIndex] && c.getSuit() != Constants.SUIT_JOKER) {
					maxSuitIndex = c.getSuit();
				}

				//checks for cards of the same suit then cards of the same index
				if (c.getSuit() == onDiscard.getSuit() && gameRules.checkCard(c, onDiscard)) {
					sameSuit.add(c);
				} else if (c.getValue() == onDiscard.getValue() && gameRules.checkCard(c, onDiscard)) {
					sameNum.add(c);
				}
			}

			//see if there is more of another suit that the computer can change it to.
			boolean moreOfOtherSuit = false;
			for (Card c : sameNum) {
				if (suits[c.getSuit()] > suits[onDiscard.getSuit()]) {
					moreOfOtherSuit = true;
					break;
				}
			}

			if (onDiscard.getSuit() == Constants.SUIT_JOKER) { //for a joker
				for (Card c : cards){
					if (c.getSuit() == maxSuitIndex && c.getValue() != Constants.EIGHT_VALUE) {
						cardSelected = c;
						break;
					}
				}
				if (cardSelected == null) {
					for (Card c : cards) {
						if (c.getSuit() == maxSuitIndex) {
							cardSelected = c;
							break;
						}
					}
				}
			} else if (moreOfOtherSuit && sameNum.size() > 0) { //choose a card of the same number that we can change the suit with
				cardSelected = sameNum.get(0);
				for (Card c : sameNum) {
					if (suits[c.getSuit()] > suits[cardSelected.getSuit()]) {
						cardSelected = c;
					}
				}
			} else if (sameSuit.size() > 0) { //choose a card of the same suit
				cardSelected = sameSuit.get(0);
				boolean hasAnotherCardWithIndex = false;
				for (Card c : sameSuit) {
					for (Card c1 : cards) {
						if (!c.equals(c1) && c.getValue() == c1.getValue() && suits[c.getSuit()] <= suits[c1.getSuit()]) {
							cardSelected = c;
							hasAnotherCardWithIndex = true;
							break;
						}
					}
					if (hasAnotherCardWithIndex) {
						break;
					}
				}
			} else if (special.size() > 0) { //play a special card as last resort
				cardSelected = special.get(0);
			} // else { no card selected }

			//computer difficulty Hard
		} else if (difficulty.equals(Constants.HARD)) {
			this.suitChosenHard = -1;

			//get game state, clone it, send to recursive function
			List<List<Card>> cardsClone = new ArrayList<List<Card>>();
			for (Player p : players) {
				cardsClone.add(new ArrayList<Card>(p.getCards()));
			}
			csc = new CardScoreCalculator(whoseTurn, cardsClone);
			Card firstOnDiscard = game.getDiscardPileTop();
			if (firstOnDiscard.getValue() == C8Constants.EIGHT_CARD_NUMBER) {
				firstOnDiscard = new Card(suitChosen, firstOnDiscard.getValue(), firstOnDiscard.getResourceId(), firstOnDiscard.getIdNum());
			}
			Card curOnDiscard = game.getDiscardPileTop();
			int suitToChoose = findMaxSuitIndex(whoseTurn, cardsClone.get(whoseTurn));

			int nextTurnIndex=whoseTurn;
			// Figure out whose turn it is next
			if (nextTurnIndex < game.getNumPlayers() - 1) {
				nextTurnIndex++;
			} else {
				nextTurnIndex =0;
			}

			List<Card> drawPile = new ArrayList<Card>(game.getShuffledDeck());
			double tmpScore = 0;
			Card cardDrawn = null;
			int movesArraySize = cardsClone.get(whoseTurn).size() + 1;
			double moves[] = new double[movesArraySize];

			//TODO: make this a constant
			int recDepth = 6 + players.size();

			int minIndex = 0;

			//recursive call
			for (int i = 0; i < cardsClone.get(whoseTurn).size(); i++) {
				curOnDiscard = firstOnDiscard;
				Card tmpCard = cardsClone.get(whoseTurn).get(0);
				cardsClone.get(whoseTurn).remove(0);
				if (gameRules.checkCard(tmpCard, curOnDiscard)) {
					tmpScore = csc.calculateScorePlayed(tmpCard, curOnDiscard, whoseTurn);
					curOnDiscard = tmpCard;
					if (curOnDiscard.getValue() == C8Constants.EIGHT_CARD_NUMBER) {
						curOnDiscard = new Card(suitToChoose, curOnDiscard.getValue(), curOnDiscard.getResourceId(), curOnDiscard.getIdNum());
						//TODO: make the suitToChoose be calculated using the future recursion stuff.
						//		Check suit chosen for all four suits to see which brings the best result.
						double suitChoosingScores[] = new double[4];
						int bestSuitIndex = 0;
						for (int k = 0; k < 4; k++) {
							curOnDiscard = new Card(k, curOnDiscard.getValue(), curOnDiscard.getResourceId(), curOnDiscard.getIdNum());
							suitChoosingScores[k] = findBestMove(whoseTurn, nextTurnIndex, cardsClone, curOnDiscard, drawPile, recDepth);
							if (suitChoosingScores[k] < suitChoosingScores[bestSuitIndex]) {
								bestSuitIndex = k;
							}
						}
						tmpScore += suitChoosingScores[bestSuitIndex];
						this.suitChosenHard = bestSuitIndex;
					} else {
						tmpScore += findBestMove(whoseTurn, nextTurnIndex, cardsClone, curOnDiscard, drawPile, recDepth);
					}
					moves[i] = tmpScore;
					if (moves[i] < moves[minIndex]) {
						minIndex = i;
					}
				} else {
					//very high number so it is not chosen
					moves[i] = 30000;
				}
				cardsClone.get(whoseTurn).add(tmpCard);
			}

			//see how we do if we draw
			if (!drawPile.isEmpty() && moves[minIndex]>= 30000) {
				cardDrawn = drawPile.get(0);
				cardsClone.get(whoseTurn).add(cardDrawn);
				drawPile.remove(0);
				tmpScore = csc.calculateScoreDrawn(cardDrawn, whoseTurn);
				tmpScore += findBestMove(whoseTurn, nextTurnIndex, cardsClone, curOnDiscard, drawPile, recDepth);
				drawPile.add(0,cardDrawn);
				cardsClone.get(whoseTurn).remove(cardDrawn);
				moves[movesArraySize-1] = tmpScore;
				//if there is no card to play then draw.
				if (moves[movesArraySize-1] < moves[minIndex]) {
					minIndex = movesArraySize-1;
				}
			}

			if (minIndex < movesArraySize - 1) {
				cardSelected = players.get(whoseTurn).getCards().get(minIndex);

				if (!gameRules.checkCard(cardSelected, onDiscard)) {
					// should never get here, this would be an error.

					cardSelected = null;
				}
			} else {
				cardSelected = null;
			}
		}

		return cardSelected;
	}

	/**
	 * Finds the suit of the list with the maximum number of cards
	 * 
	 * @param cards
	 * @return
	 */
	public int getSuitChosen(int whoseTurn, List<Card> cards) {
		// Hard
		if (difficulty.equals(Constants.HARD) && suitChosenHard != -1) {
			return suitChosenHard;
		}

		// Easy and Medium
		return findMaxSuitIndex(whoseTurn, cards);
	}

	/**
	 * Finds the suit of the list with the maximum number of cards
	 * 
	 * @param cards
	 * @return
	 */
	public int findMaxSuitIndex(int whoseTurn, List<Card> cards) {
		int suits[] = new int[5];
		int maxSuitIndex = 0;

		for (Card c : cards) {
			// checks for 8s and jokers
			if (c.getValue() == C8Constants.EIGHT_CARD_NUMBER
					|| c.getSuit() == Constants.SUIT_JOKER) {
				continue;
			}
			// this gets the number of cards of each suit
			suits[c.getSuit()]++;
			if (suits[c.getSuit()] > suits[maxSuitIndex]) {
				maxSuitIndex = c.getSuit();
			}
		}

		return maxSuitIndex;
	}

	/**
	 * This function will find the best move for any player and return the score of how good of a move it is for that player
	 * @param playerIndex
	 * @param players
	 * @param onDiscard
	 * @param drawPile
	 * @param recursionDepth
	 * @return
	 */
	private double findBestMove(int whoseTurn, int playerIndex, List<List<Card>> cardsClone, Card curOnDiscard, List<Card> drawPile, int recDepth) {
		if (recDepth == 0) {
			return 0;
		}
		int suitToChoose = findMaxSuitIndex(whoseTurn, cardsClone.get(playerIndex));
		Card firstOnDiscard = curOnDiscard;
		double tmpScore = 0;
		Card cardDrawn = null;
		int movesArraySize = cardsClone.get(playerIndex).size() + 1;
		double[] moves = new double[movesArraySize];

		int nextTurnIndex = playerIndex;
		// Figure out whose turn it is next
		if (nextTurnIndex < game.getNumPlayers() - 1) {
			nextTurnIndex++;
		} else {
			nextTurnIndex = 0;
		}

		int maxIndex = 0;
		//rec call
		for (int i = 0; i < cardsClone.get(playerIndex).size(); i++) {
			curOnDiscard = firstOnDiscard;
			Card tmpCard = cardsClone.get(playerIndex).get(0);
			cardsClone.get(playerIndex).remove(0);
			if (gameRules.checkCard(tmpCard, curOnDiscard)) {
				tmpScore = csc.calculateScorePlayed(tmpCard, curOnDiscard, playerIndex);
				if (tmpScore >= 10000 || ((whoseTurn == playerIndex) && tmpScore <= -10000)) {
					//we can win with this player so game over.
					cardsClone.get(playerIndex).add(tmpCard);
					return tmpScore;
				}
				curOnDiscard = tmpCard;
				if (curOnDiscard.getValue() == C8Constants.EIGHT_CARD_NUMBER) {
					curOnDiscard = new Card(suitToChoose, curOnDiscard.getValue(), curOnDiscard.getResourceId(), curOnDiscard.getIdNum());
				}
				tmpScore += findBestMove(whoseTurn, nextTurnIndex, cardsClone, curOnDiscard, drawPile, recDepth-1);
				moves[i] = tmpScore;
				if (moves[i] > moves[maxIndex]) {
					maxIndex = i;
				}
			} else {
				if(whoseTurn == playerIndex) {
					//very high number so it is never chosen by current player
					moves[i] = 30000;
				} else {
					//very low number so it is never chosen by another player
					moves[i] = -30000;
				}
			}
			cardsClone.get(playerIndex).add(tmpCard);
		}

		// try drawing a card, only if there is a draw pile and there is not another card that can be played
		if (!drawPile.isEmpty() && (moves[maxIndex] >= 30000 || moves[maxIndex] <= -30000)) {
			cardDrawn = drawPile.get(0);
			cardsClone.get(playerIndex).add(cardDrawn);
			drawPile.remove(0);
			tmpScore = csc.calculateScoreDrawn(cardDrawn, playerIndex);
			tmpScore += findBestMove(whoseTurn, nextTurnIndex, cardsClone, curOnDiscard, drawPile, recDepth - 1);
			drawPile.add(0,cardDrawn);
			cardsClone.get(playerIndex).remove(cardDrawn);
			moves[movesArraySize-1] = tmpScore;
			if (moves[movesArraySize-1] > moves[maxIndex]) {
				maxIndex = movesArraySize-1;
			}
		} else {
			if (whoseTurn == playerIndex) {
				//very high number so it is never chosen by current player
				moves[movesArraySize-1] = 30000;
			} else {
				//very low number so it is never chosen by another player
				moves[movesArraySize-1] = -30000;
			}
		}

		if (whoseTurn == playerIndex) {
			// if this is the current player then we want the minimum not maximum.
			int minIndex = 0;
			for (int i = 0; i< movesArraySize; i++) {
				if (moves[i] < moves[minIndex]) {
					minIndex = i;
				}
			}

			return moves[minIndex];
		}

		return moves[maxIndex];
	}
}
