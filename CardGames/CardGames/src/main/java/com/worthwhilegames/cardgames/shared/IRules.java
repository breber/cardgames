package com.worthwhilegames.cardgames.shared;

/**
 * An interface that defines which card can be played next
 * based on the current top discard pile card
 */
public interface IRules {

    /**
     * This method will check to see if a play is valid for the current game type
     *
     * @param cardPlayed the card that is played by a user
     * @param onDiscard the top of the discard pile in which to compare
     * @return true if the play is valid and false otherwise
     */
    public boolean checkCard(Card cardPlayed, Card onDiscard);

}
