package cs309.a1.shared;

import android.content.Context;
import android.content.Intent;

public interface GameController {

	/**
	 * This method is meant to be used inside the bluetooth broadcast receiver to handle the bluetooth message
	 * @param context
	 * @param intent
	 */
	public void handleBroadcastReceive(Context context, Intent intent);

	/**
	 * This will allow the GameController to handle whenever an activity finishes and returns to the gameboard
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public boolean handleActivityResult(int requestCode, int resultCode, Intent data);
	
	/**
	 * Sends a pause message to all the players so they are not able to play while the game is paused
	 */
	public void pause();
	
	/**
	 * Un-pauses all the players
	 */
	public void unpause();
	
	/**
	 * Tell the players to end the game
	 */
	public void sendGameEnd();

}
