package cs309.a1.crazyeights;

import cs309.a1.shared.Card;
import cs309.a1.shared.Constants;
import cs309.a1.shared.Rules;

public class CrazyEightGameRules implements Rules{
	
	/**
	 * This method will check to see if a play is valid
	 * 
	 * @param cardPlayed the card which is trying to be discarded by a player
	 * @param onDiscard the last card discarded in the game
	 * @return true if the card played on the discard pile is valid else false
	 */
	public boolean checkCard(Card cardPlayed, Card onDiscard){
		
		//check to see if a card is null or the discard pile is null
		if(cardPlayed == null || onDiscard == null){
			return false;
		}
		
		//joker and 8 are always accepted
		if(cardPlayed.getSuit() == Constants.SUIT_JOKER || cardPlayed.getValue() == 7){ //this 7 is the card value of an 8
			return true;
		}
		
		//8 is played and suit is correct
		if(onDiscard.getValue() ==7 && onDiscard.getSuit() == cardPlayed.getSuit()){
			return true;
		}
		
		//anything can be played on a joker
		if(onDiscard.getSuit() == Constants.SUIT_JOKER){
			return true;
		}
			
		//must match suit or value
		if(cardPlayed.getSuit() == onDiscard.getSuit() 
				 || cardPlayed.getValue() == onDiscard.getValue() ){
			return true;
		}
		
		return false;
	}
	
}
