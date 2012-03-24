package cs309.a1.shared.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cs309.a1.shared.Util;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothConnectionService {
	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = BluetoothConnectionService.class.getName();

	/**
	 * The BluetoothAdapter used to query Bluetooth information
	 */
	private final BluetoothAdapter mAdapter;

	/**
	 * The Handler to post messages to when something changes in this service
	 */
	private final Handler mHandler;

	/**
	 * The Thread that handles the process of starting a connection
	 */
	private ConnectThread mConnectThread;

	/**
	 * The Thread that handles the sending/receiving of data once
	 * a connection has been established
	 */
	private ConnectedThread mConnectedThread;

	/**
	 * The current state of this Bluetooth connection
	 * 
	 * See BluetoothConstants.STATE_* for possible values
	 */
	private int mState;

	/**
	 * The MAC address of the remote device this service is connected to
	 */
	private String deviceAddress;

	/**
	 * Constructor. Initializes information needed to create a Bluetooth connection
	 * 
	 * @param context  The UI Activity Context
	 * @param handler  A Handler to send messages back to the UI Activity
	 */
	public BluetoothConnectionService(Context context, Handler handler) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mState = BluetoothConstants.STATE_NONE;
		mHandler = handler;
	}

	/**
	 * Set the current state of the Bluetooth connection
	 * 
	 * @param state  An integer defining the current connection state
	 */
	private synchronized void setState(int state) {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "setState() " + mState + " -> " + state);
		}

		mState = state;

		// Send a message to the Handler letting them know the state has been updated
		Message msg = mHandler.obtainMessage(BluetoothConstants.STATE_MESSAGE, -1, -1);
		Bundle bundle = new Bundle();
		bundle.putInt(BluetoothConstants.KEY_STATE_MESSAGE, state);
		bundle.putString(BluetoothConstants.KEY_DEVICE_ID, deviceAddress);
		msg.setData(bundle);
		msg.sendToTarget();
	}

	/**
	 * Return the current connection state.
	 * 
	 * @return the current state
	 */
	public synchronized int getState() {
		return mState;
	}

	/**
	 * Start the Bluetooth service. Cancels any currently connected connections,
	 * and sets the state to STATE_LISTEN.
	 */
	public synchronized void start() {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "start");
		}

		// Cancel any thread attempting to make a connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		setState(BluetoothConstants.STATE_LISTEN);
	}

	/**
	 * Start the ConnectThread to initiate a connection to a remote device.
	 * 
	 * @param device  The BluetoothDevice to connect
	 */
	public synchronized void connect(BluetoothDevice device) {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "connect to: " + device);
		}

		// Cancel any thread attempting to make a connection
		if (mState == BluetoothConstants.STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to connect with the given device
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();

		setState(BluetoothConstants.STATE_CONNECTING);
	}

	/**
	 * Start the ConnectedThread to begin managing a Bluetooth connection.
	 * 
	 * Called once a Bluetooth connection has been established (either by ConnectThread
	 * or AcceptThread).
	 * 
	 * @param socket  The BluetoothSocket on which the connection was made
	 * @param device  The BluetoothDevice that has been connected
	 */
	public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "connected Socket");
		}

		// Cancel the thread that completed the connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to manage the connection and perform transmissions
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		// Store the remote device's address
		deviceAddress = device.getAddress();

		// Update our state
		setState(BluetoothConstants.STATE_CONNECTED);
	}

	/**
	 * Stop all connections, and set the state to STATE_NONE
	 */
	public synchronized void stop() {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "stop");
		}

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		setState(BluetoothConstants.STATE_NONE);
	}

	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 * 
	 * @param out The bytes to write
	 * @see ConnectedThread#write(byte[])
	 */
	public void write(byte[] out) {
		// Create temporary object
		ConnectedThread r;

		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (mState != BluetoothConstants.STATE_CONNECTED) {
				return;
			}

			r = mConnectedThread;
		}

		// Perform the write unsynchronized
		r.write(out);
	}

	/**
	 * This thread runs while attempting to make an outgoing connection
	 * with a device. It runs straight through; the connection either
	 * succeeds or fails.
	 */
	private class ConnectThread extends Thread {
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

			while (timesTried != -1 && timesTried < 3) {
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
			connected(mmSocket, mmDevice);
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

	/**
	 * This thread runs during a connection with a remote device.
	 * It handles all incoming and outgoing transmissions.
	 */
	private class ConnectedThread extends Thread {
		/**
		 * The BluetoothSocket that messages are sent/received on
		 */
		private final BluetoothSocket mmSocket;

		/**
		 * The InputStream to read messages from
		 */
		private final InputStream mmInStream;

		/**
		 * The OutputStream to write messages to
		 */
		private final OutputStream mmOutStream;

		/**
		 * Create a new ConnectedThread with the given socket
		 * 
		 * @param socket the socket to use for this thread
		 */
		public ConnectedThread(BluetoothSocket socket) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, "create ConnectedThread");
			}

			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the BluetoothSocket input and output streams
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			if (Util.isDebugBuild()) {
				Log.i(TAG, "BEGIN mConnectedThread");
			}

			byte[] buffer = new byte[1024];
			int bytes;

			// Keep listening to the InputStream while connected
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.read(buffer);

					// Send the obtained bytes to the UI Activity
					Message msg = mHandler.obtainMessage(BluetoothConstants.READ_MESSAGE, -1, -1, null);
					Bundle data = new Bundle();
					data.putString(BluetoothConstants.KEY_MESSAGE_RX, new String(buffer, 0, bytes));
					data.putString(BluetoothConstants.KEY_DEVICE_ID, deviceAddress);
					msg.setData(data);
					msg.sendToTarget();
				} catch (IOException e) {
					Log.e(TAG, "disconnected", e);

					// Start the service over to restart listening mode
					BluetoothConnectionService.this.start();
					break;
				}
			}
		}

		/**
		 * Write to the connected OutStream
		 * 
		 * @param buffer The bytes to write
		 */
		public void write(byte[] buffer) {
			try {
				mmOutStream.write(buffer);

				// Share the sent message back to the UI Activity
				mHandler.obtainMessage(BluetoothConstants.WRITE_MESSAGE, -1, -1, buffer).sendToTarget();
			} catch (IOException e) {
				Log.e(TAG, "Exception during write", e);
			}
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
