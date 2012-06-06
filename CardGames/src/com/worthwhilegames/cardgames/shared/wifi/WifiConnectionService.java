package com.worthwhilegames.cardgames.shared.wifi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
public class WifiConnectionService extends ConnectionService {
	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = WifiConnectionService.class.getName();

	/**
	 * Constructor. Initializes information needed to create a Bluetooth connection
	 * 
	 * @param context  The UI Activity Context
	 * @param handler  A Handler to send messages back to the UI Activity
	 */
	public WifiConnectionService(Context context, Handler handler) {
		super(context, handler);
	}

	/**
	 * Start the ConnectThread to initiate a connection to a remote device.
	 * 
	 * @param device  The BluetoothDevice to connect
	 */
	@Override
	public synchronized void connect(final String device) {
		super.connect(device);

		new Thread(new Runnable() {
			@Override
			public void run() {
				// Start the thread to connect with the given device
				try {
					mConnectThread = new ConnectThread(InetAddress.getByName(device));
					mConnectThread.start();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}).start();
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
		private final Socket mmSocket;

		/**
		 * The BluetoothDevice this connection will be with
		 */
		private final InetAddress mmDevice;

		/**
		 * Create a new ConnectThread with the given device
		 * 
		 * @param device the device to try and connect to
		 */
		public ConnectThread(InetAddress device) {
			mmDevice = device;
			Socket tmp = null;

			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				tmp = new Socket(device, WifiConstants.PORT_NUMBER);
			} catch (Exception e) {
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

			// If the socket is null, restart the service to cause the state to change
			if (mmSocket == null) {
				// Start the service over to restart listening mode
				WifiConnectionService.this.start();
				return;
			}

			setName("ConnectThread-" + mmDevice.getAddress());

			// Reset the ConnectThread because we're done
			synchronized (WifiConnectionService.this) {
				mConnectThread = null;
			}

			// Start the connected thread
			connected(new WifiSocket(mmSocket));
		}

		/**
		 * Cancel the current operation
		 */
		@Override
		public void cancel() {
			if (mmSocket != null) {
				try {
					mmSocket.close();
				} catch (IOException e) {
					Log.e(TAG, "close() of connect socket failed", e);
				}
			}
		}
	}
}
