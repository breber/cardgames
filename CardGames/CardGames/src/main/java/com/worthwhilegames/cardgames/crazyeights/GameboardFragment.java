package com.worthwhilegames.cardgames.crazyeights;

import android.app.Fragment;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.*;

import java.util.List;

/**
 * Created by breber on 2/2/14.
 */
public class GameboardFragment extends Fragment {

    /**
     * A constant to scale a card to the full card size
     */
    private static final int mFullCard = 1;

    /**
     * A constant to scale a card to half the card size horizontally cut
     */
    private static final int cHalfCard = 2;

    /**
     * A constant to scale a card to half of the card size vertically cut
     */
    private static final int cHalfCardVertCut = 3;

    /**
     * A constant to scale a card to a fourth of the size
     */
    private static final int cFourthCard = 4;

    /**
     * LayoutParams for adding a card to a player on the long edge of the screen
     *
     * width  = WRAP_CONTENT
     * height = cardHeight
     */
    private static LinearLayout.LayoutParams cardParams;

    /**
     * The height of each card
     */
    private static int cardHeight;

    /**
     * The height of each button
     */
    private static int buttonHeight;

    /**
     * Represents the resource id to use for the back of the cards
     */
    private static int CARD_BACK;

    /**
     * The maximum number of cards displayed for each player
     */
    private static int[] maxDisplayed = new int[] { Constants.MAX_DISPLAYED, Constants.MAX_DIS_SIDES, Constants.MAX_DISPLAYED, Constants.MAX_DIS_SIDES };

    /**
     * Holds the scaled Bitmaps of the suit images
     */
    private static Bitmap[] scaledSuitImages = new Bitmap[4];

    /**
     * These are the TextViews for all the player names
     */
    private TextView[] playerTextViews = new TextView[4];

    /**
     * These are the LinearLayouts for all the player cards
     */
    private LinearLayout[] playerLinearLayouts = new LinearLayout[4];

    /**
     * These are the TextViews for the count of remaining cards not being displayed
     */
    private TextView[] playerRemainingCards = new TextView[4];

    /**
     * The ImageViews for the cards in the center of the screen
     *
     * For games that don't use 4 cards in the middle:
     * Position 2 = discard pile
     * Position 4 = draw pile
     */
    private ImageView[] centerCards = new ImageView[4];

    /**
     * The current suit ImageView
     */
    private ImageView suitView;

