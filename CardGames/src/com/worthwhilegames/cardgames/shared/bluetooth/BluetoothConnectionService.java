package com.worthwhilegames.cardgames.shared.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionService;
import com.worthwhilegames.cardgames.shared.connection.IConnectThread;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothConnectionService extends ConnectionService {
	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = BluetoothConnectionService.class.getName();

	/**
	 * The BluetoothAdapter used to query Bluetooth information
	 */
	private final BluetoothAdapter mAdapter;

	/**
	 * Constructor. Initializes information needed to create a Bluetooth connection
	 * 
	 * @param context  The UI Activity Context
	 * @param handler  A Handler to send messages back to the UI Activity
	 */
	public BluetoothConnectionService(Context context, Handler handler) {
		super(context, handler);
		mAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	/**
	 * Start the ConnectThread to initiate a connection to a remote device.
	 * 
	 * @param name  The BluetoothDevice to connect
	 */
	@Override
	public synchronized void connect(String name) {
		super.connect(name);

		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(name);

		// Start the thread to connect with the given device
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
	}


	/**
	 * This thread runs while attempting to make an outgoing connection
	 * with a device. It runs straight through; the connection either
	 * succeeds or fails.
	 */
	private class ConnectThread extends Thread implements IConnectThread {
		/**
		 * The BluetoothSocket this connection will be opened on
		 */
		private final BluetoothSocket mmSocket;

		/**
		 * The BluetoothDevice this connection will be with
		 */
		private final BluetoothDevice mmDevice;

		/**
		 * Create a new ConnectThread with the given device
		 * 
		 * @param device the device to try and connect to
		 */
		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;

			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				tmp = device.createRfcommSocketToServiceRecord(BluetoothConstants.MY_UUID);
			} catch (IOException e) {
				Log.e(TAG, "Socket create() failed", e);
			}
			mmSocket = tmp;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			if (Util.isDebugBuild()) {
				Log.i(TAG, "BEGIN mConnectThread");
			}
			int timesTried = 0;

			setName("ConnectThread-" + mmDevice.getAddress());

			// Always cancel discovery because it will slow down a connection
			mAdapter.cancelDiscovery();

			while (timesTried != -1 && timesTried < 5) {
				// Make a connection to the BluetoothSocket
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					mmSocket.connect();

					// Set timesTried to -1 indicating we were successful
					timesTried = -1;
				} catch (IOException e) {
					Log.e(TAG, "IOException", e);
					// Close the socket
					try {
						mmSocket.close();
					} catch (IOException e2) {
						Log.e(TAG, "unable to close() socket during connection failure", e2);
					}

					// Try a few times to connect
					Log.w(TAG, "Unsuccessful attempt to connect: " + timesTried);
					timesTried++;
				}
			}

			// We failed connecting too many times
			if (timesTried != -1) {
				Log.w(TAG, "Connection initiation failed. Restarting service");

				// Restart this service, therefore updating the state and letting
				// the UI know that the connection failed
				BluetoothConnectionService.this.start();

				return;
			}

			// Reset the ConnectThread because we're done
			synchronized (BluetoothConnectionService.this) {
				mConnectThread = null;
			}

			// Start the connected thread
			connected(new com.worthwhilegames.cardgames.shared.bluetooth.BluetoothSocket(mmSocket));
		}

		/**
		 * Cancel the current operation
		 */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}
}
