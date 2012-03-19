package cs309.a1.crazyeights;

import cs309.a1.shared.Rules;
import cs309.a1.shared.Card;
import cs309.a1.crazyeights.Constants;

public class CrazyEightGameRules implements Rules{
	
	public boolean checkCard(Card cardPlayed, Card onDiscard){
		//joker and 8 are always accepted
		if(cardPlayed.getSuit() == Constants.SUIT_JOKER || cardPlayed.getValue() == 8){
			return true;
			
		//must match suit or value
		}else if(cardPlayed.getSuit() == onDiscard.getSuit() 
				 || cardPlayed.getValue() == onDiscard.getValue() ){
			return true;
		}
		
		return false;
	}
	
}
