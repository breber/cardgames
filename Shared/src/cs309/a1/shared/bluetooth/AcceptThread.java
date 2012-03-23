package cs309.a1.shared.bluetooth;

import java.io.IOException;
import java.util.HashMap;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cs309.a1.shared.Util;

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
	 * Create a new AcceptThread
	 * 
	 * @param ctx The context of this thread
	 * @param adapter The BluetoothAdapter to use for Bluetooth information
	 * @param handler The handler to assign each BluetoothConnectionService
	 * @param services A map of MAC addresses to BluetoothConnectionService
	 */
	public AcceptThread(Context ctx, BluetoothAdapter adapter, Handler handler, HashMap<String, BluetoothConnectionService> services) {
		mAdapter = adapter;
		mConnections = services;
		mContext = ctx;
		mHandler = handler;

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

		setName("AcceptThread");

		while (continueChecking) {
			BluetoothConnectionService serv = new BluetoothConnectionService(mContext, mHandler);
			BluetoothSocket socket = null;

			serv.start();

			// Listen to the server socket if we're not connected
			while (continueChecking && serv.getState() != BluetoothConstants.STATE_CONNECTED) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
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
						case BluetoothConstants.STATE_LISTEN:
						case BluetoothConstants.STATE_CONNECTING:
							// Situation normal. Start the connected thread.
							BluetoothDevice dev = socket.getRemoteDevice();
							mConnections.put(dev.getAddress(), serv);
							serv.connected(socket, dev);
							break;
						case BluetoothConstants.STATE_NONE:
						case BluetoothConstants.STATE_CONNECTED:
							// Either not ready or already connected. Terminate new socket.
							try {
								socket.close();
							} catch (IOException e) {
								Log.e(TAG, "Could not close unwanted socket", e);
							}
							break;
						}
					}
				} else {
					if (Util.isDebugBuild()) {
						Log.d(TAG, "the socket was null :( " + socket);
					}
				}
			}
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

		// Close the server socket
		try {
			mmServerSocket.close();
		} catch (IOException e) {
			Log.e(TAG, "Socket close() of server failed", e);
		}
	}
}
