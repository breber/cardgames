package com.worthwhilegames.cardgames.crazyeights;

import android.util.Log;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.worthwhilegames.cardgames.shared.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A class for keeping track of the logic and game state for the game type crazy eights
 */
public class CrazyEightsGame implements Game {

    /**
     * A tag for the class name
     */
    private static final String TAG = CrazyEightsGame.class.getName();

    /**
     * A private variable for a list of players in the current game
     */
    private List<Player> players;

    /**
     * A list of all the cards in the shuffle deck
     */
    private List<Card> shuffledDeck;

    /**
     * A list of all the cards in the discard pile
     */
    private List<Card> discardPile;

    /**
     * The turn based match from Google games
     */
    private TurnBasedMatch mTurnBasedMatch;

    /**
     * String id for the current player
     */
    private String mPlayerId;

    /**
     * Extra value for the suit
     */
    private int mSuitExtra = C8Constants.PLAY_SUIT_NONE;

    /**
     * A constructor for the crazy eights game type. This constructor will initialize the all the variables
     * for a game of crazy eights including the rules, players, deck, shuffled deck pile and the discard pile.
     */
    public CrazyEightsGame(TurnBasedMatch turnBasedMatch, String playerId) {
        players = new ArrayList<Player>();
        Deck gameDeck = new Deck(CardGame.CrazyEights);
        shuffledDeck = gameDeck.getCardIDs();
        discardPile = new ArrayList<Card>();
        mTurnBasedMatch = turnBasedMatch;
        mPlayerId = playerId;
    }

    /* (non-Javadoc)
     * @see com.worthwhilegames.cardgames.shared.Game#getPlayers()
     */
    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public Player getSelf() {
        String currentParticipantId = mTurnBasedMatch.getParticipantId(mPlayerId);
        for (Player p : getPlayers()) {
            if (p.getId().equals(currentParticipantId)) {
                return p;
            }
        }

        return null;
    }

    @Override
    public boolean isMyTurn() {
        return (mTurnBasedMatch.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);
    }

    @Override
    public IRules getRules() {
        return new CrazyEightGameRules();
    }

    @Override
    public int getDisplaySuit() {
        if (mSuitExtra != C8Constants.PLAY_SUIT_NONE) {
            return mSuitExtra;
        }

        return getDiscardPileTop().getSuit();
    }

    /* (non-Javadoc)
     * @see com.worthwhilegames.cardgames.shared.Game#setup()
     */
    @Override
    public void setup() {
        // Shuffle the card ID's
        shuffleDeck();

        // Deal the initial cards to all the players in the game
        deal();

        // Discard pile first one
        discardPile.add(shuffledDeck.get(0));

        // Remove the last card returned by iter.next()
        shuffledDeck.remove(0);
    }

    /* (non-Javadoc)
     * @see com.worthwhilegames.cardgames.shared.Game#shuffleDeck()
     */
    @Override
    public void shuffleDeck() {
        //create a random number generator
        Random generator = new Random();

        //shuffle the deck
        Collections.shuffle(shuffledDeck, generator);
    }

    /**
     * This method will shuffle the discard pile and replace the current draw
     * pile with the old discard pile. After this method call the shuffled deck
     * will only have the top card remaining.
     */
    public void shuffleDiscardPile() {
        Card card = discardPile.remove(discardPile.size() - 1);

        // Make copy of discard pile to be new shuffled deck
        // add to the shuffled deck in case there are still
        // some cards left that are unaccounted for
        shuffledDeck.addAll(discardPile);

        if (Util.isDebugBuild()) {
            Log.d(TAG, "shuffleDiscardPile: shuffledDeck: " + shuffledDeck.size() + " - discardPile: " + discardPile.size());
        }

        // Remove all the cards from the discard pile
        discardPile.removeAll(discardPile);

        // Place the last card discarded back on the discard pile
        discardPile.add(card);

        // Shuffle the deck
        shuffleDeck();
    }

