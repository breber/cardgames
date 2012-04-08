package cs309.a1.shared.connection;

import android.content.Context;
import cs309.a1.shared.bluetooth.BluetoothClient;
import cs309.a1.shared.bluetooth.BluetoothServer;

public class ConnectionUtil {

	public static ConnectionClient getClientInstance(Context ctx) {
		if (getConnectionType() == ConnectionType.BLUETOOTH) {
			return BluetoothClient.getInstance(ctx);
		}

		return null;
	}

	public static ConnectionServer getServerInstance(Context ctx) {
		if (getConnectionType() == ConnectionType.BLUETOOTH) {
			return BluetoothServer.getInstance(ctx);
		}

		return null;
	}

	public static ConnectionType getConnectionType() {
		return ConnectionType.BLUETOOTH;
	}

}
