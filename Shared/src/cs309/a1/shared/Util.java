package cs309.a1.shared;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * A Utility class that contains common generic methods relevant
 * to any object in the application
 */
public class Util {

	public static final String TAG_GENERIC = "CR8S";

	/**
	 * CHecks whether this is a debug build or not.
	 * 
	 * This toggles different logging capabilities
	 * 
	 * @return whether this is a production build or not
	 */
	public static boolean isDebugBuild() {
		return true;
	}

	/**
	 * Make sure that the device is in Bluetooth "Discoverable" mode
	 * 
	 * @param ctx
	 * @param btAdapter
	 */
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
