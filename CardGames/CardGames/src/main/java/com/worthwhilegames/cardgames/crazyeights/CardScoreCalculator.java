package com.worthwhilegames.cardgames.crazyeights;

import java.util.List;

import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;


public class CardScoreCalculator {
    
    
    
    private int playerIndex;
    
    private List<List<Card>> cards;
    
    private int numPlayers=1;
    
    public CardScoreCalculator(int playerIndexGiven, List<List<Card>> cardsGiven){
        playerIndex = playerIndexGiven;
        cards = cardsGiven;
        numPlayers = cardsGiven.size();
        
    }
    
    /**
     * this will calculate the score for the card played on the discard pile
     * @param played the card played
     * @param onDiscard the card on the discard before the play
     * @param player the index of the player that played
     * @return score
     * the more negative the score is the better for the player who is doing the analysis
     * the higher the score the worse it is for the 
     */
    public double calculateScorePlayed(Card played, Card onDiscard, int player){
        int numCards = cards.get(player).size();
        double numCardsRatio = 1;    
        if(numCards != 0){
            numCardsRatio = C8Constants.NUMBER_OF_CARDS_PER_HAND*2.0/(numCards);            
        }
        
        if(playerIndex == player){
            //this is the person who is playing

            if(numCards == 0){
                //you are going to win, play it!
                return -10000;
            }
            
            //special card
            if(played.getValue() == C8Constants.EIGHT_CARD_NUMBER || played.getSuit() == Constants.SUIT_JOKER){
                return -1 * numCardsRatio * numPlayers;
            }
            //same suit
            if(played.getSuit() == onDiscard.getSuit()){
                if(isMaxSuitForPlayer(player, played.getSuit())){
                    return -3 * numCardsRatio * numPlayers;
                }
                return -2 * numCardsRatio * numPlayers;
            }
            //same index
            if(played.getValue() == onDiscard.getValue()){
                if(isMaxSuitForPlayer(player, played.getSuit())){
                    return -3 * numCardsRatio * numPlayers;
                }
                return -2 * numCardsRatio * numPlayers;
            }
            
        } else {
            //this is a move by another player
            
            if (numCards == 0){
                //you are not going to win
                return 10000;
            }
            //special card
            if(played.getValue() == C8Constants.EIGHT_CARD_NUMBER || played.getSuit() == Constants.SUIT_JOKER){
                return 1 * numCardsRatio; 
            }
            //same suit
            if(played.getSuit() == onDiscard.getSuit()){
                if(isMaxSuitForPlayer(player, played.getSuit())){
                    return 3 * numCardsRatio;
                }
                return 2 * numCardsRatio;
            }
            //same index
            if(played.getValue() == onDiscard.getValue()){
                if(isMaxSuitForPlayer(player, played.getSuit())){
                    return 3 * numCardsRatio;
                }
                return 2 * numCardsRatio;
            }
            
        }
        
        return 1.0;
    }
    
    public double calculateScoreDrawn(Card drawn, int player){
        int numCards = cards.get(player).size();
        double numCardsRatio = 1;    
        if(numCards != 0){
            numCardsRatio = C8Constants.NUMBER_OF_CARDS_PER_HAND*2.0/(numCards);            
        }
        
        if(playerIndex == player){
            //this is the person who is playing
            if(drawn.getValue() == C8Constants.EIGHT_CARD_NUMBER || drawn.getSuit() == Constants.SUIT_JOKER){
                return 3 * numCardsRatio * numPlayers;
            }
            return 4 * numCardsRatio * numPlayers;
        } else {
            //this is a move by another player
            if(drawn.getValue() == C8Constants.EIGHT_CARD_NUMBER || drawn.getSuit() == Constants.SUIT_JOKER){
                return -3 * numCardsRatio;
            }
            return -4 * numCardsRatio;
        }
    }
    
    private boolean isMaxSuitForPlayer(int player, int suit){
        int suits[] = new int[5];
        int maxSuitIndex = 0;

        for (Card c : cards.get(player)) {
            //checks for 8s and jokers
            if( (c.getValue() == C8Constants.EIGHT_CARD_NUMBER || c.getSuit() == Constants.SUIT_JOKER) ){
                suits[4]++;
                continue;
            }

            //this gets the number of cards of each suit
            suits[c.getSuit()]++;
            if (suits[c.getSuit()] > suits[maxSuitIndex]) {
                maxSuitIndex = c.getSuit();
            }
        }
        return maxSuitIndex == suit;
    }
    
}
