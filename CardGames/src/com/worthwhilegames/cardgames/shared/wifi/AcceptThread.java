package com.worthwhilegames.cardgames.shared.wifi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.worthwhilegames.cardgames.shared.Game;
import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;

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
	 * A reference to the WifiServer so that we can check to see how many
	 * active connections there are
	 */
	private WifiServer mServer;

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
	 * @param maxConnections the maximum number of connections to open
	 */
	public AcceptThread(Context ctx, Handler handler, HashMap<String, WifiConnectionService> services, WifiServer server) {
		mConnections = services;
		mContext = ctx;
		mHandler = handler;
		mServer = server;

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
		// Try and get a reference to the game so that we can figure
		// out how many human players there were so that we allow up
		// to that many active connections
		Game g = null;
		try {
			g = GameFactory.getGameInstance(mContext);
		} catch (IllegalArgumentException ex) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, "Game hasn't been started yet - allow up to the default number of connections");
			}
		}
		setName("AcceptThread");

		if (Util.isDebugBuild()) {
			Log.d(TAG, "Socket BEGIN mAcceptThread" + this);
		}

		while (continueChecking) {
			Socket socket = null;
			WifiConnectionService serv = new WifiConnectionService(mContext, mHandler);
			serv.start();

			// Listen to the server socket if we're not connected
			try {
				// This is a blocking call and will only return on a
				// successful connection or an exception
				if (Util.isDebugBuild()) {
					Log.d(TAG, "mmServerSocket.accept() beginning");
				}

				socket = mmServerSocket.accept();

				if (Util.isDebugBuild()) {
					Log.d(TAG, "mmServerSocket.accept() completed " + socket.getInetAddress().getHostAddress());
				}
			} catch (IOException e) {
				Log.e(TAG, "Socket accept() failed", e);
				break;
			}

			// If a connection was accepted
			if (socket != null) {
				if (Util.isDebugBuild()) {
					Log.d(TAG, "the socket is not null! " + socket);
					Log.d(TAG, "game: " + g);
					if (g != null) {
						Log.d(TAG, "CurrentNumPlayers " + g.getNumPlayers());
						Log.d(TAG, "MaxNumPlayers " + g.getMaxNumPlayers());
					}
				}

				// Figure out how many active connections to allow
				// By default, we will allow as many as the game allows
				// If the game has been started, we will let it tell
				// us how many human players there were
				int numPlayersToAllow = GameFactory.getMaxAllowedPlayers(mContext);
				if (g != null) {
					numPlayersToAllow = g.getMaxNumPlayers();
				}

				// If we already have enough players, drop this connection
				if (mServer.getConnectedDeviceCount() == numPlayersToAllow) {
					if (Util.isDebugBuild()) {
						Log.d(TAG, "connecting: too many players, dropping new player");
					}

					try {
						socket.close();
					} catch (IOException e) {
						Log.e(TAG, "Socket close() of extra client failed", e);
						e.printStackTrace();
					}
				} else {
					synchronized (AcceptThread.this) {
						switch (serv.getState()) {
						case ConnectionConstants.STATE_LISTEN:
						case ConnectionConstants.STATE_CONNECTING:
							// Situation normal. Start the connected thread.
							InetAddress dev = socket.getInetAddress();

							if (Util.isDebugBuild()) {
								Log.d(TAG, "connecting: " + dev.getHostAddress());
							}

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
					} // end synchronized
				} // end else
			} // end socket != null

			if (Util.isDebugBuild()) {
				Log.d(TAG, "connecting: post socket != null : " + (continueChecking && serv.getState() != ConnectionConstants.STATE_CONNECTED));
			}
		} // end while...

		// Close the server socket
		try {
			if (Util.isDebugBuild()) {
				Log.d(TAG, "connecting: closing the server socket");
			}

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
