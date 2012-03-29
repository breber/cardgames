package cs309.a1.shared;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;
import android.view.View.OnLongClickListener;

public interface PlayerController {

	public BroadcastReceiver getBroadcastReceiver();
	public void handleBroadcastReceive(Context context, Intent intent);
	public View.OnClickListener getPlayOnClickListener();
	public View.OnClickListener getDrawOnClickListener();
	public OnLongClickListener getCardLongClickListener();
	public void handleActivityResult(int requestCode, int resultCode, Intent data);
	
}
