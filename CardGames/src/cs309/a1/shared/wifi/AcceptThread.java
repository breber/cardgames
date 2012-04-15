package cs309.a1.shared.wifi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cs309.a1.shared.Util;
import cs309.a1.shared.connection.ConnectionConstants;

/**
 * This thread runs while listening for incoming connections. When it finds
 * a new device that wants to connect, it will create a new WifiConnectionService
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
	private ServerSocket mmServerSocket;

	/**
	 * The Handler that each WifiConnectionService will get
	 */
	private final Handler mHandler;

	/**
	 * A reference to a HashMap of MAC addresses to WifiConnectionServices
	 * that is passed in by the calling server.
	 */
	private final HashMap<String, WifiConnectionService> mConnections;

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
	 * @param handler The handler to assign each WifiConnectionService
	 * @param services A map of MAC addresses to WifiConnectionService
	 */
	public AcceptThread(Context ctx, Handler handler, HashMap<String, WifiConnectionService> services) {
		mConnections = services;
		mContext = ctx;
		mHandler = handler;

		// Create a new listening server socket
		try {
			mmServerSocket = new ServerSocket(WifiConstants.PORT_NUMBER);
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
			WifiConnectionService serv = new WifiConnectionService(mContext, mHandler);
			Socket socket = null;

			serv.start();

			// Listen to the server socket if we're not connected
			while (continueChecking && serv.getState() != ConnectionConstants.STATE_CONNECTED) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					Log.d(TAG, "mmServerSocket.accept() beginning " + socket);
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
							InetAddress dev = socket.getInetAddress();
							mConnections.put(dev.getHostAddress(), serv);
							serv.connected(socket, dev);
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
