package cs309.a1.shared.bluetooth;

public class BluetoothServer {
	private static BluetoothServer instance = null;

	protected BluetoothServer() {
		// Exists only to defeat instantiation.
	}

	public static BluetoothServer getInstance() {
		if (instance == null) {
			instance = new BluetoothServer();
		}
		return instance;
	}
}
