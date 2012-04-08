package cs309.a1.shared;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnLongClickListener;

public interface PlayerController {

	/**
	 * This will handle any bluetooth messages regarding game play
	 * @param context
	 * @param intent
	 */
	public void handleBroadcastReceive(Context context, Intent intent);

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
	 */
	public OnLongClickListener getCardLongClickListener();
	
	/**
	 * This will set the player name to the name given
	 * @param name
	 * name of player
	 */
	public void setPlayerName(String name);

	/**
	 * This will be called in the onActivityResult method inside of the ShowCardsActivity
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void handleActivityResult(int requestCode, int resultCode, Intent data);

}
