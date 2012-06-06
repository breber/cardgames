package com.worthwhilegames.cardgames.shared.connection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.bluetooth.BluetoothConnectionService;
import com.worthwhilegames.cardgames.shared.bluetooth.BluetoothServerSocket;
import com.worthwhilegames.cardgames.shared.wifi.WifiConnectionService;
import com.worthwhilegames.cardgames.shared.wifi.WifiServerSocket;

/**
 * A Factory class for getting instances of a connection
 * client or server.
 */
public class ConnectionFactory {

	/**
	 * Get an instance of a Client connection based on the current
	 * connection type.
	 * 
	 * @param ctx
	 * @return the ConnectionClient
	 */
	public static ConnectionClient getClientInstance(Context ctx) {
		return ConnectionClient.getInstance(ctx);
	}

	/**
	 * Get an instance of a Server connection based on the current
	 * connection type.
	 * 
	 * @param ctx
	 * @return the ConnectionServer
	 */
	public static ConnectionServer getServerInstance(Context ctx) {
		return ConnectionServer.getInstance(ctx);
	}

	/**
	 * Get the type of connection that is currently in use
	 * 
	 * @return the type of connection in use
	 */
	public static ConnectionType getConnectionType(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREFERENCES, Context.MODE_WORLD_READABLE);
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
		SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREFERENCES, Context.MODE_WORLD_READABLE);
		String connectionType = prefs.getString(Constants.CONNECTION_TYPE, Constants.WIFI);

		if (Constants.WIFI.equals(connectionType)) {
			return new WifiServerSocket();
		} else if (Constants.BLUETOOTH.equals(connectionType)) {
			return new BluetoothServerSocket();
		}

		return new WifiServerSocket();
	}

	/**
	 * Create a new IConnectionService based on the current game type
	 * 
	 * @return a new IConnectionService
	 */
	public static ConnectionService getNewConnectionService(Context ctx, Handler handler) {
		SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREFERENCES, Context.MODE_WORLD_READABLE);
		String connectionType = prefs.getString(Constants.CONNECTION_TYPE, Constants.WIFI);

		if (Constants.WIFI.equals(connectionType)) {
			return new WifiConnectionService(ctx, handler);
		} else if (Constants.BLUETOOTH.equals(connectionType)) {
			return new BluetoothConnectionService(ctx, handler);
		}

		return new WifiConnectionService(ctx, handler);
	}
}
