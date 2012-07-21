package com.worthwhilegames.cardgames.euchre;

import static com.worthwhilegames.cardgames.shared.Constants.JACK_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_CLUBS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_DIAMONDS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_HEARTS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_SPADES;

import java.util.ArrayList;
import java.util.List;

import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Rules;

public class EuchreGameRules implements Rules{

	/**
	 * Not used for this game type
	 */
	@Override
	public boolean checkCard(Card cardPlayed, Card onDiscard) {
		return false;
	}

	/**
	 * 
	 */
	@Override
	public boolean checkCard(Card cardPlayed, int trump, int suitLed, List<Card> cards) {

		List<Card> cardsPlayed = new ArrayList<Card>();
		cardsPlayed.add(cardPlayed);

		//TODO what if a left bower is played!!! it don't work.
		adjustJacks(cards, trump);
		adjustJacks(cardsPlayed, trump);

		boolean canFollowSuit = false;

		int numCards = cards.size();

		for(int i = 0; i < numCards; i++){
			Card card = cards.get(i);
			if(suitLed == card.getSuit()){
				canFollowSuit = true;
			}
		}

		boolean returnValue = false;



		if(!canFollowSuit){
			returnValue = true;
		}else if(cardPlayed.getSuit() == suitLed){
			returnValue = true;
		}else if(cardPlayed.getSuit() == 99){

			returnValue = true;
		}

		revertJacks(cards, trump);
		revertJacks(cardsPlayed, trump);

		return returnValue;
	}

	public void adjustJacks(List<Card> cards , int trump){

		int numCards = cards.size();

		Card card;

		for(int i = 0; i < numCards; i++){

			card = cards.get(i);

			switch(trump){
			case SUIT_CLUBS:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_SPADES){
					card.setSuit(SUIT_CLUBS);
					card.setValue(99);
				}
				break;

			case SUIT_DIAMONDS:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_HEARTS){
					card.setSuit(SUIT_DIAMONDS);
					card.setValue(99);
				}
				break;

			case SUIT_HEARTS:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_DIAMONDS){
					card.setSuit(SUIT_HEARTS);
					card.setValue(99);
				}
				break;

			case SUIT_SPADES:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_CLUBS){
					card.setSuit(SUIT_SPADES);
					card.setValue(99);
				}

				break;
			}
		}
	}

	public void revertJacks(List<Card> cards, int trump){

		int numCards = cards.size();

		Card card;

		for(int i = 0; i < numCards; i++){

			card = cards.get(i);

			switch(trump){
			case SUIT_CLUBS:
				if(card.getValue() == 99 && card.getSuit() == SUIT_CLUBS){
					card.setSuit(SUIT_SPADES);
					card.setValue(JACK_VALUE);
				}
				break;

			case SUIT_DIAMONDS:
				if(card.getValue() == 99 && card.getSuit() == SUIT_DIAMONDS){
					card.setSuit(SUIT_HEARTS);
					card.setValue(JACK_VALUE);
				}
				break;

			case SUIT_HEARTS:
				if(card.getValue() == 99 && card.getSuit() == SUIT_HEARTS){
					card.setSuit(SUIT_DIAMONDS);
					card.setValue(JACK_VALUE);
				}
				break;

			case SUIT_SPADES:
				if(card.getValue() == 99 && card.getSuit() == SUIT_SPADES){
					card.setSuit(SUIT_CLUBS);
					card.setValue(JACK_VALUE);
				}

				break;
			}
		}
	}
}
