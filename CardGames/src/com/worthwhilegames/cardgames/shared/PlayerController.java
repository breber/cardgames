package com.worthwhilegames.cardgames.shared;

import android.content.Context;
import android.content.Intent;
import android.view.View.OnClickListener;

/**
 * The PlayerController is meant to be used inside the ShowCardsActivity on a player's device
 * This will handle the cards sent to the player and also will allow the player to play cards.
 * This will check if a card can be played and allow the player to draw cards. Basically handles
 * the state of the player.
 */
public interface PlayerController {

	/**
	 * This method is handles messages received from the Connection module
	 * 
	 * @param context context of the broadcast
	 * @param intent intent from the broadcast, message is stored here
	 */
	public void handleBroadcastReceive(Context context, Intent intent);

	/**
	 * This will allow the GameController to handle whenever an activity
	 * finishes and returns to the gameboard
	 * 
	 * @param requestCode the request code used to start the activity that we are
	 *            getting the result of
	 * @param resultCode the result code from the activity
	 * @param data the intent which may contain data from the activity that has finished
	 */
	public void handleActivityResult(int requestCode, int resultCode, Intent data);

	/**
	 * This will return the ClickListener for selecting cards
	 * 
	 * @return the click listener for a card ImageView each card will need to be given this
	 */
	public OnClickListener getCardClickListener();

	/**
	 * This will get the full state of the player

	 * @return full player state
	 */
	public PlayerStateFull getPlayerState();

}
