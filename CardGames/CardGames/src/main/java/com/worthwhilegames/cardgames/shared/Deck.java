package com.worthwhilegames.cardgames.shared;

import static com.worthwhilegames.cardgames.shared.Constants.ACE_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.BLACK_JOKER_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.EIGHT_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.FIVE_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.FOUR_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.JACK_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.KING_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.NINE_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.QUEEN_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.RED_JOKER_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.SEVEN_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.SIX_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_CLUBS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_DIAMONDS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_HEARTS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_JOKER;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_SPADES;
import static com.worthwhilegames.cardgames.shared.Constants.TEN_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.THREE_VALUE;
import static com.worthwhilegames.cardgames.shared.Constants.TWO_VALUE;

import java.util.ArrayList;

import com.worthwhilegames.cardgames.R;

/**
 * Represents a deck of cards
 */
public class Deck {

    /**
     * Array of card image resource IDs
     */
    private ArrayList<Card> cardImages;

    /**
     * The type of game being played
     */
    private CardGame gameName;

    /**
     * Constructor to create a deck for the given card name
     *
     * @param name the name of the card game based on the enum value
     */
    public Deck(CardGame name) {
        this.gameName = name;
        cardImages = new ArrayList<Card>();
        fillArray();
    }

    /**
     * This method will return an ArrayList representing all of the cars
     * in the deck for the given game
     * @return an ArrayList of card objects for the given value
     */
    public ArrayList<Card> getCardIDs() {
        return cardImages;
    }

    /**
     * This method is used to retrieve the back of a card image
     *
     * @return the id of the image representing the back of a card
     */
    public int getCardBackID() {
        int cardId = 0;

        switch(gameName) {

        case CrazyEights:
            cardId = R.drawable.back_blue_1;
            break;
        case Euchre:
            cardId = R.drawable.back_blue_1;
            break;
        }

        return cardId;
    }

