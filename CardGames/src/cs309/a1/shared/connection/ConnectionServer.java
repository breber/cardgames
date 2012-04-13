package cs309.a1.shared.connection;

import java.util.List;

/**
 * A Server connection. This will manage connections of
 * some sort with the many clients. The user will be able to connect,
 * disconnect, send and receive messages through this interface.
 */
public interface ConnectionServer {

	/**
	 * Write an object to the given addresses
	 * 
	 * @param messageType the type of message this is
	 * @param obj the object to write
	 * @param address the addresses to write it to
	 * @return whether the message was written or not
	 */
	boolean write(int messageType, Object obj, String ... address);

	/**
	 * Start listening for Connections
	 */
	void startListening();

	/**
	 * Stop listening for Connections
	 */
	void stopListening();

	/**
	 * Gets the number of devices listed as "connected"
	 * 
	 * @return the number of connected devices
	 */
	int getConnectedDeviceCount();

	/**
	 * Gets a list of Devices ids
	 * 
	 * @return a list of connected devices' ids
	 */
	List<String> getConnectedDevices();

	/**
	 * Terminate ALL connections
	 */
	void disconnect();
}
