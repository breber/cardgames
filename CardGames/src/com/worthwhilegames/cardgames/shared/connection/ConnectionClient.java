package com.worthwhilegames.cardgames.shared.connection;

/**
 * A Client connection. This will manage a connection of
 * some sort with the server. The user will be able to connect,
 * disconnect, send and receive messages through this interface.
 */
public interface ConnectionClient {

	/**
	 * Connect to the device given by the macAddress
	 * 
	 * This method only starts trying to connect. It would be beneficial
	 * to create a BroadcastReceiver that listens on the ConnectionConstants.STATE_CHANGE_INTENT
	 * in order to figure out whether the connection was actually made.
	 * 
	 * @param macAddress
	 */
	void connect(String macAddress);

	/**
	 * Terminate the connection with the server
	 */
	void disconnect();

	/**
	 * Write an object to the given addresses
	 * 
	 * @param messageType the type of message this is
	 * @param obj the object to write
	 * @param address the addresses to write it to
	 * @return whether the message was written or not
	 */
	boolean write(int messageType, Object obj, String ... address);
}
