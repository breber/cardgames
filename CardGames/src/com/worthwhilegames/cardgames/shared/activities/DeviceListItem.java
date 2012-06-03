package com.worthwhilegames.cardgames.shared.activities;

/**
 * A ListItem that will show a device's name and MAC address
 */
public class DeviceListItem {

	/**
	 * The device's "pretty name"
	 */
	private String deviceName;

	/**
	 * The device's MAC address
	 */
	private String deviceMacAddress;

	/**
	 * Create a DeviceListItem with the given name and MAC address
	 * 
	 * @param deviceName the name to display in the list
	 * @param deviceMacAddress the address to use to connect to
	 */
	public DeviceListItem(String deviceName, String deviceMacAddress) {
		this.deviceName = deviceName;
		this.deviceMacAddress = deviceMacAddress;
	}

	/**
	 * @return the deviceMacAddress
	 */
	public String getDeviceMacAddress() {
		return deviceMacAddress;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return deviceName + "\n" + deviceMacAddress;
	}
}
