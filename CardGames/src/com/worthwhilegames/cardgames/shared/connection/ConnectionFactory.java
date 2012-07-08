package com.worthwhilegames.cardgames.shared.connection;

import java.net.InetAddress;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.bluetooth.BluetoothServerSocket;
import com.worthwhilegames.cardgames.shared.bluetooth.BluetoothSocket;
import com.worthwhilegames.cardgames.shared.wifi.WifiServerSocket;
import com.worthwhilegames.cardgames.shared.wifi.WifiSocket;

/**
 * A Factory class for getting instances of a connection
 * client or server.
 */
public class ConnectionFactory {

	public static final String CONNECTION_ENABLED = "com.worthwhilegames.cardgames.shared.connection.ConnectionFactory.CONNECTION_ENABLED";

	/**
	 * The request code to keep track of the Bluetooth request enable intent
	 */
	public static final int REQUEST_ENABLE_BT = Math.abs("REQUEST_BLUETOOTH".hashCode());

	/**
	 * Get the type of connection that is currently in use
	 * 
	 * @return the type of connection in use
	 */
	public static ConnectionType getConnectionType(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREFERENCES, 0);
		String connectionType = prefs.getString(Constants.CONNECTION_TYPE, ConnectionType.WiFi.toString());

		if (ConnectionType.WiFi.toString().equals(connectionType)) {
			return ConnectionType.WiFi;
		} else if (ConnectionType.Bluetooth.toString().equals(connectionType)) {
			return ConnectionType.Bluetooth;
		}

		return ConnectionType.WiFi;
	}

	/**
	 * Get the string to display on the connection screen
	 * 
	 * @return the string device id
	 */
	public static String getDeviceDisplayName(Context ctx) {
		StringBuilder sb = new StringBuilder(ctx.getResources().getString(R.string.deviceName));
		sb.append("\n");

		if (ConnectionType.WiFi.equals(getConnectionType(ctx))) {
			InetAddress currentAddress = Util.getLocalIpAddress();
			if (currentAddress == null) {
				sb.append("Unknown");
			} else {
				sb.append(currentAddress.getHostAddress());
			}
		} else if (ConnectionType.Bluetooth.equals(getConnectionType(ctx))) {
			BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
			sb.append(btAdapter.getName());
		}

		return sb.toString();
	}

	/**
	 * Ensure connections are enabled
	 * 
	 * @param ctx
	 */
	public static void ensureConnectionEnabled(final Activity ctx) {
		if (ConnectionType.WiFi.equals(getConnectionType(ctx))) {
			final WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
			final ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			final BroadcastReceiver rx = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {

						NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
						if (NetworkInfo.State.CONNECTED.equals(info.getState())) {
							ctx.unregisterReceiver(this);
							ctx.sendBroadcast(new Intent(CONNECTION_ENABLED));
						}
					}
				}
			};

			ctx.registerReceiver(rx, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
			ctx.registerReceiver(rx, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
			wifiManager.setWifiEnabled(true);
		} else if (ConnectionType.Bluetooth.equals(getConnectionType(ctx))) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			ctx.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
	}

	/**
	 * Get a new ServerSocket based on the current connection type
	 * 
	 * @return a ServerSocket
	 */
	public static IServerSocket getServerSocket(Context ctx) {
		ConnectionType currentType = getConnectionType(ctx);

		switch (currentType) {
		case Bluetooth:
			return new BluetoothServerSocket();
		case WiFi:
		default:
			return new WifiServerSocket();
		}
	}

	/**
	 * Get a new Socket based on the current connection type
	 * 
	 * @return a ServerSocket
	 */
	public static ISocket getSocket(Context ctx, String address) {
		ConnectionType currentType = getConnectionType(ctx);

		switch (currentType) {
		case Bluetooth:
			return new BluetoothSocket(address);
		case WiFi:
		default:
			return new WifiSocket(address);
		}
	}

	/**
	 * Checks to see if we have bluetooth capabilities
	 * @return
	 */
	public static boolean hasBluetoothCapabilities() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

		return adapter != null;
	}
}
