package com.worthwhilegames.cardgames.euchre;

import static com.worthwhilegames.cardgames.euchre.EuchreConstants.CHANGED_JACK_SUIT_LEFT;
import static com.worthwhilegames.cardgames.shared.Constants.JACK_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_CLUBS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_DIAMONDS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_HEARTS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_SPADES;

import java.util.ArrayList;
import java.util.List;

import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Rules;

public class EuchreGameRules implements Rules {

    /**
     * Not used for this game type
     */
    @Override
    public boolean checkCard(Card cardPlayed, Card onDiscard) {
        return false;
    }

    /**
     * This method will check a card to see if it can be played or not.
     * 
     * @param cardPlayed
     *            the card the user decided to play
     * @param cardLed
     *            the lead card played in the hand
     * @param cards
     *            a list of cards in the user's hand
     * @param trump
     *            the trump of the current round
     * 
     * @return true if the card played is valid. False otherwise.
     */
    @Override
    public boolean checkCard(Card cardPlayed, int trump, Card cardLed, List<Card> cards) {
        List<Card> cardsPlayed = new ArrayList<Card>();
        cardsPlayed.add(cardPlayed);
        cardsPlayed.add(cardLed);

        adjustJacks(cards, trump);
        adjustJacks(cardsPlayed, trump);

        boolean canFollowSuit = false;

        int numCards = cards.size();

        for (int i = 0; i < numCards; i++) {
            Card card = cards.get(i);
            if (cardLed.getSuit() == card.getSuit()) {
                canFollowSuit = true;
            }
        }

        boolean returnValue = false;

        if (!canFollowSuit) {
            returnValue = true;
        } else if (cardPlayed.getSuit() == cardLed.getSuit()) {
            returnValue = true;
        } else if (cardPlayed.getSuit() == CHANGED_JACK_SUIT_LEFT) {
            returnValue = true;
        }

        revertJacks(cards, trump);
        revertJacks(cardsPlayed, trump);

        return returnValue;
    }

    /**
     * This method will adjust the jacks to make sure that the left/right jack
     * can be distinguished and when a card is checked for validity the check
     * card method returns true with the left/right jack.
     * 
     * @param cards
     *            a list of cards to adjust to new values based on the trump
     * @param trump
     *            the trump of the current round
     */
    public void adjustJacks(List<Card> cards, int trump) {
        int numCards = cards.size();

        Card card;
        for (int i = 0; i < numCards; i++) {
            card = cards.get(i);

            switch (trump) {
            case SUIT_CLUBS:
                if (card.getValue() == JACK_VALUE && card.getSuit() == SUIT_SPADES) {
                    card.setSuit(SUIT_CLUBS);
                    card.setValue(CHANGED_JACK_SUIT_LEFT);
                }
                break;

            case SUIT_DIAMONDS:
                if (card.getValue() == JACK_VALUE && card.getSuit() == SUIT_HEARTS) {
                    card.setSuit(SUIT_DIAMONDS);
                    card.setValue(CHANGED_JACK_SUIT_LEFT);
                }
                break;

            case SUIT_HEARTS:
                if (card.getValue() == JACK_VALUE && card.getSuit() == SUIT_DIAMONDS) {
                    card.setSuit(SUIT_HEARTS);
                    card.setValue(CHANGED_JACK_SUIT_LEFT);
                }
                break;

            case SUIT_SPADES:
                if (card.getValue() == JACK_VALUE && card.getSuit() == SUIT_CLUBS) {
                    card.setSuit(SUIT_SPADES);
                    card.setValue(CHANGED_JACK_SUIT_LEFT);
                }

                break;
            }
        }
    }

    /**
     * This method will revert the jacks back to their original suit/value if
     * they were changed.
     * 
     * @param cards
     *            the list of cards to revert
     * @param trump
     *            the trump of the current round.
     */
    public void revertJacks(List<Card> cards, int trump) {
        int numCards = cards.size();

        Card card;
        for (int i = 0; i < numCards; i++) {
            card = cards.get(i);

            switch (trump) {
            case SUIT_CLUBS:
                if (card.getValue() == CHANGED_JACK_SUIT_LEFT && card.getSuit() == SUIT_CLUBS) {
                    card.setSuit(SUIT_SPADES);
                    card.setValue(JACK_VALUE);
                }
                break;

            case SUIT_DIAMONDS:
                if (card.getValue() == CHANGED_JACK_SUIT_LEFT && card.getSuit() == SUIT_DIAMONDS) {
                    card.setSuit(SUIT_HEARTS);
                    card.setValue(JACK_VALUE);
                }
                break;

            case SUIT_HEARTS:
                if (card.getValue() == CHANGED_JACK_SUIT_LEFT && card.getSuit() == SUIT_HEARTS) {
                    card.setSuit(SUIT_DIAMONDS);
                    card.setValue(JACK_VALUE);
                }
                break;

            case SUIT_SPADES:
                if (card.getValue() == CHANGED_JACK_SUIT_LEFT && card.getSuit() == SUIT_SPADES) {
                    card.setSuit(SUIT_CLUBS);
                    card.setValue(JACK_VALUE);
                }

                break;
            }
        }
    }
}
