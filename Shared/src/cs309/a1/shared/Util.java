package cs309.a1.shared;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Util {

	public static final String TAG_GENERIC = "CR8S";

	public static boolean isDebugBuild() {
		return true;
	}

	public static void ensureDiscoverable(Context ctx, BluetoothAdapter btAdapter) {
		if (Util.isDebugBuild()) {
			Log.d(TAG_GENERIC, "ensure discoverable");
		}

		if (btAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			ctx.startActivity(discoverableIntent);
		}
	}

}
