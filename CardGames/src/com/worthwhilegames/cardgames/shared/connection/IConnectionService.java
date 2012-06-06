package com.worthwhilegames.cardgames.shared.connection;

/**
 * Abstract out the ConnectionService interface
 * 
 * The purpose of this is to allow the connection logic to be shared across
 * connection types.
 */
public interface IConnectionService {

	/**
	 * Get the state of the current connection
	 * 
	 * @return the current state
	 */
	int getState();

	/**
	 * Start a connection service
	 */
	void start();

	/**
	 * Connect to the device with the given address
	 * 
	 * @param device the address of the device to connect to
	 */
	void connect(final String device);

	/**
	 * Stop the connection service
	 */
	void stop();

	/**
	 * Write to the connected device
	 * 
	 * @param out the data to write
	 */
	void write(byte[] out);

	/**
	 * Logic that handles the connection once it has been made
	 * 
	 * @param socket the socket that corresponds to this service
	 */
	void connected(ISocket socket);
}
