package com.worthwhilegames.cardgames.shared.bluetooth;

import java.io.IOException;

import com.worthwhilegames.cardgames.shared.connection.ISocket;

/**
 * The Bluetooth implementation of a Socket
 */
public class BluetoothSocket implements ISocket {

	private android.bluetooth.BluetoothSocket mSocket;

	/**
	 * Create a new BluetoothSocket that wraps the given BluetoothSocket
	 * 
	 * @param socket the BluetoothSocket to wrap
	 */
	public BluetoothSocket(android.bluetooth.BluetoothSocket socket) {
		mSocket = socket;
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#close()
	 */
	@Override
	public void close() throws IOException {
		mSocket.close();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return mSocket.getRemoteDevice().getAddress();
	}

	/**
	 * Get the BluetoothSocket this object is wrapping
	 * 
	 * @return the actual Socket we are wrapping
	 */
	public android.bluetooth.BluetoothSocket getSocket() {
		return mSocket;
	}

}
