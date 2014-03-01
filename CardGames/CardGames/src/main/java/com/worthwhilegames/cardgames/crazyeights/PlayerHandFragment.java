package com.worthwhilegames.cardgames.crazyeights;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Game;
import com.worthwhilegames.cardgames.shared.Player;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by breber on 2/2/14.
 */
public class PlayerHandFragment extends Fragment {

    public interface GameUpdatedListener {
        void gameUpdated(Game game);
    }

    private String mPlayerId;

    /**
     * The height of each card
     */
    private static int sCardHeight;

    /**
     * List of cards in player's hand
     */
    private ArrayList<Card> mCardHand;

    /**
     * The current game
     */
    private Game mGame;

    /**
     * The LinearLayout holding all card images
     */
    @InjectView(R.id.playerCardContainer) LinearLayout mPlayerHandLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.player_hand, container, false);
        ButterKnife.inject(this, v);

        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        sCardHeight = screenHeight * 3 / 5;

        // Create a new, empty hand
        mCardHand = new ArrayList<Card>();

        updateGame(mGame);

        return v;
    }

    public void setCurrentPlayerId(String playerId) {
        mPlayerId = playerId;
    }

    /**
     * Adds and displays a card in the player's hand
     *
     * @param game update the view to the new game state
     */
    public void updateGame(Game game) {
        mGame = game;
        if (mCardHand == null || mGame == null || mPlayerId == null) {
            return;
        }

        mCardHand.clear();

        Player player = null;
        for (Player p : game.getPlayers()) {
            if (p.getId().equals(mPlayerId)) {
                player = p;
                break;
            }
        }

        // This shouldn't happen...
        if (player == null) {
            return;
        }

        mCardHand.addAll(player.getCards());

        // Make sure the hand is sorted
        Collections.sort(mCardHand);

        // Remove all cards from the display
        mPlayerHandLayout.removeAllViews();

        // edit layout attributes
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, sCardHeight);

        for (Card c : mCardHand) {
            // create ImageView to hold Card
            ImageView toAdd = new ImageView(getActivity());
            toAdd.setImageBitmap(scaleCard(c.getResourceId()));
            toAdd.setId(c.getIdNum());
            toAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: check if playable
                    // TODO: move to button click
                    ((GameUpdatedListener)getActivity()).gameUpdated(mGame);
                }
            });

            // Add a 5px border around the image
            toAdd.setPadding(5, 5, 5, 5);

            mPlayerHandLayout.addView(toAdd, params);
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
        for (Card c : mCardHand) {
            if (c.getIdNum() == cardId && c.getIdNum() == suggestedId) {
                ImageView iv = (ImageView)getView().findViewById(c.getIdNum());
                iv.setBackgroundColor(getResources().getColor(R.color.suggested_selected_card_color));
            } else if (c.getIdNum() == cardId) {
                ImageView iv = (ImageView)getView().findViewById(c.getIdNum());
                iv.setBackgroundColor(getResources().getColor(R.color.gold));
            } else if(c.getIdNum() == suggestedId){
                ImageView iv = (ImageView)getView().findViewById(c.getIdNum());
                iv.setBackgroundColor(getResources().getColor(R.color.suggested_card_color));
            } else {
                ImageView iv = (ImageView)getView().findViewById(c.getIdNum());
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
        for (Card c : mCardHand) {
            if (c.getIdNum() == cardId) {
                ImageView iv = (ImageView)getView().findViewById(c.getIdNum());
                iv.setBackgroundColor(getResources().getColor(R.color.gold));
            }
        }
    }

    /**
     * Set the card as greyed out, or not greyed out.
     *
     * @param cardImageViewId - This is the id of the image view of the card
     *                             that is being greyed out or not greyed out
     * @param isPlayable  - This is whether or not the card should be greyed
     *                         out based on whether it is legal to play it
     */
    public void setCardPlayable(int cardImageViewId, boolean isPlayable) {
        ImageView iv = (ImageView)getView().findViewById(cardImageViewId);
        if (isPlayable) {
            iv.setColorFilter(Color.TRANSPARENT);
        } else {
            iv.setColorFilter(getResources().getColor(R.color.transparent_grey));
        }
    }
}
