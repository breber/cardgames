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
	// Debugging
	private static final String TAG = BluetoothConnectionService.class.getName();

	// Member fields
	private final BluetoothAdapter mAdapter;
	private final Handler mHandler;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private int mState;

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

		// Give the new state to the Handler so the UI Activity can update
		Message msg = mHandler.obtainMessage(BluetoothConstants.STATE_MESSAGE, -1, -1);
		Bundle bundle = new Bundle();
		bundle.putInt(BluetoothConstants.STATE_MESSAGE_KEY, state);
		bundle.putString(BluetoothConstants.DEVICE_ID_KEY, deviceAddress);
		msg.setData(bundle);
		msg.sendToTarget();
	}

	/**
	 * Return the current connection state.
	 */
	public synchronized int getState() {
		return mState;
	}

	/**
	 * Start the Bluetooth service. Specifically start AcceptThread to begin a
	 * session in listening (server) mode. Called by the Activity onResume()
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
	 * Start the ConnectedThread to begin managing a Bluetooth connection
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

		deviceAddress = device.getAddress();

		setState(BluetoothConstants.STATE_CONNECTED);
	}

	/**
	 * Stop all threads
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
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private void connectionFailed() {
		// TODO: should this send a state change to the UI?

		// Send a failure message back to the Activity
		Message msg = mHandler.obtainMessage(BluetoothConstants.CONNECTION_FAILED_MESSAGE);
		Bundle bundle = new Bundle();
		bundle.putString(BluetoothConstants.TOAST_MESSAGE_KEY, "Unable to connect device");
		bundle.putString(BluetoothConstants.DEVICE_ID_KEY, deviceAddress);
		msg.setData(bundle);
		mHandler.sendMessage(msg);

		// Start the service over to restart listening mode
		BluetoothConnectionService.this.start();
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private void connectionLost() {
		// TODO: should this send a state change to the UI?

		// Send a failure message back to the Activity
		Message msg = mHandler.obtainMessage(BluetoothConstants.CONNECTION_FAILED_MESSAGE);
		Bundle bundle = new Bundle();
		bundle.putString(BluetoothConstants.TOAST_MESSAGE_KEY, "Device connection lost");
		bundle.putString(BluetoothConstants.DEVICE_ID_KEY, deviceAddress);
		msg.setData(bundle);
		mHandler.sendMessage(msg);

		// Start the service over to restart listening mode
		BluetoothConnectionService.this.start();
	}

	/**
	 * This thread runs while attempting to make an outgoing connection
	 * with a device. It runs straight through; the connection either
	 * succeeds or fails.
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

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

		@Override
		public void run() {
			if (Util.isDebugBuild()) {
				Log.i(TAG, "BEGIN mConnectThread");
			}
			setName("ConnectThread");

			// Always cancel discovery because it will slow down a connection
			mAdapter.cancelDiscovery();

			// Make a connection to the BluetoothSocket
			try {
				// This is a blocking call and will only return on a
				// successful connection or an exception
				mmSocket.connect();
			} catch (IOException e) {
				Log.e(TAG, "IOException", e);
				// Close the socket
				try {
					mmSocket.close();
				} catch (IOException e2) {
					Log.e(TAG, "unable to close() socket during connection failure", e2);
				}

				connectionFailed();
				return;
			}

			// Reset the ConnectThread because we're done
			synchronized (BluetoothConnectionService.this) {
				mConnectThread = null;
			}

			// Start the connected thread
			connected(mmSocket, mmDevice);
		}

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
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

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

		@Override
		public void run() {
			if (Util.isDebugBuild()) {
				Log.i(TAG, "BEGIN mConnectedThread");
			}
			byte[] buffer = new byte[1024];

			// Keep listening to the InputStream while connected
			while (true) {
				try {
					// Read from the InputStream
					mmInStream.read(buffer);

					// Send the obtained bytes to the UI Activity
					Message msg = mHandler.obtainMessage(BluetoothConstants.READ_MESSAGE, -1, -1, null);
					Bundle data = new Bundle();
					data.putString(BluetoothConstants.MESSAGE_RX_KEY, new String(buffer));
					data.putString(BluetoothConstants.DEVICE_ID_KEY, deviceAddress);
					msg.setData(data);

					msg.sendToTarget();
				} catch (IOException e) {
					Log.e(TAG, "disconnected", e);
					connectionLost();

					// Start the service over to restart listening mode
					BluetoothConnectionService.this.start();
					break;
				}
			}
		}

		/**
		 * Write to the connected OutStream.
		 * @param buffer  The bytes to write
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

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}
}
