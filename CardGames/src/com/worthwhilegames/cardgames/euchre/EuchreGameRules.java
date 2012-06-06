package com.worthwhilegames.cardgames.euchre;

import java.util.List;

import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Player;
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
	public boolean checkCard(Card cardPlayed, int trump, int suitLed, Player player) {
		boolean canFollowSuit = false;
		
		int numCards = player.getNumCards();
		
		List<Card> cards = player.getCards();
		
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
		
		return false;
	}

}
