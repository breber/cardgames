package com.worthwhilegames.cardgames.shared.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

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

	/**
	 * Create a new BluetoothSocket to the given address
	 * 
	 * @param address the address to connect to
	 */
	public BluetoothSocket(String address) {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothDevice dev = adapter.getRemoteDevice(address);
		try {
			mSocket = dev.createRfcommSocketToServiceRecord(BluetoothConstants.MY_UUID);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Always cancel discovery because it will slow down a connection
		adapter.cancelDiscovery();
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#close()
	 */
	@Override
	public void close() throws IOException {
		mSocket.close();
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return mSocket.getInputStream();
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		return mSocket.getOutputStream();
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#shutdownOutput()
	 */
	@Override
	public void shutdownOutput() throws IOException {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#shutdownInput()
	 */
	@Override
	public void shutdownInput() throws IOException {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#connect()
	 */
	@Override
	public void connect() throws IOException {
		mSocket.connect();
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
