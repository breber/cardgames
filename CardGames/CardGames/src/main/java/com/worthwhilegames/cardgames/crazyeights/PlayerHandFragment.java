package com.worthwhilegames.cardgames.crazyeights;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.player.activities.SelectSuitActivity;
import com.worthwhilegames.cardgames.shared.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by breber on 2/2/14.
 */
public class PlayerHandFragment extends Fragment {

    public interface GameUpdatedListener {
        void gameUpdated(Game game);
    }

    private GameUpdatedListener mDelegate;

    /**
     * intent code for choosing suit
     */
    private static final int CHOOSE_SUIT = Math.abs("CHOOSE_SUIT".hashCode());

    /**
     * The height of each card
     */
    private static int sCardHeight;

    /**
     * The current selected Card
     */
    private Card mCardSelected;

    /**
     * The current game
     */
    private Game mGame;

    /**
     * The view containing the buttons
     */
    private View mButtonView;

    /**
     * The LinearLayout holding all card images
     */
    @InjectView(R.id.playerCardContainer) LinearLayout mPlayerHandLayout;

    public void setDelegate(GameUpdatedListener delegate) {
        mDelegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.player_hand, container, false);
        ButterKnife.inject(this, v);

        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        sCardHeight = screenHeight * 3 / 5;

        // Set up the buttons
        if (v != null) {
            ViewStub buttonLayout = (ViewStub) v.findViewById(R.id.playerHandButtonView);
            buttonLayout.setLayoutResource(R.layout.genericbuttons);
            mButtonView = buttonLayout.inflate();

            Button play = (Button) mButtonView.findViewById(R.id.btPlayCard);
            Button draw = (Button) mButtonView.findViewById(R.id.btDrawCard);
            play.setOnClickListener(playClickListener);
            draw.setOnClickListener(drawClickListener);
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateGame(mGame);
    }

    /**
     * Adds and displays a card in the player's hand
     *
     * @param game update the view to the new game state
     */
    public void updateGame(Game game) {
        mGame = game;
        if (mGame == null || !isAdded()) {
            return;
        }

        Player player = game.getSelf();

        // This shouldn't happen...
        if (player == null) {
            return;
        }

        List<Card> cards = new ArrayList<Card>(player.getCards());

        // Make sure the hand is sorted
        Collections.sort(cards);

        // Remove all cards from the display
        mPlayerHandLayout.removeAllViews();

        // edit layout attributes
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, sCardHeight);

        for (Card c : cards) {
            // create ImageView to hold Card
            ImageView toAdd = new ImageView(getActivity());
            toAdd.setImageBitmap(scaleCard(c.getResourceId()));
            toAdd.setId(c.getIdNum());
            toAdd.setOnClickListener(new CardSelectionClickListener());

            // Add a 5px border around the image
            toAdd.setPadding(5, 5, 5, 5);

            mPlayerHandLayout.addView(toAdd, params);
        }

