package cs309.a1.shared;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnLongClickListener;

public interface GameController {

	/**
	 * @param context
	 * @param intent
	 */
	public void handleBroadcastReceive(Context context, Intent intent);

}
