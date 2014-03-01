package com.worthwhilegames.cardgames.crazyeights;

import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.IRules;

/**
 * An implementation of the IRules interface
 * for the game Crazy Eights.
 */
public class CrazyEightGameRules implements IRules {

    /**
     * This method will check to see if a play is valid
     *
     * @param cardPlayed the card which is trying to be discarded by a player
     * @param onDiscard the last card discarded in the game
     * @return true if the card played on the discard pile is valid else false
     */
    @Override
    public boolean checkCard(Card cardPlayed, Card onDiscard) {
        // check to see if a card is null or the discard pile is null
        if (cardPlayed == null || onDiscard == null) {
            return false;
        }

        // joker and 8 are always accepted
        if (cardPlayed.getSuit() == Constants.SUIT_JOKER
                || cardPlayed.getValue() == C8Constants.EIGHT_CARD_NUMBER) {
            return true;
        }

        // 8 is played and suit is correct
        if (onDiscard.getValue() == C8Constants.EIGHT_CARD_NUMBER
                && onDiscard.getSuit() == cardPlayed.getSuit()) {
            return true;
        }

        // anything can be played on a joker
        if (onDiscard.getSuit() == Constants.SUIT_JOKER) {
            return true;
        }

        // must match suit or value
        if (cardPlayed.getSuit() == onDiscard.getSuit()
                || cardPlayed.getValue() == onDiscard.getValue()) {
            return true;
        }

        return false;
    }

}
