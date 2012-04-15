package cs309.a1.shared.connection;

import android.content.Context;
import cs309.a1.shared.bluetooth.BluetoothClient;
import cs309.a1.shared.bluetooth.BluetoothServer;
import cs309.a1.shared.wifi.WifiClient;
import cs309.a1.shared.wifi.WifiServer;

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
		ConnectionType type = getConnectionType();

		if (type == ConnectionType.BLUETOOTH) {
			return BluetoothClient.getInstance(ctx);
		} else if (type == ConnectionType.WIFI) {
			return WifiClient.getInstance(ctx);
		}

		return null;
	}

	/**
	 * Get an instance of a Server connection based on the current
	 * connection type.
	 * 
	 * @param ctx
	 * @return the ConnectionServer
	 */
	public static ConnectionServer getServerInstance(Context ctx) {
		ConnectionType type = getConnectionType();

		if (type == ConnectionType.BLUETOOTH) {
			return BluetoothServer.getInstance(ctx);
		} else if (type == ConnectionType.WIFI) {
			return WifiServer.getInstance(ctx);
		}

		return null;
	}

	/**
	 * Get the type of connection that is currently in use
	 * 
	 * @return the type of connection in use
	 */
	public static ConnectionType getConnectionType() {
		// TODO: get from shared preference
		return ConnectionType.WIFI;
	}

}
