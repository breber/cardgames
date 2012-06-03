package com.worthwhilegames.cardgames.shared.wifi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

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
	 * The max number of connections to accept
	 */
	private int maxConnections = 0;

	/**
	 * Create a new AcceptThread
	 * 
	 * @param ctx The context of this thread
	 * @param handler The handler to assign each WifiConnectionService
	 * @param services A map of MAC addresses to WifiConnectionService
	 * @param maxConnections the maximum number of connections to open
	 */
	public AcceptThread(Context ctx, Handler handler, HashMap<String, WifiConnectionService> services, int maxConnections) {
		mConnections = services;
		mContext = ctx;
		mHandler = handler;
		this.maxConnections = maxConnections;

		// Create a new listening server socket
		try {
			mmServerSocket = new ServerSocket(WifiConstants.PORT_NUMBER, 0, Util.getLocalIpAddress());
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

		while (continueChecking && i < maxConnections) {
			WifiConnectionService serv = new WifiConnectionService(mContext, mHandler);
			Socket socket = null;

			serv.start();

			// Listen to the server socket if we're not connected
			while (continueChecking && serv.getState() != ConnectionConstants.STATE_CONNECTED && i < maxConnections) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					Log.d(TAG, "mmServerSocket.accept() beginning - " + i);
					socket = mmServerSocket.accept();
					Log.d(TAG, "mmServerSocket.accept() completed " + socket.getInetAddress().getHostAddress());
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

							if (Util.isDebugBuild()) {
								Log.d(TAG, "connecting: " + i + " --> " + dev.getHostAddress());
							}

							mConnections.put(dev.getHostAddress(), serv);
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

		// Close the server socket
		try {
			mmServerSocket.close();
		} catch (IOException e) {
			Log.e(TAG, "Socket close() of server failed", e);
		}
	}
}