    /**
     * This method will fill the cardImages ArrayList with card objects for the
     * given game.
     *
     * 0 - Clubs
     * 1 - Diamond
     * 2 - Heart
     * 3 - Spade
     * 4 - Joker
     *
     * Each card has a number, 0-12 associated with it to represent which card it is.
     * Ace is number 0 and king is 12.
     */
    private void fillArray() {

        switch(gameName) {
        case CrazyEights:
            cardImages.add(new Card(SUIT_CLUBS, ACE_VALUE, 0));
            cardImages.add(new Card(SUIT_CLUBS, TWO_VALUE, 1));
            cardImages.add(new Card(SUIT_CLUBS, THREE_VALUE, 2));
            cardImages.add(new Card(SUIT_CLUBS, FOUR_VALUE, 3));
            cardImages.add(new Card(SUIT_CLUBS, FIVE_VALUE, 4));
            cardImages.add(new Card(SUIT_CLUBS, SIX_VALUE, 5));
            cardImages.add(new Card(SUIT_CLUBS, SEVEN_VALUE, 6));
            cardImages.add(new Card(SUIT_CLUBS, EIGHT_VALUE, 7));
            cardImages.add(new Card(SUIT_CLUBS, NINE_VALUE, 8));
            cardImages.add(new Card(SUIT_CLUBS, TEN_VALUE, 9));
            cardImages.add(new Card(SUIT_CLUBS, JACK_VALUE, 10));
            cardImages.add(new Card(SUIT_CLUBS, QUEEN_VALUE, 11));
            cardImages.add(new Card(SUIT_CLUBS, KING_VALUE, 12));
            cardImages.add(new Card(SUIT_DIAMONDS, ACE_VALUE, 13));
            cardImages.add(new Card(SUIT_DIAMONDS, TWO_VALUE, 14));
            cardImages.add(new Card(SUIT_DIAMONDS, THREE_VALUE, 15));
            cardImages.add(new Card(SUIT_DIAMONDS, FOUR_VALUE, 16));
            cardImages.add(new Card(SUIT_DIAMONDS, FIVE_VALUE, 17));
            cardImages.add(new Card(SUIT_DIAMONDS, SIX_VALUE, 18));
            cardImages.add(new Card(SUIT_DIAMONDS, SEVEN_VALUE, 19));
            cardImages.add(new Card(SUIT_DIAMONDS, EIGHT_VALUE, 20));
            cardImages.add(new Card(SUIT_DIAMONDS, NINE_VALUE, 21));
            cardImages.add(new Card(SUIT_DIAMONDS, TEN_VALUE, 22));
            cardImages.add(new Card(SUIT_DIAMONDS, JACK_VALUE, 23));
            cardImages.add(new Card(SUIT_DIAMONDS, QUEEN_VALUE, 24));
            cardImages.add(new Card(SUIT_DIAMONDS, KING_VALUE, 25));
            cardImages.add(new Card(SUIT_HEARTS, ACE_VALUE, 26));
            cardImages.add(new Card(SUIT_HEARTS, TWO_VALUE, 27));
            cardImages.add(new Card(SUIT_HEARTS, THREE_VALUE, 28));
            cardImages.add(new Card(SUIT_HEARTS, FOUR_VALUE, 29));
            cardImages.add(new Card(SUIT_HEARTS, FIVE_VALUE, 30));
            cardImages.add(new Card(SUIT_HEARTS, SIX_VALUE, 31));
            cardImages.add(new Card(SUIT_HEARTS, SEVEN_VALUE, 32));
            cardImages.add(new Card(SUIT_HEARTS, EIGHT_VALUE, 33));
            cardImages.add(new Card(SUIT_HEARTS, NINE_VALUE, 34));
            cardImages.add(new Card(SUIT_HEARTS, TEN_VALUE, 35));
            cardImages.add(new Card(SUIT_HEARTS, JACK_VALUE, 36));
            cardImages.add(new Card(SUIT_HEARTS, QUEEN_VALUE, 37));
            cardImages.add(new Card(SUIT_HEARTS, KING_VALUE, 38));
            cardImages.add(new Card(SUIT_SPADES, ACE_VALUE, 39));
            cardImages.add(new Card(SUIT_SPADES, TWO_VALUE, 40));
            cardImages.add(new Card(SUIT_SPADES, THREE_VALUE, 41));
            cardImages.add(new Card(SUIT_SPADES, FOUR_VALUE, 42));
            cardImages.add(new Card(SUIT_SPADES, FIVE_VALUE, 43));
            cardImages.add(new Card(SUIT_SPADES, SIX_VALUE, 44));
            cardImages.add(new Card(SUIT_SPADES, SEVEN_VALUE, 45));
            cardImages.add(new Card(SUIT_SPADES, EIGHT_VALUE, 46));
            cardImages.add(new Card(SUIT_SPADES, NINE_VALUE, 47));
            cardImages.add(new Card(SUIT_SPADES, TEN_VALUE, 48));
            cardImages.add(new Card(SUIT_SPADES, JACK_VALUE, 49));
            cardImages.add(new Card(SUIT_SPADES, QUEEN_VALUE, 50));
            cardImages.add(new Card(SUIT_SPADES, KING_VALUE, 51));
            cardImages.add(new Card(SUIT_JOKER, BLACK_JOKER_VALUE, 52));
            cardImages.add(new Card(SUIT_JOKER, RED_JOKER_VALUE, 53));
            break;

        case Euchre:
            cardImages.add(new Card(SUIT_CLUBS, ACE_VALUE, 0));
            cardImages.add(new Card(SUIT_CLUBS, NINE_VALUE, 8));
            cardImages.add(new Card(SUIT_CLUBS, TEN_VALUE, 9));
            cardImages.add(new Card(SUIT_CLUBS, JACK_VALUE, 10));
            cardImages.add(new Card(SUIT_CLUBS, QUEEN_VALUE, 11));
            cardImages.add(new Card(SUIT_CLUBS, KING_VALUE, 12));
            cardImages.add(new Card(SUIT_DIAMONDS, ACE_VALUE, 13));
            cardImages.add(new Card(SUIT_DIAMONDS, NINE_VALUE, 21));
            cardImages.add(new Card(SUIT_DIAMONDS, TEN_VALUE, 22));
            cardImages.add(new Card(SUIT_DIAMONDS, JACK_VALUE, 23));
            cardImages.add(new Card(SUIT_DIAMONDS, QUEEN_VALUE, 24));
            cardImages.add(new Card(SUIT_DIAMONDS, KING_VALUE, 25));
            cardImages.add(new Card(SUIT_HEARTS, ACE_VALUE, 26));
            cardImages.add(new Card(SUIT_HEARTS, NINE_VALUE, 34));
            cardImages.add(new Card(SUIT_HEARTS, TEN_VALUE, 35));
            cardImages.add(new Card(SUIT_HEARTS, JACK_VALUE, 36));
            cardImages.add(new Card(SUIT_HEARTS, QUEEN_VALUE, 37));
            cardImages.add(new Card(SUIT_HEARTS, KING_VALUE, 38));
            cardImages.add(new Card(SUIT_SPADES, ACE_VALUE, 39));
            cardImages.add(new Card(SUIT_SPADES, NINE_VALUE, 47));
            cardImages.add(new Card(SUIT_SPADES, TEN_VALUE, 48));
            cardImages.add(new Card(SUIT_SPADES, JACK_VALUE, 49));
            cardImages.add(new Card(SUIT_SPADES, QUEEN_VALUE, 50));
            cardImages.add(new Card(SUIT_SPADES, KING_VALUE, 51));
            break;
        }
    }
}

