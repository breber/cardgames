package com.worthwhilegames.cardgames.shared;

import android.content.Context;
import android.content.Intent;

/**
 * The GameController should be used by the GameBoardActivity to do all of the
 * game specific operations, such as controlling whose turn it is, handling
 * cards played, declaring winner, sending pause and end game messages, and
 * managing the state of the players. specific GameControllers should be created
 * for each specific game that is played
 */
public interface GameController {

	/**
	 * This method is meant to be used inside the broadcast receiver
	 * to handle messages from the connection module
	 * 
	 * @param context context of the broadcast
	 * @param intent intent of the broadcast, message is stored here
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
	public boolean handleActivityResult(int requestCode, int resultCode, Intent data);

	/**
	 * Sends a pause message to all the players so they are not able to play
	 * while the game is paused
	 */
	public void pause();

	/**
	 * Un-pauses all the players
	 */
	public void unpause();

	/**
	 * Tell the players to end the game, since the gameboard is ending
	 */
	public void sendGameEnd();

}
