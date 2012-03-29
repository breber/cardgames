package cs309.a1.shared;

import java.util.Comparator;

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
	 * Checks whether this is a debug build or not.
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
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 5000);
			ctx.startActivity(discoverableIntent);
		}
	}
	
	/**
	 * Comparator for sorting cards in ascending order based on ID number
	 */
	public static class CompareIdNums implements Comparator<Card> {
		public int compare(Card card1, Card card2) {
			return card1.getIdNum() - card2.getIdNum();
		}
	}
}
