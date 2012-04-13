package cs309.a1.shared;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnLongClickListener;

/**
 * The PlayerController is meant to be used inside the ShowCardsActivity on a player's device
 * This will handle the cards sent to the player and also will allow the player to play cards. 
 * This will check if a card can be played and allow the player to draw cards. Basically handles 
 * the state of the player.
 */
public interface PlayerController {

	/**
	 * This method is meant to be used inside the bluetooth broadcast receiver
	 * to handle the bluetooth message
	 * 
	 * @param context
	 *            context of the bluetooth broadcast
	 * @param intent
	 *            intent of the bluetooth broadcast, message is stored here
	 */
	public void handleBroadcastReceive(Context context, Intent intent);

	/**
	 * This will allow the GameController to handle whenever an activity
	 * finishes and returns to the gameboard
	 * 
	 * @param requestCode
	 *            the request code used to start the activity that we are
	 *            getting the result of
	 * @param resultCode
	 *            the result code from the activity
	 * @param data
	 *            the intent which may contain data from the activity that has
	 *            finished
	 */
	public void handleActivityResult(int requestCode, int resultCode, Intent data);

	/**
	 * This will return the onClickListener for the play button.
	 * @return
	 */
	public View.OnClickListener getPlayOnClickListener();

	/**
	 * This will return the onClickListener for the Draw button
	 * @return
	 */
	public View.OnClickListener getDrawOnClickListener();

	/**
	 * This will return the LongClickListener for selecting cards
	 * @return
	 * returns the long click listener for a card imageview each card will need to be given this
	 */
	public OnLongClickListener getCardLongClickListener();
	
	/**
	 * This will set the player name to the name given
	 * @param name
	 * name of player
	 */
	public void setPlayerName(String name);


}
