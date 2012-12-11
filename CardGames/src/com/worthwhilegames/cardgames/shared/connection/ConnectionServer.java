package com.worthwhilegames.cardgames.shared.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.worthwhilegames.cardgames.shared.Util;

/**
 * A Server connection. This will manage connections of
 * some sort with the many clients. The user will be able to connect,
 * disconnect, send and receive messages through this interface.
 */
public class ConnectionServer extends ConnectionCommon {
	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = ConnectionServer.class.getName();

	/**
	 * The Singleton instance of this class
	 */
	private static ConnectionServer instance = null;

	/**
	 * A map of MAC addresses to their corresponding BluetoothConnectionServices
	 */
	private HashMap<String, ConnectionService> services;

	/**
	 * The Thread that runs while listening for connections
	 */
	private AcceptThread mAcceptThread;

	/**
	 * The context of this thread
	 */
	private static Activity mContext;

	/**
	 * The Handler to handle all messages coming from the BluetoothConnectionService
	 * 
	 * This should pretty much just pass the message on to the UI/GameLayer
	 */
	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, "handleMessage: " + msg.what);
			}

			Bundle data = msg.getData();
			switch (msg.what) {
			case ConnectionConstants.STATE_MESSAGE:
				// When the state of the Bluetooth connection has changed
				// let all the listeners know that this has happened
				Intent i = new Intent(ConnectionConstants.STATE_CHANGE_INTENT);
				i.putExtra(ConnectionConstants.KEY_STATE_MESSAGE, data.getInt(ConnectionConstants.KEY_STATE_MESSAGE));
				i.putExtra(ConnectionConstants.KEY_DEVICE_ID, data.getString(ConnectionConstants.KEY_DEVICE_ID));
				mContext.sendBroadcast(i);
				break;
			case ConnectionConstants.READ_MESSAGE:
				Intent i1 = new Intent(ConnectionConstants.MESSAGE_RX_INTENT);
				i1.putExtra(ConnectionConstants.KEY_MESSAGE_TYPE, data.getInt(ConnectionConstants.KEY_MESSAGE_TYPE));
				i1.putExtra(ConnectionConstants.KEY_MESSAGE_RX, data.getString(ConnectionConstants.KEY_MESSAGE_RX));
				i1.putExtra(ConnectionConstants.KEY_DEVICE_ID, data.getString(ConnectionConstants.KEY_DEVICE_ID));
				mContext.sendBroadcast(i1);
				break;
			}
		}
	};

	/**
	 * Create a new ConnectionServer
	 * @param ctx
	 */
	protected ConnectionServer(Activity ctx) {
		mContext = ctx;
		services = new HashMap<String, ConnectionService>();
		mAcceptThread = new AcceptThread(mContext, mHandler, services, this);
	}

	/**
	 * Gets the instance of the BluetoothServer
	 * 
	 * @param ctx the Context of this BluetoothServer
	 * @return the BluetoothServer
	 */
	public static ConnectionServer getInstance(Activity ctx) {
		if (instance == null) {
			instance = new ConnectionServer(ctx);
		}

		return instance;
	}

	/**
	 * Write an object to the given addresses
	 * 
	 * @param messageType the type of message this is
	 * @param obj the object to write
	 * @param address the addresses to write it to
	 * @return whether the message was written or not
	 */
	public boolean write(int messageType, Object obj, String ... address) {
		boolean retVal = true;

		// If the caller didn't provide any addresses, send the message to all
		// connected devices
		if (address.length == 0) {
			for (ConnectionService service : services.values()) {
				if (!performWrite(service, messageType, obj)) {
					retVal = false;
				}
			}
		} else {
			// Otherwise send it out to the devices specified in address
			for (String addr : address) {
				ConnectionService service = services.get(addr);

				if (service != null) {
					if (!performWrite(service, messageType, obj)) {
						retVal = false;
					}
				}
			}
		}

		return retVal;
	}

	/**
	 * Start listening for Connections
	 */
	public void startListening() {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "mAcceptThread: " + mAcceptThread);
		}

		// Start the AcceptThread which listens for incoming connection requests
		if (mAcceptThread == null) {
			mAcceptThread = new AcceptThread(mContext, mHandler, services, this);
		}

		if (Util.isDebugBuild()) {
			Log.d(TAG, "mAcceptThread.isAlive(): " + mAcceptThread.isAlive());
		}

		if (!mAcceptThread.isAlive()) {
			mAcceptThread.start();
		}
	}

	/**
	 * Stop listening for Connections
	 */
	public void stopListening() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (ConnectionServer.this) {
					if (mAcceptThread != null) {
						mAcceptThread.cancel();
						mAcceptThread = null;
					}
				}
			}
		}).start();
	}

	/**
	 * Gets the number of devices listed as "connected"
	 * 
	 * @return the number of connected devices
	 */
	public int getConnectedDeviceCount() {
		return getConnectedDevices().size();
	}

	/**
	 * Gets a list of Devices ids
	 * 
	 * @return a list of connected devices' ids
	 */
	public List<String> getConnectedDevices() {
		if (services.size() == 0) {
			return new ArrayList<String>();
		}

		HashSet<String> toRet = new HashSet<String>();

		for (String s : services.keySet()) {
			ConnectionService service = services.get(s);

			if (service.getState() == ConnectionConstants.STATE_CONNECTED) {
				toRet.add(s);
			}
		}

		return new ArrayList<String>(toRet);
	}

	/**
	 * Terminate ALL connections
	 */
	public void disconnect() {
		// Disconnect connections
		for (ConnectionService serv : services.values()) {
			serv.stop();
		}

		// Clear out the list of BluetoothConnectionServices
		services.clear();
	}
}
