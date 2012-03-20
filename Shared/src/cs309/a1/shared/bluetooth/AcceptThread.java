package cs309.a1.shared.bluetooth;

import java.io.IOException;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cs309.a1.shared.Util;

/**
 * This thread runs while listening for incoming connections. It behaves
 * like a server-side client. It runs until a connection is accepted
 * (or until cancelled).
 */
public class AcceptThread extends Thread {
	private static final String TAG = AcceptThread.class.getName();

	// The local server socket
	private final BluetoothServerSocket mmServerSocket;
	private final BluetoothAdapter mAdapter;
	private final List<BluetoothConnectionService> mConnections;
	private Context mContext;
	private Handler mHandler;
	private boolean continueChecking = true;

	public AcceptThread(Context ctx, BluetoothAdapter adapter, Handler handler, List<BluetoothConnectionService> services) {
		BluetoothServerSocket tmp = null;
		mAdapter = adapter;
		mConnections = services;
		mContext = ctx;
		mHandler = handler;

		// Create a new listening server socket
		try {
			tmp = mAdapter.listenUsingRfcommWithServiceRecord(BluetoothConstants.SOCKET_NAME, BluetoothConstants.MY_UUID);
		} catch (IOException e) {
			Log.e(TAG, "Socket listen() failed", e);
		}
		mmServerSocket = tmp;
	}

	@Override
	public void run() {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "Socket BEGIN mAcceptThread" + this);
		}

		setName("AcceptThread");

		while (continueChecking) {
			BluetoothConnectionService serv = new BluetoothConnectionService(mContext, mHandler);
			BluetoothSocket socket = null;

			// Listen to the server socket if we're not connected
			while (serv.getState() != BluetoothConstants.STATE_CONNECTED) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					socket = mmServerSocket.accept();
					Log.d(TAG, "mmServerSocket.accept() completed");
				} catch (IOException e) {
					Log.e(TAG, "Socket accept() failed", e);
					break;
				}

				// If a connection was accepted
				if (socket != null) {
					synchronized (AcceptThread.this) {
						switch (serv.getState()) {
						case BluetoothConstants.STATE_LISTEN:
						case BluetoothConstants.STATE_CONNECTING:
							// Situation normal. Start the connected thread.
							mConnections.add(serv);
							Log.d(TAG, "added connection to mConnections");
							serv.connected(socket, socket.getRemoteDevice());
							break;
						case BluetoothConstants.STATE_NONE:
						case BluetoothConstants.STATE_CONNECTED:
							// Either not ready or already connected. Terminate new
							// socket.
							try {
								socket.close();
							} catch (IOException e) {
								Log.e(TAG, "Could not close unwanted socket", e);
							}
							break;
						}
					}
				}
			}
		}

		try {
			mmServerSocket.close();
		} catch (IOException e) {
			Log.e(TAG, "Socket close() of server failed", e);
		}

		if (Util.isDebugBuild()) {
			Log.i(TAG, "END mAcceptThread");
		}
	}

	public void cancel() {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "Socket cancel " + this);
		}

		continueChecking = false;
	}
}
