package cs309.a1.shared.connection;

import android.content.Context;
import cs309.a1.shared.bluetooth.BluetoothClient;
import cs309.a1.shared.bluetooth.BluetoothServer;

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
		if (getConnectionType() == ConnectionType.BLUETOOTH) {
			return BluetoothClient.getInstance(ctx);
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
		if (getConnectionType() == ConnectionType.BLUETOOTH) {
			return BluetoothServer.getInstance(ctx);
		}

		return null;
	}

	/**
	 * Get the type of connection that is currently in use
	 * 
	 * @return the type of connection in use
	 */
	public static ConnectionType getConnectionType() {
		return ConnectionType.BLUETOOTH;
	}

}
