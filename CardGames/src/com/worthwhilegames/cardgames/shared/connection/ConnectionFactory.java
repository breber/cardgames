package com.worthwhilegames.cardgames.shared.connection;

import java.net.InetAddress;

import android.app.Activity;
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
import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.wifi.WifiServerSocket;
import com.worthwhilegames.cardgames.shared.wifi.WifiSocket;

/**
 * A Factory class for getting instances of a connection
 * client or server.
 */
public class ConnectionFactory {

	public static final String CONNECTION_ENABLED = "com.worthwhilegames.cardgames.shared.connection.ConnectionFactory.CONNECTION_ENABLED";

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
				sb.append(android.os.Build.MODEL);
			}
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
		case WiFi:
		default:
			return new WifiServerSocket(ctx);
		}
	}

	/**
	 * Get a new Socket based on the current connection type
	 * 
	 * @return a ServerSocket
	 */
	public static ISocket getSocket(Context ctx, String address) {
		return getSocket(ctx, address, GameFactory.getPortNumber(ctx));
	}

	/**
	 * Get a new Socket based on the current connection type
	 * 
	 * @return a ServerSocket
	 */
	public static ISocket getSocket(Context ctx, String address, int portNumber) {
		ConnectionType currentType = getConnectionType(ctx);

		switch (currentType) {
		case WiFi:
		default:
			return new WifiSocket(address, portNumber);
		}
	}
}
