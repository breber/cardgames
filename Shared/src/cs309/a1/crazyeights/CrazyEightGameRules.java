package cs309.a1.crazyeights;

import cs309.a1.shared.Rules;
import cs309.a1.shared.Card;
import cs309.a1.crazyeights.Constants;

public class CrazyEightGameRules implements Rules{
	
	public boolean checkCard(Card cardPlayed, Card onDiscard){
		if(cardPlayed == null || onDiscard == null){
			return false;
		}
		//TODO need way to know what color the 8 changed it to
		
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
