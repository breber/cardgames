package com.worthwhilegames.cardgames.shared.connection;

import android.content.Context;
import android.content.SharedPreferences;

import com.worthwhilegames.cardgames.shared.Constants;
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
		String connectionType = prefs.getString(Constants.CONNECTION_TYPE, Constants.WIFI);

		if (Constants.WIFI.equals(connectionType)) {
			return ConnectionType.WIFI;
		} else if (Constants.BLUETOOTH.equals(connectionType)) {
			return ConnectionType.BLUETOOTH;
		}

		return ConnectionType.WIFI;
	}

	/**
	 * Get a new ServerSocket based on the current connection type
	 * 
	 * @return a ServerSocket
	 */
	public static IServerSocket getServerSocket(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREFERENCES, 0);
		String connectionType = prefs.getString(Constants.CONNECTION_TYPE, Constants.WIFI);

		if (Constants.WIFI.equals(connectionType)) {
			return new WifiServerSocket();
		} else if (Constants.BLUETOOTH.equals(connectionType)) {
			return new BluetoothServerSocket();
		}

		return new WifiServerSocket();
	}

	/**
	 * Get a new Socket based on the current connection type
	 * 
	 * @return a ServerSocket
	 */
	public static ISocket getSocket(Context ctx, String address) {
		SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREFERENCES, 0);
		String connectionType = prefs.getString(Constants.CONNECTION_TYPE, Constants.WIFI);

		if (Constants.WIFI.equals(connectionType)) {
			return new WifiSocket(address);
		} else if (Constants.BLUETOOTH.equals(connectionType)) {
			return new BluetoothSocket(address);
		}

		return new WifiSocket(address);
	}
}
