package com.worthwhilegames.cardgames.shared.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

	/**
	 * Get the input stream for this socket
	 * 
	 * @return the input stream for this socket
	 * @throws IOException
	 */
	InputStream getInputStream() throws IOException;

	/**
	 * Get the output stream for this socket
	 * 
	 * @return the output stream for this socket
	 * @throws IOException
	 */
	OutputStream getOutputStream() throws IOException;

	/**
	 * Stop all output for this socket
	 * 
	 * @throws IOException
	 */
	void shutdownOutput() throws IOException;

	/**
	 * Stop all input for this socket
	 * 
	 * @throws IOException
	 */
	void shutdownInput() throws IOException;

	/**
	 * Connect to the socket
	 * 
	 * @throws IOException
	 */
	void connect() throws IOException;
}
