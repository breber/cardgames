package cs309.a1.shared;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnLongClickListener;

public interface PlayerController {

	/**
	 * @param context
	 * @param intent
	 */
	public void handleBroadcastReceive(Context context, Intent intent);

	/**
	 * @return
	 */
	public View.OnClickListener getPlayOnClickListener();

	/**
	 * @return
	 */
	public View.OnClickListener getDrawOnClickListener();

	/**
	 * @return
	 */
	public OnLongClickListener getCardLongClickListener();

	/**
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void handleActivityResult(int requestCode, int resultCode, Intent data);

}
