package com.worthwhilegames.cardgames.shared.activities;

import static com.worthwhilegames.cardgames.shared.Constants.fourthCard;
import static com.worthwhilegames.cardgames.shared.Constants.halfCard;
import static com.worthwhilegames.cardgames.shared.Constants.halfCardVertCut;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.PlayerController;
import com.worthwhilegames.cardgames.shared.TextView;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionCommon;

public abstract class GameViewActivity extends Activity {

	/**
	 * The request code to keep track of the connect device activity
	 */
	protected static final int CONNECT_DEVICE = Math.abs("CONNECT_DEVICE".hashCode());

	/**
	 * The request code to keep track of the "Are you sure you want to quit" activity
	 */
	protected static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());

	/**
	 * The request code to keep track of the "You have been disconnected" activity
	 */
	public static final int DISCONNECTED = Math.abs("DISCONNECTED".hashCode());

	/**
	 * The request code to pause the game
	 */
	protected static final int PAUSE_GAME = Math.abs("PAUSE_GAME".hashCode());

	/**
	 * LayoutParams for adding a card to a player on the long edge of the screen
	 * 
	 * width  = WRAP_CONTENT
	 * height = cardHeight
	 */
	protected static LinearLayout.LayoutParams cardParams;

	/**
	 * The height of each card
	 */
	protected static int cardHeight;

	/**
	 * The height of each button
	 */
	protected static int buttonHeight;

	/**
	 * Represents the resource id to use for the back of the cards
	 */
	protected static int CARD_BACK;

	/**
	 * These are the TextViews for all the player names
	 */
	protected TextView[] playerTextViews = new TextView[4];

	/**
	 * These are the LinearLayouts for all the player cards
	 */
	protected LinearLayout[] playerLinearLayouts = new LinearLayout[4];

	/**
	 * These are the TextViews for the count of remaining cards not being displayed
	 */
	protected TextView[] playerRemainingCards = new TextView[4];

	/**
	 * The LinearLayout holding all card images
	 */
	protected LinearLayout playerHandLayout;

	/**
	 * The maximum number of cards displayed for each player
	 */
	protected static int[] maxDisplayed = new int[] { Constants.MAX_DISPLAYED, Constants.MAX_DIS_SIDES, Constants.MAX_DISPLAYED, Constants.MAX_DIS_SIDES };

	/**
	 * Holds the scaled Bitmaps of the suit images
	 */
	protected static Bitmap[] scaledSuitImages = new Bitmap[4];

	/**
	 * The ImageViews for the cards in the center of the screen
	 * 
	 * For games that don't use 4 cards in the middle:
	 * Position 2 = discard pile
	 * Position 4 = draw pile
	 */
	protected ImageView[] centerCards = new ImageView[4];

	/**
	 * The current suit ImageView
	 */
	protected ImageView suitView;

	/**
	 * The ConnectionClient used to send messages to the server
	 */
	protected ConnectionCommon connection;

	/**
	 * The BroadcastReceiver for handling messages from the connection module
	 */
	protected BroadcastReceiver receiver;

	/**
	 * The PlayerController for handling a lot of the Game-Specific logic
	 */
	protected PlayerController playerController;

	/**
	 * The SharedPreferences used to store preferences for the game
	 */
	protected SharedPreferences sharedPreferences;

	/**
	 * Unregister the broadcast receiver
	 */
	public void unregisterReceiver() {
		// Unregister all the receivers we may have registered
		try {
			unregisterReceiver(receiver);
		} catch (IllegalArgumentException e) {
			// We didn't get far enough to register the receiver
		}
	}

	/**
	 * Set up all the references to UI elements
	 */
	protected void initUIElements() {
		// Get references to commonly used UI elements
		playerTextViews[0] = (TextView) findViewById(R.id.player1text);
		playerTextViews[1] = (TextView) findViewById(R.id.player2text);
		playerTextViews[2] = (TextView) findViewById(R.id.player3text);
		playerTextViews[3] = (TextView) findViewById(R.id.player4text);

		// Set up the Layout for the cards
		playerHandLayout = (LinearLayout) findViewById(R.id.playerCardContainer);
		playerLinearLayouts[0] = (LinearLayout) findViewById(R.id.playerCardContainer);
		playerLinearLayouts[1] = (LinearLayout) findViewById(R.id.player2ll);
		playerLinearLayouts[2] = (LinearLayout) findViewById(R.id.player3ll);
		playerLinearLayouts[3] = (LinearLayout) findViewById(R.id.player4ll);

		playerRemainingCards[0] = (TextView) findViewById(R.id.player1RemainingCount);
		playerRemainingCards[1] = (TextView) findViewById(R.id.player2RemainingCount);
		playerRemainingCards[2] = (TextView) findViewById(R.id.player3RemainingCount);
		playerRemainingCards[3] = (TextView) findViewById(R.id.player4RemainingCount);

		centerCards[0] = (ImageView) findViewById(R.id.cardPosition1);
		centerCards[1] = (ImageView) findViewById(R.id.cardPosition2);
		centerCards[2] = (ImageView) findViewById(R.id.cardPosition3);
		centerCards[3] = (ImageView) findViewById(R.id.cardPosition4);

		suitView = (ImageView) findViewById(R.id.gameboard_suit);

		// Set up the scale factors for the card images
		int screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
		cardHeight = screenHeight / 5;
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

		// Add the handler for the pause button
		ImageView pause = (ImageView) findViewById(R.id.gameboard_pause);
		pause.setImageBitmap(scaleButton(R.drawable.pause_button));

		// Update the refresh button image
		ImageView refresh = (ImageView) findViewById(R.id.gameboard_refresh);
		refresh.setImageBitmap(scaleButton(R.drawable.refresh_button));

		//TODO add this back, textview.setrotation is causing issues
		//setupGoogleTv();
	}

	/*@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setupGoogleTV(){

		// If this is a Google TV, rotate the text of player 3 so that it isn't upside down
		if (Util.isGoogleTv(this)) {
			if (null != playerTextViews[2]) {
				playerTextViews[2].setRotation(180); //TODO says it can't find this method
			}
		}
	}*/

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, QuitGameActivity.class);
		startActivityForResult(intent, QUIT_GAME);
	}

	/**
	 * Update the names that are displayed on the GameView.
	 *
	 * This data is pulled from the state variables of the game
	 */
	public abstract void updateNamesOnGameboard();

	/**
	 * This method will update the suit on the gameboard message center to show the player
	 * the current suit of the last card played or trump suit
	 * 
	 * @param suit the suit of the card in which to change the picture to
	 */
	public void updateSuit(int suit) {
		if (suit >= 0 && suit < 4) {
			suitView.setImageBitmap(scaledSuitImages[suit]);
			suitView.setVisibility(View.VISIBLE);
		} else {
			suitView.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Updates the User Interface for the GameView
	 * 
	 * Places all cards in the users' hands
	 * Updates the discard image
	 * Updates the draw card image
	 */
	public abstract void updateUi();

	/**
	 * Set the selected card. This will highlight the selected
	 * card, and clear the highlight from any other cards.
	 *
	 * @param cardId - the currently selected card
	 */
	public void setSelected(int cardId, int suggestedId) {
		if(!Util.isPlayerHost()){
			// Don't allow this to be done on a gameboard that is not playing
			return;
		}
		for (Card c : playerController.getPlayerState().cards) {
			if (c.getIdNum() == cardId && c.getIdNum() == suggestedId) {
				ImageView iv = (ImageView) findViewById(c.getIdNum());
				iv.setBackgroundColor(getResources().getColor(R.color.suggested_selected_card_color));
			} else if (c.getIdNum() == cardId) {
				ImageView iv = (ImageView) findViewById(c.getIdNum());
				iv.setBackgroundColor(getResources().getColor(R.color.gold));
			} else if(c.getIdNum() == suggestedId){
				ImageView iv = (ImageView) findViewById(c.getIdNum());
				iv.setBackgroundColor(getResources().getColor(R.color.suggested_card_color));
			} else {
				ImageView iv = (ImageView) findViewById(c.getIdNum());
				iv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			}
		}
	}

	/**
	 * Removes card from player's hand
	 *
	 * @param idNum ID number of the card to be removed
	 */
	public void removeFromHand(int idNum) {
		if(!Util.isPlayerHost()){
			// Don't allow this to be done on a gameboard that is not playing
			return;
		}

		playerHandLayout.removeView(findViewById(idNum));

		List<Card> cards = playerController.getPlayerState().cards;

		// remove card from list
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).getIdNum() == idNum) {
				cards.remove(i);
				return;
			}
		}
	}

	/**
	 * Set the card as greyed out, or not greyed out.
	 *
	 * @param cardImageViewId - This is the id of the image view of the card
	 * 							that is being greyed out or not greyed out
	 * @param isPlayable  - This is whether or not the card should be greyed
	 * 						out based on whether it is legal to play it
	 */
	public void setCardPlayable(int cardImageViewId, boolean isPlayable) {
		ImageView iv = (ImageView) findViewById(cardImageViewId);
		if (isPlayable) {
			iv.setColorFilter(Color.TRANSPARENT);
		} else {
			iv.setColorFilter(getResources().getColor(R.color.transparent_grey));
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// Disconnect Connection
		if (connection != null) {
			connection.disconnect();
		}

		// Unregister the receiver
		try {
			unregisterReceiver(receiver);
		} catch (IllegalArgumentException e) {
			// We didn't get far enough to register the receiver
		}

		super.onDestroy();
	}

	/**
	 * Scale a card image with the given resource
	 * 
	 * @param resId the resource id of the card to scale
	 * @param cardPortion the amount of the card to show
	 * 
	 * @return a scaled card image
	 */
	protected Bitmap scaleCard(int resId, int cardPortion) {
		Bitmap fullCard = BitmapFactory.decodeResource(getResources(), resId);
		float scaleFactor = (cardHeight + 0.0f) / fullCard.getHeight();
		Matrix tempMatrix = new Matrix();
		tempMatrix.setScale(scaleFactor, scaleFactor);

		// Draw fourth card
		if (cardPortion == fourthCard) {
			return Bitmap.createBitmap(fullCard, 0, 0,
					fullCard.getWidth() / 2, fullCard.getHeight() / 2, tempMatrix, true);
		} else if (cardPortion == halfCard) {
			return Bitmap.createBitmap(fullCard, 0, 0,
					fullCard.getWidth(), fullCard.getHeight() / 2, tempMatrix, true);
		} else if (cardPortion == halfCardVertCut) {
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
	protected Bitmap scaleButton(int resId) {
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
	public void highlightPlayer(int playerNumber) {
		for (int i = 0; i < 4; i++) {
			if (i == playerNumber) {
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
	public void boldPlayerText(int playerNumber){
		playerTextViews[playerNumber].setTypeface(null, Typeface.BOLD);
	}

	/**
	 * Sets all the players text to normal
	 */
	public void unboldAllPlayerText(){
		for (int i = 0; i < 4; i++) {
			playerTextViews[i].setTypeface(null, Typeface.NORMAL);
		}
	}

}