    /* (non-Javadoc)
     * @see com.worthwhilegames.cardgames.shared.Game#deal()
     */
    @Override
    public void deal() {
        if (Util.isDebugBuild()) {
            Log.d(TAG, "deal: numberOfPlayers: " + players.size());

            for (Player p : players) {
                Log.d(TAG, "pre deal: player[" + p.getId() + "] has " + p.getNumCards() + " cards");
                Log.d(TAG, "          player[" + p.getId() + "]: " + p);
            }
        }

        // Deal the given number of cards to each player
        for (int i = 0; i < 5; i++) {
            for (Player p : players) {
                draw(p);

                if (Util.isDebugBuild()) {
                    Log.d(TAG, "p.addCard: player[" + p.getId() + "] has " + p.getNumCards() + " cards");
                }
            }
        }

        if (Util.isDebugBuild()) {
            for (Player p : players) {
                Log.d(TAG, "postdeal: player[" + p.getId() + "] has " + p.getNumCards() + " cards");
                Log.d(TAG, "          player[" + p.getId() + "]: " + p);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.worthwhilegames.cardgames.shared.Game#discard(com.worthwhilegames.cardgames.shared.Player, com.worthwhilegames.cardgames.shared.Card)
     */
    @Override
    public void discard(Card card) {
        Player p = getSelf();
        if (Util.isDebugBuild()) {
            Log.d(TAG, "prediscard: player[" + p.getId() + "] has " + p.getNumCards() + " cards");
            Log.d(TAG, "            player[" + p.getId() + "]: " + p);
        }

        discardPile.add(card);
        p.removeCard(card);
        mSuitExtra = C8Constants.PLAY_SUIT_NONE;

        if (Util.isDebugBuild()) {
            Log.d(TAG, "postdiscard: player[" + p.getId() + "] has " + p.getNumCards() + " cards");
            Log.d(TAG, "             player[" + p.getId() + "]: " + p);
        }
    }

    public void discard(Card card, int extraSuit) {
        discard(card);
        mSuitExtra = extraSuit;
    }

    /* (non-Javadoc)
     * @see com.worthwhilegames.cardgames.shared.Game#isGameOver()
     */
    @Override
    public boolean isGameOver() {
        // check to see if any player has any cards left
        for (Player p : getPlayers()) {
            if (p.getCards().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.worthwhilegames.cardgames.shared.Game#draw()
     */
    @Override
    public Card draw() {
        return draw(getSelf());
    }

    private Card draw(Player p) {
        if (Util.isDebugBuild()) {
            Log.d(TAG, "predraw: player[" + p.getId() + "] has " + p.getNumCards() + " cards");
            Log.d(TAG, "         player[" + p.getId() + "]: " + p);
        }

        if (shuffledDeck.isEmpty()) {
            shuffleDiscardPile();
        }

        if (shuffledDeck.isEmpty()) {
            return null;
        }

        // Get a card out of the shuffled pile and add to the players hand
        Card card = shuffledDeck.get(0);
        p.addCard(card);

        // Remove the last card returned by iter.next()
        shuffledDeck.remove(0);

        // Shuffle the deck if the player drew the last card
        if (shuffledDeck.isEmpty()) {
            shuffleDiscardPile();
        }

        if (Util.isDebugBuild()) {
            Log.d(TAG, "postdraw: player[" + p.getId() + "] has " + p.getNumCards() + " cards");
            Log.d(TAG, "          player[" + p.getId() + "]: " + p);
        }

        return card;
    }

    /* (non-Javadoc)
     * @see com.worthwhilegames.cardgames.shared.Game#getDiscardPileTop()
     */
    @Override
    public Card getDiscardPileTop() {
        if (!discardPile.isEmpty()) {
            return discardPile.get(discardPile.size() - 1);
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.worthwhilegames.cardgames.shared.Game#getNumPlayers()
     */
    @Override
    public int getNumPlayers() {
        return players.size();
    }

    /* (non-Javadoc)
     * @see com.worthwhilegames.cardgames.shared.Game#addPlayer(com.worthwhilegames.cardgames.shared.Player)
     */
    @Override
    public void addPlayer(Player p) {
        players.add(p);
    }

    @Override
    public byte[] persist() {
        JSONObject toRet = new JSONObject();

        try {
            JSONArray players = new JSONArray();
            for (Player p : getPlayers()) {
                players.put(p.toJSONObject());
            }

            toRet.put("players", players);

            JSONArray shuffled = new JSONArray();
            for (Card c : shuffledDeck) {
                shuffled.put(c.toJSONObject());
            }
            toRet.put("shuffled", shuffled);

            JSONArray discard = new JSONArray();
            for (Card c : discardPile) {
                discard.put(c.toJSONObject());
            }
            toRet.put("discard", discard);

            toRet.put("extra_suit", mSuitExtra);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Util.isDebugBuild()) {
            Log.d(TAG, "persist: " + toRet.toString());
        }

        return toRet.toString().getBytes();
    }

    @Override
    public boolean load(byte[] state) {
        if (state == null) {
            return false;
        }

        String stringState = new String(state);

        if (Util.isDebugBuild()) {
            Log.d(TAG, "load: " + stringState);
        }

        try {
            JSONObject obj = new JSONObject(stringState);

            JSONArray players = obj.getJSONArray("players");
            for (int i = 0; i < players.length(); i++) {
                addPlayer(new Player(players.getJSONObject(i)));
            }

            JSONArray shuffled = obj.getJSONArray("shuffled");
            for (int i = 0; i < shuffled.length(); i++) {
                shuffledDeck.add(new Card(shuffled.getJSONObject(i)));
            }

            JSONArray discard = obj.getJSONArray("discard");
            for (int i = 0; i < discard.length(); i++) {
                discardPile.add(new Card(discard.getJSONObject(i)));
            }

            mSuitExtra = obj.getInt("extra_suit");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Card getCardAtPosition(int position) {
        if (position == 2) {
            return this.getDiscardPileTop();
        } else if (position == 4) {
            // TODO customizable back
            return new Card();
        }
        return null;
    }
}
