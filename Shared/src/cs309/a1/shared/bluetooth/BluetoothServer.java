package cs309.a1.shared.bluetooth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BluetoothServer extends BluetoothCommon {
	private static BluetoothServer instance = null;

	private HashMap<String, BluetoothConnectionService> services;

	private BluetoothServer() {
		services = new HashMap<String, BluetoothConnectionService>();
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

		for (BluetoothConnectionService service : services.values()) {
			if (service.getState() == BluetoothConnectionService.STATE_CONNECTED) {
				countConnected++;
			}
		}

		return countConnected;
	}

	/**
	 * Gets a list of Devices ids
	 * 
	 * @return a list of connected devices' ids
	 */
	public List<String> getConnectedDevices() {
		return new ArrayList<String>(services.keySet());
	}


	@Override
	public boolean write(Object obj, String ... address) {
		boolean retVal = true;

		if (address.length == 0) {
			for (BluetoothConnectionService service : services.values()) {
				if (!performWrite(service, obj)) {
					retVal = false;
				}
			}
		} else {
			for (String addr : address) {
				BluetoothConnectionService service = services.get(addr);

				if (service != null) {
					if (!performWrite(service, obj)) {
						retVal = false;
					}
				}
			}
		}

		return retVal;
	}

}
