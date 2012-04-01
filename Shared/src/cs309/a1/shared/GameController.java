package cs309.a1.shared;

import android.content.Context;
import android.content.Intent;

public interface GameController {

	/**
	 * @param context
	 * @param intent
	 */
	public void handleBroadcastReceive(Context context, Intent intent);

	/**
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public boolean handleActivityResult(int requestCode, int resultCode, Intent data);

}