        mCardSelected = null;
        setButtonsEnabled(mGame.isMyTurn());
    }

    /**
     * The OnClickListener for the play button
     */
    private View.OnClickListener playClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get the discard card (updating it to have the chosen suit)
            Card discardCard = new Card(mGame.getDiscardPileTop());
            if (mGame.getDisplaySuit() != C8Constants.PLAY_SUIT_NONE) {
                discardCard.setSuit(mGame.getDisplaySuit());
            }

            if (mGame.isMyTurn() &&
                mGame.getRules().checkCard(mCardSelected, discardCard) &&
                !mGame.getSelf().getCards().isEmpty())
            {
                // play card
                if (mCardSelected.getValue() == C8Constants.EIGHT_CARD_NUMBER) {
                    Intent selectSuit = new Intent(getActivity(), SelectSuitActivity.class);
                    startActivityForResult(selectSuit, CHOOSE_SUIT);
                    // go to the onActivityResult to finish this turn
                } else {
                    // Discard the card
                    mGame.discard(mCardSelected);

                    // Call game update on the parent
                    if (mDelegate != null) {
                        mDelegate.gameUpdated(mGame);
                    }
                }
            }
        }
    };

    private View.OnClickListener drawClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mGame.isMyTurn()) {
                mGame.draw();

                // Call game update on the parent
                if (mDelegate != null) {
                    mDelegate.gameUpdated(mGame);
                }
            }
        }
    };

    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_SUIT) {
            CrazyEightsGame c8Game = (CrazyEightsGame) mGame;
            boolean isSuitChosen = true;
            switch (resultCode) {
                case Constants.SUIT_CLUBS:
                    c8Game.discard(mCardSelected, Constants.SUIT_CLUBS);
                    break;
                case Constants.SUIT_DIAMONDS:
                    c8Game.discard(mCardSelected, Constants.SUIT_DIAMONDS);
                    break;
                case Constants.SUIT_HEARTS:
                    c8Game.discard(mCardSelected, Constants.SUIT_HEARTS);
                    break;
                case Constants.SUIT_SPADES:
                    c8Game.discard(mCardSelected, Constants.SUIT_SPADES);
                    break;
                case Activity.RESULT_OK:
                    isSuitChosen = false;
                    break;
            }

            if (isSuitChosen) {
                updateGame(c8Game);

                mCardSelected = null;
                setButtonsEnabled(false);

                // Call game update on the parent
                if (mDelegate != null) {
                    mDelegate.gameUpdated(mGame);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Used to set the play and draw buttons to enable or disabled
     * Also if it is the player's turn then set the cards to be greyed
     * out if they are not playable. if it is not the player's turn then
     * do not grey out any cards
     *
     * @param isEnabled
     */
    private void setButtonsEnabled(boolean isEnabled) {
        ViewGroup group = (ViewGroup) mButtonView;
        for (int i = 0; i < group.getChildCount(); i++) {
            View v = group.getChildAt(i);
            if (v != null) {
                v.setEnabled(isEnabled);
            }
        }

        if (isEnabled) {
            // Get the discard card (updating it to have the chosen suit)
            Card discardCard = new Card(mGame.getDiscardPileTop());
            if (mGame.getDisplaySuit() != C8Constants.PLAY_SUIT_NONE) {
                discardCard.setSuit(mGame.getDisplaySuit());
            }

            // it is your turn grey out cards
            for (Card c : mGame.getSelf().getCards()) {
                boolean isPlayable = mGame.getRules().checkCard(c, discardCard);
                setCardPlayable(c.getIdNum(), isPlayable);
            }
        } else {
            // it is not your turn make cards normal
            if (mPlayerHandLayout != null) {
                for (int i = 0; i < mPlayerHandLayout.getChildCount(); i++) {
                    ImageView v = (ImageView) mPlayerHandLayout.getChildAt(i);
                    if (v != null) {
                        setCardPlayable(v.getId(), true);
                    }
                }
            }
        }
    }

    /**
     * Scale a card image with the given resource
     *
     * @param resId the resource id of the card to scale
     * @return a scaled card image
     */
    private Bitmap scaleCard(int resId) {
        Bitmap fullCard = BitmapFactory.decodeResource(getResources(), resId);
        float scaleFactor = (sCardHeight + 0.0f) / fullCard.getHeight();
        Matrix tempMatrix = new Matrix();
        tempMatrix.setScale(scaleFactor, scaleFactor);

        return Bitmap.createBitmap(fullCard, 0, 0,
                fullCard.getWidth(), fullCard.getHeight(), tempMatrix, true);
    }

    /**
     * Set the selected card. This will highlight the selected
     * card, and clear the highlight from any other cards.
     *
     * @param cardId - the currently selected card
     */
    public void setSelected(int cardId, int suggestedId) {
        View v = getView();
        if (v == null) {
            return;
        }
        for (Card c : mGame.getSelf().getCards()) {
            ImageView iv = (ImageView)v.findViewById(c.getIdNum());
            if (c.getIdNum() == cardId && c.getIdNum() == suggestedId) {
                iv.setBackgroundColor(getResources().getColor(R.color.suggested_selected_card_color));
            } else if (c.getIdNum() == cardId) {
                iv.setBackgroundColor(getResources().getColor(R.color.gold));
            } else if(c.getIdNum() == suggestedId) {
                iv.setBackgroundColor(getResources().getColor(R.color.suggested_card_color));
            } else {
                iv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
        }
    }

    /**
     * Set the selected card. This will highlight the selected
     * card, and clear the highlight from any other cards.
     *
     * @param cardId - the currently selected card
     */
    public void setSuggested(int cardId) {
        View v = getView();
        if (v == null) {
            return;
        }
        for (Card c : mGame.getSelf().getCards()) {
            if (c.getIdNum() == cardId) {
                ImageView iv = (ImageView) v.findViewById(c.getIdNum());
                iv.setBackgroundColor(getResources().getColor(R.color.gold));
            }
        }
    }

    /**
     * Set the card as greyed out, or not greyed out.
     */
    public void setCardPlayable(int cardImageViewId, boolean isPlayable) {
        View v = getView();
        if (v == null) {
            return;
        }
        ImageView iv = (ImageView) v.findViewById(cardImageViewId);
        if (isPlayable) {
            iv.setColorFilter(Color.TRANSPARENT);
        } else {
            iv.setColorFilter(getResources().getColor(R.color.transparent_grey));
        }
    }

    /**
     * This will be used for each card ImageView and will allow the card to be
     * selected when it is Clicked
     */
    private class CardSelectionClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Show an animation indicating the card was selected
            ScaleAnimation scale = new ScaleAnimation((float) 1.2, (float) 1.2, (float) 1.2, (float) 1.2);
            scale.scaleCurrentDuration(5);
            v.startAnimation(scale);

            // Let the UI know which card was selected
            setSelected(v.getId(), -1);

            for (Card c : mGame.getSelf().getCards()) {
                if (c.getIdNum() == v.getId()) {
                    mCardSelected = c;
                }
            }
        }
    }
}
