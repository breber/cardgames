package com.worthwhilegames.cardgames.shared.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

import com.worthwhilegames.cardgames.shared.Util;
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
		if (mServerSocket != null) {
			return new BluetoothSocket(mServerSocket.accept());
		} else {
			// Sometimes we can get to the accept phase while still
			// having a null mServerSocket. We will try to create a new
			// one and use that to accept
			if (Util.isDebugBuild()) {
				Log.d(BluetoothConstants.TAG, "mServerSocket is null...trying to create new one...");
			}

			try {
				BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
				mServerSocket = adapter.listenUsingRfcommWithServiceRecord(ConnectionConstants.SOCKET_NAME, BluetoothConstants.MY_UUID);;
			} catch (IOException e) {
				throw new IllegalStateException("No Server Socket...");
			}

			if (Util.isDebugBuild()) {
				Log.d(BluetoothConstants.TAG, "mServerSocket was null...successfully created new one!");
			}

			return new BluetoothSocket(mServerSocket.accept());
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.IServerSocket#close()
	 */
	@Override
	public void close() throws IOException {
		// TODO: this causes an error in native code on my XOOM
		//		 disabling it for now...
		//		if (mServerSocket != null) {
		//			mServerSocket.close();
		//		}
	}
}
