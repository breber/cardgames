package com.worthwhilegames.cardgames.shared.connection;

import java.net.InetAddress;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;

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
