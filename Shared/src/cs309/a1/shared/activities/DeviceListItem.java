package cs309.a1.shared.activities;

/**
 * A ListItem that will show a device's name and MAC address
 */
public class DeviceListItem extends ListItem {

	private String deviceName;
	private String deviceMacAddress;

	public DeviceListItem(String deviceName, String deviceMacAddress) {
		this.deviceName = deviceName;
		this.deviceMacAddress = deviceMacAddress;
	}

	@Override
	public ItemType getType() {
		return ItemType.NOT_SEPARATOR;
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
