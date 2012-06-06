package com.worthwhilegames.cardgames.shared.connection;

import java.io.IOException;

/**
 * Abstract out a regular Socket.
 * 
 * The purpose of this is to allow the connection logic to be shared across
 * connection types.
 */
public interface ISocket {

	/**
	 * Close the socket
	 * 
	 * @throws IOException
	 */
	void close() throws IOException;

}
