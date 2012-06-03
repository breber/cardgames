package com.worthwhilegames.cardgames.shared.bluetooth;

import java.io.IOException;
import java.util.HashMap;

import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * This thread runs while listening for incoming connections. When it finds
 * a new device that wants to connect, it will create a new BluetoothConnectionService
 * for that device, and add it to the HashMap that was provided. It will run until
 * someone tells it to stop.
 */
public class AcceptThread extends Thread {
	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = AcceptThread.class.getName();

	/**
	 *  The local server socket
	 */
	private BluetoothServerSocket mmServerSocket;

	/**
	 * The BluetoothAdapter used to query Bluetooth information
	 */
	private final BluetoothAdapter mAdapter;

	/**
	 * The Handler that each BluetoothConnectionService will get
	 */
	private final Handler mHandler;

	/**
	 * A reference to a HashMap of MAC addresses to BluetoothConnectionServices
	 * that is passed in by the calling server.
	 */
	private final HashMap<String, BluetoothConnectionService> mConnections;

	/**
	 * The context of this thread
	 */
	private Context mContext;

	/**
	 * When this turns false, don't try to find another connection
	 */
	private boolean continueChecking = true;

	/**
	 * The max number of connections to accept
	 */
	private int maxConnections = 0;

	/**
	 * Create a new AcceptThread
	 * 
	 * @param ctx The context of this thread
	 * @param adapter The BluetoothAdapter to use for Bluetooth information
	 * @param handler The handler to assign each BluetoothConnectionService
	 * @param services A map of MAC addresses to BluetoothConnectionService
	 * @param maxConnections the maximum number of connections to accept
	 */
	public AcceptThread(Context ctx, BluetoothAdapter adapter, Handler handler, HashMap<String, BluetoothConnectionService> services, int maxConnections) {
		mAdapter = adapter;
		mConnections = services;
		mContext = ctx;
		mHandler = handler;
		this.maxConnections = maxConnections;

		// Create a new listening server socket
		try {
			mmServerSocket = mAdapter.listenUsingRfcommWithServiceRecord(BluetoothConstants.SOCKET_NAME, BluetoothConstants.MY_UUID);
		} catch (IOException e) {
			Log.e(TAG, "Socket listen() failed", e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "Socket BEGIN mAcceptThread" + this);
		}
		int i = 0;

		setName("AcceptThread");

		while (continueChecking && i < maxConnections && mmServerSocket != null) {
			BluetoothConnectionService serv = new BluetoothConnectionService(mContext, mHandler);
			BluetoothSocket socket = null;

			serv.start();

			// Listen to the server socket if we're not connected
			while (continueChecking && serv.getState() != ConnectionConstants.STATE_CONNECTED && i < maxConnections) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					Log.d(TAG, "mmServerSocket.accept() beginning - " + i);
					socket = mmServerSocket.accept();
					Log.d(TAG, "mmServerSocket.accept() completed " + socket);
				} catch (IOException e) {
					Log.e(TAG, "Socket accept() failed", e);
					break;
				}

				// If a connection was accepted
				if (socket != null) {
					if (Util.isDebugBuild()) {
						Log.d(TAG, "the socket is not null! " + socket);
					}

					synchronized (AcceptThread.this) {
						switch (serv.getState()) {
						case ConnectionConstants.STATE_LISTEN:
						case ConnectionConstants.STATE_CONNECTING:
							// Situation normal. Start the connected thread.
							BluetoothDevice dev = socket.getRemoteDevice();
							mConnections.put(dev.getAddress(), serv);
							serv.connected(socket, dev);

							i++;
							break;
						case ConnectionConstants.STATE_NONE:
						case ConnectionConstants.STATE_CONNECTED:
							// Either not ready or already connected. Terminate new socket.
							try {
								socket.close();
							} catch (IOException e) {
								Log.e(TAG, "Could not close unwanted socket", e);
							}
							break;
						}
					} // end synchronized
				} // end socket != null

				if (Util.isDebugBuild()) {
					Log.d(TAG, "connecting: post socket != null : " + i + " / " + maxConnections);
					Log.d(TAG, "connecting: post socket != null : " + (continueChecking && serv.getState() != ConnectionConstants.STATE_CONNECTED && i < maxConnections));
				}

			} // end while continue...

			if (Util.isDebugBuild()) {
				Log.d(TAG, "connecting: post while continue... : " + i + " / " + maxConnections);
				Log.d(TAG, "connecting: post while continue... : " + (continueChecking && i < maxConnections));
			}
		} // end while...

		if (Util.isDebugBuild()) {
			Log.d(TAG, "connecting: post while" + i + " / " + maxConnections);
			Log.d(TAG, "connecting: post while");
		}

		// Close the server socket
		try {
			mmServerSocket.close();
		} catch (IOException e) {
			Log.e(TAG, "Socket close() of server failed", e);
		}
	}

	/**
	 * Cancel the waiting for connections
	 */
	public void cancel() {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "Socket cancel " + this);
		}

		// Change the loop variable to prevent the loop from continuing
		continueChecking = false;

		if (mmServerSocket != null) {
			// Close the server socket
			try {
				mmServerSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "Socket close() of server failed", e);
			}
		}
	}
}