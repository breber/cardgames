package com.worthwhilegames.cardgames.shared.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;

import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;
import com.worthwhilegames.cardgames.shared.connection.IServerSocket;
import com.worthwhilegames.cardgames.shared.connection.ISocket;

/**
 * The Bluetooth implementation of a ServerSocket
 */
public class BluetoothServerSocket implements IServerSocket {

	private android.bluetooth.BluetoothServerSocket mServerSocket;

	/**
	 * Create a new BluetoothServerSocket
	 */
	public BluetoothServerSocket() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		try {
			mServerSocket = adapter.listenUsingRfcommWithServiceRecord(ConnectionConstants.SOCKET_NAME, BluetoothConstants.MY_UUID);;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.IServerSocket#accept()
	 */
	@Override
	public ISocket accept() throws IOException {
		return new BluetoothSocket(mServerSocket.accept());
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.IServerSocket#close()
	 */
	@Override
	public void close() throws IOException {
		mServerSocket.close();
	}
}
