package com.worthwhilegames.cardgames.shared.connection;

import java.io.IOException;

/**
 * Abstract out a ServerSocket.
 * 
 * The purpose of this is to allow the connection logic to be shared across
 * connection types.
 */
public interface IServerSocket {

	/**
	 * Perform any necessary setup
	 */
	void setup();

	/**
	 * Accept a connection
	 * 
	 * @return an ISocket that contains the actual Socket
	 * @throws IOException
	 */
	ISocket accept() throws IOException;

	/**
	 * Close the ServerSocket
	 * 
	 * @throws IOException
	 */
	void close() throws IOException;

}
