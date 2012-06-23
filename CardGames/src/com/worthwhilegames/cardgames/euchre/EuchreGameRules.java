package com.worthwhilegames.cardgames.euchre;

import static com.worthwhilegames.cardgames.shared.Constants.JACK_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_CLUBS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_DIAMONDS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_HEARTS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_SPADES;

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

		adjustJacks(cards, trump);

		boolean canFollowSuit = false;

		int numCards = cards.size();

		for(int i = 0; i < numCards; i++){
			Card card = cards.get(i);
			if(suitLed == card.getSuit()){
				canFollowSuit = true;
			}
		}

		if(!canFollowSuit){
			return true;
		}else if(cardPlayed.getSuit() == suitLed){
			return true;
		}

		revertJacks(cards, trump);

		return false;
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
				}
				break;

			case SUIT_DIAMONDS:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_HEARTS){
					card.setSuit(SUIT_DIAMONDS);
				}
				break;

			case SUIT_HEARTS:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_DIAMONDS){
					card.setSuit(SUIT_HEARTS);
				}
				break;

			case SUIT_SPADES:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_CLUBS){
					card.setSuit(SUIT_SPADES);
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
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_CLUBS){
					card.setSuit(SUIT_SPADES);
				}
				break;

			case SUIT_DIAMONDS:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_DIAMONDS){
					card.setSuit(SUIT_HEARTS);
				}
				break;

			case SUIT_HEARTS:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_HEARTS){
					card.setSuit(SUIT_DIAMONDS);
				}
				break;

			case SUIT_SPADES:
				if(card.getValue() == JACK_VALUE && card.getSuit() == SUIT_SPADES){
					card.setSuit(SUIT_CLUBS);
				}

				break;
			}
		}
	}
}
