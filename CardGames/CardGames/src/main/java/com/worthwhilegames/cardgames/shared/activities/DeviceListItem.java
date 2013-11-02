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
     * The port number to use
     */
    private int portNumber;

    /**
     * Create a DeviceListItem with the given name and MAC address
     * 
     * @param deviceName the name to display in the list
     * @param deviceMacAddress the address to use to connect to
     * @param portNumber the port number to use
     */
    public DeviceListItem(String deviceName, String deviceMacAddress, int portNumber) {
        this.deviceName = deviceName;
        this.deviceMacAddress = deviceMacAddress;
        this.portNumber = portNumber;
    }

    /**
     * @return the deviceMacAddress
     */
    public String getDeviceMacAddress() {
        return deviceMacAddress;
    }

    /**
     * @return the portNumber
     */
    public int getPortNumber() {
        return portNumber;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return deviceName;
    }
}
