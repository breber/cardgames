package cs309.a1.shared.bluetooth;

import java.util.ArrayList;

public class BluetoothServer extends BluetoothCommon {
	private static BluetoothServer instance = null;

	private ArrayList<BluetoothConnectionService> services;

	private BluetoothServer() {
		services = new ArrayList<BluetoothConnectionService>();
	}

	public static BluetoothServer getInstance() {
		if (instance == null) {
			instance = new BluetoothServer();
		}
		return instance;
	}

	/**
	 * Gets the number of devices listed as "connected"
	 * 
	 * @return the number of connected devices
	 */
	public int getConnectedDeviceCount() {
		if (services.size() == 0) {
			return 0;
		}

		int countConnected = 0;

		for (BluetoothConnectionService service : services) {
			if (service.getState() == BluetoothConnectionService.STATE_CONNECTED) {
				countConnected++;
			}
		}

		return countConnected;
	}


	@Override
	public boolean write(Object obj, String ... address) {
		boolean retVal = true;

		if (address.length == 0) {
			for (BluetoothConnectionService service : services) {
				if (!performWrite(service, obj)) {
					retVal = false;
				}
			}
		} else {

		}

		return retVal;
	}

}
