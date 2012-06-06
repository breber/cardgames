package com.worthwhilegames.cardgames.shared.connection;

/**
 * Abstract out the ConnectThread
 */
public interface IConnectThread {

	/**
	 * Cancel the connecting
	 */
	void cancel();

	/**
	 * Start the connecting
	 */
	void start();

}