    /**
     * The current game
     */
    private Game mGame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.gameboard, container, false);
        initUIElements(v);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFERENCES, 0);

        // Get the image to use for the back of a card
        CARD_BACK = sharedPreferences.getInt(Constants.PREF_CARD_BACK, R.drawable.back_blue_1);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUi(mGame);
    }

    private View findViewById(int id) {
        return getView().findViewById(id);
    }

    /**
     * Set up all the references to UI elements
     */
    private void initUIElements(View view) {
        // Get references to commonly used UI elements
        playerTextViews[0] = (TextView) view.findViewById(R.id.player1text);
        playerTextViews[1] = (TextView) view.findViewById(R.id.player2text);
        playerTextViews[2] = (TextView) view.findViewById(R.id.player3text);
        playerTextViews[3] = (TextView) view.findViewById(R.id.player4text);

        playerLinearLayouts[0] = (LinearLayout) view.findViewById(R.id.player1ll);
        playerLinearLayouts[1] = (LinearLayout) view.findViewById(R.id.player2ll);
        playerLinearLayouts[2] = (LinearLayout) view.findViewById(R.id.player3ll);
        playerLinearLayouts[3] = (LinearLayout) view.findViewById(R.id.player4ll);

        playerRemainingCards[0] = (TextView) view.findViewById(R.id.player1RemainingCount);
        playerRemainingCards[1] = (TextView) view.findViewById(R.id.player2RemainingCount);
        playerRemainingCards[2] = (TextView) view.findViewById(R.id.player3RemainingCount);
        playerRemainingCards[3] = (TextView) view.findViewById(R.id.player4RemainingCount);

        centerCards[0] = (ImageView) view.findViewById(R.id.cardPosition1);
        centerCards[1] = (ImageView) view.findViewById(R.id.cardPosition2);
        centerCards[2] = (ImageView) view.findViewById(R.id.cardPosition3);
        centerCards[3] = (ImageView) view.findViewById(R.id.cardPosition4);

        suitView = (ImageView) view.findViewById(R.id.gameboard_suit);

        // Set up the scale factors for the card images
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        cardHeight = screenHeight / 4;
        buttonHeight = screenHeight / 6;

        // Update the size of the text in the name TextViews
        for (TextView tv : playerTextViews) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenHeight / 15);
        }

        // Set up the layout params for the cards
        cardParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, cardHeight / 2);

        // Create the scaled suit images
        scaledSuitImages[0] = scaleButton(R.drawable.clubsuitimage);
        scaledSuitImages[1] = scaleButton(R.drawable.diamondsuitimage);
        scaledSuitImages[2] = scaleButton(R.drawable.heartsuitimage);
        scaledSuitImages[3] = scaleButton(R.drawable.spadesuitimage);
    }

    /**
     * Update the names that are displayed on the Gameboard.
     *
     * This data is pulled from the Game instance
     */
    private void updateNamesOnGameboard(Game game) {
        List<Player> players = game.getPlayers();
        for (int i = 0; i < 4; i++) {
            if (i < players.size()) {
                playerTextViews[i].setVisibility(View.VISIBLE);
                playerTextViews[i].setText(players.get(i).getName());
            } else {
                playerTextViews[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * This method will update the suit on the gameboard message center to show the player
     * the current suit of the last card played
     *
     * @param suit the suit of the card in which to change the picture to
     */
    private void updateSuit(int suit) {
        if (suit >= 0 && suit < 4) {
            suitView.setImageBitmap(scaledSuitImages[suit]);
            suitView.setVisibility(View.VISIBLE);
        } else {
            suitView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Updates the User Interface
     *
     * Places all cards in the users' hands
     * Updates the discard image
     * Updates the draw card image
     */
    public void updateUi(Game game) {
        mGame = game;
        if (mGame == null || !isAdded()) {
            return;
        }

        // TODO: highlight selected player
        updateNamesOnGameboard(game);

        updateSuit(game.getDisplaySuit());

        // Place images for all player's cards
        List<Player> players = game.getPlayers();
        int i = 0;
        for (Player p : players) {
            List<Card> cards = p.getCards();
            playerLinearLayouts[i].removeAllViews();

            for (int j = 0; j < cards.size(); j++) {
                Card c = cards.get(j);
                ImageView image = new ImageView(getActivity());
                image.setId(c.getIdNum());
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);

                int resId = CARD_BACK;

                // If we are in debug mode, show the face
                // Otherwise stick with the back of the card
                if (Util.isCheaterMode(getActivity())) {
                    resId = c.getResourceId();
                }

                int cardsToDisplay = cards.size();
                if (cardsToDisplay > maxDisplayed[i]) {
                    cardsToDisplay = maxDisplayed[i];
                }

                // Scale card
                Bitmap scaledCard = scaleCard(resId, (j < (cardsToDisplay - 1)) ? cFourthCard : cHalfCard);
                image.setImageBitmap(scaledCard);

                // Check for max displayed
                if (j < maxDisplayed[i]) {
                    playerLinearLayouts[i].addView(image, cardParams);
                    playerRemainingCards[i].setVisibility(View.INVISIBLE);
                } else {
                    // Display how many cards are remaining that aren't displayed
                    playerRemainingCards[i].setText("+" + Math.abs(maxDisplayed[i] - cards.size()));
                    playerRemainingCards[i].setVisibility(View.VISIBLE);
                    break;
                }
            }

            i++;
        }

        // Set all the cards in the center of the screen
        for (int j = 0; j < 4; j++) {
            Card c = game.getCardAtPosition(j + 1);
            if (c != null) {
                Bitmap scaledCard = scaleCard(c.getResourceId(), mFullCard);

                centerCards[j].setImageBitmap(scaledCard);
                centerCards[j].setVisibility(View.VISIBLE);
            } else {
                centerCards[j].setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Scale a card image with the given resource
     *
     * @param resId the resource id of the card to scale
     * @param cardPortion the amount of the card to show
     *
     * @return a scaled card image
     */
    private Bitmap scaleCard(int resId, int cardPortion) {
        Bitmap fullCard = BitmapFactory.decodeResource(getResources(), resId);
        float scaleFactor = (cardHeight + 0.0f) / fullCard.getHeight();
        Matrix tempMatrix = new Matrix();
        tempMatrix.setScale(scaleFactor, scaleFactor);

        // Draw fourth card
        if (cardPortion == cFourthCard) {
            return Bitmap.createBitmap(fullCard, 0, 0,
                    fullCard.getWidth() / 2, fullCard.getHeight() / 2, tempMatrix, true);
        } else if (cardPortion == cHalfCard) {
            return Bitmap.createBitmap(fullCard, 0, 0,
                    fullCard.getWidth(), fullCard.getHeight() / 2, tempMatrix, true);
        } else if (cardPortion == cHalfCardVertCut) {
            return Bitmap.createBitmap(fullCard, 0, 0,
                    fullCard.getWidth() / 2, fullCard.getHeight(), tempMatrix, true);
        } else {
            return Bitmap.createBitmap(fullCard, 0, 0,
                    fullCard.getWidth(), fullCard.getHeight(), tempMatrix, true);
        }
    }

    /**
     * Scale a button image with the given resource
     *
     * @param resId the resource id of the card to scale
     * @return a scaled button image
     */
    private Bitmap scaleButton(int resId) {
        Bitmap fullImage = BitmapFactory.decodeResource(getResources(), resId);
        float scaleFactor = (buttonHeight + 0.0f) / fullImage.getHeight();
        Matrix tempMatrix = new Matrix();
        tempMatrix.setScale(scaleFactor, scaleFactor);

        return Bitmap.createBitmap(fullImage, 0, 0,
                fullImage.getWidth(), fullImage.getHeight(), tempMatrix, true);
    }

    /**
     * Highlight the name of the person whose turn it is
     *
     * @param playerNumber the player whose turn it is
     */
    private void highlightPlayer(int playerNumber) {
        for (int i = 0; i < 4; i++) {
            if ((i + 1) == playerNumber) {
                playerTextViews[i].setTextColor(getResources().getColor(R.color.gold));
            } else {
                playerTextViews[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    /**
     * Bold the specified player text
     * @param playerNumber player whose name will be bolded
     */
    private void boldPlayerText(int playerNumber) {
        playerTextViews[playerNumber].setTypeface(null, Typeface.BOLD);
    }

    /**
     * Sets all the players text to normal
     */
    private void unboldAllPlayerText() {
        for (int i = 0; i < 4; i++) {
            playerTextViews[i].setTypeface(null, Typeface.NORMAL);
        }
    }
}
